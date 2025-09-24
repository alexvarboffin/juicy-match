package com.nativegame.juicymatch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import com.nativegame.juicymatch.SdkConfig
import com.nativegame.juicymatch.asset.Sounds
import com.nativegame.juicymatch.database.DatabaseHelper
import com.nativegame.juicymatch.item.Item
import com.nativegame.juicymatch.level.Level
import com.nativegame.juicymatch.ui.dialog.LevelDialog
import com.nativegame.juicymatch.ui.dialog.MoreCoinDialog
import com.nativegame.juicymatch.ui.dialog.MoreLivesDialog
import com.nativegame.juicymatch.ui.dialog.SettingDialog
import com.nativegame.juicymatch.ui.dialog.ShopDialog
import com.nativegame.juicymatch.ui.dialog.WheelDialog
import com.nativegame.loadPrivacyPolicy
import com.nativegame.natyengine.ui.GameActivity
import com.nativegame.natyengine.ui.GameButton
import com.nativegame.natyengine.ui.GameFragment
import com.nativegame.natyengine.ui.GameImage
import com.nativegame.natyengine.ui.GameText
import com.nativegame.timer.LivesTimer
import com.walhalla.sdk.R
import kotlin.math.ceil

/**
 * Created by Oscar Liang on 2022/02/23
 */
