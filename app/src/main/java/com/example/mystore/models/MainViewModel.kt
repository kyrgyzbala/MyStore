package com.example.mystore.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mystore.data.Item

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private var repo: Repository = Repository(app)
    private var allItems: LiveData<List<Item>> = repo.getAllItems()

    fun getAllItems(): LiveData<List<Item>> {
        return allItems
    }
    fun insert(item: Item)
    {
        repo.insert(item)
    }
    fun update(item: Item)
    {
        repo.update(item)
    }
    fun delete(item: Item)
    {
        repo.delete(item)
    }
    fun deleteAllItems()
    {
        repo.deleteAllItems()
    }
}