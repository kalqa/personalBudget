package com.example.mybudgetv2.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataBaseService {

    public static DatabaseReference mDatabase;

    public DataBaseService() {
//        if (mDatabase == null) {
//            mDatabase = FirebaseDatabase.getInstance().getReference();
//        }
    }

    public static DatabaseReference instanteDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            return mDatabase;
        }

        return mDatabase;
    }
}
