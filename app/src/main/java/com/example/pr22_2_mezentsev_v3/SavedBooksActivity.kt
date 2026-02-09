package com.example.pr22_2_mezentsev_v3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class SavedBooksActivity : AppCompatActivity() {
    private lateinit var repository: BookRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_books)

        repository = BookRepository(this)
        recyclerView = findViewById(R.id.recyclerViewBooks)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BookAdapter { book ->
            val intent = Intent(this, BookDetailActivity::class.java)
            intent.putExtra("BOOK", book)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        loadBooks()
    }

    private fun loadBooks() {
        lifecycleScope.launch {
            val books = repository.getAllBooks()
            adapter.submitList(books)
        }
    }

    override fun onResume() {
        super.onResume()
        loadBooks()
    }
}