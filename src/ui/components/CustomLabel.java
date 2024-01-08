package ui.components;

import ui.Const;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Font;

public class CustomLabel extends JPanel {
    private static final int DEFAULT_FONT_SIZE = 16;
    private final JLabel leadingLabel;
    private final JLabel contentLabel;

    public CustomLabel(String leading, String content) {
        this(leading, content, DEFAULT_FONT_SIZE);
    }

    public CustomLabel(String leading, String content, int fontSize) {
        this.setLayout(new FlowLayout());

        this.leadingLabel = new JLabel(leading);
        this.contentLabel = new JLabel(content);

        this.leadingLabel.setFont(new Font(Const.DEFAULT_FONT, Font.BOLD, fontSize));
        this.contentLabel.setFont(new Font(Const.DEFAULT_FONT, Font.PLAIN, fontSize));

        this.add(this.leadingLabel);
        this.add(this.contentLabel);

        this.setMaximumSize(this.getPreferredSize());
    }

    public void setLeading(String leading) {
        this.leadingLabel.setText(leading);
    }

    public void setContent(String content) {
        this.contentLabel.setText(content);
    }
}