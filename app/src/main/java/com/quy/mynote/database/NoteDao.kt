package com.quy.mynote.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {
    @Query("SELECT * from Note")
    fun getAll(): LiveData<List<Note>>

    @Insert
    suspend fun insert(item: Note)

    @Delete
    suspend fun delete(item: Note)
}