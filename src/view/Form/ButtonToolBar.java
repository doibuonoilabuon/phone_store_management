package view.Form;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.*;
import javax.swing.JButton;

/**
 * Nút toolbar lớn có icon bên trên, text bên dưới.
 * Kiểu giống ảnh mẫu: icon tròn màu + chữ bên dưới.
 */
public class ButtonToolBar extends JButton {

    private final String permission;

    /**
     * @param text       Nhãn hiển thị bên dưới icon (VD: "THÊM", "SỬA", "XÓA")
     * @param iconFile   Tên file SVG trong ./icon/ (VD: "add.svg")
     * @param permission Quyền: "create" | "update" | "delete" | "view"
     * @param iconSize   Kích thước icon (px), thường 40 hoặc 48
     */
    public ButtonToolBar(String text, String iconFile, String permission, int iconSize) {
        this.permission = permission;

        setText(text);
        setFocusable(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 12));
        setForeground(new Color(1, 88, 155));

        // Icon lớn: text ở dưới, icon ở trên (CENTER + BOTTOM)
        setHorizontalTextPosition(CENTER);
        setVerticalTextPosition(BOTTOM);

        // Dùng FlatLaf toolBarButton để bỏ viền, nền trong suốt
        putClientProperty("JButton.buttonType", "toolBarButton");

        try {
            setIcon(new FlatSVGIcon("./icon/" + iconFile, iconSize, iconSize));
        } catch (Exception ignored) {}
    }

    /** Constructor với icon size mặc định 40px */
    public ButtonToolBar(String text, String iconFile, String permission) {
        this(text, iconFile, permission, 40);
    }

    public String getPermission() {
        return permission;
    }
}
