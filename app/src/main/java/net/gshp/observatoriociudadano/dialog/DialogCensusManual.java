package net.gshp.observatoriociudadano.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import net.gshp.observatoriociudadano.CensusManual;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;

/**
 * Created by leo on 17/02/18.
 */

public class DialogCensusManual extends DialogFragment implements View.OnClickListener {
    private View view;
    private Button btn_agree;
    private DtoBundle dtoBundle;
    private Double lat, lon;
    private LatLng latLng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_census_manual, container);
        init();
        return view;
    }

    public void setDtoBundle(DtoBundle dtoBundle, double lat, double lon) {
        this.dtoBundle = dtoBundle;
        this.lat = lat;
        this.lon = lon;
    }

    private void init() {
        btn_agree = view.findViewById(R.id.btn_agree);
        btn_agree.setOnClickListener(this);
        ChangeFontStyle.changeFont(btn_agree,view.findViewById(R.id.txt_info));
    }

    @Override
    public void onClick(View view) {
        Log.e("dtoBunlde", "dto dialog " + dtoBundle.getIdReportLocal());
        if (lat != null & lon != null) {
            latLng = new LatLng(lat, lon);
        }
        startActivity(new Intent(getActivity(), CensusManual.class).putExtra(getString(R.string.app_bundle_name), dtoBundle).
                putExtra(getString(R.string.latlon), latLng));
        dismiss();
        getActivity().finish();
    }
}
