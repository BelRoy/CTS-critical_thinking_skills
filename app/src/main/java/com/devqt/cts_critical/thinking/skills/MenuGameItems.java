package com.devqt.cts_critical.thinking.skills;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.devqt.cts_critical.thinking.skills.game.KrestikiNoliki;
import com.devqt.cts_critical.thinking.skills.game.Mathematic;
import com.devqt.cts_critical.thinking.skills.game.Memory;
import com.devqt.cts_critical.thinking.skills.game.Rulez;
import com.devqt.cts_critical.thinking.skills.game.ShulteTabs;


public class MenuGameItems extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_game_items);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById(R.id.krestiki_noliki).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuGameItems.this, KrestikiNoliki.class));
                finish();
            }
        });

        findViewById(R.id.math).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuGameItems.this, Mathematic.class));
                finish();
            }
        });

        findViewById(R.id.head_made).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuGameItems.this, Memory.class));

            }
        });
        findViewById(R.id.shulte_btn_act).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuGameItems.this, ShulteTabs.class));
                finish();
            }
        });
        findViewById(R.id.piatnaski).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuGameItems.this, Rulez.class));
            }
        });

    }

}
