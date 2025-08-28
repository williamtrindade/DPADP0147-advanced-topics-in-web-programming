package br.ufsm.poli.csi.tapw.chavesessao;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
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
        System.out.println("chaves geradas");

        //2 Abrir o ServerSocket e aguardar a conexão
        ServerSocket ss = new ServerSocket(8080);
        Socket s = ss.accept();

        //3 Enviar a chave pública
        ObjetoTroca objetoTroca = new ObjetoTroca();
        objetoTroca.setChavePublica(keyPair.getPublic());
        ObjectOutputStream oout = new ObjectOutputStream(s.getOutputStream());
        oout.writeObject(objetoTroca);

        //4 Receber o arquivo e a chave de sessao
        ObjectInputStream oin = new ObjectInputStream(s.getInputStream());
        objetoTroca = (ObjetoTroca) oin.readObject();

        //5. Descriptografar a chave de sessao
        //transformar byte[] em SecretKey
        Cipher cipherRSA = Cipher.getInstance("RSA");
        cipherRSA.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] chaveSessao = cipherRSA.doFinal(objetoTroca.getChaveSessaoCripto());
        SecretKeySpec key = new SecretKeySpec(chaveSessao, "AES");

        //6 Descriptografar o arquivo com a chave de sessao
        Cipher cipherAES = Cipher.getInstance("AES");
        cipherAES.init(Cipher.DECRYPT_MODE, key);
        byte[] textoPlano = cipherAES.doFinal(objetoTroca.getTextoCifrado());

        //7.Gravar o arquivo
        Files.write(Path.of(objetoTroca.getNomeArquivo()), textoPlano);
        s.close();
        ss.close();
    }

}
