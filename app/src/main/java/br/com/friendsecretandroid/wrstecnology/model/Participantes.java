package br.com.friendsecretandroid.wrstecnology.model;

public class Participantes {
    private String nome;
    private String telefone;

    public Participantes (String nome, String telefone){
        this.nome = nome;
        this.telefone = telefone;
    }

    public Participantes (){
        this.nome = "";
        this.telefone = "";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}

