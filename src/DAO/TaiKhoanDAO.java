package DAO;

import database.DbConection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.TaiKhoan;

public class TaiKhoanDAO implements DAOinterface<TaiKhoan> {

    public static TaiKhoanDAO getInstance() {
        return new TaiKhoanDAO();
    }

    @Override
    public int insert(TaiKhoan t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "INSERT INTO TaiKhoan (username, manv, matkhau, manhomquyen, trangthai) VALUES (?, ?, ?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getUsername());
            pst.setInt(2, t.getManv());
            pst.setString(3, t.getMatkhau());
            pst.setInt(4, t.getManhomquyen());
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
public int updatePasswordByEmail(String email, String newPassword) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            // Lệnh SQL chuẩn theo ảnh cấu trúc bảng TaiKhoan của sếp
            String sql = "UPDATE TaiKhoan SET matkhau = ? " +
                         "WHERE manv = (SELECT manv FROM NhanVien WHERE email = ?)";
            
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, newPassword);
            pst.setString(2, email);

            result = pst.executeUpdate();
            
            
            if (result > 0) {
                System.out.println("==> Đã đổi mật khẩu thành công trong DB cho Email: " + email);
            } else {
                System.out.println("==> KHÔNG tìm thấy nhân viên nào có Email: " + email);
            }

            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public int update(TaiKhoan t) {
        // Chỉ dùng để update mật khẩu hoặc nhóm quyền, username là khóa chính không đổi
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "UPDATE TaiKhoan SET matkhau=?, manhomquyen=?, trangthai=? WHERE username=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getMatkhau());
            pst.setInt(2, t.getManhomquyen());
            pst.setInt(3, t.getTrangthai());
            pst.setString(4, t.getUsername());
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(String username) {
        // Xóa mềm: Khóa tài khoản (trangthai = 0)
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "UPDATE TaiKhoan SET trangthai=0 WHERE username=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<TaiKhoan> selectAll() {
        ArrayList<TaiKhoan> list = new ArrayList<>();
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM TaiKhoan";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan(
                        rs.getInt("manv"),
                        rs.getString("username"),
                        rs.getString("matkhau"),
                        rs.getInt("manhomquyen"),
                        rs.getInt("trangthai")
                );
                list.add(tk);
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public TaiKhoan selectById(String username) {
        TaiKhoan tk = null;
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM TaiKhoan WHERE username=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tk = new TaiKhoan(
                        rs.getInt("manv"),
                        rs.getString("username"),
                        rs.getString("matkhau"),
                        rs.getInt("manhomquyen"),
                        rs.getInt("trangthai")
                );
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tk;
    }

    @Override
    public int getAutoIncrement() {
        return -1;
    }
}