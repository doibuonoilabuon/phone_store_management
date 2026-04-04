package DAO;

import database.DbConection;
import java.sql.*;
import java.util.ArrayList;
import model.PhieuNhap;

public class PhieuNhapDAO implements DAOinterface<PhieuNhap> {

    public static PhieuNhapDAO getInstance() {
        return new PhieuNhapDAO();
    }

    @Override
    public int insert(PhieuNhap t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "INSERT INTO PhieuNhap (maphieu, manhacungcap, manguoitao, thoigiantao, tongTien, trangthai) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMaphieu());
            pst.setString(2, t.getManhacungcap());
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
    public int update(PhieuNhap t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "UPDATE PhieuNhap SET manhacungcap=?, manguoitao=?, thoigiantao=?, tongTien=?, trangthai=? WHERE maphieu=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getManhacungcap());
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
            // Xóa mềm: Cập nhật trạng thái phiếu về 0 (Đã hủy)
            String sql = "UPDATE PhieuNhap SET trangthai=0 WHERE maphieu=?";
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
    public ArrayList<PhieuNhap> selectAll() {
        ArrayList<PhieuNhap> list = new ArrayList<>();
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM PhieuNhap ORDER BY thoigiantao DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                PhieuNhap pn = new PhieuNhap(
                    rs.getString("manhacungcap"),
                    rs.getInt("maphieu"),
                    rs.getInt("manguoitao"),
                    rs.getTimestamp("thoigiantao"),
                    rs.getLong("tongTien"),
                    rs.getInt("trangthai")
                );
                list.add(pn);
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public PhieuNhap selectById(String maphieu) {
        PhieuNhap result = null;
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM PhieuNhap WHERE maphieu=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(maphieu));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = new PhieuNhap(
                    rs.getString("manhacungcap"),
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
            String sql = "SELECT MAX(maphieu) FROM PhieuNhap";
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