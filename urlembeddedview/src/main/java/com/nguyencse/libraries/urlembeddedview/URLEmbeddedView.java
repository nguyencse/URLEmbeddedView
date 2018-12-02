package com.nguyencse.libraries.urlembeddedview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class URLEmbeddedView extends ConstraintLayout {
    public static final String PROTOCOL = "http://";
    public static final String PROTOCOL_S = "https://";
    public static final String ROOT_URL_FAVOR_ICON = "https://www.google.com/s2/favicons?domain=";

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtHost;
    private ImageView imgThumbnail;
    private ImageView imgFavorIcon;
    private ConstraintLayout cslOGP;
    private ConstraintLayout cslOGPData;
    private ProgressBar prgLoading;

    public URLEmbeddedView(Context context) {
        super(context);
        initView(context, null);
    }

    public URLEmbeddedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public URLEmbeddedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_url_embedded_view, this, true);

        this.txtTitle = findViewById(R.id.txtTitle);
        this.txtDescription = findViewById(R.id.txtDescription);
        this.txtHost = findViewById(R.id.txtURL);
        this.imgThumbnail = findViewById(R.id.imgThumbnail);
        this.imgFavorIcon = findViewById(R.id.imgFavorIcon);
        this.cslOGP = findViewById(R.id.cslOGP);
        this.cslOGPData = findViewById(R.id.cslOGPData);
        this.prgLoading = findViewById(R.id.prg_loading);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.URLEmbeddedView, 0, 0);
        if (typedArray != null) {
            this.txtTitle.setText(typedArray.getText(R.styleable.URLEmbeddedView_title));
            this.txtDescription.setText(typedArray.getText(R.styleable.URLEmbeddedView_description));
            this.txtHost.setText(typedArray.getText(R.styleable.URLEmbeddedView_host));
            this.imgFavorIcon.setImageResource(typedArray.getResourceId(R.styleable.URLEmbeddedView_favor, 0));
            this.imgThumbnail.setImageResource(typedArray.getResourceId(R.styleable.URLEmbeddedView_thumbnail, 0));
            typedArray.recycle();
        }
    }

    public void title(String title) {
        if (txtTitle != null) {
            this.txtTitle.setText(title);
        }
    }

    public void description(String description) {
        if (txtDescription != null) {
            this.txtDescription.setText(description);
        }
    }

    public void host(String host) {
        if (txtHost != null) {
            txtHost.setText(host);
        }
    }

    public void thumbnail(String thumbnailURL) {
        if (imgThumbnail != null) {
            Picasso.get().load(thumbnailURL).placeholder(R.drawable.ic_url).error(R.drawable.ic_url).into(imgThumbnail);
        }
    }

    public void favor(String favorURL) {
        if (imgFavorIcon != null) {
            Picasso.get().load(favorURL).placeholder(R.drawable.ic_url).error(R.drawable.ic_url).into(imgFavorIcon);
        }
    }

    public void setURL(String url, final OnLoadURLListener onLoadURLListener) {
        prgLoading.setVisibility(View.VISIBLE);
        cslOGP.setVisibility(View.VISIBLE);
        cslOGPData.setVisibility(View.INVISIBLE);

        URLEmbeddedTask urlTask = new URLEmbeddedTask(new URLEmbeddedTask.OnLoadURLListener() {
            @Override
            public void onLoadURLCompleted(URLEmbeddedData data) {
                prgLoading.setVisibility(View.GONE);
                cslOGPData.setVisibility(View.VISIBLE);
                if (onLoadURLListener != null) {
                    onLoadURLListener.onLoadURLCompleted(data);
                }
            }
        });
        urlTask.execute(url);
    }

    public interface OnLoadURLListener {
        void onLoadURLCompleted(URLEmbeddedData data);
    }
}
