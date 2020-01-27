package com.example.pst;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class Qr extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView escanerView;
    Context context;
    String idContenedor="";
    String usuario="";



    private String nivelPlastico="0";
    private String nivelMetal="0";
    private String nivelOrganico="0";

    private TextView tvTacho1;
    private TextView tvTacho2;
    private TextView tvTacho3;


    private ImageView btnGuardar;

    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        tvTacho1=(TextView) findViewById(R.id.tvTacho1);
        tvTacho2=(TextView) findViewById(R.id.tvTacho2);
        tvTacho3=(TextView) findViewById(R.id.tvTacho3);




        EscanerQr();
    }



    public void EscanerQr(){
        escanerView=new ZXingScannerView(this);
        setContentView(escanerView);
        escanerView.setResultHandler(this);
        escanerView.startCamera();
    }


    @Override
    public void handleResult(Result result) {

        try {
            String datos = result.getText();
            escanerView.resumeCameraPreview(this);
            idContenedor=datos;
            escanerView.stopCamera();
            setContentView(R.layout.activity_qr);

            declaraElementos();

        }catch(NullPointerException n){
            System.out.println("ERROR, QR DESCONOCIDO");
        }
    }

    public void declaraElementos(){
        tvTacho1=(TextView) findViewById(R.id.tvTacho1);
        tvTacho2=(TextView) findViewById(R.id.tvTacho2);
        tvTacho3=(TextView) findViewById(R.id.tvTacho3);


        btnGuardar=(ImageView) findViewById(R.id.btnGuardar);

        SharedPreferences preferences= getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        usuario=preferences.getString("username","");

        bucle();


    }
    public void guardar(View view){

        new sendRetiro().execute();
    }

    public void toHome(View view){

        Intent i = new Intent(Qr.this,Inicio.class);
        finish();
        startActivity(i);
    }

    public void toRetiro(View view){

        Intent i = new Intent(Qr.this,Retiro.class);
        finish();
        startActivity(i);
    }
    public void logOut(View view){

        Intent i = new Intent(Qr.this,Login.class);
        finish();
        startActivity(i);
    }

    public void bucle(){


        new Thread(new Runnable() {
            public void run() {
                try {
                    while(true){

                        new getPorcentaje().execute();


                        Thread.sleep(5000);
                    }

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class getPorcentaje extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... args)
        {
            int success;
            try {
                // Building Parameters
                List parametros = new ArrayList();
                //noinspection deprecation
                parametros.add(new BasicNameValuePair("idContenedor", idContenedor));



                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(getString(R.string.URL)+"query/getPorcentajes.php", "POST", parametros);


                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1)
                {

                    nivelPlastico=json.getString("nivelPlastico");
                    nivelMetal=json.getString("nivelMetal");
                    nivelOrganico=json.getString("nivelOrganico");




                    return json.getString(TAG_MESSAGE);

                }
                else
                {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch(RuntimeException r){
                r.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override

        protected void onPostExecute(String file_url)
        {
            tvTacho1.setText(conversion(nivelPlastico));
            tvTacho2.setText(conversion(nivelMetal));
            tvTacho3.setText(conversion(nivelOrganico));

        }
    }

    private class sendRetiro extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... args)
        {
            int success;
            try {

                String nivelTacho1= comparaNivel(tvTacho1.getText().toString());
                String nivelTacho2= comparaNivel(tvTacho2.getText().toString());
                String nivelTacho3= comparaNivel(tvTacho3.getText().toString());
                // Building Parameters
                List parametros = new ArrayList();
                //noinspection deprecation
                parametros.add(new BasicNameValuePair("idContenedor", idContenedor));
                parametros.add(new BasicNameValuePair("idUsuario", usuario));
                parametros.add(new BasicNameValuePair("tPlastico", nivelTacho1));
                parametros.add(new BasicNameValuePair("tMetal",nivelTacho2 ));
                parametros.add(new BasicNameValuePair("tOrganico", nivelTacho3));



                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(getString(R.string.URL)+"query/sendRetiro.php", "POST", parametros);


                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1)
                {

                    return json.getString(TAG_MESSAGE);

                }
                else
                {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch(RuntimeException r){
                r.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override

        protected void onPostExecute(String file_url)
        {

            if (file_url != null) {
                Toast.makeText(Qr.this, file_url, Toast.LENGTH_LONG).show();
            }


        }
    }

    public String conversion(String valor){
        if(valor.equals("0")){
            return "= 0%";
        }else {
            int valInt = Integer.valueOf(valor) * 20;
            return "= " + String.valueOf(valInt) + "%";
        }
    }
    public String comparaNivel(String valor){
        valor = valor.substring(2, valor.length() - 1);
        int valInt= Integer.valueOf(valor);

        if(valInt==100){
            return "0";
        }
        return String.valueOf(valInt);
    }



    @Override
    public void onBackPressed() {


    }
}
