package com.example.optika.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.optika.models.Client

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION),
    DBService {

    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            ("CREATE TABLE " + DBConstants.TABLE_NAME + "("
                    + DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DBConstants.CLIENT_NAME + " TEXT, "
                    + DBConstants.CLIENT_PHONE + " TEXT, "
                    + DBConstants.DATE_OF_BIRTH + " TEXT, "
                    + DBConstants.IMAGE + " TEXT, "
                    + DBConstants.GENDER + " INTEGER, "
                    + DBConstants.PRODUCT_NAME + " TEXT, "
                    + DBConstants.DATE_OF_PURCHASE + " TEXT, "
                    + DBConstants.DIOPTER + " TEXT, "
                    + DBConstants.DATE_OF_EXPIRATION + " TEXT, "
                    + DBConstants.NOTES + " TEXT )")
        db?.execSQL(query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    override fun addClient(client: Client) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        with(contentValues) {
            put(DBConstants.CLIENT_NAME, client.name)
            put(DBConstants.CLIENT_PHONE, client.phone)
            put(DBConstants.DATE_OF_BIRTH, client.dateOfBirth)
            put(DBConstants.IMAGE, client.image)
            put(DBConstants.GENDER, client.gender)
            put(DBConstants.PRODUCT_NAME, client.productName)
            put(DBConstants.DATE_OF_PURCHASE, client.dateOfPurchase)
            put(DBConstants.DIOPTER, client.diopter)
            put(DBConstants.DATE_OF_EXPIRATION, client.dateOfExpiration)
            put(DBConstants.NOTES, client.notes)
        }

        db.insert(DBConstants.TABLE_NAME, null, contentValues)
        db.close()

    }

    override fun getAllClients(): ArrayList<Client> {
        val clientsList = arrayListOf<Client>()
        val db = this.readableDatabase
        val query = "SELECT * FROM ${DBConstants.TABLE_NAME}"

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                clientsList.add(
                    Client(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10)

                    )
                )


            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()
        return clientsList
    }

    override fun editClient(client: Client) {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        with(contentValues) {
            put(DBConstants.CLIENT_NAME, client.name)
            put(DBConstants.CLIENT_PHONE, client.phone)
            put(DBConstants.DATE_OF_BIRTH, client.dateOfBirth)
            put(DBConstants.IMAGE, client.image)
            put(DBConstants.GENDER, client.gender)
            put(DBConstants.PRODUCT_NAME, client.productName)
            put(DBConstants.DATE_OF_PURCHASE, client.dateOfPurchase)
            put(DBConstants.DIOPTER, client.diopter)
            put(DBConstants.DATE_OF_EXPIRATION, client.dateOfExpiration)
            put(DBConstants.NOTES, client.notes)
        }

        db.update(DBConstants.TABLE_NAME, contentValues, "${DBConstants.ID} = ${client.id}", null)
        db.close()

    }

    override fun deleteClient(client: Client) {
        val db = this.writableDatabase
        db.delete(DBConstants.TABLE_NAME, "${DBConstants.ID} = ${client.id}", null)
        db.close()
    }

}
