package view.Panel;

import view.Main;
import view.ButtonToolBar;
import view.IntegratedSearch;
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

public class SanPhamView extends JPanel implements ActionListener {

    Main main;
    DefaultTableModel tblModel;
    JTable table;
    IntegratedSearch search;

    ButtonToolBar btnAdd, btnEdit, btnDelete, btnDetail, btnXemDS, btnExport;

    Color BG = new Color(240, 247, 250);

    public SanPhamView(Main main) {
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

        // ========== TOOLBAR ==========
        JPanel functionBar = new JPanel(new GridLayout(1, 2, 30, 0));
        functionBar.setBackground(Color.WHITE);
        functionBar.setPreferredSize(new Dimension(0, 95));
        functionBar.setBorder(new EmptyBorder(8, 10, 8, 10));
        functionBar.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        // Nhóm nút trái: JToolBar để có khoảng cách đẹp
        JToolBar toolbar = new JToolBar();
        toolbar.setBackground(Color.WHITE);
        toolbar.setFloatable(false);
        toolbar.setBorderPainted(false);
        toolbar.setRollover(true);

        btnAdd    = new ButtonToolBar("THÊM",        "add.svg",         "create");
        btnEdit   = new ButtonToolBar("SỬA",         "edit.svg",        "update");
        btnDelete = new ButtonToolBar("XÓA",         "delete.svg",      "delete");
        btnDetail = new ButtonToolBar("CHI TIẾT",    "detail.svg",      "view");
        btnXemDS  = new ButtonToolBar("XEM DS",      "phone.svg",       "view");
        btnExport = new ButtonToolBar("XUẤT EXCEL",  "export_excel.svg","view");

        for (ButtonToolBar btn : new ButtonToolBar[]{btnAdd, btnEdit, btnDelete, btnDetail, btnXemDS, btnExport}) {
            btn.addActionListener(this);
            toolbar.add(btn);
        }

        functionBar.add(toolbar);

        // Nhóm tìm kiếm phải
        search = new IntegratedSearch(new String[]{"Tất cả", "Tên SP", "Thương hiệu"});
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                // sau này kết nối BUS để tìm kiếm live
            }
        });
        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        // ========== BẢNG DỮ LIỆU ==========
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        String[] cols = {"Mã SP", "Tên sản phẩm", "Số lượng tồn", "Thương hiệu",
                         "Hệ điều hành", "Kích thước màn", "Chip xử lý", "Dung lượng pin", "Xuất xứ", "Khu vực kho"};
        tblModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tblModel);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 1) table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        pnlTable.add(scroll);
        contentCenter.add(pnlTable, BorderLayout.CENTER);
    }

    private void initPadding() {
        addBorder(BorderLayout.NORTH, 10, 0);
        addBorder(BorderLayout.SOUTH, 10, 0);
        addBorder(BorderLayout.EAST, 0, 10);
        addBorder(BorderLayout.WEST, 0, 10);
    }

    private void addBorder(String pos, int h, int w) {
        JPanel p = new JPanel();
        p.setBackground(BG);
        p.setPreferredSize(new Dimension(w, h));
        add(p, pos);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            JOptionPane.showMessageDialog(this, "Chức năng Thêm Sản Phẩm đang được xây dựng!");
        } else if (e.getSource() == btnEdit) {
            if (table.getSelectedRow() == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!"); return; }
            JOptionPane.showMessageDialog(this, "Chức năng Sửa đang được xây dựng!");
        } else if (e.getSource() == btnDelete) {
            if (table.getSelectedRow() == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!"); return; }
            int cf = JOptionPane.showConfirmDialog(this, "Xác nhận xóa sản phẩm?", "Xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (cf == JOptionPane.YES_OPTION) JOptionPane.showMessageDialog(this, "Đã xóa!");
        } else if (e.getSource() == btnDetail) {
            if (table.getSelectedRow() == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!"); return; }
            JOptionPane.showMessageDialog(this, "Chức năng Chi Tiết đang được xây dựng!");
        } else if (e.getSource() == btnXemDS) {
            JOptionPane.showMessageDialog(this, "Chức năng Xem Danh Sách đang được xây dựng!");
        } else if (e.getSource() == btnExport) {
            JOptionPane.showMessageDialog(this, "Chức năng Xuất Excel đang được xây dựng!");
        }
    }
}