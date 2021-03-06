package net.gshp.observatoriociudadano;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dialog.DialogCensusManual;
import net.gshp.observatoriociudadano.dialog.DialogDeleteCensus;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoReportCensus;
import net.gshp.observatoriociudadano.listener.OnFinishLocation;
import net.gshp.observatoriociudadano.model.ModelCensus;
import net.gshp.observatoriociudadano.model.ModelInfoPerson;

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
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private PlaceAutocompleteFragment autocompleteFragment;

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
        modelCensus = new ModelCensus(this, this, dtoBundle);
        btn_save = findViewById(R.id.btn_save);
        edt_address = findViewById(R.id.edt_address);
        edt_address.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        setUpMapIfNeeded();
        new ModelInfoPerson(this).loadImage(this).loadInfo("REGISTRO DE DOMICILIO");
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build();

        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("leo", "Place: " + place.getName());
                setDirections(place.getLatLng().latitude, place.getLatLng().longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(place.getLatLng()).zoom(20).bearing(45).tilt(90).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }


            @Override
            public void onError(Status status) {
                Log.i("leo", "An error occurred: " + status);
            }


        });

    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            mapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(22.7, -102.6)).zoom(20).bearing(45).tilt(90).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onFinishLocation(Location location) {
        if (location != null) {
            if (map != null) {
                map.clear();
                if (!setLocation) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(),
                            location.getLongitude())).zoom(20).bearing(45).tilt(90).build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
                    autocompleteFragment.setText(addresses.get(0).getAddressLine(0));
                    dtoReportCensus = new DtoReportCensus();
                    dtoReportCensus.setLat(lat);
                    dtoReportCensus.setLon(lon);
                    dtoReportCensus.setAddress(addresses.get(0).getAddressLine(0));
                    dtoReportCensus.setCp(addresses.get(0).getPostalCode());
                    dtoReportCensus.setExternalNumber(addresses.get(0).getFeatureName());
                    dtoReportCensus.setSend(0);
                    dtoReportCensus.setProvider(getString(R.string.providerAutomatic));
                    dtoReportCensus.setSuburb(addresses.get(0).getSubLocality());
                    dtoReportCensus.setTown(addresses.get(0).getLocality());
                    dtoReportCensus.setState(addresses.get(0).getAdminArea());


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpDialogCensusManual() {
        DialogCensusManual dialogCensusManual = new DialogCensusManual();
        dialogCensusManual.setDtoBundle(dtoBundle, lat, lon);
        dialogCensusManual.show(getSupportFragmentManager(), "dialog");
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
                if (edt_address.getText().toString().isEmpty()) {
                    setUpDialogCensusManual();
                }
                break;
            case R.id.btn_save:
                if (edt_address.getText().toString().isEmpty()) {
                    setUpDialogCensusManual();
                } else if (modelCensus.isCompleteCensus()) {
                    DialogDeleteCensus dialogDeleteVisit = new DialogDeleteCensus();
                    dialogDeleteVisit.setDto(dtoBundle, dtoReportCensus);
                    dialogDeleteVisit.show(getSupportFragmentManager(), "dialogDelete");
                } else {
                    saveCensus();
                }
        }

    }

    public void saveCensus() {
        modelCensus.saveCensus(dtoReportCensus);
        Toast.makeText(this, "Se guardo correctamente ", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DtoReportCensus dtoReportCensus = (DtoReportCensus) data.getExtras().get(getString(R.string.address));
                setDirections(dtoReportCensus.getLat(), dtoReportCensus.getLon());
                CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(new LatLng(dtoReportCensus.getLat(),
                        dtoReportCensus.getLon()), 18);
                map.moveCamera(zoom);

            }

        }*/

    }
}

