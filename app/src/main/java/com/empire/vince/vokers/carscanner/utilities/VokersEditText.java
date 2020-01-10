package com.empire.vince.vokers.carscanner.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by VinceGee on 09/17/2016.
 */
public class VokersEditText extends EditText {

    public VokersEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public VokersEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VokersEditText(Context context) {
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
