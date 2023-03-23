package david.pablo.uv.es;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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
    CampingsAdapter adapter;
    FloatingActionButton fav;
    HTTPConnector httpConnector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_campings);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        editTextBusqueda = findViewById(R.id.textoBusqueda);
        fav = findViewById(R.id.fav);
        campings = new ArrayList<Camping>();
        httpConnector = new HTTPConnector();
        httpConnector.execute();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        editTextBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textoBusqueda = charSequence.toString().toLowerCase();

                if (textoBusqueda == ""){
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


    }

    class HTTPConnector extends AsyncTask<String, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(String... params) {
            ArrayList campings=new ArrayList<Camping>();
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                String url = "https://dadesobertes.gva.es/api/3/action/datastore_search?id=2ddaf823-5da4-4459-aa57-5bfe9f9eb474";

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
                    int plazas = JSONCamping.getInt("Plazas");
                    String direccion = JSONCamping.getString("Direccion");
                    int cp = JSONCamping.getInt("CP");

                    System.out.println(correo);
                    Camping camping = new Camping(id, cp, periodo, plazas, direccion, web, nombre, categoria, provincia, municipio, correo);
                    campings.add(camping);
                }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            return campings;
        }
        @Override
        protected void onPostExecute(ArrayList campings) {

            setupData(campings);
        }
    }

/*
    public void getData() {
        InputStream is = getApplicationContext().getResources().openRawResource(R.raw.datastore_search);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        HTTPConnector httpConnector = new HTTPConnector();

        try {


                String url = "https://dadesobertes.gva.es/api/3/action/datastore_search?id=2ddaf823-5da4-4459-aa57-5bfe9f9eb474";

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



            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //The String writer.toString() must be parsed in the campings ArrayList by using JSONArray and JSONObject

        //TODO: read the data of each camping, create a new Camping object and insert it in the campings arraylist.
        setupData();
    }
*/

    private void setupData(ArrayList<Camping> campings){
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);
        return true;
    }

    public void Ordenar_Ascendente(MenuItem item) {
        campings_filter = new ArrayList<>();

        Collections.sort(campings, Camping.comparadorNombreAscendente);

        setupData();
    }

    public void Ordernar_Descendente(MenuItem item) {
        campings_filter = new ArrayList<>();

        Collections.sort(campings, Camping.comparadorNombreDescendente);

        setupData();
    }
}