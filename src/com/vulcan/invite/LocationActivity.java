package com.vulcan.invite;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;







public class LocationActivity extends Activity implements OnMapReadyCallback, LocationListener {
	
	
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;
	TextView txtLat;
	String lat;
	String provider;
	protected String latitude,longitude; 
	protected boolean gps_enabled,network_enabled;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        
        System.out.println("IN MainAct OnCreate");
        
     


        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("hasLat", true);

        

        System.out.println("After editor.putBoolean(hasLat, true);");
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000, this);

       
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        
        System.out.println("After mapFragment.getMapAsync(this)");
        
        final GoogleMap map;
        map = mapFragment.getMap();
        
        System.out.println("After map = mapFragment.getMap()");
        
        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Log.d("Map","Map clicked");
                
                TextView myLocationText = (TextView) findViewById(R.id.loc);
                
                String latLongString;
                double lat= point.latitude;
                double lon= point.longitude;
                
               
                
               String lati = lat + "";
               String longi = lon + ""; 
                
                latLongString = "Lat:" + lat + "\nLong:" + lon;
                
              
                String latitude = "lat:"+ lat;
                SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                
               
              editor.putString("lati",lati);
              editor.putString("longi",longi);
                
                editor.putBoolean("hasLat", true);
                
                
                editor.putString("LAT_LON",latLongString);
                
                
                
                
                
                System.out.println("Lat in LocationActivity"+latitude);
                String Sare_lat = sharedpreferences.getString("LAT_LON", "");
                System.out.println("Lat in SharedPref "+Sare_lat);
                
                
                
                
                System.out.println("lat"+ latLongString);
                myLocationText.setText("Event Location:\n"+latLongString);
                
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                System.out.println("Outside try");
                try {
                	System.out.println("Inside try");
                	
        			List<Address> myList = geocoder.getFromLocation(lat, lon, 1);
        			System.out.println(myList);
        			if (myList != null && myList.size() > 0) {
                        Address address = myList.get(0);
                        String city = address.getLocality();
                        String Area = address.getFeatureName();
                        String AddressLine = address.getAddressLine(1);
                        
                        System.out.println("City = " + city);
                        System.out.println("feature = " + Area);
                        System.out.println("AddressLine = " + AddressLine);
                        
                        editor.putString("AddressLine",AddressLine);
                        editor.putString("City",city);
                        editor.commit();

                    }
        			
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			System.out.println("Inside Catch");
        			e.printStackTrace();
        		}
               
      
                map.clear();

	           // SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
				String AddressLine = sharedpreferences.getString("AddressLine", "");
				String city = sharedpreferences.getString("city", "");
                map.addMarker(new MarkerOptions()
                .title(AddressLine)
                .snippet(city)
                .position(point));
//                marker.remove();
//                drawMarker(point);
            }
            
        });
        
        
      
        
        //direction = new GMapV2Direction()    
    }
    
    @Override
    public void onMapReady(GoogleMap map) {
    	
    	System.out.println("In onMapReady");
    	
    	LocationManager locationManager;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
       
        
        System.out.println("After Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);");
        
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        
        System.out.println("After double lng = location.getLongitude();");
        
        LatLng sydney = new LatLng(lat, lng);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        
        System.out.println("After map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));");
        
        map.getMyLocation();
        
        System.out.println("After map.getMyLocation();");
        if (map.getMyLocation() != null) {

            Toast.makeText(this, "You are here", Toast.LENGTH_LONG).show();
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                    map.getMyLocation().getLatitude(), map.getMyLocation()
                            .getLongitude()), 15));
            
            System.out.println("Lat"+map.getMyLocation().getLatitude());
            System.out.println("Lon"+map.getMyLocation().getLongitude());
        }
       
       // map.moveCamera(map.getMyLocation());
        
        
//        map.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));
//        
        
        
       
        
        
        
    }




	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		txtLat = (TextView) findViewById(R.id.textview1);
		txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
		System.out.println(location.getLatitude());
		
	}




	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.d("Latitude","status");
		
	}




	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Latitude","enable");
		
	}




	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Provider","disable");
		
	}
	
	public void Set(View view)
	{
		startActivity(new Intent(LocationActivity.this, NewEvent.class));
		finish();
	}
    
    
    
    
}