package com.nativegame.juicymatch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nativegame.juicymatch.asset.Colors
import com.nativegame.juicymatch.asset.Fonts
import com.nativegame.juicymatch.asset.Musics0.load
import com.nativegame.juicymatch.asset.Preferences
import com.nativegame.juicymatch.asset.Sounds
import com.nativegame.juicymatch.asset.Textures
import com.nativegame.natyengine.ui.GameFragment
import com.walhalla.sdk.R

/**
 * Created by Oscar Liang on 2022/02/23
 */
class LoadingFragment  //--------------------------------------------------------
// Constructors
//--------------------------------------------------------
    : GameFragment() {
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
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(requireView(), savedInstanceState)

        Thread { // Load assets
            Textures.load(gameActivity.textureManager, context)
            Sounds.load(gameActivity.soundManager)
            load(gameActivity.musicManager)
            Fonts.load(context)
            Colors.load(context)
            Preferences.load(context)

            // Load ad
            //MobileAds.initialize(getContext());

            // Navigate to menu when loading complete
            gameActivity.runOnUiThread { gameActivity.navigateToFragment(MenuFragment()) }
        }.start()
    } //========================================================
}
