package com.hknews.provider;

import android.content.ContentValues;

public class News {
	public int mId;
	public int mClusterId;
	public boolean mIsCentroid;
	public String mTitle;
	public String mUrl;
	public long mPubData;

	public static final String TABLE_NAME = "news";

	public interface NewsColumns {
		public static final String ID = "_id";
		public static final String CLUSTER_ID = "cluster_id";
		public static final String IS_CENTROID = "is_centroid";
		public static final String TITLE = "title";
		public static final String URL = "url";
		public static final String PUBDATE = "pub_date";
	}

	public ContentValues toContentValues() {
		ContentValues cv = new ContentValues();
		//cv.put(NewsColumns.ID, mId);
		cv.put(NewsColumns.CLUSTER_ID, mClusterId);
		cv.put(NewsColumns.IS_CENTROID, (mIsCentroid) ? 1 : 0);
		cv.put(NewsColumns.TITLE, mTitle);
		cv.put(NewsColumns.URL, mUrl);
		cv.put(NewsColumns.PUBDATE, mPubData);
		return cv;
	}
}
