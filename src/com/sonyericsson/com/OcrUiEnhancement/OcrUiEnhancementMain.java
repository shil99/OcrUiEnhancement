package com.sonyericsson.com.OcrUiEnhancement;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Handler;
import android.os.Message;

public class OcrUiEnhancementMain extends Activity {
	public static final int TRANSLATE_COMPLETED = 23; 
	private static final String TAG = "OcrUiEnhancementMain";
	private GoogleTranslateTest mTranslateTest = null;
	private Button mTranslateBtn = null;
	private Handler mHandler = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // start google query 
        GoogleQuery.makeQuery("gsearch-java-client");
        // create handler
        mHandler = new Handler() 
        {
        	@Override
        	public void handleMessage(Message msg) 
        	{
        		switch(msg.what) 
        		{
        		case TRANSLATE_COMPLETED:
        			Log.d(TAG,"handleMessage(), receive GoogleTranslateTest.TRANSLATE_COMPLETED.");
        			stopTranslate();
        			break;
        		default:
        			super.handleMessage(msg);
        		}
        	} // handleMessage
        };
        
        // create Translate
        mTranslateTest = new GoogleTranslateTest(this);
        
        // set translate button
        mTranslateBtn = (Button)findViewById(R.id.translate_test);
        mTranslateBtn.setText(R.string.start_translate);
        mTranslateBtn.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) {
				// start translate test
				if(mTranslateBtn.getText() == getString(R.string.start_translate))
				{
					startTranslate();
				} 
				else 
				{
					stopTranslate();
				}
			}
		} // View.OnClickListener()
        );
    }
    
    private void startTranslate() 
    {
		mTranslateTest.start();
		mTranslateBtn.setText(R.string.stop_translate);
    }
    
    private void stopTranslate() 
    {
		mTranslateTest.stop();
		mTranslateBtn.setText(R.string.start_translate);
    }
    
    public void onTranslateCompleted()
    {
    	mHandler.sendMessage(mHandler.obtainMessage(TRANSLATE_COMPLETED));
    }
}