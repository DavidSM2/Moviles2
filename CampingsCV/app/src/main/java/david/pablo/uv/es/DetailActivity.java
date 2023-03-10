package david.pablo.uv.es;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DetailActivity extends AppCompatActivity {
    Camping camping;
    FavDB favDB = new FavDB(this);
    FloatingActionButton fav;

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
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favDB.isFav(camping.getId())){
                    favDB.deleteCamping(camping.getId());
                }
                else{
                    favDB.insertCamping(camping);
                }
            }
        });
    }

    public void showMap(View view) {
        String nameCoded = camping.getNombre().replace(" ","+");
        Uri uri = Uri.parse("geo:0,0?q=" + nameCoded);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void showWeb(View view) {
        Uri uri = Uri.parse(camping.getWeb());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /*


            favDB.insertCamping("CACA",1);
            favDB.insertCamping("DOS",2);

    * */
}
