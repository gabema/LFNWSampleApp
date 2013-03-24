package gabema.lfnwsampleapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

/**
 * Demonstrates how a ContentProvider could be backed by memory.<br/>
 * Rows can be inserted and as long as they contain the necessary fields can be queried.<br/>
 * Usage:
 * <code>
 * root@android:/ # content insert --uri content://lfnwsimple --bind test:s:hello --bind test2:s:world
 * root@android:/ # content query --uri content://lfnwsimple --uri content://lfnwsimple --projection test:test2
 * </code>
 * @author Gabe Martin
 */
public class SimpleProvider extends ContentProvider {
	public SimpleProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getType(Uri uri) {
		Log.i(TAG, "getType(uri=" + uri + ")");
		return "vnd.android.cursor.item"; // single record
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.i(TAG, "insert(uri=" + uri + ", contentValues...)");

		Map<String, Object> row = new HashMap<String, Object>();

		for (Map.Entry<String, Object> entry : values.valueSet())
			row.put(entry.getKey(), entry.getValue());

		m_data.add(row);
		Uri newUri = uri.buildUpon()
				.appendPath("item")
				.appendPath(Integer.toString(m_data.size() - 1))
				.build();

		// TODO: Should call notifyChange? See parent javadoc for insert
		return newUri;
	}

	@Override
	public boolean onCreate() {
		Log.i(TAG, "onCreate()");
		m_data = new ArrayList<Map<String, Object>>(16);
		return false;
	}

	/**
	 * @return rows that have all the requested columns.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.i(TAG, "query(...)");
		MatrixCursor cursor = new MatrixCursor(projection);
		for (Map<String, Object> row : m_data)
		{
			Object[] columnValues = tryGetValuesForProjection(row, projection);
			if (columnValues != null)
				cursor.addRow(columnValues);
		}

		return cursor;
	}

	/**
	 * Returns an array of Objects if map contains all the fieldNames specified in the projection
	 * @param row
	 * @param projection
	 * @return An Object[] of fieldValues or null if there was an unmatched fieldName in the projection.
	 */
	private Object[] tryGetValuesForProjection(Map<String, Object> row,
			String[] projection) {

		Object[] values = new Object[projection.length];
		int index = 0;
		for (String fieldName : projection)
		{
			if (row.containsKey(fieldName))
			{
				values[index++] = row.get(fieldName);
			}
			else
			{
				// this row does not have all field mappings so break
				values = null;
				break;
			}
		}
		return values;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	List<Map<String, Object>> m_data;

	private static final String TAG = "SimpleProvider";
}
