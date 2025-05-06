package com.example.carhelperapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider

class MapFragment : Fragment() {
    private lateinit var mapView: MapView

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                initializeMap(true)
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                initializeMap(true)
            }
            else -> {
                initializeMap(false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.map_view)
        checkLocationPermissions()
    }

    private fun initializeMap(withLocation: Boolean) {
        // Получаем объект карты
        val map = mapView.mapWindow.map

        // Настройка жестов
        with(map) {
            isZoomGesturesEnabled = true
            isRotateGesturesEnabled = true
            isScrollGesturesEnabled = true
            isTiltGesturesEnabled = true
        }

        // Настройка слоя местоположения
        if (withLocation) {
            setupUserLocationLayer(map)
        } else {
            // Устанавливаем позицию по умолчанию (Москва)
            map.move(
                CameraPosition(
                    Point(55.751574, 37.573856),
                    11.0f, 0.0f, 0.0f
                ),
                Animation(Animation.Type.SMOOTH, 0.5f),
                null
            )
        }
    }

    private fun setupUserLocationLayer(map: com.yandex.mapkit.map.Map) {
        val mapKit = MapKitFactory.getInstance()
        val userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)

        // Настройка отображения
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(object : UserLocationObjectListener {
            override fun onObjectAdded(userLocationView: UserLocationView) {
                // Когда местоположение найдено, центрируем карту на нем
                userLocationLayer.cameraPosition()?.let { cameraPosition ->
                    map.move(
                        cameraPosition,
                        Animation(Animation.Type.SMOOTH, 1f),
                        null
                    )
                }

                // Настройка внешнего вида маркера
                userLocationView.pin.setIcon(ImageProvider.fromResource(requireContext(), R.drawable.search))
            }

            override fun onObjectRemoved(p0: UserLocationView) {}
            override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}
        })
    }

    private fun checkLocationPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                initializeMap(true)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // Показать объяснение, почему нужно разрешение
                requestLocationPermissions()
            }
            else -> {
                requestLocationPermissions()
            }
        }
    }

    private fun requestLocationPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

}