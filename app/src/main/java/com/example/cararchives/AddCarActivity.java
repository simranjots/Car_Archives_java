package com.example.cararchives;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.cararchives.Model.DataItem;
import com.example.cararchives.database.DataSource;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.List;


public class AddCarActivity extends AppCompatActivity {

    private Activity activity;
    private DataSource dbManager;
    private DataItem carDetail;
    private String imagePath = "";
    private boolean forUpdate = false;
    ImageView imageView;
    AppCompatEditText edtName, edtModel, edtYear, edtColor, edtVin, edtPrice;
    AppCompatButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_data);
        activity = this;
        carDetail = new DataItem();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        imageView = findViewById(R.id.imageView);
        edtName = findViewById(R.id.edtName);
        edtModel = findViewById(R.id.edtModel);
        edtYear = findViewById(R.id.edtYear);
        edtColor = findViewById(R.id.edtColor);
        edtVin = findViewById(R.id.edtVin);
        edtPrice = findViewById(R.id.edtPrice);
        btnAdd = findViewById(R.id.btnAdd);

        dbManager = new DataSource(this);
        dbManager.open();

            getIntentValue();
            listeners();


    }

    private void getIntentValue()  {
        if (getIntent().hasExtra("forUpdate")) {
            forUpdate = true;
            if (getIntent().hasExtra("CarDetail")) {
                carDetail = (DataItem) getIntent().getSerializableExtra("CarDetail");
                setUpData();
            }
        }
    }

    private void setUpData()  {
        imagePath = carDetail.getImage();
        imageView.setImageURI(Uri.parse(imagePath));
        edtName.setText(carDetail.getItemName());
        edtModel.setText(carDetail.getCategory());
        edtYear.setText(carDetail.getSortPosition());
        edtColor.setText(carDetail.getCategory());
        edtVin.setText(carDetail.getSortPosition());
        btnAdd.setText(R.string.update);
        edtPrice.setText((int) carDetail.getPrice());
    }

    private void listeners() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(activity)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
          int i = 0;
            @Override
            public void onClick(View v) {
                if (forUpdate) {
                    dbManager.update(carDetail.getItemId(), edtName.getText().toString()," ",
                            edtModel.getText().toString(),i, Double.parseDouble(edtPrice.getText().toString()), imagePath,
                            edtColor.getText().toString(),edtYear.getText().toString(),
                            edtVin.getText().toString());
                    if (i>4){
                        i = 0;
                    }
                    i++;
                    startActivity(new Intent(activity, AdminActivity.class));
                    finishAffinity();

                } else {
                     DataItem item ;  
                    List<DataItem> dataItems =  new ArrayList<>();
                  item = new DataItem(carDetail.getItemId(), edtName.getText().toString()," ",
                            edtModel.getText().toString(), i, Double.parseDouble(edtPrice.getText().toString()), "mclaren.jpg",
                            edtColor.getText().toString(),edtYear.getText().toString(),
                            edtVin.getText().toString());
                    if (i>4){

                        i = 0;
                    }
                    i++;
                    dataItems.add(item);
                    dbManager.seedDatabase(dataItems);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            assert data != null;
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            assert uri != null;
            imagePath = uri.getPath();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "Error Image Picker", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
