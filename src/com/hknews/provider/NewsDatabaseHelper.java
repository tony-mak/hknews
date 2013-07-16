package com.hknews.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDatabaseHelper extends SQLiteOpenHelper {
	private static final int DB_VERSION = 1; 
	
	public NewsDatabaseHelper(Context context, String name) {
		super(context, name, null, DB_VERSION);
	}
	
	public NewsDatabaseHelper(Context context, String name,
	        CursorFactory factory, int version) {
		super(context, name, factory, version);
	}



	@Override
    public void onCreate(SQLiteDatabase db) {
		createNewsTable(db);
    }



	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " +News.TABLE_NAME);
		createNewsTable(db);
    }



	static void createNewsTable(SQLiteDatabase db) {
		db.execSQL("create table " + News.TABLE_NAME + " ("+
				News.NewsColumns.ID + " integer primary key autoincrement," +
				News.NewsColumns.CLUSTER_ID + " integer," +
				News.NewsColumns.IS_CENTROID + " integer," +
				News.NewsColumns.PUBDATE + " integer," + 
				News.NewsColumns.TITLE + " text," +
				News.NewsColumns.URL + " text)"
				);
	}
}
