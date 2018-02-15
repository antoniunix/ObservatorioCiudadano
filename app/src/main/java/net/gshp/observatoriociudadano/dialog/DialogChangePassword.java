package net.gshp.observatoriociudadano.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.gshp.APINetwork.NetworkTask;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.model.ModelChangePassword;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;

import org.apache.http.HttpStatus;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by LEONARDO on 20/08/2017.
 */

public class DialogChangePassword extends DialogFragment implements View.OnClickListener {
    private View v;
    private TextView txtTitle;
    private EditText edt_pss, edt_pss_confirm, edt_last_pss;
    private TextInputLayout input_pss, input_pss_confirm, input_last_pss;
    private Button btn_next;
    private ProgressBar id_progressbar;
    private TextView txtPorcent,txt_toolbar;
    private ModelChangePassword model;
    private boolean isChangePass = false;
    private boolean isUnauthorized = false;
    private SharedPreferences mySharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        v = inflater.inflate(R.layout.dialog_change_password, container);
        init();

        ChangeFontStyle.changeFont(v.findViewById(R.id.toolbar_title),edt_pss,edt_pss_confirm,
                edt_last_pss,input_pss,input_pss_confirm,input_last_pss,btn_next,txtPorcent);
        return v;
    }

    private void init() {
        txtTitle = v.findViewById(R.id.toolbar_title);
        input_pss =  v.findViewById(R.id.input_pass_new) ;
        input_last_pss =  v.findViewById(R.id.input_pass_actual);
        input_pss_confirm =  v.findViewById(R.id.input_pass_confirm);
        edt_pss =  v.findViewById(R.id.edt_pss);
        edt_pss_confirm =  v.findViewById(R.id.edt_pss_confirm);
        edt_last_pss =  v.findViewById(R.id.edt_last_pss);
        btn_next = v.findViewById(R.id.btn_next);
        txtPorcent = v.findViewById(R.id.txtPorcent);
        txt_toolbar=v.findViewById(R.id.toolbar_title);
        id_progressbar =  v.findViewById(R.id.id_progressbar);
        mySharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_share_preference_name), Context.MODE_PRIVATE);
        id_progressbar.setVisibility(View.GONE);
        txtPorcent.setVisibility(View.GONE);
        model = new ModelChangePassword(UIHandler);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String pass = edt_pss.getText().toString().trim();
        String passConfirm = edt_pss_confirm.getText().toString().trim();
        String lastpass = edt_last_pss.getText().toString().trim();
        if (lastpass.isEmpty()) {
            edt_last_pss.setError(null);
            edt_last_pss.setError("Ingrese su contraseña actual");
        } else if (pass.isEmpty()) {
            edt_pss.setError(null);
            edt_pss.setError("Ingrese nueva contraseña");
        } else if (passConfirm.isEmpty()) {
            edt_pss_confirm.setError(null);
            edt_pss_confirm.setError("Confirme la contraseña");
        } else if (!pass.equals(passConfirm)) {
            edt_pss.setError(null);
            edt_pss.setError("No coincide la contraseña");
        } else if (isChangePass) {
            isChangePass = false;
            dismiss();
        } else if (isUnauthorized) {
            isUnauthorized = false;
            new DialogAccount().show(getActivity().getSupportFragmentManager(), "Fragment_dialog_account");
            dismiss();
        } else {
            edt_pss.setError(null);
            edt_pss_confirm.setError(null);
            input_pss_confirm.setVisibility(View.GONE);
            input_last_pss.setVisibility(View.GONE);
            input_pss.setVisibility(View.GONE);
            edt_pss.setVisibility(View.GONE);
            btn_next.setVisibility(View.GONE);
            edt_last_pss.setVisibility(View.GONE);
            txtPorcent.setVisibility(View.GONE);
            edt_pss_confirm.setVisibility(View.GONE);
            id_progressbar.setVisibility(View.VISIBLE);
            if (view.getId() == R.id.btn_next) {
                try {
                    model.sendPassword(encrypt("f5e0627a0b3e0d49".getBytes(), pass.getBytes("UTF-8")), lastpass);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private static String encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return Base64.encodeToString(encrypted, STYLE_NORMAL);
    }

    private Handler UIHandler = new Handler() {
        public void handleMessage(Message msg) {
            btn_next.setVisibility(View.VISIBLE);
            txtPorcent.setVisibility(View.VISIBLE);
            id_progressbar.setVisibility(View.GONE);
            NetworkTask completedTask = (NetworkTask) msg.obj;
            switch (completedTask.getResponseStatus()) {
                case HttpStatus.SC_OK:
                    txtPorcent.setText("Se modificó la contraseña correctamente");
                    btn_next.setVisibility(Button.VISIBLE);
                    btn_next.setText(R.string.label_continue);
                    isChangePass = true;
                    mySharedPreferences.edit().putString(getString(R.string.app_share_preference_user_pass), edt_pss
                            .getText().toString().trim());
                    break;
                case HttpStatus.SC_PRECONDITION_FAILED:
                    input_pss.setVisibility(View.VISIBLE);
                    input_pss_confirm.setVisibility(View.VISIBLE);
                    input_last_pss.setVisibility(View.VISIBLE);
                    edt_pss.setVisibility(View.VISIBLE);
                    edt_last_pss.setVisibility(View.VISIBLE);
                    edt_pss_confirm.setVisibility(View.VISIBLE);
                    txtPorcent.setText(completedTask.getResponse());
                    btn_next.setVisibility(Button.VISIBLE);
                    break;
                case HttpStatus.SC_UNAUTHORIZED:
                    txtPorcent.setText("Debe " +
                            "intruducir su antigua contraseña");
                    isUnauthorized = true;
                    btn_next.setVisibility(Button.VISIBLE);
                    break;
            }
        }

        ;
    };
}
