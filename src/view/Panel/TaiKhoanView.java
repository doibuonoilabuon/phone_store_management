package view.Panel;

import view.Main;
import view.ButtonToolBar;
import view.IntegratedSearch;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class TaiKhoanView extends JPanel implements ActionListener {

    Main main;
    DefaultTableModel tblModel;
    JTable table;
    IntegratedSearch search;
    ButtonToolBar btnGrant, btnChangePass, btnLock;

    Color BG = new Color(240, 247, 250);

    public TaiKhoanView(Main main) {
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

        btnGrant      = new ButtonToolBar("CẤP TÀI KHOẢN", "add.svg",    "create");
        btnChangePass = new ButtonToolBar("ĐỔI MẬT KHẨU",  "edit.svg",   "update");
        btnLock       = new ButtonToolBar("KHÓA TK",        "cancel.svg", "delete");

        for (ButtonToolBar btn : new ButtonToolBar[]{btnGrant, btnChangePass, btnLock}) {
            btn.addActionListener(this);
            toolbar.add(btn);
        }

        functionBar.add(toolbar);

        search = new IntegratedSearch(new String[]{"Tất cả", "Tên đăng nhập", "Nhóm quyền"});
        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        String[] cols = {"Tên Đăng Nhập", "Tên Nhân Viên", "Nhóm Quyền", "Trạng Thái"};
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

    private void initPadding() {
        for (String[] s : new String[][]{{BorderLayout.NORTH,"10,0"},{BorderLayout.SOUTH,"10,0"},{BorderLayout.EAST,"0,10"},{BorderLayout.WEST,"0,10"}}) {
            JPanel p = new JPanel(); p.setBackground(BG);
            int h = Integer.parseInt(s[1].split(",")[0]), w = Integer.parseInt(s[1].split(",")[1]);
            p.setPreferredSize(new Dimension(w, h)); add(p, s[0]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGrant) {
            JOptionPane.showMessageDialog(this, "Chức năng Cấp Tài Khoản đang được xây dựng!");
        } else if (e.getSource() == btnChangePass) {
            if (table.getSelectedRow() == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản!"); return; }
            JOptionPane.showMessageDialog(this, "Chức năng Đổi Mật Khẩu đang được xây dựng!");
        } else if (e.getSource() == btnLock) {
            if (table.getSelectedRow() == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản!"); return; }
            int cf = JOptionPane.showConfirmDialog(this, "Xác nhận khóa tài khoản?", "Khóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (cf == JOptionPane.YES_OPTION) JOptionPane.showMessageDialog(this, "Đã khóa!");
        }
    }
}