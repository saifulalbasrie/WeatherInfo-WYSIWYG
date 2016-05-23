package com.kressx_genesis.saifullah.wysiwyg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kressx_genesis.saifullah.wysiwyg.model.WeatherResult;
import com.kressx_genesis.saifullah.wysiwyg.presenter.IWeatherPresenter;
import com.kressx_genesis.saifullah.wysiwyg.presenter.IWeatherView;
import com.kressx_genesis.saifullah.wysiwyg.presenter.WeatherPresenterImpl;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, IWeatherView {

    public static final int CHANGE_SETTING = 10000;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private IWeatherPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Deal with gradient banding */
        getWindow().setFormat(PixelFormat.RGBA_8888);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        presenter = new WeatherPresenterImpl(this, this);
        presenter.onCreate();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public void showInputCityDialog() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.input_city, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                presenter.getCurrentWeather(userInput.getText().toString());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position))
                .commit();
    }

    public void onSectionAttached(int number) {
        mTitle = getString(R.string.app_name);
        String[] items = getResources().getStringArray(R.array.menu_array);
        if(items!=null && number>=0 && number<items.length)
            mTitle = items[number];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            if(mNavigationDrawerFragment.getCurrentSelectedPosition()==0)
                getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

//    public void setSelectedItemInNavigationDrawer(int pos)
//    {
//        mNavigationDrawerFragment.selectItem(pos);
//        onSectionAttached(pos);
//        restoreActionBar();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_reload) {
            presenter.getCurrentWeather(null);
            return true;
        }
        else if (id == R.id.action_searchCity) {
            showInputCityDialog();
            return true;
        }
        else if (id == R.id.action_settings) {
            if(mNavigationDrawerFragment!=null && mNavigationDrawerFragment.isDrawerOpen())
                mNavigationDrawerFragment.closeDrawer();

            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivityForResult(intent, CHANGE_SETTING);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_SETTING) {
            if (resultCode == RESULT_OK && mNavigationDrawerFragment.getCurrentSelectedPosition()==0) {
                presenter.onSettingChanged();
            }
        }
    }

    @Override
    public void showLoading() {
        //Utility.showBusyIndicator(this, "Please wait");
        View v = (View) findViewById(R.id.progressDialog);
        if(v!=null)
            v.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        //Utility.closeBusyIndicator();
        View v = (View) findViewById(R.id.progressDialog);
        if(v!=null)
            v.setVisibility(View.GONE);
    }

    @Override
    public void onWeatherUpdateSuccess(WeatherResult data) {
        //Update Image weather
        ImageView ivImg = (ImageView) findViewById(R.id.imageWeather);
        Glide.with(MainActivity.this)
                .load(data.getImgUrl())
                .into(ivImg);

        //update View
        TextView v = (TextView) findViewById(R.id.txt_location);
        v.setText(data.getCity() + ", " + data.getCountry());

        TextView vTemp = (TextView) findViewById(R.id.txt_temp);
        vTemp.setText(data.getTemp());

        TextView vStatus = (TextView) findViewById(R.id.txt_status);
        vStatus.setText(data.getStatus());

        TextView vLastUpdate = (TextView) findViewById(R.id.txt_last_update);
        vLastUpdate.setText("Last update: " + data.getLastUpdate());

        TextView v1 = (TextView) findViewById(R.id.txt_humidity);
        v1.setText(data.getHumidity());
        TextView v2 = (TextView) findViewById(R.id.txt_press);
        v2.setText(data.getPressure());
        TextView v3 = (TextView) findViewById(R.id.txt_tempMin);
        v3.setText(data.getTempMin());
        TextView v4 = (TextView) findViewById(R.id.txt_tempMax);
        v4.setText(data.getTempMax());
    }

    @Override
    public void onWeatherUpdateFailed(String msg) {
        Toast t = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
        t.show();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            int pos = 0;
            if(getArguments()!=null)
                pos = getArguments().getInt(ARG_SECTION_NUMBER);

            if(pos==1)
            {
                rootView = inflater.inflate(R.layout.fragment_about, container, false);
            }
            else
            {
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
            }
            return rootView;
        }

        @Override
        public void onAttach(Context activity) {
            super.onAttach(activity);
            int pos = getArguments().getInt(ARG_SECTION_NUMBER);
            ((MainActivity) activity).onSectionAttached(pos);

            if(pos==0)
                ((MainActivity) activity).presenter.getCurrentWeather(null);

        }
    }

}
