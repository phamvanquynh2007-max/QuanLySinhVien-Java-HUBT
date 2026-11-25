package ui;

import util.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class MonHocForm extends JFrame {
    private JTextField txtMaMH, txtTenMH, txtSoTinChi, txtHocKy, txtTimKiem;
    private JTable tblMonHoc;
    private DefaultTableModel model;

    public MonHocForm() {
        setTitle("QUẢN LÝ MÔN HỌC");
        setSize(760, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlForm = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin môn học"));

        txtMaMH = new JTextField();
        txtTenMH = new JTextField();
        txtSoTinChi = new JTextField();
        txtHocKy = new JTextField();
        txtTimKiem = new JTextField();

        pnlForm.add(new JLabel("Mã môn học:"));
        pnlForm.add(txtMaMH);
        pnlForm.add(new JLabel("Tên môn học:"));
        pnlForm.add(txtTenMH);

        pnlForm.add(new JLabel("Số tín chỉ:"));
        pnlForm.add(txtSoTinChi);
        pnlForm.add(new JLabel("Học kỳ:"));
        pnlForm.add(txtHocKy);

        pnlForm.add(new JLabel("Tìm kiếm (Mã/Tên MH):"));
        pnlForm.add(txtTimKiem);
        pnlForm.add(new JLabel(""));
        pnlForm.add(new JLabel(""));

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

        model = new DefaultTableModel(new String[]{"Mã MH", "Tên môn học", "Số tín chỉ", "Học kỳ"}, 0);
        tblMonHoc = new JTable(model);
        add(new JScrollPane(tblMonHoc), BorderLayout.CENTER);

        tblMonHoc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblMonHoc.getSelectedRow();
                if (row >= 0) {
                    txtMaMH.setText(model.getValueAt(row, 0).toString());
                    txtMaMH.setEditable(false);
                    txtTenMH.setText(model.getValueAt(row, 1).toString());
                    txtSoTinChi.setText(model.getValueAt(row, 2).toString());
                    txtHocKy.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        btnThem.addActionListener(e -> themMonHoc());
        btnSua.addActionListener(e -> suaMonHoc());
        btnXoa.addActionListener(e -> xoaMonHoc());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnTimKiem.addActionListener(e -> loadData(txtTimKiem.getText().trim()));

        loadData("");
    }

    private void loadData(String keyword) {
        model.setRowCount(0);
        String sql = "SELECT * FROM MonHoc WHERE MaMH LIKE ? OR TenMH LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MaMH"),
                    rs.getString("TenMH"),
                    rs.getInt("SoTinChi"),
                    rs.getInt("HocKy")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải môn học: " + e.getMessage());
        }
    }

    private void themMonHoc() {
        String sql = "INSERT INTO MonHoc(MaMH, TenMH, SoTinChi, HocKy) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtMaMH.getText().trim());
            ps.setString(2, txtTenMH.getText().trim());
            ps.setInt(3, Integer.parseInt(txtSoTinChi.getText().trim()));
            ps.setInt(4, Integer.parseInt(txtHocKy.getText().trim()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm môn học thành công!");
            lamMoi();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm môn học: " + e.getMessage());
        }
    }

    private void suaMonHoc() {
        String sql = "UPDATE MonHoc SET TenMH=?, SoTinChi=?, HocKy=? WHERE MaMH=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtTenMH.getText().trim());
            ps.setInt(2, Integer.parseInt(txtSoTinChi.getText().trim()));
            ps.setInt(3, Integer.parseInt(txtHocKy.getText().trim()));
            ps.setString(4, txtMaMH.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật môn học thành công!");
            lamMoi();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + e.getMessage());
        }
    }

    private void xoaMonHoc() {
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa môn học này?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        String sql = "DELETE FROM MonHoc WHERE MaMH=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtMaMH.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Đã xóa môn học!");
            lamMoi();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
        }
    }

    private void lamMoi() {
        txtMaMH.setText("");
        txtMaMH.setEditable(true);
        txtTenMH.setText("");
        txtSoTinChi.setText("");
        txtHocKy.setText("");
        txtTimKiem.setText("");
        loadData("");
    }
}
