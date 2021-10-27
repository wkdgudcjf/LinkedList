/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ep.linkedlist.service.messaging;

import android.util.Log;

import com.ep.linkedlist.bo.messaging.DeviceBO;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;

@EService
public class LinkedListFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Bean
    DeviceBO deviceBO;

    private static final String TAG = "FirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "Refreshed token");
        deviceBO.register();
    }
}