package proyecto.nathan.jmovieapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFavoritas extends RecyclerView.Adapter<AdapterFavoritas.ViewHolderDatos> implements View.OnClickListener {
    static ArrayList<Pelicula> listFavoritas;
    private Context mCtx;
    private View.OnClickListener listener;
    private View.OnLongClickListener listenerLong;
    private String nombreUsuario;
    private String correoUsuario;
    int posicion;
    public AdapterFavoritas(ArrayList<Pelicula> listFavoritas, Context mCtx, String nombreUsuario, String correoUsuario) {
        this.listFavoritas = listFavoritas;   this.mCtx = mCtx;
        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
    }


    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mostrarpeli_fav,viewGroup,false);
        view.setOnClickListener(this);



        return new ViewHolderDatos(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterFavoritas.ViewHolderDatos viewHolderDatos, int i) {
        viewHolderDatos.asignarDatos(listFavoritas.get(i));
        final int posicion = i;
        viewHolderDatos.buttonViewOption.setOnClickListener(new View.OnClickListener() {

       ViewHolderDatos v = viewHolderDatos;
            @Override
            public void onClick(final View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, v.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.opcion_menu_fav);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuBorrar:
                                ConexionBBDD cbd = new ConexionBBDD(view.getContext());
                                String [] id = cbd.getIDusuario(correoUsuario);
                                cbd.eliminarFavorita(id[0],listFavoritas.get(posicion).getId());
                                listFavoritas.remove(posicion);
                                notifyDataSetChanged();

                                break;
                            case R.id.menuVer:
                                //handle menu3 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });




    }

    @Override
    public int getItemCount() {

            return listFavoritas.size();
    }

    public void setOnClickLisetner(View.OnClickListener listener){
        this.listener = listener;
    }

    public void setOnLongClickLisetner(View.OnLongClickListener listener){
        this.listenerLong = listener;
    }

    public boolean onLongClick(View v) {
        return false;
    }
    @Override
    public void onClick(View v) {
    if(listener != null){
        listener.onClick(v);
    }
    }





    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView titulo;
        CircleImageView imagen;
        TextView buttonViewOption;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagen);
            titulo = itemView.findViewById(R.id.titulo);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }




        public void asignarDatos(Pelicula p) {
            try {
                URL url = new URL(p.getImagen());
                HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
                urlcon.setDoInput(true);
                urlcon.connect();
                InputStream in = urlcon.getInputStream();
                Bitmap mIcon = BitmapFactory.decodeStream(in);
                imagen.setImageBitmap(mIcon);

            }catch (MalformedURLException e){

            } catch (IOException e) {
                e.printStackTrace();
            }
            titulo.setText(p.titulo);



        }


    }

    public void eliminarElemento(int posicion){
        listFavoritas.remove(posicion);
    }


    public void recuperarPelicula(Pelicula pelicula, int posicion){
        listFavoritas.add(posicion,pelicula);
    }
    }




