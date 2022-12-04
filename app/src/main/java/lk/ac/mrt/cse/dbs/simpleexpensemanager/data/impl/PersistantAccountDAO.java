package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistantAccountDAO implements AccountDAO {
    private AccountsHandler accountsHandler;
    private Context context;

    public PersistantAccountDAO(Context context) {
        this.context=context;
        accountsHandler = AccountsHandler.getInstance(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> returnList = new ArrayList<String>();
        Cursor cursor = accountsHandler.getAccountNumberCursor();
        if (cursor.moveToFirst()){
            returnList.add(cursor.getString(0));
            while (cursor.moveToNext()){
                returnList.add(cursor.getString(0));
            }
        }
        accountsHandler.closeReadTable();
        return returnList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> returnList = new ArrayList<Account>();
        Cursor cursor = accountsHandler.getAccountCursor();
        if (cursor !=null & cursor.moveToFirst()){
            Account newAccount = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(4));
            returnList.add(newAccount);
            while (cursor.moveToNext()){
                newAccount = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(4));
                returnList.add(newAccount);
            }
        }
        accountsHandler.closeReadTable();
        return returnList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = accountsHandler.getAccountCursor(accountNo);
        if (cursor!=null & cursor.moveToFirst()){
            Account newAccount = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));
            accountsHandler.closeReadTable();
            return  newAccount;
        }else{
            accountsHandler.closeReadTable();
            String msg="Account number "+accountNo+" not valid";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void addAccount(Account account) {
        accountsHandler.addAccount(account);
        accountsHandler.closeWriteTable();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        int val = accountsHandler.deleteAccount(accountNo);
        accountsHandler.closeWriteTable();
        if (val<=0){
            String msg="Account number "+accountNo+" not valid";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        accountsHandler.update(account);
        accountsHandler.closeWriteTable();
    }
}
