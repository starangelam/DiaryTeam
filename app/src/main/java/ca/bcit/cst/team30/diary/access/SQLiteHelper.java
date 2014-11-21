package ca.bcit.cst.team30.diary.access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Defines Database schema. Define onCreate & onUpdate behavior.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME;
	private static final int DATABASE_VERSION;
	private static final String DATABASE_CREATE;

	public static final String TABLE_ENTRIES;
	public static final String COLUMN_ID;
	public static final String COLUMN_TITLE;
	public static final String COLUMN_CONTENT;
	public static final String COLUMN_CREATION_DATE;
    public static final String COLUMN_FILE_PATH;

	static
	{
		DATABASE_VERSION = 1;
		DATABASE_NAME    = "entries.db";

		TABLE_ENTRIES 	= "entries";
		COLUMN_ID      	= "_id";
		COLUMN_TITLE 	= "title";
		COLUMN_CONTENT 	= "content";
		COLUMN_CREATION_DATE = "creationDate";
        COLUMN_FILE_PATH = "filePath";

		DATABASE_CREATE  = "CREATE TABLE " +
				TABLE_ENTRIES + "(" +
				COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, " +
				COLUMN_TITLE + " TEXT not null, " +
				COLUMN_CONTENT + " TEXT not null, " +
				COLUMN_CREATION_DATE + " TEXT not null, " +
                COLUMN_FILE_PATH + "TEXT);";
	}

	public SQLiteHelper(final Context context)
	{
		super(context,
				DATABASE_NAME,
				null,
				DATABASE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase db,
						  final int oldVersion, final int newVersion) {
		Log.w(SQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
		onCreate(db);
	}
}