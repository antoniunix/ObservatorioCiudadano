package net.gshp.observatoriociudadano.model;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.TextView;

import net.gshp.observatoriociudadano.BuildConfig;
import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dao.DaoImageLogin;
import net.gshp.observatoriociudadano.dto.DtoImageLogin;
import net.gshp.observatoriociudadano.util.Config;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gnu on 16/02/18.
 */

public class ModelInfoPerson {
    private CircleImageView imgTBPerson;
    private TextView txtTBDate, txtTBTitle, txtTBSubTitle;
    private DtoImageLogin dtoImageLogin;

    public ModelInfoPerson(Activity activity) {
        imgTBPerson = activity.findViewById(R.id.imgTBPerson);
        txtTBDate = activity.findViewById(R.id.txtTBDate);
        txtTBTitle = activity.findViewById(R.id.txtTBTitle);
        txtTBSubTitle = activity.findViewById(R.id.txtTBSubTitle);
        dtoImageLogin = new DaoImageLogin().selectLast();
    }

    public ModelInfoPerson loadImage(Activity activity) {
        try {
            File fileImageProfile = new File(dtoImageLogin.getPath());
            Uri imageUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", fileImageProfile);
            } else {
                imageUri = Uri.fromFile(fileImageProfile);
            }
            imgTBPerson.setImageURI(imageUri);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return this;
    }

    public ModelInfoPerson loadInfo() {
        txtTBDate.setText(Config.formatDate());
        txtTBTitle.setText(dtoImageLogin.getName());
        return this;
    }
}
