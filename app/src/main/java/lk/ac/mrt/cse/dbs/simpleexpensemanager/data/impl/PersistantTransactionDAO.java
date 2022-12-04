package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistantTransactionDAO implements TransactionDAO {
    private Context context;
    private TransactionsHandler transactionsHandler;

    public PersistantTransactionDAO(Context context) {
        this.context = context;
        transactionsHandler=TransactionsHandler.getInstance(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        transactionsHandler.addTranscation(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor cursor = transactionsHandler.getAlltransactions();
        List<Transaction> returnList = new ArrayList<Transaction>();
        if (cursor != null & cursor.moveToFirst()){
            do{
                String accountNo = cursor.getString(1);
                long ldate = cursor.getLong(2);
                Date date = new Date(ldate);
                ExpenseType enum1 = transactionsHandler.getEnum(cursor.getInt(3));
                double amount = cursor.getDouble(4);
                Transaction newTransaction = new Transaction(date,accountNo,enum1,amount);
                returnList.add(newTransaction);
            }while(cursor.moveToNext());
        }
        transactionsHandler.closeReadTable();
        return returnList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = this.getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
}
