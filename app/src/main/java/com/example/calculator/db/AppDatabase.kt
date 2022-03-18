package com.example.calculator.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calculator.dao.HistoryDao
import com.example.calculator.model.History

//앱이 바뀌면 앱의 버전도 바뀔 경우가 있는데
//이떄 이전 데이터 베이스의 데이터를 마이그레이션 해줘야함 마이그레이션 코드 작성시 버전코드가 필요함



@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

}