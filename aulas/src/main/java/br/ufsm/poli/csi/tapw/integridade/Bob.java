package br.ufsm.poli.csi.tapw.integridade;

import javax.crypto.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.security.*;

public class Bob {

    public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // Arquivo
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Gerar par de chaves
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            if (file.exists()) {
                // texto byte do arquivo
                byte[] textoPlano = Files.readAllBytes(file.toPath());

                //2 Conectar na Alice
                Socket socket = new Socket("127.0.0.1", 8080);

                // Gerar hash do arquivo
                byte[] hash = md.digest(textoPlano);

                // Criptografar o hash do arquivo com a chave privada
                Cipher cipherHash = Cipher.getInstance("RSA");
                cipherHash.init(Cipher.ENCRYPT_MODE, kp.getPrivate());
                byte[] assinatura = cipherHash.doFinal(hash);

                //5 Envia o arquivo
                ObjetoTroca objetoTroca = new ObjetoTroca();
                objetoTroca.setNomeArquivo(file.getName());
                objetoTroca.setArquivo(textoPlano);
                objetoTroca.setPublicKey(kp.getPublic());
                objetoTroca.setAssinatura(assinatura);

                ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
                saida.writeObject(objetoTroca);
                saida.close();
                socket.close();
            }
        }

    }
}
