package net.gshp.observatoriociudadano.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.gshp.observatoriociudadano.Census;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dto.DtoBundle;
import net.gshp.observatoriociudadano.dto.DtoReportCensus;
import net.gshp.observatoriociudadano.model.ModelCensus;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;

/**
 * Created by leo on 18/02/18.
 */

public class DialogDeleteCensus extends DialogFragment {

    private View view;
    private TextView txtAddress;
    private Button btn_acept;
    private DtoBundle dtoBundle;
    private DtoReportCensus dtoReportCensus;
    private ModelCensus modelCensus;

    public void setDto(DtoBundle dtoBundle, DtoReportCensus dtoReportCensus) {
        this.dtoBundle = dtoBundle;
        this.dtoReportCensus = dtoReportCensus;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_delete_census, container);
        init();
        return view;
    }

    private void init() {
        modelCensus = new ModelCensus(dtoBundle);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtAddress .setText(modelCensus.getAddressInfo());
        btn_acept = view.findViewById(R.id.btn_delete);
        btn_acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelCensus.saveCensus(dtoReportCensus);
                Toast.makeText(getActivity(), "Se guardo correctamente", Toast.LENGTH_SHORT).show();
                dismiss();
                getActivity().finish();
            }
        });
        ChangeFontStyle.changeFont(txtAddress,btn_acept,view.findViewById(R.id.txt_delete_visit));

    }

}
