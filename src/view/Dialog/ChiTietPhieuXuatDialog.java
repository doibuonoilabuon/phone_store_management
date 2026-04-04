package view.Dialog;

import DAO.ChiTietPhieuXuatDAO;
import DAO.KhachHangDAO;
import DAO.NhanVienDAO;
import DAO.SanPhamDAO;
import model.ChiTietPhieuXuat;
import model.KhachHang;
import model.NhanVien;
import model.PhieuXuat;
import model.SanPham;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ChiTietPhieuXuatDialog extends JDialog {

    private PhieuXuat phieuXuat;
    private DefaultTableModel tblModel;
    private JTable table;

    public ChiTietPhieuXuatDialog(JFrame parent, PhieuXuat px) {
        super(parent, "Chi tiết Phiếu Xuất", true);
        this.phieuXuat = px;
        setSize(900, 550);
        setLocationRelativeTo(parent);
        
        initComponent();
        loadDataToTable();
    }

    private void initComponent() {
        setLayout(new BorderLayout());

        // --- 1. HEADER CHUNG ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(255, 152, 0)); // Màu cam cho Phiếu xuất
        JLabel lblTitle = new JLabel("CHI TIẾT PHIẾU XUẤT");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // --- 2. THÔNG TIN PHIẾU ---
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel pnlInfo = new JPanel(new GridLayout(2, 2, 15, 10));
        
        // Dịch mã sang tên cho đẹp
        String tenNV = "Mã: " + phieuXuat.getManguoitao();
        try {
            NhanVien nv = NhanVienDAO.getInstance().selectById(String.valueOf(phieuXuat.getManguoitao()));
            if(nv != null) tenNV = nv.getHoten();
        } catch(Exception e){}

        String tenKH = "Khách vãng lai";
        if(phieuXuat.getMakh() != null && !phieuXuat.getMakh().equals("Khách vãng lai")){
            try {
                KhachHang kh = KhachHangDAO.getInstance().selectById(phieuXuat.getMakh());
                if(kh != null) tenKH = kh.getTenKH();
            } catch(Exception e){}
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DecimalFormat formatter = new DecimalFormat("###,###,### đ");

        pnlInfo.add(createField("Mã phiếu:", "PX" + phieuXuat.getMaphieu()));
        pnlInfo.add(createField("Khách hàng:", tenKH));
        pnlInfo.add(createField("Nhân viên bán:", tenNV));
        pnlInfo.add(createField("Thời gian xuất:", sdf.format(phieuXuat.getThoigiantao())));

        pnlCenter.add(pnlInfo, BorderLayout.NORTH);

        // --- 3. BẢNG SẢN PHẨM (BỎ IMEI) ---
        String[] cols = {"STT", "Mã SP", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"};
        tblModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tblModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(center);

        pnlCenter.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Dòng tổng tiền ở dưới
        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblTotal = new JLabel("TỔNG TIỀN: " + formatter.format(phieuXuat.getTongTien()));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(new Color(220, 53, 69));
        pnlTotal.add(lblTotal);
        pnlCenter.add(pnlTotal, BorderLayout.SOUTH);

        add(pnlCenter, BorderLayout.CENTER);

     
       
    }

    private JPanel createField(String title, String val) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(title), BorderLayout.NORTH);
        JTextField txt = new JTextField(val);
        txt.setEditable(false);
        txt.setBackground(Color.WHITE);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    private void loadDataToTable() {
        // Gọi DAO lấy danh sách chi tiết theo Mã phiếu
        ArrayList<ChiTietPhieuXuat> listCT = ChiTietPhieuXuatDAO.getInstance().selectByMaPhieu(phieuXuat.getMaphieu());
        DecimalFormat formatter = new DecimalFormat("###,###,### đ");
        int stt = 1;
        
        for (ChiTietPhieuXuat ct : listCT) {
            String tenSP = "N/A";
            try {
                // Lấy tên SP từ kho
                SanPham sp = SanPhamDAO.getInstance().selectById(ct.getMasp());
                if(sp != null) tenSP = sp.getTenSP();
            } catch (Exception e){}

            long thanhTien = ct.getSoluong() * ct.getDongia();

            tblModel.addRow(new Object[]{
                stt++,
                ct.getMasp(),
                tenSP,
                formatter.format(ct.getDongia()),
                ct.getSoluong(),
                formatter.format(thanhTien)
            });
        }
    }
}