package com.example.optika.db

import com.example.optika.models.Client

interface DBService {
    fun addClient(client: Client)

    fun getAllClients() : ArrayList<Client>

    fun editClient(client: Client)

    fun deleteClient(client: Client)

}