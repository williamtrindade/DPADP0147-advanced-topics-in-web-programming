package br.ufsm.poli.csi.tapw.chavesessao;

import javax.crypto.*;
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

                KeyGenerator keyGeneratorAES = KeyGenerator.getInstance("AES");
                keyGeneratorAES.init(128);
                SecretKey secretKey = keyGeneratorAES.generateKey();

                //4 Criptografa o arquivo com a chave simétrica
                Cipher cipherAES = Cipher.getInstance("AES");
                cipherAES.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] arquivoCriptografado = cipherAES.doFinal(textoPlano);

                //5.criptografar a chave de sessao
                Cipher cipherRSA = Cipher.getInstance("RSA");
                cipherRSA.init(Cipher.ENCRYPT_MODE, objetoTroca.getChavePublica());
                byte[] chaveSessaoCripto = cipherRSA.doFinal(secretKey.getEncoded());

                //6 Envia o arquivo
                objetoTroca = new ObjetoTroca();
                objetoTroca.setNomeArquivo(file.getName());
                objetoTroca.setChaveSessaoCripto(chaveSessaoCripto);
                objetoTroca.setTextoCifrado(arquivoCriptografado);
                ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
                saida.writeObject(objetoTroca);
                saida.close();
                socket.close();
            }
        }
    }
}
