package com.nativegame.juicymatch.ui.dialog

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.nativegame.juicymatch.asset.Sounds
import com.nativegame.juicymatch.level.Level
import com.nativegame.natyengine.ui.GameActivity
import com.nativegame.natyengine.ui.GameButton
import com.nativegame.natyengine.util.ResourceUtils
import com.walhalla.sdk.R

/**
 * Created by Oscar Liang on 2022/02/23
 */
open class TutorialDialog(activity: GameActivity) : BaseDialog(activity), View.OnClickListener {
    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    init {
        setContentView(R.layout.dialog_abc_tutorial)
        setContainerView(R.layout.dialog_abc_container_game)
        setEnterAnimationId(R.anim.enter_from_center)
        setExitAnimationId(R.anim.exit_to_center)

        // Init tutorial image
        val tutorialType = Level.LEVEL_DATA.tutorialType
        val imageTutorial = findViewById(R.id.image_tutorial) as ImageView
        imageTutorial.setImageResource(tutorialType.drawableId)

        // Init tutorial text
        val txtTutorial = findViewById(R.id.kwk_txt_tutorial) as TextView
        txtTutorial.text = ResourceUtils.getString(activity, tutorialType.stringId)

        // Init button
        val btnPlay = findViewById(R.id.btn_play) as GameButton
        btnPlay.popUp(200, 300)
        btnPlay.setOnClickListener(this)
    }

    //========================================================
    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    override fun onHide() {
        showTutorial()
    }

    override fun onClick(view: View) {
        Sounds.BUTTON_CLICK.play()
        val id = view.getId()
        if (id == R.id.btn_play) {
            dismiss()
        }
    }

    //========================================================
    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    open fun showTutorial() {
    } //========================================================
}
