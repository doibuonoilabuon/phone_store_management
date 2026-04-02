package controller;

import DAO.NhaCungCapDAO;
import model.NhaCungCap;
import java.util.ArrayList;

public class NhaCungCapController {
    
    private NhaCungCapDAO nccDAO;

    public NhaCungCapController() {
        nccDAO = NhaCungCapDAO.getInstance();
    }

    public ArrayList<NhaCungCap> getAllList() {
        return nccDAO.selectAll();
    }

    public boolean addNhaCungCap(NhaCungCap ncc) {
        return nccDAO.insert(ncc) > 0;
    }

    public boolean updateNhaCungCap(NhaCungCap ncc) {
        return nccDAO.update(ncc) > 0;
    }

    public boolean deleteNhaCungCap(String maNCC) {
        return nccDAO.delete(maNCC) > 0;
    }

    public String removeAccent(String s) {
        if (s == null) return "";
        String temp = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }

    public ArrayList<NhaCungCap> search(String text, String type) {
        ArrayList<NhaCungCap> result = new ArrayList<>();
        ArrayList<NhaCungCap> listAll = getAllList();
        
        text = removeAccent(text.trim().toLowerCase()); 
        
        for (NhaCungCap ncc : listAll) {
            String ten = ncc.getTenNCC() != null ? removeAccent(ncc.getTenNCC().trim().toLowerCase()) : "";
            String sdt = ncc.getSoDienThoai() != null ? ncc.getSoDienThoai().trim().toLowerCase() : "";
            String ma = ncc.getMaNCC() != null ? removeAccent(ncc.getMaNCC().trim().toLowerCase()) : "";

            if (type.equals("Tất cả")) {
                if (ten.contains(text) || sdt.contains(text) || ma.contains(text)) {
                    result.add(ncc);
                }
            } else if (type.equals("Tên NCC")) {
                if (ten.contains(text)) {
                    result.add(ncc);
                }
            } else if (type.equals("Số điện thoại")) {
                if (sdt.contains(text)) {
                    result.add(ncc);
                }
            }
        }
        return result;
    }
}