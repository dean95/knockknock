package com.raywenderlich.knockknock.data.database

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class DatabaseWrapperImpl : DatabaseWrapper {

    companion object {
        val databaseValueChangeEvent: BehaviorSubject<DataSnapshot> = BehaviorSubject.create<DataSnapshot>()
    }

    private val databaseReference by lazy { FirebaseDatabase.getInstance().reference }

    override fun onDatabaseValuesChanged(): Observable<DataSnapshot> {
        listenForDatabaseValueChanges()
        return databaseValueChangeEvent
    }

    override fun saveRingEvent() {
        databaseReference.child("test").setValue("Riiiiing!!")
    }

    private fun listenForDatabaseValueChanges() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                /* No op */
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                databaseValueChangeEvent.onNext(dataSnapshot)
            }
        })
    }
}