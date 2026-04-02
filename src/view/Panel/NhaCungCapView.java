package view.Panel;

import controller.NhaCungCapController;
import model.NhaCungCap;
import view.Dialog.NhaCungCapDialog;
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

public class NhaCungCapView extends JPanel implements ActionListener {

    Main main;
    DefaultTableModel tblModel;
    JTable table;
    IntegratedSearch search;
    ButtonToolBar btnAdd, btnEdit, btnDelete, btnDetail;
    
    private NhaCungCapController nccController;
    private javax.swing.Timer searchTimer;

    Color BG = new Color(240, 247, 250);

    public NhaCungCapView(Main main) {
        this.main = main;
        nccController = new NhaCungCapController();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        initPadding();
        initContent();
        
        // Gọi hàm load dữ liệu ngay khi mở form
        loadDataTable(nccController.getAllList());
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

        btnAdd    = new ButtonToolBar("THÊM",     "add.svg",    "create");
        btnEdit   = new ButtonToolBar("SỬA",      "edit.svg",   "update");
        btnDelete = new ButtonToolBar("XÓA",      "delete.svg", "delete");
        btnDetail = new ButtonToolBar("CHI TIẾT", "detail.svg", "view");

        for (ButtonToolBar btn : new ButtonToolBar[]{btnAdd, btnEdit, btnDelete, btnDetail}) {
            btn.addActionListener(this);
            toolbar.add(btn);
        }

        functionBar.add(toolbar);

        search = new IntegratedSearch(new String[]{"Tất cả", "Tên NCC", "Số điện thoại"});
        
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                if (searchTimer != null && searchTimer.isRunning()) {
                    searchTimer.stop(); 
                }
                searchTimer = new javax.swing.Timer(300, e -> {
                    String text = search.txtSearchForm.getText().trim();
                    String type = search.cbxChoose.getSelectedItem().toString();
                    loadDataTable(nccController.search(text, type));
                });
                searchTimer.setRepeats(false);
                searchTimer.start();
            }
        });
        
        search.cbxChoose.addActionListener(e -> {
            String text = search.txtSearchForm.getText().trim();
            String type = search.cbxChoose.getSelectedItem().toString();
            loadDataTable(nccController.search(text, type));
        });

        search.btnReset.addActionListener(e -> {
            loadDataTable(nccController.getAllList());
        });

        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        String[] cols = {"Mã NCC", "Tên Nhà Cung Cấp", "Địa Chỉ", "Số Điện Thoại", "Email"};
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
            if (i != 1) table.getColumnModel().getColumn(i).setCellRenderer(center);
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

  
    public void loadDataTable(ArrayList<NhaCungCap> result) {
        tblModel.setRowCount(0);
        for (NhaCungCap ncc : result) {
            tblModel.addRow(new Object[]{
                ncc.getMaNCC(), 
                ncc.getTenNCC(), 
                ncc.getDiaChi(),
                ncc.getSoDienThoai(), 
                ncc.getEmail()
            });
        }
    }

    public NhaCungCap getNhaCungCapSelected() {
        int index = table.getSelectedRow();
        if (index == -1) return null;
        
        String maNCC = table.getValueAt(index, 0).toString();
        for (NhaCungCap ncc : nccController.getAllList()) {
            if (ncc.getMaNCC().equals(maNCC)) {
                return ncc;
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            NhaCungCapDialog dialog = new NhaCungCapDialog(this, null, "Thêm Nhà Cung Cấp", true, "ADD", null);
            dialog.setVisible(true);

        } else if (e.getSource() == btnEdit) {
            NhaCungCap nccSelect = getNhaCungCapSelected();
            if (nccSelect != null) {
                NhaCungCapDialog dialog = new NhaCungCapDialog(this, null, "Sửa Nhà Cung Cấp", true, "EDIT", nccSelect);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần sửa!");
            }

        } else if (e.getSource() == btnDelete) {
            NhaCungCap nccSelect = getNhaCungCapSelected();
            if (nccSelect != null) {
                int cf = JOptionPane.showConfirmDialog(this, "Xác nhận xóa nhà cung cấp: " + nccSelect.getTenNCC() + "?", "Xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (cf == JOptionPane.YES_OPTION) {
                    if(nccController.deleteNhaCungCap(nccSelect.getMaNCC())) {
                        JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                        loadDataTable(nccController.getAllList());
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần xóa!");
            }

        } else if (e.getSource() == btnDetail) {
            NhaCungCap nccSelect = getNhaCungCapSelected();
            if (nccSelect != null) {
                NhaCungCapDialog dialog = new NhaCungCapDialog(this, null, "Chi Tiết Nhà Cung Cấp", true, "DETAIL", nccSelect);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp xem chi tiết!");
            }
        }
    }
}