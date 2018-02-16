package net.gshp.observatoriociudadano.faceDetection.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.gshp.observatoriociudadano.R;

/**
 * Created by alejandro on 16/01/18.
 */


public class PhotoItem extends RecyclerView.ViewHolder {
    public TextView title;
    public ImageView thumbnail;

    public PhotoItem(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        //overflow = (ImageView) view.findViewById(R.id.overflow);
    }
}
