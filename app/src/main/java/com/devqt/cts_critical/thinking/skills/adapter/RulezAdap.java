package com.devqt.cts_critical.thinking.skills.adapter;

import com.devqt.cts_critical.thinking.skills.R;

public interface RulezAdap {


	public static final int[] BACKGROUND_RES = { R.drawable.card_table_texture,
			R.drawable.background_android, R.drawable.background_woman,
			R.drawable.background_sunset };


	public static final int GAMEBOARD_N_ROWS = 4, GAMEBOARD_N_ROWS_MAX = 5,
			GAMEBOARD_N_ROWS_DEF = 4, GAMEBOARD_N_ROWS_MIN = 2;
	public static final int GAMEBOARD_N_COLUMNS = 4,
			GAMEBOARD_N_COLUMNS_DEF = 4, GAMEBOARD_N_COLUMNS_MAX = 5,
			GAMEBOARD_N_COLUMNS_MIN = 2;

	public static final int INIT_NSTEPS_DEF = 0;

	public static final String NO_PREV_GAME = null;

	public static final String SAVE_STATE_CURRENT = null;

	public static final int RANDOMIZE_MULTIPLIER = 1000;


	public static final int GAME_ONGOING = 0;
	public static final int GAME_PAUSE = 1;
	public static final int GAME_WON = 2;
	public static final int EMPTY_TAG_IDENTIFIER = -1;
	public static final int NULL_TAG_IDENTIFIER = -100;


	public static final int TILE_ON_LEFT = 1;
	public static final int TILE_ON_RIGHT = 2;
	public static final int TILE_ABOVE = 3;
	public static final int TILE_BELOW = 4;
	public static final int TILE_NOT_AROUND = -1;


	public static final double MOVED_ACROSS_PERCENTAGE = 0.3;


	public static final char SAVE_DATA_TILE_VALUE_INTERVAL = ' ';
	public static final char SAVE_DATA_TILE_STATE_INTERVAL = ';';


	public static final String KEY_N_ROWS = "KEY_N_ROWS";
	public static final String KEY_N_COLUMNS = "KEY_N_COLUMNS";
	public static final String KEY_N_STEPS = "KEY_N_STEPS";
	public static final String KEY_SAVE_LIST = "KEY_SAVE_LIST";


	public static final int DIALOG_ID_GAME_PLAY_PAUSE_DIALOG = 0;
	public static final int DIALOG_ID_NEW_GAME_DIALOG = 1;

}
