package view.Dialog;

import DAO.*;
import model.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ChiTietPhieuNhapDialog extends JDialog {
    private PhieuNhap pn;
    private DefaultTableModel tblModelSP;
    private JTable tableSP;

    public ChiTietPhieuNhapDialog(JFrame parent, PhieuNhap pn) {
        super(parent, "Chi tiết phiếu nhập", true);
        this.pn = pn;
        setSize(1000, 600);
        setLocationRelativeTo(parent);
        initComponent();
        loadDataSP();
    }

    private void initComponent() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(22, 122, 197));
        JLabel lblTitle = new JLabel("THÔNG TIN PHIẾU NHẬP");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // Info
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JPanel pnlInfo = new JPanel(new GridLayout(1, 4, 15, 0));
        // Fix lỗi lấy tên nhân viên
        NhanVien nv = NhanVienDAO.getInstance().selectById(String.valueOf(pn.getManguoitao()));
        String tenNV = (nv != null) ? nv.getHoten() : "NV mã: " + pn.getManguoitao();

        pnlInfo.add(createField("Mã phiếu", "PN" + pn.getMaphieu()));
        pnlInfo.add(createField("Người lập", tenNV));
        pnlInfo.add(createField("Nhà cung cấp", pn.getManhacungcap()));
        pnlInfo.add(createField("Ngày nhập", pn.getThoigiantao().toString()));
        pnlCenter.add(pnlInfo, BorderLayout.NORTH);

        // Table (Bản rộng, không chia đôi)
        String[] cols = {"STT", "Mã SP", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"};
        tblModelSP = new DefaultTableModel(cols, 0);
        tableSP = new JTable(tblModelSP);
        tableSP.setRowHeight(30);
        tableSP.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlCenter.add(new JScrollPane(tableSP), BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);
        
        
    }

    private JPanel createField(String title, String val) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(title), BorderLayout.NORTH);
        JTextField txt = new JTextField(val);
        txt.setEditable(false);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    private void loadDataSP() {
        ArrayList<ChiTietPhieuNhap> list = ChiTietPhieuNhapDAO.getInstance().selectAll(String.valueOf(pn.getMaphieu()));
        tblModelSP.setRowCount(0);
        int stt = 1;
        for (ChiTietPhieuNhap ct : list) {
            SanPham sp = SanPhamDAO.getInstance().selectById(ct.getMasp());
            long thanhTien = ct.getSoluong() * ct.getDongia();
            tblModelSP.addRow(new Object[]{
                stt++, ct.getMasp(), (sp!=null?sp.getTenSP():"N/A"), 
                String.format("%,dđ", ct.getDongia()), ct.getSoluong(), String.format("%,dđ", thanhTien)
            });
        }
    }
}