package com.beihui.market.db;


import android.content.Context;

import com.beihui.market.entity.DaoMaster;
import com.beihui.market.entity.DaoSession;

public class DatabaseHelper {
    public static final String DATABASE_NAME = "loan.db";

    private static DatabaseHelper instance;
    private DaoSession daoSession;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME, null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}