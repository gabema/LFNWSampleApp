package gabema.lfnwsampleapp.content;

import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.MatrixCursor;

public final class ContentProviderUtility
{
	/**
	 * Returns an array of Objects if map contains all the fieldNames specified in the projection
	 * @param row
	 * @param projection
	 * @return An Object[] of fieldValues or null if there was an unmatched fieldName in the projection.
	 */
	public static Object[] tryGetValuesForProjection(Map<String, Object> row,
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

	public static Cursor createMatrixCursor(List<Map<String, Object>> m_data,
			String[] projection) {
		MatrixCursor cursor = new MatrixCursor(projection);
		for (Map<String, Object> row : m_data)
		{
			Object[] columnValues = ContentProviderUtility.tryGetValuesForProjection(row, projection);
			if (columnValues != null)
				cursor.addRow(columnValues);
		}
		return cursor;
	}

	private ContentProviderUtility() { }
}
