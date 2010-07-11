package com.sonyericsson.com.OcrUiEnhancement;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.util.Log;

public class ImageBackground extends Activity {
	private static final String TAG = "ImageBackground";

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_background);
    }
}
