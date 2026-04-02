package controller;

import DAO.KhachHangDAO;
import model.KhachHang;
import java.util.ArrayList;

public class KhachHangController {
    
    private KhachHangDAO khachHangDAO;

    public KhachHangController() {
        khachHangDAO = KhachHangDAO.getInstance();
    }

    public ArrayList<KhachHang> getAllList() {
        return khachHangDAO.selectAll();
    }

    public boolean addKhachHang(KhachHang kh) {
        return khachHangDAO.insert(kh) > 0;
    }

    public boolean updateKhachHang(KhachHang kh) {
        return khachHangDAO.update(kh) > 0;
    }

    public boolean deleteKhachHang(String maKH) {
        return khachHangDAO.delete(maKH) > 0;
    }

  // 1. Thêm hàm phụ trợ này để "gọt" hết dấu Tiếng Việt (Bao gồm cả chữ Đ)
    public String removeAccent(String s) {
        if (s == null) return "";
        // Chuẩn hóa chuỗi tách dấu
        String temp = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        // Xóa dấu và đổi chữ Đ/đ thành D/d
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }

    // 2. Cập nhật lại hàm search xịn xò hơn
    public ArrayList<KhachHang> search(String text, String type) {
        ArrayList<KhachHang> result = new ArrayList<>();
        ArrayList<KhachHang> listAll = getAllList();
        
        // Gọt dấu và viết thường từ khóa người dùng nhập vào
        text = removeAccent(text.trim().toLowerCase()); 
        
        for (KhachHang kh : listAll) {
            // Lấy dữ liệu từ DB, gọt sạch dấu và viết thường để đem đi so sánh
            String ten = kh.getTenKH() != null ? removeAccent(kh.getTenKH().trim().toLowerCase()) : "";
            String sdt = kh.getSoDienThoai() != null ? kh.getSoDienThoai().trim().toLowerCase() : ""; // SĐT thì không cần gọt dấu
            String ma = kh.getMaKH() != null ? removeAccent(kh.getMaKH().trim().toLowerCase()) : "";

            // Tiến hành so sánh: "luu cong dung" có chứa "luu" không? -> Có!
            if (type.equals("Tất cả")) {
                if (ten.contains(text) || sdt.contains(text) || ma.contains(text)) {
                    result.add(kh);
                }
            } else if (type.equals("Tên KH")) {
                if (ten.contains(text)) {
                    result.add(kh);
                }
            } else if (type.equals("Số điện thoại")) {
                if (sdt.contains(text)) {
                    result.add(kh);
                }
            }
        }
        return result;
    }
}