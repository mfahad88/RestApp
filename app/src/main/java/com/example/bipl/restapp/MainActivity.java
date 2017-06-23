package com.example.bipl.restapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    String text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textView);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        getRestwithClient();
        // getRestwithClient();
        // getrestwithClient();

    }

    public void getRest() {
        try {
            // Setup a custom SSL Factory object which simply ignore the certificates validation and accept all type of self signed certificates
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
            DefaultHttpClient httpClient = new DefaultHttpClient(ccm, params);
            HttpGet httpGet = new HttpGet("https://10.7.255.87:8181/HelloWorldRest/ws/webapi/hello");

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream is = httpEntity.getContent();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            if ((text = br.readLine()) != null) {
                tv.setText(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRestwithClient() {
        try {

            /*KeyStore serverCert=KeyStore.getInstance("cert");
            serverCert.load(this.getAssets().open("ahmedabbas.crt"),"123456".toCharArray());
            KeyStore clientCert = KeyStore.getInstance("pkcs12");
            clientCert.load(getResources().openRawResource(R.raw.clientpos), "123456".toCharArray());

            HttpClient httpClient = null;
            HttpParams httpParams = new BasicHttpParams();
            SSLSocketFactory sslSocketFactory = new SSLSocketFactory(clientCert,null,serverCert);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("https", sslSocketFactory, 443));
            httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, registry), httpParams);
*/

            /*final HttpParams httpParams = new BasicHttpParams();
            SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
            // load the keystore containing the client certificate - keystore type is probably jks or pkcs12
            final KeyStore keystore = KeyStore.getInstance("pkcs12");
            InputStream keystoreInput = getResources().openRawResource(R.raw.clientstore);

            keystore.load(keystoreInput, "123456".toCharArray());

            // load the trustore, leave it null to rely on cacerts distributed with the JVM - truststore type is probably jks or pkcs12
            KeyStore truststore = KeyStore.getInstance("pkcs12");
            InputStream truststoreInput = getResources().openRawResource(R.raw.clientstore);

            truststore.load(truststoreInput, "123456".toCharArray());

            final SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("https", sslFactory, 8443));
            schemeRegistry.register(new Scheme("https", new SSLSocketFactory(keystore, "123456", truststore), 8443));

            final DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, schemeRegistry), httpParams);*/

            KeyStore trustStore = KeyStore.getInstance("pkcs12");
            trustStore.load(getResources().openRawResource(R.raw.clientstore), "123456".toCharArray());

            KeyStore keystore = KeyStore.getInstance("pkcs12");
            keystore.load(getResources().openRawResource(R.raw.clientstore), "123456".toCharArray());

            SSLSocketFactory sf = new SSLSocketFactory(keystore,"123456",trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);


            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("https", sf, 8443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            DefaultHttpClient httpClient=new DefaultHttpClient(ccm, params);
            HttpGet request = new HttpGet("https://192.168.214.1:8443/DebitCardWS/rs/webapi/getExpiry/4628830000273775");

            HttpResponse httpResponse = httpClient.execute(request);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream is = httpEntity.getContent();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            if ((text = br.readLine()) != null) {
                tv.setText(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public void getrestwithClient() {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream instream = getApplicationContext().getResources().openRawResource(R.raw.mpay);
            keyStore.load(instream, "123456".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext context1 = SSLContext.getInstance("SSL");
            context1.init(null, tmf.getTrustManagers(), null);

            HttpClient httpClient = null;
            HttpParams httpParams = new BasicHttpParams();
            SSLSocketFactory sslSocketFactory = new SSLSocketFactory(keyStore, null, null);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("https", sslSocketFactory, 8181));

            HttpGet httpGet = new HttpGet("https://10.7.255.87:8181/BIPOS_WS/BiPosWSService?wsdl");

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream is = httpEntity.getContent();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            if ((text = br.readLine()) != null) {
                tv.setText(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void getSoap(String name) {
        String xml = null;
        String URL = "https://10.7.255.87:8181/BIPOS_WS/BiPosWSService";

        try {

            xml = convertStreamToString(getAssets().open("request.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String request = String.format(xml, name);
        HTTPOST httpost = new HTTPOST();
        httpost.getResponseByXML(URL, request);
        httpost.getResponseByFile(URL);
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }
}
