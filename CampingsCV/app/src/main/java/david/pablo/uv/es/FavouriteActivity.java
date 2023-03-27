package david.pablo.uv.es;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.Collections;

public class FavouriteActivity extends AppCompatActivity implements RecyclerViewInterface{
        RecyclerView recyclerView;
        EditText editTextBusqueda;
        ArrayList<Camping> campings;
        CampingsAdapter adapter;
        FavDB favDB = new FavDB(this);
        FloatingActionButton fav;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_favourite);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview_campings);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            editTextBusqueda = findViewById(R.id.textoBusqueda);
            fav = findViewById(R.id.fav);
            campings = new ArrayList<Camping>();

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }

            getData();
        }

        public void getData() {
            campings = favDB.read_all_data();
            //TODO: read the data of each camping, create a new Camping object and insert it in the campings arraylist.
            setupData();
        }

        private void setupData() {
            adapter = new CampingsAdapter(campings, getApplicationContext(),this);
            recyclerView.setAdapter(adapter);
        }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
        Camping camping = campings.get(position);
        intent.putExtra("camping",camping);
        startActivity(intent);
    }
}
