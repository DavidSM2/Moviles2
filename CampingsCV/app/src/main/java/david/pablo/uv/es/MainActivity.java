package david.pablo.uv.es;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{
    RecyclerView recyclerView;
    EditText editTextBusqueda;
    ArrayList<Camping> campings;
    ArrayList<Camping> campings_filter;
    CampingsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_campings);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editTextBusqueda = findViewById(R.id.textoBusqueda);
        campings = new ArrayList<Camping>();
        getData();

        editTextBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textoBusqueda = charSequence.toString().toLowerCase();

                if (textoBusqueda == ""){
                    setupData();
                }

                else {
                    campings_filter = new ArrayList<>();
                    for (Camping camping : campings) {
                        if (camping.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                            campings_filter.add(camping);

                        }
                    }

                    setupDataFiltered();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });




    }

    public void getData() {
        InputStream is = getApplicationContext().getResources().openRawResource(R.raw.datastore_search);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //The String writer.toString() must be parsed in the campings ArrayList by using JSONArray and JSONObject
        try {
            JSONObject object = new JSONObject(writer.toString());
            JSONArray JSONCampings = object.getJSONObject("result").getJSONArray("records");
            for (int i = 0; i < JSONCampings.length(); i++) {
                JSONObject JSONCamping = JSONCampings.getJSONObject(i);
                String nombre = JSONCamping.getString("Nombre");
                String categoria = JSONCamping.getString("Categoria");
                String municipio = JSONCamping.getString("Municipio");
                String provincia = JSONCamping.getString("Provincia");
                String correo = JSONCamping.getString("Email");
                String web = JSONCamping.getString("Web");
                String periodo = JSONCamping.getString("Periodo");
                int plazas = JSONCamping.getInt("Plazas");
                String direccion = JSONCamping.getString("Direccion");
                int cp = JSONCamping.getInt("CP");

                System.out.println(correo);
                Camping camping = new Camping(nombre, categoria, provincia, municipio,correo,web,periodo,plazas,direccion,cp);
                campings.add(camping);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //TODO: read the data of each camping, create a new Camping object and insert it in the campings arraylist.
        setupData();
    }

    private void setupData() {
        adapter = new CampingsAdapter(campings, getApplicationContext(),this);
        recyclerView.setAdapter(adapter);
    }

    private void setupDataFiltered() {
        adapter = new CampingsAdapter(campings_filter, getApplicationContext(),this);
        recyclerView.setAdapter(adapter);
    }

    public void Ordernar_Descendente(View view) {
        campings_filter = new ArrayList<>();

        Collections.sort(campings, Camping.comparadorNombreDescendente);

        setupData();
    }

    public void Ordernar_Ascendente(View view) {
        campings_filter = new ArrayList<>();

        Collections.sort(campings, Camping.comparadorNombreAscendente);

        setupData();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Camping camping = campings.get(position);

        intent.putExtra("camping",camping);

        startActivity(intent);
    }


}