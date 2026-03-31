package view.Panel;

import view.Main;
import view.ButtonToolBar;
import view.IntegratedSearch;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class KhachHangView extends JPanel implements ActionListener {

    Main m;
    JTable tableKhachHang;
    DefaultTableModel tblModel;
    IntegratedSearch search;
    ButtonToolBar btnAdd, btnEdit, btnDelete, btnDetail;

    public ArrayList<model.KhachHang> listkh = new ArrayList<>();
    Color BG = new Color(240, 247, 250);

    public KhachHangView(Main m) {
        this.m = m;
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        initPadding();
        initContent();
        loadDataTable(listkh);
    }

    private void initContent() {
        JPanel contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(BG);
        add(contentCenter, BorderLayout.CENTER);

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

        btnAdd    = new ButtonToolBar("THÊM",       "add.svg",    "create");
        btnEdit   = new ButtonToolBar("SỬA",        "edit.svg",   "update");
        btnDelete = new ButtonToolBar("XÓA",        "delete.svg", "delete");
        btnDetail = new ButtonToolBar("CHI TIẾT",   "detail.svg", "view");

        for (ButtonToolBar btn : new ButtonToolBar[]{btnAdd, btnEdit, btnDelete, btnDetail}) {
            btn.addActionListener(this);
            toolbar.add(btn);
        }

        functionBar.add(toolbar);

        search = new IntegratedSearch(new String[]{"Tất cả", "Tên KH", "Số điện thoại"});
        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        String[] header = {"Mã KH", "Tên Khách Hàng", "Địa Chỉ", "Số Điện Thoại", "Ngày Tham Gia"};
        tblModel = new DefaultTableModel(header, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableKhachHang = new JTable(tblModel);
        tableKhachHang.setShowVerticalLines(false);
        tableKhachHang.setIntercellSpacing(new Dimension(0, 0));
        tableKhachHang.setFocusable(false);
        tableKhachHang.setAutoCreateRowSorter(true);
        tableKhachHang.setDefaultEditor(Object.class, null);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableKhachHang.getColumnCount(); i++) {
            if (i != 1) tableKhachHang.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scroll = new JScrollPane(tableKhachHang);
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

    public void loadDataTable(ArrayList<model.KhachHang> result) {
        tblModel.setRowCount(0);
        // Sau này nối DB
    }

    public int getRowSelected() {
        int index = tableKhachHang.getSelectedRow();
        if (index == -1) JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!");
        return index;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            JOptionPane.showMessageDialog(this, "Chức năng Thêm Khách Hàng đang được xây dựng!");
        } else if (e.getSource() == btnEdit) {
            if (getRowSelected() != -1) JOptionPane.showMessageDialog(this, "Chức năng Sửa đang được xây dựng!");
        } else if (e.getSource() == btnDelete) {
            int index = getRowSelected();
            if (index != -1) {
                int cf = JOptionPane.showConfirmDialog(this, "Xác nhận xóa khách hàng?", "Xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (cf == JOptionPane.YES_OPTION) JOptionPane.showMessageDialog(this, "Đã xóa!");
            }
        } else if (e.getSource() == btnDetail) {
            if (getRowSelected() != -1) JOptionPane.showMessageDialog(this, "Chi tiết đang được xây dựng!");
        }
    }
}