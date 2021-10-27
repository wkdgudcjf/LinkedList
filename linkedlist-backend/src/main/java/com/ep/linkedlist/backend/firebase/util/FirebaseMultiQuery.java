package com.ep.linkedlist.backend.firebase.util;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.NonNull;
import com.google.firebase.tasks.Continuation;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.TaskCompletionSource;
import com.google.firebase.tasks.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Naver on 2016-09-22.
 */
public class FirebaseMultiQuery {

    private final HashSet<Query> refs = new HashSet<Query>();
    private final HashMap<Query, DataSnapshot> snaps = new HashMap<Query, DataSnapshot>();
    private final HashMap<Query, ValueEventListener> listeners = new HashMap<Query, ValueEventListener>();

    public FirebaseMultiQuery(final Query... refs) {
        for (final Query ref : refs) {
            add(ref);
        }
    }

    public void add(final Query ref) {
        refs.add(ref);
    }

    public Task<Map<Query, DataSnapshot>> start() {
        // Create a Task<DataSnapsot> to trigger in response to each database listener.
        //
        final ArrayList<Task<DataSnapshot>> tasks = new ArrayList<Task<DataSnapshot>>(refs.size());
        for (final Query ref : refs) {
            final TaskCompletionSource<DataSnapshot> source = new TaskCompletionSource<DataSnapshot>();
            final ValueEventListener listener = new MyValueEventListener(ref, source);
            ref.addListenerForSingleValueEvent(listener);
            listeners.put(ref, listener);
            tasks.add(source.getTask());
        }

        // Return a single Task that triggers when all queries are complete.  It contains
        // a map of all original Querys originally given here to their resulting
        // DataSnapshot.
        //
        return Tasks.whenAll(tasks).continueWith(new Continuation<Void, Map<Query, DataSnapshot>>() {
            @Override
            public Map<Query, DataSnapshot> then(@NonNull Task<Void> task) throws Exception {
                task.getResult();
                return new HashMap<Query, DataSnapshot>(snaps);
            }
        });
    }

    public void stop() {
        for (final Map.Entry<Query, ValueEventListener> entry : listeners.entrySet()) {
            entry.getKey().removeEventListener(entry.getValue());
        }
        snaps.clear();
        listeners.clear();
    }

    private class MyValueEventListener implements ValueEventListener {
        private final Query ref;
        private final TaskCompletionSource<DataSnapshot> taskSource;

        public MyValueEventListener(Query ref, TaskCompletionSource<DataSnapshot> taskSource) {
            this.ref = ref;
            this.taskSource = taskSource;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            snaps.put(ref, dataSnapshot);
            taskSource.setResult(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            taskSource.setException(databaseError.toException());
        }
    }

}