package net.gshp.observatoriociudadano.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.gshp.observatoriociudadano.Census;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.Visit;
import net.gshp.observatoriociudadano.dto.DtoReportVisit;
import net.gshp.observatoriociudadano.model.ModelVisit;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;
import net.gshp.observatoriociudadano.util.Config;

/**
 * Created by leo on 18/02/18.
 */

public class DialogDeleteVisit extends DialogFragment implements View.OnClickListener {

    private View view;
    private ModelVisit modelVisit;
    private DtoReportVisit dtoReportVisit;
    private Button btn_accept;
    private TextView txt_pdv, txt_date;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_delete_visit, container);
        init();
        return view;
    }

    private void init() {
        modelVisit = new ModelVisit();
        btn_accept = view.findViewById(R.id.btn_delete);
        btn_accept.setOnClickListener(this);
        txt_date = view.findViewById(R.id.txt_date);
        txt_pdv = view.findViewById(R.id.txt_pdv);
        txt_pdv.setText(dtoReportVisit.getName());
        txt_date.setText(Config.formatDateFromCurrentMillis(dtoReportVisit.getDateCheckIn(), "dd MMMM yyyy"));
        ChangeFontStyle.changeFont(btn_accept,txt_date,txt_pdv,view.findViewById(R.id.txt_delete_visit));
    }

    @Override
    public void onClick(View view) {

        modelVisit.inactivateReport(dtoReportVisit.getId(), dtoReportVisit.getHash());
        Toast.makeText(getActivity(), "Reporte Eliminado", Toast.LENGTH_SHORT).show();
        ((Visit) getActivity()).onFinishSendReports();
        dismiss();

    }

    public void setDtoReportVisit(DtoReportVisit dtoReportVisit) {
        this.dtoReportVisit = dtoReportVisit;
    }
}
