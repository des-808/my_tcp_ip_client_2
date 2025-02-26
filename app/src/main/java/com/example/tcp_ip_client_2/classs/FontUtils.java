package com.example.tcp_ip_client_2.classs;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontUtils {
    private Context context;

    public FontUtils(Context context) {
        this.context = context;
    }
    public AssetManager getAssets() {
        return context.getAssets();
    }
    //setTypeface() Typeface.BOLD Typeface.ITALIC  Typeface.NORMAL

    public void setFont(TextView textView, String fontName) {
        Typeface type = Typeface.createFromAsset(getAssets(), fontName);
        textView.setTypeface(type);
    }

    public void setFont(TextView textView, String fontName, int style) {
        Typeface type = Typeface.createFromAsset(getAssets(), fontName);
        textView.setTypeface(type, style);
    }

    public void setFont(TextView textView, String fontName, int style, int color) {
        Typeface type = Typeface.createFromAsset(getAssets(), fontName);
        textView.setTypeface(type, style);
        textView.setTextColor(color);
    }

    public void setFont(TextView textView, String fontName, int style, int color, int size) {
        Typeface type = Typeface.createFromAsset(getAssets(), fontName);
        textView.setTypeface(type, style);
        textView.setTextColor(color);
        textView.setTextSize(size);
    }

    public void setFont(TextView textView, String fontName, int style, int color, int size, int spacing) {
        Typeface type = Typeface.createFromAsset(getAssets(), fontName);
        textView.setTypeface(type, style);
        textView.setTextColor(color);
        textView.setTextSize(size);
        textView.setLineSpacing(spacing, 1.0f);
    }

    public void setFont(TextView textView, String fontName, int style, int color, int size, int spacing, int lineHeight) {
        Typeface type = Typeface.createFromAsset(getAssets(), fontName);
        textView.setTypeface(type, style);
        textView.setTextColor(color);
        textView.setTextSize(size);
        textView.setLineSpacing(spacing, lineHeight);
    }

    public void setFont(TextView textView, String fontName, int style, int color, int size, int spacing, int lineHeight, int letterSpacing) {
        Typeface type = Typeface.createFromAsset(getAssets(), fontName);
        textView.setTypeface(type, style);
        textView.setTextColor(color);
        textView.setTextSize(size);
        textView.setLineSpacing(spacing, lineHeight);
        textView.setLetterSpacing(letterSpacing);
    }

    public void setFont(TextView textView, String fontName, int style, int color, int size, int spacing, int lineHeight, int letterSpacing, int shadow) {
        Typeface type = Typeface.createFromAsset(getAssets(), fontName);
        textView.setTypeface(type, style);
        textView.setTextColor(color);
        textView.setTextSize(size);
        textView.setLineSpacing(spacing, lineHeight);
        textView.setLetterSpacing(letterSpacing);
        textView.setShadowLayer(shadow, 0, 0, Color.BLACK);
    }
}
