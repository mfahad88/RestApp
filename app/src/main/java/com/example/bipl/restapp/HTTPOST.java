package com.example.bipl.restapp;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Environment;
import android.util.Log;

public class HTTPOST {

    public String getResponseByXML(String URL, String request) {
        HttpPost httpPost = new HttpPost(URL);
        StringEntity entity;
        String response_string = null;
        try {
            entity = new StringEntity(request, HTTP.UTF_8);
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            //	httpPost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
            httpPost.setEntity(entity);
            SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
            sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            // Register the HTTP and HTTPS Protocols. For HTTPS, register our custom SSL Factory object.
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8061));
            registry.register(new Scheme("https", sslFactory, 8181));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            HttpClient client = new DefaultHttpClient(ccm, params);
            HttpResponse response = client.execute(httpPost);
            response_string = EntityUtils.toString(response.getEntity());
            Log.d("request", response_string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response_string;
    }

    public String getResponseByFile(String URL) {

        HttpPost httpPost = new HttpPost(URL);
        FileEntity entity;
        String response_string = null;
        try {
            entity = new FileEntity(new File(Environment.getExternalStorageDirectory() + File.separator + "request.xml"), "UTF-8");
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            //	httpPost.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
            httpPost.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpPost);
            response_string = EntityUtils.toString(response.getEntity());
            Log.d("request", response_string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response_string;
    }
}