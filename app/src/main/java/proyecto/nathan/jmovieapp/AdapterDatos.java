package proyecto.nathan.jmovieapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> implements View.OnClickListener {
    static ArrayList<Pelicula> listDatos;
    private View.OnClickListener listener;
    int posicion;
    public AdapterDatos(ArrayList<Pelicula> listDatos) {
        this.listDatos = listDatos;
    }


    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mostrarpeli,viewGroup,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDatos.ViewHolderDatos viewHolderDatos, int i) {
        viewHolderDatos.asignarDatos(listDatos.get(i));




    }

    @Override
    public int getItemCount() {

            return listDatos.size();
    }

    public void setOnClickLisetner(View.OnClickListener listener){
        this.listener = listener;
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
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagen);
            titulo = itemView.findViewById(R.id.titulo);
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
        listDatos.remove(posicion);
    }


    public void recuperarPelicula(Pelicula pelicula, int posicion){
        listDatos.add(posicion,pelicula);
    }
    }




