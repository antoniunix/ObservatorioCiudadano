package net.gshp.observatoriociudadano;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import net.gshp.apiencuesta.Encuesta;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dialog.DialogMessageGeneric;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.geolocation.ServiceCheck;
import net.gshp.observatoriociudadano.faceDetection.PhotoWizardActivity;
import net.gshp.observatoriociudadano.model.ModelAHBottomNavigationMenuReport;
import net.gshp.observatoriociudadano.model.ModelInfoPerson;
import net.gshp.observatoriociudadano.model.ModelMenuReport;
import net.gshp.observatoriociudadano.util.Config;

public class MenuReport extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private ModelMenuReport modelMenuReport;
    private ModelAHBottomNavigationMenuReport modelAHBottomNavigationMenuReport;
    private DtoBundle dtoBundle;

    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFrag;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    private void init() {
        dtoBundle = (DtoBundle) getIntent().getExtras().get(getString(R.string.app_bundle_name));
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        new ModelInfoPerson(this).loadImage(this);
        modelMenuReport = new ModelMenuReport(dtoBundle);
        new ModelInfoPerson(this).loadImage(this).loadInfo();
        modelAHBottomNavigationMenuReport = new ModelAHBottomNavigationMenuReport(this, modelMenuReport, this, dtoBundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_report);
        getSupportActionBar().hide();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dtoBundle.getIdReportLocal() == 0) {
            modelMenuReport.createNewReport(this);
        }
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        DialogMessageGeneric dialog = new DialogMessageGeneric();
        int statusReportSupervisor, statusReportCensusSupervisor;
        if (dtoBundle.getIdTypeMenuReport() == getResources().getInteger(R.integer.idPollSupervisor)) {
            switch (position) {
                case 0://poll supervisor
                    startActivity(new Intent(this, Encuesta.class)
                            .putExtra("numeroEncuesta", 0)
                            .putExtra("idReporte", dtoBundle.getIdReportLocal())
                            .putExtra("idEncuesta", (long) ContextApp.context.getResources().getInteger(R.integer.idPollSupervisor)));
                    break;
                case 1:// census
                    statusReportSupervisor = modelMenuReport.isReportPollSup();

                    if (statusReportSupervisor != getResources().getInteger(R.integer.statusModuleReportDone)) {
                        dialog.setData("ENCUESTA SUPERVISOR", "Debe completar la encuesta primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else {
                        startActivity(new Intent(this, Census.class).putExtra(getString(R.string.app_bundle_name), dtoBundle));
                    }


                    break;
                case 2://Photos
                    statusReportSupervisor = modelMenuReport.isReportPollSup();
                    statusReportCensusSupervisor = modelMenuReport.isReportSupCompleteCensus();

                    if (statusReportSupervisor != getResources().getInteger(R.integer.statusModuleReportDone)) {
                        dialog.setData("ENCUESTA SUPERVISOR", "Debe completar la encuesta primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else if (statusReportCensusSupervisor == getResources().getInteger(R.integer.statusModuleReportNotDone)) {
                        dialog.setData("DIRECCIÓN SUPERVISOR", "Debe completar la dirección de supervisor primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else {
                        Intent intent = new Intent(this, PhotoWizardActivity.class);
                        intent.putExtra(getString(R.string.user_roll), getResources().getInteger(R.integer.rollSupervisor));
                        intent.putExtra(getString(R.string.app_bundle_name), dtoBundle);
                        startActivity(intent);
                    }


                    break;
                case 3://check out
                    statusReportSupervisor = modelMenuReport.isReportPollSup();
                    statusReportCensusSupervisor = modelMenuReport.isReportSupCompleteCensus();

                    if (statusReportSupervisor != getResources().getInteger(R.integer.statusModuleReportDone)) {
                        dialog.setData("ENCUESTA SUPERVISOR", "Debe completar la encuesta primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else if (statusReportCensusSupervisor == getResources().getInteger(R.integer.statusModuleReportNotDone)) {
                        dialog.setData("DIRECCIÓN SUPERVISOR", "Debe completar la dirección de supervisor primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else {
                        startService(new Intent(ContextApp.context, ServiceCheck.class).
                                putExtra(getString(R.string.app_bundle_name), dtoBundle).
                                putExtra("typeCheck", getResources().getInteger(R.integer.type_check_out)));
                        finish();
                    }


                    break;
            }

        } else if (dtoBundle.getIdTypeMenuReport() == getResources().getInteger(R.integer.idPollRepresentanteCasilla)) {
            int statusReportCasilla, statusReportRepCasilla;
            switch (position) {
                case 0:// poll station
                    startActivity(new Intent(this, Encuesta.class)
                            .putExtra("numeroEncuesta", 0)
                            .putExtra("idReporte", dtoBundle.getIdReportLocal())
                            .putExtra("idEncuesta", (long) ContextApp.context.getResources().getInteger(R.integer.idPollRepresentanteCasilla)));
                    break;
                case 1:// poll representative
                    statusReportRepCasilla = modelMenuReport.isReportPollStation(dtoBundle, ContextApp.context.getResources().getInteger(R.integer.idPollRepresentanteCasilla));
                    if (statusReportRepCasilla != getResources().getInteger(R.integer.statusModuleReportDone)) {
                        dialog.setData("ENCUESTA REPRESENTANTE", "Debe completar la encuesta de representante primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else {
                        startActivity(new Intent(this, Encuesta.class)
                                .putExtra("numeroEncuesta", 0)
                                .putExtra("idReporte", dtoBundle.getIdReportLocal())
                                .putExtra("idEncuesta", (long) ContextApp.context.getResources().getInteger(R.integer.idPollCasilla)));

                    }

                    break;
                case 2://census

                    statusReportRepCasilla = modelMenuReport.isReportPollStation(dtoBundle, ContextApp.context.getResources().getInteger(R.integer.idPollRepresentanteCasilla));
                    statusReportCasilla = modelMenuReport.isReportPollStation(dtoBundle, ContextApp.context.getResources().getInteger(R.integer.idPollCasilla));

                    if (statusReportRepCasilla != getResources().getInteger(R.integer.statusModuleReportDone)) {
                        dialog.setData("ENCUESTA REPRESENTANTE", "Debe completar la encuesta de reporesentante primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else if (statusReportCasilla != getResources().getInteger(R.integer.statusModuleReportDone)) {
                        dialog.setData("ENCUESTA CASILLA", "Debe completar la encuesta de casilla primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else {
                        startActivity(new Intent(this, Census.class).putExtra(getString(R.string.app_bundle_name), dtoBundle));
                    }


                    break;
                case 3://photo
                    statusReportRepCasilla = modelMenuReport.isReportPollStation(dtoBundle, ContextApp.context.getResources().getInteger(R.integer.idPollRepresentanteCasilla));
                    statusReportCasilla = modelMenuReport.isReportPollStation(dtoBundle, ContextApp.context.getResources().getInteger(R.integer.idPollCasilla));
                    statusReportCensusSupervisor = modelMenuReport.isReportRepCompleteCensus();

                    if (statusReportRepCasilla != getResources().getInteger(R.integer.statusModuleReportDone)) {
                        dialog.setData("ENCUESTA REPRESENTANTE", "Debe completar la encuesta de reporesentante primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else if (statusReportCasilla != getResources().getInteger(R.integer.statusModuleReportDone)) {
                        dialog.setData("ENCUESTA CASILLA", "Debe completar la encuesta de casilla primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else if (statusReportCensusSupervisor == getResources().getInteger(R.integer.statusModuleReportNotDone)) {
                        dialog.setData("DIRECCIÓN REPRESENTANTE", "Debe completar la dirección de representante primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else {
                        Intent intent = new Intent(this, PhotoWizardActivity.class);
                        intent.putExtra(getString(R.string.user_roll), getResources().getInteger(R.integer.rollRepresentanteCasilla));
                        intent.putExtra(getString(R.string.app_bundle_name), dtoBundle);
                        startActivity(intent);

                    }
                    break;
                case 4://CHECKOUT

                    statusReportRepCasilla = modelMenuReport.isReportPollStation(dtoBundle, ContextApp.context.getResources().getInteger(R.integer.idPollRepresentanteCasilla));
                    statusReportCasilla = modelMenuReport.isReportPollStation(dtoBundle, ContextApp.context.getResources().getInteger(R.integer.idPollCasilla));
                    statusReportCensusSupervisor = modelMenuReport.isReportRepCompleteCensus();

                    if (statusReportRepCasilla != getResources().getInteger(R.integer.statusModuleReportDone)) {
                        dialog.setData("ENCUESTA REPRESENTANTE", "Debe completar la encuesta de reporesentante primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else if (statusReportCasilla != getResources().getInteger(R.integer.statusModuleReportDone)) {
                        dialog.setData("ENCUESTA CASILLA", "Debe completar la encuesta de casilla primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else if (statusReportCensusSupervisor == getResources().getInteger(R.integer.statusModuleReportNotDone)) {
                        dialog.setData("DIRECCIÓN REPRESENTANTE", "Debe completar la dirección de representante primero", 0).
                                setShowButton(false, true);
                        dialog.show(getSupportFragmentManager(), "");
                    } else {
                        startService(new Intent(ContextApp.context, ServiceCheck.class).
                                putExtra(getString(R.string.app_bundle_name), dtoBundle).
                                putExtra("typeCheck", getResources().getInteger(R.integer.type_check_out)));
                        finish();

                    }

                    break;
            }
        }

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
//        mLastLocation = location;
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20).bearing(45).tilt(90).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mGoogleMap.clear();

    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MenuReport.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
