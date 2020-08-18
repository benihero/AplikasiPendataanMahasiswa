package com.example.crudmysql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipe;
    RecyclerView rv_main;
    ArrayList<String> arrayList_nama, arrayList_hobi, arrayList_alamat, arrayList_noinduk;
    ProgressDialog progressDialog;
    AdapterMahasiswa adapterMahasiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipe = findViewById(R.id.swipeup);
        rv_main = findViewById(R.id.recyecler);
        progressDialog = new ProgressDialog(this);
        rv_main.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_main.setLayoutManager(layoutManager);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollRefresh();
                swipe.setRefreshing(false);

            }
        });

   scrollRefresh();

    }

    public void scrollRefresh(){
        progressDialog.setMessage("Mengambil Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();

            }
        },1200);

    }

    void initializeArray(){

        arrayList_alamat = new ArrayList<>();
        arrayList_hobi = new ArrayList<>();
        arrayList_nama = new ArrayList<>();
        arrayList_noinduk = new ArrayList<>();
        arrayList_noinduk.clear();
        arrayList_nama.clear();
        arrayList_alamat.clear();
        arrayList_hobi.clear();


    }

    public void getData(){

        initializeArray();
        AndroidNetworking.get("http://192.168.43.38/APIproject/getData.php")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {
                            Boolean status = response.getBoolean("status");
                            if(status){

                                JSONArray jsonArray = response.getJSONArray("result");
                                Log.d("respon",""+jsonArray);

                                for (int i = 0; i< jsonArray.length(); i++){

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    arrayList_alamat.add(jsonObject.getString("alamat"));
                                    arrayList_hobi.add(jsonObject.getString("hobi"));
                                    arrayList_nama.add(jsonObject.getString("nama"));
                                    arrayList_noinduk.add(jsonObject.getString("noinduk"));

                                }
                                adapterMahasiswa = new AdapterMahasiswa(MainActivity.this,arrayList_noinduk,arrayList_nama,arrayList_hobi,arrayList_alamat);
                                rv_main.setAdapter(adapterMahasiswa);

                            }else {

                                Toast.makeText(getApplicationContext(),"Data gagal dimuat",Toast.LENGTH_SHORT).show();
                                adapterMahasiswa = new AdapterMahasiswa(MainActivity.this,arrayList_noinduk,arrayList_nama,arrayList_hobi,arrayList_alamat);
                                rv_main.setAdapter(adapterMahasiswa);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menutambah,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.menu_add){

            Intent intent = new Intent(MainActivity.this,addData.class);
            startActivityForResult(intent,1);
        }

        return super.onOptionsItemSelected(item);
    }
}


