package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DbHelper dbHelper;

    public PersistentAccountDAO(Context context){
    this.dbHelper = new DbHelper(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> accountNumbers = new ArrayList<>();
        ArrayList<Account> accountsLst = dbHelper.getAllAccounts();
        if (accountsLst.size() !=0){
            for(Account a : accountsLst){
                accountNumbers.add(a.getAccountNo());
            }
        }
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        return dbHelper.getAllAccounts();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return dbHelper.getAccount(accountNo);
    }

    @Override
    public void addAccount(Account account) {
        dbHelper.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        dbHelper.deleteAccount(accountNo);

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if(accountNo ==null){
            throw new InvalidAccountException("Invalid Account Number");

        }
        Account account = dbHelper.getAccount(accountNo);
        double balance = account.getBalance();
        if(expenseType == ExpenseType.INCOME){
            account.setBalance(balance + amount);
        }else if (expenseType == ExpenseType.EXPENSE){
            account.setBalance(balance-amount);
        }

        if(account.getBalance()<0 ){
            throw new InvalidAccountException("Insufficient credit");
        }
        else{
            dbHelper.updateAccount(account);
        }
    }
}
