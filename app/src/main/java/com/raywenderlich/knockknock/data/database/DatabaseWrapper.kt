package com.raywenderlich.knockknock.data.database

import com.google.firebase.database.DataSnapshot
import io.reactivex.Observable

interface DatabaseWrapper {

    fun onDatabaseValuesChanged(): Observable<DataSnapshot>

    fun saveRingEvent()
}