package view;



import javax.swing.*;
import java.awt.*;

    public class MainFrame extends JFrame {

        public MainFrame() {
            setTitle("Phone Store Management System");
            setSize(1000,600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JMenuBar menuBar = new JMenuBar();

            JMenu productMenu = new JMenu("Products");
            JMenu customerMenu = new JMenu("Customers");
            JMenu invoiceMenu = new JMenu("Invoices");

            menuBar.add(productMenu);
            menuBar.add(customerMenu);
            menuBar.add(invoiceMenu);

            setJMenuBar(menuBar);

            JLabel label = new JLabel("PHONE STORE MANAGEMENT", JLabel.CENTER);
            label.setFont(new Font("Arial",Font.BOLD,24));

            add(label);
        }

        public static void main(String[] args) {
            new MainFrame().setVisible(true);
        }
    }

