package com.example.mystore.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDAO {

    @Insert
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)

    @Query("DELETE FROM items_table")
    fun deleteAllItems()

    @Query("SELECT * FROM items_table ORDER BY id DESC")
    fun getAllItems(): LiveData<List<Item>>
}