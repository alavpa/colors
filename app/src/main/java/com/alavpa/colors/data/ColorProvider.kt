package com.alavpa.colors.data

import com.alavpa.colors.domain.model.RgbColor
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ColorProvider @Inject constructor() {
    fun getRandomColor(): RgbColor {
        return RgbColor(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1f
        )
    }
}
