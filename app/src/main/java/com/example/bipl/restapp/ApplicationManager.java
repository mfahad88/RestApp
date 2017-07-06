package com.example.bipl.restapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsServiceConnectionSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateExpiredException;

/**
 * Created by fahad on 6/30/2017.
 */

public class ApplicationManager {

    private final static String NAMESPACE="http://ws.bi.com/";


    public static String helloWorld(String name,Context context) {
        Boolean status = false;
        String response="";
        String METHOD_NAME = "getHelloWorldAsString";
        String SOAP_ACTION = "http://ws.bi.com/getHelloWorldAsString";
        try {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            PropertyInfo pi = new PropertyInfo();
            pi.setName("arg0");
            pi.setValue(name);
            pi.setType(String.class);
            request.addProperty(pi);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            SslRequest.allowAllSSL();
            HttpsTransportSE httpTransportSE = new HttpsTransportSE("192.168.214.1", 8181, "/BIPOS_WS/BiPosWSService?wsdl", 100000);

            ((HttpsServiceConnectionSE) httpTransportSE.getServiceConnection()).setSSLSocketFactory(trustAllHosts(context).getSocketFactory());

            envelope.setOutputSoapObject(request);
            httpTransportSE.debug = true;
            List<HeaderProperty> list = new ArrayList<>();
            list.add(new HeaderProperty("uname", "adnan"));
            list.add(new HeaderProperty("pass", "adnan123"));
            httpTransportSE.call(SOAP_ACTION, envelope, list);
            Log.e("LOGIN>>>>>>>>>", envelope.getResponse().toString());

            response=envelope.getResponse().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String helloWorld(Context context) throws SoapFault {
        Boolean status = false;
        String METHOD_NAME = "getReply";
        String SOAP_ACTION = "http://DefaultNamespace/getReply";
        SoapSerializationEnvelope envelope = null;
        SoapObject request;
        try {
            SslRequest.allowAllSSL();
            HttpsTransportSE httpTransportSE = new HttpsTransportSE("192.168.214.1", 8443, "DebitSoap/services/DebitCardSoap?wsdl", 20000);
            //((HttpsServiceConnectionSE) httpTransportSE.getServiceConnection()).setSSLSocketFactory(trustAllHosts(context).getSocketFactory());
            request = new SoapObject(NAMESPACE, METHOD_NAME);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            httpTransportSE.debug = true;
            httpTransportSE.call(SOAP_ACTION, envelope);
            Log.e("LOGIN>>>>>>>>>", envelope.getResponse().toString());
            return envelope.getResponse().toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    protected static SSLContext trustAllHosts(Context context) {
        return allowAllSSL(context);
    }

   /* public static SSLContext allowAllSSL(Context cntx) {
        SSLContext context = null;
        TrustManager[] trustManagers = null;
        try{
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            InputStream in = cntx.getResources().openRawResource(R.raw.developerp12);
            try {
                keyStore.load(in, "123456".toCharArray());
            } catch (CertificateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                in.close();
            }
            tmf.init(keyStore);


           if (trustManagers == null) {
                trustManagers = new TrustManager[] { new FakeX509TrustManager() };
            }

            try {
                context = SSLContext.getInstance("SSL");
                context.init(null, tmf.getTrustManagers(), new SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }

            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }catch(Exception ex)
        {
            Log.e("Security>>>>>>>","allowAllSSL failed: "+ex.toString());
        }
        return context;
    }*/

    public static SSLContext allowAllSSL(Context cntx) {
        SSLContext context = null;
        TrustManager[] trustManagers = null;
        KeyManagerFactory mgrFact;
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            mgrFact = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            InputStream in = cntx.getResources().openRawResource(R.raw.mobileapp);
            try {
                keyStore.load(in, "123456".toCharArray());
                mgrFact.init(keyStore, "123456".toCharArray());
            } catch (CertificateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                in.close();
            }
            tmf.init(keyStore);


          /*  HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    Log.e("Hostname>>>>>>",hostname);
                    return true;
                }

            });*/


            if (trustManagers == null) {
                trustManagers = new TrustManager[]{new FakeX509TrustManager()};
            }

            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    System.out.println("getAcceptedIssuers");
                    return null;
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    System.out.println("Сведения о сертификате : " + chain[0].getIssuerX500Principal().getName() + "\n Тип авторизации : " + authType);
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    System.out.println("checkClientTrusted : " + authType);
                }
            }};
          //  System.out.print("TMF>>>>>>>>>>>" + tmf.getTrustManagers());
            try {
                context = SSLContext.getInstance("TLS");
                context.init(mgrFact.getKeyManagers(), trustAllCerts, new SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                Log.e("NoSuchAlgorithmException", e.getMessage());
                e.printStackTrace();
            } catch (KeyManagementException e) {
                Log.e("KeyManagementException", e.getMessage());
                e.printStackTrace();
            }

            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    Log.e("Hostname>>>>>>>>",hostname);
                    return true;
                }
            });
        } catch (Exception ex) {
            Log.e("Security>>>>>>>>", "allowAllSSL failed: " + ex.toString());
        }
        return context;
    }

    @SuppressLint("LongLogTag")
    public static Element buildAuthHeader(SoapSerializationEnvelope envelope) {
        Element headers[] = new Element[1];
        headers[0]= new Element().createElement("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security");
        /*headers[0].setAttribute("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "mustUnderstand", "1");*/
        headers[0].setAttribute(envelope.env, "mustUnderstand", "1");
        Element security=headers[0];
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Log.e("currentDateTimeString>>>>>>>>>",dateFormat.format(date));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Log.e("Time>>>>>>>>>>>",simpleDateFormat.format(date));
        Calendar calendar=Calendar.getInstance();
        Log.e("Calendar>>>>", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));

        //user timestamp
        Element timestamp = new Element().createElement(envelope.xsd, "Timestamp");
        timestamp.setAttribute(envelope.xsd,"Id","TS-5EF57E4398E4A37DAA14968127994737");

        //created
        Element created = new Element().createElement(envelope.xsd, "Created");
        created.addChild(Node.TEXT, dateFormat.format(date)+"TO"+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+"Z");
        timestamp.addChild(Node.ELEMENT,created);

        // Expires
        Element expires = new Element().createElement(envelope.xsd, "Expires");
        calendar.add(Calendar.MINUTE,1);
        expires.addChild(Node.TEXT, dateFormat.format(date)+"TO"+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+"Z");
        timestamp.addChild(Node.ELEMENT,expires);

        headers[0].addChild(Node.ELEMENT, timestamp);

        return headers[0];
    }
}
