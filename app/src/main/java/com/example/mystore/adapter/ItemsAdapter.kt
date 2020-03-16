package com.example.mystore.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore.EditorActivity
import com.example.mystore.R
import com.example.mystore.data.Item
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ItemsAdapter(private var listener: OnItemClickListenerN?) : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

    private var allItems: List<Item> = emptyList()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewItemName: TextView = itemView.findViewById<TextView>(R.id.textViewItemName)
        val textViewItemSuplier = itemView.findViewById<TextView>(R.id.textViewItemSupplier)
        val textViewItemPrice = itemView.findViewById<TextView>(R.id.textViewPrice)
        val textViewItemQuantity = itemView.findViewById<TextView>(R.id.textViewItemQuantity)

        val mLayout = itemView.findViewById<CardView>(R.id.list_items)
        init {
            mLayout.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener?.onItemClick(adapterPosition)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_items, parent, false)
        )
    }

    fun setItems(items: List<Item>) {
        allItems = items
        notifyDataSetChanged()
    }

    fun notifyItemCh(){
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return allItems.size
    }

    fun getItemAt(position: Int): Item {
        return allItems[position]
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = allItems[position]

        holder.textViewItemName.text = currentItem.itemName
        holder.textViewItemSuplier.text = currentItem.supplier
        holder.textViewItemPrice.text = currentItem.itemPrice.toString()
        holder.textViewItemQuantity.text = currentItem.itemAvQuantity.toString()

        holder.imageView.setImageBitmap(byteToBitmap(currentItem.image))
    }


    private fun byteToBitmap(img: ByteArray): Bitmap {
        val imgStream = ByteArrayInputStream(img)
        return BitmapFactory.decodeStream(imgStream)
    }
    interface OnItemClickListenerN {
        fun onItemClick(position: Int)
    }

}