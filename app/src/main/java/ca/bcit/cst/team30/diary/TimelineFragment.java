package ca.bcit.cst.team30.diary;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.bcit.cst.team30.diary.access.EntryDataSource;
import ca.bcit.cst.team30.diary.model.Entry;


public class TimelineFragment extends Fragment {

	private EntryDataSource datasource;

	private TextView dump; // TODO created for testing database, delete when not needed.

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		datasource = new EntryDataSource(getActivity());
		datasource.open();

		View rootView = inflater.inflate(R.layout.activity_timeline, container, false);

		dump = (TextView) rootView.findViewById(R.id.dump);
		display();
		return rootView;
	}


	@Override
	public void onResume()
	{
		datasource.open();
		display();
		super.onResume();
	}

	@Override
	public void onPause()
	{
		datasource.close();
		super.onPause();
	}

	private void display() {

		List<Entry> all = datasource.getAllEntries();
		for (Entry e : all) {
			dump.append(e.toString());
			dump.append("===========\n");
		}
	}

}
