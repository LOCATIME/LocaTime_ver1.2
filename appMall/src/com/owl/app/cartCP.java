package com.owl.app;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class cartCP extends ContentProvider {
	public static final String uriString = "content://com.owl.app.cartProvider/cart";
	public static final Uri CONTENT_URI = Uri.parse(uriString);
	SQLiteDatabase db;
	cartSQLiteOpenHelper helper;
	public String TABLE_NAME = cartSQLiteOpenHelper.TABLE_NAME;
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String whereClause = selection;
		String[] whereArgs = selectionArgs;
		int result = db.delete(TABLE_NAME, whereClause, whereArgs);
		return result;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		db = helper.getWritableDatabase();
        long rowId = db.insert(cartSQLiteOpenHelper.TABLE_NAME, "", values);
        if (rowId > 0) {
            Uri rowUri = ContentUris.appendId(CONTENT_URI.buildUpon(), rowId).build();
            getContext().getContentResolver().notifyChange(rowUri, null);
            return rowUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		helper = new cartSQLiteOpenHelper(getContext());
		db = helper.getWritableDatabase();
//		TABLE_NAME = cartSQLiteOpenHelper.TABLE_NAME;
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection
			, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = helper.getReadableDatabase();
        qb.setTables(TABLE_NAME);
        Cursor c = qb.query(db, projection, selection, null, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection
			, String[] selectionArgs) {
		String whereClause = selection;
		String[] whereArgs = selectionArgs;
		int result = db.update(TABLE_NAME, values, whereClause, whereArgs);
		return result;
	}

}
