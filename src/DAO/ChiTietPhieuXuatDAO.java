package DAO;

import database.DbConection;
import java.sql.*;
import java.util.ArrayList;
import model.ChiTietPhieuXuat;

public class ChiTietPhieuXuatDAO {

    public static ChiTietPhieuXuatDAO getInstance() {
        return new ChiTietPhieuXuatDAO();
    }

    public int insert(ArrayList<ChiTietPhieuXuat> list) {
        int result = 0;
        for (ChiTietPhieuXuat ct : list) {
            try {
                Connection con = DbConection.getConnection();
                // 1. Lưu chi tiết phiếu xuất
                String sql = "INSERT INTO ChiTietPhieuXuat (maphieu, masp, soluong, dongia) VALUES (?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, ct.getMaphieu());
                pst.setString(2, ct.getMasp());
                pst.setInt(3, ct.getSoluong());
                pst.setLong(4, ct.getDongia());
                result += pst.executeUpdate();

                // 2. CẬP NHẬT KHO: Trừ số lượng tồn trong bảng SanPham khi bán hàng
                String sqlUpdate = "UPDATE SanPham SET soluongton = soluongton - ? WHERE masp = ?";
                PreparedStatement pstUpdate = con.prepareStatement(sqlUpdate);
                pstUpdate.setInt(1, ct.getSoluong());
                pstUpdate.setString(2, ct.getMasp());
                pstUpdate.executeUpdate();

                DbConection.closeConnection(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public ArrayList<ChiTietPhieuXuat> selectByMaPhieu(int maphieu) {
        ArrayList<ChiTietPhieuXuat> list = new ArrayList<>();
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuXuat WHERE maphieu=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, maphieu);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ChiTietPhieuXuat ct = new ChiTietPhieuXuat(
                    rs.getInt("maphieu"),
                    rs.getString("masp"),
                    rs.getInt("soluong"),
                    rs.getLong("dongia")
                );
                list.add(ct);
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}