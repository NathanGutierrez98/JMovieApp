package proyecto.nathan.jmovieapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ActividadMostrarPelicula extends AppCompatActivity {
private ImageView imagen;
private TextView titulo;
private TextView director;
private TextView guion;
private TextView pais;
private TextView lenguaje;
private TextView descripcion;
private TextView genero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutpelicula);
        titulo = findViewById(R.id.titulo_l);
        director = findViewById(R.id.director_l);
        imagen = findViewById(R.id.imagen_l);
        guion = findViewById(R.id.guion_l);
        pais = findViewById(R.id.pais_l);
        lenguaje = findViewById(R.id.lengua_l);
        descripcion = findViewById(R.id.descripcion_l);
        genero = findViewById(R.id.genero_l);

            Pelicula pelicula = (Pelicula) getIntent().getSerializableExtra("Pelicula");



            asignarDatos(pelicula);

    }

    public void asignarDatos(Pelicula pelicula) {
        try {
            URL url = new URL(pelicula.getImagen());
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            urlcon.setDoInput(true);
            urlcon.connect();
            InputStream in = urlcon.getInputStream();
            final Bitmap mIcon = BitmapFactory.decodeStream(in);
            imagen.setImageBitmap(mIcon);


        }catch (MalformedURLException e){

        } catch (IOException e) {
            e.printStackTrace();
        }
        titulo.setText(pelicula.getTitulo() +  " (" +pelicula.getAnio() + ") ");
        director.setText("Director: " +  pelicula.getDirector());
        guion.setText("Guionista: " + pelicula.getGuion());
        pais.setText("País: " + pelicula.getPais());
        lenguaje.setText("Lenguaje: " + pelicula.getLenguaje());
        descripcion.setText("Descripción: \n" +pelicula.getDescripcion());
        genero.setText("Género: " + pelicula.getGenero());



    }


}
