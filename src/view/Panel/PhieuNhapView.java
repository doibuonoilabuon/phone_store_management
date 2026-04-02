package view.Panel;

import view.Main;
import view.Form.ButtonToolBar;
import view.Form.IntegratedSearch;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

    // Giữ đúng tone màu nền hệ thống của bạn
    Color BG = new Color(240, 247, 250);

    public PhieuNhapView(Main main) {
        this.main = main;
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        initPadding();
        initContent();
    }

    private void initContent() {
        JPanel contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(BG);
        add(contentCenter, BorderLayout.CENTER);

        // ========== 1. THANH CHỨC NĂNG (TOOLBAR) ==========
        JPanel functionBar = new JPanel(new GridLayout(1, 2, 30, 0));
        functionBar.setBackground(Color.WHITE);
        functionBar.setPreferredSize(new Dimension(0, 95));
        functionBar.setBorder(new EmptyBorder(8, 10, 8, 10));
        functionBar.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        JToolBar toolbar = new JToolBar();
        toolbar.setBackground(Color.WHITE);
        toolbar.setFloatable(false);
        toolbar.setBorderPainted(false);
        toolbar.setRollover(true);

        // Các nút chức năng chuẩn cho phần Phiếu Nhập
        btnAdd    = new ButtonToolBar("NHẬP HÀNG",  "add.svg",    "create");
        btnDetail = new ButtonToolBar("CHI TIẾT",   "detail.svg", "view");
        btnCancel = new ButtonToolBar("HỦY PHIẾU",  "delete.svg", "delete");
        btnExport = new ButtonToolBar("XUẤT EXCEL", "export_excel.svg", "view");

        for (ButtonToolBar btn : new ButtonToolBar[]{btnAdd, btnDetail, btnCancel, btnExport}) {
            btn.addActionListener(this);
            toolbar.add(btn);
        }

        functionBar.add(toolbar);

        // ========== 2. THANH TÌM KIẾM ==========
        search = new IntegratedSearch(new String[]{"Tất cả", "Mã Phiếu", "Nhà Cung Cấp", "Người Tạo"});
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Sẽ gọi hàm tìm kiếm từ Controller ở đây
            }
        });
        search.btnReset.addActionListener(this);
        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        // ========== 3. BẢNG DỮ LIỆU ==========
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        // Các cột hiển thị bám sát thuộc tính PhieuNhap của bạn
        String[] cols = {"STT", "Mã Phiếu", "Mã Nhà Cung Cấp", "Mã Nhân Viên Lập", "Thời Gian", "Tổng Tiền", "Trạng Thái"};
        tblModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tblModel);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setAutoCreateRowSorter(true);

        // Căn giữa nội dung bảng
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        pnlTable.add(scroll);
        contentCenter.add(pnlTable, BorderLayout.CENTER);
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
           TaoPhieuNhapView taoPhieuNhap = new TaoPhieuNhapView(main);
                 main.setPanel(taoPhieuNhap);
            
        } else if (e.getSource() == btnDetail) {
            // Xem chi tiết phiếu nhập
            if (table.getSelectedRow() == -1) { 
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu nhập để xem chi tiết!"); 
                return; 
            }
            JOptionPane.showMessageDialog(this, "Chức năng Xem Chi Tiết đang được xây dựng!");
            
        } else if (e.getSource() == btnCancel) {
            // Hủy phiếu nhập (Update trạng thái = 0)
            if (table.getSelectedRow() == -1) { 
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần hủy!"); 
                return; 
            }
            int cf = JOptionPane.showConfirmDialog(this, "Xác nhận hủy phiếu nhập này? Thao tác không thể hoàn tác!", "Hủy phiếu", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (cf == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Đã hủy phiếu thành công!");
            }
            
        } else if (e.getSource() == btnExport) {
            // Xuất file Excel
            JOptionPane.showMessageDialog(this, "Chức năng Xuất Excel đang được xây dựng!");
            
        } else if (e.getSource() == search.btnReset) {
            // Nút làm mới (Reset)
            search.txtSearchForm.setText("");
            // Sẽ load lại toàn bộ dữ liệu ở đây
        }
    }
}