package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private DbHelper dbHelper;

    public PersistentTransactionDAO(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account = dbHelper.getAccount(accountNo);
        double balance = account.getBalance();
        if (expenseType == ExpenseType.EXPENSE && (balance-amount) >=0){
            Transaction transaction = new Transaction(date,accountNo,expenseType,amount);
            dbHelper.logTransaction(transaction);
        }
        else if(expenseType == ExpenseType.INCOME){
            Transaction transaction = new Transaction(date,accountNo,expenseType,amount);
            dbHelper.logTransaction(transaction);
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return dbHelper.getTransactions();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return dbHelper.getTransactions(limit);
    }
}
