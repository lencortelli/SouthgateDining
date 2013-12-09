package com.southgate.dining;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;
import com.southgate.dining.R;

public class MainActivity extends Activity implements OnClickListener {
	//private static final String TAG = MainActivity.class.getName();
	RelativeLayout load;
	LinearLayout ll, llbuttons, llpadme;
	TableLayout tl;
	TextView[] txtItem = new TextView[6];
	TextView txtDay, txtLoading;
	Button btnBreakfast, btnLunch, btnDinner, btnNext, btnPrevious;
	ImageView[] imgItem = new ImageView[6];
	TableRow[] tblRow = new TableRow[6];
	SpannableString Breakfast, Lunch, Dinner;
	String strBreakfast, strLunch, strDinner;
	int day = 0, time = 0, width = 0, i = 0;
    ArrayList<String> Mon = new ArrayList<String>();
    ArrayList<String> Tues = new ArrayList<String>();
    ArrayList<String> Wed = new ArrayList<String>();
    ArrayList<String> Thurs = new ArrayList<String>();
    ArrayList<String> Fri = new ArrayList<String>();
    ArrayList<String> Sat = new ArrayList<String>();
    ArrayList<String> Sun = new ArrayList<String>();
    ArrayList<ArrayList<String>> nodes = new ArrayList<ArrayList<String>>();
    Map<String, Integer> map = new HashMap<String, Integer>();
    
    String[] Days;
    
	
	/**
	 * Simple Dialog used to show the splash screen
	 */
	protected Dialog mSplashDialog;
	
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    ll = (LinearLayout)findViewById(R.id.Database);
	    llbuttons = (LinearLayout)findViewById(R.id.Buttons);
	    llpadme = (LinearLayout)findViewById(R.id.PadMe);
	    tl = (TableLayout)(findViewById(R.id.Table));  
	    Display display = getWindowManager().getDefaultDisplay(); 
        Point size = new Point();

