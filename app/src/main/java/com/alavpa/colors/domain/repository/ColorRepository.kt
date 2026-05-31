package com.alavpa.colors.domain.repository

import com.alavpa.colors.domain.model.RgbColor

interface ColorRepository {
    fun getRandomColor(): RgbColor
}
