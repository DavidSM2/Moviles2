package david.pablo.uv.es;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class DetailActivity extends AppCompatActivity {
    Camping camping;
    FavDB favDB = new FavDB(this);
    FloatingActionButton fav;
    Location actLocation;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        camping = (Camping) getIntent().getSerializableExtra("camping");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        TextView textLugar = findViewById(R.id.campingLugar);
        TextView textNombre = findViewById(R.id.campingName);
        TextView textCorreo = findViewById(R.id.campingCorreo);
        TextView textCategoria = findViewById(R.id.campingCategoria);
        TextView textDireccion = findViewById(R.id.campingDireccion);
        TextView textPeriodo = findViewById(R.id.campingPeriodo);
        TextView textPlazas = findViewById(R.id.campingPlazas);
        textLugar.setText(camping.getMunicipio() + "( " + camping.getProvincia() + ")");
        textNombre.setText(camping.getNombre());
        textCorreo.setText(camping.getCorreo());
        textCategoria.setText(camping.getCategoria());
        textDireccion.setText(camping.getDirecion() + "( " + camping.getCP() + ")");
        textPeriodo.setText(camping.getPeriodo());
        textPlazas.setText("Plazas: " + camping.getPlazas());

        fav = findViewById(R.id.fav);
        if (favDB.isFav(camping.getId()))
            fav.setImageResource(R.drawable.fav_icon);
        else
            fav.setImageResource(R.drawable.remove_fav);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favDB.isFav(camping.getId())) {
                    fav.setImageResource(R.drawable.remove_fav);
                    favDB.deleteCamping(camping.getId());
                } else {
                    fav.setImageResource(R.drawable.fav_icon);
                    favDB.insertCamping(camping);
                }
            }
        });
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, Local);

    }

    public class Localizacion implements LocationListener {
        DetailActivity detailActivity;
        public Localizacion(DetailActivity detailActivity) {
            this.detailActivity = detailActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            String Text = "Mi ubicacion actual es: " + "\n Lat = " + loc.getLatitude() + "\n Long = " + loc.getLongitude();
            detailActivity.actLocation = loc;
        }
    }

    public void showMap(View view) {
        String nameCoded = camping.getNombre().replace(" ", "+");
        Uri uri = Uri.parse("geo:0,0?q=" + nameCoded);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void showWeb(View view) {
        Uri uri = Uri.parse(camping.getWeb());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
