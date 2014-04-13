package com.projectgroup7.virtualtravelassistant;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MenuActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.menu);

	    GridView gridview = (GridView) findViewById(R.id.gridview); //access id=gridview created in main.xml
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	switch(position){
	        	case 0:
	        		Search.value = "museum|aquarium|zoo";
	        		break;
	        	case 1: 
	        		Search.value = "cafe|restaurant";
	        		break;
	        	case 2:
	        		Search.value = "lodging";
	        		break;
	        	case 3:
	        		Search.value = "bus_station|airport|taxi_stand|train_station|subway_station";
	        		break;
	        	case 4:
	        		Search.value = "shopping_mall|clothing_store";
	        		break;
	        	case 5:
	        		Search.value = "doctor|dentist";
	        		break;
	        	case 6:
	        		Search.value = "amusement_park";
	        		break;
	        	}
	        	startActivity(new Intent(MenuActivity.this, AttractionsActivity.class));
	        }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_grid_view, menu);
		return true;
	}

}