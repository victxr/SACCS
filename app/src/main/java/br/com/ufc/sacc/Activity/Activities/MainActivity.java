package br.com.ufc.sacc.Activity.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.ufc.sacc.R;

public class MainActivity extends AppCompatActivity {

    private Button btnAbrirActivityLogin;
    private Button btnAbrirActivityCadastro;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAbrirActivityLogin = findViewById(R.id.btnFazerLogin);
        btnAbrirActivityCadastro = findViewById(R.id.btnFazerCadastro);

        btnAbrirActivityLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAbrirTelaLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentAbrirTelaLogin);

            }
        });

        btnAbrirActivityCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAbrirTelaCadastro = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
                startActivity(intentAbrirTelaCadastro);
            }
        });
    }

    @Override
    protected void onResume() {
        if(getIntent().getBooleanExtra("SAIR", false)){
            finish();
            Toast.makeText(this, "Você acabou de deslogar", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        super.onResume();
    }
}
