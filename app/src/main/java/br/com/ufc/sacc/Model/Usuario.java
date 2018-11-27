package br.com.ufc.sacc.Model;

import br.com.ufc.sacc.DAO.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private String id;
    private String email;
    private String senha;
    private String nome;
    private String sexo;
    private String tipo;
    private String registro;

    public Usuario() {
    }

    public void salvar() {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuario").child(String.valueOf(getId())).setValue(this);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("nome", getNome());
        hashMapUsuario.put("sexo", getSexo());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("senha", getSenha());
        hashMapUsuario.put("tipo", getTipo());
        hashMapUsuario.put("registro", getRegistro());

        return hashMapUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }
}
