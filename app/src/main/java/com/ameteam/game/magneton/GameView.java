package com.ameteam.game.magneton;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	private volatile GameThread thread;

	private SensorEventListener sensorAccelerometer;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		//Get the holder of the screen and register interest
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

	}

	//Used to release any resources.
	public void cleanup() {
		this.thread.setRunning(false);
		this.thread.cleanup();
		
		this.removeCallbacks(thread);
		thread = null;
		
		this.setOnTouchListener(null);
		sensorAccelerometer = null;
		
		SurfaceHolder holder = getHolder();
		holder.removeCallback(this);
	}
	
	/*
	 * Setters and Getters
	 */

	public void setThread(GameThread newThread) {

		thread = newThread;

		setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if(thread!=null) {
					return thread.onTouch(event);
				}
				else return false;
			}

		});

		setClickable(true);
		setFocusable(true);
	}
	
	public GameThread getThread() {
		return thread;
	}
	
	/*
	 * Screen functions
	 */
	
	//ensure that we go into pause state if we go out of focus
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if(thread!=null) {
			Log.i("GameView", "onWindowFocusChanged");
			if (!hasWindowFocus) {
				if(thread.getMode() != GameThread.STATE_FORCE_PAUSE) {
					thread.forcePause();
				}
			}else if(thread.getMode() == GameThread.STATE_FORCE_PAUSE){
				thread.unForcePause();
			}
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.i("GameView", "surfaceCreated");
		if(thread!=null) {
			Log.i("GameView", "surfaceCreated: thread != null");
			thread.setRunning(true);

			Log.i("GameView", "surfaceCreated: state="+thread.getState());
			if(thread.getState() == Thread.State.NEW){
				Log.i("GameView", "surfaceCreated: state new");
				//Just start the new thread
				thread.doStart();
				thread.start();
			}
			else {
				if(thread.getState() == Thread.State.TERMINATED){
					//Start a new thread
					//Should be this to update screen with old game: new GameThread(this, thread);
					//The method should set all fields in new thread to the value of old thread's fields
					thread = new MagnetonGame(this);
					thread.setRunning(true);
					thread.doStart();
					thread.start();
				}
			}
			Log.i("GameView", "mode: " + thread.getMode());
			if(thread.getMode() == GameThread.STATE_FORCE_PAUSE){
				thread.unForcePause();
			}
		}
	}
	
	//Always called once after surfaceCreated. Tell the GameThread the actual size
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if(thread!=null) {
			Log.i("GameView", "surfaceChanged");
			thread.setSurfaceSize(width, height);			
		}
	}

	/*
	 * Need to stop the GameThread if the surface is destroyed
	 * Remember this doesn't need to happen when app is paused on even stopped.
	 */
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.i("GameView: ", "surfaceDestroyed");
		if(thread!=null) {
			if (thread.getMode() != GameThread.STATE_FORCE_PAUSE) {
				thread.forcePause();
			}
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
