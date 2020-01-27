package com.example.pst;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Retiro extends AppCompatActivity {

    ListView listRetiros;
    ArrayList<DatosListView> dtvRetiros;
    Adaptador adaptador;

    private final String serverRetiros = "query/getRetiros.php";
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retiro);

        listRetiros = (ListView) findViewById(R.id.listRetiros);

        dtvRetiros = new ArrayList<>();
        adaptador = new Adaptador(getApplicationContext(), dtvRetiros);
        listRetiros.setAdapter(adaptador);

        new Thread(new Runnable() {
            public void run() {
                try {
                    while(true){
                        new getRetiros().execute();
                        Thread.sleep(15000);
                    }

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }).start();


    }

    public void toHome(View view){

        Intent i = new Intent(Retiro.this,Inicio.class);
        finish();
        startActivity(i);
    }

    public void toQr(View view){

        Intent i = new Intent(Retiro.this,Qr.class);
        finish();
        startActivity(i);
    }


    public void logOut(View view){

        Intent i = new Intent(Retiro.this,Login.class);
        finish();
        startActivity(i);
    }

    private class getRetiros extends AsyncTask<String, String, String> {

        String id;
        String nombre;
        String fecha;
        String tPlastico;
        String tMetal;
        String tOrganico;

        @Override

        protected String doInBackground(String... args) {
            int success;
            try {


                List parametros = new ArrayList();
                parametros.add(new BasicNameValuePair("idContenedor", "1")); //Mandamos un parametro nulo

                JSONObject jsonEmp = jsonParser.makeHttpRequest(getString(R.string.URL) + serverRetiros, "POST", parametros);


                success = jsonEmp.getInt(TAG_SUCCESS);
                if (success == 1) {

                    JSONObject ids = jsonEmp.getJSONObject("id");
                    JSONObject nombres = jsonEmp.getJSONObject("nombre");
                    JSONObject fechas = jsonEmp.getJSONObject("fecha");
                    JSONObject tPlasticos = jsonEmp.getJSONObject("tPlastico");
                    JSONObject tMetales = jsonEmp.getJSONObject("tMetal");
                    JSONObject tOrganicos = jsonEmp.getJSONObject("tOrganico");


                    dtvRetiros = new ArrayList<>();

                    for (int i = 0; i < ids.length(); i++) {
                        id = ids.getString(String.valueOf(i + 1));
                        nombre = nombres.getString(String.valueOf(i + 1));
                        fecha = fechas.getString(String.valueOf(i + 1));
                        tPlastico = conversion(tPlasticos.getString(String.valueOf(i + 1)));
                        tMetal = conversion(tMetales.getString(String.valueOf(i + 1)));
                        tOrganico = conversion(tOrganicos.getString(String.valueOf(i + 1)));

                        DatosListView retiro = new DatosListView(id, nombre, fecha, "Plástico: " + tPlastico, "Metal: " + tMetal, "Org.: " + tOrganico);

                        dtvRetiros.add(0,retiro);


                    }


                    return jsonEmp.getString(TAG_MESSAGE);

                }
            else {

                    return jsonEmp.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPreExecute() {

        }

        @Override

        protected void onPostExecute(String file_url) { // if(listaEquipos!=null) {
            try {
                adaptador = new Adaptador(getApplicationContext(), dtvRetiros);
                listRetiros.setAdapter(adaptador);
            } catch (NullPointerException n) {
                Toast.makeText(Retiro.this, "No hay datos para mostrar", Toast.LENGTH_SHORT).show();
            }
        }//else Toast.makeText(selEmpres.this,"No hay equipo registrado",Toast.LENGTH_LONG).show();
        //}
    }

    public String conversion(String valor){
        if(valor.equals("0")){
            return "0%";
        }else {
            return "= " + valor+ "%";
        }
    }
    @Override
    public void onBackPressed() {


    }
}


