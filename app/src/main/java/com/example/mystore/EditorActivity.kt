package com.example.mystore

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.content_edit.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class EditorActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "com.example.mystore.EXTRA_ID"
        const val EXTRA_NAME = "com.example.mystore.EXTRA_NAME"
        const val EXTRA_SUPPLIER = "com.example.mystore.EXTRA_SUPPLIER"
        const val EXTRA_PRICE = "com.example.mystore.EXTRA_PRICE"
        const val EXTRA_QUANTITY = "com.example.mystore.EXTRA_QUANTITY"
        const val EXTRA_IMAGE = "com.example.mystore.EXTRA_IMAGE"
        const val CAMERA_REQUEST_CODE = 200

        const val PREFERRED_WIDTH = 150
        const val PREFERRED_HEIGHT = 150
    }

    private lateinit var editTextItemName: EditText
    private lateinit var editTextItemSupplier: EditText
    private lateinit var editTextItemPrice: EditText
    private lateinit var editTextItemQuantity: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        setSupportActionBar(toolbar_edit)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editTextItemName = findViewById(R.id.editTextItemName)
        editTextItemSupplier = findViewById(R.id.editTextSupplier)
        editTextItemPrice = findViewById(R.id.editTextItemPrice)
        editTextItemQuantity = findViewById(R.id.editTextItemAvailable)

        val intent = getIntent()
        if (intent.hasExtra(EXTRA_ID)) {
            Log.d("NUR", "Has Extra")
            title = "Edit Item"
            editTextItemName.setText(intent.getStringExtra(EXTRA_NAME))
            editTextItemSupplier.setText(intent.getStringExtra(EXTRA_SUPPLIER))
            editTextItemPrice.setText(intent.getIntExtra(EXTRA_PRICE, 99).toString())
            editTextItemQuantity.setText(intent.getIntExtra(EXTRA_QUANTITY, 1).toString())
            imageViewAdd.setImageBitmap(byteToBitmap(intent.getByteArrayExtra(EXTRA_IMAGE)!!))
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

        intent.putExtra(EXTRA_NAME, editTextItemName.text.toString().trim())
        intent.putExtra(EXTRA_SUPPLIER, editTextItemSupplier.text.toString().trim())
        intent.putExtra(EXTRA_PRICE, Integer.valueOf(editTextItemPrice.text.toString().trim()))
        intent.putExtra(
            EXTRA_QUANTITY,
            Integer.valueOf(editTextItemQuantity.text.toString().trim())
        )
        intent.putExtra(EXTRA_IMAGE, imgByteArray)
        if (getIntent().getIntExtra(EXTRA_ID, -1) != -1) {
            intent.putExtra(EXTRA_ID, getIntent().getIntExtra(EXTRA_ID, -1))
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
        image.compress(Bitmap.CompressFormat.JPEG, 30, stream)
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
