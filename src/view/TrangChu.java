package view;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TrangChu extends JFrame {

    JTable table;
    JTextField txtSearch;

    public TrangChu() {

        setTitle("Phần mềm quản lý kho hàng máy tính");
        setSize(1200,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200,700));
        sidebar.setBackground(new Color(76,175,80));
        sidebar.setLayout(new GridLayout(12,1));

        String[] menus = {
                "HI! Admin",
                "SẢN PHẨM",
                "NHÀ CUNG CẤP",
                "NHẬP HÀNG",
                "PHIẾU NHẬP",
                "XUẤT HÀNG",
                "PHIẾU XUẤT",
                "TỒN KHO",
                "TÀI KHOẢN",
                "THỐNG KÊ",
                "ĐỔI THÔNG TIN",
                "ĐĂNG XUẤT"
        };

        for(String m : menus){
            JButton btn = new JButton(m);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(76,175,80));
            btn.setForeground(Color.white);
            sidebar.add(btn);
        }

        add(sidebar,BorderLayout.WEST);

        // ===== PANEL CHÍNH =====
        JPanel mainPanel = new JPanel(new BorderLayout());

        // ===== THANH CHỨC NĂNG =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton them = new JButton("Thêm");
        JButton xoa = new JButton("Xóa");
        JButton sua = new JButton("Sửa");
        JButton chitiet = new JButton("Xem chi tiết");
        JButton excel = new JButton("Xuất Excel");

        JComboBox<String> filter = new JComboBox<>();
        filter.addItem("Tất cả");

        txtSearch = new JTextField(15);
        JButton refresh = new JButton("Làm mới");

        topPanel.add(them);
        topPanel.add(xoa);
        topPanel.add(sua);
        topPanel.add(chitiet);
        topPanel.add(excel);
        topPanel.add(filter);
        topPanel.add(txtSearch);
        topPanel.add(refresh);

        mainPanel.add(topPanel,BorderLayout.NORTH);

        // ===== BẢNG DỮ LIỆU =====
        String[] column = {
                "Mã máy",
                "Tên máy",
                "Số lượng",
                "Đơn giá",
                "CPU",
                "RAM",
                "Ổ cứng",
                "Loại máy"
        };

        DefaultTableModel model = new DefaultTableModel(column,0);

        model.addRow(new Object[]{"LP13","Laptop HP 15s",19,"9.990.000","i3","4GB","256GB","Laptop"});
        model.addRow(new Object[]{"LP14","Lenovo IdeaPad 5",3,"22.490.000","i5","16GB","512GB","Laptop"});
        model.addRow(new Object[]{"LP15","Acer Nitro Gaming",28,"25.190.000","i7","16GB","512GB","Laptop"});
        model.addRow(new Object[]{"PC01","PC Gaming G1650",7,"18.990.000","i5","16GB","512GB","PC"});

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(scrollPane,BorderLayout.CENTER);

        add(mainPanel,BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TrangChu().setVisible(true);
        });
    }
}