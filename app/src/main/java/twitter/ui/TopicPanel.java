package twitter.ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class TopicPanel extends JPanel {

    public TopicPanel(Map<String, Integer> topics) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);  // Background color of the main panel

        // Title label - Centered large text
        JLabel titleLabel = new JLabel("This Week's Topics", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Panel for holding the cards with GridBagLayout for better control
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.BLACK); // Background color for the grid panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between cards

        int row = 0;
        int col = 0;

        // Iterate over the topics and create cards for each one
        for (Map.Entry<String, Integer> entry : topics.entrySet()) {
            String keyword = entry.getKey();
            int frequency = entry.getValue();

            // Create a panel for each topic (card)
            RoundedPanel cardPanel = new RoundedPanel(30); // Custom panel with rounded corners (30px)
            cardPanel.setLayout(new BorderLayout());
            cardPanel.setPreferredSize(new Dimension(150, 150)); // Set smaller size for each card

            // Set card background to match parent (black) and add a subtle border
            cardPanel.setBackground(Color.BLACK);
            cardPanel.setBorder(new CompoundBorder(
                    new LineBorder(new Color(66, 66, 66), 1),  // Darker gray outer border (#424242)
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)   // Inner padding
            ));

            // Create a label for the keyword and frequency
            JLabel keywordLabel = new JLabel("<html><b>" + keyword + "</b><br>Frequency: " + frequency + "</html>");
            keywordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            keywordLabel.setForeground(Color.WHITE); // Text color is white to contrast with black background
            keywordLabel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding inside the card

            cardPanel.add(keywordLabel, BorderLayout.NORTH); // Align text to top-left corner

            // Add hover effect
            cardPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    cardPanel.setBackground(Color.DARK_GRAY); // Change background on hover
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    cardPanel.setBackground(Color.BLACK); // Revert background when not hovered
                }
            });

            gbc.gridx = col;
            gbc.gridy = row;
            gridPanel.add(cardPanel, gbc);

            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }

        add(gridPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Topics Overview");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450);

        // Example topics data
        Map<String, Integer> topics = Map.of(
                "Topic 1", 10,
                "Topic 2", 20,
                "Topic 3", 15,
                "Topic 4", 5
        );

        TopicPanel topicPanel = new TopicPanel(topics);
        frame.add(topicPanel);

        frame.setVisible(true);
    }
}

// Custom JPanel with rounded corners and matching background color
class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Dimension arcs = new Dimension(cornerRadius, cornerRadius); // Arc width and height for rounded corners
        int width = getWidth();
        int height = getHeight();
        
        Graphics2D graphics = (Graphics2D) g;
        
        // Set anti-aliasing for smoother edges
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded rectangle with background color matching parent background (black)
        graphics.setColor(getParent().getBackground()); 
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);

        // Draw border with rounded corners and dark gray fill inside
        graphics.setColor(getBackground());
        graphics.fillRoundRect(1, 1, width - 2, height - 2, arcs.width - 1, arcs.height - 1);
    }
}