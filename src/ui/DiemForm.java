package ui;

import util.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class DiemForm extends JFrame {
    private JComboBox<String> cbMaSV, cbMaMH;
    private JTextField txtDiemCC, txtDiemGK, txtDiemCK, txtTimKiem;
    private JTable tblDiem;
    private DefaultTableModel model;

    public DiemForm() {
        setTitle("QUẢN LÝ ĐIỂM HỌC PHẦN");
        setSize(860, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Nhập điểm sinh viên (Tự động tính Tổng kết = 10% CC + 30% GK + 60% CK)"));

        cbMaSV = new JComboBox<>();
        cbMaMH = new JComboBox<>();
        txtDiemCC = new JTextField();
        txtDiemGK = new JTextField();
        txtDiemCK = new JTextField();
        txtTimKiem = new JTextField();

        pnlForm.add(new JLabel("Mã sinh viên:"));
        pnlForm.add(cbMaSV);
        pnlForm.add(new JLabel("Mã môn học:"));
        pnlForm.add(cbMaMH);

        pnlForm.add(new JLabel("Điểm chuyên cần:"));
        pnlForm.add(txtDiemCC);
        pnlForm.add(new JLabel("Điểm giữa kỳ:"));
        pnlForm.add(txtDiemGK);

        pnlForm.add(new JLabel("Điểm cuối kỳ:"));
        pnlForm.add(txtDiemCK);
        pnlForm.add(new JLabel("Tìm kiếm (Mã SV/Tên):"));
        pnlForm.add(txtTimKiem);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        JButton btnThem = new JButton("Thêm điểm");
        JButton btnSua = new JButton("Cập nhật");
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

        model = new DefaultTableModel(new String[]{"Mã SV", "Họ tên", "Mã MH", "Tên môn học", "Chuyên cần", "Giữa kỳ", "Cuối kỳ", "Tổng kết"}, 0);
        tblDiem = new JTable(model);
        add(new JScrollPane(tblDiem), BorderLayout.CENTER);

        tblDiem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblDiem.getSelectedRow();
                if (row >= 0) {
                    cbMaSV.setSelectedItem(model.getValueAt(row, 0).toString());
                    cbMaMH.setSelectedItem(model.getValueAt(row, 2).toString());
                    txtDiemCC.setText(model.getValueAt(row, 4).toString());
                    txtDiemGK.setText(model.getValueAt(row, 5).toString());
                    txtDiemCK.setText(model.getValueAt(row, 6).toString());
                }
            }
        });

        btnThem.addActionListener(e -> themDiem());
        btnSua.addActionListener(e -> suaDiem());
        btnXoa.addActionListener(e -> xoaDiem());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTimKiem.addActionListener(e -> loadData(txtTimKiem.getText().trim()));

        loadCombos();
        loadData("");
    }

    private void loadCombos() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {
            ResultSet rsSV = st.executeQuery("SELECT MaSV FROM SinhVien");
            cbMaSV.removeAllItems();
            while (rsSV.next()) cbMaSV.addItem(rsSV.getString("MaSV"));

            ResultSet rsMH = st.executeQuery("SELECT MaMH FROM MonHoc");
            cbMaMH.removeAllItems();
            while (rsMH.next()) cbMaMH.addItem(rsMH.getString("MaMH"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh mục: " + e.getMessage());
        }
    }

    private void loadData(String keyword) {
        model.setRowCount(0);
        String sql = "SELECT d.MaSV, sv.HoTen, d.MaMH, mh.TenMH, d.DiemChuyenCan, d.DiemGiuaKy, d.DiemCuoiKy, d.DiemTongKet " +
                     "FROM Diem d JOIN SinhVien sv ON d.MaSV = sv.MaSV JOIN MonHoc mh ON d.MaMH = mh.MaMH " +
                     "WHERE d.MaSV LIKE ? OR sv.HoTen LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaSV"),
                    rs.getString("HoTen"),
                    rs.getString("MaMH"),
                    rs.getString("TenMH"),
                    rs.getDouble("DiemChuyenCan"),
                    rs.getDouble("DiemGiuaKy"),
                    rs.getDouble("DiemCuoiKy"),
                    rs.getDouble("DiemTongKet")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải bảng điểm: " + e.getMessage());
        }
    }

    private double tinhTongKet(double cc, double gk, double ck) {
        return Math.round((cc * 0.1 + gk * 0.3 + ck * 0.6) * 100.0) / 100.0;
    }

    private void themDiem() {
        String sql = "INSERT INTO Diem(MaSV, MaMH, DiemChuyenCan, DiemGiuaKy, DiemCuoiKy, DiemTongKet) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            double cc = Double.parseDouble(txtDiemCC.getText().trim());
            double gk = Double.parseDouble(txtDiemGK.getText().trim());
            double ck = Double.parseDouble(txtDiemCK.getText().trim());
            ps.setString(1, cbMaSV.getSelectedItem().toString());
            ps.setString(2, cbMaMH.getSelectedItem().toString());
            ps.setDouble(3, cc);
            ps.setDouble(4, gk);
            ps.setDouble(5, ck);
            ps.setDouble(6, tinhTongKet(cc, gk, ck));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm điểm thành công!");
            lamMoi();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm điểm: " + e.getMessage());
        }
    }

    private void suaDiem() {
        String sql = "UPDATE Diem SET DiemChuyenCan=?, DiemGiuaKy=?, DiemCuoiKy=?, DiemTongKet=? WHERE MaSV=? AND MaMH=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            double cc = Double.parseDouble(txtDiemCC.getText().trim());
            double gk = Double.parseDouble(txtDiemGK.getText().trim());
            double ck = Double.parseDouble(txtDiemCK.getText().trim());
            ps.setDouble(1, cc);
            ps.setDouble(2, gk);
            ps.setDouble(3, ck);
            ps.setDouble(4, tinhTongKet(cc, gk, ck));
            ps.setString(5, cbMaSV.getSelectedItem().toString());
            ps.setString(6, cbMaMH.getSelectedItem().toString());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật điểm thành công!");
            lamMoi();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật điểm: " + e.getMessage());
        }
    }

    private void xoaDiem() {
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa bản ghi điểm này?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        String sql = "DELETE FROM Diem WHERE MaSV=? AND MaMH=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cbMaSV.getSelectedItem().toString());
            ps.setString(2, cbMaMH.getSelectedItem().toString());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Đã xóa bản ghi điểm!");
            lamMoi();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa điểm: " + e.getMessage());
        }
    }

    private void lamMoi() {
        txtDiemCC.setText("");
        txtDiemGK.setText("");
        txtDiemCK.setText("");
        txtTimKiem.setText("");
        loadData("");
    }
}
