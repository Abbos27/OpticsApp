package com.example.optika.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.optika.R
import com.example.optika.activities.MainActivity
import com.example.optika.databinding.FragmentAddClientBinding
import com.example.optika.db.DBHelper
import com.example.optika.interfaces.NavigationController
import com.example.optika.models.Client
import java.util.*

class AddClientFragment : Fragment() {
    private var _binding: FragmentAddClientBinding? = null
    private val binding get() = _binding!!

    private lateinit var myActivity: MainActivity
    private lateinit var dbHelper: DBHelper
    private lateinit var editClient: Client
    private lateinit var lockMode: NavigationController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddClientBinding.inflate(inflater, container, false)
        initValues()
        initToolbar()
        isEdit()


        binding.etDateOfBirth.setOnClickListener {
            showDatePicker(binding.etDateOfBirth)
        }

        binding.etDateOfPurchase.setOnClickListener {
            showDatePicker(binding.etDateOfPurchase)
        }

        return binding.root
    }


    private fun initValues() {
        myActivity = activity as MainActivity
        dbHelper = DBHelper(binding.root.context)
        lockMode = activity as NavigationController
        lockMode.lockDrawer()
    }

    private fun initToolbar() {
        setHasOptionsMenu(true)
        myActivity.setSupportActionBar(binding.toolbar)
        binding.toolbar.title = ""
        myActivity.supportActionBar?.setDisplayShowHomeEnabled(true)
        myActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            myActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, HomeFragment()).commit()
        }


    }

    private fun showDatePicker(textView: TextView) {
        val datePickerFragment = DatePickerFragment()
        val supportFragmentManager = requireActivity().supportFragmentManager

        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY",
            viewLifecycleOwner
        ) { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val date = bundle.getString("SELECTED_DATE")

                textView.text = date.toString()
            }
        }

        datePickerFragment.show(
            supportFragmentManager,
            "DatePickerFragment"
        )
    }

    private fun isEdit() {
        if (arguments != null) {
            setEditClientData()
            binding.toolbarTitle.text = "Editing"
        }
    }

    private fun setEditClientData() {
        editClient = requireArguments().getSerializable("editClient") as Client

        binding.apply {
            etClientName.setText(editClient.name)
            etPhone.setText(editClient.phone)
            ///
            if (editClient.gender == 1) rbFemale.isChecked = true
            etProductName.setText(editClient.productName)
            etDiopter.setText(editClient.diopter)
            ////
            etExpirationDate.setText(editClient.dateOfExpiration)
            etNotes.setText(editClient.notes)
        }

    }

    private fun checkCredentials() {
        val clientName = binding.etClientName.text.toString()
        val phone = binding.etPhone.text.toString()
        val dateOfBirth = binding.etDateOfBirth.text.toString()
        var gender = 0
        val productName = binding.etProductName.text.toString()
        val diopter = binding.etDiopter.text.toString()
        val dateOfPurchase = binding.etDateOfPurchase.text.toString()
        val productExpiration = binding.etExpirationDate.text.toString()
        var notes = ""

        if (clientName.isEmpty() || phone.isEmpty() || dateOfBirth.isEmpty() || productName.isEmpty() || diopter.isEmpty()
            || dateOfPurchase.isEmpty() || productExpiration.isEmpty()
        ) {
            if (clientName.isEmpty()) binding.etClientName.error = "Enter Name"
            if (phone.isEmpty()) binding.etPhone.error = "Enter phone"
            if (dateOfBirth.isEmpty()) binding.etDateOfBirth.error = "Enter date of birth"
            if (productName.isEmpty()) binding.etProductName.error = "Enter product name"
            if (diopter.isEmpty()) binding.etDiopter.error = "Enter Diopter"
            if (dateOfPurchase.isEmpty()) binding.etDateOfPurchase.error = "Enter date of purchase"
            if (productExpiration.isEmpty()) binding.etExpirationDate.error =
                "Enter expiration date"
        } else {
            if (binding.rbFemale.isChecked) gender = 1
            if (binding.etNotes.text.isNotEmpty()) notes = binding.etNotes.text.toString()

            val client = Client(
                name = clientName,
                phone = phone,
                dateOfBirth = dateOfBirth,
                gender = gender,
                productName = productName,
                diopter = diopter,
                dateOfPurchase = dateOfPurchase,
                dateOfExpiration = productExpiration,
                notes = notes
            )

            if (arguments != null) {
                client.id = editClient.id
                Log.d("1", client.toString())
                dbHelper.editClient(client)
                Toast.makeText(binding.root.context, "Edited", Toast.LENGTH_SHORT).show()

            } else {
                dbHelper.addClient(client)
                Toast.makeText(binding.root.context, "Added", Toast.LENGTH_SHORT).show()
            }
            myActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, HomeFragment()).commit()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_client, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_addClient -> checkCredentials()
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        lockMode.unlockDrawer()
    }


}

