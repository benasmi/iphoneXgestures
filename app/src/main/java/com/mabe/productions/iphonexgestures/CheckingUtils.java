package com.mabe.productions.iphonexgestures;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Benas on 11/5/2017.
 */

public class CheckingUtils {

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.getDisplayMetrics());

    }
}
