package com.example.mystore.models

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.mystore.data.Item
import com.example.mystore.data.ItemDAO
import com.example.mystore.data.ItemDataBase

class Repository(app: Application) {
    private var itemDao: ItemDAO
    private var allItems: LiveData<List<Item>>

    init {
        val db: ItemDataBase = ItemDataBase.getInstance(app.applicationContext)!!
        itemDao = db.itemDao()
        allItems = itemDao.getAllItems()
    }

    fun getAllItems(): LiveData<List<Item>> {
        return allItems
    }

    fun insert(item: Item) {
        InsertItemAsyncTask(itemDao).execute(item)
    }

    fun update(item: Item) {
        UpdateItemAsyncTask(itemDao).execute(item)
    }

    fun delete(item: Item) {
        DeleteItemAsyncTask(itemDao).execute(item)
    }

    fun deleteAllItems() {
        DeleteAllItemsAsyncTask(itemDao).execute()
    }

    companion object {
        private class InsertItemAsyncTask(private val itemDAO: ItemDAO) :
            AsyncTask<Item, Unit, Unit>() {
            override fun doInBackground(vararg params: Item?) {
                itemDAO.insert(params[0]!!)
            }
        }

        private class UpdateItemAsyncTask(private val itemDAO: ItemDAO) :
            AsyncTask<Item, Unit, Unit>() {
            override fun doInBackground(vararg params: Item?) {
                itemDAO.update(params[0]!!)
            }
        }

        private class DeleteItemAsyncTask(private val itemDAO: ItemDAO) :
            AsyncTask<Item, Unit, Unit>() {
            override fun doInBackground(vararg params: Item?) {
                itemDAO.delete(params[0]!!)
            }
        }

        private class DeleteAllItemsAsyncTask(private val itemDAO: ItemDAO) :
            AsyncTask<Item, Unit, Unit>() {
            override fun doInBackground(vararg params: Item?) {
                itemDAO.deleteAllItems()
            }
        }
    }

}