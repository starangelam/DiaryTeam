package ca.bcit.cst.team30.diary;


import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import ca.bcit.cst.team30.diary.adapter.SectionsPagerAdapter;


public class Main extends FragmentActivity implements ActionBar.TabListener {

	private static final int REQUEST_CODE_NEW_ENTRY = 145;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;
	private ActionBar actionBar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupTabbedViews();

		// ??? enable or disable?
		actionBar.setHomeButtonEnabled(false);

	}

	private void setupTabbedViews() {
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), this);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		int tabNum = mSectionsPagerAdapter.getCount();
		for (int i = 0; i < tabNum; i++) {
			ActionBar.Tab tab = actionBar.newTab();
			tab.setText(mSectionsPagerAdapter.getPageTitle(i));
			tab.setTabListener(this);
			actionBar.addTab(tab);
		}

		// on swiping the viewpager make respective tab selected
		ViewPager.OnPageChangeListener pageChangeListener =
				new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// on changing the page make respected tab selected
						actionBar.setSelectedNavigationItem(position);
					}

					@Override
					public void onPageScrolled(int i, float v, int i2) {
					}

					@Override
					public void onPageScrollStateChanged(int i) {
					}
				};
		mViewPager.setOnPageChangeListener(pageChangeListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			case R.id.action_settings:
				return true;
			case R.id.action_new_entry:
				final Intent intent = new Intent(this, CreateEntry.class);
				startActivityForResult(intent, REQUEST_CODE_NEW_ENTRY);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_NEW_ENTRY && resultCode == RESULT_OK) {
			final FragmentManager fragmentManager;
			final TimelineFragment timeline;
			final long id;

			Log.d("debug", "add new entry successful.");

			fragmentManager = getFragmentManager();
			id = data.getLongExtra(TimelineFragment.EXTRA_ID, -1);
			timeline = (TimelineFragment) fragmentManager.findFragmentByTag(TimelineFragment.TIMELINE_TAG_NAME);
			timeline.updateList(id);
		}
	}
}

