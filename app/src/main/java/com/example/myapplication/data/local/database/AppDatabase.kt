package com.example.myapplication.data.local.database

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.myapplication.data.local.dao.CriptogramaDAO
import com.example.myapplication.data.local.dao.PartidaDAO
import com.example.myapplication.data.local.dao.UsuarioDAO
import com.example.myapplication.data.local.entity.Criptograma
import com.example.myapplication.data.local.entity.Partida
import com.example.myapplication.data.local.entity.Usuario

@Database(entities = [Usuario::class, Partida::class, Criptograma::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDAO(): UsuarioDAO
    abstract fun partidaDAO(): PartidaDAO
    abstract fun criptogramaDAO(): CriptogramaDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{

            val tempInstance = INSTANCE

            if(tempInstance != null){
                return tempInstance
            } else {
                synchronized(this){

                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).build()

                    INSTANCE = instance
                    return instance
                }
            }
        }
    }
}