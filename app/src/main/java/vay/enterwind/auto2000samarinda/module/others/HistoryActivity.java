package vay.enterwind.auto2000samarinda.module.others;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.module.others.fragments.BPFragment;
import vay.enterwind.auto2000samarinda.module.others.fragments.GRFragment;
import vay.enterwind.auto2000samarinda.module.others.fragments.SalesFragment;

public class HistoryActivity extends BaseActivity {
    private static final String TAG = "HistoryActivity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = HistoryActivity.this;

    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_history);
        ButterKnife.bind(this);

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SalesFragment(), "Sales");
        adapter.addFragment(new GRFragment(), "General Repair");
        adapter.addFragment(new BPFragment(), "Body Painting");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 6) {
                return null;
            }
            return mFragmentTitleList.get(position);
        }
    }
}
