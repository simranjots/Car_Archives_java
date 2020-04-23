package com.example.cararchives;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cararchives.Model.DataItem;
import com.example.cararchives.SampleData.SampleDataProvider;
import com.example.cararchives.database.DBHelper;
import com.example.cararchives.database.DataSource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
    private static final String TAG = "MainActivity";
    List<DataItem> dataItemList = SampleDataProvider.dataItemList;
    private SQLiteDatabase mDatabase;

    boolean ImageType = true;
    DataSource mDataSource;
    List<DataItem> listFromDB;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    String[] mCategories;
    RecyclerView mRecyclerView;
    DataItemAdapter mItemAdapter;
    FloatingActionButton fab;
    DataItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        item = new DataItem();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

//                Intent intent = new Intent(getApplicationContext(), AddCarActivity.class);
//                intent.putExtra("fromAdmin", true);
//                intent.putExtra("CarDetail", item.getItemId());
//                startActivity(intent);
            }
        });

//      Code to manage sliding navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mCategories = getResources().getStringArray(R.array.categories);
        mDrawerList = findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mCategories));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = mCategories[position];
                Toast.makeText(AdminActivity.this, "You chose " + category,
                        Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
//      end of navigation drawer

        mDataSource = new DataSource(this);
        mDataSource.open();
        mDataSource.seedDatabase(dataItemList);


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        mRecyclerView = findViewById(R.id.rvItems);
        if (grid) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        displayDataItems();
    }

    private void displayDataItems() {
        listFromDB.clear();
        listFromDB = mDataSource.getAllItems();
        ImageType = getIntent().getBooleanExtra("IMAGETYPE",true);
        mItemAdapter = new DataItemAdapter(this, listFromDB,ImageType);
        mRecyclerView.setAdapter(mItemAdapter);
    }

    private void showDialog()
    {
        Dialog d=new Dialog(this);

        //NO TITLE
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //layout of dialog
        d.setContentView(R.layout.custom_layout);

        nameTxt= (EditText) d.findViewById(R.id.nameEditTxt);
        posTxt= (EditText) d.findViewById(R.id.posEditTxt);
        Button savebtn= (Button) d.findViewById(R.id.saveBtn);
        Button retrieveBtn= (Button) d.findViewById(R.id.retrieveBtn);

        //ONCLICK LISTENERS
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(nameTxt.getText().toString(),posTxt.getText().toString());
            }
        });

        retrieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieve();
            }
        });

        //SHOW DIALOG
        d.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listFromDB.clear();
        mDataSource.open();
        mItemAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mItemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signin:
                Intent intent = new Intent(this, LoginDetailsActivity.class);
                startActivityForResult(intent, SIGNIN_REQUEST);
                return true;
            case R.id.action_settings:
                // Show the settings screen
                Intent settingsIntent = new Intent(this, PrefsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_all_items:
                // display all items
                displayDataItems();
                return true;
            case R.id.action_choose_category:
                //open the drawer
                mDrawerLayout.openDrawer(mDrawerList);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SIGNIN_REQUEST) {
            String email = data.getStringExtra(LoginDetailsActivity.EMAIL_KEY);
            Toast.makeText(this, "You signed in as " + email, Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor =
                    getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE).edit();
            editor.putString(LoginDetailsActivity.EMAIL_KEY, email);
            editor.apply();

        }

    }
}


