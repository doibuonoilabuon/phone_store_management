package view.Panel;

import controller.NhanVienController;
import model.NhanVien;
import view.Dialog.NhanVienDialog;
import view.Form.ButtonToolBar;
import view.Form.IntegratedSearch;
import view.Main;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class NhanVienView extends JPanel implements ActionListener {

    Main main;
    DefaultTableModel tblModel;
    JTable table;
    IntegratedSearch search;
    ButtonToolBar btnAdd, btnEdit, btnDelete;
    
    private NhanVienController nvController;
    private javax.swing.Timer searchTimer;

    Color BG = new Color(240, 247, 250);

    public NhanVienView(Main main) {
        this.main = main;
        nvController = new NhanVienController();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        initPadding();
        initContent();
        
        loadDataTable(nvController.getAllList());
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

        btnAdd    = new ButtonToolBar("THÊM",    "add.svg",    "create");
        btnEdit   = new ButtonToolBar("SỬA",     "edit.svg",   "update");
        btnDelete = new ButtonToolBar("XÓA",     "delete.svg", "delete");

        for (ButtonToolBar btn : new ButtonToolBar[]{btnAdd, btnEdit, btnDelete}) {
            btn.addActionListener(this);
            toolbar.add(btn);
        }

        functionBar.add(toolbar);

        search = new IntegratedSearch(new String[]{"Tất cả", "Tên NV", "Số điện thoại"});
        
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                if (searchTimer != null && searchTimer.isRunning()) searchTimer.stop(); 
                searchTimer = new javax.swing.Timer(300, e -> {
                    String text = search.txtSearchForm.getText().trim();
                    String type = search.cbxChoose.getSelectedItem().toString();
                    loadDataTable(nvController.search(text, type));
                });
                searchTimer.setRepeats(false);
                searchTimer.start();
            }
        });
        
        search.cbxChoose.addActionListener(e -> {
            loadDataTable(nvController.search(search.txtSearchForm.getText().trim(), search.cbxChoose.getSelectedItem().toString()));
        });

        search.btnReset.addActionListener(e -> loadDataTable(nvController.getAllList()));

        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        String[] cols = {"Mã NV", "Tên Nhân Viên", "Giới Tính", "Số Điện Thoại", "Email", "Chức Vụ"};
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

    public void loadDataTable(ArrayList<NhanVien> result) {
        tblModel.setRowCount(0);
        for (NhanVien nv : result) {
            tblModel.addRow(new Object[]{
                nv.getManv(), 
                nv.getHoten(), 
                nv.getGioitinh() == 1 ? "Nam" : "Nữ", 
                nv.getSdt(), 
                nv.getEmail(), 
                nv.getChucvu()
            });
        }
    }

    public NhanVien getNhanVienSelected() {
        int index = table.getSelectedRow();
        if (index == -1) return null;
        
        int maNV = Integer.parseInt(table.getValueAt(index, 0).toString());
        for (NhanVien nv : nvController.getAllList()) {
            if (nv.getManv() == maNV) return nv;
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            NhanVienDialog dialog = new NhanVienDialog(this, null, "Thêm Nhân Viên", true, "ADD", null);
            dialog.setVisible(true);

        } else if (e.getSource() == btnEdit) {
            NhanVien nvSelect = getNhanVienSelected();
            if (nvSelect != null) {
                NhanVienDialog dialog = new NhanVienDialog(this, null, "Sửa Nhân Viên", true, "EDIT", nvSelect);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
            }

        } else if (e.getSource() == btnDelete) {
            NhanVien nvSelect = getNhanVienSelected();
            if (nvSelect != null) {
                int cf = JOptionPane.showConfirmDialog(this, "Xác nhận cho nghỉ việc: " + nvSelect.getHoten() + "?", "Xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (cf == JOptionPane.YES_OPTION) {
                    if(nvController.deleteNhanVien(nvSelect.getManv())) {
                        JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái nghỉ việc!");
                        loadDataTable(nvController.getAllList());
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
            }
        }
    }
}