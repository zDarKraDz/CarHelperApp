package com.example.carhelperapp

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carhelperapp.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth

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