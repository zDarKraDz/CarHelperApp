package com.example.carhelperapp

import android.util.Log
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yandex.mapkit.geometry.Point
import java.util.Calendar

class Request {
    private val db = Firebase.firestore
    fun sendRequest(
        carModel: String,
        require: String,
        point: Point,
        userId: String,
        phoneNumber: String,
        description: String
    )
    {
        val requestData = hashMapOf(
            "coordinates" to GeoPoint(point.latitude, point.longitude),
            "timestamp" to Calendar.getInstance().time,
            "userId" to userId,
            "carModel" to carModel,
            "require" to require,
            "phoneNumber" to phoneNumber,
            "description" to description
        )

        db.collection("userRequests")
            .document(userId)
            .set(requestData)
            .addOnSuccessListener {
                Log.d("MapManager", "Location saved to FireStore")
            }
            .addOnFailureListener { e ->
                Log.e("MapManager", "Error saving location", e)
            }
    }
}