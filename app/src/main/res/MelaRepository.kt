package com.example.namma_mela.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MelaRepository {
    private val db = FirebaseFirestore.getInstance()

    // Real-time Flows
    val allSeats: Flow<List<Seat>> = callbackFlow {
        val listener = db.collection("seats").orderBy("seatNumber")
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let { trySend(it.toObjects(Seat::class.java)) }
            }
        awaitClose { listener.remove() }
    }

    val playDetails: Flow<PlayDetails> = callbackFlow {
        val listener = db.collection("config").document("play_info")
            .addSnapshotListener { snapshot, _ ->
                snapshot?.toObject(PlayDetails::class.java)?.let { trySend(it) }
            }
        awaitClose { listener.remove() }
    }

    val allComments: Flow<List<Comment>> = callbackFlow {
        val listener = db.collection("comments").orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let { trySend(it.toObjects(Comment::class.java)) }
            }
        awaitClose { listener.remove() }
    }

    // Actions
    fun updatePlayDetails(title: String, duration: String, posterUrl: String) {
        val details = PlayDetails(title, duration, posterUrl)
        db.collection("config").document("play_info").set(details)
    }

    fun toggleSeat(seat: Seat) {
        db.collection("seats").document(seat.seatNumber.toString())
            .update("isReserved", !seat.isReserved)
    }

    fun addComment(author: String, text: String) {
        val docRef = db.collection("comments").document()
        docRef.set(Comment(docRef.id, author, text, System.currentTimeMillis()))
    }

    fun initializeDatabase(seatCount: Int) {
        db.collection("seats").limit(1).get().addOnSuccessListener {
            if (it.isEmpty) {
                for (i in 1..seatCount) {
                    db.collection("seats").document(i.toString()).set(Seat(i, false))
                }
                // Mock Cast
                db.collection("cast").add(CastMember("Raja", "Lead Actor", ""))
                db.collection("cast").add(CastMember("Kari", "Comedian", ""))
                // Initial Play config
                updatePlayDetails("Kurukshetra", "150 mins", "")
            }
        }
    }
}
