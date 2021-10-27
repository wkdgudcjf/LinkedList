package com.ep.linkedlist.backend.firebase.push.http;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;

import java.io.IOException;

/**
 * Created by jiwon on 2016-09-10.
 */
public class FcmHttpRequestInitializer implements HttpRequestInitializer, HttpUnsuccessfulResponseHandler {

    @Override
    public boolean handleResponse(
            HttpRequest request, HttpResponse response, boolean retrySupported) throws IOException {
        System.out.println(response.getStatusCode() + " " + response.getStatusMessage());
        return false;
    }

    @Override
    public void initialize(HttpRequest request) throws IOException {
        request.setUnsuccessfulResponseHandler(this);
    }
}