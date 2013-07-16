package com.hknews.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class NewsProvider extends ContentProvider {
	private static final String DB_NAME = "news.db";
	public static final String AUTHORITY = "com.hknews.provider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	private NewsDatabaseHelper mDbHelper;

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// Currently, it would just delete all
		mDbHelper.getWritableDatabase().delete(News.TABLE_NAME, null, null);
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mDbHelper = new NewsDatabaseHelper(this.getContext(), DB_NAME);
		return true;
	}


	@Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
		SQLiteDatabase sqlDB = mDbHelper.getReadableDatabase();
		return sqlDB.query(News.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
    }

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int bulkInsert(Uri uri, ContentValues[] values) {
		int numInserted = 0;
		SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
		sqlDB.beginTransaction();
		try {
			for (ContentValues cv : values) {
				long newID = sqlDB.insertOrThrow(News.TABLE_NAME, null, cv);
				if (newID <= 0) {
					throw new SQLException("Failed to insert row into " + uri);
				}
			}
			sqlDB.setTransactionSuccessful();
			getContext().getContentResolver().notifyChange(uri, null);
			numInserted = values.length;
		} finally {
			sqlDB.endTransaction();
		}
		return numInserted;
	}

}
