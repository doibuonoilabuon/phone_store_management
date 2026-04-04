package DAO;

import database.DbConection;
import java.sql.*;
import java.util.ArrayList;
import model.ChiTietPhieuNhap;

public class ChiTietPhieuNhapDAO implements ChiTietInterface<ChiTietPhieuNhap> {
    public static ChiTietPhieuNhapDAO getInstance() { return new ChiTietPhieuNhapDAO(); }

    @Override
    public ArrayList<ChiTietPhieuNhap> selectAll(String t) {
        ArrayList<ChiTietPhieuNhap> list = new ArrayList<>();
        try {
            Connection con = DbConection.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuNhap WHERE maphieu = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(t));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new ChiTietPhieuNhap(
                    rs.getInt("maphieu"),
                    rs.getString("masp"),
                    rs.getInt("soluong"),
                    rs.getLong("dongia")
                ));
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public int insert(ArrayList<ChiTietPhieuNhap> t) {
        int result = 0;
        try {
            Connection con = DbConection.getConnection();
            String sql = "INSERT INTO ChiTietPhieuNhap (maphieu, masp, soluong, dongia) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            for (ChiTietPhieuNhap ct : t) {
                pst.setInt(1, ct.getMaphieu());
                pst.setString(2, ct.getMasp());
                pst.setInt(3, ct.getSoluong());
                pst.setLong(4, ct.getDongia());
                result += pst.executeUpdate();
            }
            DbConection.closeConnection(con);
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    @Override public int delete(String t) { return 0; } 
    @Override public int update(ArrayList<ChiTietPhieuNhap> t, String pk) { return 0; }
}