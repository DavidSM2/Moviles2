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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
        TextView textDistancia = findViewById(R.id.campingDistancia);
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

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Este método se llama cuando la ubicación del usuario cambia
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            textDistancia.setText("Distancia: " + latitude + " " + longitude);
        }
        else
        {
            textDistancia.setText("Dnd coño estas?");
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
