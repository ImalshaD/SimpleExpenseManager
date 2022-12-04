package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.Table;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class AccountsHandler implements Table {

    private static BankDB bankDB;
    private static final String TABLE_NAME="Accounts";
    private static AccountsHandler instance=null;
    //Columns
    private final String ACCOUNT_NUMBER="account_num";
    private final String BANK_NAME="bank_name";
    private final String HOLDER_NAME="account_holder_name";
    private final String BALANCE="balance";
    private SQLiteDatabase readTable=null;
    private SQLiteDatabase writeTable=null;

    public void openReadTable(){
        if (readTable==null) {
            readTable = bankDB.openReadTable();
        }
    }
    public void closeReadTable(){
        if (readTable!=null){
            bankDB.closeReadTable();
            readTable=null;
        }
    }
    public void openWriteTable(){
        if (writeTable==null) {
            writeTable = bankDB.openWriteTable();
        }
    }
    public void closeWriteTable(){
        if (writeTable!=null){
            bankDB.closeWriteTable();
            writeTable=null;
        }
    }
    private AccountsHandler(@Nullable Context context) {
        bankDB = BankDB.getInstance(context);
    }
    public static AccountsHandler getInstance(@Nullable Context context){
        if (instance==null){
            instance = new AccountsHandler(context);
        }
        return instance;
    }
    @Override
    public String onCreate() {
        /*create table Accounts (account_num VARCHAR(50) PRIMARY KEY,
                                 bank_name VARCHAR(20),
                                 account_holder_name VARCHAR(100)
                                 balance DECIMAL(15,2));
        */
        String tableCreateQuery = "create table " +TABLE_NAME+
                "("+ACCOUNT_NUMBER + " VARCHAR(50) PRIMARY KEY, "+
                BANK_NAME +" VARCHAR(20), "+
                HOLDER_NAME+" VARCHAR(100), "+
                BALANCE+" DECIMAL(15,2));";
        return tableCreateQuery;
    }

    @Override
    public String onUpgrade() {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        return dropTableQuery;
    }

    public void addAccount(Account account){
        openWriteTable();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_NUMBER,account.getAccountNo());
        contentValues.put(BANK_NAME,account.getBankName());
        contentValues.put(HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(BALANCE,account.getBalance());
        writeTable.insert(TABLE_NAME,null,contentValues);
        closeWriteTable();
    }
    public Cursor getAccountNumberCursor(){
        openReadTable();
        String query = "SELECT "+ACCOUNT_NUMBER+" from "+TABLE_NAME+";";
        Cursor cursor = readTable.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAccountCursor(){
        openReadTable();
        String query = "SELECT *"+" from "+TABLE_NAME+";";
        Cursor cursor = readTable.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAccountCursor(String accountNumber){
        openReadTable();
        String query = "SELECT *"+" from "+TABLE_NAME+" WHERE ("+ACCOUNT_NUMBER+"='"+accountNumber+"');";
        Cursor cursor = readTable.rawQuery(query,null);
        return cursor;
    }
    public int deleteAccount(String accountNumber){
        /*
            return 1 if success
            else 0
         */
        openWriteTable();
        int i = writeTable.delete(TABLE_NAME,ACCOUNT_NUMBER+"=?",new String []{accountNumber});
        return i;
    }
    public void update(Account account){
        openWriteTable();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BANK_NAME,account.getBankName());
        contentValues.put(HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(BALANCE,account.getBalance());
        writeTable.update(TABLE_NAME,contentValues,ACCOUNT_NUMBER+"=?",new String[]{account.getAccountNo()});
    }
}
