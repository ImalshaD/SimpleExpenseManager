package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public interface Table {
    public String onCreate();
    public String onUpgrade();

}
