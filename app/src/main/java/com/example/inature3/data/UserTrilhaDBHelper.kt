package com.example.inature3.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.inature3.model.Trilha
import com.example.inature3.model.UserTrilha
import java.time.LocalDate
import kotlin.math.abs

class UserTrilhaDBHelper(context: Context) :
    SQLiteOpenHelper(context, NOME_DO_BANCO, null, VERSAO_DO_BANCO) {

    companion object {
        private const val NOME_DO_BANCO = "inature3.db"
        private const val VERSAO_DO_BANCO = 1
        private const val NOME_TABELA = "user_trilha"
        private const val COLUNA_ID_USUARIO = "id_usuario"
        private const val COLUNA_ID_TRILHA = "id_trilha"
        private const val COLUNA_TEMPO_GASTO = "tempo_gasto"
        private const val COLUNA_NOTA = "nota"
        private const val COLUNA_MT_ANDADOS= "mt_andados"
    }

    override fun onCreate(db: SQLiteDatabase) {
        criarTabela(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
    fun adicionarNotaTrilha(emailUsuario: String, idTrilha: Long, nota: Int) {
        val db = writableDatabase


        if (!tabelaExiste(NOME_TABELA, db)) {
            criarTabela(db)
        }


        val trilhaUsuarioExistente = obterUsuarioTrilha(emailUsuario, idTrilha)

        if (trilhaUsuarioExistente != null) {
            val valores = ContentValues().apply {
                put(COLUNA_NOTA, nota)
            }
            db.update(
                NOME_TABELA,
                valores,
                "$COLUNA_ID_USUARIO=? AND $COLUNA_ID_TRILHA=?",
                arrayOf(emailUsuario, idTrilha.toString())
            )
        } else {
            val valores = ContentValues().apply {
                put(COLUNA_ID_USUARIO, emailUsuario)
                put(COLUNA_ID_TRILHA, idTrilha)
                put(COLUNA_NOTA, nota)
            }

            db.insert(NOME_TABELA, null, valores)
        }

        db.close()
    }

    fun atualizarUsuarioTrilha(emailUsuario: String, idTrilha: Long, tempoAdicional: String, distanciaAdicional: Double) {
        val db = writableDatabase

        if (!tabelaExiste(NOME_TABELA, db)) {
            criarTabela(db)
        }

        val trilhaUsuarioExistente = obterUsuarioTrilha(emailUsuario, idTrilha)

        if (trilhaUsuarioExistente != null) {

            val novoTempo = (abs(trilhaUsuarioExistente.tempoGasto.toLong()) + abs(tempoAdicional.toLong())).toString()
            val novaDistancia = abs(trilhaUsuarioExistente.mtAndados) + abs(distanciaAdicional)

            val valores = ContentValues().apply {
                put(COLUNA_TEMPO_GASTO, novoTempo)
                put(COLUNA_MT_ANDADOS, novaDistancia)
            }

            db.update(
                NOME_TABELA,
                valores,
                "$COLUNA_ID_USUARIO=? AND $COLUNA_ID_TRILHA=?",
                arrayOf(emailUsuario, idTrilha.toString())
            )
        } else {

            val valores = ContentValues().apply {
                put(COLUNA_ID_USUARIO, emailUsuario)
                put(COLUNA_ID_TRILHA, idTrilha)
                put(COLUNA_TEMPO_GASTO, tempoAdicional)
                put(COLUNA_MT_ANDADOS, distanciaAdicional)
            }

            db.insert(NOME_TABELA, null, valores)
        }

        db.close()
    }


    fun inserirUsuarioTrilha(emailUsuario: String, idTrilha: Long, tempoGasto: String, nota: Int, mtAndados:Double) {
        val db = writableDatabase


        if (!tabelaExiste(NOME_TABELA, db)) {
            criarTabela(db)
        }

        val valores = ContentValues().apply {
            put(COLUNA_ID_USUARIO, emailUsuario)
            put(COLUNA_ID_TRILHA, idTrilha)
            put(COLUNA_TEMPO_GASTO, tempoGasto)
            put(COLUNA_NOTA, nota)
            put(COLUNA_MT_ANDADOS, mtAndados)
        }

        db.insert(NOME_TABELA, null, valores)
        db.close()
    }

    fun obterUsuarioTrilha(emailUsuario: String, idTrilha: Long): UserTrilha? {
        val db = readableDatabase


        if (!tabelaExiste(NOME_TABELA, db)) {
            db.close()
            return null
        }

        val cursor: Cursor = db.query(
            NOME_TABELA,
            arrayOf(COLUNA_TEMPO_GASTO, COLUNA_NOTA, COLUNA_MT_ANDADOS),
            "$COLUNA_ID_USUARIO=? AND $COLUNA_ID_TRILHA=?",
            arrayOf(emailUsuario, idTrilha.toString()),
            null,
            null,
            null
        )

        var userTrilha: UserTrilha? = null

        if (cursor.moveToFirst()) {
            val indiceTempoGasto = cursor.getColumnIndex(COLUNA_TEMPO_GASTO)
            val indiceNota = cursor.getColumnIndex(COLUNA_NOTA)
            val indiceMtAndados = cursor.getColumnIndex(COLUNA_MT_ANDADOS)

            val tempoGasto = cursor.getString(indiceTempoGasto)
            val nota = cursor.getInt(indiceNota)
            val mtAndados = cursor.getDouble(indiceMtAndados)

            userTrilha = UserTrilha(emailUsuario, idTrilha, tempoGasto, nota, mtAndados)
        }

        cursor.close()
//        db.close()

        return userTrilha
    }

    fun obterTrilhas(emailUsuario: String): List<Trilha> {
        val db = readableDatabase


        if (!tabelaExiste(NOME_TABELA, db)) {
            db.close()
            return emptyList()
        }

        val trilhasList = mutableListOf<Trilha>()

        val query = """
            SELECT t.${TrilhaDBHelper.COLUNA_ID} ,
                   t.${TrilhaDBHelper.COLUNA_NOME_TRILHA},
                   t.${TrilhaDBHelper.COLUNA_TEMPO_ESTIMADO},
                   t.${TrilhaDBHelper.COLUNA_DIFICULDADE},
                   t.${TrilhaDBHelper.COLUNA_DISTANCIA},
                   t.${TrilhaDBHelper.COLUNA_LAT},
                   t.${TrilhaDBHelper.COLUNA_LONG},
                   ut.${COLUNA_TEMPO_GASTO},
                   ut.${COLUNA_NOTA},
                   ut.${COLUNA_MT_ANDADOS}
            FROM ${TrilhaDBHelper.NOME_TABELA} t
            JOIN ${NOME_TABELA} ut ON t.${TrilhaDBHelper.COLUNA_ID} = ut.${COLUNA_ID_TRILHA}
            WHERE ut.${COLUNA_ID_USUARIO} = ?
        """.trimIndent()


        val cursor: Cursor = db.rawQuery(query, arrayOf(emailUsuario))

        while (cursor.moveToNext()) {

            val indiceIdTrilha = cursor.getColumnIndex(TrilhaDBHelper.COLUNA_ID)
            val indiceNomeTrilha = cursor.getColumnIndex(TrilhaDBHelper.COLUNA_NOME_TRILHA)
            val indiceDificuldade = cursor.getColumnIndex(TrilhaDBHelper.COLUNA_DIFICULDADE)
            val indiceLat = cursor.getColumnIndex(TrilhaDBHelper.COLUNA_LAT)
            val indiceLong = cursor.getColumnIndex(TrilhaDBHelper.COLUNA_LONG)
            val indiceTempoGasto = cursor.getColumnIndex(COLUNA_TEMPO_GASTO)
            val indiceMtAndados = cursor.getColumnIndex(COLUNA_MT_ANDADOS)


            val idTrilha = cursor.getLong(indiceIdTrilha)
            val nomeTrilha = cursor.getString(indiceNomeTrilha)
            val dificuldade = cursor.getDouble(indiceDificuldade)
            val tempoGasto = cursor.getDouble(indiceTempoGasto)
            val mtAndados = cursor.getDouble(indiceMtAndados)
            val lat = cursor.getDouble(indiceLat)
            val long = cursor.getDouble(indiceLong)

            val trilha = Trilha(idTrilha, nomeTrilha, (tempoGasto/1000000000).toString(), dificuldade, mtAndados, lat, long)
            trilhasList.add(trilha)
        }

        cursor.close()
        db.close()

        return trilhasList
    }

    fun quantasTrilhasUsuarioAndou(emailUsuario: String): Int {
        val db = readableDatabase

        if (!tabelaExiste(NOME_TABELA, db)) {
            db.close()
            return 0
        }

        val cursor: Cursor = db.rawQuery(
            "SELECT COUNT(*) FROM $NOME_TABELA WHERE $COLUNA_ID_USUARIO = ?",
            arrayOf(emailUsuario)
        )

        var quantidadeTrilhas = 0

        if (cursor.moveToFirst()) {
            quantidadeTrilhas = cursor.getInt(0)
        }

        cursor.close()
        db.close()

        return quantidadeTrilhas
    }


    private fun tabelaExiste(tableName: String, db: SQLiteDatabase): Boolean {
        val cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '$tableName'", null)
        val tabelaExiste = cursor.count > 0
        cursor.close()
        return tabelaExiste
    }


    private fun criarTabela(db: SQLiteDatabase) {
        val criarTabelaQuery = "CREATE TABLE $NOME_TABELA (" +
                "$COLUNA_ID_USUARIO TEXT," +
                "$COLUNA_ID_TRILHA INTEGER," +
                "$COLUNA_TEMPO_GASTO TEXT," +
                "$COLUNA_NOTA INTEGER," +
                "$COLUNA_MT_ANDADOS DOUBLE," +
                "PRIMARY KEY ($COLUNA_ID_USUARIO, $COLUNA_ID_TRILHA)," +
                "FOREIGN KEY ($COLUNA_ID_USUARIO) REFERENCES usuarios(email) ON DELETE CASCADE," +
                "FOREIGN KEY ($COLUNA_ID_TRILHA) REFERENCES trilhas(id) ON DELETE CASCADE)"

        db.execSQL(criarTabelaQuery)
    }
    fun calcularDistanciaTotal(emailUsuario: String): Double {
        val db = readableDatabase

        if (!tabelaExiste(NOME_TABELA, db)) {
            db.close()
            return 0.0
        }

        val query = """
        SELECT SUM($COLUNA_MT_ANDADOS) AS distancia_total
        FROM $NOME_TABELA
        WHERE $COLUNA_ID_USUARIO = ?
    """.trimIndent()

        val cursor: Cursor = db.rawQuery(query, arrayOf(emailUsuario))

        var distanciaTotal = 0.0

        if (cursor.moveToFirst()) {
            val indiceDistanciaTotal = cursor.getColumnIndex("distancia_total")
            distanciaTotal = cursor.getDouble(indiceDistanciaTotal)
        }

        cursor.close()
        db.close()

        return distanciaTotal
    }

}
