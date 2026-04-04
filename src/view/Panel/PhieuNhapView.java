package view.Panel;

import view.Main;
import view.Dialog.ChiTietPhieuNhapDialog;
import view.Form.ButtonToolBar;
import view.Form.IntegratedSearch;
import DAO.PhieuNhapDAO;
import DAO.NhanVienDAO;
import model.PhieuNhap;
import model.NhanVien;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PhieuNhapView extends JPanel implements ActionListener {

    Main main;
    DefaultTableModel tblModel;
    JTable table;
    IntegratedSearch search;
    ButtonToolBar btnAdd, btnDetail, btnCancel, btnExport;
    Color BG = new Color(240, 247, 250);

    public PhieuNhapView(Main main) {
        this.main = main;
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        initPadding();
        initContent();
        
        // Nạp dữ liệu ngay khi mở Tab Nhập Hàng
        loadDataTable(PhieuNhapDAO.getInstance().selectAll());
    }

    private void initContent() {
        JPanel contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(BG);
        add(contentCenter, BorderLayout.CENTER);

        // --- Toolbar ---
        JPanel functionBar = new JPanel(new GridLayout(1, 2, 30, 0));
        functionBar.setBackground(Color.WHITE);
        functionBar.setPreferredSize(new Dimension(0, 95));
        functionBar.setBorder(new EmptyBorder(8, 10, 8, 10));
        functionBar.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        JToolBar toolbar = new JToolBar();
        toolbar.setBackground(Color.WHITE);
        toolbar.setFloatable(false);
        toolbar.setBorderPainted(false);

        btnAdd    = new ButtonToolBar("NHẬP HÀNG",  "add.svg",    "create");
        btnDetail = new ButtonToolBar("CHI TIẾT",   "detail.svg", "view");
        btnCancel = new ButtonToolBar("HỦY PHIẾU",  "cancel.svg", "delete");
       

        for (ButtonToolBar btn : new ButtonToolBar[]{btnAdd, btnDetail, btnCancel}) {
            btn.addActionListener(this);
            toolbar.add(btn);
        }
        functionBar.add(toolbar);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã Phiếu", "Nhà Cung Cấp", "Người Tạo"});
        search.btnReset.addActionListener(this);
        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        // --- Table ---
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        String[] cols = {"STT", "Mã Phiếu", "Nhà Cung Cấp", "Người Lập", "Thời Gian", "Tổng Tiền", "Trạng Thái"};
        tblModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tblModel);
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(center);

        pnlTable.add(new JScrollPane(table));
        contentCenter.add(pnlTable, BorderLayout.CENTER);
    }

    // HÀM QUAN TRỌNG: Đã sửa lỗi hiển thị tên nhân viên và xử lý NPE
    public void loadDataTable(ArrayList<PhieuNhap> list) {
        tblModel.setRowCount(0);
        int stt = 1;
        for (PhieuNhap pn : list) {
            // Xử lý an toàn: Lấy tên thật từ ID nhân viên
            String tenNhanVien = "Mã: " + pn.getManguoitao();
            try {
                NhanVien nv = NhanVienDAO.getInstance().selectById(String.valueOf(pn.getManguoitao()));
                if (nv != null) {
                    tenNhanVien = nv.getHoten(); 
                }
            } catch (Exception ex) {
                // Nếu lỗi, giữ nguyên mã ID để không sập app
            }

            tblModel.addRow(new Object[]{
                stt++,
                "PN" + pn.getMaphieu(),
                pn.getManhacungcap(),
                tenNhanVien,
                pn.getThoigiantao(),
                String.format("%,dđ", pn.getTongTien()),
                pn.getTrangthai() == 1 ? "Hoàn thành" : "Đã hủy"
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            main.setPanel(new TaoPhieuNhapView(main));
        } else if (e.getSource() == btnDetail) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu nhập!");
                return;
            }
            String maphieu = table.getValueAt(row, 1).toString().replace("PN", "");
            PhieuNhap selectedPn = PhieuNhapDAO.getInstance().selectById(maphieu);
            // Mở Dialog chi tiết xịn xò
            ChiTietPhieuNhapDialog dialog = new ChiTietPhieuNhapDialog(main, selectedPn);
            dialog.setVisible(true);
            
        } else if (e.getSource() == btnCancel) {
            int row = table.getSelectedRow();
            if (row == -1) { 
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần hủy!"); 
                return; 
            }
            int cf = JOptionPane.showConfirmDialog(this, "Xác nhận hủy phiếu nhập này?", "Hủy phiếu", JOptionPane.YES_NO_OPTION);
            if (cf == JOptionPane.YES_OPTION) {
                String maphieu = table.getValueAt(row, 1).toString().replace("PN", "");
                PhieuNhapDAO.getInstance().delete(maphieu); // Xóa mềm: Trạng thái về 0
                loadDataTable(PhieuNhapDAO.getInstance().selectAll());
                JOptionPane.showMessageDialog(this, "Đã hủy phiếu thành công!");
            }
        } else if (e.getSource() == search.btnReset) {
            loadDataTable(PhieuNhapDAO.getInstance().selectAll());
        }
    }

    private void initPadding() {
        for (String[] s : new String[][]{{BorderLayout.NORTH,"10,0"},{BorderLayout.SOUTH,"10,0"},{BorderLayout.EAST,"0,10"},{BorderLayout.WEST,"0,10"}}) {
            JPanel p = new JPanel(); p.setBackground(BG);
            int h = Integer.parseInt(s[1].split(",")[0]), w = Integer.parseInt(s[1].split(",")[1]);
            p.setPreferredSize(new Dimension(w, h)); add(p, s[0]);
        }
    }
}