    	display.getSize(size);
    	width = size.x; 
    	tblRow[0] = (TableRow)findViewById(R.id.tableRow1);
    	tblRow[1] = (TableRow)findViewById(R.id.tableRow2);
    	tblRow[2] = (TableRow)findViewById(R.id.tableRow3);
    	tblRow[3] = (TableRow)findViewById(R.id.tableRow4);
    	tblRow[4] = (TableRow)findViewById(R.id.tableRow5);
    	tblRow[5] = (TableRow)findViewById(R.id.tableRow6);
    	LinearLayout.LayoutParams params;
	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
	    	for(i =0; i<6; i++)
        	{
        		params = (LinearLayout.LayoutParams)tblRow[i].getLayoutParams();
        		params.setMargins((int)(width*.25),0,0,0);
        		tblRow[i].setLayoutParams(params);
        	}
	    else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
	    	for(i =0; i<6; i++)
        	{
        		params = (LinearLayout.LayoutParams)tblRow[i].getLayoutParams();
        		params.setMargins(0,0,0,0);
        		tblRow[i].setLayoutParams(params);
        	}
	  }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isNetworkAvailable())
        {
        	Toast.makeText(MainActivity.this, "SG Dining Failed to Load: Network Unavailable", Toast.LENGTH_LONG).show();
        	finish();
        }
        else
        {
	        setContentView(R.layout.database);
	        this.setTitle("SouthGate Dining Menu");
	        ll = (LinearLayout)findViewById(R.id.Database);
	        Display display = getWindowManager().getDefaultDisplay(); 
	        Point size = new Point();

        	display.getSize(size);
        	width = size.x;
        	tblRow[0] = (TableRow)findViewById(R.id.tableRow1);
        	tblRow[1] = (TableRow)findViewById(R.id.tableRow2);
        	tblRow[2] = (TableRow)findViewById(R.id.tableRow3);
        	tblRow[3] = (TableRow)findViewById(R.id.tableRow4);
        	tblRow[4] = (TableRow)findViewById(R.id.tableRow5);
        	tblRow[5] = (TableRow)findViewById(R.id.tableRow6);
        	
	        llbuttons = (LinearLayout)findViewById(R.id.Buttons);
		    llpadme = (LinearLayout)findViewById(R.id.PadMe);
		    tl = (TableLayout)(findViewById(R.id.Table));
	        int ot = getResources().getConfiguration().orientation;
	        if(ot == Configuration.ORIENTATION_LANDSCAPE)
	        	for(i =0; i<6; i++)
	        	{
	        		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tblRow[i].getLayoutParams();
	        		params.setMargins((int)(width*.25),0,0,0);
	        		tblRow[i].setLayoutParams(params);
	        	}
	        else if(ot == Configuration.ORIENTATION_PORTRAIT)
	        	for(i =0; i<6; i++)
	        	{
	        		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tblRow[i].getLayoutParams();
	        		params.setMargins(0,0,0,0);
	        		tblRow[i].setLayoutParams(params);
	        	}
        	
	        txtItem[0] = (TextView)findViewById(R.id.txtEnt1);
	        txtItem[1] = (TextView)findViewById(R.id.txtEnt2);
	        txtItem[2] = (TextView)findViewById(R.id.txtSide);
	        txtItem[3] = (TextView)findViewById(R.id.txtVeg1);
	        txtItem[4] = (TextView)findViewById(R.id.txtVeg2);
	        txtItem[5] = (TextView)findViewById(R.id.txtBread);
	        txtDay = (TextView)findViewById(R.id.txtDay);
	        imgItem[0] = (ImageView)findViewById(R.id.imgItem1);
	        imgItem[1] = (ImageView)findViewById(R.id.imgItem2);
	        imgItem[2] = (ImageView)findViewById(R.id.imgItem3);
	        imgItem[3] = (ImageView)findViewById(R.id.imgItem4);
	        imgItem[4] = (ImageView)findViewById(R.id.imgItem5);
	        imgItem[5] = (ImageView)findViewById(R.id.imgItem6);
	        
	    	Breakfast = new SpannableString(getResources().getString(R.string.bf));
	    	Lunch = new SpannableString(getResources().getString(R.string.lunch));
	    	Dinner = new SpannableString(getResources().getString(R.string.dinner));
			Dinner.setSpan(new UnderlineSpan(), 0, Dinner.length(), 0);
			Lunch.setSpan(new UnderlineSpan(), 0, Lunch.length(), 0);
			Breakfast.setSpan(new UnderlineSpan(), 0, Breakfast.length(), 0);
			
			map.put("SF", R.drawable.seafood);
			map.put("PY", R.drawable.poultry);
			map.put("PK", R.drawable.pork);
			map.put("BF", R.drawable.beef);
			map.put("VE", R.drawable.vegetable);
			map.put("PA", R.drawable.pasta);
			map.put("DY", R.drawable.dairy);
			map.put("GN", R.drawable.grain);
			
	        btnBreakfast = (Button)findViewById(R.id.btnBreakfast);
	        btnBreakfast.setOnClickListener(this);
	        btnLunch = (Button)findViewById(R.id.btnLunch);
	        btnLunch.setOnClickListener(this);
	        btnDinner = (Button)findViewById(R.id.btnDinner);
	        btnDinner.setOnClickListener(this);
	        btnNext = (Button)findViewById(R.id.btnNext);
	        btnNext.setOnClickListener(this);
	        btnPrevious = (Button)findViewById(R.id.btnPrevious);
	        btnPrevious.setOnClickListener(this);
	        
	        ll.setBackgroundColor(Color.parseColor("#970B23"));
	        for(i = 0; i < 6; i++)
	        	txtItem[i].setTextColor(Color.parseColor("#FFD700"));
	        txtDay.setTextColor(Color.parseColor("#FFD700"));
	        btnBreakfast.setTextColor(Color.parseColor("#FFD700"));
	        btnLunch.setTextColor(Color.parseColor("#FFD700"));
	        btnDinner.setTextColor(Color.parseColor("#FFD700"));
	        
	        strBreakfast = getResources().getString(R.string.bf);
	        strLunch = getResources().getString(R.string.lunch);
	        strDinner =  getResources().getString(R.string.dinner);
	        btnDinner.setText(strDinner);
	        btnLunch.setText(strLunch);
	        btnBreakfast.setText(strBreakfast);
	
	        Days= getResources().getStringArray(R.array.daysofweek);
	        ll.setOnTouchListener(new OnSwipeTouchListener(this) {
			    @Override
			    public void onSwipeLeft() {
			    	if(day!=6)
						day++;
					else
						day = 0;

					txtDay.setText(Days[day]);
					for(i = 0; i < 6; i++)
			    	{
						txtItem[i].setText(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):"")?nodes.get(day).get(i+time).substring(2):nodes.get(day).get(i+time));
				    	if(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):""))
				    		imgItem[i].setBackgroundResource(map.get(nodes.get(day).get(i+time).substring(0,2)));
				    	else
				    		imgItem[i].setBackgroundResource(0);
			    	}
			    }
			    
			    @Override
			    public void onSwipeRight() {
			    	if(day!=0)
						day--;
					else
						day = 6;

					txtDay.setText(Days[day]);
					for(i = 0; i < 6; i++)
			    	{
						txtItem[i].setText(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):"")?nodes.get(day).get(i+time).substring(2):nodes.get(day).get(i+time));
				    	if(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):""))
				    		imgItem[i].setBackgroundResource(map.get(nodes.get(day).get(i+time).substring(0,2)));
				    	else
				    		imgItem[i].setBackgroundResource(0);
			    	}
			    }
			    
			    @Override
			    public void onSwipeUp(){
			    	if(time == 0)
			    	{
			    		time = 7;
			    		btnBreakfast.setText(strBreakfast);
						btnLunch.setText(Lunch);
			    	}
			    	else if (time == 7)
			    	{
			    		time = 15;
						btnLunch.setText(strLunch);
						btnDinner.setText(Dinner);
			    	}
			    	else
			    		return;
			    	
					for(i = 0; i < 6; i++)
			    	{
						txtItem[i].setText(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):"")?nodes.get(day).get(i+time).substring(2):nodes.get(day).get(i+time));
				    	if(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):""))
				    		imgItem[i].setBackgroundResource(map.get(nodes.get(day).get(i+time).substring(0,2)));
				    	else
				    		imgItem[i].setBackgroundResource(0);
			    	}
			    }
			    
			    @Override
			    public void onSwipeDown(){
			    	if(time == 7)
			    	{
			    		time = 0;
			    		btnBreakfast.setText(Breakfast);
						btnLunch.setText(strLunch);
			    	}
			    	else if (time == 15)
			    	{
			    		time = 7;
						btnLunch.setText(Lunch);
						btnDinner.setText(strDinner);
			    	}
			    	else
			    		return;
			    	
					for(i = 0; i < 6; i++)
			    	{
						txtItem[i].setText(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):"")?nodes.get(day).get(i+time).substring(2):nodes.get(day).get(i+time));
				    	if(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):""))
				    		imgItem[i].setBackgroundResource(map.get(nodes.get(day).get(i+time).substring(0,2)));
				    	else
				    		imgItem[i].setBackgroundResource(0);
			    	}
			    }
	        });
        
        	showSplashScreen();
        	new eventupdate().execute();
        }
    }
    
    //Network Checker
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    /**
     * Removes the Dialog that displays the splash screen
     */
    protected void removeSplashScreen() {
        if (mSplashDialog != null) {
            mSplashDialog.dismiss();
            mSplashDialog = null;
        }
    }
     
    /**
     * Shows the splash screen over the full Activity
     */
    protected void showSplashScreen() {
        mSplashDialog = new Dialog(this, R.style.SplashScreen);
        mSplashDialog.setContentView(R.layout.loading);
        mSplashDialog.setTitle("Loading Menu Data");
        load = (RelativeLayout)mSplashDialog.findViewById(R.id.Loading);
        txtLoading = (TextView)mSplashDialog.findViewById(R.id.txtLoading);
        load.setBackgroundColor(Color.parseColor("#970B23"));
        txtLoading.setTextColor(Color.parseColor("#FFD700"));
        
        mSplashDialog.setCancelable(false);
        mSplashDialog.show();
    }
    
    public class eventupdate extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... url) {
        	SpreadsheetService service = new SpreadsheetService("SG Dining");
            try {
                // Notice that the url ends
                // with default/public/values.
            	// This spreadsheet will be where SouthGate will upload their weekly menu.
                String urlString = "https://spreadsheets.google.com/feeds/list/0AnxhBfaT2weBdDl5OEpCdm5YS2RvWmlveW1ETmxPb2c/default/public/values";

                // turn the string into a URL
                URL ss = new URL(urlString);
                
                // You could substitute a cell feed here in place of
                // the list feed
                ListFeed feed = service.getFeed(ss, ListFeed.class);
                for (ListEntry entry : feed.getEntries()) {
                  CustomElementCollection elements = entry.getCustomElements();
                  Mon.add(elements.getValue("Monday"));
                  Tues.add(elements.getValue("Tuesday"));
                  Wed.add(elements.getValue("Wednesday"));
                  Thurs.add(elements.getValue("Thursday"));
                  Fri.add(elements.getValue("Friday"));
                  Sat.add(elements.getValue("Saturday"));
                  Sun.add(elements.getValue("Sunday"));
                }
              } catch (IOException e) {
                e.printStackTrace();
              } catch (ServiceException e) {
                e.printStackTrace();
              }
          publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void...params){
			super.onProgressUpdate();
			nodes.add(Mon);
	        nodes.add(Tues);
	        nodes.add(Wed);
	        nodes.add(Thurs);
	        nodes.add(Fri);
	        nodes.add(Sat);
	        nodes.add(Sun);
	        txtItem[0] = (TextView)findViewById(R.id.txtEnt1);
	        txtItem[1] = (TextView)findViewById(R.id.txtEnt2);
	        txtItem[2] = (TextView)findViewById(R.id.txtSide);
	        txtItem[3] = (TextView)findViewById(R.id.txtVeg1);
	        txtItem[4] = (TextView)findViewById(R.id.txtVeg2);
	        txtItem[5] = (TextView)findViewById(R.id.txtBread);
			txtDay = (TextView)findViewById(R.id.txtDay);
			imgItem[0] = (ImageView)findViewById(R.id.imgItem1);
	        imgItem[1] = (ImageView)findViewById(R.id.imgItem2);
	        imgItem[2] = (ImageView)findViewById(R.id.imgItem3);
	        imgItem[3] = (ImageView)findViewById(R.id.imgItem4);
	        imgItem[4] = (ImageView)findViewById(R.id.imgItem5);
	        imgItem[5] = (ImageView)findViewById(R.id.imgItem6);
			
			Calendar calendar = Calendar.getInstance();
			
			day = calendar.get(Calendar.DAY_OF_WEEK);
			if(day==1)
				day=6;
			else
				day -= 2;
			
			time = calendar.get(Calendar.HOUR_OF_DAY);
			if(time > 19  ||  (time >18 && (day == 5 || day == 4)))//It's passed dinner time, set to breakfast next day
			{ //Friday and Saturday Close early by default
				time = 0;
				if(day==6)
					day = 0;
				else
					day++;
				btnBreakfast.setText(Breakfast);
			}
			else if (time > 15) //Dinner time starts
			{
				time = 15;
				btnDinner.setText(Dinner);
				btnLunch.setText(strLunch);
				btnBreakfast.setText(strBreakfast);
			}
			else if (time > 10) //Lunch time starts
			{
				time = 7;
				btnLunch.setText(Lunch);
				btnDinner.setText(strDinner);
				btnBreakfast.setText(strBreakfast);
			}
			else //Default to Breakfast
			{
				time = 0;
				btnBreakfast.setText(Breakfast);
				btnLunch.setText(strLunch);
				btnDinner.setText(strDinner);
			}
			
			txtDay.setText(Days[day]);
			for(i = 0; i < 6; i++)
	    	{
				txtItem[i].setText(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):"")?nodes.get(day).get(i+time).substring(2):nodes.get(day).get(i+time));
		    	if(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):""))
		    		imgItem[i].setBackgroundResource(map.get(nodes.get(day).get(i+time).substring(0,2)));
		    	else
		    		imgItem[i].setBackgroundResource(0);
	    	}
			removeSplashScreen();
        }
    }
    
	@Override
	public void onClick(View arg0) {
		txtItem[0] = (TextView)findViewById(R.id.txtEnt1);
        txtItem[1] = (TextView)findViewById(R.id.txtEnt2);
        txtItem[2] = (TextView)findViewById(R.id.txtSide);
        txtItem[3] = (TextView)findViewById(R.id.txtVeg1);
        txtItem[4] = (TextView)findViewById(R.id.txtVeg2);
        txtItem[5] = (TextView)findViewById(R.id.txtBread);
		txtDay = (TextView)findViewById(R.id.txtDay);
		
		if(btnBreakfast.getId() == ((Button) arg0).getId()){
			time = 0;
			btnBreakfast.setText(Breakfast);
			btnLunch.setText(strLunch);
			btnDinner.setText(strDinner);
			for(i = 0; i < 6; i++)
	    	{
				txtItem[i].setText(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):"")?nodes.get(day).get(i+time).substring(2):nodes.get(day).get(i+time));
		    	if(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):""))
		    		imgItem[i].setBackgroundResource(map.get(nodes.get(day).get(i+time).substring(0,2)));
		    	else
		    		imgItem[i].setBackgroundResource(0);
	    	}
			
		}
		else if (btnLunch.getId() == ((Button) arg0).getId()){
			time = 7;
			btnBreakfast.setText(strBreakfast);
			btnLunch.setText(Lunch);
			btnDinner.setText(strDinner);
			for(i = 0; i < 6; i++)
	    	{
				txtItem[i].setText(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):"")?nodes.get(day).get(i+time).substring(2):nodes.get(day).get(i+time));
		    	if(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):""))
		    		imgItem[i].setBackgroundResource(map.get(nodes.get(day).get(i+time).substring(0,2)));
		    	else
		    		imgItem[i].setBackgroundResource(0);
	    	}
		}
		else if (btnDinner.getId() == ((Button) arg0).getId()){
			time = 15;
			btnBreakfast.setText(strBreakfast);
			btnLunch.setText(strLunch);
			btnDinner.setText(Dinner);
			for(i = 0; i < 6; i++)
	    	{
				txtItem[i].setText(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):"")?nodes.get(day).get(i+time).substring(2):nodes.get(day).get(i+time));
		    	if(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):""))
		    		imgItem[i].setBackgroundResource(map.get(nodes.get(day).get(i+time).substring(0,2)));
		    	else
		    		imgItem[i].setBackgroundResource(0);
	    	}
		}
		else if (btnNext.getId() == ((Button) arg0).getId()){
			if(day!=6)
				day++;
			else
				day = 0;
			txtDay.setText(Days[day]);
			for(i = 0; i < 6; i++)
	    	{
				txtItem[i].setText(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):"")?nodes.get(day).get(i+time).substring(2):nodes.get(day).get(i+time));
		    	if(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):""))
		    		imgItem[i].setBackgroundResource(map.get(nodes.get(day).get(i+time).substring(0,2)));
		    	else
		    		imgItem[i].setBackgroundResource(0);
	    	}
		}
		else if (btnPrevious.getId() == ((Button) arg0).getId()){
			if(day!=0)
				day--;
			else
				day = 6;
			txtDay.setText(Days[day]);
			for(i = 0; i < 6; i++)
	    	{
				txtItem[i].setText(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):"")?nodes.get(day).get(i+time).substring(2):nodes.get(day).get(i+time));
		    	if(map.containsKey(nodes.get(day).get(i+time).length()>2?nodes.get(day).get(i+time).substring(0,2):""))
		    		imgItem[i].setBackgroundResource(map.get(nodes.get(day).get(i+time).substring(0,2)));
		    	else
		    		imgItem[i].setBackgroundResource(0);
	    	}
		}
	}
}
