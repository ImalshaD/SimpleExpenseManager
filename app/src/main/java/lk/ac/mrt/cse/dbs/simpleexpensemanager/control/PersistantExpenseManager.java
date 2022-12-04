package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;


public class PersistantExpenseManager extends ExpenseManager {
    private final Context context;

    public PersistantExpenseManager(Context context) {
        super();
        this.context = context;
        setup();
    }

    @Override
    public void setup()  {
        TransactionDAO persistantTransactionDAO = new PersistantTransactionDAO(context);
        setTransactionsDAO(persistantTransactionDAO);

        AccountDAO persistantAccountDAO = new PersistantAccountDAO(context);
        setAccountsDAO(persistantAccountDAO);

    }
}
