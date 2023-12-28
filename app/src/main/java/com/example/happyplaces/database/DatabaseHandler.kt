package com.example.happyplaces.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.happyplaces.model.HappyPlaceModel
class DatabaseHandler (context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null , DATABASE_VERSION) {
    companion object {

        private const val DATABASE_VERSION = 4
        private const val DATABASE_NAME = "HappyPlaceDatabase"
        private const val TABLE_HAPPY_PLACE = "HappyPlacesTable"

        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
        private const val KEY_IMAGE = "image"
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HAPPY_PLACE")
        onCreate(db)
    }
    override fun onCreate(db: SQLiteDatabase) {
        val createHappyPlaceTable =
            ("CREATE TABLE" + TABLE_HAPPY_PLACE + "("
                    + KEY_ID + "INTEGER PRIMARY KEY,"
                    + KEY_TITLE + "TEXT,"
                    + KEY_DESCRIPTION + "TEXT,"
                    + KEY_DATE + "TEXT,"
                    + KEY_LOCATION + "TEXT,"
                    + KEY_LATITUDE + "TEXT,"
                    + KEY_LONGITUDE + "TEXT,"
                    + KEY_IMAGE + "TEXT)")
        db.execSQL(createHappyPlaceTable)
    }
    fun addHappyPlace(happyPlace: HappyPlaceModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, happyPlace.title)
        contentValues.put(KEY_DESCRIPTION, happyPlace.description)
        contentValues.put(KEY_DATE, happyPlace.date)
        contentValues.put(KEY_LOCATION, happyPlace.location)
        contentValues.put(KEY_LATITUDE, happyPlace.latitude)
        contentValues.put(KEY_LONGITUDE, happyPlace.longitude)
        contentValues.put(KEY_IMAGE, happyPlace.image)

        val result = db.insert(TABLE_HAPPY_PLACE, null, contentValues)
        db.close()
        return result
    }
    @SuppressLint("Range", "Recycle")
    fun getHappyPlacesList():ArrayList<HappyPlaceModel>{
        val happyPlaceList : ArrayList<HappyPlaceModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_HAPPY_PLACE"
        val  db = this.readableDatabase

        try {
            val cursor:Cursor  = db.rawQuery(selectQuery,null)
            if(cursor.moveToFirst()){
                do {
                    val place= HappyPlaceModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                    )
                    happyPlaceList.add(place)
                }while (cursor.moveToNext())
            }
            cursor.close()
        }catch (e:SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return happyPlaceList
    }
}