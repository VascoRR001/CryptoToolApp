import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.*;
import java.util.Arrays;

public class CryptoTool {

    public static void juntarArquivos(File arquivo1, File arquivo2) throws IOException {
        File outputFile = new File("output_joined.tar");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile, true)) {
            Files.copy(arquivo1.toPath(), outputStream);
            Files.copy(arquivo2.toPath(), outputStream);
        }
    }

    public static void criptografarArquivo(File inputFile, File outputFile) throws Exception {
        KeyPair keyPair = generateRSAKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        SecretKey secretKey = generateAESKey();
        IvParameterSpec iv = generateIV();

        byte[] encryptedSecretKey = encryptWithRSA(publicKey, secretKey.getEncoded());
        byte[] encryptedIV = encryptWithRSA(publicKey, iv.getIV());

        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream
                     = new FileOutputStream(outputFile)) {
            outputStream.write(encryptedSecretKey);
            outputStream.write(encryptedIV);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
                if (encryptedBytes != null) {
                    outputStream.write(encryptedBytes);
                }
            }

            byte[] finalEncryptedBytes = cipher.doFinal();
            if (finalEncryptedBytes != null) {
                outputStream.write(finalEncryptedBytes);
            }
        }
    }

    public static void descriptografarArquivo(File inputFile, File outputFile) throws Exception {
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            byte[] encryptedSecretKey = new byte[256];
            byte[] encryptedIV = new byte[256];

            inputStream.read(encryptedSecretKey);
            inputStream.read(encryptedIV);

            KeyPair keyPair = generateRSAKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();

            byte[] decryptedSecretKeyBytes = decryptWithRSA(privateKey, encryptedSecretKey);
            byte[] decryptedIVBytes = decryptWithRSA(privateKey, encryptedIV);
            SecretKey secretKey = new SecretKeySpec(decryptedSecretKeyBytes, "AES");
            IvParameterSpec iv = new IvParameterSpec(decryptedIVBytes);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
                    if (decryptedBytes != null) {
                        outputStream.write(decryptedBytes);
                    }
                }

                byte[] finalDecryptedBytes = cipher.doFinal();
                if (finalDecryptedBytes != null) {
                    outputStream.write(finalDecryptedBytes);
                }
            }
        }
    }

    private static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    private static IvParameterSpec generateIV() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private static byte[] encryptWithRSA(PublicKey publicKey, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    private static byte[] decryptWithRSA(PrivateKey privateKey, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }
}
