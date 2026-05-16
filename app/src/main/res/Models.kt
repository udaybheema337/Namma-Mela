package com.example.namma_mela.data

data class Seat(
    val seatNumber: Int = 0,
    var isReserved: Boolean = false
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
    val posterUrl: String = ""
)

data class CastMember(
    val name: String = "",
    val role: String = "",
    val photoUrl: String = ""
)