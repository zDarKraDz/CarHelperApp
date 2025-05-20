package com.example.carhelperapp

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.carhelperapp.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ktx.firestore


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth
    private val db = com.google.firebase.ktx.Firebase.firestore
    private var isAdmin: Boolean = false
    private val userId = Firebase.auth.currentUser?.uid ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.exit.setOnClickListener {
            exitApp()
        }

        binding.signOut.setOnClickListener {
            signOut()
            navigateToStartActivity()
        }

        if (isAdmin) {
            binding.mechanicView.visibility = View.VISIBLE
        } else {
            checkAdminStatus(userId = userId)
        }


    }

    private fun checkAdminStatus(userId: String) {
        val adminRef = db.collection("adminAccount").document("accounts")
        adminRef.get()
            .addOnSuccessListener { document ->
                if (!isAdded || _binding == null) return@addOnSuccessListener

                // Получаем список id админов
                val adminIds = document.get("id") as? List<String> ?: emptyList()

                // Проверяем является ли текующий пользователь админом
                if (adminIds.contains(userId)) {
                    binding.mechanicView.visibility = View.VISIBLE
                    Log.d("ProfileFragment", "User is admin")
                    isAdmin = true
                } else {
                    binding.mechanicView.visibility = View.GONE
                    Log.d("ProfileFragment", "User is NOT admin. Admin IDs: $adminIds, User ID: $userId")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("ProfileFragment", "get failed with ", exception)
            }
    }
    private fun exitApp() {
        requireActivity().finishAffinity()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().finishAndRemoveTask()
        }
    }

    private fun signOut() {
        auth.signOut()
    }

    private fun navigateToStartActivity() {
        val intent = Intent(requireActivity(), StartActivity::class.java)
        //FLAG_ACTIVITY_NEW_TASK - запускает активность в новой задаче (task)
        //FLAG_ACTIVITY_CLEAR_TASK - очищает всю предыдущую историю активностей (back stack)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}