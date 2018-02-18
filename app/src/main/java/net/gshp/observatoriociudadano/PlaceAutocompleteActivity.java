package net.gshp.observatoriociudadano;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import net.gshp.observatoriociudadano.dto.DtoReportCensus;
import net.gshp.observatoriociudadano.dto.DtoSepomex;

/**
 * Created by leo on 16/02/18.
 */

public class PlaceAutocompleteActivity extends AppCompatActivity {

    private DtoReportCensus dtoReportCensusBundle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        dtoReportCensusBundle = (DtoReportCensus) getIntent().getExtras().get(getString(R.string.address));
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment);
        if (dtoReportCensusBundle != null) {
            autocompleteFragment.setText(dtoReportCensusBundle.getAddress());
        }
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build();

        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("leo", "Place: " + place.getName());
                startIntent(place.getLatLng().latitude, place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                Log.i("leo", "An error occurred: " + status);
            }
        });
    }

    private void startIntent(double lat, double lon) {
        Log.e("leo","start");
        dtoReportCensusBundle.setLat(lat);
        dtoReportCensusBundle.setLon(lon);
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.address), dtoReportCensusBundle);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }
}
