package proyecto.nathan.jmovieapp;

import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;



public class MainActivity extends AppCompatActivity {
    private static final int ACTIVITY_SELECT_IMAGE = 1020,
            ACTIVITY_SELECT_FROM_CAMERA = 1040, ACTIVITY_SHARE = 1030;
    private static String apikey;
    private SearchView busqueda;
    private TextView prueba;
    private ArrayList<Pelicula> listaPelis;
    private RecyclerView recycler;
    private AdapterDatos adaptador;
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

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        listaPelis = new ArrayList<Pelicula>();
        setContentView(R.layout.activity_main);
        ocultarUI();
        findViewById(R.id.mainContent).setOnClickListener(new View.OnClickListener() {
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

        setToolbar(); // Setear Toolbar como action bar

        String [] valores =  getIntent().getStringArrayExtra("nombreUsuario");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        nav_user = (TextView)hView.findViewById(R.id.username);
        nav_correo = (TextView)hView.findViewById(R.id.email);
        nav_user.setText(valores[0]);
        nav_correo.setText(valores[1]);


        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        drawerTitle = getResources().getString(R.string.home_item);
        if (savedInstanceState == null) {

        }

        inicializar();
        ConexionBBDD cbd = new ConexionBBDD(this);
        String correo[] = new String[1];
        correo[0] = nav_correo.getText().toString();
        try{
            apikey = cbd.getToken(correo[0]);
            if(apikey ==null){
                Toast.makeText(this,"No tienes una APIKEY enlazada a tu cuenta",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){

        }



    }

    private void inicializar() {
        recycler = findViewById(R.id.recyclerId);
        GridLayoutManager gridLayout = new GridLayoutManager(recycler.getContext(),2);

        recycler.setLayoutManager(gridLayout);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recycler.addItemDecoration(itemDecoration);

        adaptador = new AdapterDatos(listaPelis, this,nav_user.getText().toString(), nav_correo.getText().toString());
        recycler.setAdapter(adaptador);
        recycler.setHasFixedSize(true);


        /*final ItemTouchHelper eliminar = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
        recycler.setAdapter(adaptador);*/
    }







    public void metodoJSON() {

        String url = "http://www.omdbapi.com/";
        String parametro = busqueda.getQuery().toString();
        String resultado;
        String urlFinal = "http://www.omdbapi.com/?s=" + parametro + "&apikey="+ apikey;
        RequestQueue q = Volley.newRequestQueue(this);
       listaPelis.clear();
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


            JSONArray arrayPelis = pelicula.getJSONArray("Search");
            cabeceraPelicula(arrayPelis);





        } catch (JSONException e) {

        }

    }



    public void cabeceraPelicula(JSONArray jsonArray) {
        JSONObject peli = null;

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                final Pelicula[] pelicula = {new Pelicula()};
                peli = jsonArray.getJSONObject(i);

                //Pelicula pelicula = new Pelicula(titulo, anio, duracion, lanzamiento, lenguaje, pais, imagen, descripcion, guion, director, genero);
                final boolean[] encontrado = {false};

                String url = "http://www.omdbapi.com/";
                String parametro = busqueda.getQuery().toString();
                String resultado;
                String urlFinal = "http://www.omdbapi.com/?i=" + peli.getString("imdbID") + "&apikey="+apikey;
                RequestQueue q = Volley.newRequestQueue(this);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlFinal,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                peliculaEncontrada(response);
                                try {
                                    JSONObject objetoJSON = new JSONObject(response);
                                    pelicula[0] = detallesPelicula(objetoJSON);
                                    encontrado[0] = true;


                                    if (!listaPelis.contains(pelicula[0])) {
                                        listaPelis.add(pelicula[0]);
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

        } catch (JSONException e) {

        }



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

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Buscar series o películas");
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
                            case "Favoritas":
                                Intent i = new Intent(navigationView.getContext(),Favoritas.class);
                                String[] valores = {nav_user.getText().toString(),nav_correo.getText().toString()};
                                i.putExtra("nombreUsuario",valores);
                                startActivity(i);
                                return true;
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

                            case "Cerrar Sesión":

                                Intent l = new Intent(navigationView.getContext(),LoginUsuario.class);
                                finish();
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


}
