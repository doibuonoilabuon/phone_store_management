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
    private String mode;
    private TaiKhoan currentTK;

    public TaiKhoanDialog(TaiKhoanView parentView, Frame owner, String title, boolean modal, String mode, TaiKhoan tk) {
        super(owner, title, modal);
        this.parentView = parentView;
        this.controller = new TaiKhoanController();
        this.mode = mode;
        this.currentTK = tk;
        
        initComponents();
        setupMode();
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
        // KHỞI TẠO COMBOBOX ĐÚNG CÁCH (Tránh lỗi NullPointerException)
        cbxNhomQuyen = new JComboBox<>(new String[]{
            "Quản lý kho", 
            "Nhân viên nhập hàng", 
            "Nhân viên xuất hàng"
        });
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
                
                // CÔNG THỨC DỊCH THUẬT: Vị trí 0 -> 1, Vị trí 1 -> 2, Vị trí 2 -> 3
                int nhomQuyen = cbxNhomQuyen.getSelectedIndex() + 1;

                if (mode.equals("ADD")) {
                    TaiKhoan newTK = new TaiKhoan(maNV, txtUsername.getText(), txtPassword.getText(), nhomQuyen, 1);
                    if (controller.addTaiKhoan(newTK)) {
                        JOptionPane.showMessageDialog(this, "Cấp tài khoản thành công!");
                        parentView.loadDataTable(controller.getAllList());
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cấp thất bại! Tên đăng nhập đã tồn tại hoặc Mã NV không hợp lệ.");
                    }
                } else if (mode.equals("EDIT")) {
                    // Cập nhật lại mật khẩu và nhóm quyền
                    TaiKhoan editTK = new TaiKhoan(maNV, txtUsername.getText(), txtPassword.getText(), nhomQuyen, currentTK.getTrangthai());
                    // Lưu ý: Hãy đảm bảo trong TaiKhoanController có hàm updateTaiKhoan gọi DAO nhé
                    if (DAO.TaiKhoanDAO.getInstance().update(editTK) > 0) {
                        JOptionPane.showMessageDialog(this, "Sửa tài khoản thành công!");
                        parentView.loadDataTable(controller.getAllList());
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Sửa thất bại!");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã NV phải là số nguyên!");
            }
        });
    }

    private void setupMode() {
        if (mode.equals("EDIT") && currentTK != null) {
            txtMaNV.setText(String.valueOf(currentTK.getManv()));
            txtMaNV.setEditable(false); // Khóa không cho đổi Mã NV
            
            txtUsername.setText(currentTK.getUsername());
            txtUsername.setEditable(false); // Khóa không cho đổi Tên đăng nhập
            
            txtPassword.setText(currentTK.getMatkhau());
            
            // MAP NGƯỢC TỪ DB LÊN GIAO DIỆN: Quyền 1 -> Vị trí 0; Quyền 2 -> Vị trí 1
            cbxNhomQuyen.setSelectedIndex(currentTK.getManhomquyen() - 1);
        }
    }
}