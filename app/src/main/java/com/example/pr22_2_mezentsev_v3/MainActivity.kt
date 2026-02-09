package com.example.pr22_2_mezentsev_v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var editTextIsbn: EditText
    private lateinit var buttonSearch: Button
    private lateinit var buttonViewSaved: Button
    private lateinit var repository: BookRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextIsbn = findViewById(R.id.editTitle)
        buttonSearch = findViewById(R.id.btnSave)

        repository = BookRepository(this)
        buttonViewSaved = findViewById(R.id.buttonViewSaved)

        buttonSearch.setOnClickListener {
            val isbn = editTextIsbn.text.toString().trim()
            if (isbn.isEmpty()) {
                Toast.makeText(this, "Введите номер книги", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Поиск книги...", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                try {
                    val book = repository.searchBookByIsbn(isbn)
                    if (book != null) {
                        val intent = Intent(this@MainActivity, BookDetailActivity::class.java)
                        intent.putExtra("BOOK", book)
                        startActivity(intent)
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Книга не найдена", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Ошибка поиска: ${e.message}", Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            }
        }

        buttonViewSaved.setOnClickListener {
            val intent = Intent(this, SavedBooksActivity::class.java)
            startActivity(intent)
        }
    }
}