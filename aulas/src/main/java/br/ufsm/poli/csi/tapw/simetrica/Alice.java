package br.ufsm.poli.csi.tapw.simetrica;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Alice {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        //1 abrir o ServerSocket
        ServerSocket ss = new ServerSocket(8080);
        Socket s = ss.accept();
        //2 receber o arquivo troca
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        ObjetoTroca objetoTroca = (ObjetoTroca) ois.readObject();
        //3 decifrar o arquivo
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, objetoTroca.getChaveSimetrica());
        byte[] textoPlano = cipher.doFinal(objetoTroca.getTextoCifrado());
        //4 escrever o arquivo
        Files.write(Path.of(objetoTroca.getNomeArquivo()), textoPlano);
        s.close();
        ss.close();
    }

}
