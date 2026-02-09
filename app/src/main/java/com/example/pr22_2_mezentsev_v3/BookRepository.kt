package com.example.pr22_2_mezentsev_v3

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository(context: Context) {
    private val bookDao = AppDatabase.getDatabase(context).bookDao()
    private val apiService: OpenLibraryApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openlibrary.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(OpenLibraryApi::class.java)
    }

    suspend fun searchBookByIsbn(isbn: String): Book? = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getBookByIsbn(isbn).execute()
            if (response.isSuccessful) {
                val openLibraryBook = response.body()
                openLibraryBook?.let { olBook ->
                    val title = olBook.title ?: "Неизвестно"
                    val author = olBook.authors?.firstOrNull()?.key?.let {
                        it.split("/").lastOrNull() ?: "Неизвестный автор"
                    } ?: "Неизвестный автор"

                    val coverUrl = olBook.covers?.firstOrNull()?.let {
                        "https://covers.openlibrary.org/b/id/$it-M.jpg"
                    }

                    val description = when (olBook.description) {
                        is String -> olBook.description
                        is Map<*, *> -> olBook.description["value"] as? String
                        else -> null
                    }

                    Book(isbn, title, author, coverUrl, description)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun insertBook(book: Book) = withContext(Dispatchers.IO) {
        bookDao.insert(book)
    }

    suspend fun updateBook(book: Book) = withContext(Dispatchers.IO) {
        bookDao.update(book)
    }

    suspend fun deleteBook(book: Book) = withContext(Dispatchers.IO) {
        bookDao.delete(book)
    }

    suspend fun getAllBooks(): List<Book> = withContext(Dispatchers.IO) {
        bookDao.getAllBooks()
    }

    suspend fun getBookByIsbn(isbn: String): Book? = withContext(Dispatchers.IO) {
        bookDao.getBookByIsbn(isbn)
    }
}