package view.help;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

import DAO.NhanVienDAO;
import DAO.TaiKhoanDAO;

public class QuenMatKhau extends JDialog implements ActionListener {

    private JButton btnSendMail, btnConfirmOTP, btnChangePass;
    private JPanel jpTop, jpMain, jpCard_1, jpCard_2, jpCard_3;
    private JLabel lblTitle, lblNhapEmail, lblNhapOTP, lblNhapPassword;
    private JTextField txtEmail, txtOTP;
    private JPasswordField txtPassword;

    private String generatedOTP; // lưu OTP
    private String emailNhan; 
    public QuenMatKhau(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
    }

    public void initComponents() {
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setTitle("Quên mật khẩu");
        this.setSize(new Dimension(500, 250));
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        // ===== TOP =====
        jpTop = new JPanel(new BorderLayout());
        jpTop.setBackground(new Color(22, 122, 198));
        jpTop.setPreferredSize(new Dimension(400, 60));

        lblTitle = new JLabel("QUÊN MẬT KHẨU", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        jpTop.add(lblTitle, BorderLayout.CENTER);

        // ===== MAIN =====
        jpMain = new JPanel(new CardLayout());

        // ===== STEP 1: EMAIL =====
        jpCard_1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        jpCard_1.setBackground(Color.WHITE);

        lblNhapEmail = new JLabel("Nhập địa chỉ email:");
        txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(350, 35));

        btnSendMail = new JButton("Gửi mã");
        btnSendMail.addActionListener(this);

        jpCard_1.add(lblNhapEmail);
        jpCard_1.add(txtEmail);
        jpCard_1.add(btnSendMail);

        // ===== STEP 2: OTP =====
        jpCard_2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        jpCard_2.setBackground(Color.WHITE);

        lblNhapOTP = new JLabel("Nhập mã OTP:");
        txtOTP = new JTextField();
        txtOTP.setPreferredSize(new Dimension(350, 35));

        btnConfirmOTP = new JButton("Xác nhận");
        btnConfirmOTP.addActionListener(this);

        jpCard_2.add(lblNhapOTP);
        jpCard_2.add(txtOTP);
        jpCard_2.add(btnConfirmOTP);

        // ===== STEP 3: PASSWORD =====
        jpCard_3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        jpCard_3.setBackground(Color.WHITE);

        lblNhapPassword = new JLabel("Nhập mật khẩu mới:");
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(350, 35));

        btnChangePass = new JButton("Đổi mật khẩu");
        btnChangePass.addActionListener(this);

        jpCard_3.add(lblNhapPassword);
        jpCard_3.add(txtPassword);
        jpCard_3.add(btnChangePass);

        // ===== ADD CARD =====
        jpMain.add(jpCard_1, "step1");
        jpMain.add(jpCard_2, "step2");
        jpMain.add(jpCard_3, "step3");

        this.add(jpTop, BorderLayout.NORTH);
        this.add(jpMain, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        CardLayout c = (CardLayout) jpMain.getLayout();

        // =====================================
        // STEP 1: EMAIL
        // =====================================
        if (e.getSource() == btnSendMail) {

            String email = txtEmail.getText().trim();

            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không được để trống email");
                return;
            }

            String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);

            if (!matcher.matches()) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ");
                return;
            }
            if (!NhanVienDAO.getInstance().checkEmail(email)) {
                JOptionPane.showMessageDialog(this, "Email này không tồn tại trên hệ thống nhân viên!");
                return; // Dừng lại luôn, không gửi mail nữa
            }
            // Lưu email lại để dùng
            this.emailNhan = email;

           
            btnSendMail.setText("Đang gửi...");
            btnSendMail.setEnabled(false);

            // Dùng SwingWorker để gửi mail ngầm, không làm đơ giật giao diện
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    // Gọi sang class helper của sếp để tạo mã và gửi mail
                    generatedOTP = SendEmailSMTP.getOTP();
                    SendEmailSMTP.sendOTP(emailNhan, generatedOTP);
                    return null;
                }

                @Override
                protected void done() {
                    // Chạy xong thì trả lại nút bấm như cũ và chuyển màn hình
                    btnSendMail.setText("Gửi mã");
                    btnSendMail.setEnabled(true);
                    
                    JOptionPane.showMessageDialog(QuenMatKhau.this, "Đã gửi mã OTP đến email của bạn! Vui lòng kiểm tra hộp thư.");
                    c.show(jpMain, "step2");
                }
            };
            worker.execute(); // Bắt đầu tiến trình ngầm
        }

       
        else if (e.getSource() == btnConfirmOTP) {

            String otp = txtOTP.getText().trim();

            if (otp.equals(generatedOTP)) {
                JOptionPane.showMessageDialog(this, "Xác nhận OTP thành công!");
                c.show(jpMain, "step3");
            } else {
                JOptionPane.showMessageDialog(this, "Mã OTP không chính xác, vui lòng thử lại!");
            }
        }

      
        else if (e.getSource() == btnChangePass) {

            String pass = new String(txtPassword.getPassword());

            if (pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không được để trống mật khẩu");
                return;
            }

           
             TaiKhoanDAO.getInstance().updatePasswordByEmail(emailNhan, pass);
            
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            this.dispose();
        }
    }
}