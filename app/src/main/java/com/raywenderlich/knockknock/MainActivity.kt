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

package com.raywenderlich.knockknock

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.database.*
import com.raywenderlich.knockknock.board.BoardManager
import com.raywenderlich.knockknock.board.BoardManagerImpl
import com.raywenderlich.knockknock.board.BoardManagerImpl.RingEvent
import com.raywenderlich.knockknock.data.repository.RingRepository
import com.raywenderlich.knockknock.data.repository.RingRepositoryImpl
import io.reactivex.disposables.CompositeDisposable

class MainActivity : Activity() {

  companion object {
    private const val LIGHT_OFF_DELAY_MILLIS = 3000L
  }

  private val disposables = CompositeDisposable()

  private lateinit var ringRepository: RingRepository
  private lateinit var boardManager: BoardManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initialize()
  }

  override fun onDestroy() {
    super.onDestroy()
    boardManager.clear()
  }

  private fun initialize() {
    ringRepository = RingRepositoryImpl()
    boardManager = BoardManagerImpl()

    boardManager.initialize()

    disposables.addAll(
        boardManager
            .listenForRingEvents()
            .subscribe(this::onRingEvent),
        ringRepository
            .listenForRingResponseEvents()
            .subscribe(this::onRingResponseReceived)
    )
  }

  private fun onRingResponseReceived(dataSnapshot: DataSnapshot) {
    val unlockDoor = dataSnapshot.value as Boolean
    if (unlockDoor) boardManager.turnGreenLedLightOn() else boardManager.turnRedLedLightOn()
    turnLightsOffAfterDelay()
  }

  private fun onRingEvent(ringEvent: RingEvent) = ringRepository.saveRingEvent()

  private fun turnLightsOffAfterDelay() {
    Handler().postDelayed({
      boardManager.turnGreenLedLightOff()
      boardManager.turnRedLedLightOff()
    }, LIGHT_OFF_DELAY_MILLIS)
  }
}
