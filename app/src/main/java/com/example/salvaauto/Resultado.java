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
import android.widget.Button;

public class Resultado extends AppCompatActivity {
    EditText edit_codigo_falla;
    EditText edit_descripcion;
    EditText edit_causa;
    ImageView iv_imagen;
    Button button_añadir;

    private String webservice_url = "http://salvatuauto.herokuapp" +
            ".com/api_fallas?user_hash=12345&action=get&codigo_falla=";


    private String images_url = "http://salvatuauto.herokuapp" +
            ".com/static/files/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        setContentView(R.layout.activity_resultado);

        //inicialización de EditText de la vista
        edit_codigo_falla = findViewById(R.id.edit_codigo_falla);
        edit_descripcion = findViewById(R.id.edit_descripcion);
        edit_causa = findViewById(R.id.edit_causa);
        iv_imagen = findViewById(R.id.iv_imagen);
        //Objeto tipo Intent para recuperar el parametro enviado
        Intent intent = getIntent();
        //Se almacena el id_descripcion enviado
        String codigo_falla = intent.getStringExtra(Historial.CODIGO_FALLA);
        //Se concatena la url con el descripcion para obtener los datos
        webservice_url+=codigo_falla;
        webServiceRest(webservice_url);
    }
    public void onClick(View view){
        Intent intent= new Intent(Resultado.this, Busqueda.class);
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
    public void activityInsertOnClick(View view) {
        Intent i = new Intent(Resultado.this, Importantes.class);
        startActivity(i);
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
                //Se obtiene cada uno de los datos falla del webservice
                codigo_falla= jsonObject.getString("codigo_falla");
                descripcion = jsonObject.getString("descripcion");
                causa = jsonObject.getString("causa");
                imagen = jsonObject.getString("imagen");

                //Se muestran los datos de la falla en su respectivo EditText
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