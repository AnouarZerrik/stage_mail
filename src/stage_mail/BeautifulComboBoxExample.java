package stage_mail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.FlatLightLaf;

public class BeautifulComboBoxExample extends JFrame {

    public BeautifulComboBoxExample() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Beautiful ComboBox Example");
        setResizable(true);
        getContentPane().setLayout(null);

        // Set the FlatLaf look and feel
        FlatLightLaf.install();

        JComboBox<String> compteComboBox = new JComboBox<>();
        compteComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCompte = (String) compteComboBox.getSelectedItem();
                //afficher_informations_de_compte(selectedCompte);
            }
        });
        compteComboBox.setBounds(182, 48, 285, 22);
        getContentPane().add(compteComboBox);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BeautifulComboBoxExample example = new BeautifulComboBoxExample();
            example.setVisible(true);
        });
    }
}
