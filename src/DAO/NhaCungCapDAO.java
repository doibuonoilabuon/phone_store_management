package DAO;

import database.DbConection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.NhaCungCap;

public class NhaCungCapDAO implements DAOinterface<NhaCungCap> {

    public static NhaCungCapDAO getInstance() {
        return new NhaCungCapDAO();
    }

    @Override
    public int insert(NhaCungCap t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "INSERT INTO NhaCungCap (maNCC, tenNCC, diaChi, soDienThoai, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getMaNCC());
            pst.setString(2, t.getTenNCC());
            pst.setString(3, t.getDiaChi());
            pst.setString(4, t.getSoDienThoai());
            pst.setString(5, t.getEmail());
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(NhaCungCap t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "UPDATE NhaCungCap SET tenNCC=?, diaChi=?, soDienThoai=?, email=? WHERE maNCC=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenNCC());
            pst.setString(2, t.getDiaChi());
            pst.setString(3, t.getSoDienThoai());
            pst.setString(4, t.getEmail());
            pst.setString(5, t.getMaNCC());
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(String maNCC) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "DELETE FROM NhaCungCap WHERE maNCC=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maNCC);
            result = pst.executeUpdate();
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<NhaCungCap> selectAll() {
        ArrayList<NhaCungCap> list = new ArrayList<>();
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM NhaCungCap";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap(
                        rs.getString("maNCC"),
                        rs.getString("tenNCC"),
                        rs.getString("diaChi"),
                        rs.getString("soDienThoai"),
                        rs.getString("email")
                );
                list.add(ncc);
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public NhaCungCap selectById(String maNCC) {
        NhaCungCap ncc = null;
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM NhaCungCap WHERE maNCC=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, maNCC);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                ncc = new NhaCungCap(
                        rs.getString("maNCC"),
                        rs.getString("tenNCC"),
                        rs.getString("diaChi"),
                        rs.getString("soDienThoai"),
                        rs.getString("email")
                );
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ncc;
    }

    @Override
    public int getAutoIncrement() {
        return -1;
    }
}