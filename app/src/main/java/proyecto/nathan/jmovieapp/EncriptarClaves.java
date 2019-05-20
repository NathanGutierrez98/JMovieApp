package proyecto.nathan.jmovieapp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncriptarClaves {
    public static String encriptarClaves(String clave){
        try {

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(clave.getBytes(),0,clave.length());
            String claveEncriptada = new BigInteger(1,md5.digest()).toString(16);
            return claveEncriptada;

        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
