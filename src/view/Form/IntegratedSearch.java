package view.Form;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class IntegratedSearch extends JPanel {

    public JComboBox<String> cbxChoose;
    public JButton btnReset;
    public JTextField txtSearchForm;

    public IntegratedSearch(String[] options) {
        initComponent(options);
    }

    private void initComponent(String[] options) {
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout(8, 0));
        this.setBorder(new EmptyBorder(18, 15, 18, 15));

        // ComboBox lọc loại
        cbxChoose = new JComboBox<>(options);
        cbxChoose.setPreferredSize(new Dimension(120, 36));
        cbxChoose.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        cbxChoose.setFocusable(false);
        this.add(cbxChoose, BorderLayout.WEST);

        // TextField tìm kiếm
        txtSearchForm = new JTextField();
        txtSearchForm.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        txtSearchForm.putClientProperty("JTextField.placeholderText", "Nhập nội dung tìm kiếm...");
        txtSearchForm.putClientProperty("JTextField.showClearButton", true);
        this.add(txtSearchForm, BorderLayout.CENTER);

        // Nút Làm mới
        btnReset = new JButton("Làm mới");
        btnReset.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        try {
            btnReset.setIcon(new FlatSVGIcon("./icon/refresh.svg", 18, 18));
        } catch (Exception ignored) {}
        btnReset.setPreferredSize(new Dimension(120, 36));
        btnReset.setFocusable(false);
        btnReset.addActionListener(e -> {
            txtSearchForm.setText("");
            cbxChoose.setSelectedIndex(0);
        });
        this.add(btnReset, BorderLayout.EAST);
    }
}
