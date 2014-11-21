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
    private String filePath;

	public Entry() {
		this.creationDate = cal.getTime();
	}

	/**
	 * Constructor for quickly constructing an entry with required info when not reading from database.
	 *
	 * @param title   Entry title
	 * @param content Entry text content
	 */
	public Entry(final String title, final String content, final String filePath) {
		this.title = title;
		this.content = content;
		this.creationDate = cal.getTime();
        this.filePath = filePath;
	}

	/**
	 * Constructor for reading entries from database.
	 *
	 * @param id           Entry's unique id
	 * @param title        Entry title
	 * @param content      Entry text content
	 * @param creationDate date entry was created on
	 */
	public Entry(final long id, final String title, final String content, final Date creationDate, final String filePath) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.creationDate = creationDate;
        this.filePath = filePath;
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

    public String getFilePath() { return filePath; }

    public void setFilePath(String filePath) { this.filePath = filePath; }

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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("Title: ").append(title).append('\n');
		sb.append("Date: " ).append(creationDate.toString()).append('\n');
		sb.append("Content: " ).append(content).append('\n');
        sb.append("FilePath: " ).append(filePath).append('\n');

		return sb.toString();
	}
}
