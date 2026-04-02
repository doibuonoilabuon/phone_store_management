package controller;

import DAO.SanPhamDAO;
import model.SanPham;
import java.util.ArrayList;

public class SanPhamController {
    
    private SanPhamDAO sanPhamDAO;

    public SanPhamController() {
        sanPhamDAO = SanPhamDAO.getInstance();
    }

    public ArrayList<SanPham> getAllList() {
        return sanPhamDAO.selectAll();
    }

    public boolean addSanPham(SanPham sp) {
        return sanPhamDAO.insert(sp) > 0;
    }

    public boolean updateSanPham(SanPham sp) {
        return sanPhamDAO.update(sp) > 0;
    }

    public boolean deleteSanPham(String maSP) {
        return sanPhamDAO.delete(maSP) > 0;
    }

    // Hàm gọt dấu Tiếng Việt
    public String removeAccent(String s) {
        if (s == null) return "";
        String temp = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }

    public ArrayList<SanPham> search(String text, String type) {
        ArrayList<SanPham> result = new ArrayList<>();
        ArrayList<SanPham> listAll = getAllList();
        
        text = removeAccent(text.trim().toLowerCase()); 
        
        for (SanPham sp : listAll) {
            String ten = sp.getTenSP() != null ? removeAccent(sp.getTenSP().trim().toLowerCase()) : "";
            String hang = sp.getThuongHieu()!= null ? removeAccent(sp.getThuongHieu().trim().toLowerCase()) : "";
            String ma = sp.getMaSP()!= null ? removeAccent(sp.getMaSP().trim().toLowerCase()) : "";

            if (type.equals("Tất cả")) {
                if (ten.contains(text) || hang.contains(text) || ma.contains(text)) {
                    result.add(sp);
                }
            } else if (type.equals("Tên SP")) {
                if (ten.contains(text)) {
                    result.add(sp);
                }
            } else if (type.equals("Thương hiệu")) {
                if (hang.contains(text)) {
                    result.add(sp);
                }
            }
        }
        return result;
    }
}