package view.Dialog;

import controller.TaiKhoanController;
import model.TaiKhoan;
import view.Panel.TaiKhoanView;
import javax.swing.*;
import java.awt.*;

public class DoiMatKhauDialog extends JDialog {
    private JTextField txtNewPass;
    private JButton btnLuu, btnHuy;
    private TaiKhoanController controller;
    private TaiKhoanView parentView;
    private TaiKhoan currentTK;

    public DoiMatKhauDialog(TaiKhoanView parentView, Frame owner, String title, boolean modal, TaiKhoan tk) {
        super(owner, title, modal);
        this.parentView = parentView;
        this.currentTK = tk;
        this.controller = new TaiKhoanController();
        
        initComponents();
    }

    private void initComponents() {
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel pnlCenter = new JPanel(new GridLayout(2, 1, 10, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        pnlCenter.add(new JLabel("Nhập mật khẩu mới cho: " + currentTK.getUsername()));
        txtNewPass = new JTextField();
        pnlCenter.add(txtNewPass);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlBottom.add(btnLuu);
        pnlBottom.add(btnHuy);
        add(pnlBottom, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dispose());

        btnLuu.addActionListener(e -> {
            if(txtNewPass.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống!");
                return;
            }
            currentTK.setMatkhau(txtNewPass.getText());
            if (controller.updateTaiKhoan(currentTK)) {
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
                parentView.loadDataTable(controller.getAllList());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại!");
            }
        });
    }
}