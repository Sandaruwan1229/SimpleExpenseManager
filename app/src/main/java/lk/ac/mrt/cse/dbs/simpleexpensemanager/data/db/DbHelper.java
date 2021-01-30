package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public  static final String DB_NAME = "180335F";
    public  static final String TABLE_1 = "account";
    public  static final String TABLE_2 = "transaction_table";


    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 2
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table_q1 ="CREATE TABLE " + TABLE_1+ " (accountNo TEXT(50) PRIMARY KEY,bankName TEXT(50),accountHolderName TEXT(50),balance REAL) ";
        String table_q2 = " CREATE TABLE "+TABLE_2+" (accountNo TEXT(50) ,date date, expenseType TEXT(20),amount REAL,FOREIGN KEY (accountNo) REFERENCES "+TABLE_1+"(accountNo))";
        db.execSQL(table_q1);
        db.execSQL(table_q2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String del_sql1 = "DROP TABLE IF EXISTS "+TABLE_1;
        String del_sql2 = "DROP TABLE IF EXISTS "+TABLE_2;
        db.execSQL(del_sql1);
        db.execSQL(del_sql2);
        onCreate(db);
    }

    



}

