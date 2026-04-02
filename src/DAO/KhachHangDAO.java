package DAO;

import database.DbConection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.KhachHang;

public class KhachHangDAO implements DAOinterface<KhachHang> {

    public static KhachHangDAO getInstance() {
        return new KhachHangDAO();
    }

    @Override
    public int insert(KhachHang t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "INSERT INTO KhachHang (maKH, tenKH, soDienThoai, diaChi, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, t.getMaKH());
            pst.setString(2, t.getTenKH());
            pst.setString(3, t.getSoDienThoai());
            pst.setString(4, t.getDiaChi());
            pst.setString(5, t.getEmail());

            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(KhachHang t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "UPDATE KhachHang SET tenKH=?, soDienThoai=?, diaChi=?, email=? WHERE maKH=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, t.getTenKH());
            pst.setString(2, t.getSoDienThoai());
            pst.setString(3, t.getDiaChi());
            pst.setString(4, t.getEmail());
            pst.setString(5, t.getMaKH());

            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(String maKH) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "DELETE FROM KhachHang WHERE maKH=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, maKH);

            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<KhachHang> selectAll() {
        ArrayList<KhachHang> list = new ArrayList<>();
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM KhachHang";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                KhachHang kh = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("tenKH"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"),
                        rs.getString("email")
                );
                list.add(kh);
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public KhachHang selectById(String maKH) {
        KhachHang kh = null;
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM KhachHang WHERE maKH=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, maKH);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                kh = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("tenKH"),
                        rs.getString("soDienThoai"),
                        rs.getString("diaChi"),
                        rs.getString("email")
                );
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kh;
    }

    @Override
    public int getAutoIncrement() {
        return -1; // Tạm thời tắt tự tăng vì mã KH có vẻ bạn đang cho phép nhập tay
    }
}