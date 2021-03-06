package com.ameteam.game.magneton;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public abstract class GameThread extends Thread {
	//Different mMode states
	public static final int STATE_LOSE = 1;
	public static final int STATE_PAUSE = 2;
	public static final int STATE_READY = 3;
	public static final int STATE_RUNNING = 4;
	public static final int STATE_WIN = 5;
	public static final int STATE_FORCE_PAUSE = 6;

	//State after force puase
	protected int mModeAux;

	//Control variable for the mode of the game (e.g. STATE_WIN)
	protected int mMode = 1;

	//Control of the actual running inside run()
	private boolean mRun = false;
		
	//The surface this thread (and only this thread) writes upon
	private SurfaceHolder mSurfaceHolder;

	//Android Context - this stores almost all we need to know
	private Context mContext;
	
	//The view
	private GameView mGameView;

	//We might want to extend this call - therefore protected
	protected int mCanvasWidth = 1;
	protected int mCanvasHeight = 1;

	//Last time we updated the game physics
	protected long mLastTime = 0;
 
	protected Bitmap mBackgroundImage;
	
	protected long score = 0;
	
	private long now;
	private float elapsed;
	

	public GameThread(GameView gameView) {		
		mGameView = gameView;
		
		mSurfaceHolder = gameView.getHolder();
		mContext = gameView.getContext();
		
		mBackgroundImage = BitmapFactory.decodeResource
							(gameView.getContext().getResources(), 
							R.drawable.background);
	}
	
	/*
	 * Called when app is destroyed, so not really that important here
	 * But if (later) the game involves more thread, we might need to stop a thread, and then we would need this
	 * Dare I say memory leak...
	 */
	public void cleanup() {		
		this.mContext = null;
		this.mGameView = null;
		this.mSurfaceHolder = null;
	}
	
	//Pre-begin a game
	abstract public void setupBeginning();
	
	//Starting up the game
	public void doStart() {
		synchronized(mSurfaceHolder) {
			setupBeginning();
			mLastTime = System.currentTimeMillis() + 100;
			setScore(0);
		}
	}
	
	//The thread start
	@Override
	public void run() {
		Canvas canvasRun;
		while (mRun) {
			if(mMode != STATE_FORCE_PAUSE){
				canvasRun = null;
				try {
					canvasRun = mSurfaceHolder.lockCanvas(null);
					synchronized (mSurfaceHolder) {
						if (mMode == STATE_RUNNING) {
							updatePhysics();
						}
						doDraw(canvasRun);
					}
				} finally {
					if (canvasRun != null) {
						if (mSurfaceHolder != null)
							mSurfaceHolder.unlockCanvasAndPost(canvasRun);
					}
				}
			}else{
				Thread.yield();
			}
		}
	}
	
	/*
	 * Surfaces and drawing
	 */
	public void setSurfaceSize(int width, int height) {
		synchronized (mSurfaceHolder) {
			mCanvasWidth = width;
			mCanvasHeight = height;

			resize(width, height);
		}
	}

	public abstract void resize(int width, int height);

	protected void doDraw(Canvas canvas) {
		
		if(canvas == null) return;

	}
	
	private void updatePhysics() {
		now = System.currentTimeMillis();
		elapsed = (now - mLastTime) / 1000.0f;

		updateGame(elapsed);

		mLastTime = now;
	}
	
	abstract protected void updateGame(float secondsElapsed);
	
	/*
	 * Control functions
	 */
	
	//Finger touches the screen
	public boolean onTouch(MotionEvent e) {
		if(e.getAction() != MotionEvent.ACTION_DOWN &&
				e.getAction() != MotionEvent.ACTION_UP &&
				e.getAction() != MotionEvent.ACTION_MOVE) return false;

		synchronized (mSurfaceHolder) {
				switch (e.getAction()) {
					case MotionEvent.ACTION_DOWN:
						this.actionOnTouch(e.getRawX(), e.getRawY());
						break;
					case MotionEvent.ACTION_UP:
						this.actionOnTouchUp(e.getRawX(), e.getRawY());
						break;
					case MotionEvent.ACTION_MOVE:
						this.actionOnTouchMove(e.getRawX(), e.getRawY());
						break;
				}
		}
		 
		return false;
	}
	
	protected void actionOnTouch(float x, float y) {
		//Override to do something
	}

	protected void actionOnTouchUp(float x, float y) {
		//Override to do something
	}

	protected void actionOnTouchMove(float x, float y) {
		//Override to do something
	}

	public abstract boolean onBackPressed();

	/*
	 * Game states
	 */
	public void pause() {
		synchronized (mSurfaceHolder) {
			if (mMode == STATE_RUNNING) setState(STATE_PAUSE);
		}
	}

	public void unpause() {
		// Move the real time clock up to now
		synchronized (mSurfaceHolder) {
			mLastTime = System.currentTimeMillis();
		}
		setState(STATE_RUNNING);
	}

	public void forcePause(){
		mModeAux = mMode;
		synchronized (mSurfaceHolder) {
			if (mMode != STATE_FORCE_PAUSE) setState(STATE_FORCE_PAUSE);
		}
	}

	public void unForcePause() {
		// Move the real time clock up to now
		synchronized (mSurfaceHolder) {
			mLastTime = System.currentTimeMillis();
		}
		setState(mModeAux);
	}

	//Send messages to View/Activity thread
	public void setState(int mode) {
		synchronized (mSurfaceHolder) {
			setState(mode, null);
		}
	}

	public void setState(int mode, CharSequence message) {
		synchronized (mSurfaceHolder) {
			mMode = mode;
			switch (mMode){
				case STATE_RUNNING:
					break;
				case STATE_READY:
					break;
				case STATE_PAUSE:
					break;
				case STATE_LOSE:
					break;
				case STATE_WIN:
					break;
			}
		}
	}
	
	/*
	 * Getter and setter
	 */	
	public void setSurfaceHolder(SurfaceHolder h) {
		mSurfaceHolder = h;
	}
	
	public boolean isRunning() {
		return mRun;
	}
	
	public void setRunning(boolean running) {
		mRun = running;
	}
	
	public int getMode() {
		return mMode;
	}

	public void setMode(int mMode) {
		this.mMode = mMode;
	}
	
	
	/* ALL ABOUT SCORES */
	
	//Send a score to the View to view 
	//Would it be better to do this inside this thread writing it manually on the screen?
	public void setScore(long score) {
		this.score = score;
		
		synchronized (mSurfaceHolder) {
			//TODO
		}
	}

	public float getScore() {
		return score;
	}
	
	public void updateScore(long score) {
		this.setScore(this.score + score);
	}
	
	
	protected CharSequence getScoreString() {
		return Long.toString(Math.round(this.score));
	}

	public GameView getmGameView() {
		return mGameView;
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