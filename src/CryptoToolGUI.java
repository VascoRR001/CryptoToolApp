import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class CryptoToolGUI {
    private JFrame frame;
    private JButton btnJuntar;
    private JButton btnCriptografar;
    private JButton btnDescriptografar;
    private JTextField txtArquivo1;
    private JTextField txtArquivo2;
    private JButton btnSelecionarArquivo1;
    private JButton btnSelecionarArquivo2;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CryptoToolGUI window = new CryptoToolGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public CryptoToolGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("CryptoTool");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        btnJuntar = new JButton("Juntar");
        btnJuntar.setBounds(10, 228, 89, 23);
        frame.getContentPane().add(btnJuntar);

        btnCriptografar = new JButton("Criptografar");
        btnCriptografar.setBounds(109, 228, 107, 23);
        frame.getContentPane().add(btnCriptografar);

        btnDescriptografar = new JButton("Descriptografar");
        btnDescriptografar.setBounds(226, 228, 126, 23);
        frame.getContentPane().add(btnDescriptografar);

        txtArquivo1 = new JTextField();
        txtArquivo1.setBounds(10, 36, 314, 20);
        frame.getContentPane().add(txtArquivo1);
        txtArquivo1.setColumns(10);

        txtArquivo2 = new JTextField();
        txtArquivo2.setBounds(10, 88, 314, 20);
        frame.getContentPane().add(txtArquivo2);
        txtArquivo2.setColumns(10);

        btnSelecionarArquivo1 = new JButton("Selecionar Arquivo 1");
        btnSelecionarArquivo1.setBounds(10, 11, 169, 23);
        frame.getContentPane().add(btnSelecionarArquivo1);

        btnSelecionarArquivo2 = new JButton("Selecionar Arquivo 2");
        btnSelecionarArquivo2.setBounds(10, 67, 169, 23);
        frame.getContentPane().add(btnSelecionarArquivo2);

        btnSelecionarArquivo1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    txtArquivo1.setText(selectedFile.getAbsolutePath());
                }
            }
        });


        btnSelecionarArquivo2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    txtArquivo2.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        btnJuntar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File arquivo1 = new File(txtArquivo1.getText());
                File arquivo2 = new File(txtArquivo2.getText());

                try {
                    CryptoTool.juntarArquivos(arquivo1, arquivo2);
                    JOptionPane.showMessageDialog(frame, "Arquivos juntados com sucesso!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Erro ao juntar arquivos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCriptografar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File arquivo1 = new File(txtArquivo1.getText());
                File arquivo2 = new File(txtArquivo2.getText());

                try {
                    CryptoTool.criptografarArquivo(arquivo1,arquivo2);
                    JOptionPane.showMessageDialog(frame, "Arquivo criptografado com sucesso!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Erro ao criptografar arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnDescriptografar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File arquivo1 = new File(txtArquivo1.getText());
                File arquivo2 = new File(txtArquivo2.getText());

                try {
                    CryptoTool.descriptografarArquivo(arquivo1, arquivo2);
                    JOptionPane.showMessageDialog(frame, "Arquivo descriptografado com sucesso!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Erro ao descriptografar arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}


