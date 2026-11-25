package ui;

import util.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class SinhVienForm extends JFrame {
    private JTextField txtMaSV, txtHoTen, txtNgaySinh, txtDiaChi, txtSDT, txtTimKiem;
    private JComboBox<String> cbGioiTinh, cbMaLop;
    private JTable tblSinhVien;
    private DefaultTableModel model;

    public SinhVienForm() {
        setTitle("QUẢN LÝ SINH VIÊN");
        setSize(860, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin sinh viên"));

        txtMaSV = new JTextField();
        txtHoTen = new JTextField();
        txtNgaySinh = new JTextField("2007-01-01");
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        txtDiaChi = new JTextField();
        txtSDT = new JTextField();
        cbMaLop = new JComboBox<>();
        txtTimKiem = new JTextField();

        pnlForm.add(new JLabel("Mã sinh viên:"));
        pnlForm.add(txtMaSV);
        pnlForm.add(new JLabel("Họ và tên:"));
        pnlForm.add(txtHoTen);

        pnlForm.add(new JLabel("Ngày sinh (yyyy-MM-dd):"));
        pnlForm.add(txtNgaySinh);
        pnlForm.add(new JLabel("Giới tính:"));
        pnlForm.add(cbGioiTinh);

        pnlForm.add(new JLabel("Địa chỉ:"));
        pnlForm.add(txtDiaChi);
        pnlForm.add(new JLabel("Số điện thoại:"));
        pnlForm.add(txtSDT);

        pnlForm.add(new JLabel("Lớp:"));
        pnlForm.add(cbMaLop);
        pnlForm.add(new JLabel("Tìm kiếm (Mã/Tên):"));
        pnlForm.add(txtTimKiem);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLamMoi = new JButton("Làm mới");
        JButton btnTimKiem = new JButton("Tìm kiếm");

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        pnlButtons.add(btnTimKiem);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(pnlForm, BorderLayout.CENTER);
        pnlTop.add(pnlButtons, BorderLayout.SOUTH);
        add(pnlTop, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Mã SV", "Họ tên", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT", "Mã lớp"}, 0);
        tblSinhVien = new JTable(model);
        add(new JScrollPane(tblSinhVien), BorderLayout.CENTER);

        tblSinhVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblSinhVien.getSelectedRow();
                if (row >= 0) {
                    txtMaSV.setText(model.getValueAt(row, 0).toString());
                    txtMaSV.setEditable(false);
                    txtHoTen.setText(model.getValueAt(row, 1).toString());
                    txtNgaySinh.setText(model.getValueAt(row, 2).toString());
                    cbGioiTinh.setSelectedItem(model.getValueAt(row, 3).toString());
                    txtDiaChi.setText(model.getValueAt(row, 4).toString());
                    txtSDT.setText(model.getValueAt(row, 5).toString());
                    cbMaLop.setSelectedItem(model.getValueAt(row, 6).toString());
                }
            }
        });

        btnThem.addActionListener(e -> themSinhVien());
        btnSua.addActionListener(e -> suaSinhVien());
        btnXoa.addActionListener(e -> xoaSinhVien());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTimKiem.addActionListener(e -> timKiemSinhVien());

        loadComboLop();
        loadData("");
    }

    private void loadComboLop() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT MaLop FROM Lop")) {
            cbMaLop.removeAllItems();
            while (rs.next()) {
                cbMaLop.addItem(rs.getString("MaLop"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách lớp: " + e.getMessage());
        }
    }

    private void loadData(String keyword) {
        model.setRowCount(0);
        String sql = "SELECT * FROM SinhVien WHERE MaSV LIKE ? OR HoTen LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaSV"),
                    rs.getString("HoTen"),
                    rs.getDate("NgaySinh"),
                    rs.getString("GioiTinh"),
                    rs.getString("DiaChi"),
                    rs.getString("SoDienThoai"),
                    rs.getString("MaLop")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void themSinhVien() {
        String sql = "INSERT INTO SinhVien(MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, SoDienThoai, MaLop) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtMaSV.getText().trim());
            ps.setString(2, txtHoTen.getText().trim());
            ps.setDate(3, Date.valueOf(txtNgaySinh.getText().trim()));
            ps.setString(4, cbGioiTinh.getSelectedItem().toString());
            ps.setString(5, txtDiaChi.getText().trim());
            ps.setString(6, txtSDT.getText().trim());
            ps.setString(7, cbMaLop.getSelectedItem().toString());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
            lamMoi();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm sinh viên: " + e.getMessage());
        }
    }

    private void suaSinhVien() {
        String sql = "UPDATE SinhVien SET HoTen=?, NgaySinh=?, GioiTinh=?, DiaChi=?, SoDienThoai=?, MaLop=? WHERE MaSV=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtHoTen.getText().trim());
            ps.setDate(2, Date.valueOf(txtNgaySinh.getText().trim()));
            ps.setString(3, cbGioiTinh.getSelectedItem().toString());
            ps.setString(4, txtDiaChi.getText().trim());
            ps.setString(5, txtSDT.getText().trim());
            ps.setString(6, cbMaLop.getSelectedItem().toString());
            ps.setString(7, txtMaSV.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
            lamMoi();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + e.getMessage());
        }
    }

    private void xoaSinhVien() {
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa sinh viên này?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        String sql = "DELETE FROM SinhVien WHERE MaSV=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtMaSV.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Đã xóa sinh viên!");
            lamMoi();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
        }
    }

    private void timKiemSinhVien() {
        loadData(txtTimKiem.getText().trim());
    }

    private void lamMoi() {
        txtMaSV.setText("");
        txtMaSV.setEditable(true);
        txtHoTen.setText("");
        txtNgaySinh.setText("2007-01-01");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtTimKiem.setText("");
        loadData("");
    }
}
