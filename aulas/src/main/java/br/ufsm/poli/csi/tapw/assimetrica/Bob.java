package br.ufsm.poli.csi.tapw.assimetrica;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Bob {

    public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException {
        //1 Selecionar o arquivo
        //1 Selecionar e ler um arquivo do sistema de arquivos
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            if (file.exists()) {
                byte[] textoPlano = Files.readAllBytes(file.toPath());
                //2 Conectar na Alice
                Socket socket = new Socket("127.0.0.1", 8080);
                //3 Receber a chave publica
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                ObjetoTroca objetoTroca = (ObjetoTroca) entrada.readObject();
                //4 Criptografa o arquivo com a chave pública
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, objetoTroca.getChavePublica());
                byte[] arquivoCriptografado = cipher.doFinal(textoPlano);
                //5 Envia o arquivo
                objetoTroca = new ObjetoTroca();
                objetoTroca.setNomeArquivo(file.getName());
                objetoTroca.setTextoCifrado(arquivoCriptografado);
                ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
                saida.writeObject(objetoTroca);
                saida.close();
                socket.close();
            }
        }

    }

}
