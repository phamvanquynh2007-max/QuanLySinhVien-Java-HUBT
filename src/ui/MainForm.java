package ui;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {
    public MainForm() {
        setTitle("HỆ THỐNG QUẢN LÝ SINH VIÊN - HUBT");
        setSize(520, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("PHẦN MỀM QUẢN LÝ SINH VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 70, 140));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlMenu = new JPanel(new GridLayout(3, 1, 15, 15));
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(15, 60, 25, 60));

        JButton btnSinhVien = new JButton("1. QUẢN LÝ HỒ SƠ SINH VIÊN");
        JButton btnMonHoc = new JButton("2. QUẢN LÝ DANH MỤC MÔN HỌC");
        JButton btnDiem = new JButton("3. QUẢN LÝ ĐIỂM & KẾT QUẢ HỌC TẬP");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        btnSinhVien.setFont(btnFont);
        btnMonHoc.setFont(btnFont);
        btnDiem.setFont(btnFont);

        btnSinhVien.addActionListener(e -> new SinhVienForm().setVisible(true));
        btnMonHoc.addActionListener(e -> new MonHocForm().setVisible(true));
        btnDiem.addActionListener(e -> new DiemForm().setVisible(true));

        pnlMenu.add(btnSinhVien);
        pnlMenu.add(btnMonHoc);
        pnlMenu.add(btnDiem);
        add(pnlMenu, BorderLayout.CENTER);

        JLabel lblFooter = new JLabel("Sinh viên thực hiện: Phạm Văn Quỳnh - Lớp TH30.21", SwingConstants.CENTER);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        add(lblFooter, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainForm().setVisible(true));
    }
}
