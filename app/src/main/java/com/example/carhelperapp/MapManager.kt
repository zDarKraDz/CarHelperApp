package com.example.carhelperapp

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.location.*
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import java.util.*

class MapManager(private val context: Context, private val mapView: MapView) {
    private val mapKit = MapKitFactory.getInstance()
    private val locationManager = mapKit.createLocationManager()
    private var locationListener: LocationListener? = null
    private var lastKnownLocation: Point? = null
    private var userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
    //private val mapObjects: MapObjectCollection = mapView.map.mapObjects

    private val db = Firebase.firestore

    fun enableLocationTracking(enable: Boolean) {
        if (enable) {
            // Настройка отображения местоположения
            userLocationLayer.isVisible = true
            userLocationLayer.isHeadingEnabled = true
            userLocationLayer.setObjectListener(null)

            locationListener = object : LocationListener {
                // Обработка информации об обновлении местоположения
                override fun onLocationUpdated(location: Location) {
                    location.position.let { point ->
                        Log.d("MapManager", "New location: $point")
                        lastKnownLocation = point
                        //moveCameraTo(point)
                        //saveLocationToFirestore(point)
                    }
                }
                // Обработка изменения статуса определения местоположения
                override fun onLocationStatusUpdated(status: LocationStatus) {
                    when (status) {
                        LocationStatus.AVAILABLE -> Log.d("MapManager", "Location available")
                        LocationStatus.NOT_AVAILABLE -> Log.d("MapManager", "Location not available")
                        else -> Log.d("MapManager", "Unknown status: $status")
                    }
                }
            }

            val settings = SubscriptionSettings(
                UseInBackground.ALLOW,  // Разрешить использование в фоне
                Purpose.GENERAL           // Общее назначение
            )

            try {
                locationManager.subscribeForLocationUpdates(settings, locationListener!!)
            } catch (e: Exception) {
                Log.e("MapManager", "Location updates error", e)
            }
        } else {
            userLocationLayer.isVisible = false
            locationListener?.let { locationManager.unsubscribe(it) }
        }
    }

    fun saveLocationToFireStore(point: Point, userId: String) {
        if (userId.isEmpty()) {
            Log.w("MapManager", "User ID not set, cannot save location")
            return
        }

        val locationData = hashMapOf(
            "coordinates" to GeoPoint(point.latitude, point.longitude),
            "timestamp" to Calendar.getInstance().time,
            "userId" to userId
        )

        db.collection("userLocations")
            .document(userId)
            .set(locationData)
            .addOnSuccessListener {
                Log.d("MapManager", "Location saved to FireStore")
            }
            .addOnFailureListener { e ->
                Log.e("MapManager", "Error saving location", e)
            }
    }

    fun moveCameraTo(point: Point, zoom: Float) {
        mapView.map.move(CameraPosition(point, zoom, 0f, 0f))
    }

    fun dispose() {
        locationListener?.let { locationManager.unsubscribe(it) }
    }
    fun getCurrentLocation(): Point? {
        return lastKnownLocation
    }
}