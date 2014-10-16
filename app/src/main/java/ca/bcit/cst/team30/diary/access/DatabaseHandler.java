package ca.bcit.cst.team30.diary.access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ca.bcit.cst.team30.diary.model.Entry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles communication with app's private SQLite database.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "entriesManager";

	// Entries table name
	private static final String TABLE_ENTRIES = "entries";

	// Entries Table Column names
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_CONTENT = "content";
	private static final String KEY_CREATION_DATE = "creationDate";

	// TODO bug - seconds are truncated
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");


	public DatabaseHandler(Context context) {
		// TODO pass cursorfactory for cursorAdapter? instead of null
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Create Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ").append(TABLE_ENTRIES).append("(");
		sb.append(KEY_ID).append(" INTEGER PRIMARY KEY,");
		sb.append(KEY_TITLE).append(" TEXT,");
		sb.append(KEY_CONTENT).append(" TEXT,");
		sb.append(KEY_CREATION_DATE).append(" TEXT)");

		final String CREATE_ENTRIES_TABLE = sb.toString();
		db.execSQL(CREATE_ENTRIES_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i2) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Add a diary entry to datebase.
	 *
	 * @param entry
	 */
	public void addEntry(Entry entry) {
		SQLiteDatabase db = this.getWritableDatabase();

		String ISOdate = dateFormat.format(entry.getCreationDate());

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, entry.getTitle());
		values.put(KEY_CONTENT, entry.getContent());
		values.put(KEY_CREATION_DATE, ISOdate);
		// TODO when to set id?

		// Inserting Row
		db.insert(TABLE_ENTRIES, null, values);
		db.close(); // Closing database connection
	}

	/**
	 * Get a diary entry from database by id.
	 *
	 * @param id entry unique identifier
	 * @return Entry with specified id.
	 */
	public Entry getEntry(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ENTRIES,
				new String[]{KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_CREATION_DATE},
				KEY_ID + "=?",
				new String[]{String.valueOf(id)}, null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}

		return populateEntry(cursor);
	}

	/**
	 * Get all diary entries.
	 *
	 * @return list of all diary entries.
	 */
	public List<Entry> getAllEntries() {
		List<Entry> entries = new ArrayList<Entry>();

		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_ENTRIES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {

				Entry entry = populateEntry(cursor);
				if (entry != null) entries.add(entry);
				else Log.d("Error", "Entry shouldn't be null!");

			} while (cursor.moveToNext());
		}

		// TODO no need to close it?
		return entries;
	}

	private Entry populateEntry(Cursor cursor) {
		if (cursor == null) {
			Log.d("LOG", "No entry found");
			return null;
		}

		try {

			Entry entry = new Entry(
					cursor.getLong(0),
					cursor.getString(1),
					cursor.getString(2),
					dateFormat.parse(cursor.getString(3))
			);

			return entry;

		} catch (ParseException e) {
			Log.d("ERROR", "unable to convert date string to Date");
			e.printStackTrace();

			return null;
		}
	}
}
