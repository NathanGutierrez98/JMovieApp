package proyecto.nathan.jmovieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import static proyecto.nathan.jmovieapp.AdapterFavoritas.listFavoritas;


public class Favoritas extends AppCompatActivity {
    private static final int ACTIVITY_SELECT_IMAGE = 1020,
            ACTIVITY_SELECT_FROM_CAMERA = 1040, ACTIVITY_SHARE = 1030;
    private static String apikey;
    private SearchView busqueda;
    private TextView prueba;
    private ArrayList<Pelicula> listaFavoritas;
    private RecyclerView recycler;
    private AdapterFavoritas adaptador;
    private View v;
    private DrawerLayout drawerLayout;
    private TextView nav_user;
    private TextView nav_correo;

    /**
     * Titulo inicial del drawer
     */
    private String drawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        listaFavoritas = new ArrayList<Pelicula>();
        setContentView(R.layout.favoritas);
        ocultarUI();

        setToolbar(); // Setear Toolbar como action bar

        String[] valores = getIntent().getStringArrayExtra("nombreUsuario");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        nav_user = (TextView) hView.findViewById(R.id.username);
        nav_correo = (TextView) hView.findViewById(R.id.email);
        nav_user.setText(valores[0]);
        nav_correo.setText(valores[1]);


        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        drawerTitle = getResources().getString(R.string.home_item);
        if (savedInstanceState == null) {

        }
        cargarInformacion();
        inicializar();
        ConexionBBDD cbd = new ConexionBBDD(this);
        String correo[] = new String[1];
        correo[0] = nav_correo.getText().toString();
        apikey = cbd.getToken(correo[0]);
        obtenerPeliculaFav(cargarPeliculasFavoritas());



    }

    private void inicializar() {
        recycler = findViewById(R.id.recyclerId);
        GridLayoutManager gridLayout = new GridLayoutManager(recycler.getContext(), 1);

        recycler.setLayoutManager(gridLayout);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(),
                DividerItemDecoration.VERTICAL);

        recycler.addItemDecoration(dividerItemDecoration);
        adaptador = new AdapterFavoritas(listaFavoritas, this, nav_user.getText().toString(), nav_correo.getText().toString());
        recycler.setAdapter(adaptador);
        recycler.setHasFixedSize(true);


        ItemTouchHelper mover = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder actual, @NonNull RecyclerView.ViewHolder seleccionada) {
                int posicionActual = actual.getAdapterPosition();
                int posicionSeleccionada = seleccionada.getAdapterPosition();
                Collections.swap(listaFavoritas, posicionActual, posicionSeleccionada);
                adaptador.notifyItemMoved(posicionActual, posicionSeleccionada);
                guardarInformacion();
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
                final Pelicula peliculaEliminada = listaFavoritas.get(posicion);
                if(direccion == ItemTouchHelper.LEFT){
                    final boolean[] recuperada = {false};
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
                            recuperada[0] = true;

                        }
                    });
                    if(recuperada[0] == false){
                        ConexionBBDD cbd = new ConexionBBDD(getApplicationContext());
                        String [] id = cbd.getIDusuario(nav_correo.getText().toString());
                        cbd.eliminarFavorita(id[0],peliculaEliminada.getId());

                    }

                }

            }
        });

        eliminar.attachToRecyclerView(recycler);
        recycler.setAdapter(adaptador);
        guardarInformacion();
    }
    public void guardarInformacion(){
        SharedPreferences sharedP = getSharedPreferences("guardarFavoritas", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedP.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listaFavoritas);
        ConexionBBDD cbd = new ConexionBBDD(this);
        String id [] = cbd.getIDusuario(nav_correo.getText().toString());
        editor.putString("id",""+ id[0]);
        editor.putString("listaFavoritas", json);
        editor.apply();
        listenerAdaptador();
    }
    public ArrayList<Pelicula> cargarInformacion(){

        SharedPreferences sharedP = getSharedPreferences("guardarFavoritas", MODE_PRIVATE);
        Gson gson = new Gson();
        ConexionBBDD cbd = new ConexionBBDD(this);
        String idActual [] = cbd.getIDusuario(nav_correo.getText().toString());
        String id =sharedP.getString("id", null);

        if(id.equals(idActual[0])) {

            String json = sharedP.getString("listaFavoritas", null);
            Type type = new TypeToken<ArrayList<Pelicula>>() {
            }.getType();
            listaFavoritas = gson.fromJson(json, type);
            if (listaFavoritas == null) {
                listaFavoritas = new ArrayList<Pelicula>();
            }
        }
        return listaFavoritas;


    }
    private void listenerAdaptador(){
        adaptador.setOnClickLisetner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pelicula p = listFavoritas.get(recycler.getChildAdapterPosition(v));

                Intent i = new Intent(v.getContext(), ActividadMostrarPelicula.class);

                i.putExtra("Pelicula", p);
                v.getContext().startActivity(i);
            }

        });
    }
    public void obtenerPeliculaFav(ArrayList<String> listaPelisFav) {


        for (int i = 0; i < listaPelisFav.size(); i++) {
            final Pelicula[] pelicula = {null};
            final boolean[] encontrado = {false};
            String url = "http://www.omdbapi.com/";
            String resultado;
            String urlFinal = "http://www.omdbapi.com/?i=" + listaPelisFav.get(i) + "&apikey=" + apikey;
            RequestQueue q = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject objetoJSON = new JSONObject(response);
                                pelicula[0] = detallesPelicula(objetoJSON);
                                encontrado[0] = true;


                                if (!listaFavoritas.contains(pelicula[0])) {
                                    listaFavoritas.add(pelicula[0]);
                                    adaptador.notifyDataSetChanged();
                                    recycler.setAdapter(adaptador);

                                }

                            } catch (JSONException e) {

                            }


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

        adaptador.setOnClickLisetner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pelicula p = listaFavoritas.get(recycler.getChildAdapterPosition(v));

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

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Películas y series favoritas");
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupDrawerContent(final NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        // Marcar item presionado

                        switch (menuItem.toString()) {
                            case "Añadir API KEY":
                                Intent y = new Intent(navigationView.getContext(),AgregarToken.class);
                                String[] valoresy = {nav_user.getText().toString(),nav_correo.getText().toString()};
                                y.putExtra("nombreUsuario",valoresy);
                                startActivity(y);
                                return true;
                            case "Pendientes por ver":
                                Intent z = new Intent(navigationView.getContext(),Pendientes.class);
                                String[] valoresz = {nav_user.getText().toString(),nav_correo.getText().toString()};
                                z.putExtra("nombreUsuario",valoresz);
                                startActivity(z);
                                return true;

                            case "Buscar series o películas":
                                Intent x = new Intent(navigationView.getContext(),MainActivity.class);
                                String[] valoresx = {nav_user.getText().toString(),nav_correo.getText().toString()};
                                x.putExtra("nombreUsuario",valoresx);
                                startActivity(x);
                                return true;

                            case "Cerrar Sesión":
                                // finish();
                                Intent l = new Intent(navigationView.getContext(),LoginUsuario.class);
                                startActivity(l);
                                return true;

                        }

                        return true;
                    }
                }
        );

    }
    private Pelicula detallesPelicula(JSONObject pelicula){
        Pelicula peli = null;
        try{
            String id = pelicula.getString("imdbID");
            String titulo = pelicula.getString("Title");
            String anio = pelicula.getString("Year");
            String imagen = pelicula.getString("Poster");
            String lanzamiento = pelicula.getString("Released");
            String duracion = pelicula.getString("Runtime");
            String lenguaje = pelicula.getString("Language");
            String pais = pelicula.getString("Country");
            String guion = pelicula.getString("Writer");
            String descripcion = pelicula.getString("Plot");
            String director = pelicula.getString("Director");
            String genero = pelicula.getString("Genre");

            peli = new Pelicula(id,titulo,anio,duracion,lanzamiento,lenguaje,pais,imagen,descripcion,guion,director,genero);

        }catch (JSONException e){

        }
        guardarInformacion();
        return peli;
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public ArrayList<String> cargarPeliculasFavoritas(){
        ConexionBBDD cbd = new ConexionBBDD(this);
        String nom[] =cbd.getIDusuario(nav_correo.getText().toString());


       return cbd.getFavoritas(nom[0]);
    }
}
