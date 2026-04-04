package view.Panel;

import view.Main;
import view.Form.ButtonToolBar;
import view.Form.IntegratedSearch;
import com.formdev.flatlaf.FlatClientProperties;

import com.toedter.calendar.JDateChooser; 
import view.Dialog.ChiTietPhieuXuatDialog;
import DAO.PhieuXuatDAO;
import DAO.NhanVienDAO;
import DAO.KhachHangDAO;
import model.PhieuXuat;
import model.NhanVien;
import model.KhachHang;
import controller.PhieuXuatFilterController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PhieuXuatView extends JPanel implements ActionListener {

    Main main;
    DefaultTableModel tblModel;
    JTable table;
    IntegratedSearch search;
    ButtonToolBar btnAdd, btnDetail, btnCancel, btnExport;
    
    JComboBox<String> cbxKhachHang, cbxNhanVien;
    JTextField txtMoneyMin, txtMoneyMax;
    JDateChooser dateStart, dateEnd;
    JButton btnApplyFilter;

    PhieuXuatFilterController filterController;
    ArrayList<PhieuXuat> listKhoGoc;
    Color BG = new Color(240, 247, 250);

    public PhieuXuatView(Main main) {
        this.main = main;
        this.filterController = new PhieuXuatFilterController();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        initPadding();
        initContent();
        
        loadComboBoxData();
        listKhoGoc = PhieuXuatDAO.getInstance().selectAll();
        loadDataToTable(listKhoGoc);
    }

    private void initContent() {
        JPanel contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(BG);
        add(contentCenter, BorderLayout.CENTER);

        // --- 1. TOOLBAR --- (Giữ nguyên)
        JPanel functionBar = new JPanel(new GridLayout(1, 2, 30, 0));
        functionBar.setBackground(Color.WHITE);
        functionBar.setPreferredSize(new Dimension(0, 95));
        functionBar.setBorder(new EmptyBorder(8, 10, 8, 10));
        functionBar.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        JToolBar toolbar = new JToolBar();
        toolbar.setBackground(Color.WHITE); toolbar.setFloatable(false); toolbar.setBorderPainted(false);

        btnAdd = new ButtonToolBar("XUẤT HÀNG", "add.svg", "create");
        btnDetail = new ButtonToolBar("CHI TIẾT", "detail.svg", "view");
        btnCancel = new ButtonToolBar("HỦY PHIẾU", "cancel.svg", "cancel");
        

        for (ButtonToolBar btn : new ButtonToolBar[]{btnAdd, btnDetail, btnCancel}) {
            btn.addActionListener(this);
            toolbar.add(btn);
        }
        functionBar.add(toolbar);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã Phiếu", "Khách Hàng", "Người Tạo"});
        search.btnReset.addActionListener(this);
        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        // --- 2. BỘ LỌC CÓ LỊCH JDATECHOOSER ---
        JPanel pnlFilter = new JPanel(new GridLayout(6, 1, 0, 15));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setPreferredSize(new Dimension(260, 0));
        pnlFilter.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlFilter.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        pnlFilter.add(createFilterPanel("Khách hàng:", cbxKhachHang = new JComboBox<>()));
        pnlFilter.add(createFilterPanel("Nhân viên xuất:", cbxNhanVien = new JComboBox<>()));

        JPanel pnlDate = new JPanel(new GridLayout(1, 2, 10, 0)); pnlDate.setOpaque(false);
        dateStart = new JDateChooser(); dateStart.setDateFormatString("dd/MM/yyyy");
        pnlDate.add(createFilterPanel("Từ ngày:", dateStart));
        
        dateEnd = new JDateChooser(); dateEnd.setDateFormatString("dd/MM/yyyy");
        pnlDate.add(createFilterPanel("Đến ngày:", dateEnd));
        
        pnlFilter.add(pnlDate);

        JPanel pnlMoney = new JPanel(new GridLayout(1, 2, 10, 0)); pnlMoney.setOpaque(false);
        pnlMoney.add(createInputPanel("Từ số tiền:", txtMoneyMin = new JTextField()));
        pnlMoney.add(createInputPanel("Đến:", txtMoneyMax = new JTextField()));
        pnlFilter.add(pnlMoney);

        btnApplyFilter = new JButton("Áp dụng Lọc");
        btnApplyFilter.setBackground(new Color(1, 87, 155)); btnApplyFilter.setForeground(Color.WHITE);
        btnApplyFilter.addActionListener(this); 
        pnlFilter.add(btnApplyFilter);
        
        contentCenter.add(pnlFilter, BorderLayout.WEST);

        // --- 3. BẢNG DỮ LIỆU ---
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        String[] cols = {"STT", "Mã Phiếu", "Khách Hàng", "Nhân Viên Lập", "Thời Gian", "Tổng Tiền", "Trạng Thái"};
        tblModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tblModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Nếu nháy đúp 2 lần
                    btnDetail.doClick(); // Tự động kích hoạt nút Chi Tiết
                }
            }
        });
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(center);

        pnlTable.add(new JScrollPane(table), BorderLayout.CENTER);
        contentCenter.add(pnlTable, BorderLayout.CENTER);
    }

    private void loadComboBoxData() {
        try {
            cbxKhachHang.removeAllItems();
            cbxKhachHang.addItem("Tất cả");
            ArrayList<KhachHang> dsKH = KhachHangDAO.getInstance().selectAll();
            if (dsKH != null) for (KhachHang kh : dsKH) cbxKhachHang.addItem( kh.getTenKH());

            cbxNhanVien.removeAllItems();
            cbxNhanVien.addItem("Tất cả");
            ArrayList<NhanVien> dsNV = NhanVienDAO.getInstance().selectAll();
            if (dsNV != null) for (NhanVien nv : dsNV) cbxNhanVien.addItem(nv.getHoten());
        } catch (Exception e) {}
    }

    public void loadDataToTable(ArrayList<PhieuXuat> list) {
        tblModel.setRowCount(0); 
        int stt = 1;
        DecimalFormat formatter = new DecimalFormat("###,###,### đ");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (PhieuXuat px : list) {
            String tenKH = "Khách vãng lai";
            if (px.getMakh() != null && !px.getMakh().equals("Khách vãng lai")) {
                try {
                    KhachHang kh = KhachHangDAO.getInstance().selectById(px.getMakh());
                    if (kh != null) tenKH = kh.getTenKH();
                } catch (Exception e) {}
            }

            String tenNV = "Mã: " + px.getManguoitao();
            try {
                NhanVien nv = NhanVienDAO.getInstance().selectById(String.valueOf(px.getManguoitao()));
                if (nv != null) tenNV = nv.getHoten();
            } catch (Exception e) {}

            tblModel.addRow(new Object[]{
                stt++, "PX" + px.getMaphieu(), tenKH, tenNV, sdf.format(px.getThoigiantao()), formatter.format(px.getTongTien()), px.getTrangthai() == 1 ? "Hoàn thành" : "Đã hủy"
            });
        }
    }

    private JPanel createFilterPanel(String title, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 5)); p.setBackground(Color.WHITE);
        p.add(new JLabel(title), BorderLayout.NORTH); p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JPanel createInputPanel(String title, JTextField txt) {
        JPanel p = new JPanel(new BorderLayout(0, 5)); p.setBackground(Color.WHITE);
        p.add(new JLabel(title), BorderLayout.NORTH); p.add(txt, BorderLayout.CENTER);
        return p;
    }

    private void initPadding() {
        for (String[] s : new String[][]{{BorderLayout.NORTH,"10,0"},{BorderLayout.SOUTH,"10,0"},{BorderLayout.EAST,"0,10"},{BorderLayout.WEST,"0,10"}}) {
            JPanel p = new JPanel(); p.setBackground(BG);
            int h = Integer.parseInt(s[1].split(",")[0]), w = Integer.parseInt(s[1].split(",")[1]);
            p.setPreferredSize(new Dimension(w, h)); add(p, s[0]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            main.setPanel(new TaoPhieuXuatView(main));
        } else if (e.getSource() == btnDetail) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) { 
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu xuất để xem chi tiết!"); 
                return; 
            }

            try {
                int maPhieuLoc = Integer.parseInt(table.getValueAt(selectedRow, 1).toString().replace("PX", ""));
                PhieuXuat pxSelected = null;
                for (PhieuXuat px : listKhoGoc) {
                    if (px.getMaphieu() == maPhieuLoc) {
                        pxSelected = px;
                        break;
                    }
                }

                if (pxSelected != null) {
                   
                    new ChiTietPhieuXuatDialog(main, pxSelected).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu của phiếu này!");
                }
            } catch (Exception ex) {
                // In lỗi ra terminal và hiện bảng thông báo
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Bị lỗi ngầm rồi sếp ơi: " + ex.getMessage());
            }
        } else if (e.getSource() == btnCancel) {
           int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) { 
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần hủy!"); 
                return; 
            }

            // Lấy mã phiếu từ cột số 1 (cột Mã Phiếu), cắt bỏ chữ "PX" đi để gửi xuống DB
            String maPhieuString = table.getValueAt(selectedRow, 1).toString().replace("PX", "");
            
            // Hiện bảng xác nhận cho chắc cú
            int cf = JOptionPane.showConfirmDialog(this, "Xác nhận hủy phiếu xuất này? Thao tác không thể hoàn tác!", "Hủy phiếu", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (cf == JOptionPane.YES_OPTION) {
                // Gọi DAO để update trạng thái phiếu trong database (thường là update trạng thái về 0)
                if (PhieuXuatDAO.getInstance().delete(maPhieuString) > 0) {
                    JOptionPane.showMessageDialog(this, "Đã hủy phiếu xuất thành công!");
                    
                    // Cập nhật lại danh sách gốc và load lại bảng cho mới
                    listKhoGoc = PhieuXuatDAO.getInstance().selectAll();
                    loadDataToTable(listKhoGoc);
                } else {
                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi hủy phiếu!");
                }}
        } else if (e.getSource() == search.btnReset) {
            cbxKhachHang.setSelectedIndex(0);
            cbxNhanVien.setSelectedIndex(0);
            dateStart.setDate(null); // Reset Lịch
            dateEnd.setDate(null);   // Reset Lịch
            txtMoneyMin.setText("");
            txtMoneyMax.setText("");
            listKhoGoc = PhieuXuatDAO.getInstance().selectAll();
            loadDataToTable(listKhoGoc);
        } else if (e.getSource() == btnApplyFilter) {
            String kh = cbxKhachHang.getSelectedItem() != null ? cbxKhachHang.getSelectedItem().toString() : "Tất cả";
            String nv = cbxNhanVien.getSelectedItem() != null ? cbxNhanVien.getSelectedItem().toString() : "Tất cả";
            
            // Lấy Date từ Lịch
            Date tuNgay = dateStart.getDate();
            Date denNgay = dateEnd.getDate();
            
            String tuTien = txtMoneyMin.getText();
            String denTien = txtMoneyMax.getText();

            ArrayList<PhieuXuat> ketQua = filterController.locPhieuXuat(listKhoGoc, kh, nv, tuNgay, denNgay, tuTien, denTien);
            loadDataToTable(ketQua);
        }
    }
}