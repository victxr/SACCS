package br.com.ufc.sacc.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import br.com.ufc.sacc.Activity.Fragments.CadAlunoFragment;
import br.com.ufc.sacc.Activity.Fragments.CadServidorFragment;
import br.com.ufc.sacc.Activity.Fragments.HomeFragment;
import br.com.ufc.sacc.Config.Base64Custom;
import br.com.ufc.sacc.Config.Preferencias;
import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import br.com.ufc.sacc.Model.Aluno;
import br.com.ufc.sacc.Model.Servidor;
import br.com.ufc.sacc.Model.Usuario;
import br.com.ufc.sacc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import android.support.v4.app.Fragment;
import android.widget.Spinner;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtCadNome;
    private EditText edtCadEmail;
    private EditText edtCadSenha;
    private EditText edtCadConfirmaSenha;
    private RadioButton rbMasculino;
    private RadioButton rbFeminino;
    private Button btnGravar;
    private Button btnTipoUser;

    private EditText edtCadMatricula;
    private EditText edtCadSiac;
    private Spinner edtCadCurso;
    private Spinner edtCadFuncao;

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
        btnTipoUser = findViewById(R.id.btnTipoUsuario);


        btnTipoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(CadastroActivity.this, btnTipoUser);
                popupMenu.getMenuInflater().inflate(R.menu.dropdown_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String itemTitle = (String) item.getTitle();
                        itemTitle.toLowerCase();

                        Fragment fragment = null;
                        if (itemTitle.equalsIgnoreCase("aluno")) {
                            btnTipoUser.setText("Aluno");
                            fragment = new CadAlunoFragment();
                            edtCadCurso = fragment.getView().findViewById(R.id.edtCadCurso);
                            edtCadMatricula = fragment.getView().findViewById(R.id.edtCadMatricula);

                        } else if (itemTitle.equalsIgnoreCase("servidor")) {
                            btnTipoUser.setText("Servidor");
                            fragment = new CadServidorFragment();
                            edtCadFuncao = fragment.getView().findViewById(R.id.edtCadFuncao);
                            edtCadSiac = fragment.getView().findViewById(R.id.edtCadSiac);

                        }
                        return loadFragment(fragment);
                    }

                });
                popupMenu.show();
            }
        });

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmaSenha(edtCadSenha.getText().toString(), edtCadConfirmaSenha.getText().toString())) {
                    if(btnTipoUser.getText() == "Aluno"){
                         usuario = new Aluno();

                        usuario.setNome(edtCadNome.getText().toString());
                        usuario.setEmail(edtCadEmail.getText().toString());
                        usuario.setSenha(edtCadSenha.getText().toString());
                        usuario.setSexo(retornaSexo(rbMasculino, rbFeminino));
                        ((Aluno) usuario).setMatricula(edtCadMatricula.getText().toString());
                        ((Aluno) usuario).setCurso(edtCadCurso.getSelectedItem().toString());
                    }else{
                        usuario = new Servidor();

                        usuario.setNome(edtCadNome.getText().toString());
                        usuario.setEmail(edtCadEmail.getText().toString());
                        usuario.setSenha(edtCadSenha.getText().toString());
                        usuario.setSexo(retornaSexo(rbMasculino, rbFeminino));
                        ((Servidor) usuario).setSiac(edtCadSiac.getText().toString());
                        ((Servidor) usuario).setFuncao(edtCadFuncao.getSelectedItem().toString());
                    }

                    //mecher no validar cadastro
                    if (validarUsuario(usuario)) {
                        cadastrarUsuario();
                    }
                } else {
                    alert("As senhas não correspondem");
                }
            }
        });
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_cadastro, fragment).commit();
            return true;
        }
        return false;
    }
    private void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getAutenticacaoFirebase();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    alert("Usuário cadastrado com sucesso!");

                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(identificadorUsuario);
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarUsuarioPreferencias(identificadorUsuario, usuario.getNome());

                    abrirLoginUsuario();

                } else {
                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, com pelo menos 8 caracteres com letras e números";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O email digitado é inválido, digite o novo email";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está cadastrado, utilize outro e-mail";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro, contacte o Administrador";
                        e.printStackTrace();
                    }
                    alert("Erro: " + erroExcecao);
                }
            }
        });
    }

    public void abrirLoginUsuario() {
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean confirmaSenha(String senha, String confirmaSenha) {
        if (senha.equals(confirmaSenha)) {
            return true;
        }
        return false;
    }

    //
    private String retornaSexo(RadioButton rbMasculino, RadioButton rbFeminino) {
        if (rbMasculino.isChecked()) {
            return "Masculino";
        }
        return "Feminino";
    }

    public void alert(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private boolean validarUsuario(Usuario usuario) {

        if (usuario.getNome().isEmpty() || usuario.getNome() == "" || usuario.getNome() == null) {
            alert("Digite seu nome para poder se cadastrar");
            return false;
        }

        if (usuario.getEmail().isEmpty() || usuario.getEmail() == "" || usuario.getEmail() == null) {
            alert("Digite o email para poder se cadastrar");
            return false;
        }
        if (usuario.getSenha().isEmpty() || usuario.getSenha() == "" || usuario.getSenha() == null) {
            alert("Digite uma senha para poder se cadastrar");
            return false;
        }

        return true;
    }
}
