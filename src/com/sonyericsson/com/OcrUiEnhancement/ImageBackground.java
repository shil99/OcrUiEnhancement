package com.sonyericsson.com.OcrUiEnhancement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.view.View;
import android.widget.RelativeLayout;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;
import android.util.Log;

public class ImageBackground extends Activity {
	private static final String TAG = "ImageBackground";
	private static final int MENU_TRANSPARENT_TEXT_BOX = 0x01;
	private static final int MENU_TRANSPARENT_ICON_BOX = 0x02;
	// the relative layout of image view
	// TODO: replace absolute layout with relative layout 
	private AbsoluteLayout rootView = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_background);
        // get root view
        rootView = (AbsoluteLayout)findViewById(R.id.image_background_parent);
    }
    
    /**
     * Option menu operation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MENU_TRANSPARENT_TEXT_BOX, 0, "Text Box");
    	menu.add(0, MENU_TRANSPARENT_ICON_BOX, 0, "Image Box");
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_TRANSPARENT_TEXT_BOX:
        	addTransparentTextView();
            return true;
        case MENU_TRANSPARENT_ICON_BOX:
        	addTransparentImageView();
        	return true;
        }
        return false;
    }
    
    /**
     * 
     */
    private void addTransparentTextView() {
    	// create a new text view
    	TextView textView = new TextView(this);
    	textView.setText("Hi, this is Shi Ling!");
    	textView.setTextColor(0xFF000000);
    	textView.setBackgroundColor(0);
    	
    	// set layout
    	int width = rootView.getWidth();
    	int height = rootView.getHeight();
    	
    	@SuppressWarnings("deprecation")
		AbsoluteLayout.LayoutParams lp = 
			new AbsoluteLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT, 
					width/2 - 20, 
					height/2 - 20);
    	rootView.addView(textView, lp);
    }
    
    private void addTransparentImageView() {
    	// to make transparent icon
    	// Google search result
    	// http://www.google.com.hk/search?sourceid=chrome&ie=UTF-8&q=android+how+to+make+transparent+icon
    	// http://developer.android.com/guide/practices/ui_guidelines/icon_design.html
    	// It's image editor's issue, try to edit it in specific PNG editor
    	// create a image view
    	ImageView imageView = new ImageView(this);
    	imageView.setImageResource(R.drawable.red_markers);
    	// 
		AbsoluteLayout.LayoutParams lp = 
			new AbsoluteLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT, 
					20, 
					20);
		rootView.addView(imageView, lp);
    }
}
