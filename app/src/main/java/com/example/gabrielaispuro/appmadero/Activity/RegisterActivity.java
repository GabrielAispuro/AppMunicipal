package com.example.gabrielaispuro.appmadero.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gabrielaispuro.appmadero.R;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class RegisterActivity extends ActionBarActivity {

    private EditText email;
    private EditText password;
    private EditText password2;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Registro");

        email = (EditText) findViewById(R.id.editText_email);
        password = (EditText) findViewById(R.id.editText_password);
        password2 = (EditText) findViewById(R.id.editText_password2);
        save = (Button) findViewById(R.id.button_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveData()){
                    finish();
                }
            }
        });

    }

    public Boolean saveData(){

        String mail = email.getText().toString();
        if(mail.trim().equals("")){
            Toast.makeText(this, "¡Favor de llenar el campo de email!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String pass = password.getText().toString();
        if(mail.trim().equals("")){
            Toast.makeText(this, "¡Favor de llenar el primer campo de contraseña!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if(!mail.contains("@")){
            Toast.makeText(this, "¡Favor de introducir un correo valido!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String pass2 = password2.getText().toString();
        if(mail.trim().equals("")){
            Toast.makeText(this, "¡Favor de llenar el segunto campo de contraseña!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!pass.equals(pass2)){
            Toast.makeText(this, "¡Una contraseña no coincide!",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            ParseObject user = new ParseObject("Usuario");
            user.put("email", mail);
            user.put("password", pass);

            Toast.makeText(this, "Guardando..........",
                    Toast.LENGTH_SHORT).show();
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(RegisterActivity.this, "¡Te has registrado exitosamente!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "¡Error al intentar registrarse!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
