package net.gshp.observatoriociudadano;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dialog.DialogSaveCensus;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoReportCensus;
import net.gshp.observatoriociudadano.listener.OnFinishLocation;
import net.gshp.observatoriociudadano.model.ModelCensus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by leo on 15/02/18.
 */

public class Census extends AppCompatActivity implements OnMapReadyCallback, OnFinishLocation, View.OnClickListener {

    private DtoBundle dtoBundle;
    private EditText edt_address;
    private Button btn_save;
    private ModelCensus modelCensus;
    private GoogleMap map;
    private MapView mapView;
    public boolean setLocation = false;
    private double lat, lon;
    private DialogSaveCensus dialogSaveCensus;
    private DtoReportCensus dtoReportCensus;
    private GoogleMap.OnCameraChangeListener onCameraChangeListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_census);
        getSupportActionBar().hide();
        init(savedInstanceState);

    }

    private void init(Bundle savedInstanceState) {
        dtoBundle = (DtoBundle) getIntent().getExtras().get(getString(R.string.app_bundle_name));
        btn_save = findViewById(R.id.btn_save);
        edt_address = findViewById(R.id.edt_address);
        edt_address.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        modelCensus = new ModelCensus(this, this);
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        setUpMapIfNeeded();
        edt_address.setOnClickListener(this);

    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            mapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            setUpMap();
            configureCameraIdle();
            map.setOnCameraChangeListener(onCameraChangeListener);
            modelCensus.onStart();
        }
    }

    private void setUpMap() {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != -1
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != -1) {
        }
        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(new LatLng(22.7,
                -102.6), 3);
        map.moveCamera(zoom);

    }

    @Override
    public void onFinishLocation(Location location) {
        if (location != null) {
            if (map != null) {
                map.clear();
                if (!setLocation) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                            location.getLongitude()), 16), 2000, null);
                    setLocation = true;
                }
                lat = location.getLatitude();
                lon = location.getLongitude();
                setDirections(lat, lon);
            }
        }
    }

    private void setDirections(double lat, double lon) {
        try {
            Geocoder geo = new Geocoder(ContextApp.context, Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(lat, lon, 1);
            if (!addresses.isEmpty()) {
                if (addresses.size() > 0) {
                    Log.e("adress", "Address "
                            + "FeatureName: " + addresses.get(0).getFeatureName() + ", \n"
                            + "County Name: " + addresses.get(0).getCountryName() + ", \n"
                            + "Admin Area: " + addresses.get(0).getAdminArea() + ",\n "
                            + "Locality: " + addresses.get(0).getLocality() + ", \n"
                            + "Postal Code: " + addresses.get(0).getPostalCode() + ",\n "
                            + "Max Adrees " + addresses.get(0).getAddressLine(0) + ", \n"
                            + "Max Adrees " + addresses.get(0).getAddressLine(1) + ", \n"
                            + "Max Adrees " + addresses.get(0).getAddressLine(2) + ", \n"
                            + "Max Adrees " + addresses.get(0).getAddressLine(3) + ", \n"
                            + "Max Adrees " + addresses.get(0).getAddressLine(4) + ", \n"
                            + "Max Adrees " + addresses.get(0).getAddressLine(5) + ", \n"
                            + "SubAdmin " + addresses.get(0).getSubAdminArea() + ", \n"
                            + "SubLocality " + addresses.get(0).getSubLocality() + ", \n"
                            + "SubThoroughfare " + addresses.get(0).getSubThoroughfare() + ", \n"
                            + "Thoroughfare " + addresses.get(0).getThoroughfare() + ", \n"
                            + "Locale: " + addresses.get(0).getLocale() + ", "
                            + " size: " + addresses.size());
                    edt_address.setText(addresses.get(0).getAddressLine(0));
                    dtoReportCensus = new DtoReportCensus();
                    dtoReportCensus.setLat(lat);
                    dtoReportCensus.setLon(lon);
                    dtoReportCensus.setAddress(addresses.get(0).getAddressLine(0));
                    dtoReportCensus.setCp(addresses.get(0).getPostalCode());
                    dtoReportCensus.setExternalNumber(addresses.get(0).getFeatureName());
                    dtoReportCensus.setSend(0);
                    dtoReportCensus.setIdReporteLocal(dtoBundle.getIdReportLocal());
                    dtoReportCensus.setProvider(getString(R.string.providerAutomatic));
                    dtoReportCensus.setName_street(addresses.get(0).getThoroughfare());
                    dtoReportCensus.setSuburb(addresses.get(0).getSubLocality());
                    dtoReportCensus.setTown(addresses.get(0).getLocality());
                    dtoReportCensus.setState(addresses.get(0).getAdminArea());


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void configureCameraIdle() {
        onCameraChangeListener = new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                setDirections(cameraPosition.target.latitude, cameraPosition.target.longitude);
            }
        };

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        modelCensus.onStopGeo();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_address:
                dtoReportCensus.setAddress(edt_address.getText().toString());
                Intent intent = new Intent(this, PlaceAutocompleteActivity.class).putExtra(getString(R.string.address), dtoReportCensus);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_save:
                Log.e("leo", "census " + dtoReportCensus.toString());
                FragmentManager fm = getSupportFragmentManager();
                dialogSaveCensus = new DialogSaveCensus();
                dialogSaveCensus.setBundle(dtoReportCensus);
                dialogSaveCensus.show(fm, "dialog");
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                DtoReportCensus dtoReportCensus = (DtoReportCensus) data.getExtras().get(getString(R.string.address));
                setDirections(dtoReportCensus.getLat(), dtoReportCensus.getLon());
            }

        }
    }
}




    /*private void init() {
        modelCensus = new ModelCensus();
        txt_search = findViewById(R.id.txt_search);
        txt_search.setThreshold(1);
        txt_search.setAdapter(modelCensus.getAdapterAutocomplete());
        edt_address = findViewById(R.id.edt_address);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetCoordinates().execute(edt_address.getText().toString().replace("", "+"));
            }
        });

    }

    private class GetCoordinates extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(Census.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Espere");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try {
                String address = strings[0];
                HttpDataHandler httpDataHandler = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&components=country:MEX&key=AIzaSyDS0DNkryOziysua2mruAiEHrOzu7a63AY", address);
                String url1 =
                        "http://maps.googleapis.com/maps/api/geocode/json?components=postal_code:11560&sensor=false&components=country:MEX";

                response = httpDataHandler.getHTTPData(url);
                return response;

            } catch (Exception e) {

            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();
                String text = ((JSONArray) jsonObject.get("results")).getJSONObject(0).get("formatted_address").toString();
                Log.e("leo", "coordenadas lat " + lat + "lon" + lng + " text " + text);
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/