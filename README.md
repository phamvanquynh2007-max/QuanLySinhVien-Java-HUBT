# PHẦN MỀM QUẢN LÝ SINH VIÊN (JAVA SWING + SQL SERVER)

**Đồ án môn học Lập trình Java — Khoa Công nghệ thông tin (HUBT)**
- **Sinh viên thực hiện:** Phạm Văn Quỳnh
- **Lớp:** TH30.21
- **Mã sinh viên:** 3025110255

## Cấu trúc thư mục dự án
- `database/QLSinhVien_HUBT.sql`: Script T-SQL tạo cơ sở dữ liệu gồm 4 bảng (`Lop`, `SinhVien`, `MonHoc`, `Diem`) và dữ liệu mẫu.
- `src/util/DBConnection.java`: Lớp cấu hình kết nối JDBC tới Microsoft SQL Server.
- `src/ui/MainForm.java`: Giao diện điều hướng chính của hệ thống.
- `src/ui/SinhVienForm.java`: Giao diện quản lý hồ sơ sinh viên (Thêm, Sửa, Xóa, Tìm kiếm).
- `src/ui/MonHocForm.java`: Giao diện quản lý danh mục môn học.
- `src/ui/DiemForm.java`: Giao diện nhập điểm và tự động tính điểm tổng kết học phần.
