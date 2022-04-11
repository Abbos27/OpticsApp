package com.example.optika.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.optika.R
import com.example.optika.activities.MainActivity
import com.example.optika.adapters.ClientsRecyclerViewAdapter
import com.example.optika.adapters.OnItemClickListener
import com.example.optika.databinding.FragmentHomeBinding
import com.example.optika.db.DBHelper
import com.example.optika.models.Client
import com.example.optika.objects.MyDropAnimation


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var myActivity: MainActivity
    private lateinit var myAdapter: ClientsRecyclerViewAdapter
    private lateinit var clientsList: ArrayList<Client>
    private lateinit var searchedList: ArrayList<Client>
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        initValues()
        initToolbar()
        loadClientsFromDB()

        myActivity.setHomeFragmentToolbar(binding.toolbar)

        binding.btnAddClient.setOnClickListener {
            moveToFragment(AddClientFragment())
        }

        myAdapter = ClientsRecyclerViewAdapter(object : OnItemClickListener {
            override fun itemClicked(client: Client, view: View) {
                client.isExpanded = !client.isExpanded
                if (client.isExpanded)
                    MyDropAnimation.expand(view)
                else
                    MyDropAnimation.collapse(view)
            }

            override fun editClient(client: Client) {
                val bundle = Bundle()
                bundle.putSerializable("editClient", client)
                val fragment = AddClientFragment()
                fragment.arguments = bundle
                moveToFragment(fragment)
            }

            override fun deleteItem(client: Client, position: Int) {
                openDeleteDialog(client, position)
            }
        })

        searchedList = clientsList
        myAdapter.differ.submitList(searchedList)
        binding.rvClients.adapter = myAdapter

        checkList()

        binding.etSearch.addTextChangedListener { editText ->
            searchedList = arrayListOf()
            val input = editText.toString().lowercase()

            clientsList.forEach { client ->
                if (client.name.toString().lowercase().contains(input) || client.phone.toString()
                        .contains(input)
                )
                    searchedList.add(client)
            }

            checkList()

            myAdapter.notifyDataSetChanged()
            myAdapter.differ.submitList(searchedList)

        }

        return binding.root
    }

    private fun checkList() {
        if (searchedList.isEmpty()) {
            binding.rvClients.visibility = View.GONE
            binding.tvSearch.visibility = View.VISIBLE

            binding.tvSearch.text = if (binding.etSearch.text.isEmpty())
                "List is Empty..."
            else
                "Nothing has been found..."
        } else {
            binding.rvClients.visibility = View.VISIBLE
            binding.tvSearch.visibility = View.GONE
        }
    }

    private fun initValues() {
        myActivity = activity as MainActivity
        dbHelper = DBHelper(binding.root.context)
    }

    private fun initToolbar() {
        myActivity.setSupportActionBar(binding.toolbar)
        myActivity.supportActionBar?.title = ""

    }

    private fun loadClientsFromDB() {
        clientsList = dbHelper.getAllClients()
    }

    private fun moveToFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.addToBackStack("HomeFragment()")
            ?.replace(R.id.main_container, fragment)
            ?.commit()
    }

    private fun openDeleteDialog(client: Client, position: Int) {
        val builder = AlertDialog.Builder(binding.root.context)

        builder.setTitle(client.name)
        builder.setMessage("Are you sure you want to delete this client from the list?")
        builder.setPositiveButton("Delete", DialogInterface.OnClickListener { dialog, i ->
            searchedList.remove(client)
            clientsList.remove(client)
            dbHelper.deleteClient(client)
            myAdapter.notifyItemRemoved(position)
            myAdapter.notifyItemRangeChanged(position, searchedList.size)
            checkList()
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, i ->
            dialog.dismiss()
        })
        val dialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notifications, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_notification -> moveToFragment(NotificationsFragment())
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
