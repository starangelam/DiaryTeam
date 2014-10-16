package ca.bcit.cst.team30.diary.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Representing a diary entry.
 */
public class Entry {

	private Calendar cal;
	private static String DaysOfWeek[] = {"MON", "TUE", "WED", "THUR", "FRI", "SAT", "SUN"};

	private String title;
	private String content;
	private Date creationDate;

	public Entry() {
		this.cal = Calendar.getInstance();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
		cal.setTime(creationDate);
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Get day of month, derived property of creation date.
	 * @return day of month
	 */
	public int getDayOfMoth() {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Get day of week, derived property of creation date.
	 * @return day of week in abbreviated string format (e.g. MON)
	 */
	public String getDayOfWeek() {
		final int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return DaysOfWeek[dayOfWeek];
	}
}
