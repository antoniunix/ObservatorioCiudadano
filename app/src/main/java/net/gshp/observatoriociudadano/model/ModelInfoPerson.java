package net.gshp.observatoriociudadano.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.TextView;

import net.gshp.observatoriociudadano.BuildConfig;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.contextApp.ContextApp;
import net.gshp.observatoriociudadano.dao.DaoImageLogin;
import net.gshp.observatoriociudadano.dto.DtoImageLogin;
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
    private DtoImageLogin dtoImageLogin;

    public ModelInfoPerson(Activity activity) {
        imgTBPerson = activity.findViewById(R.id.imgTBPerson);
        txtTBTitle = activity.findViewById(R.id.txtTBTitle);
        txtTBSubTitle = activity.findViewById(R.id.txtTBSubTitle);
        dtoImageLogin = new DaoImageLogin().selectLast();
        ChangeFontStyle.changeFont(txtTBTitle, txtTBSubTitle);
    }

    public ModelInfoPerson loadImage(Activity activity) {
        Log.e("leo", "load ");
        try {
            File fileImageProfile = new File(dtoImageLogin.getPath());
            Uri imageUri;
            Log.e("leo", "dto path " + dtoImageLogin.getPath());
            if (fileImageProfile.exists()) {
                Log.e("leo", "exist ");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", fileImageProfile);
                } else {
                    imageUri = Uri.fromFile(fileImageProfile);
                }
                imgTBPerson.setImageURI(imageUri);

            } else {
                Log.e("leo", "not exist ");
                imgTBPerson.setImageResource(R.drawable.supervisor2);
            }


        } catch (NullPointerException e) {
            Log.e("leo", "error ");
            imgTBPerson.setImageResource(R.drawable.supervisor2);
            e.printStackTrace();
        }

        return this;
    }

    public ModelInfoPerson loadInfo(String subtitle) {
        txtTBTitle.setText(SharePreferenceCustom.read(R.string.app_share_preference_name, R.string.app_share_preference_user_account, "").toUpperCase());
        txtTBSubTitle.setText(subtitle);
        return this;
    }
}
