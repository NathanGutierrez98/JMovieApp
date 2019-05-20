package proyecto.nathan.jmovieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginUsuario extends AppCompatActivity {
    private Button btnLogin;
    private TextView txtRegistrate;
    private EditText txtUsuario;
    private EditText txtContrasenia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.loginusuario);
        txtUsuario = findViewById(R.id.txtUsuCorreo);
        txtContrasenia = findViewById(R.id.txtContrasenia);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegistrate = findViewById(R.id.txtRegistrate);
        txtRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RegistroUsuario.class);
                startActivity(i);

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtUsuario.getText().toString().equals("") && !txtContrasenia.getText().toString().equals("")){
                    ConexionBBDD cbdd = new ConexionBBDD(getApplicationContext());
                    if(cbdd.login(txtUsuario.getText().toString(),EncriptarClaves.encriptarClaves(txtContrasenia.getText().toString()))){
                        Intent i = new Intent(v.getContext(),MainActivity.class);
                        startActivity(i);
                    }
                }
            }
        });

    }
}
