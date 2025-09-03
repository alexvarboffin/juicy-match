package com.nativegame.juicymatch.ui.dialog

import android.view.View
import android.widget.TextView
import com.nativegame.juicymatch.R
import com.nativegame.juicymatch.asset.Sounds
import com.nativegame.juicymatch.database.DatabaseHelper
import com.nativegame.juicymatch.level.Level
import com.nativegame.natyengine.ui.GameActivity
import com.nativegame.natyengine.ui.GameButton
import com.nativegame.natyengine.ui.GameImage
import com.nativegame.natyengine.ui.GameText
import com.nativegame.natyengine.util.ResourceUtils

/**
 * Created by Oscar Liang on 2022/02/23
 */
open class LevelDialog(activity: GameActivity) : BaseDialog(activity), View.OnClickListener {
    private var mSelectedId = 0

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    init {
        setContentView(R.layout.dialog_level)
        setContainerView(R.layout.dialog_container)
        setEnterAnimationId(R.anim.enter_from_center)
        setExitAnimationId(R.anim.exit_to_center)

        // Init level text
        val txtLevel = findViewById(R.id.txt_level) as TextView
        txtLevel.setText(
            ResourceUtils.getString(
                activity,
                R.string.txt_level,
                Level.LEVEL_DATA.level
            )
        )

        // Init button
        val btnPlay = findViewById(R.id.btn_play) as GameButton
        btnPlay.popUp(200, 700)
        btnPlay.setOnClickListener(this)

        val btnCancel = findViewById(R.id.btn_cancel) as GameButton
        btnCancel.setOnClickListener(this)

        initStar()
        initTargetImage()
        initTargetText()
    }

    //========================================================
    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    override fun onHide() {
        if (mSelectedId == R.id.btn_play) {
            startGame()
        }
    }

    override fun onClick(view: View) {
        Sounds.BUTTON_CLICK.play()
        val id = view.getId()
        if (id == R.id.btn_cancel) {
            dismiss()
        } else if (id == R.id.btn_play) {
            mSelectedId = id
            dismiss()
        }
    }

    //========================================================
    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private fun initStar() {
        val level = Level.LEVEL_DATA.level
        // Init star image from current level star
        val imageStar = findViewById(R.id.image_star) as GameImage
        imageStar.popUp(200, 200)
        val databaseHelper = DatabaseHelper.getInstance(mParent)
        val star = databaseHelper.getLevelStar(level)
        if (star != -1) {
            when (star) {
                1 -> imageStar.setImageResource(R.drawable.ui_star_set_01)
                2 -> imageStar.setImageResource(R.drawable.ui_star_set_02)
                3 -> imageStar.setImageResource(R.drawable.ui_star_set_03)
            }
        }
    }

    private fun initTargetImage() {
        val targetTypes = Level.LEVEL_DATA.targetTypes
        // Init target image from TargetType
        val imageTargetA = findViewById(R.id.image_target_01) as GameImage
        val imageTargetB = findViewById(R.id.image_target_02) as GameImage
        val imageTargetC = findViewById(R.id.image_target_03) as GameImage
        when (targetTypes.size) {
            1 -> {
                //imageTargetB.setImageResource(targetTypes.get(0).getDrawableId())
                imageTargetB.popUp(200, 300)
                imageTargetA.setVisibility(View.GONE)
                imageTargetB.setVisibility(View.VISIBLE)
                imageTargetC.setVisibility(View.GONE)
            }

            2 -> {
                imageTargetA.setImageResource(targetTypes.get(0)!!.getDrawableId())
                imageTargetC.setImageResource(targetTypes.get(1)!!.getDrawableId())
                imageTargetA.popUp(200, 300)
                imageTargetC.popUp(200, 400)
                imageTargetA.setVisibility(View.VISIBLE)
                imageTargetB.setVisibility(View.GONE)
                imageTargetC.setVisibility(View.VISIBLE)
            }

            3 -> {
                imageTargetA.setImageResource(targetTypes.get(0)!!.getDrawableId())
                imageTargetB.setImageResource(targetTypes.get(1)!!.getDrawableId())
                imageTargetC.setImageResource(targetTypes.get(2)!!.getDrawableId())
                imageTargetA.popUp(200, 300)
                imageTargetB.popUp(200, 400)
                imageTargetC.popUp(200, 500)
                imageTargetA.setVisibility(View.VISIBLE)
                imageTargetB.setVisibility(View.VISIBLE)
                imageTargetC.setVisibility(View.VISIBLE)
            }
        }
    }

    private fun initTargetText() {
        val targetTypes = Level.LEVEL_DATA.targetCounts
        // Init target image from TargetType
        val txtTargetA = findViewById(R.id.txt_target_01) as GameText
        val txtTargetB = findViewById(R.id.txt_target_02) as GameText
        val txtTargetC = findViewById(R.id.txt_target_03) as GameText
        when (targetTypes.size) {
            1 -> {
                txtTargetB.setText(targetTypes.get(0).toString())
                txtTargetB.popUp(200, 300)
                txtTargetA.setVisibility(View.GONE)
                txtTargetB.setVisibility(View.VISIBLE)
                txtTargetC.setVisibility(View.GONE)
            }

            2 -> {
                txtTargetA.setText(targetTypes.get(0).toString())
                txtTargetC.setText(targetTypes.get(1).toString())
                txtTargetA.popUp(200, 300)
                txtTargetC.popUp(200, 400)
                txtTargetA.setVisibility(View.VISIBLE)
                txtTargetB.setVisibility(View.GONE)
                txtTargetC.setVisibility(View.VISIBLE)
            }

            3 -> {
                txtTargetA.setText(targetTypes.get(0).toString())
                txtTargetB.setText(targetTypes.get(1).toString())
                txtTargetC.setText(targetTypes.get(2).toString())
                txtTargetA.popUp(200, 300)
                txtTargetB.popUp(200, 400)
                txtTargetC.popUp(200, 500)
                txtTargetA.setVisibility(View.VISIBLE)
                txtTargetB.setVisibility(View.VISIBLE)
                txtTargetC.setVisibility(View.VISIBLE)
            }
        }
    }

    open fun startGame() {
    } //========================================================
}
