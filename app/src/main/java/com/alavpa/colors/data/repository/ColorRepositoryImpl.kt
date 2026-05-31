package com.alavpa.colors.data.repository

import com.alavpa.colors.data.ColorProvider
import com.alavpa.colors.domain.model.RgbColor
import com.alavpa.colors.domain.repository.ColorRepository
import javax.inject.Inject

class ColorRepositoryImpl @Inject constructor(
    private val colorProvider: ColorProvider
) : ColorRepository {
    override fun getRandomColor(): RgbColor {
        return colorProvider.getRandomColor()
    }
}
