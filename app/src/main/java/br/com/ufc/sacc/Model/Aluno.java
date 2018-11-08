package br.com.ufc.sacc.Model;

import java.util.HashMap;
import java.util.Map;

public class Aluno extends Usuario{

    private String curso;
    private String matricula;

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("nome", getNome());
        hashMapUsuario.put("sexo", getSexo());
        hashMapUsuario.put("curso", getCurso());
        hashMapUsuario.put("matricula", getMatricula());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("senha", getSenha());

        return hashMapUsuario;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }


    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
