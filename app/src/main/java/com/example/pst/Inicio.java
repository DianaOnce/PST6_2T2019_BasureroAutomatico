package com.example.pst;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Inicio extends AppCompatActivity {

    private String idContenedor="1";
    private String nivelPlastico="0";
    private String nivelMetal="0";
    private String nivelOrganico="0";

    private TextView tvTacho1;
    private TextView tvTacho2;
    private TextView tvTacho3;


    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    NotificationCompat.Builder notificacion;
    private static final int idUnica = 51623;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        tvTacho1=(TextView) findViewById(R.id.tvTacho1);
        tvTacho2=(TextView) findViewById(R.id.tvTacho2);
        tvTacho3=(TextView) findViewById(R.id.tvTacho3);
        notificacion = new NotificationCompat.Builder(this);
        notificacion.setAutoCancel(true);

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

    public void notificacion(){


        notificacion.setSmallIcon(R.mipmap.ic_launcher);
        notificacion.setTicker("Tacho lleno");
        notificacion.setPriority(Notification.PRIORITY_HIGH);
        notificacion.setWhen(System.currentTimeMillis());
        notificacion.setContentTitle("Existe un tacho lleno");
        notificacion.setContentText("Hay un tacho por retirar");

        Intent intent = new Intent(Inicio.this,Inicio.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(Inicio.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificacion.setContentIntent(pendingIntent);


        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(idUnica,notificacion.build());


    }
    public void toQr(View view){

        Intent i = new Intent(Inicio.this,Qr.class);
        finish();
        startActivity(i);
    }

    public void toRetiro(View view){

        Intent i = new Intent(Inicio.this,Retiro.class);
        finish();
        startActivity(i);
    }

    public void logOut(View view){

        Intent i = new Intent(Inicio.this,Login.class);
        finish();
        startActivity(i);
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

        protected void onPostExecute(String file_url) {


                    tvTacho1.setText(conversion(nivelPlastico));
                    tvTacho2.setText(conversion(nivelMetal));
                    tvTacho3.setText(conversion(nivelOrganico));


        }
    }

    public String conversion(String valor){
        if(valor.equals("0")){
            return "0%";
        }else {
            int valInt = Integer.valueOf(valor) * 20;
            if (valInt==100){
                notificacion();
            }
            return "= " + String.valueOf(valInt) + "%";
        }
    }


    @Override
    public void onBackPressed() {
    }
}
