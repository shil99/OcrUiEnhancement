package com.sonyericsson.com.OcrUiEnhancement;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;
import android.view.Gravity;
import android.view.MenuItem;
import android.content.Intent;


public class OcrUiEnhancementMain 
		extends Activity implements WorkerThreadListener, View.OnClickListener {
	public static final int TRANSLATE_COMPLETED = 23; 
	private static final String TAG = "OcrUiEnhancementMain";
	private static final int MENU_IMAGE_VIEW = 0x01;
	private static final int MENU_CAMERA_VIEW = 0x02;
	private WorkerThreadTest mWorkerTest = null;
	private Button mTranslateBtn = null;
	private Button mTranslateBtn2 = null;
	private Button mSearchBtn = null;
	private Toast mToast = null;
	
	private WorkerThreadHandler mWorkerThreadHandler = new WorkerThreadHandler();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // create Translate
        mWorkerTest = new WorkerThreadTest(this);

        // TODO: how to set the two buttons the same size in one line
        // set translate button
        mTranslateBtn = (Button)findViewById(R.id.translate_test);
        mTranslateBtn.setText(R.string.start_translate);
        mTranslateBtn.setOnClickListener(this);

        // set translate button
        mTranslateBtn2 = (Button)findViewById(R.id.translate_test_2);
        mTranslateBtn2.setText(R.string.start_translate);
        mTranslateBtn2.setOnClickListener(this);

        // set translate button
        mSearchBtn = (Button)findViewById(R.id.search_test);
        mSearchBtn.setText(R.string.start_search);
        mSearchBtn.setOnClickListener(this);
    }
    
    /**
     * Option menu operation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_IMAGE_VIEW, 0, R.string.menu_image_view);
    	menu.add(0, MENU_CAMERA_VIEW, 0, R.string.menu_camera_view);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = null;
        switch (item.getItemId()) {
        case MENU_IMAGE_VIEW:
        	intent = new Intent(this, ImageBackground.class);
        	startActivity(intent);
            return true;
        case MENU_CAMERA_VIEW:
        	intent = new Intent(this, CameraPreview.class);
        	startActivity(intent);
            return true;
        }
        return false;
    }
    
    
    /**
     * OnClickListener callback
     */
    public void onClick(View v) {
		// start translate test
    	Button btn = (Button)v;
    	if(btn == mTranslateBtn) {
    		// TODO: disable other button here
    		if(mTranslateBtn.getText() == getString(R.string.start_translate)) {
    			mWorkerTest.startTranslate(getString(R.string.translate_string));
    			mTranslateBtn.setText(R.string.stop_translate);
    		} else {
    			mWorkerTest.stopTranslate();
    			mTranslateBtn.setText(R.string.start_translate);
    		}    	
    	} else if(btn == mTranslateBtn2) {
    		if(btn.getText() == getString(R.string.start_translate)) {
    			mWorkerTest.startTranslateMsg(getString(R.string.translate_string));
    			mTranslateBtn2.setText(R.string.translating);
    			mTranslateBtn2.setClickable(false);
    		}    	
    	} else if(btn == mSearchBtn) {
			mWorkerTest.startSearchMsg(getString(R.string.search_string));
    	} else {
    		Log.v(TAG, "onClick(), invalid view");
    	}
    }
    
	/**
	 * WorkerThreadHandler
	 * Dispatch the function call from worker thread into main thread
	 */
	private class WorkerThreadHandler extends Handler {
    	@Override
    	public void handleMessage(Message msg) 
    	{
    		switch(msg.what) 
    		{
    		case TRANSLATE_COMPLETED:
    			Log.d(TAG,"handleMessage(), receive GoogleTranslateTest.TRANSLATE_COMPLETED." + 
    				" the translate result is: \n" + ((String)msg.obj));
    			mWorkerTest.stopTranslate();
    			mTranslateBtn.setText(R.string.start_translate);
    			mTranslateBtn2.setText(R.string.start_translate);
    			mTranslateBtn2.setClickable(true);
    			if(mToast != null) {
    				mToast.cancel();
    			}
    			StringBuilder result = new StringBuilder(256);
    			result.append(getString(R.string.translate_string));
    			result.append(" --> ");
    			result.append(((String)msg.obj));
    			
    			mToast = Toast.makeText(OcrUiEnhancementMain.this, 
    					result.toString(), Toast.LENGTH_LONG);
    			mToast.setGravity(Gravity.CENTER, 0, 0);
    			mToast.show();
    			break;
    		default:
    			super.handleMessage(msg);
    		}
    	} // handleMessage
	}
    
    /**
     * WorkerThreadListener implementation
     */
    public void onTranslateCompleted(String output)
    {
    	Message msg = mWorkerThreadHandler.obtainMessage(TRANSLATE_COMPLETED);
    	msg.obj = output;
    	mWorkerThreadHandler.sendMessage(msg);
    }
    
    /**
     * 
     */
}