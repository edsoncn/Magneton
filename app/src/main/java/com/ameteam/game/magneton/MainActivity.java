package com.ameteam.game.magneton;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class MainActivity extends ActionBarActivity {

    private static final int MENU_RESUME = 1;
    private static final int MENU_START = 2;
    private static final int MENU_STOP = 3;

    private GameThread mGameThread;
    private GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mGameView = (GameView)findViewById(R.id.gamearea);
        this.startGame(mGameView, null, savedInstanceState);
    }

    private void startGame(GameView gView, GameThread gThread, Bundle savedInstanceState) {

        //Set up a new game, we don't care about previous states
        mGameThread = new MagnetonGame(mGameView);
        mGameView.setThread(mGameThread);
        mGameThread.setState(GameThread.STATE_READY);
    }


    @Override
    protected void onStop() {
        Log.i("MainActivity", "onStop");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.i("MainActivity", "onResume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.i("MainActivity", "onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i("MainActivity", "onRestart");
        super.onRestart();
    }

	/*
	 * Activity state functions
	 */

    @Override
    protected void onPause() {
        Log.i("MainActivity", "onPause");
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        Log.i("MainActivity", "onDestroy");
        super.onDestroy();

        mGameView.cleanup();
        mGameThread = null;
        mGameView = null;
    }


    @Override
    public void onBackPressed() {
        if(mGameThread != null){
            if(mGameThread.onBackPressed()){
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }

}

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
//
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.
