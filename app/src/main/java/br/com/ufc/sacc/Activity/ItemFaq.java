package br.com.ufc.sacc.Activity;

public class ItemFaq {
    private static int contadorId = 0;

    private int id;
    private String pergunta;
    private String resposta;

    public ItemFaq(){}

    public ItemFaq(String pergunta, String resposta) {
        this.id =contadorId++;

        this.pergunta = pergunta;
        this.resposta = resposta;
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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return pergunta;
    }

    public String getFullItem(){
        return id + "-" +
                pergunta + "-" +
                resposta;
    }
}
