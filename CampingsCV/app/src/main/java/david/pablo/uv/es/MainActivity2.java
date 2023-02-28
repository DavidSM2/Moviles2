package david.pablo.uv.es;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity2 extends AppCompatActivity {
    Camping camping;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        camping = (Camping) getIntent().getSerializableExtra("camping");
        TextView textLugar = (TextView) findViewById(R.id.campingLugar2);
        TextView textNombre = (TextView) findViewById(R.id.campingName2);
        TextView textCorreo = (TextView) findViewById(R.id.campingCorreo2);
        TextView textCategoria = (TextView) findViewById(R.id.campingCategoria2);
        textLugar.setText(camping.getMunicipio() + "( " + camping.getProvincia() + ")");
        textNombre.setText(camping.getNombre());
        textCorreo.setText(camping.getCorreo());
        textCategoria.setText(camping.getCategoria());
    }
}
