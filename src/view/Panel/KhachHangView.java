package view.Panel;

import view.Main;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import controller.KhachHangController;
import view.Dialog.KhachHangDialog;
import view.Form.ButtonToolBar;
import view.Form.IntegratedSearch;
import model.KhachHang;

public class KhachHangView extends JPanel implements ActionListener {
    private javax.swing.Timer searchTimer;
    Main m;
    JTable tableKhachHang;
    DefaultTableModel tblModel;
    IntegratedSearch search;
    ButtonToolBar btnAdd, btnEdit, btnDelete, btnDetail;
    private KhachHangController khController;
    public ArrayList<model.KhachHang> listkh = new ArrayList<>();
    Color BG = new Color(240, 247, 250);

   public KhachHangView(Main m) {
        this.m = m;
        khController = new KhachHangController(); // Khởi tạo controller
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        initPadding();
        initContent();
        loadDataTable(khController.getAllList()); // Tự động load dữ liệu từ DB khi mở form
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
       search.txtSearchForm.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                // 1. Nếu Timer đang chạy (tức là người dùng vẫn đang gõ liên tục), thì hủy bỏ đợt tìm kiếm cũ
                if (searchTimer != null && searchTimer.isRunning()) {
                    searchTimer.stop(); 
                }
                
                // 2. Tạo một đợt tìm kiếm mới, nhưng hẹn giờ 300ms (0.3 giây) sau mới chạy
                searchTimer = new javax.swing.Timer(300, e -> {
                    String text = search.txtSearchForm.getText().trim();
                    String type = search.cbxChoose.getSelectedItem().toString();
                    
                    // Chỉ gọi DB 1 lần duy nhất sau khi người dùng đã ngừng gõ
                    loadDataTable(khController.search(text, type));
                });
                
                searchTimer.setRepeats(false); // Đảm bảo Timer chỉ đếm 1 lần rồi tắt
                searchTimer.start(); // Bắt đầu đếm ngược
            }
        });

        // 2. Bắt sự kiện thay đổi ComboBox (nếu người dùng đổi từ "Tất cả" sang "Tên KH" thì cũng tự lọc lại luôn)
        search.cbxChoose.addActionListener(e -> {
            String text = search.txtSearchForm.getText().trim();
            String type = search.cbxChoose.getSelectedItem().toString();
            loadDataTable(khController.search(text, type));
        });

        // 3. Nút Làm mới (Do file IntegratedSearch chỉ mới reset text, ta cần gọi thêm hàm load lại bảng)
        search.btnReset.addActionListener(e -> {
            loadDataTable(khController.getAllList());
        });
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        String[] header = {"Mã KH", "Tên Khách Hàng", "Địa Chỉ", "Số Điện Thoại", "Email"};
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
        tblModel.setRowCount(0); // Xóa dữ liệu cũ trên bảng
        for (model.KhachHang kh : result) {
            tblModel.addRow(new Object[]{
                kh.getMaKH(), 
                kh.getTenKH(), 
                kh.getDiaChi(), 
                kh.getSoDienThoai(), 
                kh.getEmail() 
            });
        }
    }

   public KhachHang getKhachHangSelected() {
        int row = tableKhachHang.getSelectedRow();
        if (row == -1) return null;
        String maKH = tableKhachHang.getValueAt(row, 0).toString();
        String tenKH = tableKhachHang.getValueAt(row, 1).toString();
        String diaChi = tableKhachHang.getValueAt(row, 2).toString();
        String sdt = tableKhachHang.getValueAt(row, 3).toString();
        String email = tableKhachHang.getValueAt(row, 4).toString();
        return new KhachHang(maKH, tenKH, sdt, diaChi, email);
    }

    
   @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            // Mở Dialog Thêm
            KhachHangDialog dialog = new KhachHangDialog(this, null, "Thêm Khách Hàng", true, "ADD", null);
            dialog.setVisible(true);

        } else if (e.getSource() == btnEdit) {
            KhachHang khSelect = getKhachHangSelected();
            if (khSelect != null) {
                // Mở Dialog Sửa, truyền đối tượng đang chọn vào
                KhachHangDialog dialog = new KhachHangDialog(this, null, "Sửa Khách Hàng", true, "EDIT", khSelect);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
            }

        } else if (e.getSource() == btnDelete) {
            KhachHang khSelect = getKhachHangSelected();
            if (khSelect != null) {
                int cf = JOptionPane.showConfirmDialog(this, "Xác nhận xóa khách hàng " + khSelect.getTenKH() + "?", "Xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (cf == JOptionPane.YES_OPTION) {
                    if(khController.deleteKhachHang(khSelect.getMaKH())) {
                        JOptionPane.showMessageDialog(this, "Đã xóa!");
                        loadDataTable(khController.getAllList()); // Load lại bảng
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
            }

        } else if (e.getSource() == btnDetail) {
            KhachHang khSelect = getKhachHangSelected();
            if (khSelect != null) {
                // Mở Dialog Chi tiết
                KhachHangDialog dialog = new KhachHangDialog(this, null, "Chi Tiết Khách Hàng", true, "DETAIL", khSelect);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng xem chi tiết!");
            }
        }
    }

}