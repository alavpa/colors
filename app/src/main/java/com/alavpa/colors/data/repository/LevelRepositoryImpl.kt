package com.alavpa.colors.data.repository

import com.alavpa.colors.data.LevelProvider
import com.alavpa.colors.domain.model.Level
import com.alavpa.colors.domain.repository.LevelRepository
import javax.inject.Inject

class LevelRepositoryImpl @Inject constructor(
    private val levelProvider: LevelProvider
) : LevelRepository {
    override fun getLevel(levelId: Int): Level {
        return levelProvider.getLevel(levelId)
    }
}
