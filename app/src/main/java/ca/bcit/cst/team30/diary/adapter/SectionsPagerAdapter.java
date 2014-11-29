package ca.bcit.cst.team30.diary.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import ca.bcit.cst.team30.diary.AndroidfyFragment;
import ca.bcit.cst.team30.diary.R;
import ca.bcit.cst.team30.diary.TimelineFragment;

import java.util.Locale;

/**
 * A {@link android.support.v13.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	public static final int TIMELINE_POS = 0;

	private final int PAGE_COUNT = 2;
	private Context context;

	public SectionsPagerAdapter(FragmentManager fm, Context nContext) {
		super(fm);
		context = nContext;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class below).
		switch (position) {
			case TIMELINE_POS:
				return new TimelineFragment();
			case 1:
				return new AndroidfyFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
			case 0:
				return context.getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return context.getString(R.string.title_section2).toUpperCase(l);
		}
		return null;
	}
}