package com.alavpa.colors.data.repository

import com.alavpa.colors.domain.factory.LevelFactory
import com.alavpa.colors.domain.model.Level
import com.alavpa.colors.domain.repository.LevelRepository
import javax.inject.Inject

class LevelRepositoryImpl @Inject constructor(
    private val levelFactory: LevelFactory
) : LevelRepository {
    override fun getLevel(levelId: Int): Level {
        return levelFactory.createLevel(levelId)
    }
}
