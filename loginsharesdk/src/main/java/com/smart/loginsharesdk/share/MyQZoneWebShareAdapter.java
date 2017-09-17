package com.smart.loginsharesdk.share;

import android.view.View;
import android.view.Window;

import cn.sharesdk.tencent.qzone.QZoneWebShareAdapter;

/** 一个用于演示{@link cn.sharesdk.tencent.qzone.QZoneWebShareAdapter}的例子。 */
public class MyQZoneWebShareAdapter extends QZoneWebShareAdapter {

	public void onCreate() {
		// 设置页面以Dialog的方式展示
//		getActivity().setTheme(android.R.style.Theme_Dialog);
		getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate();

		// 修改页面标题
//		getTitleLayout().getTvTitle().setText(R.string.qzone_customer_share_style);
        getTitleLayout().setVisibility(View.GONE);
		// 下面的代码可以拦截webview加载的页面地址，但是添加后，分享操作将可能无法正确执行
//		getWebBody().setWebViewClient(new WebViewClient() {
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				System.out.println("=========== " + url);
//				return super.shouldOverrideUrlLoading(view, url);
//			}
//		});

	}

}
