package arturoki.myke;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
/*DESECHADO EL MAIN DE LA ACTIVIDAD PARA HACER UN SOLO LAYOUT DE PREFERENCIAS*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferencias()).commit();
    }

    public void rojo(View v){
        SharedPreferences prefe=getSharedPreferences("Blanco",Context.MODE_PRIVATE);
        Toast.makeText(this,"Teclado cambiado a rojo",Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor=prefe.edit();
        editor.putBoolean("datos",false);
        editor.commit();
    }
    public void blanco(View v){
        SharedPreferences prefe=getSharedPreferences("Blanco",Context.MODE_PRIVATE);
        Toast.makeText(this,"Teclado cambiado a blanco",Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor=prefe.edit();
        editor.putBoolean("datos",true);
        editor.commit();
    }
}
