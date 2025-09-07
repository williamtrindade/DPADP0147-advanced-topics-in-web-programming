package br.ufsm.poli.csi.tapw.assimetrica;

import java.io.Serializable;
import java.security.PublicKey;

public class ObjetoTroca implements Serializable {

    private PublicKey chavePublica;
    private byte[] textoCifrado;
    private String nomeArquivo;

    public PublicKey getChavePublica() {
        return chavePublica;
    }

    public void setChavePublica(PublicKey chavePublica) {
        this.chavePublica = chavePublica;
    }

    public byte[] getTextoCifrado() {
        return textoCifrado;
    }

    public void setTextoCifrado(byte[] textoCifrado) {
        this.textoCifrado = textoCifrado;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
}