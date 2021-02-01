package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DbHelper extends SQLiteOpenHelper {

    public  static final String DB_NAME = "180335F";
    public  static final String TABLE_1 = "account";
    public  static final String TABLE_2 = "transaction_table";

    //columns names
    private static final String ACCOUNT_NO = "accountNo";
    private static final String BANK_NAME = "bankName";
    private static final String ACCOUNT_HOLDER_NAME = "accountHolderName";
    private static final String BALANCE = "balance";
    private static final String DATE = "date";
    private static final String EXPENSE_TYPE = "expenseType";
    private static final String AMOUNT = "amount";





    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 2
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLE_CREATE_QUERY_1 ="CREATE TABLE " + TABLE_1+
                " ("
                +ACCOUNT_NO+ " TEXT(50) PRIMARY KEY,"
                +BANK_NAME+ " TEXT(50) ,"
                +ACCOUNT_HOLDER_NAME+ " TEXT(50),"
                +BALANCE+ " REAL"
                +" )";

        String TABLE_CREATE_QUERY_2 ="CREATE TABLE " + TABLE_2+
                " ("
                +ACCOUNT_NO+ " TEXT(50)  ,"
                +DATE+ " date,"
                +EXPENSE_TYPE+ " TEXT(20),"
                +AMOUNT+ " REAL,FOREIGN KEY ("+ACCOUNT_NO+") REFERENCES "+TABLE_1+"(" +ACCOUNT_NO+ "))";

        db.execSQL(TABLE_CREATE_QUERY_1);
        db.execSQL(TABLE_CREATE_QUERY_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String del_sql1 = "DROP TABLE IF EXISTS "+TABLE_1;
        String del_sql2 = "DROP TABLE IF EXISTS "+TABLE_2;
        db.execSQL(del_sql1);
        db.execSQL(del_sql2);
        onCreate(db);
    }

    public boolean addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_NO,account.getAccountNo());
        contentValues.put(BANK_NAME,account.getBankName());
        contentValues.put(ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(BALANCE,account.getBalance());
        long result = db.insert(TABLE_1,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public boolean updateAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_NO,account.getAccountNo());
        contentValues.put(BANK_NAME,account.getBankName());
        contentValues.put(ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(BALANCE,account.getBalance());
        long result = db.update(TABLE_1,contentValues,ACCOUNT_NO+" = ?",new String[]{account.getAccountNo()});
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public Account getAccount(String accNo){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_1+" WHERE "+ACCOUNT_NO+" = ?";
        Cursor cursor = db.rawQuery(query,new String[]{accNo});
        Account account = null;

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getDouble(3);
                account = new Account(accountNo, bankName, accountHolderName, balance);
            }
        }
        return account;
    }

    public ArrayList<Account> getAllAccounts(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_1;
        Cursor cursor = db.rawQuery(query,null);
        ArrayList<Account> accountLst = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                double balance = cursor.getDouble(3);
                accountLst.add(new Account(accountNo, bankName, accountHolderName, balance));
            }
        }
        return accountLst;
    }

    public boolean deleteAccount(String accountNo){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_1,"accountNo = "+accountNo,null) > 0;
    }

    public boolean logTransaction(Transaction transaction){

        DateFormat format = new SimpleDateFormat("m-d-yyyy", Locale.ENGLISH);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_NO,transaction.getAccountNo());
        contentValues.put(DATE,format.format(transaction.getDate()));
        contentValues.put(EXPENSE_TYPE,transaction.getExpenseType().toString());
        contentValues.put(AMOUNT,transaction.getAmount());
        long res = db.insert(TABLE_2,null,contentValues);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<Transaction> getTransactions(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_2;
        Cursor cursor = db.rawQuery(query,null);
        return extractCursor(cursor);
    }

    public ArrayList<Transaction> getTransactions(int limit){
        String query = "SELECT * FROM "+TABLE_2+" LIMIT "+limit;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return extractCursor(cursor);
    }

    private ArrayList<Transaction> extractCursor(Cursor cursor){
        ArrayList<Transaction> transactionList = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("m-d-yyyy", Locale.ENGLISH);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String accountNo = cursor.getString(0);
                Date date = new Date();
                try {
                    date = format.parse(cursor.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(2));
                double amount = cursor.getDouble(3);
                transactionList.add(new Transaction(date, accountNo, expenseType, amount));
            }
        }
        return transactionList;
    }



}

