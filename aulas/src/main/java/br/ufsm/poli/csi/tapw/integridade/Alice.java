package br.ufsm.poli.csi.tapw.integridade;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.Arrays;

public class Alice {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        //2 Abrir o ServerSocket e aguardar a conexão
        ServerSocket ss = new ServerSocket(8080);
        Socket s = ss.accept();

        //4 Receber o arquivo com assinatura
        ObjectInputStream oin = new ObjectInputStream(s.getInputStream());
        ObjetoTroca objetoTroca = (ObjetoTroca) oin.readObject();

        //5 Descriptografar o arquivo com a chave publica
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, objetoTroca.getPublicKey());
        byte[] hashDaAssinatura = cipher.doFinal(objetoTroca.getAssinatura());

        // Gerar hash do arquivo
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(objetoTroca.getArquivo());

        // Conferir integridade do arquivo
        if (Arrays.equals(hash, hashDaAssinatura)) {
            Files.write(Path.of(objetoTroca.getNomeArquivo()), objetoTroca.getArquivo());
        } else {
            System.out.println("Assinatura do arquivo incorreta ou arquivvo não integro");
        }

        //6 Gravar o arquivo
        s.close();
        ss.close();
    }
}
