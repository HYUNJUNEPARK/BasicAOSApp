package com.example.calculator.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.calculator.model.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    @Insert
    fun insetHistory(history: History)

    @Query("DELETE FROM history")
    fun deleteAll()

//    @Delete
//    fun delete(history: History)

//    결과 하나만 반환하는 함수
//    @Query("SELECT * FROM history WHERE result LIKE :result LIMIT 1")
//    fun findByResult(result: String): History
}