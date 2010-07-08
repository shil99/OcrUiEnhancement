/**
 * 
 */
package com.sonyericsson.com.OcrUiEnhancement;

import java.lang.Thread;
import android.util.Log;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

/**
 * @author 28850043
 * Test google translate API
 */
public class GoogleTranslateTest {
	private static final String TAG = "OcrUiEnhancementMain";
	private Worker mWorker = null;
	private boolean mStop = false;
	private OcrUiEnhancementMain mContext = null;
	private static final boolean mTranslate = true;
	
	// helper thread
	private class Worker extends Thread {
		@Override
		public void run () {
			if(mStop == true) {
				Log.d(TAG,"Worker.run(), stop before run");
				return;
			}
			// do nothing here, just print some log
			String input = mContext.getString(R.string.translate_string);
			
			for(int i=0; i<5; i++)
			{
				if(mStop == true) {
					Log.d(TAG,"Worker.run(), stop in run");
					return;
				}
				if(mTranslate) {
			        String output = null;
			        try {
			        	output = Translate.execute(input, Language.CHINESE_SIMPLIFIED, Language.ENGLISH);
			            Log.v(TAG, "translate string: " + output.toString());
			        } catch(Exception e) {
			        	// do nothing
			            Log.v(TAG, "translate failed");
			        }
				} else {
					try {
						Thread.sleep(200);
						Log.d(TAG,"Worker.run(), running " + i);
					}
					catch(Exception e) {
						Log.d(TAG,"critical errors");
					}
				}
			} // for(...)
			
			Log.d(TAG,"Worker.run(), before quit");
			mContext.onTranslateCompleted();
		} // run ()
	}
	
	// constructors
	public GoogleTranslateTest(OcrUiEnhancementMain context) {
		mContext = context;
        Translate.setHttpReferrer("http://shil99.blogspot.com/");
		// XXX: if thread is terminated, it cannot be start anymore
        // mWorker = new Worker();
	}
	
	public void start() {
		if(mWorker != null) {
			Log.e(TAG,"start(), test is running, do nothing!");
			return;
		}

		mWorker = new Worker();
		Log.d(TAG,"start(), start test");
		mStop = false;
		mWorker.start();
	}
	
	public void stop() {
		if(mWorker == null) {
			Log.e(TAG,"start(), test isn't running, do nothing!");
			return;
		}

		mStop = true;
		Log.d(TAG,"stop(), before join...");
		try {
			mWorker.join();
		} catch (Exception e) {
			Log.e(TAG,"start(), cannot join thread!");
		}
		Log.d(TAG,"stop(), join successfully");
		mWorker = null;
	}
}
