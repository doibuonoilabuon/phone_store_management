package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class InputForm extends JPanel {

    private JLabel lblTitle;
    private JTextField txtForm;
    private JPasswordField txtPass;

    // ===== TEXT FIELD =====
    public InputForm(String title) {
        this.setLayout(new GridLayout(2, 1));
        this.setBackground(Color.white);
        this.setBorder(new EmptyBorder(5, 10, 5, 10));

        lblTitle = new JLabel(title);
        txtForm = new JTextField();

        this.add(lblTitle);
        this.add(txtForm);
    }

    // ===== PASSWORD FIELD =====
    public InputForm(String title, String type) {
        this.setLayout(new GridLayout(2, 1));
        this.setBackground(Color.white);
        this.setBorder(new EmptyBorder(5, 10, 5, 10));

        lblTitle = new JLabel(title);
        txtPass = new JPasswordField();

        this.add(lblTitle);
        this.add(txtPass);
    }

    // ===== GET TEXT =====
    public String getText() {
        if (txtForm != null) {
            return txtForm.getText();
        }
        return "";
    }

    public void setText(String text) {
        if (txtForm != null) {
            txtForm.setText(text);
        }
    }

    // ===== GET PASSWORD =====
    public String getPass() {
        if (txtPass != null) {
            return new String(txtPass.getPassword());
        }
        return "";
    }

    public void setPass(String pass) {
        if (txtPass != null) {
            txtPass.setText(pass);
        }
    }

    // ===== GET COMPONENT =====
    public JTextField getTxtForm() {
        return txtForm;
    }

    public JPasswordField getTxtPass() {
        return txtPass;
    }
}