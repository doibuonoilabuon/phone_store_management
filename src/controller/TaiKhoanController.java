package controller;

import DAO.TaiKhoanDAO;
import model.TaiKhoan;
import java.util.ArrayList;

public class TaiKhoanController {
    
    private TaiKhoanDAO tkDAO;

    public TaiKhoanController() {
        tkDAO = TaiKhoanDAO.getInstance();
    }

    public ArrayList<TaiKhoan> getAllList() {
        return tkDAO.selectAll();
    }

    public boolean addTaiKhoan(TaiKhoan tk) {
        return tkDAO.insert(tk) > 0;
    }

    public boolean updateTaiKhoan(TaiKhoan tk) {
        return tkDAO.update(tk) > 0;
    }

    public boolean lockTaiKhoan(String username) {
        return tkDAO.delete(username) > 0;
    }

    public ArrayList<TaiKhoan> search(String text, String type) {
        ArrayList<TaiKhoan> result = new ArrayList<>();
        ArrayList<TaiKhoan> listAll = getAllList();
        
        text = text.trim().toLowerCase(); 
        
        for (TaiKhoan tk : listAll) {
            String user = tk.getUsername() != null ? tk.getUsername().toLowerCase() : "";
            String quyen = tk.getManhomquyen() == 1 ? "quản lý" : "nhân viên";

            if (type.equals("Tất cả")) {
                if (user.contains(text) || quyen.contains(text)) {
                    result.add(tk);
                }
            } else if (type.equals("Tên đăng nhập")) {
                if (user.contains(text)) {
                    result.add(tk);
                }
            } else if (type.equals("Nhóm quyền")) {
                if (quyen.contains(text)) {
                    result.add(tk);
                }
            }
        }
        return result;
    }
}