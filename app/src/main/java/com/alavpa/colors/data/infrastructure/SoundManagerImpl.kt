package com.alavpa.colors.data.infrastructure

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.alavpa.colors.R
import com.alavpa.colors.domain.infrastructure.SoundManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : SoundManager {
    private val soundPool: SoundPool
    private var clickSoundId: Int = -1

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        clickSoundId = soundPool.load(context, R.raw.click_005, 1)
    }

    override fun playTapSound() {
        if (clickSoundId != -1) {
            soundPool.play(clickSoundId, 1f, 1f, 1, 0, 1f)
        }
    }

    override fun release() {
        soundPool.release()
    }
}
