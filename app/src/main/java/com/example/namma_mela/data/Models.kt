package com.example.namma_mela.data

import com.google.firebase.firestore.PropertyName
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seats")
data class Seat(
    @PrimaryKey 
    var seatNumber: Int = 0,
    var rowLabel: String = "",
    
    @get:PropertyName("reserved") @set:PropertyName("reserved")
    var reserved: Boolean = false,
    
    var bookedBy: String = "",
    var phone: String = "",
    var price: Int = 100,
    var bookingTime: Long = 0L
)

data class Comment(
    val id: String = "",
    val author: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)

data class PlayDetails(
    val title: String = "Tonight's Play",
    val duration: String = "120 mins",
    val posterUrl: String = "",
    val leadActor: String = "Lead Actor",
    val comedian: String = "Comedian",
    val singer: String = "Singer",
    val leadActorPhoto: String = "",
    val comedianPhoto: String = "",
    val singerPhoto: String = ""
)
