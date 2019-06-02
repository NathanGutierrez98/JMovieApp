package proyecto.nathan.jmovieapp;

import android.provider.BaseColumns;

public class FavoritasColum {
    public static abstract class FavoritasEntry implements BaseColumns {
        public static final String TABLE_NAME = "FAVORITAS";

        public static final String ID_USUARIO = "ID_USUARIO";
        public static final String ID_PELICULA = "ID_PELICULA";

    }
}
