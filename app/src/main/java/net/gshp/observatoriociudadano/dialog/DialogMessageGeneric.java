package net.gshp.observatoriociudadano.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.listener.OnDissmisDialogListener;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;

/**
 * Created by Antoniunix on 31/10/17.
 */

public class DialogMessageGeneric extends DialogFragment implements View.OnClickListener {

    private View view;
    private TextView txt_message, toolbar_title;
    private Button btn_cancel, btn_agree;
    private OnDissmisDialogListener onDissmisDialogListener;
    private int requestCode;
    private String title, message;
    private boolean showCancelButton = true, showContinueButton = true;


    public DialogMessageGeneric() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_message_generic, container);
        txt_message = view.findViewById(R.id.txt_message);
        toolbar_title = view.findViewById(R.id.toolbar_title);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_agree = view.findViewById(R.id.btn_agree);

        btn_cancel.setOnClickListener(this);
        btn_agree.setOnClickListener(this);

        ChangeFontStyle.changeFont(view.findViewById(R.id.toolbar_title), txt_message, btn_cancel, btn_agree);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar_title.setText(title);
        txt_message.setText(message);
        btn_cancel.setVisibility(showCancelButton ? View.VISIBLE : View.GONE);
        btn_agree.setVisibility(showContinueButton ? View.VISIBLE : View.GONE);
    }

    public DialogMessageGeneric setOnDissmisDialogListener(OnDissmisDialogListener onDissmisDialogListener) {
        this.onDissmisDialogListener = onDissmisDialogListener;
        return this;
    }

    public DialogMessageGeneric setData(String title, String message, int requestCode) {
        this.requestCode = requestCode;
        this.title = title;
        this.message = message;
        return this;
    }

    public DialogMessageGeneric setShowButton(boolean showCancelButton, boolean showContinueButton) {
        this.showCancelButton = showCancelButton;
        this.showContinueButton = showContinueButton;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancel) {
            dismiss();
        } else if (onDissmisDialogListener != null) {
            onDissmisDialogListener.onDissmisDialogListener(Activity.RESULT_OK, requestCode, null);
            dismiss();
        } else {
            dismiss();
        }

    }

}
