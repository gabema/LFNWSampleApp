package gabema.lfnwsampleapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * TODO: list source files in assets directory in spinner. selecting an entry should show it.
 * @author Gabe Martin
 */
public final class SourceCodeFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);

		view = inflater.inflate(R.layout.source_code, null);

		m_spinner = (Spinner) view.findViewById(R.id.spinner);
		AssetSpinnerAdapter adapter = new AssetSpinnerAdapter(Collections.<String>emptyList());
		m_spinner.setAdapter(adapter);
		m_spinner.setOnItemSelectedListener(adapter);

		m_sourceView = (TextView) view.findViewById(R.id.source);

		return view;
	}

	@Override
	public void onStart()
	{
		super.onStart();
		startLoadAssetSources();
	}

	@Override
	public void onStop()
	{
		super.onStop();
		stopLoadAssetSources();
	}

	private void startLoadAssetSources()
	{
		if (m_startLoadAssetTask == null)
		{
			m_startLoadAssetTask = new AsyncTask<Void, Void, List<String>>()
			{
				@Override
				protected void onPreExecute()
				{
					getActivity().setProgressBarIndeterminateVisibility(true);
				}

				@Override
				protected List<String> doInBackground(Void... params)
				{
					List<String> listEntries = new ArrayList<String>();
					populateSpinner(getActivity().getAssets(), listEntries, "src");
					return listEntries;
				}

				@Override
				protected void onPostExecute(List<String> result)
				{
					getActivity().setProgressBarIndeterminateVisibility(false);
					AssetSpinnerAdapter adapter = (AssetSpinnerAdapter) m_spinner.getAdapter();
					adapter.setAssetList(result);
					m_startLoadAssetTask = null;
				}

				@Override
				protected void onCancelled(List<String> result)
				{
					getActivity().setProgressBarIndeterminateVisibility(false);
					m_startLoadAssetTask = null;
				}
			};
			AsyncTaskUtility.execute(m_startLoadAssetTask);
		}
	}

	private void stopLoadAssetSources()
	{
		if (m_startLoadAssetTask != null)
			m_startLoadAssetTask.cancel(true);
	}

	protected void startLoadSourceFile(final String item)
	{
		if (m_startLoadAssetFileTask == null)
		{
			m_startLoadAssetFileTask = new AsyncTask<Void, Void, StringBuilder>()
			{
				@Override
				protected void onPreExecute()
				{
					getActivity().setProgressBarIndeterminateVisibility(true);
				}

				@Override
				protected StringBuilder doInBackground(Void... params)
				{
					StringBuilder fileContents = new StringBuilder();
					InputStreamReader reader = null;

					try
					{
						reader = new InputStreamReader(getActivity().getAssets().open(item, AssetManager.ACCESS_STREAMING));
						char buffer[] = new char[4098];
						int readLength;
						while ((readLength = reader.read(buffer)) != -1)
							fileContents.append(buffer, 0, readLength);
					}
					catch (IOException e) {
						e.printStackTrace();
						fileContents.append("Failed reading file contents");
					}
					finally
					{
						try {
							if (reader != null)
								reader.close();
						} catch (IOException ioEx) {
							ioEx.printStackTrace();
						}
					}

					return fileContents;
				}

				@Override
				protected void onPostExecute(StringBuilder result)
				{
					getActivity().setProgressBarIndeterminateVisibility(false);
					m_sourceView.setText(result.toString());
					m_startLoadAssetFileTask = null;
				}

				@Override
				protected void onCancelled(StringBuilder result)
				{
					getActivity().setProgressBarIndeterminateVisibility(false);
					m_startLoadAssetFileTask = null;
				}
			};
			AsyncTaskUtility.execute(m_startLoadAssetFileTask);
		}
	}

	private void populateSpinner(AssetManager assetManager, List<String> entries, String path)
	{
		try {
			String[] currentEntries = assetManager.list(path);
			for (String currentEntry : currentEntries)
			{
				String entryPath = path + "/" + currentEntry;
				try
				{
					// TODO: Is there a better way to check if this entry is a file other than attempting to open
					// and handle the exception assuming its a directory
					InputStream input = assetManager.open(entryPath, AssetManager.ACCESS_STREAMING);
					input.close();
					entries.add(entryPath);
				}
				catch (FileNotFoundException fileNotFound)
				{
					populateSpinner(assetManager, entries, entryPath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class AssetSpinnerAdapter extends BaseAdapter implements OnItemSelectedListener
	{
		public AssetSpinnerAdapter(List<String> assetList)
		{
			m_assetList = assetList;
		}

		public void setAssetList(List<String> assetList)
		{
			m_assetList = assetList;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return m_assetList.size();
		}

		@Override
		public String getItem(int position)
		{
			return m_assetList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
			TextView textView = (TextView) view.findViewById(android.R.id.text1);
			textView.setText(getItem(position));
			return view;
		}

		private List<String> m_assetList;

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id)
		{
			startLoadSourceFile(getItem(position));
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent)
		{
			// TODO Auto-generated method stub
		}
	}

	private static final String TAG = "SourceCodeFragment";

	private AsyncTask<Void, Void, List<String>> m_startLoadAssetTask;
	private AsyncTask<Void, Void, StringBuilder> m_startLoadAssetFileTask;

	private Spinner m_spinner;
	private TextView m_sourceView;
}
