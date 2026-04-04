package view.Dialog;

import controller.NhanVienController;
import model.NhanVien;
import view.Panel.NhanVienView;
import javax.swing.*;
import java.awt.*;

public class NhanVienDialog extends JDialog {
    private JTextField txtMaNV, txtTenNV, txtSDT, txtEmail;
    private JComboBox<String> cbxGioiTinh, cbxChucVu;
    private JButton btnLuu, btnHuy;
    private NhanVienController controller;
    private NhanVienView parentView;
    private String mode;
    private NhanVien currentNV;

    public NhanVienDialog(NhanVienView parentView, Frame owner, String title, boolean modal, String mode, NhanVien nv) {
        super(owner, title, modal);
        this.parentView = parentView;
        this.mode = mode;
        this.currentNV = nv;
        this.controller = new NhanVienController();
        
        initComponents();
        setupMode();
    }

    private void initComponents() {
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel pnlCenter = new JPanel(new GridLayout(6, 2, 10, 15));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        pnlCenter.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField();
        txtMaNV.setEditable(false); // Mã tự tăng nên luôn khóa
        pnlCenter.add(txtMaNV);

        pnlCenter.add(new JLabel("Tên Nhân Viên:"));
        txtTenNV = new JTextField();
        pnlCenter.add(txtTenNV);
        
        pnlCenter.add(new JLabel("Giới Tính:"));
        cbxGioiTinh = new JComboBox<>(new String[]{"Nữ", "Nam"}); // 0: Nữ, 1: Nam
        pnlCenter.add(cbxGioiTinh);

     pnlCenter.add(new JLabel("Chức Vụ:"));
        cbxChucVu = new JComboBox<>(new String[]{
            "Quản lý kho", 
            "Nhân viên nhập hàng", 
            "Nhân viên xuất hàng"
        });
        pnlCenter.add(cbxChucVu);

        pnlCenter.add(new JLabel("Số Điện Thoại:"));
        txtSDT = new JTextField();
        pnlCenter.add(txtSDT);

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
            if(txtTenNV.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhân viên!");
                return;
            }

            int gioiTinh = cbxGioiTinh.getSelectedIndex(); // 0 hoặc 1
            String chucVu = cbxChucVu.getSelectedItem().toString();

            if (mode.equals("ADD")) {
                NhanVien newNV = new NhanVien(0, txtTenNV.getText(), gioiTinh, chucVu, txtSDT.getText(), 1, txtEmail.getText());
                if (controller.addNhanVien(newNV)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    parentView.loadDataTable(controller.getAllList());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            } else if (mode.equals("EDIT")) {
                NhanVien editNV = new NhanVien(currentNV.getManv(), txtTenNV.getText(), gioiTinh, chucVu, txtSDT.getText(), 1, txtEmail.getText());
                if (controller.updateNhanVien(editNV)) {
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
            txtMaNV.setText("Tự động sinh");
        } else {
            txtMaNV.setText(String.valueOf(currentNV.getManv()));
            txtTenNV.setText(currentNV.getHoten());
            cbxGioiTinh.setSelectedIndex(currentNV.getGioitinh());
            cbxChucVu.setSelectedItem(currentNV.getChucvu());
            txtSDT.setText(currentNV.getSdt());
            txtEmail.setText(currentNV.getEmail());
        }
    }
}