package university.gardencity.gcu.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import university.gardencity.gcu.home.AboutFragment;
import university.gardencity.gcu.home.EventsFragment;
import university.gardencity.gcu.home.HomeActivity;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private HomeActivity ctx;

    public ViewPagerAdapter(FragmentManager fm, HomeActivity ctx) {
        super(fm);
        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EventsFragment.newInstance("", "");
            case 1:
                return AboutFragment.newInstance("", "");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Events & News";
            case 1:
                return "About Us";
            default:
                return "error";
        }
    }
}
