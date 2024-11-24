package twitter.ui.Comment;

import javax.swing.*;
import java.awt.*;

public class CommentContentPanel extends JPanel {
    public CommentContentPanel(String contentText) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        JTextArea contentArea = new JTextArea(contentText);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.BLACK);
        contentArea.setForeground(Color.WHITE);
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(contentArea);
    }
}