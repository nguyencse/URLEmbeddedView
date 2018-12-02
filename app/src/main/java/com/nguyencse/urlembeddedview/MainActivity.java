package com.nguyencse.urlembeddedview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nguyencse.libraries.urlembeddedview.URLEmbeddedData;
import com.nguyencse.libraries.urlembeddedview.URLEmbeddedView;

public class MainActivity extends AppCompatActivity {

    private EditText edtURL;
    private Button btnURL;
    private URLEmbeddedView urlEmbeddedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtURL = findViewById(R.id.edt_url);
        btnURL = findViewById(R.id.btn_url);
        urlEmbeddedView = findViewById(R.id.uev);

        btnURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = edtURL.getText().toString();
                urlEmbeddedView.setURL(edtURL.getText().toString(), new URLEmbeddedView.OnLoadURLListener() {
                    @Override
                    public void onLoadURLCompleted(URLEmbeddedData data) {
                        String lastUrl = ((url.startsWith(URLEmbeddedView.PROTOCOL) || url.startsWith(URLEmbeddedView.PROTOCOL_S)) ? "" : URLEmbeddedView.PROTOCOL) + url;
                        urlEmbeddedView.title(data.getTitle() != null ? data.getTitle() : lastUrl);
                        urlEmbeddedView.description(data.getDescription());
                        urlEmbeddedView.host(data.getHost());
                        urlEmbeddedView.thumbnail(data.getThumbnailURL());
                        urlEmbeddedView.favor(data.getFavorURL());
                    }
                });
            }
        });
    }
}
