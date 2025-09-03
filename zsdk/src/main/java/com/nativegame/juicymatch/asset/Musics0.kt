package com.nativegame.juicymatch.asset

import com.nativegame.natyengine.audio.music.Music
import com.nativegame.natyengine.audio.music.MusicManager
import com.walhalla.sdk.R

/**
 * Created by Oscar Liang on 2022/02/23
 */
object Musics0 {
    @JvmField
    var BG_MUSIC: Music? = null
    @JvmField
    var GAME_MUSIC: Music? = null

    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    @JvmStatic
    fun load(musicManager: MusicManager) {
        BG_MUSIC = musicManager.load(R.raw.happy_and_joyful_children)
        BG_MUSIC!!.setVolume(0.3f, 0.3f)
        BG_MUSIC!!.isLooping = true
        BG_MUSIC!!.isCurrentStream = true

        GAME_MUSIC = musicManager.load(R.raw.bgm)
        GAME_MUSIC!!.setVolume(1f, 1f)
        GAME_MUSIC!!.isLooping = true
        GAME_MUSIC!!.isCurrentStream = false
    } //========================================================
}
