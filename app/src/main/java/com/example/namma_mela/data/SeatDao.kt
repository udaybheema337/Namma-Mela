package com.example.namma_mela.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SeatDao {
    @Query("SELECT * FROM seats ORDER BY seatNumber ASC")
    fun getAllSeats(): Flow<List<Seat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(seats: List<Seat>)

    @Update
    suspend fun updateSeat(seat: Seat)

    @Query("SELECT COUNT(*) FROM seats")
    suspend fun getCount(): Int
}