package com.example.pst;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CAMERA;

public class Login extends AppCompatActivity {

    private ImageButton btnIngreso;
    private EditText edtUsuario;
    private EditText edtContraseña;
    private String usuario;
    private String contraseña;
    private String nomUsuario;


    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edtUsuario=(EditText)findViewById(R.id.edtUsuario);
        edtContraseña=(EditText)findViewById(R.id.edtContraseña);
        btnIngreso= (ImageButton) findViewById(R.id.btnIngreso);

        if (btnIngreso != null) {
            btnIngreso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validaPermisos()) {
                        usuario = edtUsuario.getText().toString();
                        contraseña = edtContraseña.getText().toString();


                        if (usuario.equals("") && contraseña.equals("")) {
                            Toast.makeText(Login.this, "Usuario o contraseña incorrecta", Toast.LENGTH_LONG).show();
                            return;

                        }

                        try {
                            new AttemptLogin().execute();
                        } catch (Exception e) {
                            Toast.makeText(Login.this, "Error de conexion", Toast.LENGTH_LONG).show();
                        }


                    }


                }
            });
        }

    }



    private class AttemptLogin extends AsyncTask<String, String, String> {

        @Override

        protected String doInBackground(String... args)
        {
            int success;
            try {
                // Building Parameters
                List parametros = new ArrayList();
                //noinspection deprecation
                parametros.add(new BasicNameValuePair("username", usuario));
                //noinspection deprecation
                parametros.add(new BasicNameValuePair("password", contraseña));


                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(getString(R.string.URL), "POST", parametros);


                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1)
                {



                    SharedPreferences preferences =getSharedPreferences
                            ("credenciales", Context.MODE_PRIVATE);

                    SharedPreferences.Editor edit = preferences.edit();

                    edit.putString("username", json.getString("idUsuario"));
                    edit.apply();


                    Intent i = new Intent(Login.this,Inicio.class);
                    finish();
                    startActivity(i);

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
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }



    private boolean validaPermisos() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        if((shouldShowRequestPermissionRationale(CAMERA))){
            cargarDialogoRecomendacion();
        }else{

            requestPermissions(new String[]{CAMERA},100);
        }
        return false;
    }
    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(Login.this);
        dialogo.setTitle("Permisos Activados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{CAMERA},100);
                }
            }
        });
        dialogo.show();

    }


    @Override
    public void onBackPressed() {
    }
}
