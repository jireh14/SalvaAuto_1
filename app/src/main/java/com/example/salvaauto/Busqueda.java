package com.example.salvaauto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Busqueda extends AppCompatActivity {
    Spinner  spinner_marca;
    Spinner spinner_modelo;
    Spinner spinner_año;
    EditText edit_codigo_falla;
    EditText edit_descripcion;
    EditText edit_causa;
    ImageView iv_imagen;
    String[] items;
    private boolean isFirstTime = true;

    private String webservice_url = "http://salvatuauto.herokuapp" +
            ".com/api_fallas?user_hash=12345&action=get&codigo_falla=";

    private String images_url = "http://salvatuauto.herokuapp" +
            ".com/static/files/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        setContentView(R.layout.activity_busqueda);

        //inicialización de EditText de la vista
        spinner_marca = (Spinner) findViewById(R.id.spinner_marca);
        spinner_modelo = (Spinner) findViewById(R.id.spinner_modelo);
        spinner_año = (Spinner) findViewById(R.id.spinner_año);
        edit_codigo_falla = findViewById(R.id.edit_codigo_falla);
        edit_descripcion = findViewById(R.id.edit_descripcion);
        edit_causa = findViewById(R.id.edit_causa);
        iv_imagen = findViewById(R.id.iv_imagen);

        //Objeto tipo Intent para recuperar el parametro enviado
        Intent intent = getIntent();
        //Se almacena el codigo_falla enviado
        String codigo_falla = intent.getStringExtra(Historial.CODIGO_FALLA);
        //Se concatena la url con el codigo_falla para obtener los datos el cliente
        webservice_url+=codigo_falla;
        webServiceRest(webservice_url);

        items= getResources().getStringArray(R.array.lista_marca);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_marca.setAdapter(adapter);
        spinner_marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isFirstTime){
                    isFirstTime = true;
                }
                else{
                    Toast.makeText(getApplicationContext(),items[position], Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        items= getResources().getStringArray(R.array.lista_modelo);
        ArrayAdapter<String> adapter1= new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item,items);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_modelo.setAdapter(adapter1);
        spinner_modelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isFirstTime){
                    isFirstTime = true;
                }
                else{
                    Toast.makeText(getApplicationContext(),items[position], Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        items= getResources().getStringArray(R.array.lista_año);
        ArrayAdapter<String> adapter2= new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item,items);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_año.setAdapter(adapter2);
        spinner_año.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isFirstTime){
                    isFirstTime = true;
                }
                else{
                    Toast.makeText(getApplicationContext(),items[position], Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void activitySearchOnClick(View view){
        Intent intent = new Intent(Busqueda.this,Resultado.class);
        startActivity(intent);
    }

    private void webServiceRest(String requestURL){
        try{
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String webServiceResult="";
            while ((line = bufferedReader.readLine()) != null){
                webServiceResult += line;
            }
            bufferedReader.close();
            parseInformation(webServiceResult);
        }catch(Exception e){
            Log.e("Error 100",e.getMessage());
        }
    }

    private void parseInformation(String jsonResult){
        JSONArray jsonArray = null;
        String codigo_falla;
        String descripcion;
        String causa;
        String imagen;
        try{
            jsonArray = new JSONArray(jsonResult);
        }catch (JSONException e){
            Log.e("Error 101",e.getMessage());
        }
        for(int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //Se obtiene cada uno de los datos de falla del webservice
                codigo_falla = jsonObject.getString("codigo_falla");
                descripcion = jsonObject.getString("descripcion");
                causa = jsonObject.getString("causa");
                imagen = jsonObject.getString("imagen");

                //Se muestran los datos de falla en su respectivo EditText
                edit_codigo_falla.setText(codigo_falla);
                edit_descripcion.setText(descripcion);
                edit_causa.setText(causa);
                URL newurl = new URL(images_url+imagen);
                Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
                iv_imagen.setImageBitmap(mIcon_val);

            }catch (JSONException e){
                Log.e("Error 102",e.getMessage());
            } catch (MalformedURLException e) {
                Log.e("Error 103",e.getMessage());
            } catch (IOException e) {
                Log.e("Error 104",e.getMessage());
            }
        }
    }
}