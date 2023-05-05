package com.example.booky.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.booky.MainActivity
import com.example.booky.R
import com.example.booky.ui.view.LoginActivity


class SettingsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val name = view.findViewById<TextView>(R.id.profileUsernameINPT)
        val email = view.findViewById<TextView>(R.id.profileEmailINPT)
        val logoutButton = view.findViewById<Button>(R.id.logout_btn)

        val sharedPreferences  = this.requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", null).toString()
        val userFirstName = sharedPreferences.getString("userFirstName", null).toString()
        val userLastName = sharedPreferences.getString("userLastName", null).toString()


        email.text=userEmail
        name.text= "$userFirstName $userLastName"

        logoutButton.setOnClickListener {
            sharedPreferences.edit().clear().apply();
            Toast.makeText(requireContext(), "Log !out!", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

        }





    }


}