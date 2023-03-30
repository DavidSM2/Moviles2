package david.pablo.uv.es;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{
    RecyclerView recyclerView;
    EditText editTextBusqueda;
    ArrayList<Camping> campings;
    ArrayList<Camping> campings_filter;
    Spinner spinner;
    CampingsAdapter adapter;
    FloatingActionButton fav;
    HTTPConnector httpConnector;

    String[] opciones_spinner = {"Ordenar por...", "Nombre ascendente", "Nombre descendente", "Categoria"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_campings);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editTextBusqueda = findViewById(R.id.textoBusqueda);
        fav = findViewById(R.id.fav);
        spinner = findViewById(R.id.camping_spinner);
        campings = new ArrayList<Camping>();
        httpConnector = new HTTPConnector();
        httpConnector.execute();
        editTextBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textoBusqueda = charSequence.toString().toLowerCase();

                if ("".equals(textoBusqueda)){
                    setupData(campings);
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
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> adapter_string = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones_spinner);
        adapter_string.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter_string);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()){
                    case "Nombre ascendente":
                        Collections.sort(campings, Camping.comparadorNombreAscendente);
                        break;
                    case "Nombre descendente":
                        Collections.sort(campings, Camping.comparadorNombreDescendente);
                        break;
                    case "Categoria":
                        Collections.sort(campings, Camping.comparadorCategoria);
                        break;
                    default:
                        httpConnector = new HTTPConnector();
                        httpConnector.execute();
                        break;
                }

                setupData(campings);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class HTTPConnector extends AsyncTask<String, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(String... params) {
            ArrayList campings=new ArrayList<Camping>();
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                String id = getLastId();
                String url = "https://dadesobertes.gva.es/api/3/action/datastore_search?id=2ddaf823-5da4-4459-aa57-5bfe9f9eb474";
                if(id != "")
                    url = "https://dadesobertes.gva.es/api/3/action/datastore_search?id=" + id;

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                //add request header
                con.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                con.setRequestProperty("accept", "application/json;");
                con.setRequestProperty("accept-language", "es");
                con.connect();
                int responseCode = con.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                int n;
                while ((n = in.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                in.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject object = new JSONObject(writer.toString());
                JSONArray JSONCampings = object.getJSONObject("result").getJSONArray("records");
                for (int i = 0; i < JSONCampings.length(); i++) {
                    JSONObject JSONCamping = JSONCampings.getJSONObject(i);
                    int id = JSONCamping.getInt("_id");
                    String nombre = JSONCamping.getString("Nombre");
                    String categoria = JSONCamping.getString("Categoria");
                    String municipio = JSONCamping.getString("Municipio");
                    String provincia = JSONCamping.getString("Provincia");
                    String correo = JSONCamping.getString("Email");
                    String web = JSONCamping.getString("Web");
                    String periodo = JSONCamping.getString("Periodo");
                    String plazas = JSONCamping.getString("Plazas");
                    String direccion = JSONCamping.getString("Direccion");
                    String cp = JSONCamping.getString("CP");
                    Camping camping = new Camping(id, cp, periodo, plazas, direccion, web, nombre, categoria, provincia, municipio, correo);
                    campings.add(camping);
                }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            return campings;
        }

        private String getLastId() {
            ArrayList campings=new ArrayList<Camping>();
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                String url = "https://dadesobertes.gva.es/api/3/action/package_search?q=dades-turisme-campings-comunitat-valenciana";

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                //add request header
                con.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                con.setRequestProperty("accept", "application/json;");
                con.setRequestProperty("accept-language", "es");
                con.connect();
                int responseCode = con.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                int n;
                while ((n = in.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                in.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String id = "";
            try {
                JSONObject object = new JSONObject(writer.toString());
                JSONArray JSONCampings = object.getJSONObject("result").getJSONArray("results").getJSONObject(0).getJSONArray("resources");
                JSONObject JSONCamping = JSONCampings.getJSONObject(0);
                id = JSONCamping.getString("id");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return id;
        }

        @Override
        protected void onPostExecute(ArrayList campings) {
            setupData(campings);
        }
    }

    private void setupData(ArrayList<Camping> campings){
        this.campings = campings;
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
        setupData(campings);
    }
    public void Ordernar_Ascendente(View view) {
        campings_filter = new ArrayList<>();
        Collections.sort(campings, Camping.comparadorNombreAscendente);
        setupData(campings);
    }
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Camping camping = campings.get(position);
        intent.putExtra("camping",camping);
        startActivity(intent);
    }
    public void Ordenar_Ascendente(MenuItem item) {
        campings_filter = new ArrayList<>();
        Collections.sort(campings, Camping.comparadorNombreAscendente);
        setupData(campings);
    }
    public void Ordernar_Descendente(MenuItem item) {
        campings_filter = new ArrayList<>();
        Collections.sort(campings, Camping.comparadorNombreDescendente);
        setupData(campings);
    }
}