package DAO;

import database.DbConection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.SanPham;

public class SanPhamDAO implements DAOinterface<SanPham> {

    public static SanPhamDAO getInstance() {
        return new SanPhamDAO();
    }

    @Override
    public int insert(SanPham t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            // Thêm hinhAnh vào lệnh INSERT
            String sql = "INSERT INTO SanPham (maSP, tenSP, thuongHieu, donGia, soLuongTon, mauSac, dungLuong, ram, hinhAnh) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, t.getMaSP());
            pst.setString(2, t.getTenSP());
            pst.setString(3, t.getThuongHieu());
            pst.setDouble(4, t.getDonGia());
            pst.setInt(5, t.getSoLuongTon());
            pst.setString(6, t.getMauSac());
            pst.setString(7, t.getDungLuong());
            pst.setString(8, t.getRam());
            pst.setString(9, t.getHinhAnh()); // Set giá trị hình ảnh

            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(SanPham t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            // Thêm hinhAnh vào lệnh UPDATE
            String sql = "UPDATE SanPham SET tenSP=?, thuongHieu=?, donGia=?, soLuongTon=?, mauSac=?, dungLuong=?, ram=?, hinhAnh=? WHERE maSP=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, t.getTenSP());
            pst.setString(2, t.getThuongHieu());
            pst.setDouble(3, t.getDonGia());
            pst.setInt(4, t.getSoLuongTon());
            pst.setString(5, t.getMauSac());
            pst.setString(6, t.getDungLuong());
            pst.setString(7, t.getRam());
            pst.setString(8, t.getHinhAnh()); // Vị trí số 8 là hình ảnh
            pst.setString(9, t.getMaSP());    // Vị trí số 9 là mã SP

            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(String maSP) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "DELETE FROM SanPham WHERE maSP=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, maSP);

            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<SanPham> selectAll() {
        ArrayList<SanPham> list = new ArrayList<>();
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM SanPham";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                // Nhớ thêm rs.getString("hinhAnh") vào cuối nhé
                SanPham sp = new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getString("thuongHieu"),
                        rs.getDouble("donGia"),
                        rs.getInt("soLuongTon"),
                        rs.getString("mauSac"),
                        rs.getString("dungLuong"),
                        rs.getString("ram"),
                        rs.getString("hinhAnh") 
                );
                list.add(sp);
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public SanPham selectById(String maSP) {
        SanPham sp = null;
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM SanPham WHERE maSP=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maSP);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                sp = new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getString("thuongHieu"),
                        rs.getDouble("donGia"),
                        rs.getInt("soLuongTon"),
                        rs.getString("mauSac"),
                        rs.getString("dungLuong"),
                        rs.getString("ram"),
                        rs.getString("hinhAnh")
                );
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sp;
    }

    @Override
    public int getAutoIncrement() {
        return -1; 
    }
}