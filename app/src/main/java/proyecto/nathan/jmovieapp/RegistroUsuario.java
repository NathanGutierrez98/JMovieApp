package proyecto.nathan.jmovieapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroUsuario extends AppCompatActivity {
private EditText txtNombre;
private EditText txtApellidos;
private EditText txtUsuario;
private EditText txtPass1;
private EditText txtCorreo;
private EditText txtPass2;
private Button btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtPass1 = findViewById(R.id.txtPassword);
        txtPass2 = findViewById(R.id.txtPassword2);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtUsuario = findViewById(R.id.txtUsuario);
        btnRegistro = findViewById(R.id.btnRegistro);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comprobarCampos();





            }
        });

    }

    private boolean comprobarContrasenias(){
        if(txtPass1.getText().toString().equals(txtPass2.getText().toString())){
            return true;
        }else{
            Toast.makeText(getBaseContext(), "Las contraseñas no coinciden",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void comprobarCampos(){


            if( comprobarContrasenias() && comprobarUsuario() && comprobarVacios()){
                Usuario usuario = new Usuario(txtUsuario.getText().toString(), txtNombre.getText().toString(), txtApellidos.getText().toString(), txtPass1.getText().toString(), txtCorreo.getText().toString());
                ConexionBBDD cbdd = new ConexionBBDD(getApplicationContext());
                String resultado = cbdd.guardar(usuario);
                Toast.makeText(getBaseContext(), resultado,
                        Toast.LENGTH_LONG).show();
                cbdd.close();

            }


    }

    private boolean comprobarUsuario() {

        ConexionBBDD cbdd = new ConexionBBDD(getApplicationContext());
        if (!cbdd.comprobarUsuario(txtUsuario.getText().toString())) {
            Toast.makeText(getBaseContext(), "El nombre de usuario ya existe, elija otro",
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }


    }
    private boolean comprobarVacios(){

        if (txtPass1.getText().toString().equals("") || txtPass2.getText().toString().equals("") || txtUsuario.getText().toString().equals("") || txtCorreo.getText().toString().equals("") || txtApellidos.getText().toString().equals("") || txtNombre.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "Debes rellenar todos los campos",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}

