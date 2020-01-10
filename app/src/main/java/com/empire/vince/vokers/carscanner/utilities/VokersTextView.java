package com.empire.vince.vokers.carscanner.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by VinceGee on 08/27/2016.
 */
public class VokersTextView extends TextView {

    public VokersTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public VokersTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VokersTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Roboto.ttf");
            setTypeface(tf);
        }
    }

}
