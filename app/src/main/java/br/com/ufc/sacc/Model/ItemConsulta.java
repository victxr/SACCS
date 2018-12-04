package br.com.ufc.sacc.Model;



public class ItemConsulta {


    private String uid;
    private String diaDaSemana;
    private String horario;

    private String tipo;

    public ItemConsulta(){}

    public ItemConsulta(String uid, String diaDaSemana, String horario, String tipo) {

        this.uid = uid;
        this.diaDaSemana = diaDaSemana;
        this.horario = horario;
        this.tipo = tipo;
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
