package com.devqt.cts_critical.thinking.skills.game;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devqt.cts_critical.thinking.skills.MenuGameItems;
import com.devqt.cts_critical.thinking.skills.R;

public class KrestikiNoliki extends Activity {

    private int activeplayer = 0;
    private int[] avalableplaces = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    private int[][] winning = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    private TextView TV;
    private boolean gameRunning = true;

    @Override

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.krestiki_noliki);
        TV = (TextView) findViewById(R.id.winnermsg);
        TV.setVisibility(View.INVISIBLE);
        findViewById(R.id.imageView9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KrestikiNoliki.this, MenuGameItems.class));
                finish();
            }
        });
    }


    public void dropIn(View v) {
        ImageView counter = (ImageView) v;

        int tapedCounter = Integer.parseInt(counter.getTag().toString());
        if (avalableplaces[tapedCounter] == 2 && gameRunning == true) {
            avalableplaces[tapedCounter] = activeplayer;
            counter.setTranslationY(-1000f);
            if (activeplayer == 0) {
                counter.setImageResource(R.drawable.black);
                activeplayer = 1;
            } else {
                counter.setImageResource(R.drawable.red);
                activeplayer = 0;
            }

            counter.animate().translationYBy(1000f).rotationYBy(360f).setDuration(400);

            for (int[] winningposition : winning) {
                if (avalableplaces[winningposition[0]] == avalableplaces[winningposition[1]] &&
                        avalableplaces[winningposition[1]] == avalableplaces[winningposition[2]] &&
                        avalableplaces[winningposition[0]] != 2) {
                    String winner = "";
                    gameRunning = false;
                    if (0 == avalableplaces[winningposition[0]]) {
                        winner = "black";
                    } else {
                        winner = "red";
                    }
                    final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.winningLayout);
                    TextView msg = (TextView) findViewById(R.id.winnermsg);
                    msg.setText(winner + " WINS...!");
                    Button btn = (Button) findViewById(R.id.playAgainbutton);
                    linearLayout.setVisibility(View.VISIBLE);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int[] aavalableplaces = {2, 2, 2, 2, 2, 2, 2, 2, 2};
                            avalableplaces = aavalableplaces;

                            activeplayer = 0;
                            linearLayout.setVisibility(View.INVISIBLE);
                            LinearLayout gridLayout = (LinearLayout) findViewById(R.id.gridLayout);
                            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                                LinearLayout tmp = (LinearLayout) gridLayout.getChildAt(i);
                                for (int j = 0; j < tmp.getChildCount(); j++) {
                                    ((ImageView) tmp.getChildAt(j)).setImageResource(0);
                                }
                            }
                            gameRunning = true;
                        }
                    });
                } else {
                    boolean gameisOver = true;
                    for (int x : avalableplaces) {
                        if (x == 2) {
                            gameisOver = false;
                        }
                    }
                    if (gameisOver) {
                        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.winningLayout);
                        TextView msg = (TextView) findViewById(R.id.winnermsg);
                        msg.setText("Draw");
                        msg.setAllCaps(true);
                        Button btn = (Button) findViewById(R.id.playAgainbutton);
                        linearLayout.setVisibility(View.VISIBLE);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int[] aavalableplaces = {2, 2, 2, 2, 2, 2, 2, 2, 2};
                                avalableplaces = aavalableplaces;
                                activeplayer = 0;
                                linearLayout.setVisibility(View.INVISIBLE);
                                LinearLayout gridLayout = (LinearLayout) findViewById(R.id.gridLayout);
                                for (int i = 0; i < gridLayout.getChildCount(); i++) {
                                    LinearLayout tmp = (LinearLayout) gridLayout.getChildAt(i);
                                    for (int j = 0; j < tmp.getChildCount(); j++) {
                                        ((ImageView) tmp.getChildAt(j)).setImageResource(0);
                                    }
                                }
                                gameRunning = true;
                            }
                        });
                    }
                }
            }
        }
    }
}