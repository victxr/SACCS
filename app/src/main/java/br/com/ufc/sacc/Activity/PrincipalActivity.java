package br.com.ufc.sacc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import br.com.ufc.sacc.R;

public class PrincipalActivity extends AppCompatActivity {

    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btnLogout = findViewById(R.id.btnLogout);


        //comentario
        //fechar tudo ao deslogar
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("SAIR", true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
