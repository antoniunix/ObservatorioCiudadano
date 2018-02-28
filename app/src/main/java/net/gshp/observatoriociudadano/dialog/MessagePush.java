package net.gshp.observatoriociudadano.dialog;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.gshp.observatoriociudadano.R;
import net.gshp.observatoriociudadano.dto.DtoBundleMessagePush;

public class MessagePush extends AppCompatActivity implements View.OnClickListener {

    private DtoBundleMessagePush dtoBundleMessagePush;
    private TextView toolbar_title;
    private Button btn_agree;
    private WebView txt_message;

    private void init() {
        dtoBundleMessagePush = (DtoBundleMessagePush) getIntent().getExtras().get(getString(R.string.app_bundle_name));
        toolbar_title = findViewById(R.id.toolbar_title);
        btn_agree = findViewById(R.id.btn_agree);
        txt_message = findViewById(R.id.txt_message);

        if (dtoBundleMessagePush.getTitle() != null) {
            toolbar_title.setText(dtoBundleMessagePush.getTitle());
        }
        txt_message.loadDataWithBaseURL(null, dtoBundleMessagePush.getMessage(), "text/html", "utf-8", null);
        btn_agree.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_push);
        getSupportActionBar().hide();
        init();
    }

    @Override
    public void onClick(View v) {
        finish();
    }

}
