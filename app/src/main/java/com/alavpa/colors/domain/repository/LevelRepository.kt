package com.alavpa.colors.domain.repository

import com.alavpa.colors.domain.model.Level

interface LevelRepository {
    fun getLevel(levelId: Int): Level
}
