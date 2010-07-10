/**
 * 
 */
package com.sonyericsson.com.OcrUiEnhancement;

import java.lang.Thread;
import android.util.Log;
import android.os.Looper;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

/**
 * @author 28850043
 * Test wording thread
 */
public class WorkerThreadTest {
	private static final String TAG = "OcrUiEnhancementMain";
	private static final boolean mTranslate = true;
	
	// worker sub class
	private TranslateWorker mTranslateWorker = null;
	private MsgHandler mMsgHandler = null;
    private volatile Looper mMsgLooper = null;
    	
	// thread control
	private boolean mStop = false;
	private WorkerThreadListener mListener = null;
	
	// translate helper members
	private String mTransInput = null;
	
	/**
	 * WorkerThreadTest()
	 * @param context
	 */
	public WorkerThreadTest(WorkerThreadListener context) {
		mListener = context;
		// XXX: if thread is terminated, it cannot be start anymore
		
	    // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        HandlerThread thread = new HandlerThread("MsgWorder");
        thread.start();
        
        mMsgLooper = thread.getLooper();
        mMsgHandler = new MsgHandler(mMsgLooper);
	}
	
	/**
	 * Worker
	 * - a simple thread worker
	 * @author 28850043
	 */
	private class TranslateWorker extends Thread {
		@Override
		public void run () {
			if(mStop == true) {
				Log.d(TAG,"Worker.run(), stop before run");
				return;
			}
		
			String output = null;
			for(int i=0; i<5; i++)
			{
				if(mStop == true) {
					Log.d(TAG,"Worker.run(), stop in run");
					return;
				}
				if(mTranslate) {
					output = TranslateTest(mTransInput);
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
			mListener.onTranslateCompleted(output);
		} // run ()
	}
	
	public void startTranslate(String input) {
		if(mTranslateWorker != null) {
			Log.e(TAG,"start(), test is running, do nothing!");
			return;
		}
		mTranslateWorker = new TranslateWorker();
		Log.d(TAG,"start(), start test");
		mStop = false;
		mTransInput = input;
		mTranslateWorker.start();
	}
	
	public void stopTranslate() {
		if(mTranslateWorker == null) {
			Log.e(TAG,"start(), test isn't running, do nothing!");
			return;
		}

		mStop = true;
		Log.d(TAG,"stop(), before join...");
		try {
			mTranslateWorker.join();
		} catch (Exception e) {
			Log.e(TAG,"start(), cannot join thread!");
		}
		Log.d(TAG,"stop(), join successfully");
		mTranslateWorker = null;
	}

	/**
	 * MsgHandler
	 * the message looper run in working thread
	 */
	private static final int MSG_TRANSLATE = 0x01;
	private static final int MSG_SEARCH = 0x02;
	
	private final class MsgHandler extends Handler	{

		public MsgHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			// process incoming messages here
			switch(msg.what) {
			case MSG_TRANSLATE:
				Log.d(TAG,"MsgWorker.handleMessage(), MSG_TRANSLATE");
				mListener.onTranslateCompleted(
						TranslateTest((String)msg.obj));
				break;
			case MSG_SEARCH:
				Log.d(TAG,"MsgWorker.handleMessage(), MSG_SEARCH");
				SearchTest((String)msg.obj);
			default:
				Log.e(TAG,"MsgWorker.handleMessage(), invalid message " + msg.what);
				super.handleMessage(msg);
			}
        }
	}

	/**
	 * public interface
	 */
	public void startTranslateMsg(String input) {
		Message msg = mMsgHandler.obtainMessage(MSG_TRANSLATE, 0, 0, input);
		mMsgHandler.sendMessage(msg);
	}
	
	public void startSearchMsg(String input) {
		Message msg = mMsgHandler.obtainMessage(MSG_SEARCH, 0, 0, input);
		mMsgHandler.sendMessage(msg);
	}
	
	
	/**
	 * translate private functions 
	 */
	private String TranslateTest(String input) {
        Translate.setHttpReferrer("http://shil99.blogspot.com/");

        // XXX: log cannot support Chinese GBK output
       	Log.v(TAG, "TranslateTest(), input: " + input);
        String output = null;
        
        try {
        	output = Translate.execute(input, Language.CHINESE_SIMPLIFIED, Language.ENGLISH);
            Log.v(TAG, "TranslateTest(), output: " + output.toString());
        } catch(Exception e) {
        	// do nothing
            Log.v(TAG, "TranslateTest(), translate fail!");
        }
        return output;
	}
	
	/**
	 * google search
	 */
	private void SearchTest(String input) {
		GoogleQuery.makeQuery(input);
	}
}
