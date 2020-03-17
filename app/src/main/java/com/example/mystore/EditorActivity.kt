package com.example.mystore

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProviders
import com.example.mystore.data.Item
import com.example.mystore.models.MainViewModel
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.content_edit.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class EditorActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "com.example.mystore.EXTRA_ID"
        const val CAMERA_REQUEST_CODE = 200
        const val EXTRA_ITEM = "com.example.mystore.EXTRA_IMAGE"
    }

    private lateinit var itemViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        setSupportActionBar(toolbar_edit)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        itemViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val intent = getIntent()
        if (intent.hasExtra(EXTRA_ID)) {
            Log.d("NUR", "Has Extra")
            title = "Edit Item"
            val item: Item = intent.getSerializableExtra(EXTRA_ITEM) as Item
            editTextItemName.setText(item.itemName)
            editTextItemSupplier.setText(item.supplier)
            editTextItemPrice.setText(item.itemPrice.toString())
            editTextItemQuantity.setText(item.itemAvQuantity.toString())
            imageViewAdd.setImageBitmap(byteToBitmap(item.image))
        } else {
            title = "Add Item"
        }

        buttonCamera.setOnClickListener {
            openCamera()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_save -> {
                saveItem()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveItem() {

        if (editTextItemName.text.toString().trim()
                .isBlank() || editTextItemSupplier.text.toString().trim()
                .isBlank() || editTextItemPrice.text.toString().trim()
                .isBlank() || editTextItemQuantity.text.toString().trim().isBlank()
        ) {
            Toast.makeText(this, "Please Fill all the blanks!!!", Toast.LENGTH_LONG).show()
            return
        }

        val bitmap: Bitmap = imageViewAdd.drawable.toBitmap(100, 100, null)
        val imgByteArray = bitmapToByte(bitmap)

        val intent = Intent()

        val itemName = editTextItemName.text.toString().trim()
        val itemSupplier = editTextItemSupplier.text.toString().trim()
        val itemQuantity = Integer.valueOf(editTextItemPrice.text.toString().trim())
        val itemPrice = Integer.valueOf(editTextItemQuantity.text.toString().trim())

        val newItem = Item(itemName, itemPrice, itemQuantity, itemSupplier, imgByteArray)
        if (getIntent().getIntExtra(EXTRA_ID, -1) != -1) {
            itemViewModel.update(newItem)
        } else {
            itemViewModel.insert(newItem)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun openCamera() {
        val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePicIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePicIntent, CAMERA_REQUEST_CODE)
        }
    }

    private fun bitmapToByte(image: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 25, stream)
        return stream.toByteArray()
    }

    private fun byteToBitmap(img: ByteArray): Bitmap {
        val imgStream = ByteArrayInputStream(img)
        return BitmapFactory.decodeStream(imgStream)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val extras: Bundle = data!!.extras!!
            val image = extras.get("data")
            imageViewAdd.setImageBitmap(image as Bitmap?)
        }
    }
}
