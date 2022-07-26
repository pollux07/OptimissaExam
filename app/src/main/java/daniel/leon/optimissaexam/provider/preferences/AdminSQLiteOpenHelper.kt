package daniel.leon.optimissaexam.provider.preferences

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteOpenHelper(context: Context,
                            name: String,
                            factory: SQLiteDatabase.CursorFactory?,
                            version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table contactos(id string, phone string, username string)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun searchContact(): Cursor? {
        val bd = writableDatabase
        var fila = bd.rawQuery("select * from contactos", null)
        return fila
    }


    fun searchContact(searchId: String): Cursor? {
        val bd = writableDatabase
        var fila = bd.rawQuery("select * from contactos where id = '${searchId}'", null)
        return fila
    }
}