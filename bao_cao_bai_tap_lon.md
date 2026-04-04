# BÁO CÁO BÀI TẬP LỚN: HỆ THỐNG QUẢN LÝ CỬA HÀNG/KHO ĐIỆN THOẠI BẰNG JAVA SWING

## 1. Giới thiệu dự án (Tổng quan)
- **Tên đề tài:** Xây dựng phần mềm Quản lý Cửa hàng / Kho Điện thoại.
- **Mục tiêu:** Ứng dụng công nghệ Java Core và Java Swing để xây dựng phần mềm quản lý trên Desktop giúp cửa hàng tự động hoá việc bán hàng, quản lý kho (vật tư/thiết bị điện thoại), quản lý nhân sự, khách hàng, và nhà cung cấp.
- **Phạm vi:** Dành cho các cửa hàng vừa và nhỏ, xử lý các tác vụ như nhập hàng, xuất hàng định kỳ, quản lý danh mục, và phân quyền người dùng nội bộ.

---

## 2. Kiến trúc & Công nghệ áp dụng
Dự án được xây dựng dựa trên nguyên lý thiết kế phần mềm linh hoạt, dễ bảo trì, dễ mở rộng với các kỹ thuật và thư viện uy tín.
- **Ngôn ngữ phát triển:** Java (Java SE / Core).
- **Mô hình lập trình:** Mô hình 3 lớp (MVC - Model/View/Controller hoặc quy chuẩn DTO-DAO-BUS-GUI ở một số module). Phân tách rõ ràng giữa giao diện, luồng điều khiển và thao tác cơ sở dữ liệu.
- **Giao diện người dùng (GUI):** Java Swing kết hợp thư viện **FlatLaf** để mang lại giao diện hiện đại (Flat UI/Material Design), mượt mà và trực quan hơn hẳn so với giao diện mặc định. Hỗ trợ hiển thị icon chất lượng cao bằng **svgSalamander**.
- **Hệ quản trị Cơ sở dữ liệu:** Microsoft SQL Server (hoặc MySQL tùy máy), kết nối thông qua JDBC.
- **Thư viện bên thứ ba nổi bật:**
  - `flatlaf-3.0.jar` & `flatlaf-extras`: Thư viện giao diện.
  - `mssql-jdbc-xxx.jar` hoặc `mysql-connector-java`: JDBC Driver.
  - `itextpdf-5.5.12.jar`: Xuất hóa đơn/phiếu nhập ra định dạng PDF.
  - `javax.mail.jar`: Hỗ trợ gửi Email mã OTP (Đổi mật khẩu/Quên mật khẩu).
  - `jcalendar`: Hỗ trợ Calendar Dialog dễ dàng chọn ngày tháng.

---

## 3. Phân tích thiết kế Cơ sở dữ liệu
Dữ liệu được tổ chức chuẩn hoá qua các bảng chính mang tính thực tiễn cao:
1. **SanPham:** Quản lý thông tin máy (Mã SP, Tên SP, Cấu hình, Đơn giá, Số lượng tồn kho, Hình ảnh...).
2. **KhachHang & NhaCungCap:** Thông tin đối tác (Họ tên, SĐT, Địa chỉ) để truy xuất lịch sử giao dịch.
3. **NhanVien:** Danh sách nhân sự đang làm việc.
4. **Hệ thống chứng từ kho:**
   - **PhieuNhap / ChiTietPhieuNhap:** Ghi nhận lô hàng nhập (Nhân viên nhập, NCC, Ngày nhập, Tổng tiền).
   - **PhieuXuat / ChiTietPhieuXuat:** Giao dịch bán/xuất cho khách hàng (Nhân viên xuất, Khách hàng, Tổng tiền).
5. **Hệ thống Phân Quyền:** Bảng `TaiKhoan`, `NhomQuyen`, `ChiTietQuyen` cho phép cấu hình linh hoạt (ai được xem, thêm, sửa, xóa chức năng nào).

---

## 4. Danh sách các chức năng (Use-case) đưa vào báo cáo
Khi viết báo cáo, bạn nên liệt kê theo từng phân hệ quản lý:

