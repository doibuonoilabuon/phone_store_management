package view.Panel;

import controller.SanPhamController;
import model.SanPham;
import view.Dialog.SanPhamDialog;
import view.Form.ButtonToolBar;
import view.Form.IntegratedSearch;
import view.Main;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
    
    private SanPhamController spController;
    private javax.swing.Timer searchTimer;

    Color BG = new Color(240, 247, 250);

    public SanPhamView(Main main) {
        this.main = main;
        spController = new SanPhamController();
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        initPadding();
        initContent();
        
        // Load dữ liệu lần đầu khi mở giao diện
        loadDataTable(spController.getAllList());
    }

    private void initContent() {
        JPanel contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(BG);
        add(contentCenter, BorderLayout.CENTER);

        // ========== TOOLBAR CHỨA NÚT VÀ TÌM KIẾM ==========
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

        // ========== KHU VỰC TÌM KIẾM TÍCH HỢP ==========
        search = new IntegratedSearch(new String[]{"Tất cả", "Tên SP", "Thương hiệu"});
        
        // Bắt sự kiện gõ phím (Có chống lag - Debounce 300ms)
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                if (searchTimer != null && searchTimer.isRunning()) {
                    searchTimer.stop(); 
                }
                searchTimer = new javax.swing.Timer(300, e -> {
                    String text = search.txtSearchForm.getText().trim();
                    String type = search.cbxChoose.getSelectedItem().toString();
                    loadDataTable(spController.search(text, type));
                });
                searchTimer.setRepeats(false);
                searchTimer.start();
            }
        });
        
        // Bắt sự kiện đổi tiêu chí tìm kiếm trong ComboBox
        search.cbxChoose.addActionListener(e -> {
            String text = search.txtSearchForm.getText().trim();
            String type = search.cbxChoose.getSelectedItem().toString();
            loadDataTable(spController.search(text, type));
        });

        // Bắt sự kiện nút Làm mới
        search.btnReset.addActionListener(e -> {
            loadDataTable(spController.getAllList());
        });

        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        // ========== BẢNG DỮ LIỆU SẢN PHẨM ==========
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.putClientProperty(FlatClientProperties.STYLE, "arc: 12");

        String[] cols = {"Mã SP", "Tên sản phẩm", "Thương hiệu", "Đơn giá", "Số lượng tồn", "Màu sắc", "Dung lượng", "RAM"};
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

    // ========== CÁC HÀM HỖ TRỢ XỬ LÝ DỮ LIỆU ==========
    
    // Đổ dữ liệu từ danh sách lên JTable
    public void loadDataTable(ArrayList<SanPham> result) {
        tblModel.setRowCount(0);
        DecimalFormat df = new DecimalFormat("###,###,### đ"); // Định dạng tiền tệ đẹp mắt
        
        for (SanPham sp : result) {
            tblModel.addRow(new Object[]{
                sp.getMaSP(), 
                sp.getTenSP(), 
                sp.getThuongHieu(), 
                df.format(sp.getDonGia()), 
                sp.getSoLuongTon(), 
                sp.getMauSac(), 
                sp.getDungLuong(), 
                sp.getRam()
            });
        }
    }

    // Lấy nguyên đối tượng Sản Phẩm từ dòng đang chọn
    public SanPham getSanPhamSelected() {
        int index = table.getSelectedRow();
        if (index == -1) return null;
        
        String maSP = table.getValueAt(index, 0).toString();
        // Lấy từ Controller để đảm bảo không bị lỗi sai format định dạng số của JTable
        for (SanPham sp : spController.getAllList()) {
            if (sp.getMaSP().equals(maSP)) {
                return sp;
            }
        }
        return null;
    }

    // ========== XỬ LÝ SỰ KIỆN CLICK NÚT BẤM ==========
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            SanPhamDialog dialog = new SanPhamDialog(this, null, "Thêm Sản Phẩm", true, "ADD", null);
            dialog.setVisible(true);

        } else if (e.getSource() == btnEdit) {
            SanPham spSelect = getSanPhamSelected();
            if (spSelect != null) {
                SanPhamDialog dialog = new SanPhamDialog(this, null, "Sửa Sản Phẩm", true, "EDIT", spSelect);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa trên bảng!");
            }

        } else if (e.getSource() == btnDelete) {
            SanPham spSelect = getSanPhamSelected();
            if (spSelect != null) {
                int cf = JOptionPane.showConfirmDialog(this, "Xác nhận xóa sản phẩm: " + spSelect.getTenSP() + "?", "Xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (cf == JOptionPane.YES_OPTION) {
                    if(spController.deleteSanPham(spSelect.getMaSP())) {
                        JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                        loadDataTable(spController.getAllList());
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại! Vui lòng kiểm tra lại.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa trên bảng!");
            }

        } else if (e.getSource() == btnDetail) {
            SanPham spSelect = getSanPhamSelected();
            if (spSelect != null) {
                SanPhamDialog dialog = new SanPhamDialog(this, null, "Chi Tiết Sản Phẩm", true, "DETAIL", spSelect);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm muốn xem chi tiết!");
            }
            
        } else if (e.getSource() == btnXemDS) {
            JOptionPane.showMessageDialog(this, "Chức năng Xem Danh Sách đang được xây dựng!");
        } else if (e.getSource() == btnExport) {
            JOptionPane.showMessageDialog(this, "Chức năng Xuất Excel đang được xây dựng!");
        }
    }
}