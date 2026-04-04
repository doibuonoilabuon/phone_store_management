package DAO;

import database.DbConection;
import java.sql.*;
import java.util.ArrayList;
import model.ChiTietQuyen;

public class ChiTietQuyenDAO {
    
    public static ChiTietQuyenDAO getInstance() {
        return new ChiTietQuyenDAO();
    }

    /**
     * Hàm cực kỳ quan trọng: Kiểm tra quyền để ẩn/hiện MenuTaskbar
     *
     */
    public boolean checkPermission(int roleId, String functionId, String action) {
        boolean isAllowed = false;
        try {
            Connection con = DbConection.getConnection();
            // Truy vấn bảng ChiTietQuyen theo đúng sơ đồ của bạn
            String sql = "SELECT * FROM ChiTietQuyen WHERE manhomquyen = ? AND machucnang = ? AND hanhdong = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, roleId);
            pst.setString(2, functionId);
            pst.setString(3, action);
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                isAllowed = true;
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAllowed;
    }

    /**
     * Lấy toàn bộ danh sách chi tiết quyền theo nhóm (Dùng cho giao diện phân quyền)
     */
    public ArrayList<ChiTietQuyen> selectAll(int roleId) {
        ArrayList<ChiTietQuyen> list = new ArrayList<>();
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM ChiTietQuyen WHERE manhomquyen = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, roleId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new ChiTietQuyen(
                    rs.getInt("manhomquyen"),
                    rs.getString("machucnang"),
                    rs.getString("hanhdong")
                ));
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}