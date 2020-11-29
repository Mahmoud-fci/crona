package com.abdo.crona;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient mFusedLocationClient;
    Double Lat = 0.0, Longt = 0.0;
    String MyAddress = "non";
    Button ok_button;
    Marker marker;
    ImageView backbt;

    LatLng distnation = null;

    GoogleMap googleMap;
    RequestQueue requestQueue;
    List<LatLng> decoded;

    final Polyline[] polyline = new Polyline[1];

    ArrayList<Hospital> Hospitails = new ArrayList<>();


    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Lat = mLastLocation.getLatitude();
            Longt = mLastLocation.getLongitude();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT <= 25) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                Log.d("skdhigh", "sdklow");

            } else {

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);

                Log.d("skdhigh", "skdhigh");


            }

            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().setNavigationBarColor(Color.WHITE);

        }

        setContentView(R.layout.activity_maps);
        ok_button = findViewById(R.id.ok_button);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hospital hos1 = new Hospital(29.978781, 31.436, "مستشفي القاهره الجديدة");
                Hospital hos2 = new Hospital(30.0282706, 31.1964546, "مستشفي بولاق");
                Hospital hos3 = new Hospital(30.062819, 31.295002, "مستشفي صدر العباسية ");
                Hospital hos4 = new Hospital(Lat, Longt, "مستشفي صدر احا ");



                Hospitails.add(hos1);
                Hospitails.add(hos2);
                Hospitails.add(hos3);
                Hospitails.add(hos4);


                Hospital hosSelected = getNearest(Hospitails);

                GetAddressLine(getMyAddress(hosSelected.getLat(), hosSelected.getLongt()), Lat, Longt);


            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }// Add a marker in Sydney and move the camera


    }


    private void getLastLocation() {
        if (true) {
            if (true) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
//                                    latTextView.setText(location.getLatitude()+"");
//                                    lonTextView.setText(location.getLongitude()+"");
                                    Lat = location.getLatitude();
                                    Longt = location.getLongitude();
                                    LatLng sydney = new LatLng(Lat, Longt);
                                    //      mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                                    marker = mMap.addMarker(new MarkerOptions()
                                            .position(sydney)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                            .anchor(0.5f, 0.8f)
                                            .title(MyAddress));


                                    LatLng NewCairo = new LatLng(29.978781, 31.436);
                                    marker = mMap.addMarker(new MarkerOptions()
                                            .position(NewCairo)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                            .anchor(0.5f, 0.8f)
                                            .title("مستشفي القاهره الجديدة"));

                                    LatLng Manshia = new LatLng(30.062819, 31.295002);
                                    marker = mMap.addMarker(new MarkerOptions()
                                            .position(Manshia)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                            .anchor(0.5f, 0.8f)
                                            .title("مستشفي صدر العباسية "));


                                    LatLng Bolak = new LatLng(30.0282706, 31.1964546);
                                    marker = mMap.addMarker(new MarkerOptions()
                                            .position(Bolak)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                            .anchor(0.5f, 0.8f)
                                            .title("مستشفي بولاق"));


                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Lat, Longt), 14.0f));

                                    MyAddress = getMyAddress(Lat, Longt);
                                    Toast.makeText(MapsActivity.this, MyAddress, Toast.LENGTH_LONG).show();


                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            //   requestPermissions();
        }
    }

    void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    String getMyAddress(Double latw, Double Longt) {
        String Addresss = "Non";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latw, Longt, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Toast.makeText(MapsActivity.this, address, Toast.LENGTH_LONG).show();
            Addresss = address;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return Addresss;
    }

    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions) {
        //  map.clear();
//        Toast.makeText(First.this,Double.toString(positions.get(1).latitude),Toast.LENGTH_LONG).show();
//        Toast.makeText(First.this,Double.toString(positions.get(1).longitude),Toast.LENGTH_LONG).show();
        PolylineOptions options = new PolylineOptions().width(6).color(this.getResources().getColor(R.color.order_not_selected)).geodesic(true);
        options.addAll(positions);
        Polyline polyline = map.addPolyline(options);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(positions.get(1).latitude, positions.get(1).longitude))
                .zoom(17)
                .build();
        distnation = new LatLng(positions.get(positions.size() - 1).latitude, positions.get(positions.size() - 1).longitude);
        mMap.addMarker(new MarkerOptions().position(distnation).title("مكان الوصول"));
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    public void GetAddressLine(String Addres, double lat, double longg) {

        requestQueue = Volley.newRequestQueue(MapsActivity.this);


        final String[] distance = {""};

        Geocoder geocoder =
                new Geocoder(MapsActivity.this, Locale.getDefault());

        final List<LatLng> positions = new ArrayList<>();

        String showURL = "https://maps.googleapis.com/maps/api/directions/json";
        final ProgressDialog loading;
        try {
            polyline[0].remove();
        } catch (Exception ewe) {
        }
        loading = ProgressDialog.show(MapsActivity.this, " ..", "", true, true);

        final Boolean[] state = {false};

        String data = null;
        try {
            String org = Double.toString(lat);
            org += ",";
            org += Double.toString(longg);


            data = "?origin=" + URLEncoder.encode(org, "UTF-8");
            data += "&destination=" + URLEncoder.encode(Addres, "UTF-8");
            data += "&key=" + URLEncoder.encode("AIzaSyAAzXo5BHq_tVgIIOdO1b2juR2Ie4VAPN0", "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, showURL + data
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONArray routes = response.getJSONArray("routes");
                    JSONArray legs = ((JSONObject) routes.get(0)).getJSONArray("legs");

                    JSONObject overview_polyline = routes.getJSONObject(0);
                    JSONObject overview_polyline2 = overview_polyline.getJSONObject("overview_polyline");
                    String points = overview_polyline2.getString("points");
                    decoded = PolyUtil.decode(points);

                    for (int i = 0; i < legs.length(); i++) {


                        JSONObject leg = legs.getJSONObject(i);

                        String e = leg.getString("distance");
                        distance[0] = e;

                        JSONObject duration = leg.getJSONObject("duration");


                        Log.d("distance", e);


                        JSONArray steps = ((JSONObject) legs.get(0)).getJSONArray("steps");
                        for (int j = 0; j < steps.length(); j++) {

                            JSONObject step = steps.getJSONObject(j);
                            String start_location = step.getString("start_location");
                            String end_location = step.getString("end_location");

                            Log.d("Locations", start_location);

                            String[] parts = start_location.split(",");

                            String[] lats = parts[0].split(":");

                            String[] lngs = parts[1].split(":");

                            double latt = Double.parseDouble(lats[1]);

                            lngs[1] = lngs[1].replace("}", "");
                            lngs[1] = lngs[1].replace("\"", "");

                            double lngg = Double.parseDouble(lngs[1]);


                            String[] parts2 = end_location.split(",");

                            String[] lats2 = parts2[0].split(":");

                            String[] lngs2 = parts2[1].split(":");

                            double latt2 = Double.parseDouble(lats2[1]);

                            lngs2[1] = lngs2[1].replace("}", "");
                            lngs2[1] = lngs2[1].replace("\"", "");

                            double lngg2 = Double.parseDouble(lngs2[1]);

                            if (state[0] == true) {
                                positions.add(new LatLng(lngg, latt));
                                positions.add(new LatLng(lngg2, latt2));
                            } else {
                                positions.add(new LatLng(latt, lngg));
                                positions.add(new LatLng(latt2, lngg2));
                            }

                        }

                        drawRouteOnMap(mMap, decoded);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                loading.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //       Toast.makeText(First.this,"خطأ",Toast.LENGTH_LONG).show();
                loading.dismiss();
            }

        });
        requestQueue.add(jsonObjectRequest);
    }


    Hospital getNearest(ArrayList<Hospital> hospitails) {
        double dis = 9999999;
        int selectedItem = 0;


        for (int i = 0; i < hospitails.size(); i++) {

            double thisdis = distance(hospitails.get(i).getLat(), hospitails.get(i).getLongt(), Lat, Longt);

            if (dis < thisdis) {

                dis = thisdis;
                selectedItem = i;
            }

        }

        return hospitails.get(selectedItem);
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}
