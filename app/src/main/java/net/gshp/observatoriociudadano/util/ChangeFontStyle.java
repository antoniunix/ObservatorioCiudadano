package net.gshp.observatoriociudadano.util;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import net.gshp.observatoriociudadano.contextApp.ContextApp;

/**
 * Created by gnu on 15/03/17.
 */


public class ChangeFontStyle {

    public static enum TYPE_FONT {NORMAL, BOLD, BLACK}

    ;


    public static void changeFont(TYPE_FONT typeFont, View... view) {
        Typeface type;

        switch (typeFont) {
            case NORMAL:
                type = Typeface.createFromAsset(ContextApp.context.getAssets(), "fonts/Lato-Regular.ttf");
                break;
            case BOLD:
                type = Typeface.createFromAsset(ContextApp.context.getAssets(), "fonts/Lato-Semibold.ttf");
                break;
            case BLACK:
                type = Typeface.createFromAsset(ContextApp.context.getAssets(), "fonts/Lato-Black.ttf");
                break;
            default:
                type = Typeface.createFromAsset(ContextApp.context.getAssets(), "fonts/Lato-Regular.ttf");
                break;
        }

        for (int position = 0; position < view.length; position++) {
            if (view[position] instanceof TextView) {
                ((TextView) view[position]).setTypeface(type);
            } else if (view[position] instanceof EditText) {
                ((EditText) view[position]).setTypeface(type);
            } else if (view[position] instanceof Button) {
                ((Button) view[position]).setTypeface(type);
            } else if (view[position] instanceof CheckBox) {
                ((CheckBox) view[position]).setTypeface(type);
            } else {
            }

        }

    }

    public static void changeFontById(TYPE_FONT typeFont, Activity activity, int... resourceId) {
        Typeface type;

        switch (typeFont) {
            case NORMAL:
                type = Typeface.createFromAsset(ContextApp.context.getAssets(), "fonts/Lato-Regular.ttf");
                break;
            case BOLD:
                type = Typeface.createFromAsset(ContextApp.context.getAssets(), "fonts/Lato-Semibold.ttf");
                break;
            case BLACK:
                type = Typeface.createFromAsset(ContextApp.context.getAssets(), "fonts/Lato-Black.ttf");
                break;
            default:
                type = Typeface.createFromAsset(ContextApp.context.getAssets(), "fonts/Lato-Regular.ttf");
                break;
        }

        for (int position = 0; position < resourceId.length; position++) {

            if ( activity.findViewById(resourceId[position]) instanceof TextView) {
                ((TextView) activity.findViewById(resourceId[position])).setTypeface(type);
            } else if (activity.findViewById(resourceId[position]) instanceof EditText) {
                ((EditText) activity.findViewById(resourceId[position])).setTypeface(type);
            } else if (activity.findViewById(resourceId[position]) instanceof Button) {
                ((Button) activity.findViewById(resourceId[position])).setTypeface(type);
            } else if (activity.findViewById(resourceId[position]) instanceof CheckBox) {
                ((CheckBox) activity.findViewById(resourceId[position])).setTypeface(type);
            } else {
            }

        }

    }

    public static void changeFont(View... view) {
        Typeface type = Typeface.createFromAsset(ContextApp.context.getAssets(), "fonts/Lato-Regular.ttf");
        for (int position = 0; position < view.length; position++) {
            if (view[position] instanceof TextView) {
                ((TextView) view[position]).setTypeface(type);
            } else if (view[position] instanceof EditText) {
                ((EditText) view[position]).setTypeface(type);
            } else if (view[position] instanceof Button) {
                ((Button) view[position]).setTypeface(type);
            } else if (view[position] instanceof CheckBox) {
                ((CheckBox) view[position]).setTypeface(type);
            } else {
            }

        }

    }
}
