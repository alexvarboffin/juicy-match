package com.nativegame.juicymatch;

import com.nativegame.juicymatch.timer.LivesTimer;
import com.nativegame.natyengine.ui.GameActivity;

public class BaseActivity extends GameActivity {

    //private AdManager mAdManager;
    protected LivesTimer mLivesTimer;

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
//    public AdManager getAdManager() {
//        return mAdManager;
//    }

    public LivesTimer getLivesTimer() {
        return mLivesTimer;
    }
    //========================================================
}
