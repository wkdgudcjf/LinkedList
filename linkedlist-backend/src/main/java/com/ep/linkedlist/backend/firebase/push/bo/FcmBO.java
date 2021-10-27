package com.ep.linkedlist.backend.firebase.push.bo;

import com.ep.linkedlist.backend.firebase.push.http.FcmHttpRequestInitializer;
import com.ep.linkedlist.backend.firebase.push.model.Data;
import com.ep.linkedlist.backend.firebase.push.model.DownStreamHttpMessage;
import com.ep.linkedlist.backend.firebase.push.model.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by jiwon on 2016-09-18.
 * Firebase Cloud Message BO
 */
@Service
public class FcmBO {
    private final Logger logger = LoggerFactory.getLogger(FcmBO.class);

    @Autowired
    private JacksonFactory jacksonFactory;

    // FCM 설정
    private static final String FCM_SERVER_KEY = "key=AIzaSyA6bKMegyKIfO9JewMGTuVWcKgNMkbjJR4";
    private static final String FCM_CONTENT_TYPE = "application/json";
    private static final String FCM_API_URL = "https://fcm.googleapis.com/fcm/send";

    public String sendFirebaseCloudMessage(String token, String title, String body, String scheme) throws IOException {
        DownStreamHttpMessage downStreamHttpMessage = new DownStreamHttpMessage();
        Notification notification = new Notification();
        Data data = new Data();

        notification.setTitle(title);
        notification.setBody(body);
        data.setScheme(scheme);

        downStreamHttpMessage.setTo(token);
        downStreamHttpMessage.setNotification(notification);
        downStreamHttpMessage.setData(data);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> message = objectMapper.convertValue(downStreamHttpMessage, Map.class);
        HttpContent content = new JsonHttpContent(jacksonFactory, message);
        HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(new FcmHttpRequestInitializer());
        HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(FCM_API_URL), content);

        ExponentialBackOff backoff = new ExponentialBackOff.Builder()
                .setInitialIntervalMillis(500)
                .setMaxElapsedTimeMillis(900000)
                .setMaxIntervalMillis(6000)
                .setMultiplier(1.5)
                .setRandomizationFactor(0.5)
                .build();

        request.setUnsuccessfulResponseHandler(new HttpBackOffUnsuccessfulResponseHandler(backoff));

        HttpHeaders httpHeaders = new HttpHeaders()
                .setAuthorization(FCM_SERVER_KEY)
                .setContentType(FCM_CONTENT_TYPE);

        request.setHeaders(httpHeaders);

        HttpResponse httpResponse = request.execute();
        InputStream inputStream = httpResponse.getContent();

        String ret = IOUtils.toString(inputStream, CharEncoding.UTF_8);
        return ret;
    }
}
