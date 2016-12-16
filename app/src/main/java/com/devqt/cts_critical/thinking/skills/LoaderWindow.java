package com.devqt.cts_critical.thinking.skills;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class LoaderWindow extends AppCompatActivity {

    private final int DISPLAY_LOADER = 3200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader_window);
        String fontPath = "fonts/disney.ttf";

        View view = findViewById(R.id.animView);
        AnimationDrawable animation = (AnimationDrawable) view.getBackground();
        animation.setOneShot(false);
        animation.start();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView text = (TextView) findViewById(R.id.Load);
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        text.setTypeface(typeface);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent mainIntent = new Intent(LoaderWindow.this,StartMenu.class);
                LoaderWindow.this.startActivity(mainIntent);
                LoaderWindow.this.finish();
            }
        }, DISPLAY_LOADER);
    }


}