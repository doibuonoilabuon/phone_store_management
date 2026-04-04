package DAO;

import database.DbConection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.NhanVien;

public class NhanVienDAO implements DAOinterface<NhanVien> {

    public static NhanVienDAO getInstance() {
        return new NhanVienDAO();
    }

    @Override
    public int insert(NhanVien t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "INSERT INTO NhanVien (hoten, gioitinh, chucvu, sdt, email, trangthai) VALUES (?, ?, ?, ?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setNString(1, t.getHoten()); // Dùng setNString để lưu tiếng Việt chuẩn
            pst.setInt(2, t.getGioitinh());
            pst.setNString(3, t.getChucvu());
            pst.setString(4, t.getSdt());
            pst.setString(5, t.getEmail());
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(NhanVien t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "UPDATE NhanVien SET hoten=?, gioitinh=?, chucvu=?, sdt=?, email=? WHERE manv=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setNString(1, t.getHoten());
            pst.setInt(2, t.getGioitinh());
            pst.setNString(3, t.getChucvu());
            pst.setString(4, t.getSdt());
            pst.setString(5, t.getEmail());
            pst.setInt(6, t.getManv());
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(String manv) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "UPDATE NhanVien SET trangthai=0 WHERE manv=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(manv));
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<NhanVien> selectAll() {
        ArrayList<NhanVien> list = new ArrayList<>();
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM NhanVien WHERE trangthai=1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getInt("manv"),
                        rs.getNString("hoten"), // Dùng getNString để lấy tiếng Việt chuẩn
                        rs.getInt("gioitinh"),
                        rs.getNString("chucvu"),
                        rs.getString("sdt"),
                        rs.getInt("trangthai"),
                        rs.getString("email")
                );
                list.add(nv);
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ĐÃ FIX: Triển khai hàm tìm kiếm theo ID
    @Override
    public NhanVien selectById(String manv) {
        NhanVien result = null;
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM NhanVien WHERE manv = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(manv));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = new NhanVien(
                        rs.getInt("manv"),
                        rs.getNString("hoten"),
                        rs.getInt("gioitinh"),
                        rs.getNString("chucvu"),
                        rs.getString("sdt"),
                        rs.getInt("trangthai"),
                        rs.getString("email")
                );
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
public boolean checkEmail(String email) {
    try {
        Connection con = DbConection.getConnection();
        String sql = "SELECT * FROM NhanVien WHERE email = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return true; 
        }
        DbConection.closeConnection(con);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Không tìm thấy
}
    @Override
    public int getAutoIncrement() {
        return -1;
    }
}