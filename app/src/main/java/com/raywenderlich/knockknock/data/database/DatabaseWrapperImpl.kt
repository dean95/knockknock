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
    private const val RING_EVENT_CHILD = "ring_event"
    private const val RING_RESPONSE_CHILD = "ring_response"
  }

  private val databaseReference by lazy { FirebaseDatabase.getInstance().reference }

  private var counter = 0

  override fun onDatabaseValuesChanged(): Observable<DataSnapshot> {
    listenForDatabaseValueChanges()
    return databaseValueChangeEvent
  }

  override fun saveRingEvent() {
    counter++
    val message = "Person $counter is ringing!"
    databaseReference
        .child(RING_EVENT_CHILD)
        .setValue(message)
  }

  private fun listenForDatabaseValueChanges() {
    databaseReference
        .child(RING_RESPONSE_CHILD)
        .addValueEventListener(object : ValueEventListener {
          override fun onCancelled(databaseError: DatabaseError) {
            /* No op */
          }

          override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
              databaseValueChangeEvent.onNext(dataSnapshot)
            }
          }
        })
  }
}