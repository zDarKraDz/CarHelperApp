package com.example.carhelperapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.carhelperapp.databinding.FragmentRequestBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class RequestFragment : Fragment() {
    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapManager: MapManager
    private lateinit var request: Request
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Инициализация MapManager и Request
        mapManager = MapManager(requireContext(), binding.mapView)
        request = Request()

        // Включаем геолокацию
        mapManager.enableLocationTracking(true)

        binding.requestButton.setOnClickListener{
            val userId = getUId()
            val point = mapManager.getCurrentLocation()
            val carModel = binding.carModel.text?.toString() ?: ""
            val require = binding.require.text?.toString() ?: ""
            val description = binding.descriptionText.text?.toString() ?: ""
            val phoneNumber = binding.phoneNumber.text?.toString() ?: ""

            // Отправляем запрос
            request.sendRequest(
                carModel = carModel,
                require = require,
                point = point!!,
                userId = userId,
                description = description,
                phoneNumber = phoneNumber
            )
            Toast.makeText(context, "Запрос отправлен", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUId(): String {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        return userId
    }


}