### 4.1. Phân hệ Hệ thống (Tài khoản & Phân quyền)
- **Đăng nhập & Đăng xuất:** Kiểm tra phân quyền để load giao diện (MenuTaskbar) tương ứng.
- **Quản lý Tài Khoản:** Tạo và cấp tài khoản cho Nhân viên.
- **Phân quyền động:** Phân chia thành các nhóm (Admin, Nhân viên bán hàng, Quản lý kho), cấp quyền thao tác Thêm/Sửa/Xóa riêng biệt cho mỗi trang tính năng.
- **Đổi mật khẩu & Quên mật khẩu:** Có hỗ trợ gửi mã OTP qua hệ thống Email (Sử dụng hệ SMTP) để xác thực. Hash mật khẩu (BCrypt) đảm bảo tính bảo mật.

### 4.2. Phân hệ Danh mục
- **Quản lý Sản phẩm:** Thêm, Sửa, Xóa, Tìm kiếm điện thoại. Cho phép upload file ảnh minh họa.
- **Quản lý Nhân viên:** Lưu trữ hồ sơ.
- **Quản lý Khách hàng & Nhà Cung Cấp:** Quản lý thông tin liên hệ.

### 4.3. Phân hệ Nhập / Xuất (Cốt lõi nghiệp vụ)
- **Tạo Phiếu Nhập / Xuất:** Giao diện Split-pane (1 bên chọn sản phẩm bỏ vào giỏ hàng, 1 bên tính toán hoá đơn). Tự động cập nhật (+/-) số lượng tồn kho của Sản phẩm sau khi lưu phiếu.
- **Xem Lịch sử Phiếu:** Tìm kiếm phiếu đa chiều (theo thời gian, theo giá tiền, theo nhân viên). Xem chi tiết từng mặt hàng trong phiếu.

### 4.4. Chức năng Xử lý đặc thù & Cải tiến (Điểm cộng bài làm)
- **Tìm kiếm tích hợp (Integrated Search):** Filter mạnh mẽ cho tất cả các bảng.
- **Xuất file PDF (writePDF):** Render Phiếu Nhập/Xuất thành hóa đơn chuẩn để in ấn.
- **Xuất/Nhập file Excel (JTableExporter):** Dùng Apache POI để kết xuất dữ liệu thống kê ra file `*.xlsx`.
- **Định dạng (Format/Validation):** Biểu diễn tiền tệ định dạng VNĐ tinh tế. Ràng buộc các trường Input chặt chẽ bằng Regex.

---

## 5. Nhật ký/Tiến trình công việc cá nhân (Task Breakdown)
*Phần này rất tốt để ghi điểm ở phần Đánh giá năng lực làm việc.*
- **Giai đoạn 1:** Khảo sát yêu cầu, thiết kế CSDL (ERD, vẽ Diagram), tạo Script DB.
- **Giai đoạn 2:** Setup Base Code, kết nối JDBC, viết Base DAO.
- **Giai đoạn 3:** Xây dựng khung GUI (tích hợp FlatLaf), tạo Menu động.
- **Giai đoạn 4:** Code các Module danh mục (Nhân viên, NCC, Khách, Sản phẩm).
- **Giai đoạn 5:** Code Module nghiệp vụ chuyên sâu (Phiếu Nhập, Phiếu Xuất, Trigger tính Tồn kho).
- **Giai đoạn 6:** Code báo cáo thống kê, các Helper tiện ích (Email, Excel, PDF).
- **Giai đoạn 7:** Testing, dò lỗi luồng nhập xuất, tối ưu Layout.

---

## 6. Tổng kết và Hướng phát triển
- **Đã làm được:** Hoàn thiện 100% Core Java, giao diện đẹp, CSDL chuẩn hóa, phân quyền sâu, có báo cáo in ấn.
- **Hạn chế:** Chỉ chạy trên máy tính Windows/Local (Desktop app).
- **Phát triển:** Bổ sung App Mobile, Quản lý Serial/IMEI từng dòng máy, Tích hợp quét mã Vạch (Barcode/QR) bằng ZXing.
