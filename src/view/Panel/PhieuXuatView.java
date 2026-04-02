package view.Panel;

import view.Main;
import view.Form.ButtonToolBar;
import view.Form.IntegratedSearch;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    
    // Các component cho thanh Lọc (Filter) bên trái
    JComboBox<String> cbxKhachHang, cbxNhanVien;
    JTextField txtDateStart, txtDateEnd, txtMoneyMin, txtMoneyMax;

    Color BG = new Color(240, 247, 250);

    public PhieuXuatView(Main main) {
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

        // ========== 1. THANH CHỨC NĂNG (TOOLBAR) & TÌM KIẾM ==========
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

        btnAdd    = new ButtonToolBar("XUẤT HÀNG",  "add.svg",    "create");
        btnDetail = new ButtonToolBar("CHI TIẾT",   "detail.svg", "view");
        btnCancel = new ButtonToolBar("HỦY PHIẾU",  "delete.svg", "delete");
        btnExport = new ButtonToolBar("XUẤT EXCEL", "export_excel.svg", "view");

        for (ButtonToolBar btn : new ButtonToolBar[]{btnAdd, btnDetail, btnCancel, btnExport}) {
            btn.addActionListener(this);
            toolbar.add(btn);
        }
        functionBar.add(toolbar);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã Phiếu", "Khách Hàng", "Người Tạo"});
        search.btnReset.addActionListener(this);
        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        // ========== 2. THANH LỌC NÂNG CAO (BÊN TRÁI) ==========
        JPanel pnlFilter = new JPanel(new GridLayout(6, 1, 0, 15));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setPreferredSize(new Dimension(250, 0));
        pnlFilter.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlFilter.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        // Combobox Khách hàng
        JPanel pnlKhachHang = new JPanel(new BorderLayout(0, 5));
        pnlKhachHang.setBackground(Color.WHITE);
        pnlKhachHang.add(new JLabel("Khách hàng:"), BorderLayout.NORTH);
        cbxKhachHang = new JComboBox<>(new String[]{"Tất cả"}); // Sẽ load data từ DB lên sau
        pnlKhachHang.add(cbxKhachHang, BorderLayout.CENTER);
        pnlFilter.add(pnlKhachHang);

        // Combobox Nhân viên
        JPanel pnlNhanVien = new JPanel(new BorderLayout(0, 5));
        pnlNhanVien.setBackground(Color.WHITE);
        pnlNhanVien.add(new JLabel("Nhân viên xuất:"), BorderLayout.NORTH);
        cbxNhanVien = new JComboBox<>(new String[]{"Tất cả"}); // Sẽ load data từ DB lên sau
        pnlNhanVien.add(cbxNhanVien, BorderLayout.CENTER);
        pnlFilter.add(pnlNhanVien);

        // Khoảng thời gian
        JPanel pnlDate = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlDate.setBackground(Color.WHITE);
        JPanel pnlDateStart = createInputPanel("Từ ngày:", txtDateStart = new JTextField());
        txtDateStart.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "dd/MM/yyyy");
        JPanel pnlDateEnd = createInputPanel("Đến ngày:", txtDateEnd = new JTextField());
        txtDateEnd.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "dd/MM/yyyy");
        pnlDate.add(pnlDateStart);
        pnlDate.add(pnlDateEnd);
        pnlFilter.add(pnlDate);

        // Khoảng giá tiền
        JPanel pnlMoney = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlMoney.setBackground(Color.WHITE);
        JPanel pnlMoneyMin = createInputPanel("Từ số tiền:", txtMoneyMin = new JTextField());
        txtMoneyMin.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VNĐ");
        JPanel pnlMoneyMax = createInputPanel("Đến số tiền:", txtMoneyMax = new JTextField());
        txtMoneyMax.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "VNĐ");
        pnlMoney.add(pnlMoneyMin);
        pnlMoney.add(pnlMoneyMax);
        pnlFilter.add(pnlMoney);

        // Nút áp dụng bộ lọc (Tùy chọn thêm để dễ bấm)
        JButton btnApplyFilter = new JButton("Áp dụng Lọc");
        btnApplyFilter.setBackground(new Color(1, 87, 155));
        btnApplyFilter.setForeground(Color.WHITE);
        btnApplyFilter.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        pnlFilter.add(btnApplyFilter);

        contentCenter.add(pnlFilter, BorderLayout.WEST);

        // ========== 3. BẢNG DỮ LIỆU ==========
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        String[] cols = {"STT", "Mã Phiếu", "Mã Khách Hàng", "Mã Nhân Viên Lập", "Thời Gian", "Tổng Tiền", "Trạng Thái"};
        tblModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tblModel);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setAutoCreateRowSorter(true);

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

    // Hàm hỗ trợ tạo các ô nhập liệu cho code ngắn gọn
    private JPanel createInputPanel(String title, JTextField txt) {
        JPanel pnl = new JPanel(new BorderLayout(0, 5));
        pnl.setBackground(Color.WHITE);
        pnl.add(new JLabel(title), BorderLayout.NORTH);
        pnl.add(txt, BorderLayout.CENTER);
        return pnl;
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
            TaoPhieuXuatView taoPhieuXuat = new TaoPhieuXuatView(main);
                 main.setPanel(taoPhieuXuat);
        } else if (e.getSource() == btnDetail) {
            if (table.getSelectedRow() == -1) { 
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu xuất để xem chi tiết!"); 
                return; 
            }
            JOptionPane.showMessageDialog(this, "Chức năng Xem Chi Tiết đang được xây dựng!");
        } else if (e.getSource() == btnCancel) {
            if (table.getSelectedRow() == -1) { 
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần hủy!"); 
                return; 
            }
            int cf = JOptionPane.showConfirmDialog(this, "Xác nhận hủy phiếu xuất này? Thao tác không thể hoàn tác!", "Hủy phiếu", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (cf == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Đã hủy phiếu xuất thành công!");
            }
        } else if (e.getSource() == btnExport) {
            JOptionPane.showMessageDialog(this, "Chức năng Xuất Excel đang được xây dựng!");
        } else if (e.getSource() == search.btnReset) {
            search.txtSearchForm.setText("");
            cbxKhachHang.setSelectedIndex(0);
            cbxNhanVien.setSelectedIndex(0);
            txtDateStart.setText("");
            txtDateEnd.setText("");
            txtMoneyMin.setText("");
            txtMoneyMax.setText("");
        }
    }
}