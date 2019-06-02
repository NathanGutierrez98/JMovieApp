package proyecto.nathan.jmovieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class ConexionBBDD extends SQLiteOpenHelper {


    public ConexionBBDD(Context context) {
        super(context, "JMOVIEAPP",null,1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + UsuarioColumn.UsuarioEntry.TABLE_NAME + " ("
                +  UsuarioColumn.UsuarioEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UsuarioColumn.UsuarioEntry.USUARIO+ " TEXT NOT NULL,"
                + UsuarioColumn.UsuarioEntry.CONTRASENIA+ " TEXT NOT NULL,"
                + UsuarioColumn.UsuarioEntry.NOMBRE + " TEXT NOT NULL,"
                + UsuarioColumn.UsuarioEntry.APELLIDOS + " TEXT NOT NULL,"
                + UsuarioColumn.UsuarioEntry.CORREO + " TEXT NOT NULL UNIQUE,"
                + UsuarioColumn.UsuarioEntry.TOKEN + " TEXT )");

        db.execSQL("CREATE TABLE " + FavoritasColum.FavoritasEntry.TABLE_NAME + " ("
                +  FavoritasColum.FavoritasEntry.ID_USUARIO + " INTEGER REFERENCES USUARIOS(ID),"
                + FavoritasColum.FavoritasEntry.ID_PELICULA + " TEXT," +
                "UNIQUE (ID_USUARIO, ID_PELICULA))");

        db.execSQL("CREATE TABLE " + PendientesColum.PendientesEntry.TABLE_NAME + " ("
                +  FavoritasColum.FavoritasEntry.ID_USUARIO + " INTEGER REFERENCES USUARIOS(ID),"
                + FavoritasColum.FavoritasEntry.ID_PELICULA + " TEXT," +
                "UNIQUE (ID_USUARIO, ID_PELICULA))");




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public String guardar(Usuario usuario){
        String mensaje = "";
         SQLiteDatabase db = (SQLiteDatabase) getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UsuarioColumn.UsuarioEntry.USUARIO,usuario.getUsuario());
        values.put(UsuarioColumn.UsuarioEntry.CONTRASENIA,EncriptarClaves.encriptarClaves(usuario.getContrasenia()));
        values.put(UsuarioColumn.UsuarioEntry.NOMBRE,usuario.getNombre());
        values.put(UsuarioColumn.UsuarioEntry.APELLIDOS,usuario.getApellidos());
        values.put(UsuarioColumn.UsuarioEntry.CORREO,usuario.getCorreo());
        //db.insert(UsuarioColumn.UsuarioEntry.TABLE_NAME, null, values);

        try {
            db.insertOrThrow(UsuarioColumn.UsuarioEntry.TABLE_NAME,null, values);
            mensaje = "Registrado correctamente, ya puede iniciar sesión";
        }catch (SQLException e){
            mensaje = "NO Ingresado";
        }

        //db.close();
        return mensaje;
    }

    public Boolean comprobarUsuario(String nombreUsuario){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT USUARIO  FROM " + UsuarioColumn.UsuarioEntry.TABLE_NAME + " WHERE USUARIO LIKE '" + nombreUsuario + "'", null);
        if (c.moveToFirst()){
            return false;
        }
        c.close();
       // db.close();
        return true;
    }


    public Boolean login(String nombreUsuario, String contrasenia){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT *  FROM " + UsuarioColumn.UsuarioEntry.TABLE_NAME + " WHERE (USUARIO LIKE '" + nombreUsuario + "' OR CORREO LIKE '" + nombreUsuario +"')  AND CONTRASENIA = '" +  contrasenia + "'" , null);
        if (c.moveToFirst()){
            return true;
        }
        c.close();
       // db.close();
        return false;
    }

    public String[] getUsuarioCorreo(String nombreUsuario){
        String []valores = new String[3];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT USUARIO,CORREO  FROM " + UsuarioColumn.UsuarioEntry.TABLE_NAME + " WHERE (USUARIO LIKE '" + nombreUsuario + "' OR CORREO LIKE '" + nombreUsuario +"')", null);
        if (c.moveToFirst()){

          valores[0] = c.getString(0);
          valores[1] = c.getString(1);

        }
        c.close();
      //  db.close();
        return valores;
    }

    public String anadirFavoritas(String usuario,String idPelicula){
        String mensaje = "";
        SQLiteDatabase db = (SQLiteDatabase) getWritableDatabase();

       String id[] = getIDusuario(usuario);
        ContentValues values = new ContentValues();
        values.put(FavoritasColum.FavoritasEntry.ID_USUARIO,id[0]);
        values.put(FavoritasColum.FavoritasEntry.ID_PELICULA,idPelicula);

        //db.insert(FavoritasColum.FavoritasEntry.TABLE_NAME, null, values);

        try {
            db.insert(FavoritasColum.FavoritasEntry.TABLE_NAME,null, values);
           Log.d("ingreso","Registrado correctamente, ya puede iniciar sesión");
        }catch (SQLException e){
            Log.d("ingreso","NO Ingresado");
        }

       // db.close();
        return mensaje;
    }

    public String[] getIDusuario(String correoUsuario){
        String []valores = new String[3];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ID  FROM " + UsuarioColumn.UsuarioEntry.TABLE_NAME + " WHERE (CORREO LIKE '" + correoUsuario + "')", null);
        if (c.moveToFirst()){

            valores[0] = c.getString(0);


        }
        c.close();
        //db.close();
        return valores;
    }


    public ArrayList<String> getFavoritas(String idUsuario){
        ArrayList<String> idsPeliculas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ID_PELICULA  FROM " + FavoritasColum.FavoritasEntry.TABLE_NAME + " WHERE ID_USUARIO LIKE '" + idUsuario + "'", null);
        if (c.moveToFirst()){
            do {
                // Passing values
                Log.d("GET FAVORITAS",c.getString(0));
                idsPeliculas.add(c.getString(0));
                // Do something Here with values
            } while(c.moveToNext());



        }
        c.close();
        //db.close();
        return idsPeliculas;
    }

    public void eliminarFavorita(String idUsuario, String idPelicula){
        SQLiteDatabase db = this.getReadableDatabase();
       db.delete(FavoritasColum.FavoritasEntry.TABLE_NAME ,"ID_USUARIO LIKE ? AND ID_PELICULA LIKE ? ",new String[]{String.valueOf(idUsuario),idPelicula});

                // Passing values


                // Do something Here with values



        }


    public String agregarToken(String usuario,String token){
        String mensaje = "";
        SQLiteDatabase db = (SQLiteDatabase) getWritableDatabase();

        String id[] = getIDusuario(usuario);
        ContentValues values = new ContentValues();
        values.put(UsuarioColumn.UsuarioEntry.ID,id[0]);
        values.put(UsuarioColumn.UsuarioEntry.TOKEN,token);

        //db.insert(FavoritasColum.FavoritasEntry.TABLE_NAME, null, values);

        try {
            db.update(UsuarioColumn.UsuarioEntry.TABLE_NAME,values,UsuarioColumn.UsuarioEntry.ID + "= ?",new String[]{String.valueOf(id[0])});

            mensaje = "API KEY añadida correctamente";
        }catch (SQLException e){
           mensaje = "Error al agregar el API KEY";
        }

        // db.close();
        return mensaje;
    }

    public String getToken(String correoUsuario){
        String []valores = new String[3];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT TOKEN  FROM " + UsuarioColumn.UsuarioEntry.TABLE_NAME + " WHERE (CORREO LIKE '" + correoUsuario + "')", null);
        if (c.moveToFirst()){

            valores[0] = c.getString(0);


        }
        c.close();
        //db.close();
        return valores[0];
    }

    public ArrayList<String> getPendientes(String idUsuario){
        ArrayList<String> idsPeliculas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ID_PELICULA  FROM " + PendientesColum.PendientesEntry.TABLE_NAME + " WHERE ID_USUARIO LIKE '" + idUsuario + "'", null);
        if (c.moveToFirst()){
            do {
                // Passing values
                Log.d("GET PENDIENTES",c.getString(0));
                idsPeliculas.add(c.getString(0));
                // Do something Here with values
            } while(c.moveToNext());



        }
        c.close();
        //db.close();
        return idsPeliculas;
    }

    public void eliminarPendientes(String idUsuario, String idPelicula){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(PendientesColum.PendientesEntry.TABLE_NAME ,"ID_USUARIO LIKE ? AND ID_PELICULA LIKE ? ",new String[]{String.valueOf(idUsuario),idPelicula});

        // Passing values


        // Do something Here with values



    }

    public String anadirPendientes(String usuario,String idPelicula){
        String mensaje = "";
        SQLiteDatabase db = (SQLiteDatabase) getWritableDatabase();

        String id[] = getIDusuario(usuario);
        ContentValues values = new ContentValues();
        values.put(PendientesColum.PendientesEntry.ID_USUARIO,id[0]);
        values.put(PendientesColum.PendientesEntry.ID_PELICULA,idPelicula);

        //db.insert(FavoritasColum.FavoritasEntry.TABLE_NAME, null, values);

        try {
            db.insert(PendientesColum.PendientesEntry.TABLE_NAME,null, values);
            Log.d("Pendiente","Pendiente añadida");
        }catch (SQLException e){
            Log.d("Pendiente","Pendiente no añadida");
        }

        // db.close();
        return mensaje;
    }
}
