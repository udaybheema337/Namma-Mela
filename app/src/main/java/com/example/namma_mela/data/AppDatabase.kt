package com.example.namma_mela.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Seat::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun seatDao(): SeatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mela_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}