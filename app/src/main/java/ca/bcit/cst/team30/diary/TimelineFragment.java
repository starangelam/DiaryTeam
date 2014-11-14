package ca.bcit.cst.team30.diary;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ca.bcit.cst.team30.diary.access.EntryDataSource;
import ca.bcit.cst.team30.diary.adapter.EntryListAdapter;
import ca.bcit.cst.team30.diary.model.Entry;

import java.util.List;


public class TimelineFragment extends ListFragment {

	private EntryDataSource dataSource;
	private ArrayAdapter<Entry> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_timeline, container, false);

		dataSource = new EntryDataSource(getActivity());
		dataSource.open();

		List<Entry> all = dataSource.getAllEntries();
		adapter = new EntryListAdapter(getActivity(), all);
		setListAdapter(adapter);

		return rootView;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		final Entry entry = adapter.getItem(position);

		dataSource.deleteEntry(entry);
		adapter.remove(entry);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		dataSource.open();
		super.onResume();
	}

	@Override
	public void onPause() {
		dataSource.close();
		super.onPause();
	}

}
