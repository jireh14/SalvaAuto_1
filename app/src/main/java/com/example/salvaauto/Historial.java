package com.example.salvaauto;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Historial extends AppCompatActivity {
    private ListView mi_lista;
    private ArrayAdapter adapter;

    private  String url= "https://salvatuauto.herokuapp" +
        ".com/api_fallas?user_hash=dc243fdf1a24cbced74db81708b30788&action=get&codigo_falla=";

    public static final String CODIGO_FALLA= "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        setContentView(R.layout.activity_historial);
        mi_lista = findViewById(R.id.mi_lista);
        adapter = new ArrayAdapter(this, R.layout.codigo_falla_item);
        mi_lista.setAdapter(adapter);

        mi_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ITEM", mi_lista.getItemAtPosition(position).toString());
                String datos_codigo_falla[] =
                        mi_lista.getItemAtPosition(position).toString().split(":");
                String codigo_falla = datos_codigo_falla[0];
                Log.e("CODIGO_FALLA",codigo_falla);
                Intent i = new Intent(Historial.this, ActivityResult.class);
                i.putExtra(CODIGO_FALLA,codigo_falla);
                startActivity(i);
            }
        });
    }
    public void activityInsertOnClick(View view){
        Intent i = new Intent(Historial.this,Busqueda.class);
        startActivity(i);
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
                codigo_falla = jsonObject.getString("codigo_falla");
                descripcion = jsonObject.getString("descripcion");
                causa = jsonObject.getString("causa");
                imagen = jsonObject.getString("imagen");
                adapter.add(codigo_falla + ":" + descripcion + " " + causa + " " + imagen);
            }catch (JSONException e){
                Log.e("Error 102",e.getMessage());
            }
        }
    }
}


