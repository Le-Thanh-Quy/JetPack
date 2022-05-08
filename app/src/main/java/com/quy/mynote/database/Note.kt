package com.quy.mynote.database

import android.icu.text.CaseMap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Note")
data class Note(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "content")
    var content: String,

    @ColumnInfo(name = "time")
    var time: String
)
