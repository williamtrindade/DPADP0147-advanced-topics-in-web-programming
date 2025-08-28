package br.ufsm.poli.csi.tapw.simetrica;

import javax.crypto.*;
import javax.swing.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Bob {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //1 Selecionar e ler um arquivo do sistema de arquivos
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            if (file.exists()) {
                byte[] textoPlano = Files.readAllBytes(file.toPath());
                //2 Gerar uma chave
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(256);
                SecretKey key = kgen.generateKey();
                //3 Criptografar o arquivo
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] textoCifrado = cipher.doFinal(textoPlano);
                //4 Conectar com a Alice por socket
                Socket socket = new Socket("127.0.0.1", 8080);
                ObjetoTroca objetoTroca = new ObjetoTroca();
                objetoTroca.setTextoCifrado(textoCifrado);
                objetoTroca.setNomeArquivo(file.getName());
                objetoTroca.setChaveSimetrica(key);
                //5 Envia o arquivo criptografado
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(objetoTroca);
                oos.flush();
                oos.close();
                socket.close();
            }
        }
    }

}
