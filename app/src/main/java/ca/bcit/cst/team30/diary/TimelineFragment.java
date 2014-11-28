package ca.bcit.cst.team30.diary;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import ca.bcit.cst.team30.diary.access.EntryDataSource;
import ca.bcit.cst.team30.diary.adapter.EntryListAdapter;
import ca.bcit.cst.team30.diary.adapter.SectionsPagerAdapter;
import ca.bcit.cst.team30.diary.model.Entry;

import java.util.Comparator;
import java.util.List;


public class TimelineFragment extends ListFragment {

	public static final String EXTRA_ID = "ca.bcit.cst.team30.diary.TimelineFragment.entry_id";
	public static final String TIMELINE_TAG_NAME = "android:switcher:" + R.id.pager + ':'
			+ SectionsPagerAdapter.TIMELINE_POS;

	private static final int REQUEST_CODE_VIEW_ENTRY = 229;

	public static final int CREATE = 0;
	public static final int UPDATE = 1;
	public static final int DELETE = 2;

	private EntryDataSource dataSource;
	private EntryListAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
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
		final Intent intent = new Intent(getActivity(), ViewEntry.class);
		final Entry entry = adapter.getItem(position);

		intent.putExtra(EXTRA_ID, entry.getId());
		startActivityForResult(intent, REQUEST_CODE_VIEW_ENTRY);
	}

	private void setItemLongClickListener() {
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				boolean consumedLongClick = false;

				dataSource.deleteEntry(id);
				adapter.remove(position);

				Toast.makeText(getActivity(), "Entry deleted", Toast.LENGTH_SHORT).show();

				return consumedLongClick;
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("debug", "result returned.");
		if (requestCode == REQUEST_CODE_VIEW_ENTRY && resultCode == Activity.RESULT_OK) {
			updateList(-1, UPDATE);
		}
	}

	public void updateList(final long id, final int action) {
		switch (action) {
			case CREATE:
				adapter.add(dataSource.getEntry(id));
				break;
			case UPDATE:
				adapter.update(dataSource.getAllEntries());
				break;
		}
	}
}
