package br.ufsm.poli.csi.tapw.simetrica;

import javax.crypto.SecretKey;
import java.io.Serializable;

public class ObjetoTroca implements Serializable {

    private byte[] textoCifrado;
    private String nomeArquivo;
    private SecretKey chaveSimetrica;

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

    public SecretKey getChaveSimetrica() {
        return chaveSimetrica;
    }

    public void setChaveSimetrica(SecretKey chaveSimetrica) {
        this.chaveSimetrica = chaveSimetrica;
    }
}
