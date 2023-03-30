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

import androidx.annotation.NonNull;
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
    Address campingLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        camping = (Camping) getIntent().getSerializableExtra("camping");
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

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                actLocation = location;
            }

            @Override
            public void onLocationChanged(@NonNull List<Location> locations) {
                LocationListener.super.onLocationChanged(locations);
            }

            @Override
            public void onFlushComplete(int requestCode) {
                LocationListener.super.onFlushComplete(requestCode);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LocationListener.super.onStatusChanged(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                actLocation = location;
                calcularDistancia();
            }
        }


    }

    private void calcularDistancia() {
        TextView textDistancia = findViewById(R.id.campingDistancia);
        cogerLocationCamping();

        if (actLocation != null && campingLocation != null) {
            double longitud1 = actLocation.getLongitude();
            double longitud2 = campingLocation.getLongitude();
            double latitud1 = actLocation.getLatitude();
            double latitud2 = campingLocation.getLatitude();

            double radioTierra = 6371;
            double dLat = Math.toRadians(latitud2 - latitud1);
            double dLon = Math.toRadians(longitud2 - longitud1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(latitud1)) * Math.cos(Math.toRadians(latitud2)) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distancia = Math.round(radioTierra * c);

            textDistancia.setText("Distancia al camping: " + distancia + " Km.");
        }
        else
            textDistancia.setText("");
    }

    private void cogerLocationCamping() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String campingAddress = camping.getDirecion();

        try {

            List<Address> addresses = geocoder.getFromLocationName(campingAddress, 1);
            if (addresses != null && !addresses.isEmpty()) {
                campingLocation = addresses.get(0);
            }
        } catch (IOException e) {}
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
