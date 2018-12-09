package br.com.ufc.sacc.Model;


public class ItemFaq {

    private static int contadorId = 0;
    private int id;

    private String uid;
    private String pergunta;
    private String resposta;

    private String tipo;

    public ItemFaq(){}

    public ItemFaq(String uid, String pergunta, String resposta, String tipo) {
        this.id = contadorId++;

        this.uid = uid;
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.tipo = tipo;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return pergunta;
    }

    public String getFullItem(){
        return uid + "-" +
                pergunta + "-" +
                resposta;
    }
}
