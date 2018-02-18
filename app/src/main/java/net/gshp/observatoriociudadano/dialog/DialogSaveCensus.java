package net.gshp.observatoriociudadano.dialog;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.gshp.api.utils.ResizePicture;

import net.gshp.observatoriociudadano.BuildConfig;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dto.DtoReportCensus;
import net.gshp.observatoriociudadano.model.ModelCensus;
import net.gshp.observatoriociudadano.util.Config;

import java.io.File;

import static net.gshp.observatoriociudadano.contextApp.ContextApp.context;

/**
 * Created by leo on 17/02/18.
 */

public class DialogSaveCensus extends DialogFragment implements View.OnClickListener {

    private DtoReportCensus dtoReportCensus;
    private String path = "";
    private Button btn_save, btn_photo;
    private View view;
    private ModelCensus modelCensus;

    public void setBundle(DtoReportCensus dtoReportCensus){
        this.dtoReportCensus=dtoReportCensus;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_save_census, container);
        init();
        return view;
    }

    private void init() {
        modelCensus = new ModelCensus();
        btn_save = view.findViewById(R.id.btn_save);
        btn_photo = view.findViewById(R.id.btn_photo);
        btn_photo.setOnClickListener(this);
        btn_save.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (!path.isEmpty()) {
                    dtoReportCensus.setPath(path);
                    modelCensus.saveCensus(dtoReportCensus);
                } else {
                    Toast.makeText(context, "Debe Tomar Fotografía", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_photo:
                path = getString(R.string.app_path_photo) + System.currentTimeMillis() + ".jpg";
                File file = new File(path);
                Uri outputFileUri;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                     outputFileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);

                }else {
                     outputFileUri = Uri.fromFile(file);
                }
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                startActivityForResult(intent, 0);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            File f = new File(path);
            if (f.exists()) {
                ResizePicture.resizeAndRotate(path, getResources().getInteger(R.integer.SIZE_WIDTH_PHOTO),
                        getResources().getInteger(R.integer.SIZE_HEIGHT_PHOTO),
                        "" + dtoReportCensus.getAddress());

            } else
                path = "";
        } else
            path = "";
    }
}
