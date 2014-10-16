package ca.bcit.cst.team30.diary;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ca.bcit.cst.team30.diary.access.DatabaseHandler;
import ca.bcit.cst.team30.diary.model.Entry;

import java.util.List;


public class TimelineFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_timeline, container, false);

		View.OnClickListener addEntry = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				addEntry(view);
			}
		};
		Button addNew = (Button) rootView.findViewById(R.id.add_entry);
		addNew.setOnClickListener(addEntry);

		View.OnClickListener display = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				display(view);
			}
		};
		Button view = (Button) rootView.findViewById(R.id.display);
		view.setOnClickListener(display);

		return rootView;
	}

	private void addEntry(View v) {
		TextView fDate = (TextView) getView().findViewById(R.id.creationDate);
		TextView fTitle = (TextView) getView().findViewById(R.id.title);
		TextView fContent = (TextView) getView().findViewById(R.id.content);

		String title = fTitle.getText().toString();
		String content = fContent.getText().toString();
		Entry entry = new Entry(title, content);

		// test add new entry
		DatabaseHandler db = new DatabaseHandler(getActivity());
		db.addEntry(entry);
		fDate.setText("Last entry created on: " + entry.getCreationDate());
	}

	private void display(View v) {
		DatabaseHandler db = new DatabaseHandler(getActivity());

		TextView fTitle = (TextView) getView().findViewById(R.id.title);
		TextView fContent = (TextView) getView().findViewById(R.id.content);
		TextView fdump = (TextView) getView().findViewById(R.id.dump);

		// test get 1 entry by id
		Entry entry = db.getEntry(1);
		fTitle.setText(entry.getTitle());
		fContent.setText(entry.getContent());

		// test get all entries
		fdump.setText("");
		List<Entry> all = db.getAllEntries();
		for (Entry e : all) {
			fdump.append("entry id = " + e.getId() + "\n");
			fdump.append("entry title = " + e.getTitle() + "\n");
			fdump.append("entry Content = " + e.getContent() + "\n");
			fdump.append("entry created = " + e.getCreationDate() + "\n");
			fdump.append("test day of week = " + e.getDayOfWeek() + "\n");
			fdump.append("test day of month = " + e.getDayOfMoth() + "\n");
		}

	}
}
