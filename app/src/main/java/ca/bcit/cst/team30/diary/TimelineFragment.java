package ca.bcit.cst.team30.diary;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import ca.bcit.cst.team30.diary.access.EntryDataSource;
import ca.bcit.cst.team30.diary.adapter.EntryListAdapter;
import ca.bcit.cst.team30.diary.model.Entry;

import java.util.List;


public class TimelineFragment extends ListFragment {

	private EntryDataSource dataSource;
	private ArrayAdapter<Entry> adapter;

	@Override
	public  void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		dataSource = new EntryDataSource(getActivity());
		dataSource.open();

		List<Entry> all = dataSource.getAllEntries();
		adapter = new EntryListAdapter(getActivity(), all);
		setListAdapter(adapter);

		setItemLongClickListener();
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
	}

	private void setItemLongClickListener() {
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
				boolean consumedLongClick = false;

				final Entry entry = adapter.getItem(position);
				dataSource.deleteEntry(entry);
				adapter.remove(entry);
				adapter.notifyDataSetChanged();

				Toast.makeText(getActivity(), "Entry deleted", Toast.LENGTH_SHORT).show();

				return consumedLongClick;
			}
		});
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
