package com.example.mystore.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "items_table")
data class Item(
    var itemName: String,
    var itemPrice: Int,
    var itemAvQuantity: Int,
    var supplier: String,
    var image: ByteArray
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}