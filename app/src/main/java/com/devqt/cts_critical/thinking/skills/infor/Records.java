package com.devqt.cts_critical.thinking.skills.infor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.devqt.cts_critical.thinking.skills.R;
import com.devqt.cts_critical.thinking.skills.game.PlayMath;


public class Records extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_records);

        //get text view
        TextView scoreView = (TextView)findViewById(R.id.high_scores_list);
        //get shared prefs
        SharedPreferences scorePrefs = getSharedPreferences(PlayMath.GAME_PREFS, 0);
        //get scores
        String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
        //build string
        StringBuilder scoreBuild = new StringBuilder("");
        for(String score : savedScores){
            scoreBuild.append(score+"\n");
        }
        //display scores
        scoreView.setText(scoreBuild.toString());
    }

}
