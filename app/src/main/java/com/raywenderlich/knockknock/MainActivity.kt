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
