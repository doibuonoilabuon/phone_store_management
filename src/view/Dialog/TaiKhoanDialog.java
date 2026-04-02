package view.Dialog;

import controller.TaiKhoanController;
import model.TaiKhoan;
import view.Panel.TaiKhoanView;
import javax.swing.*;
import java.awt.*;

public class TaiKhoanDialog extends JDialog {
    private JTextField txtMaNV, txtUsername, txtPassword;
    private JComboBox<String> cbxNhomQuyen;
    private JButton btnLuu, btnHuy;
    private TaiKhoanController controller;
    private TaiKhoanView parentView;

    public TaiKhoanDialog(TaiKhoanView parentView, Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        this.parentView = parentView;
        this.controller = new TaiKhoanController();
        
        initComponents();
    }

    private void initComponents() {
        setSize(400, 320);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel pnlCenter = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        pnlCenter.add(new JLabel("Mã Nhân Viên:"));
        txtMaNV = new JTextField();
        pnlCenter.add(txtMaNV);

        pnlCenter.add(new JLabel("Tên Đăng Nhập:"));
        txtUsername = new JTextField();
        pnlCenter.add(txtUsername);
        
        pnlCenter.add(new JLabel("Mật Khẩu:"));
        txtPassword = new JTextField();
        pnlCenter.add(txtPassword);

        pnlCenter.add(new JLabel("Nhóm Quyền:"));
        cbxNhomQuyen = new JComboBox<>(new String[]{"Quản lý", "Nhân viên"}); // 1: Quản lý, 2: Nhân viên
        pnlCenter.add(cbxNhomQuyen);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlBottom.add(btnLuu);
        pnlBottom.add(btnHuy);
        add(pnlBottom, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dispose());

        btnLuu.addActionListener(e -> {
            if(txtMaNV.getText().isEmpty() || txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin!");
                return;
            }

            try {
                int maNV = Integer.parseInt(txtMaNV.getText());
                int nhomQuyen = cbxNhomQuyen.getSelectedIndex() == 0 ? 1 : 2;

                TaiKhoan newTK = new TaiKhoan(maNV, txtUsername.getText(), txtPassword.getText(), nhomQuyen, 1);
                
                if (controller.addTaiKhoan(newTK)) {
                    JOptionPane.showMessageDialog(this, "Cấp tài khoản thành công!");
                    parentView.loadDataTable(controller.getAllList());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cấp thất bại! Tên đăng nhập đã tồn tại hoặc Mã NV không hợp lệ.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã NV phải là số nguyên!");
            }
        });
    }
}