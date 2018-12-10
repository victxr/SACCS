package br.com.ufc.sacc.Activity.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.Usuario;
import br.com.ufc.sacc.R;
import br.com.ufc.sacc.ServicesBroadcasts.ServiceVerificaWifi;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnLogar;
    private LoginButton btnLogarFacebook;
    private TextView tvAbreCadastro;
    private CallbackManager callbackManager;
    private SignInButton btnLogarGoogle;

    private FirebaseAuth autenticacao;
    private GoogleApiClient googleApiClient;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificaWifi();

        alert("Digite os dados de login");

        inicializarComponentes();

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificaWifi();
                if(validarCampos(edtEmail.getText().toString(), edtSenha.getText().toString())){
                    usuario = new Usuario();
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setSenha(edtSenha.getText().toString());
                    validarLogin();
                }
            }
        });

        tvAbreCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaCadastroUsuario();
            }
        });

        //inicializando e clicando no botão de logar com facebook
        inicializarComponenteFacebook();
        inicializarFirebaseCallBack();
        clickButtonFacebook();

        //inicializando e clicando no botão de logar com google
        inicializarComponentesGoogle();
        inicializarFirebaseParaGoogle();
        conectarGoogleApi();
        clickButtonGoogle();
    }

    private void verificaWifi() {
        Intent intent = new Intent(LoginActivity.this, ServiceVerificaWifi.class);
        startService(intent);
    }

    private void clickButtonGoogle() {
        btnLogarGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificaWifi();
                signIn();
            }
        });
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, 1);
//        alert("Login efetuado. Bem vindo ao SACCS");
//        Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
//        startActivity(intent);
    }

    private void conectarGoogleApi() {
        GoogleSignInOptions googleOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleOptions)
                .build();
    }

    private void inicializarFirebaseParaGoogle() {
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
    }

    private void inicializarComponentesGoogle() {
        btnLogarGoogle = findViewById(R.id.btnLogarGoogle);
    }

    private void clickButtonFacebook() {
        btnLogarFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseLoginFacebook(loginResult.getAccessToken());
            }


            @Override
            public void onCancel() {
                alert("Operação cancelada");
            }

            @Override
            public void onError(FacebookException error) {
                alert("Erro ao logar com o facebook, tente novamente");
            }
        });
    }

    private void firebaseLoginFacebook(AccessToken accessToken) {
        AuthCredential credencial = FacebookAuthProvider.getCredential(accessToken.getToken());
        autenticacao.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                }else{
                    alert("Erro de autenticação com o Servidor.");
                }
            }
        });
    }

    private void inicializarFirebaseCallBack() {
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        callbackManager = CallbackManager.Factory.create();
        //criar uma transposição para o Singleton depois
    }

    private void inicializarComponenteFacebook() {
        btnLogarFacebook = findViewById(R.id.btnLogarFacebook);
        btnLogarFacebook.setReadPermissions("email", "public_profile");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //diz respeito call do facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //diz respeito ao signin do google
        if(requestCode == 1){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount acount = result.getSignInAccount();
                firebaseLoginGoogle(acount);
            }
        }
    }

    private void firebaseLoginGoogle(GoogleSignInAccount acount) {
        AuthCredential credencialGoogle = GoogleAuthProvider.getCredential(acount.getIdToken(), null);
        autenticacao.signInWithCredential(credencialGoogle)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, PerfilActivity.class);
                    startActivity(intent);
                }else{
                    alert("Falha na autenticação com Google");
                }
                }
            });
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();

        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                alert("Login efetuado. Bem vindo ao SACCS");
                abrirTelaPrincipal();
            }
            }
        });
    }

    private boolean validarCampos(String email, String senha){
        if((email == "" || email == null || email.equals("")) && (senha == "" || senha == null || senha.equals(""))){
            Toast.makeText(LoginActivity.this, "Digite o email e a senha para prosseguir", Toast.LENGTH_SHORT).show();
            return false;

        }else if(email == "" || email == null || email.equals("")){
            Toast.makeText(LoginActivity.this, "Digite o email para prosseguir", Toast.LENGTH_SHORT).show();
            return false;

        }else if(senha == "" || senha == null || senha.equals("")){
            Toast.makeText(LoginActivity.this, "Digite a senha para prosseguir", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void abrirTelaPrincipal(){
        Intent intentAbrirTelaPrincipal = new Intent(LoginActivity.this, PrincipalActivity.class);
        startActivity(intentAbrirTelaPrincipal);
        onBackPressed();
    }

    public void abrirTelaCadastroUsuario(){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    private void alert(String operação_cancelada) {
        Toast.makeText(LoginActivity.this, operação_cancelada, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        alert("Falha na conexão");
    }

    private void inicializarComponentes() {
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogar = findViewById(R.id.btnLogar);
        tvAbreCadastro = findViewById(R.id.tvAbreCadastro);
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Toast.makeText(context, "Por favor, ative seu wifi. O SACCS precisa de" +
                                                " acesso a internet para funcionar completamente.", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        registerReceiver(br, new IntentFilter("Wifi"));
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        unregisterReceiver(br);
    }
}
