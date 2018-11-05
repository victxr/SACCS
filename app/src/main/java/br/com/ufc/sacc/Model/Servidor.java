package br.com.ufc.sacc.Model;

import java.util.HashMap;
import java.util.Map;

public class Servidor extends Usuario{

    private String siac;

    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("nome", getNome());
        hashMapUsuario.put("sexo", getSexo());
        hashMapUsuario.put("siac", getSiac());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("senha", getSenha());

        return hashMapUsuario;
    }

    public String getSiac() {
        return siac;
    }

    public void setSiac(String siac) {
        this.siac = siac;
    }
}
