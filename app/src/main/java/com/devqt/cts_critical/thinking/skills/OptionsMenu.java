package com.devqt.cts_critical.thinking.skills;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class OptionsMenu extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_options);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String fontPath = "fonts/amazing_spider_man.ttf";

        TextView text = (TextView) findViewById(R.id.mus);
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPath);
        text.setTypeface(typeface);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsMenu.this, StartMenu.class));
                finish();
            }
        });

    }

}
