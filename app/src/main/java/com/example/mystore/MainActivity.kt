package com.example.mystore

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore.adapter.ItemsAdapter
import com.example.mystore.data.Item
import com.example.mystore.models.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import com.example.mystore.EditorActivity.Companion as EditorActivity1

class MainActivity : AppCompatActivity(), ItemsAdapter.OnItemClickListenerN {

    companion object {
        const val REQUEST_ADD = 1
        const val REQUEST_EDIT = 2

        lateinit var imgByteArray: ByteArray
        const val DELETE_ALL_CODE = 22
        const val DELETE_ITEM_CODE = 23
    }

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Log.d("NUR", "OnCreared")
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val adapter = ItemsAdapter(this)
        recyclerView.adapter = adapter

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.getAllItems().observe(this, Observer {
            adapter.submitList(it)
        })

        fab.setOnClickListener {
            val intent = Intent(this, EditorActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD)
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val dialogBuilder = AlertDialog.Builder(this@MainActivity)
                dialogBuilder.setMessage("Are you sure to delete this item?")
                    .setCancelable(false)
                    .setPositiveButton("DELETE") { _, _ ->
                        mainViewModel.delete(adapter.getItemAt(viewHolder.adapterPosition))
                    }
                dialogBuilder.setNegativeButton("CANCEL") { _, _ ->
                    Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                }
                val alert = dialogBuilder.create()
                alert.setTitle("CAUTION:")
                alert.show()
            }

        }).attachToRecyclerView(recyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "New item has been added", Toast.LENGTH_SHORT).show()
        } else if (requestCode == REQUEST_EDIT && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Item has been Updated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_add_default -> {
                val bitmap: Bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.nur)
                imgByteArray = bitmapToByte(bitmap)
                val newItem = Item("defName", 99, 1, "defSupplier", imgByteArray)
                mainViewModel.insert(newItem)
                true
            }
            R.id.action_delete_all -> {
                val dialogBuilder = AlertDialog.Builder(this@MainActivity)
                dialogBuilder.setMessage("Are you sure to delete this item?")
                    .setCancelable(false)
                    .setPositiveButton("DELETE") { _, _ ->
                        mainViewModel.deleteAllItems()
                    }
                dialogBuilder.setNegativeButton("CANCEL") { _, _ ->
                    Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_SHORT).show()
                }
                val alert = dialogBuilder.create()
                alert.setTitle("CAUTION:")
                alert.show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onItemClick(position: Int) {
        Log.d("NUR", "Before start Activity")
        var intent = Intent(baseContext, EditorActivity::class.java)
        mainViewModel.getAllItems().observe(this, Observer {
            intent.apply {
                Log.d("NUR", "Obersver")
                putExtra(EditorActivity.EXTRA_ID, it[position].id)
                putExtra(EditorActivity.EXTRA_ITEM, it[position])
            }
            Log.d("NUR", "One more starts")
        })
        startActivityForResult(intent, REQUEST_EDIT)
    }

    private fun bitmapToByte(image: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 30, stream)
        return stream.toByteArray()
    }

    /*private fun bitmapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }*/

}
