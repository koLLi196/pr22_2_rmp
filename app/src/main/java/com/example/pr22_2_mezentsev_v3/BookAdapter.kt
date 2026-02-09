package com.example.pr22_2_mezentsev_v3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private val onItemClick: (Book) -> Unit) :
    ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    class BookViewHolder(view: View, private val onItemClick: (Book) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val textViewTitle: TextView = view.findViewById(R.id.textViewItemTitle)
        private val textViewAuthor: TextView = view.findViewById(R.id.textViewItemAuthor)
        private var currentBook: Book? = null

        init {
            view.setOnClickListener {
                currentBook?.let { book ->
                    onItemClick(book)
                }
            }
        }

        fun bind(book: Book) {
            currentBook = book
            textViewTitle.text = book.title
            textViewAuthor.text = book.author
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.isbn == newItem.isbn
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}