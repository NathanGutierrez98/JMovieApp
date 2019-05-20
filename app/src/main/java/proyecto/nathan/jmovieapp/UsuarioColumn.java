package proyecto.nathan.jmovieapp;

import android.provider.BaseColumns;

public class UsuarioColumn {
    public static abstract class UsuarioEntry implements BaseColumns {
        public static final String TABLE_NAME = "USUARIOS";

        public static final String ID = "ID";
        public static final String USUARIO = "USUARIO";
        public static final String CONTRASENIA = "CONTRASENIA";
        public static final String NOMBRE = "NOMBRE";
        public static final String APELLIDOS = "APELLIDOS";
        public static final String CORREO = "CORREO";
    }
}
