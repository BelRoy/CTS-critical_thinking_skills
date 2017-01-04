package com.devqt.cts_critical.thinking.skills.game;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devqt.cts_critical.thinking.skills.R;
import com.devqt.cts_critical.thinking.skills.adapter.RulezAdap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Rulez extends Activity implements RulezAdap, OnClickListener {


    private FrameLayout mGameBoard;
    private TextView mStatusBar;


    private TileArray mTiles;
    private int mGameState;


    private int mGameBoardWidth;
    private int mGameBoardHeight;
    private int mTileWidth;
    private int mNRow, mNCol;
    private int mGameBoardBezelWidthHorizontal;
    private int mGameBoardBezelWidthVertical;
    private ImageView mUndoButton, mRedoButton;
    private ImageView mPauseButton;
    private Button mNewGameButton;
    private TextView mTvRowN;
    private TextView mTvColumnN;
    private Dialog mNewGameDialog;
    private Dialog pauseMenuDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rulez_act);


        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        mNRow = prefs.getInt(KEY_N_ROWS, GAMEBOARD_N_ROWS_DEF);
        mNCol = prefs.getInt(KEY_N_COLUMNS, GAMEBOARD_N_COLUMNS_DEF);
        int nSteps = prefs.getInt(KEY_N_STEPS, INIT_NSTEPS_DEF);
        String prevSaves = prefs.getString(KEY_SAVE_LIST, NO_PREV_GAME);

        // initialize objects

        // capture the gameboard
        mGameBoard = (FrameLayout) findViewById(R.id.gameplay_gameboard);
        // capture the status bar
        mStatusBar = (TextView) findViewById(R.id.gameplay_status_bar);
        // capture the buttons
        mUndoButton = (ImageView) findViewById(R.id.gameplay_undo_button);
        mRedoButton = (ImageView) findViewById(R.id.gameplay_redo_button);
        mPauseButton = (ImageView) findViewById(R.id.gameplay_menu_button);
        mNewGameButton = (Button) findViewById(R.id.gameplay_new_game_button);
        mNewGameButton.setOnClickListener(this);
        mUndoButton.setOnClickListener(this);
        mRedoButton.setOnClickListener(this);
        mPauseButton.setOnClickListener(this);

        // init dialog part

        initGame(mNRow, mNCol, nSteps, prevSaves);
        // initGame(4, 4, 0, null);
    }

    private void initGame(int nRows, int nColumns, int nSteps, String prevSaves) {
        // setup game specifications related to the game
        mGameState = GAME_ONGOING;

        // calculate the size and position of the game board and width of tile
        Resources res = getResources();
        int spaceHeight = res.getDisplayMetrics().heightPixels
                - res.getDimensionPixelSize(R.dimen.dp_system_top_bar_height)
                - res.getDimensionPixelSize(R.dimen.dp_gameplay_status_bar_height)
                - res.getDimensionPixelSize(R.dimen.dp_gameplay_bottom_button_bar_height)
                - 2
                * res.getDimensionPixelSize(R.dimen.dp_gameboard_top_bottom_margin);
        int spaceWidth = res.getDisplayMetrics().widthPixels
                - 2
                * res.getDimensionPixelSize(R.dimen.dp_gameboard_left_right_margin);
        int spaceLeftMargin = res
                .getDimensionPixelSize(R.dimen.dp_gameboard_left_right_margin), spaceTopMargin = res
                .getDimensionPixelSize(R.dimen.dp_gameplay_status_bar_height)
                + res.getDimensionPixelSize(R.dimen.dp_gameboard_top_bottom_margin);

        RelativeLayout.LayoutParams gameBoardLayoutParams = (RelativeLayout.LayoutParams) mGameBoard
                .getLayoutParams();
        double percentageAvailableWidthForTiles = 1 - 2
                * res.getDimension(R.dimen.px_gameboard_bezel_width)
                / res.getDimension(R.dimen.px_gameboard_width);
        double percentageAvailableHeightForTiles = 1 - 2
                * res.getDimension(R.dimen.px_gameboard_bezel_width)
                / res.getDimension(R.dimen.px_gameboard_height);
        int availableWidthForTiles = (int) (spaceWidth * percentageAvailableWidthForTiles);
        int availableHeightForTiles = (int) (spaceHeight * percentageAvailableHeightForTiles);
        int availableTileWidth = Math.min(availableWidthForTiles / nColumns,
                availableHeightForTiles / nRows);

        mTileWidth = Math.min(availableTileWidth,
                res.getDimensionPixelSize(R.dimen.dp_tile_max_width));
        mGameBoardWidth = (int) (mTileWidth * nColumns / percentageAvailableWidthForTiles);
        mGameBoardBezelWidthHorizontal = (mGameBoardWidth - mTileWidth
                * nColumns) / 2;
        mGameBoardHeight = (int) (mTileWidth * nRows / percentageAvailableHeightForTiles);
        mGameBoardBezelWidthVertical = (mGameBoardHeight - mTileWidth * nRows) / 2;

        // set gameboard size and position
        gameBoardLayoutParams.height = mGameBoardHeight;
        gameBoardLayoutParams.width = mGameBoardWidth;
        gameBoardLayoutParams.topMargin = (spaceHeight - mGameBoardHeight) / 2
                + spaceTopMargin;
        gameBoardLayoutParams.leftMargin = (spaceWidth - mGameBoardWidth) / 2
                + spaceLeftMargin;
        mGameBoard.setPadding(mGameBoardBezelWidthHorizontal,
                mGameBoardBezelWidthVertical, mGameBoardBezelWidthHorizontal,
                mGameBoardBezelWidthVertical);

        // create and initialise all the content on gameboard
        mTiles = new TileArray(nRows, nColumns, nSteps, prevSaves);

        // update everything
        updateAll();
    }

    class TileArray extends ArrayList<TextView> implements OnTouchListener {
        private int emptyPos;
        private int nRows, nColumns, gridSize;
        private int nSteps;
        private MarginRange mTouchedTileMargin;
        private SaveList mSaves;

        public TileArray(int nRows, int nColumns) {
            this(nRows, nColumns, 0, NO_PREV_GAME);
        }

        public TileArray(int nRows, int nColumns, int nSteps, String prevSaves) {
            super();
            emptyPos = nRows * nColumns - 1;
            this.nRows = nRows;
            this.nColumns = nColumns;
            gridSize = nRows * nColumns;
            this.nSteps = nSteps;

            // draw background for the tiles
            for (int i = 0; i < gridSize; i++) {
                View v = getLayoutInflater().inflate(R.layout.empty_slot,
                        mGameBoard, false);
                FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(
                        mTileWidth, mTileWidth);
                par.gravity = Gravity.TOP;
                par.leftMargin = supposedLeftMargin(i);
                par.topMargin = supposedTopMargin(i);
                mGameBoard.addView(v, par);
            }

            // initialize all the tiles
            for (int i = 1; i < gridSize; i++) {
                // draw tile
                TextView t = (TextView) getLayoutInflater().inflate(
                        R.layout.tile, mGameBoard, false);
                FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(
                        mTileWidth, mTileWidth);
                par.gravity = Gravity.TOP;
                mGameBoard.addView(t, par);
                // set text, tag, ontouchlistener
                t.setText("" + i);
                t.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        mTileWidth
                                - getResources()
                                .getDimension(
                                        R.dimen.px_difference_between_tile_width_n_tile_number));
                t.setTag(i);
                t.setOnTouchListener(this);

                // add to the list
                add(t);
            }
            TextView empty = new TextView(Rulez.this);
            empty.setTag(EMPTY_TAG_IDENTIFIER);
            add(empty);

            // initialize control parameters
            mTouchedTileMargin = new MarginRange();

            // initialize save load
            mSaves = new SaveList(prevSaves);

            if (prevSaves == NO_PREV_GAME) {
                // randomize the array if necessary
                randomize();
                // save the initial state
                mSaves.add(new TileState());
            } else {
                load(mSaves.get(nSteps));
            }

        }

        public void shift(int pos) {
            Collections.swap(this, pos, emptyPos);
            emptyPos = pos;
        }

        private void randomize() {
            Random rgen = new Random();
            for (int i = 0; i < RANDOMIZE_MULTIPLIER; i++)
                shift(movablePos().get(rgen.nextInt(movablePos().size())));
        }

        private ArrayList<Integer> movablePos() {
            ArrayList<Integer> movablePos = new ArrayList<Integer>();
            if (hasTileOnLeft())
                movablePos.add(emptyPos - 1);
            if (hasTileOnRight())
                movablePos.add(emptyPos + 1);
            if (hasTileAbove())
                movablePos.add(emptyPos - nColumns);
            if (hasTileBelow())
                movablePos.add(emptyPos + nColumns);
            return movablePos;
        }

        private boolean hasTileBelow() {
            return emptyPos < nColumns * (nRows - 1);
        }

        private boolean hasTileAbove() {
            return emptyPos >= nColumns;
        }

        private boolean hasTileOnRight() {
            return ((emptyPos + 1) % nColumns) != 0;
        }

        private boolean hasTileOnLeft() {
            return emptyPos % nColumns != 0;
        }

        private int relativePosOfTile(int vPos) {
            if (vPos == emptyPos - 1 && hasTileOnLeft())
                return TILE_ON_LEFT;
            else if (vPos == emptyPos + 1 && hasTileOnRight())
                return TILE_ON_RIGHT;
            else if (vPos == emptyPos - nColumns && hasTileAbove())
                return TILE_ABOVE;
            else if (vPos == emptyPos + nColumns && hasTileBelow())
                return TILE_BELOW;
            else
                return TILE_NOT_AROUND;
        }

        public void updateDisplay() {
            for (int i = 0; i < size(); i++) {
                if (i != emptyPos) {
                    FrameLayout.LayoutParams tvLParams = (FrameLayout.LayoutParams) get(
                            i).getLayoutParams();
                    tvLParams.leftMargin = supposedLeftMargin(i);
                    tvLParams.topMargin = supposedTopMargin(i);
                    get(i).setLayoutParams(tvLParams);
                }
            }
        }

        private int supposedLeftMargin(int pos) {
            return (pos % nColumns) * mTileWidth;
        }

        private int supposedTopMargin(int pos) {
            return (pos / nColumns) * mTileWidth;
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (mGameState != GAME_ONGOING)
                return false;
            final int vPos = indexOf(v);
            if (relativePosOfTile(vPos) == TILE_NOT_AROUND)
                return false;

            FrameLayout.LayoutParams vPar = (LayoutParams) v.getLayoutParams();
            int vLM = vPar.leftMargin;
            int vTM = vPar.topMargin;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // define the available space for moving: a range of margin
                    // for the tile
                    int emptyLeftMargin = supposedLeftMargin(emptyPos);
                    int emptyTopMargin = supposedTopMargin(emptyPos);
                    mTouchedTileMargin.leftOriginal = vLM;
                    mTouchedTileMargin.topOriginal = vTM;
                    mTouchedTileMargin.leftFinal = emptyLeftMargin;
                    mTouchedTileMargin.topFinal = emptyTopMargin;
                    mTouchedTileMargin.leftMin = Math.min(vLM, emptyLeftMargin);
                    mTouchedTileMargin.leftMax = Math.max(vLM, emptyLeftMargin);
                    mTouchedTileMargin.topMin = Math.min(vTM, emptyTopMargin);
                    mTouchedTileMargin.topMax = Math.max(vTM, emptyTopMargin);
                    break;
                case MotionEvent.ACTION_MOVE:
                    // translate the moving
                    vPar.leftMargin = lmAllowed((int) event.getX() + vLM);
                    vPar.topMargin = tmAllowed((int) event.getY() + vTM);
                    v.setLayoutParams(vPar);
                    break;
                case MotionEvent.ACTION_UP:
                    // check to see whether the tile has crossed to the new position
                    if (Math.abs(vLM - mTouchedTileMargin.leftOriginal) > mTouchedTileMargin
                            .lmRange() * MOVED_ACROSS_PERCENTAGE
                            || Math.abs(vTM - mTouchedTileMargin.topOriginal) > mTouchedTileMargin
                            .tmRange() * MOVED_ACROSS_PERCENTAGE) {
                        // success move
                        shift(vPos);
                        // record present move
                        save();
                        if (hasWon()) {
                            mGameState = GAME_WON;
                        }

                    }

                    updateAll();
                    break;
            }
            return true;
        }

        private boolean hasWon() {
            if (emptyPos != gridSize - 1)
                return false;
            boolean hasWon = true;
            for (int i = 0; i < size() - 1; i++) {
                if ((Integer) get(i).getTag() != i + 1)
                    hasWon = false;
            }
            return hasWon;
        }

        private int tmAllowed(int givenTM) {
            if (givenTM < mTouchedTileMargin.topMin)
                return mTouchedTileMargin.topMin;
            else if (givenTM > mTouchedTileMargin.topMax)
                return mTouchedTileMargin.topMax;
            else
                return givenTM;
        }

        private int lmAllowed(int givenLM) {
            if (givenLM < mTouchedTileMargin.leftMin)
                return mTouchedTileMargin.leftMin;
            else if (givenLM > mTouchedTileMargin.leftMax)
                return mTouchedTileMargin.leftMax;
            else
                return givenLM;
        }

        private void save() {
            if (nSteps < mSaves.size() - 1)
                mSaves.removeRange(nSteps + 1, mSaves.size());
            else if (nSteps >= mSaves.size())
                Log.d("CA", "no. of steps greater than no. of saved states");
            mSaves.add(new TileState());
            nSteps++;
        }

        private void load(TileState ts) {
            for (int i = 0; i < gridSize; i++) {
                if (ts.tilePos[i] == EMPTY_TAG_IDENTIFIER)
                    emptyPos = i;
                int pos = posOfValue(ts.tilePos[i]);
                if (pos == NULL_TAG_IDENTIFIER)
                    Log.d("CA", "cannot find the tile with specified value");
                else {
                    Collections.swap(this, pos, i);
                }
            }
        }

        public String saveListInString() {
            return mSaves.toString();
        }

        private int posOfValue(int value) {
            for (int i = 0; i < size(); i++) {
                if (((Integer) get(i).getTag()).intValue() == value)
                    return i;
            }
            return NULL_TAG_IDENTIFIER;
        }

        public void undo() {
            if (!cannotUndo()) {
                nSteps--;
                load(mSaves.get(nSteps));
            }

        }

        public void redo() {
            if (!cannotRedo()) {
                nSteps++;
                load(mSaves.get(nSteps));
            }
        }

        public boolean cannotUndo() {
            return nSteps == 0;
        }

        public boolean cannotRedo() {
            return nSteps >= mSaves.size() - 1;
        }

        class MarginRange {
            public int leftMin = 0, leftMax = 0, topMin = 0, topMax = 0,
                    leftOriginal = 0, topOriginal = 0, leftFinal = 0,
                    topFinal = 0;

            public int lmRange() {
                return leftMax - leftMin;
            }

            public int tmRange() {
                return topMax - topMin;
            }
        }

        class TileState {
            private int[] tilePos;
            private String strCode = "";

            public TileState() {
                this(SAVE_STATE_CURRENT);
            }

            public TileState(String prevState) {
                tilePos = new int[gridSize];
                if (prevState == SAVE_STATE_CURRENT) {
                    for (int i = 0; i < gridSize; i++) {
                        tilePos[i] = ((Integer) TileArray.this.get(i).getTag())
                                .intValue();
                        strCode += tilePos[i] + ""
                                + SAVE_DATA_TILE_VALUE_INTERVAL;
                    }
                } else {
                    strCode = prevState;
                    int pos = prevState.indexOf(SAVE_DATA_TILE_VALUE_INTERVAL), lb = 0;
                    for (int i = 0; i < gridSize; i++) {
                        tilePos[i] = Integer.parseInt(prevState.substring(lb,
                                pos));
                        lb = pos + 1;
                        pos = prevState.indexOf(SAVE_DATA_TILE_VALUE_INTERVAL,
                                lb);
                    }
                }
            }

            @Override
            public String toString() {
                return strCode;
            }
        }

        class SaveList extends ArrayList<TileState> {
            String strCode = "";

            public SaveList() {
                this(NO_PREV_GAME);
            }

            public SaveList(String prevSaves) {
                super();
                if (prevSaves != NO_PREV_GAME) {
                    strCode = prevSaves;
                    int pos = prevSaves.indexOf(SAVE_DATA_TILE_STATE_INTERVAL), lb = 0;
                    while (pos != -1) {
                        super.add(new TileState(prevSaves.substring(lb, pos)));
                        lb = pos + 1;
                        pos = prevSaves.indexOf(SAVE_DATA_TILE_STATE_INTERVAL,
                                lb);
                    }
                }
            }

            @Override
            public boolean add(TileState object) {
                strCode += object.toString() + SAVE_DATA_TILE_STATE_INTERVAL;
                return super.add(object);
            }

            @Override
            public TileState remove(int location) {
                TileState ts = super.remove(location);
                rewriteToString();
                return ts;
            }

            @Override
            public boolean remove(Object object) {
                boolean b = super.remove(object);
                if (!b)
                    return false;
                rewriteToString();
                return true;
            }

            @Override
            public void removeRange(int start, int end) {
                super.removeRange(start, end);
                rewriteToString();
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                boolean b = super.removeAll(collection);
                if (!b)
                    return false;
                strCode = "";
                return true;
            }

            @Override
            public String toString() {
                return strCode;
            }

            public void rewriteToString() {
                strCode = "";
                for (TileState ts : this) {
                    strCode += ts.toString() + SAVE_DATA_TILE_STATE_INTERVAL;
                }
            }

        }

    }

    private void updateStatusBar() {
        if (mGameState == GAME_ONGOING) {
            mStatusBar.setText(getString(
                    R.string.game_play_status_bar_step_format, mTiles.nSteps));
        } else if (mGameState == GAME_WON) {
            mStatusBar.setText(R.string.game_play_status_bar_win);
        }
    }

    public void updateAll() {
        mTiles.updateDisplay();
        updateStatusBar();
        updateButtonRow();
    }

    private void updateButtonRow() {
        if (mTiles.cannotUndo() || mGameState != GAME_ONGOING)
            mUndoButton.setClickable(false);
        else
            mUndoButton.setClickable(true);
        if (mTiles.cannotRedo() || mGameState != GAME_ONGOING)
            mRedoButton.setClickable(false);
        else
            mRedoButton.setClickable(true);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gameplay_undo_button:
                mTiles.undo();
                updateAll();
                break;
            case R.id.gameplay_redo_button:
                mTiles.redo();
                updateAll();
                break;
            case R.id.gameplay_menu_button:
                if (mGameState == GAME_ONGOING)
                    mGameState = GAME_PAUSE;
                showDialog(DIALOG_ID_GAME_PLAY_PAUSE_DIALOG);
                updateAll();
                break;
            case R.id.gameplay_new_game_button:
                if (mGameState == GAME_ONGOING)
                    mGameState = GAME_PAUSE;
                showDialog(DIALOG_ID_NEW_GAME_DIALOG);
                updateAll();
                break;
            case R.id.new_game_row_left_button:
                if (mNRow > GAMEBOARD_N_ROWS_MIN) {
                    mNRow--;
                    mTvRowN.setText("" + mNRow);
                    initGame(mNRow, mNCol, 0, null);
                }
                break;
            case R.id.new_game_row_right_button:
                if (mNRow < GAMEBOARD_N_ROWS_MAX) {
                    mNRow++;
                    mTvRowN.setText("" + mNRow);
                    initGame(mNRow, mNCol, 0, null);
                }
                break;
            case R.id.new_game_column_left_button:
                if (mNCol > GAMEBOARD_N_COLUMNS_MIN) {
                    mNCol--;
                    mTvColumnN.setText("" + mNCol);
                    initGame(mNRow, mNCol, 0, null);
                }
                break;
            case R.id.new_game_column_right_button:
                if (mNCol < GAMEBOARD_N_COLUMNS_MAX) {
                    mNCol++;
                    mTvColumnN.setText("" + mNCol);
                    initGame(mNRow, mNCol, 0, null);
                }
                break;
            case R.id.new_game_enter:
                mNewGameDialog.dismiss();
                break;
            case R.id.pause_menu_background_left_button:
                changeBackground(false);
                break;
            case R.id.pause_menu_background_right_button:
                changeBackground(true);
                break;
            case R.id.pause_menu_resume:
                pauseMenuDialog.dismiss();
                break;
            case R.id.pause_menu_exit:
                Rulez.this.finish();
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_N_ROWS, mTiles.nRows)
                .putInt(KEY_N_COLUMNS, mTiles.nColumns)
                .putInt(KEY_N_STEPS, mTiles.nSteps)
                .putString(KEY_SAVE_LIST, mTiles.saveListInString()).commit();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ID_NEW_GAME_DIALOG:
                mNewGameDialog = new Dialog(this, R.style.NewGameDialog);
                mNewGameDialog.setContentView(R.layout.new_game_menu);
                mNewGameDialog
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {

                            public void onDismiss(DialogInterface dialog) {
                                if (mGameState == GAME_PAUSE)
                                    mGameState = GAME_ONGOING;
                                updateAll();
                            }
                        });
                mTvRowN = (TextView) mNewGameDialog
                        .findViewById(R.id.new_game_menu_row_number);
                mTvColumnN = (TextView) mNewGameDialog
                        .findViewById(R.id.new_game_menu_column_number);
                mTvRowN.setText("" + mNRow);
                mTvColumnN.setText("" + mNCol);
                ((ImageView) mNewGameDialog
                        .findViewById(R.id.new_game_row_left_button))
                        .setOnClickListener(this);
                ((ImageView) mNewGameDialog
                        .findViewById(R.id.new_game_row_right_button))
                        .setOnClickListener(this);
                ((ImageView) mNewGameDialog
                        .findViewById(R.id.new_game_column_left_button))
                        .setOnClickListener(this);
                ((ImageView) mNewGameDialog
                        .findViewById(R.id.new_game_column_right_button))
                        .setOnClickListener(this);
                ((ImageView) mNewGameDialog.findViewById(R.id.new_game_enter))
                        .setOnClickListener(this);

                return mNewGameDialog;
            case DIALOG_ID_GAME_PLAY_PAUSE_DIALOG:
                pauseMenuDialog = new Dialog(this, R.style.PauseDialog);
                pauseMenuDialog.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                pauseMenuDialog.getWindow().getAttributes().dimAmount = (float) 0.5;
                pauseMenuDialog.setContentView(R.layout.game_play_pause_menu);
                pauseMenuDialog
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {

                            public void onDismiss(DialogInterface dialog) {
                                if (mGameState == GAME_PAUSE)
                                    mGameState = GAME_ONGOING;
                                updateAll();
                            }
                        });
                ((ImageView) pauseMenuDialog
                        .findViewById(R.id.pause_menu_background_left_button))
                        .setOnClickListener(this);
                ((ImageView) pauseMenuDialog
                        .findViewById(R.id.pause_menu_background_right_button))
                        .setOnClickListener(this);
                ((ImageView) pauseMenuDialog.findViewById(R.id.pause_menu_resume))
                        .setOnClickListener(this);
                ((ImageView) pauseMenuDialog.findViewById(R.id.pause_menu_exit))
                        .setOnClickListener(this);
                return pauseMenuDialog;
            default:
                return super.onCreateDialog(id);
        }
    }

    private int backgroundNo = 0;

    private void changeBackground(boolean forward) {
        if (forward && backgroundNo < BACKGROUND_RES.length - 1) {
            backgroundNo++;
            ((RelativeLayout) findViewById(R.id.gameplay_base_layout))
                    .setBackgroundResource(BACKGROUND_RES[backgroundNo]);
        } else if ((!forward) && backgroundNo > 0) {
            backgroundNo--;
            ((RelativeLayout) findViewById(R.id.gameplay_base_layout))
                    .setBackgroundResource(BACKGROUND_RES[backgroundNo]);

        }
        Log.d("CA", "backgroundNo = " + backgroundNo);
    }

}