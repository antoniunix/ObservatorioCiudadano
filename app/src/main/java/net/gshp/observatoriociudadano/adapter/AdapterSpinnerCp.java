package net.gshp.observatoriociudadano.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.gshp.observatoriociudadano.util.ChangeFontStyle;

import java.util.List;

/**
 * Created by leo on 17/02/18.
 */

public class AdapterSpinnerCp extends ArrayAdapter<String> {

    public AdapterSpinnerCp(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        ChangeFontStyle.changeFont(view);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        ChangeFontStyle.changeFont(view);
        return view;
    }
}
