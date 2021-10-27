package com.ep.linkedlist.service.rest;


import com.ep.linkedlist.util.AppRequestFactory;

import org.androidannotations.annotations.Background;
import org.androidannotations.rest.spring.annotations.Field;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Header;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

/**
 * Created by jiwon on 2016-09-19.
 */
@Rest(
//        rootUrl = "http://10.0.2.2:8080",
        rootUrl = "http://linkedlist-566ea.appspot.com",
        requestFactory = AppRequestFactory.class,
        converters = {
                StringHttpMessageConverter.class,
                FormHttpMessageConverter.class
        }
)
public interface PushRestService {

    @Post("/push/meeting/message")
    @Header(name = "Content-Type", value = "application/x-www-form-urlencoded")
    String pushMeetingMessage(@Field String meetingId);

    @Post("/push/meeting/join")
    @Header(name = "Content-Type", value = "application/x-www-form-urlencoded")
    String pushMeetingJoin(@Field String meetingId, @Field String gender);
    
}
