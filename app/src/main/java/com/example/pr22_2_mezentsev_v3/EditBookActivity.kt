package com.example.pr22_2_mezentsev_v3


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class EditBookActivity : AppCompatActivity() {
    private lateinit var repository: BookRepository
    private lateinit var currentBook: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)

        repository = BookRepository(this)
        currentBook = intent.getSerializableExtra("BOOK") as Book

        val editTextTitle = findViewById<EditText>(R.id.editTextTitle)
        val editTextAuthor = findViewById<EditText>(R.id.editTextAuthor)
        val editTextDescription = findViewById<EditText>(R.id.editTextDescription)
        val buttonSave = findViewById<Button>(R.id.buttonSaveChanges)

        editTextTitle.setText(currentBook.title)
        editTextAuthor.setText(currentBook.author)
        editTextDescription.setText(currentBook.description ?: "")

        buttonSave.setOnClickListener {
            val title = editTextTitle.text.toString().trim()
            val author = editTextAuthor.text.toString().trim()
            val description = editTextDescription.text.toString().trim()

            if (title.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "Заполните обязательные поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedBook = currentBook.copy(
                title = title,
                author = author,
                description = if (description.isNotEmpty()) description else null
            )

            lifecycleScope.launch {
                try {
                    repository.updateBook(updatedBook)
                    Toast.makeText(this@EditBookActivity, "Изменения сохранены", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@EditBookActivity, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}