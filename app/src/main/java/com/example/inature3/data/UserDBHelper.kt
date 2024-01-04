package com.example.inature3.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.inature3.model.UserRank

class UserDBHelper(context: Context) :
    SQLiteOpenHelper(context, NOME_DO_BANCO, null, VERSAO_DO_BANCO) {

    companion object {
        private const val NOME_DO_BANCO = "inature3.db"
        private const val VERSAO_DO_BANCO = 1
        private const val NOME_TABELA = "usuarios"
        private const val COLUNA_EMAIL = "email"
        private const val COLUNA_NOME = "nome"
        private const val COLUNA_SENHA = "senha"
        private const val COLUNA_PONTUACAO = "pontos"
    }

    override fun onCreate(db: SQLiteDatabase) {
        criarTabela(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Política de atualização se necessário
    }

    fun incrementarPontos(email: String, pontosIncremento: Int) {
        val db = writableDatabase

        try {
            if (!tabelaExiste(NOME_TABELA, db)) {
                criarTabela(db)
            }

            val pontosAtuais = obterPontos(email)

            if (pontosAtuais == null) {
                val valoresIniciais = ContentValues().apply {
                    put(COLUNA_PONTUACAO, pontosIncremento)
                }
                db.update(NOME_TABELA, valoresIniciais, "$COLUNA_EMAIL=?", arrayOf(email))
            } else {
                val contentValues = ContentValues()
                contentValues.put(COLUNA_PONTUACAO, pontosAtuais + pontosIncremento)
                db.update(NOME_TABELA, contentValues, "$COLUNA_EMAIL=?", arrayOf(email))
            }

        } catch (e: Exception) {
            Log.e("IncrementarPontos", "Erro: ${e.message}")
        } finally {
            db.close()
        }
    }



    @SuppressLint("Range")
    private fun obterPontos(email: String): Int? {
        val db = readableDatabase

        if (!tabelaExiste(NOME_TABELA, db)) {
            db.close()
            return null
        }

        val cursor: Cursor = db.query(
            NOME_TABELA,
            arrayOf(COLUNA_PONTUACAO),
            "$COLUNA_EMAIL=?",
            arrayOf(email),
            null,
            null,
            null
        )

        var pontos: Int? = null

        if (cursor.moveToFirst()) {
            pontos = cursor.getInt(cursor.getColumnIndex(COLUNA_PONTUACAO))
        }

        cursor.close()

//        db.close()

        return pontos
    }


    @SuppressLint("Range")
    fun obterTodosUsuarios():List<UserRank>? {
        val db = readableDatabase
        val usuarios = mutableListOf<UserRank>()


        if (!tabelaExiste(NOME_TABELA, db)) {
            db.close()
            return null
        }

        val cursor: Cursor = db.query(
            NOME_TABELA,
            arrayOf(COLUNA_EMAIL, COLUNA_NOME, COLUNA_PONTUACAO),
            null,
            null,
            null,
            null,
            "$COLUNA_PONTUACAO DESC" // Order by points in descending order
        )

        while (cursor.moveToNext()) {
            val email = cursor.getString(cursor.getColumnIndex(COLUNA_EMAIL))
            val nome = cursor.getString(cursor.getColumnIndex(COLUNA_NOME))
            val pontuacao = cursor.getInt(cursor.getColumnIndex(COLUNA_PONTUACAO))

            val usuario = UserRank(obtemFotoAleatoria(), nome, pontuacao)
            usuarios.add(usuario)
        }

        cursor.close()
        db.close()


        return usuarios
    }


    private fun obtemFotoAleatoria(): String {
        val indiceFotoAleatatoria = (1..15).random()
        return "avatar$indiceFotoAleatatoria"
    }

    fun inserirUsuario(email: String, nome: String, senha: String) {
        val db = writableDatabase


        if (!tabelaExiste(NOME_TABELA, db)) {
            criarTabela(db)
        }

        val valores = ContentValues().apply {
            put(COLUNA_EMAIL, email)
            put(COLUNA_NOME, nome)
            put(COLUNA_SENHA, senha)
        }

        db.insert(NOME_TABELA, null, valores)
        db.close()
    }

    fun lerUsuario(email: String): String? {
        val db = readableDatabase


        if (!tabelaExiste(NOME_TABELA, db)) {
            db.close()
            return null
        }

        val cursor: Cursor = db.query(
            NOME_TABELA,
            arrayOf(COLUNA_NOME, COLUNA_SENHA),
            "$COLUNA_EMAIL=?",
            arrayOf(email),
            null,
            null,
            null
        )

        var resultado: String? = null

        if (cursor.moveToFirst()) {
            val indiceNome = cursor.getColumnIndex(COLUNA_NOME)
            val indiceSenha = cursor.getColumnIndex(COLUNA_SENHA)

            val nome = cursor.getString(indiceNome)
            val senha = cursor.getString(indiceSenha)

            resultado = "$nome|$senha"
        }

        cursor.close()
        db.close()

        return resultado
    }

    private fun tabelaExiste(tableName: String, db: SQLiteDatabase): Boolean {
        val cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '$tableName'", null)
        val tabelaExiste = cursor.count > 0
        cursor.close()
        return tabelaExiste
    }


    private fun criarTabela(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $NOME_TABELA (" +
                "$COLUNA_EMAIL TEXT PRIMARY KEY," +
                "$COLUNA_NOME TEXT," +
                "$COLUNA_SENHA TEXT," +
                "$COLUNA_PONTUACAO INT)"     )
    }
}
