package gabema.lfnwsampleapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Hosts three fragments - SlideShowFragment, SourceCodeFragment, CommandLineFragment
 * See <a href="http://developer.android.com/reference/android/support/v4/app/FragmentPagerAdapter.html">FragmentPagerAdapter</a> in the Android SDK.
 * @author Gabe Martin
 */
public final class MainPagerAdapter extends FragmentStatePagerAdapter {

	public MainPagerAdapter(android.support.v4.app.FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		if (index < 0 || index >= FRAGMENT_COUNT)
			throw new IndexOutOfBoundsException("index must be between 0 and " + FRAGMENT_COUNT + " was " + index);

		Fragment fragment;
		switch(index)
		{
		case COMMAND_LINE_FRAGMENT_INDEX:
			fragment = new CommandLineFragment();
			break;
		case SLIDE_SHOW_FRAGMENT_INDEX:
			fragment = new SlideShowFragment();
			break;
		case SOURCE_CODE_FRAGMENT_INDEX:
			fragment = new SourceCodeFragment();
			break;
		default:
			throw new IllegalStateException("No fragment for index " + index);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return FRAGMENT_COUNT;
	}

	private static final int FRAGMENT_COUNT = 3;
	private static final int COMMAND_LINE_FRAGMENT_INDEX = 1;
	private static final int SLIDE_SHOW_FRAGMENT_INDEX = 0;
	private static final int SOURCE_CODE_FRAGMENT_INDEX = 2;
}
