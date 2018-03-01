package net.gshp.observatoriociudadano.model;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.TextView;

import net.gshp.observatoriociudadano.BuildConfig;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dao.DaoEARespuesta;
import net.gshp.observatoriociudadano.dao.DaoPhoto;
import net.gshp.observatoriociudadano.dto.DtoEARespuesta;
import net.gshp.observatoriociudadano.dto.DtoPhoto;
import net.gshp.observatoriociudadano.util.ChangeFontStyle;
import net.gshp.observatoriociudadano.util.SharePreferenceCustom;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gnu on 16/02/18.
 */

public class ModelInfoPerson {
    private CircleImageView imgTBPerson;
    private TextView txtTBTitle, txtTBSubTitle;
    private DtoPhoto dtoPhoto;

    public ModelInfoPerson(Activity activity) {
        imgTBPerson = activity.findViewById(R.id.imgTBPerson);
        txtTBTitle = activity.findViewById(R.id.txtTBTitle);
        txtTBSubTitle = activity.findViewById(R.id.txtTBSubTitle);
        dtoPhoto = new DaoPhoto().select(1);
        ChangeFontStyle.changeFont(txtTBTitle, txtTBSubTitle);
    }

    public ModelInfoPerson loadImage(Activity activity) {
        try {
            File fileImageProfile = new File(dtoPhoto.getPath());
            Uri imageUri;
            if (fileImageProfile.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", fileImageProfile);
                } else {
                    imageUri = Uri.fromFile(fileImageProfile);
                }
                imgTBPerson.setImageURI(imageUri);

            } else {
                imgTBPerson.setImageResource(R.drawable.supervisor2);
            }


        } catch (NullPointerException e) {
            imgTBPerson.setImageResource(R.drawable.supervisor2);
            e.printStackTrace();
        }

        return this;
    }

    public ModelInfoPerson loadImage(Activity activity, CircleImageView circleImageView) {
        try {
            File fileImageProfile = new File(dtoPhoto.getPath());
            Uri imageUri;
            if (fileImageProfile.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", fileImageProfile);
                } else {
                    imageUri = Uri.fromFile(fileImageProfile);
                }
                circleImageView.setImageURI(imageUri);

            } else {
                circleImageView.setImageResource(R.drawable.supervisor2);
            }


        } catch (NullPointerException e) {
            circleImageView.setImageResource(R.drawable.supervisor2);
            e.printStackTrace();
        }

        return this;
    }

    public ModelInfoPerson loadInfo(String subtitle) {
        String username = SharePreferenceCustom.read(R.string.app_share_preference_name, R.string.app_share_preference_user_account, "")
                .toUpperCase();
        txtTBTitle.setText(username.equals("USER1") ? "Sin registro" : username);
        txtTBSubTitle.setText(subtitle);
        return this;
    }

    public ModelInfoPerson loadInfoRegister(String subtitle, TextView textView) {
        DtoEARespuesta dtoEARespuesta = new DaoEARespuesta().selectUserName(39, 1, 1);
        if (dtoEARespuesta != null) {
            txtTBTitle.setText(dtoEARespuesta.getRespuesta().toUpperCase());
            textView.setText(dtoEARespuesta.getRespuesta().toUpperCase());
        } else {
            txtTBTitle.setText("Sin registro");
            textView.setText("");
        }
        txtTBSubTitle.setText(subtitle);
        return this;
    }

    public ModelInfoPerson loadInfo(String title, String subtitle) {
        txtTBTitle.setText(title);
        txtTBSubTitle.setText(subtitle);
        return this;
    }
}
