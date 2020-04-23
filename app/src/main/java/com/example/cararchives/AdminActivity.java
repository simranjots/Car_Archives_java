package com.example.cararchives;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cararchives.Model.DataItem;
import com.example.cararchives.Model.SimpleItemTouchHelperCallback;
import com.example.cararchives.SampleData.SampleDataProvider;
import com.example.cararchives.database.DBHelper;
import com.example.cararchives.database.DataSource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
    List<DataItem> dataItemList;

    boolean ImageType = true;
    DataSource mDataSource;
    List<DataItem> listFromDB;
    RecyclerView mRecyclerView;
    DataItemAdapter mItemAdapter;
    FloatingActionButton fab;
    DataItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("key_name", true);
        editor.commit();
        if (pref.getBoolean("key_name", false)) {
            dataItemList = SampleDataProvider.dataItemList;
        }else {
            dataItemList = new ArrayList<>();
        }
        item =  new DataItem();


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AddCarActivity.class);
                startActivity(intent);
            }
        });


        mDataSource = new DataSource(this);
        mDataSource.open();
        mDataSource.seedDatabase(dataItemList);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        mRecyclerView = findViewById(R.id.rvItems);
        if (grid) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                 removeItem((long)viewHolder.itemView.getTag());
                Toast.makeText(AdminActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerView);

        displayDataItems();
    }

    private void displayDataItems() {
        listFromDB = mDataSource.getAllItems();
        mItemAdapter = new DataItemAdapter(this, listFromDB,ImageType);
        mRecyclerView.setAdapter(mItemAdapter);
    }
 private void removeItem(long id){
    mDataSource.delete(id);
 }
    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataSource.open();
        displayDataItems();
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





