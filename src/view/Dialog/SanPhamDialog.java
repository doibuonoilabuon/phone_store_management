package view.Dialog;

import controller.SanPhamController;
import model.SanPham;
import view.Panel.SanPhamView;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SanPhamDialog extends JDialog {
    private JTextField txtMaSP, txtTenSP, txtThuongHieu, txtDonGia, txtSoLuongTon, txtMauSac, txtDungLuong, txtRam;
    private JButton btnLuu, btnHuy, btnChonAnh;
    private JLabel lblHinhAnh;
    private SanPhamController controller;
    private SanPhamView parentView;
    private String mode;
    private SanPham currentSP;
    
    // Biến lưu tên file ảnh để tẹo nữa nhét vào Database
    private String tenFileAnh = "default.png"; 

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
        setSize(750, 480); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // ==========================================
        // 1. NỬA BÊN TRÁI: KHUNG CHỨA ẢNH
        // ==========================================
        JPanel pnlLeft = new JPanel(new BorderLayout(0, 10));
        pnlLeft.setPreferredSize(new Dimension(280, 0));
        pnlLeft.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));

        lblHinhAnh = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblHinhAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnlLeft.add(lblHinhAnh, BorderLayout.CENTER);

        btnChonAnh = new JButton("Chọn Ảnh Mới");
        btnChonAnh.addActionListener(e -> chonAnhTuMayTinh());
        pnlLeft.add(btnChonAnh, BorderLayout.SOUTH);

        add(pnlLeft, BorderLayout.WEST);

        // ==========================================
        // 2. NỬA BÊN PHẢI: KHUNG THÔNG TIN
        // ==========================================
        JPanel pnlCenter = new JPanel(new GridLayout(8, 2, 10, 15));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 30));

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

        // ==========================================
        // 3. NÚT CHỨC NĂNG BÊN DƯỚI
        // ==========================================
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
            
            if(txtMaSP.getText().isEmpty() || txtTenSP.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin bắt buộc!");
                return;
            }

            try {
                double donGia = Double.parseDouble(txtDonGia.getText());
                int soLuong = Integer.parseInt(txtSoLuongTon.getText());

                SanPham newSP = new SanPham(
                    txtMaSP.getText(), txtTenSP.getText(), txtThuongHieu.getText(),
                    donGia, soLuong, txtMauSac.getText(), txtDungLuong.getText(), txtRam.getText(), tenFileAnh
                );

                if (mode.equals("ADD")) {
                    if (controller.addSanPham(newSP)) {
                        JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                        parentView.loadDataTable(controller.getAllList());
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm thất bại! Mã SP có thể đã tồn tại.");
                    }
                } else if (mode.equals("EDIT")) {
                    if (controller.updateSanPham(newSP)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                        parentView.loadDataTable(controller.getAllList());
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập định dạng SỐ hợp lệ cho Đơn Giá và Số Lượng!");
            }
        });
    }

    // ==========================================
    // HÀM XỬ LÝ ẢNH CHUYÊN DỤNG
    // ==========================================
    private void chonAnhTuMayTinh() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh (JPG, PNG)", "jpg", "png", "jpeg"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String newFileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                File destFile = new File("src/img_product/" + newFileName); // Đã sửa tên thư mục
                
                if(!destFile.getParentFile().exists()) destFile.getParentFile().mkdirs();
                
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                loadHinhAnhLable(newFileName);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi khi sao chép ảnh!");
            }
        }
    }

private void loadHinhAnhLable(String fileName) {
        this.tenFileAnh = fileName;
        try {
            File file = new File("src/img_product/" + fileName); 
            if (!file.exists()) file = new File("src/img_product/default.png");
            
            // Dùng ImageIO đọc file sẽ an toàn và lấy kích thước chuẩn 100%
            BufferedImage originalImage = ImageIO.read(file);
            if (originalImage == null) throw new Exception("Không đọc được ảnh");

            int orgWidth = originalImage.getWidth();
            int orgHeight = originalImage.getHeight();
            
            // Khung chứa của sếp hiện tại là Hình Chữ Nhật Đứng!
            int maxWidth = 240;  // Rộng tối đa
            int maxHeight = 340; // Cao tối đa

            // Thuật toán: Co dãn lấy tỷ lệ tốt nhất để lấp đầy khung mà không bị méo
            double scale = Math.min((double) maxWidth / orgWidth, (double) maxHeight / orgHeight);
            int newWidth = (int) (orgWidth * scale);
            int newHeight = (int) (orgHeight * scale);

            // Khởi tạo bộ nhớ đồ họa và áp dụng thuật toán làm nét ảnh (Bicubic)
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImage.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Vẽ ảnh ra
            g2.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g2.dispose(); // Giải phóng RAM

            lblHinhAnh.setIcon(new ImageIcon(resizedImage));
            lblHinhAnh.setText(""); 
        } catch (Exception e) {
            e.printStackTrace();
            lblHinhAnh.setIcon(null);
            lblHinhAnh.setText("Lỗi hiển thị ảnh");
        }
    }
    private void setupMode() {
        if (mode.equals("ADD")) {
            txtMaSP.setText("");
            loadHinhAnhLable("default.png"); 
        } else {
            txtMaSP.setText(currentSP.getMaSP());
            txtMaSP.setEditable(false);
            txtTenSP.setText(currentSP.getTenSP());
            txtThuongHieu.setText(currentSP.getThuongHieu());
            txtDonGia.setText(String.format("%.0f", currentSP.getDonGia())); 
            txtSoLuongTon.setText(String.valueOf(currentSP.getSoLuongTon()));
            txtMauSac.setText(currentSP.getMauSac());
            txtDungLuong.setText(currentSP.getDungLuong());
            txtRam.setText(currentSP.getRam());

            String anhSP = currentSP.getHinhAnh();
            if (anhSP == null || anhSP.isEmpty()) anhSP = "default.png";
            loadHinhAnhLable(anhSP);

            if (mode.equals("DETAIL")) {
                txtTenSP.setEditable(false);
                txtThuongHieu.setEditable(false);
                txtDonGia.setEditable(false);
                txtSoLuongTon.setEditable(false);
                txtMauSac.setEditable(false);
                txtDungLuong.setEditable(false);
                txtRam.setEditable(false);
                
                btnChonAnh.setVisible(false); 
                btnLuu.setText("Đóng");
                btnHuy.setVisible(false);
            }
        }
    }
}