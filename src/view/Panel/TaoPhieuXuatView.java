package view.Panel;

import view.Main;
import com.formdev.flatlaf.FlatClientProperties;
import DAO.PhieuXuatDAO;
import DAO.SanPhamDAO;
import DAO.KhachHangDAO;
import controller.PhieuXuatController;
import model.ChiTietPhieuXuat;
import model.PhieuXuat;
import model.SanPham;
import model.KhachHang;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TaoPhieuXuatView extends JPanel implements ActionListener {

    Main main;
    PhieuXuatController controller;
    Color BG = new Color(240, 247, 250);
    
    DefaultTableModel tblModelSP, tblModelPhieu;
    JTable tableSP, tablePhieu;
    
    JTextField txtMaSP, txtTenSP, txtGiaXuat, txtSoLuong;
    JButton btnAddSP, btnDeleteSP;

    JTextField txtMaPhieu, txtNhanVien;
    JComboBox<String> cbxKhachHang; 
    JLabel lblTongTien;
    JButton btnXacNhan;

    // Danh sách ngầm để lưu trữ thông tin Khách Hàng gốc
    private ArrayList<KhachHang> listKhachHang;

    public TaoPhieuXuatView(Main main) {
        this.main = main;
        this.controller = new PhieuXuatController(this);
        setLayout(new BorderLayout(10, 10));
        setBackground(BG);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        initComponent();
        loadDataToTableSP();
        loadDataKhachHang(); 
    }

    private void initComponent() {
        // --- TRÁI: KHO HÀNG ---
        JPanel pnlLeft = new JPanel(new BorderLayout(5, 5));
        pnlLeft.setPreferredSize(new Dimension(400, 0));
        pnlLeft.setOpaque(false);

        JTextField txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm sản phẩm trong kho...");
        pnlLeft.add(txtSearch, BorderLayout.NORTH);

        tblModelSP = new DefaultTableModel(new String[]{"Mã SP", "Tên sản phẩm", "Tồn kho"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableSP = new JTable(tblModelSP);
        tableSP.setRowHeight(30);
        
        tableSP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableSP.getSelectedRow();
                if (row != -1) {
                    txtMaSP.setText(tableSP.getValueAt(row, 0).toString());
                    txtTenSP.setText(tableSP.getValueAt(row, 1).toString());
                }
            }
        });
        pnlLeft.add(new JScrollPane(tableSP), BorderLayout.CENTER);

        // --- GIỮA: FORM VÀ GIỎ HÀNG ---
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setOpaque(false);

        JPanel pnlForm = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlForm.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        pnlForm.add(createInput("Mã sản phẩm", txtMaSP = new JTextField())); txtMaSP.setEditable(false);
        pnlForm.add(createInput("Tên sản phẩm", txtTenSP = new JTextField())); txtTenSP.setEditable(false);
        pnlForm.add(createInput("Giá bán (VNĐ)", txtGiaXuat = new JTextField()));
        pnlForm.add(createInput("Số lượng xuất", txtSoLuong = new JTextField()));
        
        JPanel pnlMidButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); pnlMidButtons.setOpaque(false);
        btnAddSP = new JButton("Thêm vào giỏ"); btnAddSP.setBackground(new Color(40, 167, 69)); btnAddSP.setForeground(Color.WHITE);
        btnDeleteSP = new JButton("Xoá khỏi giỏ");
        btnAddSP.addActionListener(this);
        btnDeleteSP.addActionListener(e -> {
            int row = tablePhieu.getSelectedRow();
            if(row != -1) { tblModelPhieu.removeRow(row); tinhTongTien(); }
        });
        
        pnlMidButtons.add(btnAddSP); pnlMidButtons.add(btnDeleteSP);
        pnlForm.add(new JLabel()); pnlForm.add(pnlMidButtons);

        tblModelPhieu = new DefaultTableModel(new String[]{"STT", "Mã SP", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"}, 0);
        tablePhieu = new JTable(tblModelPhieu);
        tablePhieu.setRowHeight(30);
        
        pnlCenter.add(pnlForm, BorderLayout.NORTH);
        pnlCenter.add(new JScrollPane(tablePhieu), BorderLayout.CENTER);

        // --- PHẢI: THÔNG TIN PHIẾU ---
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setPreferredSize(new Dimension(300, 0));
        pnlRight.setOpaque(false);

        JPanel pnlTicket = new JPanel(new GridLayout(3, 1, 10, 10));
        pnlTicket.setBackground(Color.WHITE);
        pnlTicket.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlTicket.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        
        txtMaPhieu = new JTextField("PX" + PhieuXuatDAO.getInstance().getAutoIncrement()); txtMaPhieu.setEditable(false);
        
        String tenNV = main.getCurrentUser() != null ? main.getCurrentUser().getUsername() : "Admin";
        txtNhanVien = new JTextField(tenNV); txtNhanVien.setEditable(false);
        
        cbxKhachHang = new JComboBox<>();
        
        pnlTicket.add(createInput("Mã phiếu xuất", txtMaPhieu));
        pnlTicket.add(createInput("Nhân viên bán", txtNhanVien));
        pnlTicket.add(createInput("Khách hàng", cbxKhachHang));

        JPanel pnlTotal = new JPanel(new BorderLayout()); pnlTotal.setOpaque(false);
        lblTongTien = new JLabel("0đ", JLabel.RIGHT);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 24)); lblTongTien.setForeground(new Color(220, 53, 69));
        
        btnXacNhan = new JButton("XÁC NHẬN XUẤT");
        btnXacNhan.setBackground(new Color(255, 152, 0)); btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 16)); btnXacNhan.setPreferredSize(new Dimension(0, 55));
        btnXacNhan.addActionListener(this);
        
        pnlTotal.add(new JLabel("TỔNG TIỀN:"), BorderLayout.WEST);
        pnlTotal.add(lblTongTien, BorderLayout.CENTER);
        pnlTotal.add(btnXacNhan, BorderLayout.SOUTH);

        pnlRight.add(pnlTicket, BorderLayout.NORTH);
        pnlRight.add(pnlTotal, BorderLayout.SOUTH);

        add(pnlLeft, BorderLayout.WEST);
        add(pnlCenter, BorderLayout.CENTER);
        add(pnlRight, BorderLayout.EAST);
    }

    private JPanel createInput(String title, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 5)); p.setOpaque(false);
        p.add(new JLabel(title), BorderLayout.NORTH); p.add(comp, BorderLayout.CENTER);
        return p;
    }

    public void loadDataToTableSP() {
        ArrayList<SanPham> list = SanPhamDAO.getInstance().selectAll();
        tblModelSP.setRowCount(0);
        for (SanPham sp : list) tblModelSP.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getSoLuongTon()});
    }

    public void loadDataKhachHang() {
        cbxKhachHang.removeAllItems();
        cbxKhachHang.addItem("Khách vãng lai"); // Index 0
        
        try {
            listKhachHang = KhachHangDAO.getInstance().selectAll();
            for (KhachHang kh : listKhachHang) {
                cbxKhachHang.addItem(kh.getTenKH()); // Chỉ hiển thị tên
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tinhTongTien() {
        long tong = 0;
        for (int i = 0; i < tblModelPhieu.getRowCount(); i++) tong += (long) tblModelPhieu.getValueAt(i, 5);
        lblTongTien.setText(String.format("%,d", tong) + "đ");
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddSP) {
            int rowSP = tableSP.getSelectedRow();
            if(rowSP == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm từ kho!"); return; }
            try {
                int slXuat = Integer.parseInt(txtSoLuong.getText());
                long giaXuat = Long.parseLong(txtGiaXuat.getText());
                int tonKho = (int) tableSP.getValueAt(rowSP, 2);
                
                if(slXuat > tonKho) { JOptionPane.showMessageDialog(this, "Lỗi: Số lượng xuất vượt quá tồn kho (" + tonKho + ")!"); return; }
                
                long thanhTien = slXuat * giaXuat;
                tblModelPhieu.addRow(new Object[]{ tblModelPhieu.getRowCount() + 1, txtMaSP.getText(), txtTenSP.getText(), giaXuat, slXuat, thanhTien });
                tinhTongTien();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Số lượng và Giá phải là số!"); }
        } else if (e.getSource() == btnXacNhan) {
            if(tblModelPhieu.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "Giỏ hàng trống!"); return; }
            controller.handleXuatHang();
        }
    }

    public PhieuXuat getPhieuXuatInfo() {
        int selectedIndex = cbxKhachHang.getSelectedIndex();
        String maKH = "Khách vãng lai"; // Mặc định cho index 0
        
        if (selectedIndex > 0) {
            // Trừ 1 vì listKhachHang không chứa "Khách vãng lai"
            maKH = listKhachHang.get(selectedIndex - 1).getMaKH();
        }

        int nv = main.getCurrentUser() != null ? main.getCurrentUser().getManv() : 1;
        long tongTien = Long.parseLong(lblTongTien.getText().replaceAll("[^0-9]", ""));
        return new PhieuXuat(maKH, PhieuXuatDAO.getInstance().getAutoIncrement(), nv, new java.sql.Timestamp(System.currentTimeMillis()), tongTien, 1);
    }

    public ArrayList<ChiTietPhieuXuat> getDsChiTiet() {
        ArrayList<ChiTietPhieuXuat> list = new ArrayList<>();
        int maPhieu = PhieuXuatDAO.getInstance().getAutoIncrement();
        for (int i = 0; i < tblModelPhieu.getRowCount(); i++) {
            String maSP = tblModelPhieu.getValueAt(i, 1).toString();
            long donGia = (long) tblModelPhieu.getValueAt(i, 3);
            int soLuong = (int) tblModelPhieu.getValueAt(i, 4);
            list.add(new ChiTietPhieuXuat(maPhieu, maSP, soLuong, donGia));
        }
        return list;
    }
}