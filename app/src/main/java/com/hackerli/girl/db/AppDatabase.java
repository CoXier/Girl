package com.hackerli.girl.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by CoXier on 2016/5/25.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    static final String NAME = "Girl";
    static final int VERSION = 1;
}
