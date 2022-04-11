package com.example.optika.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.optika.interfaces.NavigationController
import com.example.optika.activities.MainActivity
import com.example.optika.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var myActivity: MainActivity
    private lateinit var lockMode: NavigationController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        initValues()
        initToolbar()


        binding.toolbar.setNavigationOnClickListener {
            myActivity.onBackPressed()
        }

        return binding.root
    }

    private fun initValues() {
        myActivity = activity as MainActivity
        lockMode = activity as NavigationController
        lockMode.lockDrawer()
    }

    private fun initToolbar() {
        myActivity.setSupportActionBar(binding.toolbar)
        myActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.title = "Settings"



    }



    override fun onDestroyView() {
        super.onDestroyView()
        lockMode.unlockDrawer()
        _binding = null
    }

}




