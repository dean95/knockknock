package com.raywenderlich.knockknock.data.repository

import com.google.firebase.database.DataSnapshot
import io.reactivex.Observable

interface RingRepository {

    fun listenForRingResponseEvents(): Observable<DataSnapshot>

    fun saveRingEvent()
}