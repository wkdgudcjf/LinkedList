package com.ep.linkedlist.util;

import android.util.Log;

import com.ep.linkedlist.model.ChatInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

/**
 * Created by jiwon on 2016-12-25.
 */
public class FirebaseUtils {
    private static final String TAG = FirebaseUtils.class.getName();

    public static void incrementCounter(String reference, final int count) {
        incrementCounter(reference, count, null);
    }

    public static void incrementCounter(final String reference, final int count, final FirebaseResult transactionResult) {
        FirebaseDatabase.getInstance().getReference(reference).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() != null) {
                    Log.d(TAG, "Transaction : " + reference);
                    currentData.setValue((Long) currentData.getValue() + count);
                } else {
                    Log.d(TAG, "Transaction fail : " + reference);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                if (databaseError != null) {
                    if (transactionResult != null) {
                        transactionResult.onError();
                    }
                } else {
                    if (transactionResult != null) {
                        transactionResult.onSuccess();
                    }
                }
            }
        });
    }

    public interface FirebaseResult {
        void onSuccess();

        void onError();
    }
}
