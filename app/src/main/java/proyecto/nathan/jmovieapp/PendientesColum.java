package proyecto.nathan.jmovieapp;

import android.provider.BaseColumns;

public class PendientesColum {
    public static abstract class PendientesEntry implements BaseColumns {
        public static final String TABLE_NAME = "PENDIENTES";

        public static final String ID_USUARIO = "ID_USUARIO";
        public static final String ID_PELICULA = "ID_PELICULA";

    }
}
