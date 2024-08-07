package ru.androidtools.multithreadtest.loader

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper


class TestDatabase(
    private val context: Context
) {
    private var dbHelper: DBHelper? = null
    private var database: SQLiteDatabase? = null

    fun open() {
        dbHelper = DBHelper(context, DB_NAME, null, DB_VERSION)
        database = dbHelper?.writableDatabase
    }

    fun close() = dbHelper?.close()

    fun getAllData(): Cursor? = database?.query(
        DB_TABLE,
        null,
        null,
        null,
        null,
        null,
        null
    )

    private class DBHelper(
        context: Context,
        name: String,
        factory: CursorFactory?,
        version: Int
    ) : SQLiteOpenHelper(context, name, factory, version) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(DB_CREATE)
            for (i in 1..4) {
                val cv = ContentValues().apply {
                    put(COLUMN_TXT, "Some message $i")
                }
                db.insert(DB_TABLE, null, cv)
            }
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        }
    }

    companion object {
        private const val DB_NAME = "test_db"
        private const val DB_VERSION = 1
        private const val DB_TABLE = "test_table"

        private const val COLUMN_ID = "_id"
        const val COLUMN_TXT = "message"

        private const val DB_CREATE =
            "create table $DB_TABLE(" +
                "$COLUMN_ID integer primary key autoincrement, " +
                "$COLUMN_TXT text" +
                ");"
    }
}
