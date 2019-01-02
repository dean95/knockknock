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

package com.raywenderlich.knockknock.data.database

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DatabaseWrapper {

  companion object {
    val databaseValue = MutableLiveData<DataSnapshot>()
    private const val RING_EVENT_CHILD = "ring_event"
    private const val RING_RESPONSE_CHILD = "ring_response"
  }

  private val databaseReference by lazy { FirebaseDatabase.getInstance().reference }

  private var counter = 0

  fun onDatabaseValuesChanged(): LiveData<DataSnapshot> {
    listenForDatabaseValueChanges()
    return databaseValue
  }

  fun saveRingEvent() {
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
              databaseValue.postValue(dataSnapshot)
            }
          }
        })
  }
}