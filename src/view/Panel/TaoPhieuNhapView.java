package view.Panel;

import view.Main;
import com.formdev.flatlaf.FlatClientProperties;
import DAO.SanPhamDAO;
import DAO.PhieuNhapDAO;
import DAO.NhaCungCapDAO;
import controller.PhieuNhapController;
import model.ChiTietPhieuNhap;
import model.PhieuNhap;
import model.SanPham;
import model.NhaCungCap;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TaoPhieuNhapView extends JPanel implements ActionListener {

    Main main;
    PhieuNhapController controller;
    Color BG = new Color(240, 247, 250);

    DefaultTableModel tblModelSP, tblModelChiTiet;
    JTable tableSP, tableChiTiet;

    JTextField txtTimKiem, txtMaSP, txtTenSP, txtGiaNhap, txtSoLuong;
    // Bỏ txtImeiBatDau và cbxPhuongThuc
    JButton btnAddSP, btnImportExcel, btnEditSP, btnDeleteSP;

    JTextField txtMaPhieu, txtNhanVien;
    JComboBox<String> cbxNhaCungCap;
    JLabel lblTongTienValue;
    JButton btnXacNhan;

    public TaoPhieuNhapView(Main main) {
        this.main = main;
        this.controller = new PhieuNhapController(this);
        setLayout(new BorderLayout(15, 15));
        setBackground(BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        initComponent();
        loadDataToTableSP();
        loadDataNhaCungCap();
    }

    private void initComponent() {
        // --- PANEL TRÁI: CHỌN SẢN PHẨM & GIỎ HÀNG ---
        JPanel pnlLeft = new JPanel(new BorderLayout(0, 15));
        pnlLeft.setOpaque(false);

        // Khối trên: Chọn SP và Nhập số lượng
        JPanel pnlLeftTop = new JPanel(new BorderLayout(20, 0));
        pnlLeftTop.setBackground(Color.WHITE);
        pnlLeftTop.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlLeftTop.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlLeftTop.setPreferredSize(new Dimension(0, 250));

        // 1. Bảng chọn sản phẩm nhanh
        JPanel pnlSearchAndTable = new JPanel(new BorderLayout(0, 10));
        pnlSearchAndTable.setOpaque(false);
        pnlSearchAndTable.setPreferredSize(new Dimension(400, 0));

        txtTimKiem = new JTextField();
        txtTimKiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm sản phẩm...");
        pnlSearchAndTable.add(txtTimKiem, BorderLayout.NORTH);

        tblModelSP = new DefaultTableModel(new String[]{"Mã SP", "Tên sản phẩm", "Tồn kho"}, 0);
        tableSP = new JTable(tblModelSP);
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
        pnlSearchAndTable.add(new JScrollPane(tableSP), BorderLayout.CENTER);
        pnlLeftTop.add(pnlSearchAndTable, BorderLayout.WEST);

        // 2. Form nhập liệu (Đã bỏ IMEI)
        JPanel pnlForm = new JPanel(new GridLayout(3, 1, 0, 10));
        pnlForm.setOpaque(false);

        txtMaSP = new JTextField(); txtMaSP.setEditable(false);
        txtTenSP = new JTextField(); txtTenSP.setEditable(false);
        txtGiaNhap = new JTextField();
        txtSoLuong = new JTextField();

        pnlForm.add(createField("Sản phẩm đang chọn", txtTenSP));
        
        JPanel row2 = new JPanel(new GridLayout(1, 2, 15, 0)); row2.setOpaque(false);
        row2.add(createField("Giá nhập (VNĐ)", txtGiaNhap));
        row2.add(createField("Số lượng nhập", txtSoLuong));
        pnlForm.add(row2);

        // Panel nút bấm
        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        pnlBtns.setOpaque(false);
        btnAddSP = new JButton("Thêm vào giỏ"); btnAddSP.addActionListener(this);
        btnEditSP = new JButton("Sửa dòng");
        btnDeleteSP = new JButton("Xoá dòng"); btnDeleteSP.addActionListener(this);
        pnlBtns.add(btnAddSP); pnlBtns.add(Box.createHorizontalStrut(10));
        pnlBtns.add(btnEditSP); pnlBtns.add(Box.createHorizontalStrut(10));
        pnlBtns.add(btnDeleteSP);
        pnlForm.add(pnlBtns);

        pnlLeftTop.add(pnlForm, BorderLayout.CENTER);
        pnlLeft.add(pnlLeftTop, BorderLayout.NORTH);

        // Khối dưới: Bảng Chi tiết phiếu (Giỏ hàng)
        JPanel pnlLeftBottom = new JPanel(new BorderLayout());
        pnlLeftBottom.setBackground(Color.WHITE);
        pnlLeftBottom.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlLeftBottom.setBorder(new EmptyBorder(15, 15, 15, 15));

        tblModelChiTiet = new DefaultTableModel(new String[]{"STT", "Mã SP", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"}, 0);
        tableChiTiet = new JTable(tblModelChiTiet);
        pnlLeftBottom.add(new JScrollPane(tableChiTiet), BorderLayout.CENTER);
        pnlLeft.add(pnlLeftBottom, BorderLayout.CENTER);

        add(pnlLeft, BorderLayout.CENTER);

        // --- PANEL PHẢI: THÔNG TIN CHUNG ---
        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setBackground(Color.WHITE);
        pnlRight.putClientProperty(FlatClientProperties.STYLE, "arc: 12");
        pnlRight.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlRight.setPreferredSize(new Dimension(320, 0));

        JPanel pnlTicket = new JPanel(new GridLayout(3, 1, 0, 20));
        pnlTicket.setOpaque(false);
        txtMaPhieu = new JTextField("PN" + PhieuNhapDAO.getInstance().getAutoIncrement());
        txtMaPhieu.setEditable(false);
        
        String user = (main.getCurrentUser() != null) ? main.getCurrentUser().getUsername() : "Admin";
        txtNhanVien = new JTextField(user); txtNhanVien.setEditable(false);
        cbxNhaCungCap = new JComboBox<>();

        pnlTicket.add(createField("Mã phiếu nhập", txtMaPhieu));
        pnlTicket.add(createField("Nhân viên thực hiện", txtNhanVien));
        pnlTicket.add(createField("Nhà cung cấp", cbxNhaCungCap));
        pnlRight.add(pnlTicket, BorderLayout.NORTH);

        JPanel pnlTotalArea = new JPanel(new BorderLayout(0, 15));
        pnlTotalArea.setOpaque(false);
        lblTongTienValue = new JLabel("0đ", JLabel.RIGHT);
        lblTongTienValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTongTienValue.setForeground(new Color(231, 76, 60));
        
        btnXacNhan = new JButton("Xác nhận nhập hàng");
        btnXacNhan.setPreferredSize(new Dimension(0, 45));
        btnXacNhan.setBackground(new Color(46, 204, 113)); btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXacNhan.addActionListener(this);
        
        pnlTotalArea.add(lblTongTienValue, BorderLayout.NORTH);
        pnlTotalArea.add(btnXacNhan, BorderLayout.SOUTH);
        pnlRight.add(pnlTotalArea, BorderLayout.SOUTH);

        add(pnlRight, BorderLayout.EAST);
    }

    private JPanel createField(String title, Component comp) {
        JPanel p = new JPanel(new BorderLayout(0, 5)); p.setOpaque(false);
        p.add(new JLabel(title), BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    public void loadDataToTableSP() {
        ArrayList<SanPham> list = SanPhamDAO.getInstance().selectAll();
        tblModelSP.setRowCount(0);
        for (SanPham sp : list) {
            tblModelSP.addRow(new Object[]{ sp.getMaSP(), sp.getTenSP(), sp.getSoLuongTon() });
        }
    }

    public void loadDataNhaCungCap() {
        ArrayList<NhaCungCap> list = NhaCungCapDAO.getInstance().selectAll();
        cbxNhaCungCap.removeAllItems();
        for (NhaCungCap ncc : list) cbxNhaCungCap.addItem(ncc.getTenNCC());
    }

    public void tinhTongTien() {
        long tong = 0;
        for (int i = 0; i < tblModelChiTiet.getRowCount(); i++) {
            tong += (long) tblModelChiTiet.getValueAt(i, 5);
        }
        lblTongTienValue.setText(String.format("%,d", tong) + "đ");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddSP) {
            if (txtMaSP.getText().isEmpty() || txtSoLuong.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn SP và nhập số lượng!");
                return;
            }
            try {
                int sl = Integer.parseInt(txtSoLuong.getText());
                long gia = Long.parseLong(txtGiaNhap.getText());
                long thanhTien = sl * gia;
                tblModelChiTiet.addRow(new Object[]{
                    tblModelChiTiet.getRowCount() + 1, txtMaSP.getText(), txtTenSP.getText(), gia, sl, thanhTien
                });
                tinhTongTien();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Số lượng/Giá phải là số!");
            }
        } else if (e.getSource() == btnXacNhan) {
            if (tblModelChiTiet.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
                return;
            }
            controller.handleNhapHang();
        }
    }

    public PhieuNhap getPhieuNhapInfo() {
        String ncc = cbxNhaCungCap.getSelectedItem().toString();
        int nv = (main.getCurrentUser() != null) ? main.getCurrentUser().getManv() : 1;
        long tong = Long.parseLong(lblTongTienValue.getText().replaceAll("[^0-9]", ""));
        return new PhieuNhap(ncc, PhieuNhapDAO.getInstance().getAutoIncrement(), nv, new java.sql.Timestamp(System.currentTimeMillis()), tong, 1);
    }

    public ArrayList<ChiTietPhieuNhap> getDsChiTiet() {
        ArrayList<ChiTietPhieuNhap> ds = new ArrayList<>();
        int mp = PhieuNhapDAO.getInstance().getAutoIncrement();
        for (int i = 0; i < tblModelChiTiet.getRowCount(); i++) {
            String msp = tblModelChiTiet.getValueAt(i, 1).toString();
            int sl = (int) tblModelChiTiet.getValueAt(i, 4);
            long gia = (long) tblModelChiTiet.getValueAt(i, 3);
            ds.add(new ChiTietPhieuNhap(mp, msp, sl, gia));
        }
        return ds;
    }
}