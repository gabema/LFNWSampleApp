package gabema.lfnwsampleapp.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

/**
 * Demonstrates how a ContentProvider could be backed by web content.
 * @author Gabe Martin
 */
public class SampleWebProvider extends ContentProvider {
	public SampleWebProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getType(Uri uri) {
		return "vnd.android.cursor.item"; // single record
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO: Implement this to handle requests to insert a new row.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	/**
	 * Querying requires selectionArgs[0] to contain a URI (network or local) to JSON content.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder)
	{
		MatrixCursor cursor = null;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			URL url = new URL(selection);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String input = reader.readLine();
			while (input != null)
			{
				builder.append(input).append('\n');
				input = reader.readLine();
			}

			cursor = new MatrixCursor(new String[] { "data" });
			cursor.addRow(new Object[] { builder.toString() });
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
			{
				try {
					reader.close();
				} catch (IOException ioEx) {
					ioEx.printStackTrace();
				}
			}
		}
		

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
