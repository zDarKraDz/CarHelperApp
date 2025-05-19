package com.example.carhelperapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.carhelperapp.databinding.ActivityMainBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        if (savedInstanceState == null) {
            changeFragment(MapFragment())
        }

        binding.bottomNavigationView.menu.findItem(R.id.map).isChecked = true

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.map ->{
                    changeFragment(MapFragment())
                }
                R.id.profile ->{
                    changeFragment(ProfileFragment())
                }
                R.id.request ->{
                    changeFragment(RequestFragment())
                }
            }
            return@setOnItemSelectedListener true
        }

    }
    // Функция смены фрагментов экрана
    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        if (::mapView.isInitialized) {
            mapView.onStart()
        }
    }

    override fun onStop() {
        if (::mapView.isInitialized) {
            mapView.onStop()
        }
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }



}