package com.raywenderlich.knockknock.board

import com.raywenderlich.knockknock.board.BoardManagerImpl.RingEvent
import io.reactivex.Observable

interface BoardManager {

  fun initialize()

  fun listenForRingEvents(): Observable<RingEvent>

  fun turnRedLedLightOn()

  fun turnRedLedLightOff()

  fun turnGreenLedLightOn()

  fun turnGreenLedLightOff()

  fun clear()
}