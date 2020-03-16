package com.example.mystore.data

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mystore.MainActivity

@Database(entities = [Item::class], version = 1)
abstract class ItemDataBase : RoomDatabase() {

    abstract fun itemDao(): ItemDAO

    companion object {
        private var instance: ItemDataBase? = null
        fun getInstance(context: Context): ItemDataBase? {
            if (instance == null) {
                synchronized(ItemDataBase::class)
                {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            ItemDataBase::class.java,
                            "item_database"
                        ).fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance).execute()
            }
        }

        private class PopulateDbAsyncTask(db: ItemDataBase?) : AsyncTask<Unit, Unit, Unit>() {
            private val itemDAO = db?.itemDao()
            override fun doInBackground(vararg params: Unit?) {
                //itemDAO?.insert(Item("Watch", 99, 23, "Michael Kors", MainActivity.imgStr))
            }
        }

    }
}