package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.Table;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class TransactionsHandler implements Table {
    private static BankDB bankDB=null;
    private static String TABLE_NAME="Transactions";
    private static TransactionsHandler instance=null;
    //columns
    private String ID="id";
    private String ACCOUNT_NUMBER="account_number";
    private String DATE="date";
    private String EXPENSE_TYPE="expense_type";
    private String AMOUNT = "amount";
    private static ExpenseType[] enum_list = ExpenseType.values();
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
    private int getExpenseIndex(ExpenseType expenseType){
        for (int i=0;i< enum_list.length;i++){
            if (expenseType == enum_list[i]){
                return i;
            }
        }
        return -1;
    }
    private TransactionsHandler(@Nullable Context context) {
        bankDB= BankDB.getInstance(context);
    }
    public static TransactionsHandler getInstance(Context context){
        if (instance==null){
            instance= new TransactionsHandler(context);
        }
        return  instance;
    }
    @Override
    public String onCreate() {
        /*
            create table transactions (id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        account_number VARCHAR(50),
                                        date VARCHAR(30),
                                        expense_type INTEGER,
                                        amount DECIMAL(15,2)
                                        );
         */
        String tableCreateQuery = "create table " +TABLE_NAME+
                " ("+ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ACCOUNT_NUMBER +" VARCHAR(50), "+
                DATE+" TEXT, "+
                EXPENSE_TYPE+" INTEGER, "+
                AMOUNT+" DECIMAL(15,2), " +
                "FOREIGN KEY(" +ACCOUNT_NUMBER+") REFERENCES Accounts(account_num)"+
                ");";
        return tableCreateQuery;
    }

    @Override
    public String onUpgrade() {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        return dropTableQuery;
    }
    public void addTranscation(Transaction transaction){
        openWriteTable();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_NUMBER,transaction.getAccountNo());
        long date = transaction.getDate().getTime();
        contentValues.put(DATE,date);
        int expense = getExpenseIndex(transaction.getExpenseType());
        contentValues.put(EXPENSE_TYPE,expense);
        contentValues.put(AMOUNT,transaction.getAmount());
        writeTable.insert(TABLE_NAME,null,contentValues);
        closeWriteTable();
    }
    public Cursor getAlltransactions(){
        openReadTable();
        String query = "SELECT *"+" from "+TABLE_NAME+";";
        Cursor cursor = readTable.rawQuery(query,null);
        return cursor;
    }
    public ExpenseType getEnum(int i){
        return enum_list[i];
    }
}
