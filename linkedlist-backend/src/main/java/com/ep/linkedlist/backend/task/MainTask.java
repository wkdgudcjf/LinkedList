/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.ep.linkedlist.backend.task;

import com.ep.linkedlist.backend.meeting.bo.MeetingBO;
import com.ep.linkedlist.backend.task.push.bo.PushBO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/task")
public class MainTask {
    private final Logger logger = LoggerFactory.getLogger(MainTask.class);

    @Autowired
    MeetingBO meetingBO;

    @Autowired
    PushBO pushBO;

    @RequestMapping(value = "/meeting/invite")
    @ResponseBody
    public String meetingInviteTask() {
        meetingBO.invite();
        return "success";
    }

    @RequestMapping(value = "/push/sendMessage")
    @ResponseBody
    public String sendMessageTask() {
        pushBO.sendMessage();
        return "success";
    }
}