package com.owl.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class cartSQLiteOpenHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_FILE = "cart.db";
	public static final String TABLE_NAME       = "cart";

	public cartSQLiteOpenHelper(Context context) {
		super(context, DATABASE_FILE, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE_NAME + " ("
			+"num integer primary key autoincrement"
			+", rowid integer"
			+", prodname varchar"
			+", sortname varchar"
			+", price integer"
			+", ea integer"
			+", maxea integer"
			+", img varchar"
			+");";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists " + TABLE_NAME + ";";
		db.execSQL(sql);
		onCreate(db);
	}

}
