package view.Form;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import view.DangNhap;
import view.Main;
import view.Panel.KhachHangView;
import view.Panel.NhanVienView;
import view.Panel.SanPhamView;
import view.Panel.TaiKhoanView;
import view.Panel.TrangChu; 
import view.Panel.NhaCungCapView;
import view.Panel.PhieuNhapView; 
import view.Panel.PhieuXuatView; 
import model.TaiKhoan;
import model.NhanVien;
import DAO.ChiTietQuyenDAO; 
import database.DbConection; 

public class MenuTaskbar extends JPanel {

    TrangChu trangChu; 
    SanPhamView sanPham;
    PhieuNhapView phieuNhap; 
    PhieuXuatView phieuXuat; 
    KhachHangView khachHang;
    NhaCungCapView nhacungcap;
    NhanVienView nhanVien;
    TaiKhoanView taiKhoan;
    
    String[][] getSt = {
        {"Trang chủ", "home.svg", "trangchu"},
        {"Sản phẩm", "product.svg", "sanpham"},
        {"Nhập hàng", "import.svg", "nhaphang"},  
        {"Xuất hàng", "export.svg", "xuathang"},  
        {"Khách hàng", "customer.svg", "khachhang"},
        {"Nhà cung cấp", "supplier.svg", "nhacungcap"},
        {"Nhân viên", "staff.svg", "nhanvien"},
        {"Tài khoản", "account.svg", "taikhoan"},
        {"Đăng xuất", "log_out.svg", "dangxuat"}
    };

    Main main;
    TaiKhoan user;
    public itemTaskbar[] listitem; 

    JLabel lblTenNhomQuyen, lblUsername;
    JScrollPane scrollPane;

    JPanel pnlCenter, pnlTop, pnlBottom, bar1, bar2, bar3, bar4;

    Color FontColor = new Color(96, 125, 139);
    Color DefaultColor = new Color(255, 255, 255);
    Color HowerFontColor = new Color(1, 87, 155);
    Color HowerBackgroundColor = new Color(187, 222, 251);
    
