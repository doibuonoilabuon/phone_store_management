package controller;

import DAO.PhieuNhapDAO;
import DAO.ChiTietPhieuNhapDAO;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.PhieuNhap;
import model.ChiTietPhieuNhap;
import view.Panel.TaoPhieuNhapView;

public class PhieuNhapController {
    private TaoPhieuNhapView view;

    public PhieuNhapController(TaoPhieuNhapView view) {
        this.view = view;
    }

    // Hàm xử lý chính khi người dùng nhấn nút "Nhập hàng"
    public void handleNhapHang() {
        // 1. Lấy thông tin phiếu tổng (Mã NCC, Người tạo, Ngày giờ, Tổng tiền)
        PhieuNhap pn = view.getPhieuNhapInfo();
        
        // 2. Lấy danh sách các mặt hàng trong bảng "Giỏ hàng" (Center Table)
        ArrayList<ChiTietPhieuNhap> dsChiTiet = view.getDsChiTiet();

        // Kiểm tra xem đã thêm sản phẩm nào chưa
        if (dsChiTiet.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng thêm ít nhất một sản phẩm vào phiếu nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 3. Thực hiện lưu Phiếu nhập tổng vào Database
            int resultPn = PhieuNhapDAO.getInstance().insert(pn);
            
            // 4. Thực hiện lưu Danh sách chi tiết và tự động TĂNG số lượng tồn kho
            int resultCt = ChiTietPhieuNhapDAO.getInstance().insert(dsChiTiet);

            if (resultPn > 0 && resultCt > 0) {
                JOptionPane.showMessageDialog(view, "Nhập hàng thành công! Dữ liệu kho đã được cập nhật.");
                
                // 5. Tùy chọn: Sau khi nhập thành công thì làm mới form hoặc quay lại danh sách
                // view.resetForm(); 
            } else {
                JOptionPane.showMessageDialog(view, "Lưu dữ liệu thất bại. Vui lòng kiểm tra lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi hệ thống: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}