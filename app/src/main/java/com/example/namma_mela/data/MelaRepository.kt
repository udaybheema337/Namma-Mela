package com.example.namma_mela.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MelaRepository(private val seatDao: SeatDao) {
    private val db = FirebaseFirestore.getInstance()

    // --- Seats (Firestore for real-time remote booking) ---
    val allSeats: Flow<List<Seat>> = callbackFlow {
        val listener = db.collection("seats").orderBy("seatNumber")
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let { trySend(it.toObjects(Seat::class.java)) }
            }
        awaitClose { listener.remove() }
    }

    fun toggleSeat(seat: Seat, userName: String = "") {
        val newStatus = !seat.isReserved
        val updates = hashMapOf<String, Any>(
            "reserved" to newStatus, // Matches @PropertyName in Models.kt
            "bookedBy" to if (newStatus) userName else "",
            "bookingTime" to if (newStatus) System.currentTimeMillis() else 0L
        )
        db.collection("seats").document(seat.seatNumber.toString())
            .set(updates, SetOptions.merge())
    }

    fun initializeSeats(count: Int) {
        db.collection("seats").get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                val batch = db.batch()
                val rows = listOf("A", "B", "C", "D", "E", "F", "G", "H")
                for (i in 0 until count) {
                    val rowIndex = i / 5
                    val colIndex = (i % 5) + 1
                    val label = "${rows.getOrElse(rowIndex) { "X" }}$colIndex"
                    val seatNum = i + 1
                    val seat = Seat(
                        seatNumber = seatNum,
                        rowLabel = label,
                        isReserved = false,
                        price = if (rowIndex < 2) 150 else 100
                    )
                    val docRef = db.collection("seats").document(seatNum.toString())
                    batch.set(docRef, seat)
                }
                batch.commit()
            }
        }
    }

    fun clearAllBookings() {
        db.collection("seats").get().addOnSuccessListener { snapshot ->
            val batch = db.batch()
            for (doc in snapshot.documents) {
                val updates = hashMapOf<String, Any>(
                    "reserved" to false, // Matches @PropertyName in Models.kt
                    "bookedBy" to "",
                    "bookingTime" to 0L
                )
                batch.update(doc.reference, updates)
            }
            batch.commit()
        }
    }

    // --- Play Details ---
    val playDetails: Flow<PlayDetails> = callbackFlow {
        val listener = db.collection("config").document("play_info")
            .addSnapshotListener { snapshot, _ ->
                snapshot?.toObject(PlayDetails::class.java)?.let { trySend(it) }
            }
        awaitClose { listener.remove() }
    }

    fun updatePlayDetails(details: PlayDetails) {
        db.collection("config").document("play_info").set(details)
    }

    // --- Fan Wall ---
    val allComments: Flow<List<Comment>> = callbackFlow {
        val listener = db.collection("comments").orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let { trySend(it.toObjects(Comment::class.java)) } }
        awaitClose { listener.remove() }
    }

    fun addComment(author: String, text: String) {
        val docRef = db.collection("comments").document()
        docRef.set(Comment(docRef.id, author, text, System.currentTimeMillis()))
    }
}
