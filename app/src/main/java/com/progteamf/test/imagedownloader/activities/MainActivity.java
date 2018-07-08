package com.progteamf.test.imagedownloader.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.progteamf.test.imagedownloader.R;
import com.progteamf.test.imagedownloader.adapters.HistoryAdapter;
import com.progteamf.test.imagedownloader.controllers.SortingImageController;
import com.progteamf.test.imagedownloader.db.ImageDAO;
import com.progteamf.test.imagedownloader.db.ImageHelper;
import com.progteamf.test.imagedownloader.db.model.ImageRealmObject;
import com.progteamf.test.imagedownloader.exceptions.SortException;
import com.progteamf.test.imagedownloader.model.Image;
import com.progteamf.test.imagedownloader.model.SortType;
import com.progteamf.test.imagedownloader.model.Status;
import com.progteamf.test.imagedownloader.utils.ItemDivider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by N.Babiy on 7/5/2018.
 */

public class MainActivity extends AppCompatActivity {

    static private boolean REALM_ISNT_INIT = true;
    static private final String MAIN_TAG = "MainActivity_log";
    static private Realm defaultRealm;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MAIN", "onCreate");

        while (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            getPermissionForExternalStorage();
        }
        if (REALM_ISNT_INIT) {
            try {
                initRealm();
                defaultRealm = Realm.getDefaultInstance();
            } catch (IOException | IllegalArgumentException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }


    private void initRealm() throws IOException {
        REALM_ISNT_INIT = false;
        Log.e(MAIN_TAG, "realm initialization.");
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .directory(new File(Environment.getExternalStorageDirectory() + "/BIGDIG/test/realm"))
                .name("progTeamF.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        defaultRealm.where(ImageRealmObject.class)
                .findAllAsync()
                .addChangeListener(new RealmChangeListener() {
                    @Override
                    public void onChange(Object o) {
                        PlaceholderFragment.images = new ImageDAO().readAll();
                        PlaceholderFragment.mAdapter = new HistoryAdapter(getApplicationContext(), PlaceholderFragment.images);
                        PlaceholderFragment.mRecyclerView.setAdapter(PlaceholderFragment.mAdapter);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        defaultRealm.close();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String LINK_TAG = "link_tag";
        private static final String APP_A_URL_TAG = "app_a_url_tag";

        /**
         * attributes for displaying tab of history
         */
        static private RecyclerView mRecyclerView;
        static private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        private LinearLayoutManager linearLayoutManager;
        static private List<Image> images = new ArrayList<>();

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_main, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            switch (id) {
                case R.id.action_sort_by_date:
                    try {
                        images = SortingImageController.sort(images, SortType.SORT_BY_DATE);
                    } catch (SortException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                    }
                    break;
                case R.id.action_sort_by_status:
                    try {
                        images = SortingImageController.sort(images, SortType.SORT_BY_STATUS);
                    } catch (SortException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                    }
                    break;
            }

            mAdapter = new HistoryAdapter(getContext(), images);
            mRecyclerView.setAdapter(mAdapter);

            return super.onOptionsItemSelected(item);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 0) {

                View view = inflater.inflate(R.layout.fragment_test, null);

                setHasOptionsMenu(false);

                final EditText linkEdit = view.findViewById(R.id.link);
                Button ok = view.findViewById(R.id.ok_link);
                final Intent openAppB = getContext().getPackageManager()
                        .getLaunchIntentForPackage("com.progteamf.test.imageviewer");
                ok.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        String link = linkEdit.getText().toString().trim();
                        if (openAppB != null & link.length() > 4) {
                            openAppB.addCategory(APP_A_URL_TAG);
                            openAppB.putExtra(LINK_TAG, link);
                            startActivity(openAppB);
                        } else {
                            Toast.makeText(getContext(), "NULL", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                return view;


            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {


                View rootView = inflater.inflate(R.layout.fragment_history, container, false);
                setHasOptionsMenu(true);

                images = new ImageDAO().readAll();

                /** There is initialization of recyclerView which displays links*/
                mRecyclerView = (RecyclerView) rootView.findViewById(R.id.history_recycler);

                // use this setting to improve performance because changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // add divider between lines
                mRecyclerView.addItemDecoration(new ItemDivider(getActivity().getApplicationContext()));

                // use a linear layout manager
                linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                mLayoutManager = linearLayoutManager;
                mRecyclerView.setLayoutManager(mLayoutManager);

                // specify an adapter
                mAdapter = new HistoryAdapter(getContext(), images);

                //setting adapter
                mRecyclerView.setAdapter(mAdapter);

                defaultRealm.where(ImageRealmObject.class)
                        .findAllAsync()
                        .addChangeListener(new RealmChangeListener() {
                            @Override
                            public void onChange(Object o) {
                                PlaceholderFragment.images = new ImageDAO().readAll();
                                PlaceholderFragment.mAdapter = new HistoryAdapter(getActivity().getApplicationContext(), PlaceholderFragment.images);
                                PlaceholderFragment.mRecyclerView.setAdapter(PlaceholderFragment.mAdapter);
                            }
                        });

                return rootView;
            } else return null;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "TEST";
                case 1:
                    return "HISTORY";
            }
            return null;
        }

    }


    private void getPermissionForExternalStorage() {
        do {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


                // You can show your dialog message here but instead I am
                // showing the grant permission dialog box
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        10);


            } else {

                //Requesting permission
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        10);

            }
        }
        while (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED);
    }


}
