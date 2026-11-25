CREATE DATABASE QLSinhVien_HUBT;
GO

USE QLSinhVien_HUBT;
GO

CREATE TABLE Lop (
    MaLop VARCHAR(15) PRIMARY KEY,
    TenLop NVARCHAR(100) NOT NULL,
    Khoa NVARCHAR(100) NOT NULL,
    NienKhoa VARCHAR(20)
);
GO

CREATE TABLE SinhVien (
    MaSV VARCHAR(15) PRIMARY KEY,
    HoTen NVARCHAR(100) NOT NULL,
    NgaySinh DATE NOT NULL,
    GioiTinh NVARCHAR(10) CHECK (GioiTinh IN (N'Nam', N'Nữ')),
    DiaChi NVARCHAR(200),
    SoDienThoai VARCHAR(15),
    MaLop VARCHAR(15) NOT NULL,
    CONSTRAINT FK_SinhVien_Lop FOREIGN KEY (MaLop) REFERENCES Lop(MaLop)
        ON UPDATE CASCADE ON DELETE NO ACTION
);
GO

CREATE TABLE MonHoc (
    MaMH VARCHAR(15) PRIMARY KEY,
    TenMH NVARCHAR(100) NOT NULL,
    SoTinChi INT NOT NULL CHECK (SoTinChi > 0),
    HocKy INT NOT NULL CHECK (HocKy >= 1)
);
GO

CREATE TABLE Diem (
    MaSV VARCHAR(15) NOT NULL,
    MaMH VARCHAR(15) NOT NULL,
    DiemChuyenCan FLOAT CHECK (DiemChuyenCan >= 0 AND DiemChuyenCan <= 10),
    DiemGiuaKy FLOAT CHECK (DiemGiuaKy >= 0 AND DiemGiuaKy <= 10),
    DiemCuoiKy FLOAT CHECK (DiemCuoiKy >= 0 AND DiemCuoiKy <= 10),
    DiemTongKet FLOAT CHECK (DiemTongKet >= 0 AND DiemTongKet <= 10),
    CONSTRAINT PK_Diem PRIMARY KEY (MaSV, MaMH),
    CONSTRAINT FK_Diem_SinhVien FOREIGN KEY (MaSV) REFERENCES SinhVien(MaSV)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_Diem_MonHoc FOREIGN KEY (MaMH) REFERENCES MonHoc(MaMH)
        ON UPDATE CASCADE ON DELETE CASCADE
);
GO

INSERT INTO Lop VALUES
('TH30.21', N'Tin học ứng dụng 30.21', N'Công nghệ thông tin', '2025-2029'),
('TH30.22', N'Tin học ứng dụng 30.22', N'Công nghệ thông tin', '2025-2029'),
('PM30.01', N'Kỹ thuật phần mềm 30.01', N'Công nghệ thông tin', '2025-2029');

INSERT INTO SinhVien VALUES
('3025110255', N'Phạm Văn Quỳnh', '2007-12-24', N'Nam', N'Hà Nội', '0987654321', 'TH30.21'),
('3025110256', N'Nguyễn Thị Minh Anh', '2007-05-14', N'Nữ', N'Hà Nội', '0912345678', 'TH30.21'),
('3025110257', N'Trần Hoàng Nam', '2007-08-20', N'Nam', N'Bắc Ninh', '0934567890', 'TH30.22'),
('3025110258', N'Lê Thu Hà', '2007-11-02', N'Nữ', N'Hải Dương', '0978123456', 'PM30.01');

INSERT INTO MonHoc VALUES
('JAVA01', N'Lập trình Java cơ bản và nâng cao', 3, 3),
('CSDL01', N'Hệ quản trị Cơ sở dữ liệu SQL Server', 3, 2),
('WEB01', N'Thiết kế và Lập trình Web', 3, 3),
('TRR01', N'Toán rời rạc', 2, 2);

INSERT INTO Diem VALUES
('3025110255', 'JAVA01', 9.0, 8.5, 9.0, 8.85),
('3025110255', 'CSDL01', 9.5, 8.0, 8.5, 8.45),
('3025110256', 'JAVA01', 8.5, 8.0, 8.5, 8.35),
('3025110257', 'WEB01', 8.0, 7.5, 8.0, 7.85);
GO
