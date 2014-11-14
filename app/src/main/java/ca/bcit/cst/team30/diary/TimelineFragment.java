package ca.bcit.cst.team30.diary;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.bcit.cst.team30.diary.access.EntryDataSource;
import ca.bcit.cst.team30.diary.adapter.EntryListAdapter;
import ca.bcit.cst.team30.diary.model.Entry;


public class TimelineFragment extends ListFragment {

	private EntryDataSource datasource;
	private ArrayAdapter<Entry> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_timeline, container, false);

		datasource = new EntryDataSource(getActivity());
		datasource.open();

		List<Entry> all = datasource.getAllEntries();
		adapter = new EntryListAdapter(getActivity(), all.toArray(new Entry[]{}));
		setListAdapter(adapter);

		return rootView;
	}


	@Override
	public void onResume()
	{
		datasource.open();
		super.onResume();
	}

	@Override
	public void onPause()
	{
		datasource.close();
		super.onPause();
	}

}
