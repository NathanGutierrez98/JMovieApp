package proyecto.nathan.jmovieapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class EncriptarClavesTest {

    @Test
    public void encriptarClaves() {
        EncriptarClaves enc = new EncriptarClaves();
        String claves [] = new String[] {"CONTRASENIA","12345","PEPITO","MANOLO","INFORMATICA"};
        String valores [] = new String[] {"568E5C567627A850EB7059FB82449803".toLowerCase(),"827CCB0EEA8A706C4C34A16891F84E7B".toLowerCase(),"F843A482EF1FC4FE0E038AA2C609E081".toLowerCase(),"A14543A65B79852663D489ABECDFF2".toLowerCase(),"58586BA28662504AC1890B14FF7C6B0B".toLowerCase()};


        for(int i = 0; i < claves.length; i++){
            String clave = enc.encriptarClaves(claves[i]);
            assertEquals(clave,valores[i]);
        }

    }
}