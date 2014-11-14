package ca.bcit.cst.team30.diary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.bcit.cst.team30.diary.R;
import ca.bcit.cst.team30.diary.model.Entry;

import java.util.List;

/**
 * User: ama
 * Date: 2014-11-13
 * Time: 7:24 PM
 */
public class EntryListAdapter extends ArrayAdapter<Entry> {
	private static final int MAX_CHAR_PER_LINE = 35;
	private static final int MAX_LINES_IN_BODY = 2;
	private static final String MORE_TEXT_SYMBOL = "...";

	private Context mContext;
	private List<Entry> data = null;

	public EntryListAdapter(Context context, List<Entry> data) {
		super(context, R.layout.component_entry_preview, data);

		mContext = context;
		this.data = data;
	}

	/* 	TODO
		1. add time stamp to entry previews
		2. investigate why textView is cropping text
	 */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		final String weekText;
		final String monthText;
		final String titleText;
		final String bodyText;

		final Entry entry = data.get(position);

		if (convertView == null) {
			viewHolder = new ViewHolder();
			final LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.component_entry_preview, parent, false);

			viewHolder.dayOfWeek = (TextView) convertView.findViewById(R.id.entry_preview_day_of_week);
			viewHolder.dayOfMonth = (TextView) convertView.findViewById(R.id.entry_preview_day_of_month);
			viewHolder.title = (TextView) convertView.findViewById(R.id.entry_preview_title);
			viewHolder.body = (TextView) convertView.findViewById(R.id.entry_preview_body);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		weekText = entry.getDayOfWeek().toUpperCase();
		monthText = Integer.toString(entry.getDayOfMoth());
		titleText = truncatedText(entry.getTitle(), MAX_CHAR_PER_LINE);
		bodyText = truncatedText(entry.getContent(), MAX_CHAR_PER_LINE * MAX_LINES_IN_BODY);

		viewHolder.dayOfWeek.setText(weekText);
		viewHolder.dayOfMonth.setText(monthText);
		viewHolder.title.setText(titleText);
		viewHolder.body.setText(bodyText);

		return convertView;
	}

	private String truncatedText(final String fullText, final int maxLength) {
		String truncatedTitle = fullText;

		final int length = fullText.length();
		if (length > maxLength) {
			truncatedTitle = fullText.substring(0, maxLength - 1);
			truncatedTitle += MORE_TEXT_SYMBOL;
		}

		return truncatedTitle;
	}


	// View lookup cache - to improve performance
	// please read this tutorial for detail - https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView#improving-performance-with-the-viewholder-pattern
	private static class ViewHolder {
		TextView dayOfWeek;
		TextView dayOfMonth;
		TextView title;
		TextView body;
	}
}
