package ca.bcit.cst.team30.diary.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.bcit.cst.team30.diary.model.Entry;

/**
 * Manage CRUD operation of Entry object in SQLite Database.
 */
public class EntryDataSource {

	private final SQLiteHelper dbHelper;
	private final String[] allColumns;
	private SQLiteDatabase database;

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

	{
		allColumns = new String[]
				{
						SQLiteHelper.COLUMN_ID,
						SQLiteHelper.COLUMN_TITLE,
						SQLiteHelper.COLUMN_CONTENT,
						SQLiteHelper.COLUMN_CREATION_DATE,
                        SQLiteHelper.COLUMN_FILE_PATH
				};
	}

	public EntryDataSource(final Context context) {
		dbHelper = new SQLiteHelper(context);
	}

	public void open()
			throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void createEntry(final Entry entry) {
		final String ISOdate = dateFormat.format(entry.getCreationDate());
		final ContentValues values;
		final long insertId;
		final Cursor cursor;

		values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_TITLE, entry.getTitle());
		values.put(SQLiteHelper.COLUMN_CONTENT, entry.getContent());
		values.put(SQLiteHelper.COLUMN_CREATION_DATE, ISOdate);
        values.put(SQLiteHelper.COLUMN_FILE_PATH, entry.getFilePath());
		insertId = database.insert(SQLiteHelper.TABLE_ENTRIES,
				null,
				values);
		cursor = database.query(SQLiteHelper.TABLE_ENTRIES,
				allColumns,
				SQLiteHelper.COLUMN_ID + " = " + insertId,
				null,
				null,
				null,
				null);
		cursor.moveToFirst();
		cursor.close();
	}

	public void deleteEntry(final Entry entry) {
		final long id = entry.getId();
		deleteEntry(id);
	}

	public void deleteEntry(final long id) {
		Log.d("Info", "Entry deleted with id: " + id);
		database.delete(SQLiteHelper.TABLE_ENTRIES,
				SQLiteHelper.COLUMN_ID + " = " + id,
				null);
	}

	public int editEntry(final Entry entry) {
		final ContentValues values;
		final String ISOdate;
		final int numRowsAffected;

		ISOdate = dateFormat.format(entry.getCreationDate());

		values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_TITLE, entry.getTitle());
		values.put(SQLiteHelper.COLUMN_CONTENT, entry.getContent());
		values.put(SQLiteHelper.COLUMN_CREATION_DATE, ISOdate);

		numRowsAffected = database.update(
				SQLiteHelper.TABLE_ENTRIES,
				values,
				SQLiteHelper.COLUMN_ID + " =?",
				new String[] { String.valueOf(entry.getId()) }
		);

		return numRowsAffected;
	}

	/**
	 * Get a diary entry from database by id.
	 *
	 * @param id entry unique identifier
	 * @return Entry with specified id.
	 */
	public Entry getEntry(long id) {
		final Entry entry;
		final Cursor cursor;

		cursor = database.query(SQLiteHelper.TABLE_ENTRIES,
				allColumns,
				SQLiteHelper.COLUMN_ID + "=?",
				new String[]{String.valueOf(id)},
				null,
				null,
				null,
				null);

		try {
			cursor.moveToFirst();
			entry = cursorToEntry(cursor);
		} finally {
			// make sure to close the cursor
			cursor.close();
		}

		return entry;
	}

	public List<Entry> getAllEntries() {
		final List<Entry> entries;
		final Cursor cursor;

		String orderBy = dbHelper.COLUMN_CREATION_DATE + " DESC";

		entries = new ArrayList<Entry>();
		cursor = database.query(SQLiteHelper.TABLE_ENTRIES,
				allColumns,
				null,
				null,
				null,
				null,
				orderBy);

		try {
			cursor.moveToFirst();

			while (!(cursor.isAfterLast())) {
				final Entry entry;

				entry = cursorToEntry(cursor);
				entries.add(entry);
				cursor.moveToNext();
			}
		} finally {
			// make sure to close the cursor
			cursor.close();
		}

		return (entries);
	}

	private Entry cursorToEntry(final Cursor cursor) {
		final Entry entry;

		entry = new Entry();
		entry.setId(cursor.getLong(0));
		entry.setTitle(cursor.getString(1));
		entry.setContent(cursor.getString(2));
		String dateText = cursor.getString(3);
		try {
			Date date = dateFormat.parse(dateText);
			entry.setCreationDate(date);
		} catch (ParseException e) {
			Log.d("EntryDataSource.ERROR", "unable to convert date string " + dateText + " to Date");
		}
        entry.setContent(cursor.getString(4));

		return (entry);
	}

}
