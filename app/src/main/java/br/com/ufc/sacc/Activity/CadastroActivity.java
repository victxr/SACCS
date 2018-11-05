package br.com.ufc.sacc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import br.com.ufc.sacc.Config.Base64Custom;
import br.com.ufc.sacc.Config.Preferencias;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.Usuario;
import br.com.ufc.sacc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtCadNome;
    private EditText edtCadEmail;
    private EditText edtCadSenha;
    private EditText edtCadConfirmaSenha;
    private RadioButton rbMasculino;
    private RadioButton rbFeminino;
    private Button btnGravar;

    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        alert("Digite os dados de cadastro");

        edtCadNome = findViewById(R.id.edtCadNome);
        edtCadEmail = findViewById(R.id.edtCadEmail);
        edtCadSenha = findViewById(R.id.edtCadSenha);
        edtCadConfirmaSenha = findViewById(R.id.edtCadConfirmaSenha);
        rbMasculino = findViewById(R.id.rbMasculino);
        rbFeminino = findViewById(R.id.rbFeminino);
        btnGravar = findViewById(R.id.btnGravar);

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmaSenha(edtCadSenha.getText().toString(), edtCadConfirmaSenha.getText().toString())){
                    usuario = new Usuario();
                    usuario.setNome(edtCadNome.getText().toString());
                    usuario.setEmail(edtCadEmail.getText().toString());
                    usuario.setSenha(edtCadSenha.getText().toString());
                    usuario.setSexo(retornaSexo(rbMasculino, rbFeminino));

                    if(validarUsuario(usuario)){
                        cadastrarUsuario();
                    }
                }else{
                   alert("As senhas não correspondem");
                }
            }
        });
    }

    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        autenticacao.createUserWithEmailAndPassword(
                                                    usuario.getEmail(),
                                                    usuario.getSenha()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    alert("Usuário cadastrado com sucesso!");

                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(identificadorUsuario);
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarUsuarioPreferencias(identificadorUsuario, usuario.getNome());

                    abrirLoginUsuario();

                }else{
                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte, com pelo menos 8 caracteres com letras e números";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O email digitado é inválido, digite o novo email";
                    }catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está cadastrado, utilize outro e-mail";
                    }catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro, contacte o Administrador";
                        e.printStackTrace();
                    }
                    alert("Erro: " + erroExcecao);
                }
            }
        });
    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean confirmaSenha(String senha, String confirmaSenha) {
        if(senha.equals(confirmaSenha)){
            return true;
        }
        return false;
    }

    private String retornaSexo(RadioButton rbMasculino, RadioButton rbFeminino) {
        if(rbMasculino.isChecked()){
            return "Masculino";
        }
        return "Feminino";
    }

    public void alert(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private boolean validarUsuario(Usuario usuario) {

        if(usuario.getNome().isEmpty() || usuario.getNome() == "" || usuario.getNome() == null){
            alert("Digite seu nome para poder se cadastrar");
            return false;
        }

        if(usuario.getEmail().isEmpty() || usuario.getEmail() == "" || usuario.getEmail() == null){
            alert("Digite o email para poder se cadastrar");
            return false;
        }
        if(usuario.getSenha().isEmpty() || usuario.getSenha() == "" || usuario.getSenha() == null){
            alert("Digite uma senha para poder se cadastrar");
            return false;
        }

        return true;
    }
}
