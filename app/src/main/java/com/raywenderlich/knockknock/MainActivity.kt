package com.raywenderlich.knockknock

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager
import com.google.firebase.database.*
import com.raywenderlich.knockknock.data.repository.RingRepository
import com.raywenderlich.knockknock.data.repository.RingRepositoryImpl
import io.reactivex.disposables.CompositeDisposable
import java.io.IOException

class MainActivity : Activity() {

  companion object {
    private const val TAG = "MainActivity"
    private const val BUTTON_PIN_NAME = "BCM21"
    private const val RED_LED_PIN_NAME = "BCM6"
  }

  private val disposables = CompositeDisposable()

  private var buttonGpio: Gpio? = null
  private var ledGpio: Gpio? = null

  private lateinit var ringRepository: RingRepository

  private val gpioCallback = GpioCallback {
    try {
      val butttonValue = it.value
      ledGpio?.value = butttonValue
    } catch (exception: IOException) {
      handleError(exception)
    }
    true
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    initialize()

    val peripheralManager = PeripheralManager.getInstance()
    Log.d(TAG, "Available GPIO: ${peripheralManager.gpioList}")

    try {
      buttonGpio = peripheralManager.openGpio(BUTTON_PIN_NAME)
      buttonGpio?.apply {
        setDirection(Gpio.DIRECTION_IN)
        setEdgeTriggerType(Gpio.EDGE_BOTH)
        setActiveType(Gpio.ACTIVE_LOW)
        registerGpioCallback(gpioCallback)
      }

      ledGpio = peripheralManager.openGpio(RED_LED_PIN_NAME)
      ledGpio?.apply {
        setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
      }
    } catch (exception: IOException) {
      handleError(exception)
    }
  }

  private fun initialize() {
    ringRepository = RingRepositoryImpl()

    disposables.add(
        ringRepository.listenForRingResponseEvents()
            .subscribe { onRingResponseReceived(it) }
    )

    ringRepository.saveRingEvent()
  }

  private fun onRingResponseReceived(dataSnapshot: DataSnapshot) {
    Log.d(TAG, "DATA changed")
  }

  override fun onDestroy() {
    super.onDestroy()

    buttonGpio?.apply {
      unregisterGpioCallback(gpioCallback)
      try {
        close()
      } catch (exception: IOException) {
        handleError(exception)
      }
    }

    ledGpio?.apply {
      try {
        close()
      } catch (exception: IOException) {
        handleError(exception)
      }
    }
  }

  private fun handleError(throwable: Throwable) {
    //Do nothing
  }
}
