package com.example.crudmysql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class editData extends AppCompatActivity {

    com.rengwuxian.materialedittext.MaterialEditText et_noinduk,et_nama,et_alamat,et_hobi;
    String noinduk,nama,alamat,hobi;
    Button btn_submit;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        et_noinduk          = findViewById(R.id.et_noinduk);
        et_nama             = findViewById(R.id.et_nama);
        et_alamat           = findViewById(R.id.et_alamat);
        et_hobi             = findViewById(R.id.et_hobi);
        btn_submit          = findViewById(R.id.btn_update);

        progressDialog      = new ProgressDialog(this);
        getDataIntent();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Mengupdate data...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                noinduk = et_noinduk.getText().toString();
                nama = et_nama.getText().toString();
                alamat = et_alamat.getText().toString();
                hobi = et_hobi.getText().toString();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        validasiData();

                    }
                },1000);




            }
        });


    }

    void getDataIntent(){

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            et_noinduk.setText(bundle.getString("noinduk"));
            et_nama.setText(bundle.getString("nama"));
            et_alamat.setText(bundle.getString("alamat"));
            et_hobi.setText(bundle.getString("hobi"));


        }else {
            et_noinduk.setText("");
            et_nama.setText("");
            et_alamat.setText("");
            et_hobi.setText("");

        }



    }


    void validasiData(){

        if(noinduk.equals("") || hobi.equals("") || alamat.equals("") || nama.equals("")){
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Periksa kembali data",Toast.LENGTH_SHORT).show();


        }else {

            updateData();

        }




    }

    private void updateData() {
        AndroidNetworking.post("http://192.168.43.38/APIproject/updateSiswa.php")
                .addBodyParameter("noinduk",""+noinduk)
                .addBodyParameter("nama",""+nama)
                .addBodyParameter("alamat",""+alamat)
                .addBodyParameter("hobi",""+hobi)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("status",""+response);
                        try {
                            boolean status = response.getBoolean("status");

                            if(status){

                                new AlertDialog.Builder(editData.this)
                                        .setMessage("data berhasil update")
                                        .setCancelable(false)
                                        .setPositiveButton("kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_OK,i);
                                                Intent intent = new Intent(editData.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }).show();


                            } else {
                               progressDialog.dismiss();
                                new AlertDialog.Builder(editData.this)
                                        .setMessage("Gagal update Data")
                                        .setCancelable(false)
                                        .setPositiveButton("kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED,i);
                                                editData.this.finish();

                                            }
                                        }).show();

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
        getMenuInflater().inflate(R.menu.menuback,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_back){

            this.finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
