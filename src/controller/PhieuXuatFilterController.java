package controller;

import model.PhieuXuat;
import model.NhanVien;
import model.KhachHang;
import DAO.NhanVienDAO;
import DAO.KhachHangDAO;
import java.util.ArrayList;
import java.util.Date;

public class PhieuXuatFilterController {

    public ArrayList<PhieuXuat> locPhieuXuat(ArrayList<PhieuXuat> listGoc, 
                                             String tenKhachHang, String tenNhanVien, 
                                             Date tuNgay, Date denNgay, 
                                             String tuGiaStr, String denGiaStr) {
        
        ArrayList<PhieuXuat> result = new ArrayList<>();

        // ==============================================================
        // BƯỚC 1: DỊCH TỪ "TÊN" SANG "MÃ" TRƯỚC KHI LỌC
        // ==============================================================
        
        // Dịch tên Khách hàng sang Mã KH
        String maKH_Loc = null;
        if (!tenKhachHang.equals("Tất cả") && !tenKhachHang.equals("Khách vãng lai")) {
            for (KhachHang kh : KhachHangDAO.getInstance().selectAll()) {
                if (kh.getTenKH().equalsIgnoreCase(tenKhachHang.trim())) {
                    maKH_Loc = kh.getMaKH();
                    break;
                }
            }
        }

        // Dịch tên Nhân viên sang Mã NV
        int maNV_Loc = -1;
        if (!tenNhanVien.equals("Tất cả")) {
            for (NhanVien nv : NhanVienDAO.getInstance().selectAll()) {
                if (nv.getHoten().equalsIgnoreCase(tenNhanVien.trim())) {
                    maNV_Loc = nv.getManv();
                    break;
                }
            }
        }

        // ==============================================================
        // BƯỚC 2: TIẾN HÀNH DUYỆT VÀ LỌC CÁC PHIẾU
        // ==============================================================
        for (PhieuXuat px : listGoc) {
            boolean matchKH = true, matchNV = true, matchDate = true, matchPrice = true;

            // 1. Lọc Khách Hàng (So sánh bằng Mã KH vừa tìm được)
            if (!tenKhachHang.equals("Tất cả")) {
                if (tenKhachHang.equals("Khách vãng lai")) {
                    if (px.getMakh() != null && !px.getMakh().equals("Khách vãng lai")) matchKH = false;
                } else {
                    if (px.getMakh() == null || !px.getMakh().equals(maKH_Loc)) matchKH = false;
                }
            }

            // 2. Lọc Nhân Viên (So sánh bằng Mã NV vừa tìm được)
            if (!tenNhanVien.equals("Tất cả")) {
                if (px.getManguoitao() != maNV_Loc) matchNV = false;
            }

            // 3. Lọc Ngày tháng (Chạy bằng Lịch JDateChooser cực chuẩn)
            Date ngayTao = new Date(px.getThoigiantao().getTime());
            if (tuNgay != null) {
                if (ngayTao.before(tuNgay)) matchDate = false;
            }
            if (denNgay != null) {
                Date denNgayCuoi = new Date(denNgay.getTime() + 86399999L); 
                if (ngayTao.after(denNgayCuoi)) matchDate = false;
            }

            // 4. Lọc Giá tiền
            try {
                long tongTien = px.getTongTien();
                if (!tuGiaStr.trim().isEmpty()) {
                    long tuGia = Long.parseLong(tuGiaStr.replaceAll("[^0-9]", ""));
                    if (tongTien < tuGia) matchPrice = false;
                }
                if (!denGiaStr.trim().isEmpty()) {
                    long denGia = Long.parseLong(denGiaStr.replaceAll("[^0-9]", ""));
                    if (tongTien > denGia) matchPrice = false;
                }
            } catch (Exception e) {}

            // Vượt qua hết "bài test" thì nạp vào danh sách
            if (matchKH && matchNV && matchDate && matchPrice) {
                result.add(px);
            }
        }
        return result;
    }
}