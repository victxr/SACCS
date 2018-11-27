package br.com.ufc.sacc.Model;



public class ItemConsulta {
    private static int contadorId = 0;

    private int id;

    private String uid;
    private String diaDaSemana;
    private String horario;

    private String tipo;

    public ItemConsulta(){}

    public ItemConsulta(String uid, String diaDaSemana, String horario, String tipo) {
        this.id = contadorId++;

        this.uid = uid;
        this.diaDaSemana = diaDaSemana;
        this.horario = horario;
        this.tipo = tipo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(String diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {

        return diaDaSemana+" "+horario;
    }
}
