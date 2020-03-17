package com.example.mystore.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.mystore.R
import com.example.mystore.data.Item
import java.io.ByteArrayInputStream

class ItemsAdapter(private var listener: OnItemClickListenerN?) :
    androidx.recyclerview.widget.ListAdapter<Item, ItemsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemName == newItem.itemName && oldItem.itemPrice == newItem.itemPrice && oldItem.supplier == newItem.supplier
            }

        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewItemName: TextView = itemView.findViewById(R.id.textViewItemName)
        val textViewItemSupplier: TextView = itemView.findViewById(R.id.textViewItemSupplier)
        val textViewItemPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        val textViewItemQuantity: TextView = itemView.findViewById(R.id.textViewItemQuantity)

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


    fun getItemAt(position: Int): Item {
        return getItem(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.textViewItemName.text = currentItem.itemName
        holder.textViewItemSupplier.text = currentItem.supplier
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