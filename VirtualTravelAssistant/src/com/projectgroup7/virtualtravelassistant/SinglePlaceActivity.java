package com.projectgroup7.virtualtravelassistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SinglePlaceActivity extends Activity {
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;
	
	// Place Details
	PlaceDetails placeDetails;
	
	// Progress dialog
	ProgressDialog pDialog;
	
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_place);
		
		Intent i = getIntent();
		
		// Place referece id
		String reference = i.getStringExtra(KEY_REFERENCE);
		
		// Calling a Async Background thread
		new LoadSinglePlaceDetails().execute(reference);
	}
	
	
	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SinglePlaceActivity.this);
			pDialog.setMessage("Loading profile ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Profile JSON
		 * */
		protected String doInBackground(String... args) {
			String reference = args[0];
			
			// creating Places class object
			googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
			try {
				placeDetails = googlePlaces.getPlaceDetails(reference);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		String website;
		
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					if(placeDetails != null){
						String status = placeDetails.status;
						
						// check place deatils status
						// Check for all possible status
						if(status.equals("OK")){
							if (placeDetails.result != null) {
								String name = placeDetails.result.name;
								String address = placeDetails.result.formatted_address;
								String rating = Double.toString(placeDetails.result.rating);
								String price_level = "";
								switch(placeDetails.result.price_level){
								case 0: 
									price_level = "Free";
									break;
								case 1:
									price_level = "Inexpensive";
									break;
								case 2:
									price_level = "Moderate";
									break;
								case 3:
									price_level = "Expensive";
									break;
								case 4:
									price_level = "Very Expensive";
									break;								
								}
								String phone = placeDetails.result.formatted_phone_number;
								String latitude = Double.toString(placeDetails.result.geometry.location.lat);
								String longitude = Double.toString(placeDetails.result.geometry.location.lng);
								website = placeDetails.result.website;
								
								Log.d("Place ", name + address + phone + latitude + longitude);
								
								// Displaying all the details in the view
								// single_place.xml
								TextView lbl_name = (TextView) findViewById(R.id.name);
								TextView lbl_address = (TextView) findViewById(R.id.address);
								TextView lbl_phone = (TextView) findViewById(R.id.phone);
								TextView lbl_location = (TextView) findViewById(R.id.location);
								TextView lbl_rating = (TextView) findViewById(R.id.rating);
								TextView lbl_price_level = (TextView) findViewById(R.id.price_level);
								TextView lbl_website = (TextView) findViewById(R.id.website);
								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Not present" : name; // if name is null display as "Not present"
								address = address == null ? "Not present" : address;
								rating = rating == null ? "Not present" : rating;
								price_level = price_level == null ? "Not present" : price_level;
								website = website == null ? "Not present" : website;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present" : latitude;
								longitude = longitude == null ? "Not present" : longitude;
								
								lbl_name.setText(name);
								lbl_address.setText(address);
								lbl_rating.setText(rating);
								lbl_price_level.setText(price_level);
								lbl_website.setText(website);
								lbl_website.setTextColor(Color.BLUE);
								lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
								lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
							
								lbl_website.setOnClickListener(new OnClickListener() {
							        public void onClick(View v) {
							          Intent i = new Intent(SinglePlaceActivity.this, WebViewActivity.class);
							          i.putExtra("WEBSITE_URL", website);
							          startActivity(i);
							          //startActivity(new Intent(SinglePlaceActivity.this, WebViewActivity.class));
							        }      
							    });
								
						//		Button button1 = (Button) findViewById(R.id.lianxi);  
						//		button1.setOnClickListener(new OnClickListener() {
						//			String url = placeDetails.result.formatted_phone_number;
						//			Intent bohao = new Intent(Intent.ACTION_CALL, Uri.parse(url));
						//			public void onClick(View v) { //Intent is an event which launches activities
						//				startActivity(bohao);
							//		}      
					//			});
								
								((Button)findViewById(R.id.lianxi)).setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										String url = placeDetails.result.formatted_phone_number;
										Intent myIntent = new Intent(Intent.ACTION_CALL);
										 String phNum = "tel:" + url;
										 myIntent.setData(Uri.parse(phNum));
										  startActivity( myIntent ) ;
									  }
									});
								
								
								
								
							}
						}
						else if(status.equals("ZERO_RESULTS")){
							alert.showAlertDialog(SinglePlaceActivity.this, "Near Places",
									"Sorry no place found.",
									false);
						}
						else if(status.equals("UNKNOWN_ERROR"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry unknown error occured.",
									false);
						}
						else if(status.equals("OVER_QUERY_LIMIT"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry query limit to google places is reached",
									false);
						}
						else if(status.equals("REQUEST_DENIED"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured. Request is denied",
									false);
						}
						else if(status.equals("INVALID_REQUEST"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured. Invalid Request",
									false);
						}
						else
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured.",
									false);
						}
					}else{
						alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
								"Sorry error occured.",
								false);
					}
					
					
				}
			});

		}

	}

}
