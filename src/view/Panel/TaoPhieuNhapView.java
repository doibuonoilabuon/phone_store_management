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

public class TaoPhieuNhapView extends JPanel implements ActionListener {

    Main main;
    Color BG = new Color(240, 247, 250);

    // Bảng dữ liệu
    JTable tableSanPham, tableChiTiet;
    DefaultTableModel tblModelSP, tblModelChiTiet;

    // Các trường nhập liệu bên Trái (Sản phẩm)
    JTextField txtTimKiem, txtMaSp, txtTenSp, txtDonGia, txtSoLuong, txtImei;
    JComboBox<String> cbxCauHinh, cbxPhuongThuc;
    JButton btnAddSp, btnEditSp, btnDeleteSp, btnImportExcel, btnScanImei;

    // Các trường nhập liệu bên Phải (Phiếu Nhập)
    JTextField txtMaPhieu, txtNhanVien;
    JComboBox<String> cbxNhaCungCap;
    JLabel lblTongTien;
    JButton btnXacNhanNhap;

    public TaoPhieuNhapView(Main main) {
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

        // 1. Top Left: Bảng danh sách sản phẩm mẫu
        JPanel pnlTopLeft = new JPanel(new BorderLayout(0, 5));
        pnlTopLeft.setBackground(Color.WHITE);
        pnlTopLeft.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlTopLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlTopLeft.setPreferredSize(new Dimension(0, 250));

        txtTimKiem = new JTextField();
        txtTimKiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm mã, tên sản phẩm...");
        pnlTopLeft.add(txtTimKiem, BorderLayout.NORTH);

        tblModelSP = new DefaultTableModel(new String[]{"Mã SP", "Tên Sản Phẩm", "Số Lượng Tồn"}, 0);
        tableSanPham = new JTable(tblModelSP);
        pnlTopLeft.add(new JScrollPane(tableSanPham), BorderLayout.CENTER);
        pnlLeft.add(pnlTopLeft, BorderLayout.NORTH);

        // 2. Center Left: Form cấu hình sản phẩm chuẩn bị nhập
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
        pnlForm.add(createInputPanel("Giá Nhập", txtDonGia = new JTextField()));

        pnlForm.add(createComboPanel("Phương thức", cbxPhuongThuc = new JComboBox<>(new String[]{"Nhập lô", "Nhập lẻ"})));
        pnlForm.add(createInputPanel("Số Lượng", txtSoLuong = new JTextField()));
        pnlForm.add(createInputPanel("Mã IMEI (Lô/Đầu)", txtImei = new JTextField()));
        
        JPanel pnlImeiActions = new JPanel(new GridLayout(1, 2, 5, 0));
        pnlImeiActions.setBackground(Color.WHITE);
        btnScanImei = new JButton("Quét IMEI");
        btnImportExcel = new JButton("Excel IMEI");
        pnlImeiActions.add(btnScanImei);
        pnlImeiActions.add(btnImportExcel);
        pnlForm.add(createPanel("Hành động IMEI", pnlImeiActions));

        pnlMidLeft.add(pnlForm, BorderLayout.CENTER);

        // Nút thêm/sửa/xóa sản phẩm vào phiếu
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlActions.setBackground(Color.WHITE);
        btnAddSp = new JButton("Thêm vào phiếu");
        btnAddSp.setBackground(new Color(40, 167, 69)); btnAddSp.setForeground(Color.WHITE);
        btnEditSp = new JButton("Sửa");
        btnDeleteSp = new JButton("Xóa");
        
        btnAddSp.addActionListener(this);
        pnlActions.add(btnAddSp); pnlActions.add(btnEditSp); pnlActions.add(btnDeleteSp);
        pnlMidLeft.add(pnlActions, BorderLayout.SOUTH);
        
        pnlLeft.add(pnlMidLeft, BorderLayout.CENTER);

        // 3. Bottom Left: Bảng danh sách sản phẩm ĐÃ ĐƯỢC CHỌN VÀO PHIẾU
        JPanel pnlBotLeft = new JPanel(new BorderLayout());
        pnlBotLeft.setBackground(Color.WHITE);
        pnlBotLeft.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlBotLeft.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm trong phiếu nhập"));
        pnlBotLeft.setPreferredSize(new Dimension(0, 200));

        tblModelChiTiet = new DefaultTableModel(new String[]{"STT", "Mã SP", "Tên SP", "Cấu hình", "Đơn Giá", "Số Lượng"}, 0);
        tableChiTiet = new JTable(tblModelChiTiet);
        pnlBotLeft.add(new JScrollPane(tableChiTiet), BorderLayout.CENTER);
        pnlLeft.add(pnlBotLeft, BorderLayout.SOUTH);

        add(pnlLeft, BorderLayout.CENTER);

        // ==================== BÊN PHẢI: KHU VỰC THÔNG TIN PHIẾU ====================
        JPanel pnlRight = new JPanel(new BorderLayout(0, 10));
        pnlRight.setPreferredSize(new Dimension(300, 0));
        pnlRight.setOpaque(false);

        JPanel pnlInfo = new JPanel(new GridLayout(4, 1, 0, 15));
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlInfo.setBorder(new EmptyBorder(20, 15, 20, 15));

        pnlInfo.add(createInputPanel("Mã Phiếu Nhập", txtMaPhieu = new JTextField("Tự động tạo")));
        txtMaPhieu.setEditable(false);
        pnlInfo.add(createInputPanel("Nhân Viên Lập", txtNhanVien = new JTextField("Tên nhân viên đang đăng nhập")));
        txtNhanVien.setEditable(false);
        pnlInfo.add(createComboPanel("Nhà Cung Cấp", cbxNhaCungCap = new JComboBox<>(new String[]{"Chọn nhà cung cấp"})));

        pnlRight.add(pnlInfo, BorderLayout.NORTH);

        // Tổng tiền và Nút xác nhận
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
        lblTongTien.setForeground(new Color(220, 53, 69)); // Màu đỏ
        pnlTongTien.add(lblTitle); pnlTongTien.add(lblTongTien);

        btnXacNhanNhap = new JButton("XÁC NHẬN NHẬP HÀNG");
        btnXacNhanNhap.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnXacNhanNhap.setBackground(new Color(13, 110, 253));
        btnXacNhanNhap.setForeground(Color.WHITE);
        btnXacNhanNhap.setPreferredSize(new Dimension(0, 50));
        btnXacNhanNhap.addActionListener(this);

        pnlThanhToan.add(pnlTongTien, BorderLayout.CENTER);
        pnlThanhToan.add(btnXacNhanNhap, BorderLayout.SOUTH);

        pnlRight.add(pnlThanhToan, BorderLayout.SOUTH);

        add(pnlRight, BorderLayout.EAST);

        // Căn giữa các bảng
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tableSanPham.getColumnCount(); i++) tableSanPham.getColumnModel().getColumn(i).setCellRenderer(center);
        for(int i=0; i<tableChiTiet.getColumnCount(); i++) tableChiTiet.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    // Các hàm hỗ trợ vẽ UI cho gọn code
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
            JOptionPane.showMessageDialog(this, "Chức năng nạp sản phẩm vào phiếu chờ xử lý ở Controller!");
        } else if (e.getSource() == btnXacNhanNhap) {
            JOptionPane.showMessageDialog(this, "Chức năng lưu Phiếu Nhập xuống CSDL chờ xử lý ở Controller!");
        }
    }
}