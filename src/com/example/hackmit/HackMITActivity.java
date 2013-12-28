package com.example.hackmit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HackMITActivity extends Activity {
	EditText editText;
	TextView viewText;
	String sodaName;
	String sodaRating;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hack_mit);

		editText = (EditText) findViewById(R.id.editText1);
		viewText = (TextView) findViewById(R.id.textView1);
		
		new DownloadImageTask((ImageView) findViewById(R.id.imageView1))
		.execute("http://files.softicons.com/download/food-drinks-icons/coca-cola-family-icons-by-alejandro-lopez/png/256/Coke%20Classic_woops.png");
		
		String recieve = recieve();
		
		try {
		      JSONArray jsonArray = new JSONArray(recieve);
		      Log.i(HackMITActivity.class.getName(),
		          "Number of entries " + jsonArray.length());
		      for (int i = 0; i < jsonArray.length(); i++) {
		        JSONObject jsonObject = jsonArray.getJSONObject(i);
		        Log.i(HackMITActivity.class.getName(), jsonObject.getString("text"));
		      }
		    } catch (Exception e) {
		      e.printStackTrace();
		    }

		/*
		 * Button sendButton = (Button) findViewById(R.id.sendbutton);
		 * sendButton.setText("Send Soda List");
		 * sendButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * public void onClick(View v) { sodaName =
		 * editText.getText().toString();
		 * 
		 * String[] tokens = sodaName .split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		 * for (String t : tokens) { send(t); }
		 * 
		 * viewText.append("Soda preferences have been recorded...");
		 * 
		 * } });
		 */

		editText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					sodaName = editText.getText().toString();

					String[] tokens = sodaName
							.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					for (String t : tokens) {
						send(t);
					}

					viewText.append("Soda preferences have been recorded...");

					return true;
				}
				return false;
			}
		});

	}
	
	 public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_1star:
	            if (checked)
	                // Test1
	            break;
	        case R.id.radio_2star:
	            if (checked)
	                // Test2
	            break;
	        case R.id.radio_3star:
	            if (checked)
	                // Test3
	            break;
	        case R.id.radio_4star:
	            if (checked)
	                // Test4
	            break;
	        case R.id.radio_5star:
	            if (checked)
	                // Test5
	            break;
	    }
	}

	public boolean send(String name) {

		boolean result = true;
		HttpClient hc = new DefaultHttpClient();
		String message;

		HttpPost p = new HttpPost("http://18.111.92.11:8888/sodapp/");
		JSONObject object = new JSONObject();
		try {

			object.put("name", name);
//			object.put("rating", rating);

		} catch (Exception ex) {

		}

		try {
			message = object.toString();

			p.setEntity(new StringEntity(message, "UTF8"));
			p.setHeader("Content-type", "application/json");
			HttpResponse resp = hc.execute(p);
			if (resp != null) {
				if (resp.getStatusLine().getStatusCode() == 204)
					result = true;
			}

			Log.d("Status line", "" + resp.getStatusLine().getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();

		}
		viewText.setText(object.toString());
		return result;

	}

	public String recieve() {
		StringBuilder builder = new StringBuilder();
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet("http://18.111.92.11:8888/sodapp/");
	    try {
	      HttpResponse response = client.execute(httpGet);
	      StatusLine statusLine = response.getStatusLine();
	      int statusCode = statusLine.getStatusCode();
	      if (statusCode == 200) {
	        HttpEntity entity = response.getEntity();
	        InputStream content = entity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	        String line;
	        while ((line = reader.readLine()) != null) {
	          builder.append(line);
	        }
	      } else {
	        Log.e(HackMITActivity.class.toString(), "Failed to download file");
	      }
	    } catch (ClientProtocolException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    viewText.setText(builder.toString());
	    
	    return builder.toString();
	  }
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hack_mit, menu);

		return true;
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

}
