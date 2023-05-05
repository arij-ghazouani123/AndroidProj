package com.example.booky.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.booky.R
import com.example.booky.data.api.RestApiService
import com.example.booky.data.api.RetrofitInstance
import com.example.booky.data.models.Book
import com.google.android.material.imageview.ShapeableImageView

class BookAdapter(private val bookList: MutableList<Book>): RecyclerView.Adapter<BookAdapter.BookViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_row,parent,false)
        return BookViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return bookList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentItem = bookList[position]

        holder.bookTitle.text = currentItem.title
        holder.bookDescription.text = currentItem.description
        holder.bookOffer.text = currentItem.offre
        var imagee= bookList[position].image
        if( imagee!=null && imagee.length>14){
            imagee = "img/"+ imagee.subSequence(14,imagee.length)
Log.d("ima",imagee)
            Log.d("ima",bookList[position].image)
        }


        Glide.with(holder.bookImage).load("http://10.0.2.2:9090/" +imagee).placeholder(R.drawable.user).circleCrop()
            .error(R.drawable.user).into(holder.bookImage)

    }


    class BookViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val bookImage: ShapeableImageView =itemView.findViewById(R.id.title_image)
        val bookTitle: TextView = itemView.findViewById(R.id.book_title)
        val bookDescription: TextView = itemView.findViewById(R.id.book_description)
        val bookOffer: TextView = itemView.findViewById(R.id.book_offer)

    }
}
