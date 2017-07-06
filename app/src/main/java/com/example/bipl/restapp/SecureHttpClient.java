package com.example.bipl.restapp;

import android.content.Context;
import android.util.Log;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;


public class SecureHttpClient extends DefaultHttpClient {
    private static final String TAG = "Secure>>>>>>>>>";
    private int securePort;
    private Context context;

    public SecureHttpClient(final int port, Context ctx) {
        this.securePort = port;
        this.context = ctx;
    }

    private SSLSocketFactory createSSLSocketFactory() throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException {
        Log.d(TAG, "Creating SSL socket factory");

        final KeyStore truststore = KeyStore.getInstance("BKS");

        truststore.load(context.getResources().openRawResource(R.raw.clienttruststore), "123456".toCharArray());

        final KeyStore keystore = KeyStore.getInstance("BKS");
        keystore.load(context.getResources().openRawResource(R.raw.client), "123456".toCharArray());

        return this.createFactory(keystore, "123456", truststore);
    }

    private SSLSocketFactory createFactory(final KeyStore keystore,
                                           final String keystorePassword, final KeyStore truststore) {

        SSLSocketFactory factory;
        try {
            factory = new SSLSocketFactory(keystore, keystorePassword, truststore);
            factory.setHostnameVerifier(
                    (X509HostnameVerifier) SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            Log.e(TAG, "Caught exception when trying to create ssl socket factory. Reason: " +
                    e.getMessage());
            throw new RuntimeException(e);
        }

        return factory;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        Log.d(TAG, "Creating client connection manager");

        final SchemeRegistry registry = new SchemeRegistry();

        Log.d(TAG, "Adding https scheme for port " + securePort);
        try {
            registry.register(new Scheme("https", createSSLSocketFactory(), this.securePort));
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        return new SingleClientConnManager(getParams(), registry);
    }
}