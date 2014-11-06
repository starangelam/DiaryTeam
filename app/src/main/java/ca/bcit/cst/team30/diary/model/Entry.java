package ca.bcit.cst.team30.diary.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Representing a diary entry.
 */
public class Entry {

	private static String DaysOfWeek[] = {"SUN", "MON", "TUE", "WED", "THUR", "FRI", "SAT"};
	private Calendar cal = Calendar.getInstance();
	/**
	 * Entry's unique id. Will be set by database.
	 */
	private long id;
	private String title;
	private String content;
	private Date creationDate;

	public Entry() {
		this.creationDate = cal.getTime();
	}

	/**
	 * Constructor for quickly constructing an entry with required info when not reading from database.
	 *
	 * @param title   Entry title
	 * @param content Entry text content
	 */
	public Entry(final String title, final String content) {
		this.title = title;
		this.content = content;
		this.creationDate = cal.getTime();
	}

	/**
	 * Constructor for reading entries from database.
	 *
	 * @param id           Entry's unique id
	 * @param title        Entry title
	 * @param content      Entry text content
	 * @param creationDate date entry was created on
	 */
	public Entry(final long id, final String title, final String content, final Date creationDate) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.creationDate = creationDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		cal.setTime(creationDate);
	}

	/**
	 * Get day of month, derived property of creation date.
	 *
	 * @return day of month
	 */
	public int getDayOfMoth() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Get day of week, derived property of creation date.
	 *
	 * @return day of week in abbreviated string format (e.g. MON)
	 */
	public String getDayOfWeek() {
		return android.text.format.DateFormat.format("EEE", creationDate).toString();
	}
}
