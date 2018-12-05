package br.com.ufc.sacc.Activity.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PerfilActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView ivFoto;
    private TextView txtEmail, txtId;
    private Button btnDeslogarGoogle;

    private FirebaseAuth autenticacao;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        
        inicializarComponentes();
        inciializarFirebase();
        conexaoGoogleApi();
        clickButtonGoogle();
    }

    private void clickButtonGoogle() {
        btnDeslogarGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deslogar();
            }
        });
    }

    private void deslogar() {
        autenticacao.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                alert("Conta deslogada");
                finish();
            }
        });
    }

    private void conexaoGoogleApi() {
        GoogleSignInOptions googleOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleOptions)
                .build();
    }

    private void inciializarFirebase() {
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    exibirDados(firebaseUser);
                }else{
                    finish();
                }
            }
        };
    }

    private void exibirDados(FirebaseUser firebaseUser) {
        txtEmail.setText(firebaseUser.getEmail());
        txtId.setText(firebaseUser.getUid());

        Glide.with(PerfilActivity.this).load(firebaseUser.getPhotoUrl()).into(ivFoto);
    }

    private void inicializarComponentes() {

        //depois adiciona as opções do google no logou da principal
        redirecionaPrincipal();

        ivFoto = findViewById(R.id.ivFoto);
        txtEmail = findViewById(R.id.txtEmail);
        txtId = findViewById(R.id.txtId);
        btnDeslogarGoogle = findViewById(R.id.btnDeslogarGoogle);
    }

    private void redirecionaPrincipal() {
        alert("Login efetuado. Bem vindo ao SACCS");
        Intent intent = new Intent(PerfilActivity.this, PrincipalActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        autenticacao.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        autenticacao.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        alert("Falha na conexão");
    }

    private void alert(String mensagem) {
        Toast.makeText(PerfilActivity.this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
