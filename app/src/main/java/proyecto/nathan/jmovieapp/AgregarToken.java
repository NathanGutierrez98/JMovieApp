package proyecto.nathan.jmovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AgregarToken extends AppCompatActivity {
    private Button btnAñadir;
    private EditText edToken;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_token);
        edToken = findViewById(R.id.txtToken);
        ConexionBBDD cbd = new ConexionBBDD(getApplicationContext());
        final String correoUsuario[] =  getIntent().getStringArrayExtra("nombreUsuario");
        String token = "";
        token =""+cbd.getToken(correoUsuario[1]);

        if(!token.equals("") && !token.equals("null")){
            Toast.makeText(getApplicationContext(),"Ya tienes una API key enlazada a esta cuenta", Toast.LENGTH_SHORT).show();
        }
        btnAñadir = findViewById(R.id.btnToken);
        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConexionBBDD cbd = new ConexionBBDD(v.getContext());

                String mensaje = cbd.agregarToken(correoUsuario[1],edToken.getText().toString());
                Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_SHORT).show();;
                if(mensaje.equals("API KEY añadida correctamente")){
                    try {
                        Thread.sleep(1000);
                        Intent i  = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("nombreUsuario",correoUsuario);
                        startActivity(i);
                    } catch (InterruptedException e) {

                    }

                }


            }
        });

        }

}
