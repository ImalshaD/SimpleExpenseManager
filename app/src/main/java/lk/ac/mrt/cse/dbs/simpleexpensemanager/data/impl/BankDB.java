package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class BankDB extends SQLiteOpenHelper {
    private static final int version=5;
    private static final String DB_NAME="200487B";
    private static BankDB instance=null;
    private SQLiteDatabase readTable=null;
    private SQLiteDatabase writeTable=null;
    private BankDB(@Nullable Context context) {
        super(context, DB_NAME, null, version);
    }
    public static BankDB getInstance(@Nullable Context context){
        if (instance==null){
            instance = new BankDB(context);
        }
        return instance;
    }
    public SQLiteDatabase openReadTable(){
        if (readTable==null) {
            readTable = this.getReadableDatabase();
        }
        return readTable;
    }
    public void closeReadTable(){
        if (readTable!=null){
            readTable.close();
            readTable=null;
        }
    }
    public SQLiteDatabase openWriteTable(){
        if (writeTable==null) {
            writeTable = this.getWritableDatabase();
        }
        return writeTable;
    }
    public void closeWriteTable(){
        if (writeTable!=null){
            writeTable.close();
            writeTable=null;
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String q1 = TransactionsHandler.getInstance(null).onCreate();
        String q2 = AccountsHandler.getInstance(null).onCreate();
        Log.d("query1",q1);
        Log.d("query2",q2);
        sqLiteDatabase.execSQL(q1);
        sqLiteDatabase.execSQL(q2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String q1 = TransactionsHandler.getInstance(null).onUpgrade();
        String q2 = AccountsHandler.getInstance(null).onUpgrade();
        sqLiteDatabase.execSQL(q1);
        sqLiteDatabase.execSQL(q2);
        onCreate(sqLiteDatabase);
    }
}
