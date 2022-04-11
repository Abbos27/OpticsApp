package com.example.optika.models

import java.io.Serializable

data class Client(
    var id: Int = 0,
    var name: String? = null,
    var phone: String? = null,
    var dateOfBirth: String? = null,
    var image: String? = null,
    var gender: Int? = 0,
    var productName: String? = null,
    var diopter: String? = null,
    var dateOfPurchase: String? = null,
    var dateOfExpiration: String? = null,
    var notes: String? = null,
    var isExpanded: Boolean = false,
) : Serializable
