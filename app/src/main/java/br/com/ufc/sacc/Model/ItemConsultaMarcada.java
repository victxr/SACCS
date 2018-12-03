package br.com.ufc.sacc.Model;



public class ItemConsultaMarcada {
    private static int contadorId = 0;

    private int id;

    private String uid;
    private String marcada;

    private String tipo;
    private String motivo;


    public ItemConsultaMarcada(){}

    public ItemConsultaMarcada(String uid, String marcada, String tipo, String motivo) {
        this.id = contadorId++;

        this.uid = uid;
        this.marcada = this.marcada;
        this.tipo = tipo;
        this.motivo = motivo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
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

    public String getMarcada() {
        return marcada;
    }

    public void setMarcada(String marcada) {
        this.marcada = marcada;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {

        return marcada;
    }
}
