package org.example;

import javax.crypto.SecretKey;
import java.io.Serializable;

public class ObjetoTroca implements Serializable {
    private byte[] texto;
    private String nomeArquivo;
    private SecretKey chaveSimetrica;

    public byte[] getTexto() {
        return texto;
    }

    public void setTexto(byte[] texto) {
        this.texto = texto;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public SecretKey getChaveSimetrica() {
        return chaveSimetrica;
    }

    public void setChaveSimetrica(SecretKey chaveSimetrica) {
        this.chaveSimetrica = chaveSimetrica;
    }
}
