package com.pixfootball.pixfoot.betpix.footballbraz.app

import android.app.Application
import com.nativegame.juicymatch.SdkConfig

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SdkConfig.faqUrl = "https://mixers.top/FAQQ9".toCharArray()
        SdkConfig.privacyPolicyUrl = "https://mixers.top/Privacyy9".toCharArray()
        SdkConfig.termsUrl = "https://mixers.top/termss8".toCharArray()
    }

}