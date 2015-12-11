package com.uni.rai.softwareengineeringproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {

    private EditText HouseNumber;
    private EditText FlatNumber;
    private EditText Street;
    private EditText City;
    private EditText PostCode;
    private Button submitButton;
    private MapsActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        HouseNumber = (EditText)findViewById(R.id.houseNumberTextField);
        FlatNumber = (EditText)findViewById(R.id.FlatNumberTextField);
        Street = (EditText)findViewById(R.id.StreetTextField);
        City = (EditText)findViewById(R.id.CityTextField);
        PostCode = (EditText)findViewById(R.id.PostcodeTextField);

        activity = new MapsActivity();

        submitButton = (Button)findViewById(R.id.SubmitButton);
        submitButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                String houseNumberText = HouseNumber.getText().toString();
                String FlatNumberText = FlatNumber.getText().toString();
                String StreetText = Street.getText().toString();
                String CityText = City.getText().toString();
                String PostCodeText = PostCode.getText().toString();

                if (CityText.matches("")){
//                    Toast.makeText(getApplicationContext(),"Make sure City/Town is not empty",
//                            Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getApplicationContext(),houseNumberText + "\n" +FlatNumberText + "\n"  + StreetText + "\n" +
//                                    CityText + "\n" + PostCodeText,
//                            Toast.LENGTH_LONG).show();

                    List<List<String>> sales = new ArrayList<List<String>>();
                    try {
                        sales = activity.searchSales(houseNumberText,FlatNumberText,StreetText,CityText,PostCodeText );
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ArrayList<String> salesList = new ArrayList<String>();
                    for(List<String> l : sales) {
                        salesList.add(l.get(0) + "," + l.get(1) + "," + l.get(2));
                        System.out.println("SearchAct, unique ref: " + l.get(0) + ", lat: " + l.get(1) + ", long: " + l.get(2));
                    }
                    Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
//                    intent.putExtra("houseNumberText", houseNumberText);
//                    intent.putExtra("flatNumberText", FlatNumberText);
//                    intent.putExtra("StreetText", StreetText);
//                    intent.putExtra("CityText", CityText);
                    intent.putExtra("salesList", salesList);

                    startActivity(intent);




                }



            }

        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
