package com.nativegame.juicymatch.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.nativegame.juicymatch.asset.Musics0
import com.nativegame.juicymatch.asset.Preferences
import com.nativegame.juicymatch.asset.Sounds
import com.nativegame.juicymatch.ui.dialog.ExitDialog
import com.nativegame.juicymatch.ui.dialog.SettingDialog
import com.nativegame.natyengine.ui.GameButton
import com.nativegame.natyengine.ui.GameFragment
import com.nativegame.natyengine.ui.GameImage
import com.walhalla.sdk.R

/**
 * Created by Oscar Liang on 2022/02/23
 */
class MenuFragment  //--------------------------------------------------------
// Constructors
//--------------------------------------------------------
    : GameFragment(), View.OnClickListener {
    //========================================================
    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init logo image
        val imageLogo = view.findViewById<View?>(R.id.image_logo) as GameImage
        imageLogo.popUp(1000, 300)
        val scaleAnimation = AnimationUtils.loadAnimation(activity, R.anim.logo_pulse)
        imageLogo.startAnimation(scaleAnimation)

        val imageLogoBg = view.findViewById<View?>(R.id.image_logo_bg) as GameImage
        imageLogoBg.popUp(300, 300)
        val rotateAnimation = AnimationUtils.loadAnimation(activity, R.anim.logo_rotate)
        imageLogoBg.startAnimation(rotateAnimation)

        // Init button
        val btnPlay = view.findViewById<View?>(R.id.btn_start) as GameButton
        btnPlay.popUp(200, 600)
        btnPlay.setOnClickListener(this)
        val pulseAnimation = AnimationUtils.loadAnimation(activity, R.anim.button_pulse)
        btnPlay.startAnimation(pulseAnimation)



        val btnFaq = view.findViewById<View>(R.id.btnFaq) as GameButton
        btnFaq.setOnClickListener {
            openInCustomTab(
                view.context,
                "https://mixers.top/FAQ9"
            )
        }
        val btnPrivacy = view.findViewById<View>(R.id.btnPrivacy) as GameButton
        btnPrivacy.setOnClickListener {
            openInCustomTab(
                view.context,
                "https://mixers.top/Privacy9"
            )
        }


        val btnSetting = view.findViewById<View?>(R.id.btn_setting) as GameButton
        btnSetting.setOnClickListener(this)

        // Init audio state from Preference
        val musicEnable = Preferences.PREF_SETTING.getBoolean(Preferences.KEY_MUSIC, true)
        val soundEnable = Preferences.PREF_SETTING.getBoolean(Preferences.KEY_SOUND, true)
        getGameActivity().getMusicManager().setAudioEnable(musicEnable)
        getGameActivity().getSoundManager().setAudioEnable(soundEnable)

        // Play bg music
        Musics0.BG_MUSIC!!.play()
    }
    fun openInCustomTab(context: Context, url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, url.toUri())
    }
    override fun onBackPressed(): Boolean {
        showExitDialog()
        return true
    }

    override fun onClick(view: View) {
        Sounds.BUTTON_CLICK.play()
        val id = view.getId()
        if (id == R.id.btn_start) {
            getGameActivity().navigateToFragment(MapFragment())
        } else if (id == R.id.btn_setting) {
            showSettingDialog()
        }
    }

    //========================================================
    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private fun showExitDialog() {
        val exitDialog: ExitDialog = object : ExitDialog(getGameActivity()) {
            override fun exit() {
                getGameActivity().finish()
            }
        }
        getGameActivity().showDialog(exitDialog)
    }

    private fun showSettingDialog() {
        val settingDialog = SettingDialog(getGameActivity())
        getGameActivity().showDialog(settingDialog)
    } //========================================================
}