class MapFragment  //--------------------------------------------------------
// Constructors
//--------------------------------------------------------
    : GameFragment(), View.OnClickListener {
    private var mLivesTimer: LivesTimer? = null

    private var mCurrentLevel = 0
    private var mCurrentPage = 0

    //========================================================
    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webView = WebView(requireActivity()).apply {} //not set WebViewClient!!!
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLivesTimer = (gameActivity as GameActivity).livesTimer

        // Init button
        val btnSetting = view.findViewById<View?>(R.id.btn_setting) as GameButton
        btnSetting.setOnClickListener(this)

        val btnNext = view.findViewById<View?>(R.id.btn_page_next) as GameButton
        btnNext.setOnClickListener(this)

        val btnPrevious = view.findViewById<View?>(R.id.btn_page_previous) as GameButton
        btnPrevious.setOnClickListener(this)

        val btnShop = view.findViewById<View?>(R.id.btn_shop) as GameButton
        btnShop.setOnClickListener(this)

        val btnWheel = view.findViewById<View?>(R.id.btn_wheel) as GameButton
        btnWheel.setOnClickListener(this)

        val btnLives = view.findViewById<View?>(R.id.btn_lives) as GameButton
        btnLives.setOnClickListener(this)

        val btnCoin = view.findViewById<View?>(R.id.btn_coin) as GameButton
        btnCoin.setOnClickListener(this)

        // Init level button and star
        val databaseHelper = DatabaseHelper.getInstance(context)
        mCurrentLevel = databaseHelper.getAllLevelStars().size + 1
        if (mCurrentLevel > TOTAL_LEVEL) {
            mCurrentLevel = TOTAL_LEVEL
        }
        mCurrentPage = ceil(mCurrentLevel * 1.0 / LEVEL_PRE_PAGE).toInt()
        updatePage(mCurrentPage)
        loadCoin()

        // Show current level dialog
        view.postDelayed(object : Runnable {
            override fun run() {
                showLevelDialog(mCurrentLevel)
            }
        }, 800)
    }
    private lateinit var webView: WebView
    override fun onResume() {
        super.onResume()
        mLivesTimer!!.startTimer()


        webView.loadPrivacyPolicy(SdkConfig.termsUrl)
    }

    override fun onPause() {
        super.onPause()
        mLivesTimer!!.stopTimer()
    }

    override fun onBackPressed(): Boolean {
        gameActivity.navigateToFragment(MenuFragment())
        return true
    }

    override fun onClick(view: View) {
        Sounds.BUTTON_CLICK.play()
        val id = view.id
        if (id == R.id.btn_page_next) {
            if (mCurrentPage < MAX_PAGE) {
                mCurrentPage++
                updatePage(mCurrentPage)
            }
        } else if (id == R.id.btn_page_previous) {
            if (mCurrentPage > 1) {
                mCurrentPage--
                updatePage(mCurrentPage)
            }
        } else if (id == R.id.btn_shop) {
            showShopDialog()
        } else if (id == R.id.btn_wheel) {
            showWheelDialog()
        } else if (id == R.id.btn_coin) {
            showMoreCoinDialog()
        } else if (id == R.id.btn_lives) {
            if (mLivesTimer!!.livesCount < LivesTimer.MAX_LIVES) {
                showMoreLivesDialog()
            }
        } else if (id == R.id.btn_setting) {
            showSettingDialog()
        }
    }

    //========================================================
    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private fun updatePage(page: Int) {
        // Update page number text
        val currentPage = requireView().findViewById<View?>(R.id.kwk_txt_current_page) as TextView
        currentPage.text = page.toString()

        val previousPage = requireView().findViewById<View?>(R.id.kwk_txt_previous_page) as TextView
        previousPage.text = if (page == 1) "" else (page - 1).toString()

        val nextPage = requireView().findViewById<View?>(R.id.kwk_txt_next_page) as TextView
        nextPage.text = if (page == MAX_PAGE) "" else (page + 1).toString()

        // Update level button and star
        loadButton(page)
        loadStar(page)
    }

    private fun loadButton(page: Int) {
        val increment = (page - 1) * 20

        for (i in 1..LEVEL_PRE_PAGE) {
            // Init level button

            val name = "btn_level_" + i
            val id = getResources().getIdentifier(name, "id", gameActivity.packageName)
            val txtLevel = requireView().findViewById<View?>(id) as GameText

            val level = i + increment
            if (level <= mCurrentLevel) {
                txtLevel.setOnClickListener {
                    Sounds.BUTTON_CLICK.play()
                    showLevelDialog(level)
                }
                txtLevel.setBackgroundResource(R.drawable.ui_btn_level)
                txtLevel.setEnabled(true)
            } else {
                txtLevel.setOnClickListener(null)
                txtLevel.setBackgroundResource(R.drawable.ui_btn_level_lock)
                txtLevel.setEnabled(false)
            }
            txtLevel.setText(level.toString())
            txtLevel.popUp(200, (i * 30).toLong())
        }
    }

    private fun loadStar(page: Int) {
        val increment = (page - 1) * 20
        val databaseHelper = DatabaseHelper.getInstance(context)
        val stars = databaseHelper.getAllLevelStars()

        for (i in 1..LEVEL_PRE_PAGE) {
            // Init level star

            val name = "image_level_star_" + i
            val id = resources.getIdentifier(name, "id", requireActivity().packageName)
            val imageStar = requireView().findViewById<View?>(id) as GameImage

            val level = i + increment
            if (level < mCurrentLevel) {
                val star: Int = stars.get(level - 1)!!
                when (star) {
                    1 -> imageStar.setBackgroundResource(R.drawable.ui_star_set_01)
                    2 -> imageStar.setBackgroundResource(R.drawable.ui_star_set_02)
                    3 -> imageStar.setBackgroundResource(R.drawable.ui_star_set_03)
                }
                imageStar.setVisibility(View.VISIBLE)
            } else {
                imageStar.setVisibility(View.INVISIBLE)
            }
            imageStar.popUp(200, (i * 40).toLong())
        }
    }

    private fun loadCoin() {
        val textCoin = requireView().findViewById<View?>(R.id.kwk_txt_coin) as TextView
        val databaseHelper = DatabaseHelper.getInstance(context)
        val coin = databaseHelper.getItemCount(Item.COIN)
        textCoin.setText(coin.toString())
    }

    private fun showLevelDialog(level: Int) {
        // We load level data here before starting game
        Level.load(context, level)
        val levelDialog: LevelDialog = object : LevelDialog(getGameActivity()) {
            public override fun startGame() {
                // Check is player lives enough
                if (mLivesTimer!!.livesCount > 0) {
                    getGameActivity().navigateToFragment(JuicyMatchFragment())
                } else {
                    showMoreLivesDialog()
                }
            }
        }
        getGameActivity().showDialog(levelDialog)
    }

    private fun showShopDialog() {
        val shopDialog: ShopDialog = object : ShopDialog(getGameActivity()) {
            override fun updateCoin() {
                loadCoin()
            }
        }
        getGameActivity().showDialog(shopDialog)
    }

    private fun showWheelDialog() {
        val wheelDialog: WheelDialog = object : WheelDialog(getGameActivity()) {
            override fun updateCoin() {
                loadCoin()
            }
        }
        getGameActivity().showDialog(wheelDialog)
    }

    private fun showMoreCoinDialog() {
        val moreCoinDialog: MoreCoinDialog = object : MoreCoinDialog(getGameActivity()) {
            override fun updateCoin() {
                loadCoin()
            }
        }
        getGameActivity().showDialog(moreCoinDialog)
    }

    private fun showMoreLivesDialog() {
        val moreLivesDialog = MoreLivesDialog(getGameActivity())
        getGameActivity().showDialog(moreLivesDialog)
    }

    private fun showSettingDialog() {
        val settingDialog = SettingDialog(getGameActivity())
        getGameActivity().showDialog(settingDialog)
    } //========================================================

    companion object {
        private const val TOTAL_LEVEL = 100
        private const val LEVEL_PRE_PAGE = 20
        private const val MAX_PAGE = 5
    }
}
