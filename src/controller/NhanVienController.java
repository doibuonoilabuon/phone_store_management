package controller;

import DAO.NhanVienDAO;
import model.NhanVien;
import java.util.ArrayList;

public class NhanVienController {
    
    private NhanVienDAO nvDAO;

    public NhanVienController() {
        nvDAO = NhanVienDAO.getInstance();
    }

    public ArrayList<NhanVien> getAllList() {
        return nvDAO.selectAll();
    }

    public boolean addNhanVien(NhanVien nv) {
        return nvDAO.insert(nv) > 0;
    }

    public boolean updateNhanVien(NhanVien nv) {
        return nvDAO.update(nv) > 0;
    }

    public boolean deleteNhanVien(int manv) {
        return nvDAO.delete(String.valueOf(manv)) > 0;
    }

    public String removeAccent(String s) {
        if (s == null) return "";
        String temp = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }

    public ArrayList<NhanVien> search(String text, String type) {
        ArrayList<NhanVien> result = new ArrayList<>();
        ArrayList<NhanVien> listAll = getAllList();
        
        text = removeAccent(text.trim().toLowerCase()); 
        
        for (NhanVien nv : listAll) {
            String ten = nv.getHoten() != null ? removeAccent(nv.getHoten().trim().toLowerCase()) : "";
            String sdt = nv.getSdt() != null ? nv.getSdt().trim().toLowerCase() : "";
            String ma = String.valueOf(nv.getManv()); // Mã là số int

            if (type.equals("Tất cả")) {
                if (ten.contains(text) || sdt.contains(text) || ma.contains(text)) {
                    result.add(nv);
                }
            } else if (type.equals("Tên NV")) {
                if (ten.contains(text)) {
                    result.add(nv);
                }
            } else if (type.equals("Số điện thoại")) {
                if (sdt.contains(text)) {
                    result.add(nv);
                }
            }
        }
        return result;
    }
}