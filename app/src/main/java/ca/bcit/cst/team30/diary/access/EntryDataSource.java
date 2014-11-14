package ca.bcit.cst.team30.diary.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ca.bcit.cst.team30.diary.model.Entry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Manage CRUD operation of Entry object in SQLite Database.
 */
public class EntryDataSource {

	private final SQLiteHelper dbHelper;
	private final String[] allColumns;
	private SQLiteDatabase database;

	// TODO bug - seconds are truncated
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

	{
		allColumns = new String[]
				{
						SQLiteHelper.COLUMN_ID,
						SQLiteHelper.COLUMN_TITLE,
						SQLiteHelper.COLUMN_CONTENT,
						SQLiteHelper.COLUMN_CREATION_DATE
				};
	}

	public EntryDataSource(final Context context) {
		// TODO pass cursorfactory for cursorAdapter? instead of null
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
		final long id;

		id = entry.getId();
		Log.d("Info", "Entry deleted with id: " + id);
		database.delete(SQLiteHelper.TABLE_ENTRIES,
				SQLiteHelper.COLUMN_ID + " = " + id,
				null);
	}

	/**
	 * Get a diary entry from database by id.
	 *
	 * @param id entry unique identifier
	 * @return Entry with specified id.
	 */
	public Entry getEntry(int id) {
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
			while (!(cursor.isAfterLast())) {
				cursor.moveToFirst();
			}
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
		try {
			Date date = dateFormat.parse(cursor.getString(3));
			entry.setCreationDate(date);
		} catch (ParseException e) {
			Log.d("ERROR", "unable to convert date string to Date");
			e.printStackTrace();
		}

		return (entry);
	}

}
