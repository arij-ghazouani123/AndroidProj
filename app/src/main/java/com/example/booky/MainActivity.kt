package com.example.booky

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.booky.databinding.ActivityComfirmAccountBinding
import com.example.booky.databinding.ActivityMainBinding
import com.example.booky.ui.fragments.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.miHome -> replaceFragment(HomeFragment())
                R.id.miAdd -> replaceFragment(AddFragment())
                R.id.miMessage -> replaceFragment(MessagesFragment())
                R.id.miSettings -> replaceFragment(SettingsFragment())
                else ->{

            }


        }
            true
        }

    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}