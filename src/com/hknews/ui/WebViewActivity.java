package com.hknews.ui;

import com.hknews.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
	private static final String WEB_VIEW_SAVED_STATE_KEY = "webViewSavedState";
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.webview_activity);

		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		Intent intent = getIntent();
		if (intent.getData() != null && savedInstanceState == null) {
			mWebView.loadUrl(intent.getDataString());
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Bundle webViewSavedState = new Bundle();
		mWebView.saveState(webViewSavedState);
		outState.putBundle(WEB_VIEW_SAVED_STATE_KEY, webViewSavedState);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// Restore the state of the WebView
		mWebView.restoreState(savedInstanceState
		        .getBundle(WEB_VIEW_SAVED_STATE_KEY));
	}

}