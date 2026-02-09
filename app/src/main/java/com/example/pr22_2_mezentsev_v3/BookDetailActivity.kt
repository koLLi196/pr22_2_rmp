package com.example.pr22_2_mezentsev_v3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class BookDetailActivity : AppCompatActivity() {
    private lateinit var repository: BookRepository
    private lateinit var currentBook: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        repository = BookRepository(this)
        currentBook = intent.getSerializableExtra("BOOK") as Book

        val textViewTitle = findViewById<TextView>(R.id.textViewTitle)
        val textViewAuthor = findViewById<TextView>(R.id.textViewAuthor)
        val textViewIsbn = findViewById<TextView>(R.id.textViewIsbn)
        val textViewDescription = findViewById<TextView>(R.id.textViewDescription)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonEdit = findViewById<Button>(R.id.buttonEdit)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)

        textViewTitle.text = currentBook.title
        textViewAuthor.text = "Автор: ${currentBook.author}"
        textViewIsbn.text = "ISBN: ${currentBook.isbn}"
        textViewDescription.text = currentBook.description ?: "Описание отсутствует"

        buttonSave.setOnClickListener {
            lifecycleScope.launch {
                try {
                    repository.insertBook(currentBook)
                    Toast.makeText(this@BookDetailActivity, "Книга сохранена", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@BookDetailActivity, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonEdit.setOnClickListener {
            val intent = Intent(this, EditBookActivity::class.java)
            intent.putExtra("BOOK", currentBook)
            startActivity(intent)
        }

        buttonDelete.setOnClickListener {
            lifecycleScope.launch {
                try {
                    repository.deleteBook(currentBook)
                    Toast.makeText(this@BookDetailActivity, "Книга удалена", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@BookDetailActivity, "Ошибка удаления", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}