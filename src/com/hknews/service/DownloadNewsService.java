package com.hknews.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.JsonReader;
import android.util.Log;

import com.hknews.provider.News;
import com.hknews.provider.NewsProvider;

public class DownloadNewsService extends IntentService {

	private static final String TAG = "DownloadNewsService";
	public static final String ACTION_UPDATE_NEWS = "com.hknews.update_news";

	public DownloadNewsService() {
		super("DownloadNewsService");
	}
	public DownloadNewsService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
	        List<News> newsList = getJsonFromWebService();
	        ContentValues[] contentValuesList = new ContentValues[newsList.size()];
	        for (int i = 0; i < newsList.size(); i++) {
	        	contentValuesList[i] = newsList.get(i).toContentValues();
	        }
	        this.getContentResolver().delete(NewsProvider.CONTENT_URI, null, null);
	        this.getContentResolver().bulkInsert(NewsProvider.CONTENT_URI, contentValuesList);
        } catch (IOException e) {
	        Log.e(TAG, "exception", e);
        }

	}

	private List<News> getJsonFromWebService() throws IOException {
		URL url = new URL("http://hknews-hkhk.rhcloud.com/");
		HttpURLConnection urlConnection = (HttpURLConnection) url
		        .openConnection();
		try {
			InputStream in = new BufferedInputStream(
			        urlConnection.getInputStream());
			return parseJson(in);
		} finally {
			urlConnection.disconnect();
		}
	}

	public List<News> parseJson(InputStream in) throws IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		List<News> newsList = new ArrayList<News>();
		try {
			reader.beginArray();
			while (reader.hasNext()) {
				newsList.add(readNews(reader));
			}
			reader.endArray();
		} finally {
			reader.close();
		}
		return newsList;
	}

	public News readNews(JsonReader reader) throws IOException {
		News news = new News();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("clusterId")) {
				news.mClusterId = reader.nextInt();
			} else if (name.equals("title")) {
				news.mTitle = reader.nextString();
			} else if (name.equals("isCentroid")) {
				news.mIsCentroid = reader.nextBoolean();
			} else if (name.equals("pubDate")) {
				// parse date

				String dateStr = reader.nextString();
				SimpleDateFormat formatter = new SimpleDateFormat(
				        "EEE, dd MMM yyyy HH:mm:ss zzz");
				try {
					news.mPubData = formatter.parse(dateStr).getTime();
				} catch (ParseException e) {
					news.mPubData = System.currentTimeMillis();
				}

			} else if (name.equals("url")) {
				news.mUrl = reader.nextString();
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();
		return news;
	}

}
