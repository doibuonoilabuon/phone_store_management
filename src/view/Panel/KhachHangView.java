package view.Panel;


import view.Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class KhachHangView extends JPanel implements ActionListener {

    JPanel pnlTop, pnlCenter;
    JTable tableKhachHang;
    JScrollPane scrollTableKhachHang;
    DefaultTableModel tblModel;
    
    // Nút chức năng cơ bản thay thế cho MainFunction
    JButton btnAdd, btnEdit, btnDelete, btnDetail;
    JTextField txtSearch;
    JButton btnSearch, btnReset;

    // Tạm thời dùng ArrayList trống để giao diện có thể chạy được. 
    // Sau này bạn sẽ thay bằng dữ liệu lấy từ Database
    public ArrayList<model.KhachHang> listkh = new ArrayList<>();
    
    Main m;
    Color BackgroundColor = new Color(240, 247, 250);

    public KhachHangView(Main m) {
        this.m = m;
        initComponent();
        
        // Cấm edit trực tiếp trên bảng
        tableKhachHang.setDefaultEditor(Object.class, null); 
        loadDataTable(listkh);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setOpaque(true);

        // --- PHẦN TOP: CHỨA CÁC NÚT CHỨC NĂNG & TÌM KIẾM ---
        pnlTop = new JPanel();
        pnlTop.setBackground(BackgroundColor);
        pnlTop.setLayout(new GridLayout(1, 2, 20, 0));
        pnlTop.setPreferredSize(new Dimension(0, 80));

        // 1. Nhóm nút chức năng
        JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 25));
        pnlAction.setBackground(BackgroundColor);
        
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnDetail = new JButton("Xem chi tiết");

        btnAdd.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        btnDetail.addActionListener(this);

        pnlAction.add(btnAdd);
        pnlAction.add(btnEdit);
        pnlAction.add(btnDelete);
        pnlAction.add(btnDetail);

        // 2. Nhóm tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 25));
        pnlSearch.setBackground(BackgroundColor);
        
        txtSearch = new JTextField(15);
        btnSearch = new JButton("Tìm kiếm");
        btnReset = new JButton("Làm mới");

        btnSearch.addActionListener(this);
        btnReset.addActionListener(this);

        pnlSearch.add(new JLabel("Tìm kiếm:"));
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        pnlSearch.add(btnReset);

        pnlTop.add(pnlAction);
        pnlTop.add(pnlSearch);
        this.add(pnlTop, BorderLayout.NORTH);

        // --- PHẦN CENTER: BẢNG DỮ LIỆU ---
        pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(Color.WHITE);

        tableKhachHang = new JTable();
        scrollTableKhachHang = new JScrollPane();
        tblModel = new DefaultTableModel();
        
        String[] header = new String[]{"Mã khách hàng", "Tên khách hàng", "Địa chỉ", "Số điện thoại", "Ngày tham gia"};
        tblModel.setColumnIdentifiers(header);
        tableKhachHang.setModel(tblModel);
        tableKhachHang.setFocusable(false);
        tableKhachHang.setRowHeight(40);
        
        scrollTableKhachHang.setViewportView(tableKhachHang);

        // Căn giữa nội dung bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableKhachHang.getColumnCount(); i++) {
            tableKhachHang.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        tableKhachHang.setAutoCreateRowSorter(true);

        pnlCenter.add(scrollTableKhachHang, BorderLayout.CENTER);
        this.add(pnlCenter, BorderLayout.CENTER);
    }

    public void loadDataTable(ArrayList<model.KhachHang> result) {
        tblModel.setRowCount(0);
        // Lưu ý: Đảm bảo model.KhachHang của bạn có các hàm getter này
        // Nếu tên hàm getter khác, bạn hãy tự điều chỉnh lại cho đúng nhé
        /*
        for (model.KhachHang khachHang : result) {
            tblModel.addRow(new Object[]{
                khachHang.getMaKH(), 
                khachHang.getHoten(), 
                khachHang.getDiachi(), 
                khachHang.getSdt(), 
                khachHang.getNgaythamgia()
            });
        }
        */
    }

    public int getRowSelected() {
        int index = tableKhachHang.getSelectedRow();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng trên bảng!");
        }
        return index;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            JOptionPane.showMessageDialog(this, "Tính năng Thêm đang được xây dựng!");
        } else if (e.getSource() == btnEdit) {
            int index = getRowSelected();
            if (index != -1) {
                JOptionPane.showMessageDialog(this, "Tính năng Sửa đang được xây dựng!");
            }
        } else if (e.getSource() == btnDelete) {
            int index = getRowSelected();
            if (index != -1) {
                int input = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc chắn muốn xóa khách hàng này?", "Xác nhận",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (input == 0) {
                    // Xóa logic ở đây
                    JOptionPane.showMessageDialog(this, "Đã xóa!");
                }
            }
        } else if (e.getSource() == btnDetail) {
            int index = getRowSelected();
            if (index != -1) {
                JOptionPane.showMessageDialog(this, "Tính năng Xem chi tiết đang được xây dựng!");
            }
        } else if (e.getSource() == btnSearch) {
            String txt = txtSearch.getText();
            JOptionPane.showMessageDialog(this, "Đang tìm kiếm: " + txt);
            // Thêm logic tìm kiếm ở đây
        } else if (e.getSource() == btnReset) {
            txtSearch.setText("");
            loadDataTable(listkh);
        }
    }
}