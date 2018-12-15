/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.knockknock.board

import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.io.IOException

class BoardManagerImpl : BoardManager {

  companion object {
    private const val BUTTON_PIN_NAME = "BCM21"
    private const val RED_LED_PIN_NAME = "BCM6"
    private const val GREEN_LED_PIN_NAME = "BCM19"
    private const val BLUE_LED_PIN_NAME = "BCM26"

    val ringEvent: BehaviorSubject<RingEvent> = BehaviorSubject.create<RingEvent>()
  }

  private val peripheralManager by lazy { PeripheralManager.getInstance() }

  private lateinit var buttonGpio: Gpio
  private lateinit var redLedGpio: Gpio
  private lateinit var greenLedGpio: Gpio
  private lateinit var blueLedGpio: Gpio
  private lateinit var buttonCallback: GpioCallback

  override fun initialize() {
    initializeLedLights()
    initializeButton()
  }

  override fun listenForRingEvents(): Observable<RingEvent> = ringEvent

  override fun turnRedLedLightOn() {
    redLedGpio.value = true
  }

  override fun turnRedLedLightOff() {
    redLedGpio.value = false
  }

  override fun turnGreenLedLightOn() {
    greenLedGpio.value = true
  }

  override fun turnGreenLedLightOff() {
    greenLedGpio.value = false
  }

  override fun clear() {
    buttonGpio.apply {
      unregisterGpioCallback(buttonCallback)
      try {
        close()
      } catch (exception: IOException) {
        handleError(exception)
      }
    }

    redLedGpio.apply {
      try {
        close()
      } catch (exception: IOException) {
        handleError(exception)
      }
    }

    greenLedGpio.apply {
      try {
        close()
      } catch (exception: IOException) {
        handleError(exception)
      }
    }

    blueLedGpio.apply {
      try {
        close()
      } catch (exception: IOException) {
        handleError(exception)
      }
    }
  }

  private fun initializeButton() {
    initializeButtonCallback()
    try {
      buttonGpio = peripheralManager.openGpio(BUTTON_PIN_NAME)
      buttonGpio.apply {
        setDirection(Gpio.DIRECTION_IN)
        setEdgeTriggerType(Gpio.EDGE_BOTH)
        setActiveType(Gpio.ACTIVE_LOW)
        registerGpioCallback(buttonCallback)
      }
    } catch (exception: IOException) {
      handleError(exception)
    }
  }

  private fun initializeButtonCallback() {
    buttonCallback = GpioCallback {
      try {
        val buttonValue = it.value
        blueLedGpio.value = buttonValue
        ringEvent.onNext(RingEvent)
      } catch (exception: IOException) {
        handleError(exception)
      }
      true
    }
  }

  private fun initializeLedLights() {
    try {
      redLedGpio = peripheralManager.openGpio(RED_LED_PIN_NAME)
      redLedGpio.apply {
        setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
      }

      greenLedGpio = peripheralManager.openGpio(GREEN_LED_PIN_NAME)
      greenLedGpio.apply {
        setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
      }

      blueLedGpio = peripheralManager.openGpio(BLUE_LED_PIN_NAME)
      blueLedGpio.apply {
        setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
      }
    } catch (exception: IOException) {
      handleError(exception)
    }
  }

  private fun handleError(throwable: Throwable) {
    /* Do nothing */
  }

  object RingEvent
}