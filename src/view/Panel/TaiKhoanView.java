package view.Panel;

import view.Main;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class TaiKhoanView extends JPanel {
    public TaiKhoanView(Main main) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 247, 250));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Phần Top: Nút chức năng & Tìm kiếm
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(new Color(240, 247, 250));
        
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlButtons.setBackground(new Color(240, 247, 250));
        pnlButtons.add(new JButton("Cấp Tài Khoản"));
        pnlButtons.add(new JButton("Đổi Mật Khẩu"));
        pnlButtons.add(new JButton("Khóa Tài Khoản"));
        
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlSearch.setBackground(new Color(240, 247, 250));
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        pnlSearch.add(new JTextField(15));
        pnlSearch.add(new JButton("Tìm"));

        pnlTop.add(pnlButtons, BorderLayout.WEST);
        pnlTop.add(pnlSearch, BorderLayout.EAST);
        add(pnlTop, BorderLayout.NORTH);

        // Phần Center: Bảng dữ liệu
        String[] columns = {"Tên Đăng Nhập", "Tên Nhân Viên", "Nhóm Quyền", "Trạng Thái"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setDefaultEditor(Object.class, null);
        
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}