package net.gshp.observatoriociudadano.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.listener.OnDissmisDialogListener;
import net.gshp.observatoriociudadano.listener.OnProgressSync;
import net.gshp.observatoriociudadano.model.ModelSincronizar;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;
import net.gshp.observatoriociudadano.util.SharePreferenceCustom;

import org.apache.http.HttpStatus;

import java.util.List;


/**
 * Created by LEONARDO on 20/08/2017.
 */

public class DialogSync extends DialogFragment implements View.OnClickListener, OnProgressSync {

    private Context context;
    private final static int ACTUALIZAR = 2000;
    private View view;
    private ModelSincronizar modelSync;
    private ProgressBar id_progressbar;
    private TextView txtPorcent, txtTitle;
    private Button btn_next;
    private Integer statusSync;
    private ListView lst_no_content;
    private OnDissmisDialogListener onDissmisDialogListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_sync, container);
        init();
        return view;
    }

    private void init() {
        context = ContextApp.context;
        modelSync = new ModelSincronizar(this, context);
        modelSync.setAuthentication();
        id_progressbar = (ProgressBar) view.findViewById(R.id.id_progressbar);
        txtTitle = (TextView) view.findViewById(R.id.toolbar_title);
        txtPorcent = (TextView) view.findViewById(R.id.txtPorcent);
        btn_next = (Button) view.findViewById(R.id.btn_next);
        lst_no_content = (ListView) view.findViewById(R.id.lst_no_content);
        btn_next.setOnClickListener(this);

        ChangeFontStyle.changeFont(txtTitle, txtPorcent, btn_next);

    }


    @Override
    public void onClick(View view) {
        switch (statusSync) {
            case HttpStatus.SC_UNAUTHORIZED:
                DialogAccount dialogAccount = new DialogAccount();
                dialogAccount.show(getFragmentManager(), "DialogAccount");
                break;
            case HttpStatus.SC_PAYMENT_REQUIRED:
                DialogChangePassword dialog = new DialogChangePassword();
                dialog.show(getFragmentManager(), "Dialog Change Password");
                break;
            default:
                if (Long.valueOf(SharePreferenceCustom.read(R.string.app_share_preference_name, R.string.app_share_preference_first_synch, "0")) > 0) {
                    SharePreferenceCustom.write(R.string.app_share_preference_name, R.string.app_share_preference_time_synch, System.currentTimeMillis() + "");
                }
                break;
        }
        this.dismiss();
        if (onDissmisDialogListener != null) {
            onDissmisDialogListener.onDissmisDialogListener(Activity.RESULT_OK, 0, null);
        }
    }

    @Override
    public void onProgresSync(int porcentOfProgress, int httpstatus, String service) {
        id_progressbar.setProgress(porcentOfProgress);
        txtPorcent.setText("% " + porcentOfProgress);
    }


    @Override
    public void onNewVersionExist(boolean isExist) {
        if (isExist) {
            DialogUpdateApp dialogUpdateApp = new DialogUpdateApp();
            dialogUpdateApp.setCancelable(false);
            dialogUpdateApp.show(getFragmentManager(), "Dialog Update App");
            dismiss();
        }
    }

    @Override
    public void onFinishSync(int httpstatus, String response, Object object) {
        switch (httpstatus) {
            case HttpStatus.SC_OK:
                SharePreferenceCustom.write(R.string.app_share_preference_name, R.string.app_share_preference_first_synch, "1");
               /* if (dto.getStatus() == 2) {
                    txtPorcent.setText("Su contraseña caducará el " + new SimpleDateFormat("dd MMMM").format(new Date(dto.getFecha())) + ", puede cambiarla en el menú principal");
                }
                Toast.makeText(context, "Sincronización satisfactoria", Toast.LENGTH_SHORT).show();*/
                btn_next.setVisibility(Button.VISIBLE);
                break;
            case HttpStatus.SC_CONFLICT:
                txtPorcent.setText(getString(R.string.network_sc_conflict));
                btn_next.setVisibility(Button.VISIBLE);
                break;
            case HttpStatus.SC_UNAUTHORIZED:
                txtPorcent.setText(getString(R.string.network_sc_unauthorized));
                btn_next.setVisibility(Button.VISIBLE);

                break;
            case HttpStatus.SC_NO_CONTENT:
                SharePreferenceCustom.write(R.string.app_share_preference_name, R.string.app_share_preference_first_synch, "1");
                txtPorcent.setText(getString(R.string.network_no_content));
                lst_no_content.setVisibility(ListView.VISIBLE);
                btn_next.setVisibility(Button.VISIBLE);
                lst_no_content.setAdapter(new ArrayAdapter<>(context, R.layout.row_no_content, (List<String>) object));
                break;
            case HttpStatus.SC_FORBIDDEN:
                txtPorcent.setText(getString(R.string.network_forbidden));
                btn_next.setVisibility(Button.VISIBLE);
                break;
            case HttpStatus.SC_METHOD_FAILURE:
                SharePreferenceCustom.write(R.string.app_share_preference_name, R.string.app_share_preference_count_unauthorized, "1");
                txtPorcent.setText(object.toString());
                btn_next.setVisibility(Button.VISIBLE);
                break;
            case HttpStatus.SC_PAYMENT_REQUIRED:
                txtPorcent.setText(R.string.network_sc_precondition_failed);
                btn_next.setVisibility(Button.VISIBLE);
                break;
        }
        statusSync = httpstatus;
        btn_next.setVisibility(View.VISIBLE);

    }

    public DialogSync setOnDissmiDialogListener(OnDissmisDialogListener onDissmisDialogListener) {
        this.onDissmisDialogListener = onDissmisDialogListener;
        return this;
    }
}
