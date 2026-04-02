package view.Dialog;

import controller.KhachHangController;
import model.KhachHang;
import view.Panel.KhachHangView;
import javax.swing.*;
import java.awt.*;

public class KhachHangDialog extends JDialog {
    private JTextField txtMaKH, txtTenKH, txtSDT, txtDiaChi, txtEmail;
    private JButton btnLuu, btnHuy;
    private KhachHangController controller;
    private KhachHangView parentView;
    private String mode; 
    private KhachHang currentKH;

    public KhachHangDialog(KhachHangView parentView, Frame owner, String title, boolean modal, String mode, KhachHang kh) {
        super(owner, title, modal);
        this.parentView = parentView;
        this.mode = mode;
        this.currentKH = kh;
        this.controller = new KhachHangController();
        
        initComponents();
        setupMode();
    }

    private void initComponents() {
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel pnlCenter = new JPanel(new GridLayout(5, 2, 10, 15));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        pnlCenter.add(new JLabel("Mã KH:"));
        txtMaKH = new JTextField();
        pnlCenter.add(txtMaKH);

        pnlCenter.add(new JLabel("Tên Khách Hàng:"));
        txtTenKH = new JTextField();
        pnlCenter.add(txtTenKH);

        pnlCenter.add(new JLabel("Số Điện Thoại:"));
        txtSDT = new JTextField();
        pnlCenter.add(txtSDT);

        pnlCenter.add(new JLabel("Địa Chỉ:"));
        txtDiaChi = new JTextField();
        pnlCenter.add(txtDiaChi);

        pnlCenter.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        pnlCenter.add(txtEmail);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlBottom.add(btnLuu);
        pnlBottom.add(btnHuy);
        add(pnlBottom, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dispose());

        btnLuu.addActionListener(e -> {
            if (mode.equals("DETAIL")) {
                dispose();
                return;
            }
            
            // Validate sơ bộ
            if(txtMaKH.getText().isEmpty() || txtTenKH.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin bắt buộc!");
                return;
            }

            KhachHang newKH = new KhachHang(
                txtMaKH.getText(), txtTenKH.getText(), txtSDT.getText(), txtDiaChi.getText(), txtEmail.getText()
            );

            if (mode.equals("ADD")) {
                if (controller.addKhachHang(newKH)) {
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                    parentView.loadDataTable(controller.getAllList()); 
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại! Vui lòng kiểm tra lại (Mã KH có thể bị trùng).");
                }
            } else if (mode.equals("EDIT")) {
                if (controller.updateKhachHang(newKH)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    parentView.loadDataTable(controller.getAllList());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            }
        });
    }

    private void setupMode() {
        if (mode.equals("ADD")) {
            txtMaKH.setText("");
        } else {
            txtMaKH.setText(currentKH.getMaKH());
            txtMaKH.setEditable(false); 
            txtTenKH.setText(currentKH.getTenKH());
            txtSDT.setText(currentKH.getSoDienThoai());
            txtDiaChi.setText(currentKH.getDiaChi());
            txtEmail.setText(currentKH.getEmail());

            if (mode.equals("DETAIL")) {
                txtTenKH.setEditable(false);
                txtSDT.setEditable(false);
                txtDiaChi.setEditable(false);
                txtEmail.setEditable(false);
                btnLuu.setText("Đóng");
                btnHuy.setVisible(false);
            }
        }
    }
}