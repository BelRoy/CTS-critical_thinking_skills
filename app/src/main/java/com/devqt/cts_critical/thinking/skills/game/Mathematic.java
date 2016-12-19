package com.devqt.cts_critical.thinking.skills.game;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.devqt.cts_critical.thinking.skills.R;
import com.devqt.cts_critical.thinking.skills.infor.Records;


public class Mathematic extends Activity implements View.OnClickListener {

    private Button playButton, recordButton, exitButton;

    private String[] level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        level = getResources().getStringArray(R.array.levels);

        Animation buttonAnim = AnimationUtils.loadAnimation(this, R.anim.button_anim);
        Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.text_anim);

        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(this);
        playButton.startAnimation(buttonAnim);
        recordButton = (Button) findViewById(R.id.high_button);
        recordButton.setOnClickListener(this);
        recordButton.startAnimation(buttonAnim);
        exitButton = (Button) findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        exitButton.startAnimation(buttonAnim);

        TextView infoTextView = (TextView) findViewById(R.id.intro);
        infoTextView.startAnimation(textAnim);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_button:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.level_name).setSingleChoiceItems(level,
                        0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startPlay(which);
                            }
                        });
                AlertDialog ad = builder.create();
                ad.show();
                break;
            case R.id.high_button:

                Intent highIntent = new Intent(this, Records.class);
                this.startActivity(highIntent);
                break;
            case R.id.exit_button:

                openQuitDialog();
                break;
        }
    }

    private void startPlay(int chosenLevel) {
        Intent playIntent = new Intent(this, PlayMath.class);
        playIntent.putExtra("level", chosenLevel);
        this.startActivity(playIntent);
    }

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                Mathematic.this);
        quitDialog.setTitle(R.string.dialog_title);

        quitDialog.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }


}
