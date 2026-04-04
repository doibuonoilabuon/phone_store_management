package controller;

import DAO.PhieuXuatDAO;
import DAO.ChiTietPhieuXuatDAO;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.PhieuXuat;
import model.ChiTietPhieuXuat;
import view.Panel.TaoPhieuXuatView;

public class PhieuXuatController {
    private TaoPhieuXuatView view;

    public PhieuXuatController(TaoPhieuXuatView view) {
        this.view = view;
    }

    // Hàm thực hiện lưu hóa đơn xuất hàng
    public void handleXuatHang() {
        // 1. Lấy thông tin hóa đơn xuất
        PhieuXuat px = view.getPhieuXuatInfo();
        
        // 2. Lấy danh sách sản phẩm khách mua
        ArrayList<ChiTietPhieuXuat> dsChiTiet = view.getDsChiTiet();

        if (dsChiTiet.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Giỏ hàng đang trống!");
            return;
        }

        try {
            // Lưu phiếu xuất tổng
            int checkPx = PhieuXuatDAO.getInstance().insert(px);
            
            // Lưu chi tiết và trừ số lượng trong kho
            int checkCt = ChiTietPhieuXuatDAO.getInstance().insert(dsChiTiet);

            if (checkPx > 0 && checkCt > 0) {
                JOptionPane.showMessageDialog(view, "Xuất hàng thành công! Hóa đơn đã được lưu.");
            } else {
                JOptionPane.showMessageDialog(view, "Lỗi lưu phiếu xuất!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage());
        }
    }
}