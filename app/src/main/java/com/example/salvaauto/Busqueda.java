package com.example.salvaauto;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

public class Busqueda extends AppCompatActivity {
    public static final String CODIGO_FALLA ="1" ;
    private EditText edit_codigo_falla;
    private ArrayAdapter adapter;

    private Button button_buscar;

    public Spinner spinner_marca;
    public Spinner spinner_modelo;
    public Spinner spinner_anio;


    String[] items;
    private boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        setContentView(R.layout.activity_busqueda);
        edit_codigo_falla = findViewById(R.id.edit_codigo_falla);

        spinner_marca = (Spinner) findViewById(R.id.spinner_marca);
        spinner_modelo = (Spinner) findViewById(R.id.spinner_modelo);
        spinner_anio = (Spinner) findViewById(R.id.spinner_anio);

        button_buscar=(Button) findViewById(R.id.button_buscar);
        button_buscar.setOnClickListener(onClickListener);

        //SPINNER PARA MARCA DE CARRO
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
        //SPINNER PARA MODELO DE CARRO
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
        //SPINNER PARA AÑO DE CARRO
        items= getResources().getStringArray(R.array.lista_año);
        ArrayAdapter<String> adapter2= new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_spinner_item,items);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_anio.setAdapter(adapter2);
        spinner_anio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        //FINALIZA CODIGO DE LOS TRES SPINNER (MARCA, MODELO Y AÑO)
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v ==  button_buscar)
                button_Search_OnClick();
        }
    };

    private void button_Search_OnClick(){
        String datos_falla[] =
                edit_codigo_falla.getText().toString().split(":");
        String codigo_falla = datos_falla[0];
        Log.e("CODIGO_FALLA", codigo_falla);
        Intent intent = new Intent(Busqueda.this, ActivityResult.class);
        intent.putExtra(CODIGO_FALLA, codigo_falla);
        startActivity(intent);

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

