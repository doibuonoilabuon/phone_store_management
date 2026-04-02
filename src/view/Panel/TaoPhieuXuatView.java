package view.Panel;

import view.Main;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TaoPhieuXuatView extends JPanel implements ActionListener {

    Main main;
    Color BG = new Color(240, 247, 250);

    // Bảng dữ liệu
    JTable tableSanPham, tableChiTiet;
    DefaultTableModel tblModelSP, tblModelChiTiet;

    // Các trường nhập liệu bên Trái (Sản phẩm)
    JTextField txtTimKiem, txtMaSp, txtTenSp, txtDonGia, txtSoLuong, txtImei;
    JComboBox<String> cbxCauHinh;
    JButton btnAddSp, btnEditSp, btnDeleteSp, btnScanImei;

    // Các trường nhập liệu bên Phải (Phiếu Xuất)
    JTextField txtMaPhieu, txtNhanVien;
    JComboBox<String> cbxKhachHang; // Khác biệt: Dùng Khách Hàng
    JLabel lblTongTien;
    JButton btnXacNhanXuat;

    public TaoPhieuXuatView(Main main) {
        this.main = main;
        setLayout(new BorderLayout(10, 10));
        setBackground(BG);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        initComponent();
    }

    private void initComponent() {
        // ==================== BÊN TRÁI: KHU VỰC CHỌN SẢN PHẨM ====================
        JPanel pnlLeft = new JPanel(new BorderLayout(0, 10));
        pnlLeft.setOpaque(false);

        // 1. Top Left: Bảng danh sách sản phẩm trong kho
        JPanel pnlTopLeft = new JPanel(new BorderLayout(0, 5));
        pnlTopLeft.setBackground(Color.WHITE);
        pnlTopLeft.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlTopLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlTopLeft.setPreferredSize(new Dimension(0, 250));

        txtTimKiem = new JTextField();
        txtTimKiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm mã, tên sản phẩm trong kho...");
        pnlTopLeft.add(txtTimKiem, BorderLayout.NORTH);

        tblModelSP = new DefaultTableModel(new String[]{"Mã SP", "Tên Sản Phẩm", "Số Lượng Tồn"}, 0);
        tableSanPham = new JTable(tblModelSP);
        pnlTopLeft.add(new JScrollPane(tableSanPham), BorderLayout.CENTER);
        pnlLeft.add(pnlTopLeft, BorderLayout.NORTH);

        // 2. Center Left: Form thông tin sản phẩm bán ra
        JPanel pnlMidLeft = new JPanel(new BorderLayout(10, 10));
        pnlMidLeft.setBackground(Color.WHITE);
        pnlMidLeft.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlMidLeft.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlForm.setBackground(Color.WHITE);
        
        pnlForm.add(createInputPanel("Mã SP", txtMaSp = new JTextField()));
        txtMaSp.setEditable(false);
        pnlForm.add(createInputPanel("Tên Sản Phẩm", txtTenSp = new JTextField()));
        txtTenSp.setEditable(false);
        pnlForm.add(createComboPanel("Cấu hình", cbxCauHinh = new JComboBox<>(new String[]{"Chọn sản phẩm trước"})));
        pnlForm.add(createInputPanel("Giá Bán", txtDonGia = new JTextField())); // Khác biệt: Giá Bán

        pnlForm.add(createInputPanel("Số Lượng", txtSoLuong = new JTextField()));
        pnlForm.add(createInputPanel("Mã IMEI (Máy bán)", txtImei = new JTextField()));
        
        JPanel pnlImeiActions = new JPanel(new GridLayout(1, 1, 5, 0));
        pnlImeiActions.setBackground(Color.WHITE);
        btnScanImei = new JButton("Quét mã IMEI");
        btnScanImei.setBackground(new Color(23, 162, 184)); // Màu xanh lơ
        btnScanImei.setForeground(Color.WHITE);
        pnlImeiActions.add(btnScanImei);
        pnlForm.add(createPanel("Hành động", pnlImeiActions));

        pnlMidLeft.add(pnlForm, BorderLayout.CENTER);

        // Nút thêm/sửa/xóa sản phẩm vào giỏ hàng (Phiếu xuất)
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlActions.setBackground(Color.WHITE);
        btnAddSp = new JButton("Thêm vào giỏ");
        btnAddSp.setBackground(new Color(40, 167, 69)); btnAddSp.setForeground(Color.WHITE);
        btnEditSp = new JButton("Sửa");
        btnDeleteSp = new JButton("Xóa");
        
        btnAddSp.addActionListener(this);
        pnlActions.add(btnAddSp); pnlActions.add(btnEditSp); pnlActions.add(btnDeleteSp);
        pnlMidLeft.add(pnlActions, BorderLayout.SOUTH);
        
        pnlLeft.add(pnlMidLeft, BorderLayout.CENTER);

        // 3. Bottom Left: Bảng danh sách sản phẩm ĐÃ ĐƯỢC CHỌN VÀO GIỎ (Phiếu xuất)
        JPanel pnlBotLeft = new JPanel(new BorderLayout());
        pnlBotLeft.setBackground(Color.WHITE);
        pnlBotLeft.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlBotLeft.setBorder(BorderFactory.createTitledBorder("Giỏ hàng (Sản phẩm xuất bán)"));
        pnlBotLeft.setPreferredSize(new Dimension(0, 200));

        tblModelChiTiet = new DefaultTableModel(new String[]{"STT", "Mã SP", "Tên SP", "Cấu hình", "Đơn Giá", "Số Lượng"}, 0);
        tableChiTiet = new JTable(tblModelChiTiet);
        pnlBotLeft.add(new JScrollPane(tableChiTiet), BorderLayout.CENTER);
        pnlLeft.add(pnlBotLeft, BorderLayout.SOUTH);

        add(pnlLeft, BorderLayout.CENTER);

        // ==================== BÊN PHẢI: KHU VỰC THÔNG TIN PHIẾU XUẤT ====================
        JPanel pnlRight = new JPanel(new BorderLayout(0, 10));
        pnlRight.setPreferredSize(new Dimension(300, 0));
        pnlRight.setOpaque(false);

        JPanel pnlInfo = new JPanel(new GridLayout(4, 1, 0, 15));
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlInfo.setBorder(new EmptyBorder(20, 15, 20, 15));

        pnlInfo.add(createInputPanel("Mã Phiếu Xuất", txtMaPhieu = new JTextField("Tự động tạo")));
        txtMaPhieu.setEditable(false);
        pnlInfo.add(createInputPanel("Nhân Viên Lập", txtNhanVien = new JTextField("Tên nhân viên đang đăng nhập")));
        txtNhanVien.setEditable(false);
        pnlInfo.add(createComboPanel("Khách Hàng", cbxKhachHang = new JComboBox<>(new String[]{"Khách vãng lai"}))); // Khác biệt: Khách hàng

        pnlRight.add(pnlInfo, BorderLayout.NORTH);

        // Tổng tiền và Nút xác nhận xuất kho
        JPanel pnlThanhToan = new JPanel(new BorderLayout(0, 15));
        pnlThanhToan.setBackground(Color.WHITE);
        pnlThanhToan.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlThanhToan.setBorder(new EmptyBorder(20, 15, 20, 15));

        JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTongTien.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("TỔNG TIỀN: ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongTien = new JLabel("0 đ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongTien.setForeground(new Color(220, 53, 69)); 
        pnlTongTien.add(lblTitle); pnlTongTien.add(lblTongTien);

        btnXacNhanXuat = new JButton("XÁC NHẬN XUẤT HÀNG");
        btnXacNhanXuat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnXacNhanXuat.setBackground(new Color(255, 152, 0)); // Màu cam đặc trưng cho bán hàng
        btnXacNhanXuat.setForeground(Color.WHITE);
        btnXacNhanXuat.setPreferredSize(new Dimension(0, 50));
        btnXacNhanXuat.addActionListener(this);

        pnlThanhToan.add(pnlTongTien, BorderLayout.CENTER);
        pnlThanhToan.add(btnXacNhanXuat, BorderLayout.SOUTH);

        pnlRight.add(pnlThanhToan, BorderLayout.SOUTH);

        add(pnlRight, BorderLayout.EAST);

        // Căn giữa các bảng
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tableSanPham.getColumnCount(); i++) tableSanPham.getColumnModel().getColumn(i).setCellRenderer(center);
        for(int i=0; i<tableChiTiet.getColumnCount(); i++) tableChiTiet.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    private JPanel createInputPanel(String title, Component comp) {
        JPanel pnl = new JPanel(new BorderLayout(0, 5));
        pnl.setBackground(Color.WHITE);
        pnl.add(new JLabel(title), BorderLayout.NORTH);
        pnl.add(comp, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel createComboPanel(String title, JComboBox cbx) {
        JPanel pnl = new JPanel(new BorderLayout(0, 5));
        pnl.setBackground(Color.WHITE);
        pnl.add(new JLabel(title), BorderLayout.NORTH);
        pnl.add(cbx, BorderLayout.CENTER);
        return pnl;
    }
    
    private JPanel createPanel(String title, JPanel inner) {
        JPanel pnl = new JPanel(new BorderLayout(0, 5));
        pnl.setBackground(Color.WHITE);
        pnl.add(new JLabel(title), BorderLayout.NORTH);
        pnl.add(inner, BorderLayout.CENTER);
        return pnl;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddSp) {
            JOptionPane.showMessageDialog(this, "Chức năng nạp sản phẩm vào giỏ chờ xử lý ở Controller!");
        } else if (e.getSource() == btnXacNhanXuat) {
            JOptionPane.showMessageDialog(this, "Chức năng Lưu Phiếu Xuất và in hóa đơn chờ xử lý ở Controller!");
        }
    }
}