package com.example.booky.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.booky.R
import com.example.booky.data.models.Book
import com.google.android.material.imageview.ShapeableImageView

class BookAdapter(private val bookList: ArrayList<Book>): RecyclerView.Adapter<BookAdapter.BookViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_row,parent,false)
        return BookViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return bookList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentItem = bookList[position]
        holder.bookImage.setImageResource(currentItem.image)
        holder.bookTitle.text = currentItem.title
        holder.bookDescription.text = currentItem.description
        holder.bookOffer.text = currentItem.offre
    }


    class BookViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val bookImage: ShapeableImageView =itemView.findViewById(R.id.title_image)
        val bookTitle: TextView = itemView.findViewById(R.id.book_title)
        val bookDescription: TextView = itemView.findViewById(R.id.book_description)
        val bookOffer: TextView = itemView.findViewById(R.id.book_offer)

    }
}
