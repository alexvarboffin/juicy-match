package com.cric.cricin.tencric

import android.app.Application
import com.nativegame.juicymatch.SdkConfig

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SdkConfig.faqUrl = "https://rotyik.top/FAQQ7".toCharArray()
        SdkConfig.privacyPolicyUrl = "https://rotyik.top/Privacyy7".toCharArray()
        SdkConfig.termsUrl = "https://rotyik.top/termss7".toCharArray()
    }

}