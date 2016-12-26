package com.devqt.cts_critical.thinking.skills.game;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devqt.cts_critical.thinking.skills.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShulteGame extends Activity implements View.OnClickListener{

    final private int START = 1;
    final private int END = 25;

    private ArrayList buttonList = new ArrayList<Button>();
    private ArrayList cellList = new ArrayList<>();
    private Map<Button, Integer> buttonCellMap = new HashMap<>();
    private int current;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shulte_game);


        initialize();

        int size = buttonList.size();
        for (int i = 0; i < size; ++i) {
            Button button = (Button) buttonList.get(i);
            button.setOnClickListener(this);
        }
    }

@Override
    public void onClick(View v) {
        Button button = (Button) findViewById(v.getId());

        Integer cell = buttonCellMap.get(button);
        if (cell == current) {
            button.setEnabled(false);

            if (current == END) {
                restart();
            } else {
                ++current;
            }
        }
    }

    private void restart() {
        for (int i = 0; i < 25; ++i) {
            Button button = (Button) buttonList.get(i);

            button.setEnabled(true);
        }

        reset();
    }

    private void reset() {
        Collections.shuffle(cellList);

        for (int i = 0; i < 25; ++i) {
            int cell = (int) cellList.get(i);
            Integer cellObject = new Integer(cell);
            Button button = (Button) buttonList.get(i);
            button.setText(Integer.toString(cell));
            buttonCellMap.put(button, cellObject);
        }

        current = START;
    }

    private void initialize() {
        String prefix = "button";

        for (int i = 1; i <= 25; ++i) {
            cellList.add(i);
            buttonList.add(findViewById(getResources().getIdentifier(prefix + Integer.toString(i), "id", getPackageName())));
        }

        reset();
    }
}