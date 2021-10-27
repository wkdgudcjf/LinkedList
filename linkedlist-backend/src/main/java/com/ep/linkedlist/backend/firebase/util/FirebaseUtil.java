package com.ep.linkedlist.backend.firebase.util;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.NonNull;
import com.google.firebase.tasks.OnCompleteListener;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.TaskCompletionSource;
import com.google.firebase.tasks.Tasks;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Naver on 2016-09-22.
 */
@Service
public class FirebaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseUtil.class);

    @Autowired
    Gson gson;

    public static DataSnapshot await(Query query) {
        FirebaseMultiQuery firebaseMultiQuery = new FirebaseMultiQuery(query);

        DataSnapshot dataSnapshot = null;

        try {
            dataSnapshot = Tasks.await(firebaseMultiQuery.start()).get(query);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return dataSnapshot;
    }

    public static Map<String, Map<String, Object>> join(Query... queries) {
        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();

        FirebaseMultiQuery firebaseMultiQuery = new FirebaseMultiQuery(queries);
        Map<Query, DataSnapshot> dataSnapshotMap = null;

        try {
            dataSnapshotMap = Tasks.await(firebaseMultiQuery.start());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }


        for (int i = 0; i < queries.length; i++) {
            DataSnapshot dataSnapshot = dataSnapshotMap.get(queries[i]);
            String lastPath = dataSnapshot.getKey();
            for (DataSnapshot children : dataSnapshot.getChildren()) {
                String key = children.getKey();
                Object value = children.getValue();

                Map<String, Object> valueMap = result.get(key) == null ? new HashMap<String, Object>() : result.get(key);
                valueMap.put(lastPath, value);
                result.put(key, valueMap);
            }
        }

        return result;
    }

    public static <T> T getValue(Map<String, Object> map, String path) {
        if (map == null) {
            return null;
        }

        Object result = null;
        String[] paths = path.split("/");
        for (int i = 0; i < paths.length; i++) {
            String key = paths[i];
            result = map.get(key);
            if(result==null){
                break;
            }

            if(result instanceof Map){
                map = (Map<String, Object>) result;
            } else {
                break;
            }
        }

        return (T)result;
    }

    public <T> T getObject(Map<String, Object> map, String path, Class<T> classOfT) {
        if (map == null) {
            return null;
        }

        Object result = null;
        String[] paths = path.split("/");
        for (int i = 0; i < paths.length; i++) {
            String key = paths[i];
            result = map.get(key);
            if(result==null){
                break;
            }

            if(result instanceof Map){
                map = (Map<String, Object>) result;
            } else {
                break;
            }
    }

        return gson.fromJson(gson.toJson(result), classOfT);
    }
}