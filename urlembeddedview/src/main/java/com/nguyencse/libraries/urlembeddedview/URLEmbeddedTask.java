package com.nguyencse.libraries.urlembeddedview;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

import static com.nguyencse.libraries.urlembeddedview.URLEmbeddedView.PROTOCOL;
import static com.nguyencse.libraries.urlembeddedview.URLEmbeddedView.PROTOCOL_S;
import static com.nguyencse.libraries.urlembeddedview.URLEmbeddedView.ROOT_URL_FAVOR_ICON;

public class URLEmbeddedTask extends AsyncTask<String, Void, URLEmbeddedData> {

    private OnLoadURLListener listener;

    public URLEmbeddedTask(OnLoadURLListener listener) {
        this.listener = listener;
    }

    @Override
    protected URLEmbeddedData doInBackground(String... params) {
        URLEmbeddedData data = new URLEmbeddedData();
        try {
            String url = params[0];
            url = ((url.startsWith(PROTOCOL) || url.startsWith(PROTOCOL_S)) ? "" : PROTOCOL) + url;

            URL host = new URL(url);
            data.setHost(host.getHost());

            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("meta");
            for (Element e : elements) {
                String tag = e.attr("property").toLowerCase();
                String content = e.attr("content");
                switch (tag) {
                    case "og:url":
                        URL urlNew = new URL(content);
                        data.setHost(urlNew.getHost());
                        break;
                    case "og:image":
                        data.setThumbnailURL(content);
                        break;
                    case "og:title":
                        data.setTitle(content);
                        break;
                    case "og:description":
                        data.setDescription(content);
                        break;
                }
            }
            data.setFavorURL(ROOT_URL_FAVOR_ICON + data.getHost());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(URLEmbeddedData result) {
        if (listener != null) {
            listener.onLoadURLCompleted(result);
        }
    }

    public interface OnLoadURLListener {
        void onLoadURLCompleted(URLEmbeddedData data);
    }
}