package com.ep.linkedlist.util;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * Created by jiwon on 2016-09-19.
 */
@EBean
public class AppRequestFactory extends SimpleClientHttpRequestFactory {

    @AfterInject
    void afterinject() {
        setReadTimeout(3*1000);
        setConnectTimeout(3*1000);
    }
}