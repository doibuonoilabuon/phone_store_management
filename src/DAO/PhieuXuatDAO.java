package DAO;

import database.DbConection;
import java.sql.*;
import java.util.ArrayList;
import model.PhieuXuat;

public class PhieuXuatDAO implements DAOinterface<PhieuXuat> {

    public static PhieuXuatDAO getInstance() {
        return new PhieuXuatDAO();
    }

    @Override
    public int insert(PhieuXuat t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "INSERT INTO PhieuXuat (maphieu, makh, manguoitao, thoigiantao, tongTien, trangthai) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMaphieu());
            pst.setString(2, t.getMakh());
            pst.setInt(3, t.getManguoitao());
            pst.setTimestamp(4, t.getThoigiantao());
            pst.setLong(5, t.getTongTien());
            pst.setInt(6, t.getTrangthai());
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(PhieuXuat t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "UPDATE PhieuXuat SET makh=?, manguoitao=?, thoigiantao=?, tongTien=?, trangthai=? WHERE maphieu=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getMakh());
            pst.setInt(2, t.getManguoitao());
            pst.setTimestamp(3, t.getThoigiantao());
            pst.setLong(4, t.getTongTien());
            pst.setInt(5, t.getTrangthai());
            pst.setInt(6, t.getMaphieu());
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(String maphieu) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            // Xóa mềm: Chuyển trạng thái phiếu về 0 (Đã hủy/đã xóa)
            String sql = "UPDATE PhieuXuat SET trangthai=0 WHERE maphieu=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(maphieu));
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<PhieuXuat> selectAll() {
        ArrayList<PhieuXuat> list = new ArrayList<>();
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM PhieuXuat ORDER BY thoigiantao DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                PhieuXuat px = new PhieuXuat(
                    rs.getString("makh"),
                    rs.getInt("maphieu"),
                    rs.getInt("manguoitao"),
                    rs.getTimestamp("thoigiantao"),
                    rs.getLong("tongTien"),
                    rs.getInt("trangthai")
                );
                list.add(px);
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public PhieuXuat selectById(String maphieu) {
        PhieuXuat result = null;
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM PhieuXuat WHERE maphieu=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(maphieu));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = new PhieuXuat(
                    rs.getString("makh"),
                    rs.getInt("maphieu"),
                    rs.getInt("manguoitao"),
                    rs.getTimestamp("thoigiantao"),
                    rs.getLong("tongTien"),
                    rs.getInt("trangthai")
                );
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT MAX(maphieu) FROM PhieuXuat";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1) + 1;
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}