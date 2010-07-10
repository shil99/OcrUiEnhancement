package com.sonyericsson.com.OcrUiEnhancement;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.json.JSONArray;      // JSON library from http://www.json.org/java/
import org.json.JSONObject;

/* The source code is gotten from 
 * http://www.ajaxlines.com/ajax/stuff/article/using_google_is_ajax_search_api_with_java.php
 * The more detailed description of google RESTful search API is here
 * http://code.google.com/intl/zh-CN/apis/ajaxsearch/documentation/#fonje
 */
public class GoogleQuery {
	private static final String TAG = "GoogleQuery";
	private static final String HTTP_REFERER = "http://shil99.blogspot.com";

	public static final void makeQuery(String query) {
		Log.v(TAG, "makeQuery(), query string" + query);
		
		try
		{
			// Convert spaces to +, etc. to make a valid URL
			query = URLEncoder.encode(query, "UTF-8");
			URL url = new URL("http://ajax.googleapis.com/ajax/services/search/web?start=0&rsz=large&v=1.0&q=" + query);
			
			// send request
			URLConnection connection = url.openConnection();
			connection.addRequestProperty("Referer", HTTP_REFERER);

			// Get the JSON response
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			Log.v(TAG, "makeQuery(), result json string:\n" + builder.toString());

			// parse json string to get the result
			String response = builder.toString();
			JSONObject json = new JSONObject(response);

			Log.v(TAG, "makeQuery(), total results = " +
				json.getJSONObject("responseData")
				.getJSONObject("cursor")
				.getString("estimatedResultCount"));

			JSONArray ja = json.getJSONObject("responseData")
			.getJSONArray("results");

			Log.v(TAG, "makeQuery(), results:");
			for (int i = 0; i < ja.length(); i++) {
				Log.v(TAG, "makeQuery(), " +(i+1) + ". ");
				JSONObject j = ja.getJSONObject(i);
				Log.v(TAG, "makeQuery(), " + j.getString("titleNoFormatting"));
				Log.v(TAG, "makeQuery(), " + j.getString("url"));
			}
		} // try
		catch (Exception e) {
			Log.v(TAG, "makeQuery(), query error");
		}
	 } // makeQuery()
}
