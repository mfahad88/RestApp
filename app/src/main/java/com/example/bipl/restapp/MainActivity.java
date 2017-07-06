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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.ksoap2.SoapFault;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Enumeration;


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
        try {
           /* KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            Enumeration<String> aliases = ks.aliases();
            while(aliases.hasMoreElements()) {
                tv.setText(aliases.nextElement());
            }*/
            tv.setText(ApplicationManager.helloWorld("fahad",getApplicationContext()));
        } catch (Exception soapFault) {
            soapFault.printStackTrace();
        }
        // getRestwithClient();
    }

  /*  public void getRest() {
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
*/
    public void getRestwithClient() {
        try {

            final HttpClient client = new SecureHttpClient(443,getApplicationContext());
            HttpGet get = new HttpGet("https://192.168.214.1:8443/DebitCardWS/rs/webapi/getExpiry/4628830000273775");
            // Execute the GET call and obtain the response
            HttpResponse getResponse = client.execute(get);
            HttpEntity responseEntity = getResponse.getEntity();


            InputStream is = responseEntity.getContent();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            if ((text = br.readLine()) != null) {
                tv.setText(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
