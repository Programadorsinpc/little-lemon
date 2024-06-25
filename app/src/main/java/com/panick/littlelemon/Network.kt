package com.panick.littlelemon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuNetworkData(
    @SerialName("menu") val menu: List<MenuItemNetwork>
)

@Serializable
data class MenuItemNetwork(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("price") val price: Double,
    @SerialName("image") val image: String
)
