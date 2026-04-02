package view.Dialog;

import controller.SanPhamController;
import model.SanPham;
import view.Panel.SanPhamView;
import javax.swing.*;
import java.awt.*;

public class SanPhamDialog extends JDialog {
    private JTextField txtMaSP, txtTenSP, txtThuongHieu, txtDonGia, txtSoLuongTon, txtMauSac, txtDungLuong, txtRam;
    private JButton btnLuu, btnHuy;
    private SanPhamController controller;
    private SanPhamView parentView;
    private String mode;
    private SanPham currentSP;

    public SanPhamDialog(SanPhamView parentView, Frame owner, String title, boolean modal, String mode, SanPham sp) {
        super(owner, title, modal);
        this.parentView = parentView;
        this.mode = mode;
        this.currentSP = sp;
        this.controller = new SanPhamController();
        
        initComponents();
        setupMode();
    }

    private void initComponents() {
        setSize(450, 480); // Nới rộng form một chút cho đủ 8 cột
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel pnlCenter = new JPanel(new GridLayout(8, 2, 10, 15));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        pnlCenter.add(new JLabel("Mã SP:"));
        txtMaSP = new JTextField();
        pnlCenter.add(txtMaSP);

        pnlCenter.add(new JLabel("Tên Sản Phẩm:"));
        txtTenSP = new JTextField();
        pnlCenter.add(txtTenSP);

        pnlCenter.add(new JLabel("Thương Hiệu:"));
        txtThuongHieu = new JTextField();
        pnlCenter.add(txtThuongHieu);

        pnlCenter.add(new JLabel("Đơn Giá (VNĐ):"));
        txtDonGia = new JTextField();
        pnlCenter.add(txtDonGia);

        pnlCenter.add(new JLabel("Số Lượng Tồn:"));
        txtSoLuongTon = new JTextField();
        pnlCenter.add(txtSoLuongTon);

        pnlCenter.add(new JLabel("Màu Sắc:"));
        txtMauSac = new JTextField();
        pnlCenter.add(txtMauSac);

        pnlCenter.add(new JLabel("Dung Lượng:"));
        txtDungLuong = new JTextField();
        pnlCenter.add(txtDungLuong);

        pnlCenter.add(new JLabel("RAM:"));
        txtRam = new JTextField();
        pnlCenter.add(txtRam);

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
            
            // Validate để không bị sập chương trình khi để trống
            if(txtMaSP.getText().isEmpty() || txtTenSP.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin bắt buộc!");
                return;
            }

            try {
                // Ép kiểu chữ sang số cho Đơn giá và Số lượng
                double donGia = Double.parseDouble(txtDonGia.getText());
                int soLuong = Integer.parseInt(txtSoLuongTon.getText());

                SanPham newSP = new SanPham(
                    txtMaSP.getText(), txtTenSP.getText(), txtThuongHieu.getText(),
                    donGia, soLuong, txtMauSac.getText(), txtDungLuong.getText(), txtRam.getText()
                );

                if (mode.equals("ADD")) {
                    if (controller.addSanPham(newSP)) {
                        JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                        parentView.loadDataTable(controller.getAllList()); // Refresh lại bảng
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm thất bại! Mã SP có thể đã tồn tại.");
                    }
                } else if (mode.equals("EDIT")) {
                    if (controller.updateSanPham(newSP)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                        parentView.loadDataTable(controller.getAllList()); // Refresh lại bảng
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                    }
                }
            } catch (NumberFormatException ex) {
                // Nếu người dùng nhập chữ vào ô số điện thoại hoặc tiền, máy sẽ báo lỗi này
                JOptionPane.showMessageDialog(this, "Vui lòng nhập định dạng SỐ hợp lệ cho Đơn Giá và Số Lượng!");
            }
        });
    }

    private void setupMode() {
        if (mode.equals("ADD")) {
            txtMaSP.setText("");
        } else {
            txtMaSP.setText(currentSP.getMaSP());
            txtMaSP.setEditable(false); // Sửa thì không được đổi Mã SP
            txtTenSP.setText(currentSP.getTenSP());
            txtThuongHieu.setText(currentSP.getThuongHieu());
            // Trả số tiền về dạng không có dấy phẩy (vd: 15000000) để không bị lỗi khi Lưu lại
            txtDonGia.setText(String.format("%.0f", currentSP.getDonGia())); 
            txtSoLuongTon.setText(String.valueOf(currentSP.getSoLuongTon()));
            txtMauSac.setText(currentSP.getMauSac());
            txtDungLuong.setText(currentSP.getDungLuong());
            txtRam.setText(currentSP.getRam());

            if (mode.equals("DETAIL")) {
                txtTenSP.setEditable(false);
                txtThuongHieu.setEditable(false);
                txtDonGia.setEditable(false);
                txtSoLuongTon.setEditable(false);
                txtMauSac.setEditable(false);
                txtDungLuong.setEditable(false);
                txtRam.setEditable(false);
                btnLuu.setText("Đóng");
                btnHuy.setVisible(false);
            }
        }
    }
}