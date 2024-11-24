package twitter.ui.mainPage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import twitter.main.MainFrame;

public class BookmarkTopPanel extends JPanel {

    private MainFrame mainframe;

    public BookmarkTopPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(7, 7, 7));
        setPreferredSize(new Dimension(400, 80));

        // 상단의 뒤로 가기 버튼, 타이틀, 오른쪽 여백
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(7, 7, 7));
        topPanel.setPreferredSize(new Dimension(400, 40));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // 뒤로 가기 버튼
        JButton backButton = new JButton("<");
        backButton.setForeground(Color.GRAY);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainframe.showTwitterMainUiPanel(); // 이전 화면으로 돌아가는 메서드를 호출
            }
        });
        topPanel.add(backButton, BorderLayout.WEST);

        // 북마크 텍스트
        JLabel titleLabel = new JLabel("북마크", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        // 기존 Font 설정에서 Arial을 기본 폰트로 변경
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Arial 대신 SansSerif 같은 기본 폰트를 사용

        topPanel.add(titleLabel, BorderLayout.CENTER);

        // 오른쪽 빈 공간
        JPanel rightSpacer = new JPanel();
        rightSpacer.setBackground(new Color(7, 7, 7));
        rightSpacer.setPreferredSize(new Dimension(40, 40));
        topPanel.add(rightSpacer, BorderLayout.EAST);

        // topPanel 추가
        add(topPanel, BorderLayout.NORTH);
    }
}
