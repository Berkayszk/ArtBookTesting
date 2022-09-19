package com.example.artbooktesting.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArtDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(art : Art)

    @Delete
    suspend fun delete(art: Art)

    @Query("SELECT * FROM arts")
    fun observeArts() : LiveData<List<Art>>
}