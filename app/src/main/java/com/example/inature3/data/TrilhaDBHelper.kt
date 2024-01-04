package com.example.inature3.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.inature3.model.Trilha

class TrilhaDBHelper(context: Context) :
    SQLiteOpenHelper(context, NOME_DO_BANCO, null, VERSAO_DO_BANCO) {

    companion object {
        private const val NOME_DO_BANCO = "inature3.db"
        private const val VERSAO_DO_BANCO = 1
        const val NOME_TABELA = "trilhas"
        const val COLUNA_ID = "id"
        const val COLUNA_NOME_TRILHA = "nome_trilha"
        const val COLUNA_TEMPO_ESTIMADO = "tempo_gasto"
        const val COLUNA_DIFICULDADE = "dificuldade"
        const val COLUNA_DISTANCIA = "distancia"
        const val COLUNA_LAT = "latitude"
        const val COLUNA_LONG = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val criarTabelaQuery = "CREATE TABLE $NOME_TABELA (" +
                "$COLUNA_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUNA_NOME_TRILHA TEXT," +
                "$COLUNA_TEMPO_ESTIMADO TEXT," +
                "$COLUNA_DIFICULDADE REAL," +
                "$COLUNA_DISTANCIA REAL," +
                "$COLUNA_LAT REAL," +
                "$COLUNA_LONG REAL)"
        println(criarTabelaQuery)
        db.execSQL(criarTabelaQuery)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
    @SuppressLint("Range")
    fun obterTodasTrilhas(): List<Trilha> {
        val db = readableDatabase
        val trilhas = mutableListOf<Trilha>()


        if (!tabelaExiste(NOME_TABELA, db)) {
            db.close()
            return trilhas
        }

        val cursor: Cursor = db.query(
            NOME_TABELA,
            arrayOf(COLUNA_ID, COLUNA_NOME_TRILHA, COLUNA_TEMPO_ESTIMADO, COLUNA_DIFICULDADE, COLUNA_DISTANCIA, COLUNA_LAT, COLUNA_LONG),
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(COLUNA_ID))
            val nomeTrilha = cursor.getString(cursor.getColumnIndex(COLUNA_NOME_TRILHA))
            val tempoEstimado = cursor.getString(cursor.getColumnIndex(COLUNA_TEMPO_ESTIMADO))
            val dificuldade = cursor.getDouble(cursor.getColumnIndex(COLUNA_DIFICULDADE))
            val distancia = cursor.getDouble(cursor.getColumnIndex(COLUNA_DISTANCIA))
            val lat = cursor.getDouble(cursor.getColumnIndex(COLUNA_LAT))
            val long = cursor.getDouble(cursor.getColumnIndex(COLUNA_LONG))

            val trilha = Trilha(id, nomeTrilha, tempoEstimado, dificuldade, distancia, lat, long)
            trilhas.add(trilha)
        }


        cursor.close()
        db.close()

        return trilhas
    }
    fun inserirTrilha(trilha: Trilha) {
        val db = writableDatabase

        if (!tabelaExiste(NOME_TABELA, db)) {
            criarTabela(db)
        }
        val valores = ContentValues().apply {
            put(COLUNA_NOME_TRILHA, trilha.nomeTrilha)
            put(COLUNA_TEMPO_ESTIMADO, trilha.tempoEstimado)
            put(COLUNA_DIFICULDADE, trilha.dificuldade)
            put(COLUNA_DISTANCIA, trilha.distancia)
            put(COLUNA_LAT, trilha.lat)
            put(COLUNA_LONG, trilha.lng)
        }

        db.insert(NOME_TABELA, null, valores)
        db.close()
    }
    // Verifica se a tabela existe
    private fun tabelaExiste(nomeTabela: String, db: SQLiteDatabase): Boolean {
        val cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '$nomeTabela'", null)
        val tabelaExiste = cursor.count > 0
        cursor.close()
        return tabelaExiste
    }

    private fun criarTabela(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $NOME_TABELA (" +
                "$COLUNA_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUNA_NOME_TRILHA TEXT," +
                "$COLUNA_TEMPO_ESTIMADO TEXT," +
                "$COLUNA_DIFICULDADE REAL," +
                "$COLUNA_DISTANCIA REAL," +
                "$COLUNA_LAT REAL," +
                "$COLUNA_LONG REAL)")
    }

    fun lerTrilha(id: Long): Trilha? {
        val db = readableDatabase

        val cursor: Cursor = db.query(
            NOME_TABELA,
            arrayOf(
                COLUNA_NOME_TRILHA,
                COLUNA_TEMPO_ESTIMADO,
                COLUNA_DIFICULDADE,
                COLUNA_DISTANCIA,
                COLUNA_LAT,
                COLUNA_LONG
            ),
            "$COLUNA_ID=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var trilha: Trilha? = null

        if (cursor.moveToFirst()) {
            val indiceNomeTrilha = cursor.getColumnIndex(COLUNA_NOME_TRILHA)
            val indiceTempoEstimado = cursor.getColumnIndex(COLUNA_TEMPO_ESTIMADO)
            val indiceDificuldade = cursor.getColumnIndex(COLUNA_DIFICULDADE)
            val indiceDistancia = cursor.getColumnIndex(COLUNA_DISTANCIA)
            val indiceLat = cursor.getColumnIndex(COLUNA_LAT)
            val indiceLong = cursor.getColumnIndex(COLUNA_LONG)

            val nomeTrilha = cursor.getString(indiceNomeTrilha)
            val tempoEstimado = cursor.getString(indiceTempoEstimado)
            val dificuldade = cursor.getDouble(indiceDificuldade)
            val distancia = cursor.getDouble(indiceDistancia)
            val lat = cursor.getDouble(indiceLat)
            val long = cursor.getDouble(indiceLong)


            trilha = Trilha(id, nomeTrilha, tempoEstimado, dificuldade, distancia, lat, long)
        }

        cursor.close()
        db.close()

        return trilha
    }
}
