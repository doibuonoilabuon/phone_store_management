package view.Dialog;

import controller.NhaCungCapController;
import model.NhaCungCap;
import view.Panel.NhaCungCapView;
import javax.swing.*;
import java.awt.*;

public class NhaCungCapDialog extends JDialog {
    private JTextField txtMaNCC, txtTenNCC, txtDiaChi, txtSDT, txtEmail;
    private JButton btnLuu, btnHuy;
    private NhaCungCapController controller;
    private NhaCungCapView parentView;
    private String mode;
    private NhaCungCap currentNCC;

    public NhaCungCapDialog(NhaCungCapView parentView, Frame owner, String title, boolean modal, String mode, NhaCungCap ncc) {
        super(owner, title, modal);
        this.parentView = parentView;
        this.mode = mode;
        this.currentNCC = ncc;
        this.controller = new NhaCungCapController();
        
        initComponents();
        setupMode();
    }

    private void initComponents() {
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel pnlCenter = new JPanel(new GridLayout(5, 2, 10, 15));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        pnlCenter.add(new JLabel("Mã NCC:"));
        txtMaNCC = new JTextField();
        pnlCenter.add(txtMaNCC);

        pnlCenter.add(new JLabel("Tên Nhà Cung Cấp:"));
        txtTenNCC = new JTextField();
        pnlCenter.add(txtTenNCC);
        
        pnlCenter.add(new JLabel("Địa Chỉ:"));
        txtDiaChi = new JTextField();
        pnlCenter.add(txtDiaChi);

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
            if (mode.equals("DETAIL")) {
                dispose();
                return;
            }
            
            if(txtMaNCC.getText().isEmpty() || txtTenNCC.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin bắt buộc!");
                return;
            }

            // Chú ý thứ tự biến truyền vào phải đúng với Model
            NhaCungCap newNCC = new NhaCungCap(
                txtMaNCC.getText(), txtTenNCC.getText(), txtDiaChi.getText(), txtSDT.getText(), txtEmail.getText()
            );

            if (mode.equals("ADD")) {
                if (controller.addNhaCungCap(newNCC)) {
                    JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
                    parentView.loadDataTable(controller.getAllList());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại! Mã NCC có thể đã tồn tại.");
                }
            } else if (mode.equals("EDIT")) {
                if (controller.updateNhaCungCap(newNCC)) {
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
            txtMaNCC.setText("");
        } else {
            txtMaNCC.setText(currentNCC.getMaNCC());
            txtMaNCC.setEditable(false);
            txtTenNCC.setText(currentNCC.getTenNCC());
            txtDiaChi.setText(currentNCC.getDiaChi());
            txtSDT.setText(currentNCC.getSoDienThoai());
            txtEmail.setText(currentNCC.getEmail());

            if (mode.equals("DETAIL")) {
                txtTenNCC.setEditable(false);
                txtDiaChi.setEditable(false);
                txtSDT.setEditable(false);
                txtEmail.setEditable(false);
                btnLuu.setText("Đóng");
                btnHuy.setVisible(false);
            }
        }
    }
}   