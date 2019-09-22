package com.vulcan.invite;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements OnMapReadyCallback {
 
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.activity_main);

    LocationManager locationManager;
    locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    
    updateWithNewLocation(location);

    
//    MapFragment mapFragment = (MapFragment) getFragmentManager()
//            .findFragmentById(R.id.map);
//    mapFragment.getMapAsync(this);
//    
    
    
//    try{
//    updateWithNewLocation(location);
//    }
//    catch(Exception e)
//    {
//    	System.out.println("Exception Caught");
//    }
  }
  
  /** Update UI with a new location */
  private void updateWithNewLocation(Location location) {
    TextView myLocationText = (TextView)findViewById(R.id.textview1);
    
    String latLongString;
     
    if (location != null) {
      double lat = location.getLatitude();
      double lng = location.getLongitude();
      latLongString = "Lat:" + lat + "\nLong:" + lng;
    } else {
      latLongString = "No location found"; 
    }
    
    myLocationText.setText("Your Current Position is:\n" + latLongString);
    
  }

@Override
public void onMapReady(GoogleMap googleMap) {
	// TODO Auto-generated method stub
	
}

}