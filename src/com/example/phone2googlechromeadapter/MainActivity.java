package com.example.phone2googlechromeadapter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent newIntent = handleURLIntent(getIntent());
		
		// not a share intent
		if (newIntent != null)
		{
			try 
			{
				startActivity(newIntent);
				finish();
			} 
			catch (Exception ex)
			{
				// install Phone 2 chrome thing
				Toast.makeText(this, "Whoops. something went wrong", Toast.LENGTH_SHORT).show();
			}
		}
		
		// started with a wrong intent
		Toast.makeText(this, "Dude! READ THE DOCUMENTATION!", Toast.LENGTH_SHORT).show();
		
		// kill app
		System.exit(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	// returns a new parsed intent or null
	Intent handleURLIntent(Intent intent)
	{
		if (!intent.getAction().equalsIgnoreCase("android.intent.action.SEND"))
			return null;
		
		String text = (String) intent.getExtras().get(Intent.EXTRA_TEXT).toString();
		ArrayList<String> URLs = retrieveLinks(text);
		
		for (String value : URLs)
		{
			Log.d("URL's", value);
		}
		
		Intent i = new Intent(Intent.ACTION_VIEW);
		
		// Pulse sends [0] as the correct url
		i.setData(Uri.parse(URLs.get(0)));
		i.setPackage("com.lg.valle.phone2chrome");
		return i;
	}
	
	ArrayList<String> retrieveLinks(String text) 
	{
        ArrayList<String> links = new ArrayList<String>();

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        
        while(m.find()) {
        	String urlStr = m.group();

	        if (urlStr.startsWith("(") && urlStr.endsWith(")"))
	        {
	            char[] stringArray = urlStr.toCharArray(); 
	
	            char[] newArray = new char[stringArray.length-2];
	            System.arraycopy(stringArray, 1, newArray, 0, stringArray.length-2);
	            urlStr = new String(newArray);
	        }
	        
	        links.add(urlStr);
        }
        
        return links;
    }

}
