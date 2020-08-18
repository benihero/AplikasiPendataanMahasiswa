package com.example.crudmysql;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterMahasiswa extends RecyclerView.Adapter<AdapterMahasiswa.myHolder> {

    private Context context;
    private ArrayList<String> arrayList_noinduk,arrayList_nama,arrayList_hobi,arrayList_alamat;
    ProgressDialog progressDialog;

    public AdapterMahasiswa(Context context, ArrayList<String> arrayList_noinduk, ArrayList<String> arrayList_nama, ArrayList<String> arrayList_hobi, ArrayList<String> arrayList_alamat) {
        this.context = context;
        this.arrayList_noinduk = arrayList_noinduk;
        this.arrayList_nama = arrayList_nama;
        this.arrayList_hobi = arrayList_hobi;
        this.arrayList_alamat = arrayList_alamat;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.listdata,parent,false);
        return new AdapterMahasiswa.myHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, final int position) {

        holder.tv_noind.setText(arrayList_noinduk.get(position));
        holder.tv_nama.setText(arrayList_nama.get(position));
        holder.tv_alamat.setText(arrayList_alamat.get(position));
        holder.tv_hobi.setText(arrayList_hobi.get(position));
        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,editData.class);
                i.putExtra("noinduk",arrayList_noinduk.get(position));
                i.putExtra("nama",arrayList_nama.get(position));
                i.putExtra("alamat",arrayList_alamat.get(position));
                i.putExtra("hobi",arrayList_hobi.get(position));
              ((MainActivity)context).startActivityForResult(i,2);
            }
        });


       holder.cv_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder((MainActivity)context)
                        .setMessage("Ingin menghapus nomor induk "+arrayList_noinduk.get(position)+" ?")
                        .setCancelable(false)
                        .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("menghapus..");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                AndroidNetworking.post("http://192.168.43.38/APIproject/deleteSiswa.php")
                                        .addBodyParameter("noinduk",""+arrayList_noinduk.get(position))
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                progressDialog.dismiss();

                                                try {
                                                    Boolean status = response.getBoolean("status");
                                                   Log.d("statuss"," "+status);
                                                    String result = response.getString("result");
                                                   if(status){

                                                        if (context instanceof MainActivity){
                                                            ((MainActivity)context).scrollRefresh();


                                                        }

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
                        }
                        ).show();







                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList_noinduk.size();
    }


    public class myHolder extends RecyclerView.ViewHolder{
        public TextView tv_noind,tv_nama,tv_alamat,tv_hobi;
        public CardView cv_main;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            cv_main = itemView.findViewById(R.id.cv_main);
            tv_noind = itemView.findViewById(R.id.tv_noind);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_alamat = itemView.findViewById(R.id.tv_alamat);
            tv_hobi = itemView.findViewById(R.id.tv_hobi);

            progressDialog = new ProgressDialog(context);
        }
    }


}
