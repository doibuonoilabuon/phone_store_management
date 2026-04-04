package view.Panel;

import controller.TaiKhoanController;
import model.TaiKhoan;
import view.Dialog.TaiKhoanDialog;
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

public class TaiKhoanView extends JPanel implements ActionListener {

    Main main;
    DefaultTableModel tblModel;
    JTable table;
    IntegratedSearch search;
    ButtonToolBar btnGrant, btnEdit, btnLock; // Đổi tên nút để rõ nghĩa hơn
    
    private TaiKhoanController tkController;
    private javax.swing.Timer searchTimer;

    Color BG = new Color(240, 247, 250);

    public TaiKhoanView(Main main) {
        this.main = main;
        tkController = new TaiKhoanController();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        initPadding();
        initContent();
        
        loadDataTable(tkController.getAllList());
    }

    private void initContent() {
        JPanel contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(BG);
        add(contentCenter, BorderLayout.CENTER);

        // ========== THANH CÔNG CỤ (TOOLBAR) ==========
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

        btnGrant = new ButtonToolBar("CẤP TÀI KHOẢN", "add.svg", "create");
        btnEdit  = new ButtonToolBar("SỬA TÀI KHOẢN", "edit.svg", "update"); // Đổi từ Đổi mật khẩu thành Sửa
        btnLock  = new ButtonToolBar("KHÓA TK", "cancel.svg", "delete");

        for (ButtonToolBar btn : new ButtonToolBar[]{btnGrant, btnEdit, btnLock}) {
            btn.addActionListener(this);
            toolbar.add(btn);
        }

        functionBar.add(toolbar);

        // ========== THANH TÌM KIẾM ==========
        search = new IntegratedSearch(new String[]{"Tất cả", "Tên đăng nhập", "Nhóm quyền"});
        
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                if (searchTimer != null && searchTimer.isRunning()) searchTimer.stop(); 
                searchTimer = new javax.swing.Timer(300, e -> {
                    String text = search.txtSearchForm.getText().trim();
                    String type = search.cbxChoose.getSelectedItem().toString();
                    loadDataTable(tkController.search(text, type));
                });
                searchTimer.setRepeats(false);
                searchTimer.start();
            }
        });
        
        search.cbxChoose.addActionListener(e -> {
            loadDataTable(tkController.search(search.txtSearchForm.getText().trim(), search.cbxChoose.getSelectedItem().toString()));
        });

        search.btnReset.addActionListener(e -> {
            search.txtSearchForm.setText("");
            loadDataTable(tkController.getAllList());
        });

        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        // ========== BẢNG HIỂN THỊ DỮ LIỆU ==========
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        String[] cols = {"Tên Đăng Nhập", "Mã NV", "Nhóm Quyền", "Trạng Thái"};
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

    // Hàm load dữ liệu và "dịch thuật" mã nhóm quyền sang tên chức vụ
    public void loadDataTable(ArrayList<TaiKhoan> result) {
        tblModel.setRowCount(0);
        for (TaiKhoan tk : result) {
            String roleName = "Khác";
            if (tk.getManhomquyen() == 1) roleName = "Quản lý kho";
            else if (tk.getManhomquyen() == 2) roleName = "NV Nhập hàng";
            else if (tk.getManhomquyen() == 3) roleName = "NV Xuất hàng";

            tblModel.addRow(new Object[]{
                tk.getUsername(), 
                tk.getManv(), 
                roleName, 
                tk.getTrangthai() == 1 ? "Hoạt động" : "Bị khóa"
            });
        }
    }

    // Lấy đối tượng tài khoản đang được chọn trên bảng
    public TaiKhoan getTaiKhoanSelected() {
        int index = table.getSelectedRow();
        if (index == -1) return null;
        
        String username = table.getValueAt(index, 0).toString();
        // Tìm lại đối tượng chuẩn từ danh sách controller
        for (TaiKhoan tk : tkController.getAllList()) {
            if (tk.getUsername().equals(username)) return tk;
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGrant) {
            // Mở form cấp mới với chế độ ADD
            TaiKhoanDialog dialog = new TaiKhoanDialog(this, main, "Cấp Tài Khoản", true, "ADD", null);
            dialog.setVisible(true);

        } else if (e.getSource() == btnEdit) {
            TaiKhoan tkSelect = getTaiKhoanSelected();
            if (tkSelect != null) {
                // Mở form sửa với chế độ EDIT và truyền đối tượng đã chọn
                TaiKhoanDialog dialog = new TaiKhoanDialog(this, main, "Sửa Tài Khoản", true, "EDIT", tkSelect);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa!");
            }

        } else if (e.getSource() == btnLock) {
            TaiKhoan tkSelect = getTaiKhoanSelected();
            if (tkSelect != null) {
                if(tkSelect.getTrangthai() == 0) {
                    JOptionPane.showMessageDialog(this, "Tài khoản này đã bị khóa từ trước!");
                    return;
                }
                int cf = JOptionPane.showConfirmDialog(this, "Xác nhận khóa tài khoản: " + tkSelect.getUsername() + "?", "Khóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (cf == JOptionPane.YES_OPTION) {
                    if(tkController.lockTaiKhoan(tkSelect.getUsername())) {
                        JOptionPane.showMessageDialog(this, "Đã khóa thành công!");
                        loadDataTable(tkController.getAllList());
                    } else {
                        JOptionPane.showMessageDialog(this, "Khóa thất bại!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần khóa!");
            }
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