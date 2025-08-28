package br.ufsm.poli.csi.tapw.assimetrica;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Alice {

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        //1 Gerar o par de chaves
        System.out.println("Gerando chaves...");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();
        System.out.println("KeyPair generated");

        //2 Abrir o ServerSocket e aguardar a conexão
        ServerSocket ss = new ServerSocket(8080);
        Socket s = ss.accept();

        //3 Enviar a chave pública
        ObjetoTroca objetoTroca = new ObjetoTroca();
        objetoTroca.setChavePublica(keyPair.getPublic());
        ObjectOutputStream oout = new ObjectOutputStream(s.getOutputStream());
        oout.writeObject(objetoTroca);

        //4 Receber o arquivo
        ObjectInputStream oin = new ObjectInputStream(s.getInputStream());
        objetoTroca = (ObjetoTroca) oin.readObject();

        //5 Descriptografar o arquivo com a chave privada
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] textoPlano = cipher.doFinal(objetoTroca.getTextoCifrado());

        //6 Gravar o arquivo
        Files.write(Path.of(objetoTroca.getNomeArquivo()), textoPlano);
        s.close();
        ss.close();
    }

}
