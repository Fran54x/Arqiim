package com.example.proyectofinal_moviles

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.sql.SQLException

class ControladorBD(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context,name,factory,version) {
    override fun onCreate(dataBase: SQLiteDatabase?) {
        //Instruccion DDL (Create)
        val sql = "create table usuario " +
                "(ID INTEGER primary key AUTOINCREMENT, " +
                "nombre text,"+
                "correo text, " +
                "password text, " +
                "telefono int, " +
                "terminosCondiciones text" +
                ")"
        try{
            dataBase?.execSQL(sql)
        }catch (e: SQLException){
            Toast.makeText(
                null, "Error al crear la base de datos", Toast.LENGTH_SHORT).show()

        }
    }

    @SuppressLint("Range")
    fun validarUsuario(nombre: String, password: String): Boolean{
            val db = this.readableDatabase // es adecuado para consultas que no van a modificar la base de datos
            val query = "SELECT * FROM usuario where nombre = ? AND password = ?" //interrogación (?) son marcadores de posición para los parámetros que se pasarán más tarde.
            //El método rawQuery toma la consulta SQL y un array de parámetros que reemplazará los marcadores de posición (?)
            val cursor = db.rawQuery(query, arrayOf(nombre, password))

        return if (cursor != null && cursor.moveToFirst()) {
            val nombreDB = cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
            val passwordDB = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            Log.d("ValidarUsuario", "Usuario encontrado: $nombreDB, Password: $passwordDB")
            cursor.close()
            true
        } else {
            Log.d("ValidarUsuario", "Usuario no encontrado o credenciales incorrectas")
            cursor?.close()
            false
        }

    }
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

}