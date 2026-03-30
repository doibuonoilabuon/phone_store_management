package view.Panel;

import view.Main;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class NhaCungCapView extends JPanel {
    public NhaCungCapView(Main main) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 247, 250));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Phần Top: Nút chức năng & Tìm kiếm
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(new Color(240, 247, 250));
        
        // Cụm nút bên trái
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlButtons.setBackground(new Color(240, 247, 250));
        pnlButtons.add(new JButton("Thêm NCC"));
        pnlButtons.add(new JButton("Sửa NCC"));
        pnlButtons.add(new JButton("Xóa NCC"));
        pnlButtons.add(new JButton("Chi tiết"));
        
        // Cụm tìm kiếm bên phải
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlSearch.setBackground(new Color(240, 247, 250));
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        pnlSearch.add(new JTextField(15));
        pnlSearch.add(new JButton("Tìm"));

        pnlTop.add(pnlButtons, BorderLayout.WEST);
        pnlTop.add(pnlSearch, BorderLayout.EAST);
        add(pnlTop, BorderLayout.NORTH);

        // Phần Center: Bảng dữ liệu
        String[] columns = {"Mã NCC", "Tên Nhà Cung Cấp", "Số Điện Thoại", "Email", "Địa Chỉ"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setDefaultEditor(Object.class, null); // Khóa không cho gõ trực tiếp vào bảng
        
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}