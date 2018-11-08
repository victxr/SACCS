package br.com.ufc.sacc.Model;

import java.util.HashMap;
import java.util.Map;

public class Servidor extends Usuario{

    private String siac;
    private String funcao;

    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("nome", getNome());
        hashMapUsuario.put("sexo", getSexo());
        hashMapUsuario.put("siac", getSiac());
        hashMapUsuario.put("cargo", getFuncao());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("senha", getSenha());

        return hashMapUsuario;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getSiac() {
        return siac;
    }

    public void setSiac(String siac) {
        this.siac = siac;
    }
}
