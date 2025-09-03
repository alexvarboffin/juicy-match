package com.nativegame.juicymatch.asset;

import android.content.Context;
import android.graphics.Typeface;

import com.nativegame.natyengine.util.ResourceUtils;
import com.walhalla.sdk.R;

/**
 * Created by Oscar Liang on 2022/02/23
 */

public class Fonts {

    public static Typeface BALOO;

    //--------------------------------------------------------
    // Static methods
    //--------------------------------------------------------
    public static void load(Context context) {
        BALOO = ResourceUtils.getFont(context, R.font.baloo);
    }
    //========================================================

}
