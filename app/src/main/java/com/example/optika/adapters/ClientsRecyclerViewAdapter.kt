package com.example.optika.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.optika.databinding.ItemClientBinding
import com.example.optika.models.Client

class ClientsRecyclerViewAdapter(private var listener: OnItemClickListener) :
    RecyclerView.Adapter<ClientsRecyclerViewAdapter.VH>() {

    private val differCallback = object : DiffUtil.ItemCallback<Client>() {
        override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class VH(private var binding: ItemClientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(client: Client?, position: Int) {
            binding.tvName.text = client?.name
            binding.tvPhone.text = client?.phone
            binding.tvDescProductName.text = client?.productName
            binding.tvDescDiopter.text = client?.diopter
            binding.tvDecsDateOfPurchase.text = client?.dateOfPurchase
            binding.tvDescDateOfExpiration.text = client?.dateOfExpiration
            binding.tvDecsNotes.text = client?.notes



            binding.expandableLayout.visibility =
                if (client!!.isExpanded) View.VISIBLE else View.GONE

            binding.root.setOnClickListener {
                listener.itemClicked(client, binding.expandableLayout)
            }

            binding.btnEdit.setOnClickListener {
                listener.editClient(client)
            }

            binding.btnDelete.setOnClickListener {
                listener.deleteItem(client, position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.onBind(differ.currentList[position], position)

    override fun getItemCount(): Int = differ.currentList.size


}

interface OnItemClickListener {

    fun itemClicked(client: Client, view: View)

    fun editClient(client: Client)

    fun deleteItem(client: Client, position: Int)

}