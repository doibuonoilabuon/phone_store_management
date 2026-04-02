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
            // Không insert manv vì nó tự tăng, mặc định trangthai = 1 (Đang làm)
            String sql = "INSERT INTO NhanVien (hoten, gioitinh, chucvu, sdt, email, trangthai) VALUES (?, ?, ?, ?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getHoten());
            pst.setInt(2, t.getGioitinh());
            pst.setString(3, t.getChucvu());
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
            pst.setString(1, t.getHoten());
            pst.setInt(2, t.getGioitinh());
            pst.setString(3, t.getChucvu());
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
            // Xóa mềm: Update trạng thái về 0 (Nghỉ việc)
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
            String sql = "SELECT * FROM NhanVien WHERE trangthai=1"; // Chỉ lấy người đang làm việc
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getInt("manv"),
                        rs.getString("hoten"),
                        rs.getInt("gioitinh"),
                        rs.getString("chucvu"),
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

    @Override
    public NhanVien selectById(String manv) {
        // Có thể tự triển khai tương tự selectAll nếu cần
        return null;
    }

    @Override
    public int getAutoIncrement() {
        return -1;
    }
}