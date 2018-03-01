package net.gshp.observatoriociudadano.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.gshp.observatoriociudadano.BuildConfig;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;
import net.gshp.observatoriociudadano.util.DownloadFileFromURL;

import java.io.File;

/**
 * Created by LEONARDO on 20/08/2017.
 */

public class DialogUpdateApp extends DialogFragment {

    private View view;
    private Context context;
    private TextView txtTitle, txtPercent;
    private DownloadFileFromURL donDownloadFileFromURL;
    private Handler h;
    private ProgressBar idProgressbar;
    private Button btnNext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_update, container);
        init();
        donDownloadFileFromURL.execute(getString(R.string.app_download_apk));

        ChangeFontStyle.changeFont(view.findViewById(R.id.toolbar_title), txtPercent, btnNext);
        return view;
    }

    private void init() {
        context = view.getContext();
        txtTitle =  view.findViewById(R.id.toolbar_title);
        txtPercent =  view.findViewById(R.id.txt_percent_update);
        h = new HandlerTask();
        donDownloadFileFromURL = new DownloadFileFromURL(h, ContextApp.context.getString(R.string.app_path_apk));// se pone 0 para especificar que se descargara la app
        idProgressbar = view.findViewById(R.id.pgb_update);
        btnNext =  view.findViewById(R.id.btn_next_update);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    class HandlerTask extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    txtPercent.setText("0 %");
                    break;
                case 2:
                    idProgressbar.setProgress(msg.arg2);
                    txtPercent.setText(msg.arg2 + " %");
                    break;
                case 3:
                    // si se descargo completo se instala
                    //el MD5 siempre debe ir en mayusculas
                    if (txtPercent.getText().equals("100 %")) {
                        try {
                            File toInstall = new File(getString(R.string.app_path_apk));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                                intent.setData(apkUri);
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivity(intent);
                            } else {
                                Uri apkUri = Uri.fromFile(toInstall);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            idProgressbar.setVisibility(ProgressBar.GONE);
                            txtPercent.setText(R.string.dialog_update_error);
                            btnNext.setVisibility(Button.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

