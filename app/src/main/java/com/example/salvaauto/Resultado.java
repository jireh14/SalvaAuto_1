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


public class Resultado extends AppCompatActivity {
    EditText edit_falla;
    EditText edit_descripcion;
    EditText edit_causas;
    ImageView iv_imagen;

    private String webservice_url = "https://salvatuauto.herokuapp" +
            ".com/api_falla?user_hash=12345&action=get&id_falla=";


    private String images_url = "http://salvatuauto.herokuapp" +
            ".com/static/files/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        setContentView(R.layout.activity_resultado);
        //inicialización de EditText de la vista
        edit_falla = findViewById(R.id.edit_falla);
        edit_descripcion = findViewById(R.id.edit_descripcion);
        edit_causas = findViewById(R.id.edit_causas);
        iv_imagen = findViewById(R.id.iv_imagen);
        //Objeto tipo Intent para recuperar el parametro enviado
        Intent intent = getIntent();
        //Se almacena el id_descripcion enviado
        String id_falla = intent.getStringExtra(Importantes.ID_FALLA);
        //Se concatena la url con el descripcion para obtener los datos
        webservice_url+=id_falla;
        webServiceRest(webservice_url);
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
    public void activityInsertOnClick(View view) {
        Intent i = new Intent(Resultado.this, Importantes.class);
        startActivity(i);
    }
    private void parseInformation(String jsonResult){
        JSONArray jsonArray = null;
        String id_falla;
        String descripcion;
        String causas;
        String imagen;
        try{
            jsonArray = new JSONArray(jsonResult);
        }catch (JSONException e){
            Log.e("Error 101",e.getMessage());
        }
        for(int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //Se obtiene cada uno de los datos falla del webservice
                id_falla= jsonObject.getString("id_falla");
                descripcion = jsonObject.getString("descripcion");
                causas = jsonObject.getString("causas");
                imagen = jsonObject.getString("imagen");

                //Se muestran los datos de la falla en su respectivo EditText
                edit_falla.setText(id_falla);
                edit_descripcion.setText(descripcion);
                edit_causas.setText(causas);
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