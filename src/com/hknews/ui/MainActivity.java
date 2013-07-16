package com.hknews.ui;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import com.hknews.R;
import com.hknews.provider.News;
import com.hknews.provider.NewsProvider;
import com.hknews.service.DownloadNewsService;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class MainActivity extends ListActivity implements
        LoaderCallbacks<Cursor> {
	private static final String TAG = "MainActivity";
	private NewsContentObserver mNewsContentObserver;
	private Handler mHandler;
	private LoaderManager mLoaderManager;
	private Loader<Cursor> mLoader;
	private SimpleCursorAdapter adapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.startService(new Intent(DownloadNewsService.ACTION_UPDATE_NEWS));
		mLoaderManager = getLoaderManager();
		mLoader = mLoaderManager.initLoader(1, null, this);
		mHandler = new Handler();
		mNewsContentObserver = new NewsContentObserver(mHandler);

		this.getContentResolver().registerContentObserver(
		        NewsProvider.CONTENT_URI, true, mNewsContentObserver);

		// Set a listener to be invoked when the list should be refreshed.
		((PullToRefreshListView) getListView())
		        .setOnRefreshListener(new OnRefreshListener() {
			        @Override
			        public void onRefresh() {
				        MainActivity.this.startService(new Intent(
				                DownloadNewsService.ACTION_UPDATE_NEWS));
			        }
		        });

		adapter = new SimpleCursorAdapter(this,
		        android.R.layout.simple_list_item_1, null,
		        new String[] { News.NewsColumns.TITLE },
		        new int[] { android.R.id.text1 },
		        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position,
			        long id) {
				Cursor c = adapter.getCursor();
				c.moveToPosition(position - 1);
				int rowId = c.getColumnIndexOrThrow(News.NewsColumns.URL);

				String url = c.getString(rowId);
				Intent i = new Intent(MainActivity.this, WebViewActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		setListAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.getContentResolver().unregisterContentObserver(
		        mNewsContentObserver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class NewsContentObserver extends ContentObserver {

		public NewsContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			Log.d(TAG, "onChange");
			super.onChange(selfChange);
			mLoader.forceLoad();

		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Log.d(TAG, "onCreateLoader");
		return new CursorLoader(this, NewsProvider.CONTENT_URI, new String[] {
		        News.NewsColumns.ID, News.NewsColumns.TITLE,
		        News.NewsColumns.URL }, News.NewsColumns.IS_CENTROID + "= 1", null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
		Log.d(TAG, "onLoadFinished:" + c.getCount());
		adapter.swapCursor(c);
		((PullToRefreshListView) getListView()).onRefreshComplete();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

}
