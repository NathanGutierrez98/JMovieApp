package proyecto.nathan.jmovieapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private SearchView busqueda;
    private TextView prueba;
    private ArrayList<Pelicula> listaPelis;
    private RecyclerView recycler;
    private AdapterDatos adaptador;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        listaPelis = new ArrayList<Pelicula>();
        setContentView(R.layout.activity_main);
        findViewById(R.id.layoutPrincipal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarUI();
            }
        });
        busqueda = findViewById(R.id.busqueda);

        busqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                            @Override
                                            public boolean onQueryTextSubmit(String s) {
                                                metodoJSON();
                                                return true;
                                            }

                                            @Override
                                            public boolean onQueryTextChange(String s) {
                                                return false;
                                            }
                                        }
        );

        inicializar();


    }

    private void inicializar() {
        recycler = findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(recycler.getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        recycler.addItemDecoration(dividerItemDecoration);
        adaptador = new AdapterDatos(listaPelis);
        recycler.setAdapter(adaptador);

        ItemTouchHelper mover = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder actual, @NonNull RecyclerView.ViewHolder seleccionada) {
                int posicionActual = actual.getAdapterPosition();
                int posicionSeleccionada = seleccionada.getAdapterPosition();
                Collections.swap(listaPelis, posicionActual, posicionSeleccionada);
                adaptador.notifyItemMoved(posicionActual, posicionSeleccionada);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }

        });
        mover.attachToRecyclerView(recycler);


        final ItemTouchHelper eliminar = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder elemento, int direccion) {
                final int posicion = elemento.getAdapterPosition();
                final Pelicula peliculaEliminada = listaPelis.get(posicion);
                if(direccion == ItemTouchHelper.LEFT){
                    adaptador.eliminarElemento(posicion);
                    adaptador.notifyItemRemoved(posicion);
                    ocultarUI();
                    Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Eliminado", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    snackbar.setAction("Recuperar serie o pelicula", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // undo is selected, restore the deleted item
                            adaptador.recuperarPelicula(peliculaEliminada, posicion);
                            adaptador.notifyItemInserted(posicion);
                            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), " Pelicula o serie recuperada", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                    });
                }
            }
        });
        eliminar.attachToRecyclerView(recycler);
        recycler.setAdapter(adaptador);
    }







    public void metodoJSON() {
        String key = "&apikey=96bdd898";
        String url = "http://www.omdbapi.com/";
        String parametro = busqueda.getQuery().toString();
        String resultado;
        String urlFinal = "http://www.omdbapi.com/?t=" + parametro + key;
        RequestQueue q = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        peliculaEncontrada(response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );

        q.add(stringRequest);

    }


    public void peliculaEncontrada(String resultado) {

        try {


            JSONObject pelicula = new JSONObject(resultado);
            detallesPelicula(pelicula);

        } catch (JSONException e) {
        }

    }


    public void detallesPelicula(JSONObject jsonPeli) {
        try {
            String titulo = jsonPeli.getString("Title");
            String anio = jsonPeli.getString("Year");
            String lanzamiento = jsonPeli.getString("Released");
            String duracion = jsonPeli.getString("Runtime");
            String lenguaje = jsonPeli.getString("Language");
            String pais = jsonPeli.getString("Country");
            String imagen = jsonPeli.getString("Poster");
            String guion = jsonPeli.getString("Writer");
            String descripcion = jsonPeli.getString("Plot");
            String director = jsonPeli.getString("Director");
            String genero = jsonPeli.getString("Genre");

            Pelicula pelicula = new Pelicula(titulo, anio, duracion, lanzamiento, lenguaje, pais, imagen, descripcion, guion, director, genero);
            if (!listaPelis.contains(pelicula)) {
                listaPelis.add(pelicula);
            }

        } catch (JSONException e) {

        }
        adaptador = new AdapterDatos(listaPelis);

        recycler.setAdapter(adaptador);

        adaptador.setOnClickLisetner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pelicula p = listaPelis.get(recycler.getChildAdapterPosition(v));
                Intent i = new Intent(v.getContext(), ActividadMostrarPelicula.class);

                i.putExtra("Pelicula", p);
                v.getContext().startActivity(i);
            }
        });




    }


    public void ocultarUI(){
        View v = this.getWindow().getDecorView();
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api

            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
        InputMethodManager teclado = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        teclado.hideSoftInputFromWindow(v.getWindowToken(),0);
    }
}
