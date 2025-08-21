package org.example;

import javax.crypto.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Bob {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 1. selecionar e ler um arquivo do SE
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            if (file.exists()) {
                byte[] textoPlano = Files.readAllBytes(file.toPath());

                // 2. gerar uma chave
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(128);
                SecretKey key = keyGen.generateKey();

                // 3. criptografar o arquivo
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] textoCifrado = cipher.doFinal(textoPlano);

                // 4. conectar com alice por socket
                Socket socket = new Socket("127.0.0.1", 8080);
                ObjetoTroca objetoTroca = new ObjetoTroca();
                objetoTroca.setTexto(textoCifrado);
                objetoTroca.setNomeArquivo("texto");
                objetoTroca.setChaveSimetrica(key);

                // 5 . Enviar o arquivo criptografado
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(objetoTroca);
                oos.flush();
                oos.close();
                socket.close();
            }
        }




    }
}
