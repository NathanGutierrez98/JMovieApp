package proyecto.nathan.jmovieapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
                if(resultado.equals("Ingresado Correctamente")){

                    obtenerToken(usuario);
                }
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

    private void obtenerToken(Usuario usuario){
        String url = "http://www.omdbapi.com/apikey.aspx?__VIEWSTATE=%2FwEPDwUKLTIwNDY4MTIzNQ9kFgYCAQ9kFggCAQ8QDxYCHgdDaGVja2VkaGRkZGQCAw8QDxYCHwBnZGRkZAIFDxYCHgdWaXNpYmxlaGQCBw8WAh8BZ2QCAg8WAh8BZxYCAgEPDxYCHgRUZXh0BUtBIHZlcmlmaWNhdGlvbiBsaW5rIHRvIGFjdGl2YXRlIHlvdXIga2V5IHdhcyBzZW50IHRvOiBuYXRoYW5ndXQ5OEBnbWFpbC5jb21kZAIDDxYCHwFoZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAwULcGF0cmVvbkFjY3QFC3BhdHJlb25BY2N0BQhmcmVlQWNjdDckTIIITKzdw3r%2BfWhkbZE%2BtCAG4rQpx8arAzbus0ht&__VIEWSTATEGENERATOR=5E550F58&__EVENTVALIDATION=%2FwEdAAj09WVq39%2FmYiBQWH0q7TeQmSzhXfnlWWVdWIamVouVTzfZJuQDpLVS6HZFWq5fYphdL1XrNEjnC%2FKjNya%2Bmqh8hRPnM5dWgso2y7bj7kVNLSFbtYIt24Lw6ktxrd5Z67%2F4LFSTzFfbXTFN5VgQX9Nbzfg78Z8BXhXifTCAVkevd%2FNWy%2BK4CxZOz5c%2Fli8qlS7FL2aBd9SS6XAIAaB8oDW0&at=freeAcct&Email2="+usuario.getCorreo() + "&FirstName="+usuario.getNombre() +"&LastName=" + usuario.getApellidos() + "&TextArea1=Usoeducativo&Button1=Submit";


        RequestQueue q = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("e",response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );

        q.add(stringRequest);
    }
}

