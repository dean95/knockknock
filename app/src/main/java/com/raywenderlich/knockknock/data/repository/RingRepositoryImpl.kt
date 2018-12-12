package com.raywenderlich.knockknock.data.repository

import com.raywenderlich.knockknock.data.database.DatabaseWrapperImpl

class RingRepositoryImpl : RingRepository {

  private val databaseWrapper by lazy { DatabaseWrapperImpl() }

  override fun listenForRingResponseEvents() = databaseWrapper.onDatabaseValuesChanged()

  override fun saveRingEvent() = databaseWrapper.saveRingEvent()
}