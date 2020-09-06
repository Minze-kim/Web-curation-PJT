package com.nuga.curation.util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class kakaoGetAuthKey {
    public static String line;
    public static BufferedReader br = null;
    public static String htmlUrl = "https://kauth.kakao.com/oauth/authorize";
    public static String clientApiKey = "5528e94942e2c3b31e26144a9b7ca1fa";
    public static String redirect_uri="http://localhost:3000/kakaoauth";
    public static String response_type="code";
    public static String fullUrl = htmlUrl + "?client_id=" + clientApiKey + "&redirect_uri=" + redirect_uri + "&response_type=" + response_type;

    public static void getKey() throws NoSuchAlgorithmException, KeyManagementException,IOException{
        StringBuilder sb = new StringBuilder();

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HttpsURLConnection conn = (HttpsURLConnection) new URL(fullUrl).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type","application/json;utf-8");
        conn.setRequestProperty("Accept","application/json");

        InputStream is = conn.getInputStream();
        br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        System.out.println(sb.toString());
    }

}