    public NhanVien nhanVienDTO;
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);

    public MenuTaskbar(Main main) {
        this.main = main;
        initComponent();
    }

    public MenuTaskbar(Main main, TaiKhoan tk) {
        this.main = main;
        this.user = tk;
        initComponent();
    }

    private void initComponent() {
        listitem = new itemTaskbar[getSt.length];
        this.setOpaque(true);
        this.setBackground(DefaultColor);
        this.setLayout(new BorderLayout(0, 0));

        // pnlTop
        pnlTop = new JPanel();
        pnlTop.setPreferredSize(new Dimension(250, 80));
        pnlTop.setBackground(DefaultColor);
        pnlTop.setLayout(new BorderLayout(0, 0));
        this.add(pnlTop, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BorderLayout(0, 0));
        pnlTop.add(info, BorderLayout.CENTER);

        in4(info);

        bar1 = new JPanel();
        bar1.setBackground(new Color(204, 214, 219));
        bar1.setPreferredSize(new Dimension(1, 0));
        pnlTop.add(bar1, BorderLayout.EAST);

        bar2 = new JPanel();
        bar2.setBackground(new Color(204, 214, 219));
        bar2.setPreferredSize(new Dimension(0, 1));
        pnlTop.add(bar2, BorderLayout.SOUTH);

        // pnlCenter
        pnlCenter = new JPanel();
        pnlCenter.setPreferredSize(new Dimension(230, 600));
        pnlCenter.setBackground(DefaultColor);
        pnlCenter.setLayout(new FlowLayout(0, 0, 5));

        bar3 = new JPanel();
        bar3.setBackground(new Color(204, 214, 219));
        bar3.setPreferredSize(new Dimension(1, 1));
        this.add(bar3, BorderLayout.EAST);

        scrollPane = new JScrollPane(pnlCenter, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(5, 10, 0, 10));
        this.add(scrollPane, BorderLayout.CENTER);

        // pnlBottom
        pnlBottom = new JPanel();
        pnlBottom.setPreferredSize(new Dimension(250, 50));
        pnlBottom.setBackground(DefaultColor);
        pnlBottom.setLayout(new BorderLayout(0, 0));

        bar4 = new JPanel();
        bar4.setBackground(new Color(204, 214, 219));
        bar4.setPreferredSize(new Dimension(1, 1));
        pnlBottom.add(bar4, BorderLayout.EAST);

        this.add(pnlBottom, BorderLayout.SOUTH);

       
        int roleId = user != null ? user.getManhomquyen() : 0; 

        for (int i = 0; i < getSt.length; i++) {
            listitem[i] = new itemTaskbar(getSt[i][1], getSt[i][0]);
            
            String maChucNang = getSt[i][2]; 
            boolean isAllowed = true;
            
           
            if (!maChucNang.equals("trangchu") && !maChucNang.equals("dangxuat")) {
                isAllowed = checkQuyen(roleId, maChucNang, "view");
            }
            
          
            listitem[i].setVisible(isAllowed);
            
            if (i + 1 == getSt.length) {
                pnlBottom.add(listitem[i]);
            } else {
                pnlCenter.add(listitem[i]);
            }
        }

        // Active tab Trang chủ mặc định
        listitem[0].setBackground(HowerBackgroundColor);
        listitem[0].setForeground(HowerFontColor);
        listitem[0].isSelected = true;

        // Sự kiện đổi màu
        for (int i = 0; i < getSt.length; i++) {
            listitem[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                    pnlMenuTaskbarMousePress(evt);
                }
            });
        }

    
        listitem[0].addMouseListener(new MouseAdapter() { @Override public void mousePressed(MouseEvent evt) { trangChu = new TrangChu(); main.setPanel(trangChu); } });
        listitem[1].addMouseListener(new MouseAdapter() { @Override public void mousePressed(MouseEvent evt) { sanPham = new SanPhamView(main); main.setPanel(sanPham); } });
        listitem[2].addMouseListener(new MouseAdapter() { @Override public void mousePressed(MouseEvent evt) { phieuNhap = new PhieuNhapView(main); main.setPanel(phieuNhap); } });
        listitem[3].addMouseListener(new MouseAdapter() { @Override public void mousePressed(MouseEvent evt) { phieuXuat = new PhieuXuatView(main); main.setPanel(phieuXuat); } });
        listitem[4].addMouseListener(new MouseAdapter() { @Override public void mousePressed(MouseEvent evt) { khachHang = new KhachHangView(main); main.setPanel(khachHang); } });
        listitem[5].addMouseListener(new MouseAdapter() { @Override public void mousePressed(MouseEvent evt) { nhacungcap = new NhaCungCapView(main); main.setPanel(nhacungcap); } });
        listitem[6].addMouseListener(new MouseAdapter() { @Override public void mousePressed(MouseEvent evt) { nhanVien = new NhanVienView(main); main.setPanel(nhanVien); } });
        listitem[7].addMouseListener(new MouseAdapter() { @Override public void mousePressed(MouseEvent evt) { taiKhoan = new TaiKhoanView(main); main.setPanel(taiKhoan); } });
        
        listitem[8].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                int input = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (input == 0) {
                    main.dispose();
                    new DangNhap().setVisible(true); 
                }
            }
        });
    }

    // ==============================================================
    // HÀM KIỂM TRA QUYỀN CHUẨN
    // ==============================================================
    private boolean checkQuyen(int roleId, String chucNang, String hanhDong) {
        if (roleId == 1) return true;  // Nhóm 1 (Admin) auto có tất cả quyền
        if (roleId == 0) return false; // Không đăng nhập (0) thì auto cấm cửa
        return ChiTietQuyenDAO.getInstance().checkPermission(roleId, chucNang, hanhDong);
    }

    public void pnlMenuTaskbarMousePress(MouseEvent evt) {
        for (int i = 0; i < getSt.length; i++) {
            if (evt.getSource() == listitem[i]) {
                listitem[i].isSelected = true;
                listitem[i].setBackground(HowerBackgroundColor);
                listitem[i].setForeground(HowerFontColor);
            } else {
                listitem[i].isSelected = false;
                listitem[i].setBackground(DefaultColor);
                listitem[i].setForeground(FontColor);
            }
        }
    }
    
   
    private String getTenNhanVienTuDB(int maNV) {
        String tenThat = "Admin";
        try {
            java.sql.Connection con = DbConection.getConnection();
            java.sql.PreparedStatement pst = con.prepareStatement("SELECT hoten FROM nhanvien WHERE manv = ?");
            pst.setInt(1, maNV);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tenThat = rs.getString("hoten");
            }
            DbConection.closeConnection(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tenThat;
    }

    public void in4(JPanel info) {
        JPanel pnlIcon = new JPanel(new FlowLayout());
        pnlIcon.setPreferredSize(new Dimension(60, 0));
        pnlIcon.setOpaque(false);
        info.add(pnlIcon, BorderLayout.WEST);
        
        JLabel lblIcon = new JLabel();
        lblIcon.setPreferredSize(new Dimension(50, 70));
        
        try {
            lblIcon.setIcon(new FlatSVGIcon("./icon/man_50px.svg"));
        } catch(Exception e){
        }
        pnlIcon.add(lblIcon);

        JPanel pnlInfo = new JPanel();
        pnlInfo.setOpaque(false);
        pnlInfo.setLayout(new GridLayout(2, 1));
        pnlInfo.setBorder(new EmptyBorder(15, 0, 0, 0));
        info.add(pnlInfo, BorderLayout.CENTER);

        // THIẾT LẬP THÔNG TIN TÊN VÀ CHỨC VỤ THẬT
        String tenHienThi = "Khách Hàng";
      

        if (user != null) {
           
            tenHienThi = getTenNhanVienTuDB(user.getManv());
            
          
        }

        lblUsername = new JLabel(tenHienThi);
        lblUsername.putClientProperty("FlatLaf.style", "font: 110% $bold.font");
        
    
       

        pnlInfo.add(lblUsername);
       
    }
}