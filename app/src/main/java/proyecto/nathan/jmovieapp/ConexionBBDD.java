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

public class ConexionBBDD extends SQLiteOpenHelper {

    final String CREACION_TABLA = "CREATE TABLE USUARIOS (id INTEGER primary key AUTOINCREMENT,  usuario VARCHAR(50) NOT NULL, contrasenia TEXT NOT NULL, nombre VARCHAR(50) NOT NULL, apellidos VARCHAR(50) NOT NULL, correo VARCHAR(100) NOT NULL)";
    public ConexionBBDD(Context context) {
        super(context, UsuarioColumn.UsuarioEntry.TABLE_NAME,null,1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + UsuarioColumn.UsuarioEntry.TABLE_NAME + " ("
                +  UsuarioColumn.UsuarioEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UsuarioColumn.UsuarioEntry.USUARIO+ " TEXT NOT NULL,"
                + UsuarioColumn.UsuarioEntry.CONTRASENIA+ " TEXT NOT NULL,"
                + UsuarioColumn.UsuarioEntry.NOMBRE + " TEXT NOT NULL,"
                + UsuarioColumn.UsuarioEntry.APELLIDOS + " TEXT NOT NULL,"
                + UsuarioColumn.UsuarioEntry.CORREO + " TEXT NOT NULL)");



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
       Log.d("mensaje",EncriptarClaves.encriptarClaves(usuario.getContrasenia()));
        values.put(UsuarioColumn.UsuarioEntry.NOMBRE,usuario.getNombre());
        values.put(UsuarioColumn.UsuarioEntry.APELLIDOS,usuario.getApellidos());
        values.put(UsuarioColumn.UsuarioEntry.CORREO,usuario.getCorreo());
        db.insert(UsuarioColumn.UsuarioEntry.TABLE_NAME, null, values);

        try {
            db.insertOrThrow(UsuarioColumn.UsuarioEntry.TABLE_NAME,null, values);
            mensaje = "Ingresado Correctamente";
        }catch (SQLException e){
            mensaje = "NO Ingresado";
        }

        db.close();
        return mensaje;
    }

    public Boolean comprobarUsuario(String nombreUsuario){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT USUARIO  FROM " + UsuarioColumn.UsuarioEntry.TABLE_NAME + " WHERE USUARIO LIKE '" + nombreUsuario + "'", null);
        if (c.moveToFirst()){
            return false;
        }
        c.close();
        db.close();
        return true;
    }


    public Boolean login(String nombreUsuario, String contrasenia){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT *  FROM " + UsuarioColumn.UsuarioEntry.TABLE_NAME + " WHERE (USUARIO LIKE '" + nombreUsuario + "' OR CORREO LIKE '" + nombreUsuario +"')  AND CONTRASENIA = '" +  contrasenia + "'" , null);
        if (c.moveToFirst()){
            return true;
        }
        c.close();
        db.close();
        return false;
    }
}
