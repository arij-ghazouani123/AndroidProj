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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booky.MainActivity
import com.example.booky.R
import com.example.booky.data.api.RestApiService
import com.example.booky.data.api.RetrofitInstance
import com.example.booky.data.models.Book
import com.example.booky.data.models.User
import com.example.booky.ui.adapter.BookAdapter
import com.example.booky.ui.view.LoginActivity
import com.example.booky.ui.view.MyBook
import com.example.booky.ui.view.PREF_NAME
import com.example.booky.ui.view.myuser
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SettingsFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val name = view.findViewById<TextView>(R.id.profileUsernameINPT)
        val email = view.findViewById<TextView>(R.id.profileEmailINPT)
        val logoutButton = view.findViewById<Button>(R.id.logout_btn)
        val Button = view.findViewById<Button>(R.id.logout_btn_mylist)


        val gson = Gson()

        val sharedPreferences  =this.requireActivity()?.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        val  us =  sharedPreferences?.getString(myuser, "")

        val nowuser = gson.fromJson(us, User::class.java)



        email.text=nowuser.email
        name.text= "${nowuser.firstName} ${nowuser.lastName}"

        logoutButton.setOnClickListener {

            sharedPreferences?.edit()?.clear()?.apply();
            Toast.makeText(requireContext(), "Log out!", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)


        }
        Button.setOnClickListener {


            val intent = Intent(requireContext(), MyBook::class.java)
            startActivity(intent)

        }





    }


}