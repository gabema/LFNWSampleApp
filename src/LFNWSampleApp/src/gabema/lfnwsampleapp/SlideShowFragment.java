package gabema.lfnwsampleapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public final class SlideShowFragment extends Fragment
{
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.slide_show, null);

		WebView presentation = (WebView) view.findViewById(R.id.presentation);
		presentation.getSettings().setJavaScriptEnabled(true);
		presentation.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				return false;
			}
		});
		presentation.loadUrl("https://docs.google.com/presentation/d/1RyCTjGct6oWazSU6KuEnZbYWdH4lyZA5Bw4zN10yWks/edit?usp=sharing");

		return view;
	}
}
