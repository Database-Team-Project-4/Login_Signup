package twitter.ui.mainPage;

import twitter.main.MainFrame;
import twitter.service.userService;
import twitter.ui.module.CustomSearchField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

public class SearchTopPanel extends JPanel {
    private JButton popularButton, recentButton;
    private JPanel popularUnderline, recentUnderline, userUnderline, photosUnderline;
    private CustomSearchField searchField; // 인스턴스 변수로 수정
    private JComboBox<String> filterCombo;
    private String currentFilter = "popular";
    private MainFrame mainFrame;
    private Runnable searchListener;


    public SearchTopPanel(MainFrame mainframe, Connection connection, userService userService) {
        setLayout(new BorderLayout());
        setBackground(new Color(7, 7, 7));
        this.mainFrame = mainframe;

        // 뒤로가기 버튼
        JButton backButton = new JButton("<");
        backButton.setForeground(Color.GRAY);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainFrame != null) {  // mainFrame이 null인지 확인
                    mainFrame.showTwitterMainUiPanel();  // 뒤로가기 동작
                } else {
                    System.err.println("Error: MainFrame is null in SearchTopPanel.");
                }
            }
        });

        // 검색창 - 여기에서 CustomSearchField를 사용합니다.
        // CustomSearchField 생성 시 파라미터 없이 생성 (내부에서 placeholderText와 iconPath 정의)
        searchField = new CustomSearchField();




        // 필터 버튼
        JButton filterButton = new JButton("필터");
        filterButton.setForeground(Color.GRAY);
        filterButton.setFocusPainted(false);
        filterButton.setBorderPainted(false);
        filterButton.setContentAreaFilled(false);

        // 상단 바 패널 구성
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(7, 7, 7));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        topBar.add(backButton, BorderLayout.WEST);
        topBar.add(searchField, BorderLayout.CENTER);  // CustomSearchField 추가
        topBar.add(filterButton, BorderLayout.EAST);

        // 하단부 인기/최근/사용자/사진 버튼 패널
        JPanel subTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        subTopPanel.setBackground(new Color(7, 7, 7));

        popularButton = createCustomButton("인기");
        recentButton = createCustomButton("최근");


        // 언더라인 생성
        popularUnderline = createUnderlinePanel();
        recentUnderline = createUnderlinePanel();
        userUnderline = createUnderlinePanel();
        photosUnderline = createUnderlinePanel();

        // 기본 선택 상태 설정
        popularUnderline.setVisible(true);
        popularButton.setForeground(Color.WHITE);

        // 클릭 시 언더라인 설정
        addUnderlineToggle(popularButton, popularUnderline, true);
        addUnderlineToggle(recentButton, recentUnderline, false);


        // 버튼을 패널에 추가
        subTopPanel.add(createButtonPanel(popularButton, popularUnderline));
        subTopPanel.add(createButtonPanel(recentButton, recentUnderline));


        searchField.addActionListener(e -> triggerSearch(mainframe));

        // 각 버튼 클릭 시 이벤트
        popularButton.addActionListener(e -> {
            currentFilter = "popular"; // 필터를 인기순으로 설정
            searchField.setSearchText(searchField.getSearchText());
            triggerSearch(mainframe);  // mainframe을 파라미터로 전달
        });

        recentButton.addActionListener(e -> {
            currentFilter = "recent"; // 필터를 최신순으로 설정
            searchField.setSearchText(searchField.getSearchText());
            triggerSearch(mainframe); // mainframe을 파라미터로 전달
        });


        searchField.addActionListener(e -> triggerSearch(mainframe));

        // 레이아웃에 상단바와 하단부 버튼 패널 추가
        add(topBar, BorderLayout.NORTH);
        add(subTopPanel, BorderLayout.SOUTH);
    }

    // SearchTopPanel 클래스 수정 부분
    public String getKeyword() {
        return searchField.getSearchText(); // 검색 필드에서 키워드 가져오기
    }

    public String getCurrentFilterType() {
        return currentFilter; // 현재 필터 타입 반환
    }

    public void addSearchListener(Runnable listener) {
        searchField.addActionListener(e -> listener.run());
    }

    

    private void triggerSearch(MainFrame mainFrame) {
        String keyword = searchField.getSearchText().trim(); // 검색어 가져오기

        // 검색 텍스트가 비어 있는 경우 처리
        if (keyword.isEmpty()) {
            mainFrame.getMainUi().updateSearchContent("", currentFilter);
        } else {
            mainFrame.getMainUi().updateSearchContent(keyword, currentFilter);

        }

        // 스크롤 초기화
        JPanel mainPanel = mainFrame.getMainUi().getMainPanel();
        JScrollPane scrollPane = (JScrollPane) mainPanel.getParent().getParent();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));

        // 디버그 로그
        System.out.println("검색 실행: 키워드 = " + keyword + ", 필터 = " + currentFilter);
    }



    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.GRAY);
        button.setBackground(new Color(30, 30, 30));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        // 호버 시 텍스트 색상 변경
        button.addMouseListener(new HoverButtonMouseAdapter(button));
        return button;
    }

    private JPanel createUnderlinePanel() {
        JPanel underline = new JPanel();
        underline.setBackground(new Color(0, 122, 255));
        underline.setPreferredSize(new Dimension(50, 3));
        return underline;
    }

    private JPanel createButtonPanel(JButton button, JPanel underline) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(7, 7, 7));
        panel.add(button, BorderLayout.CENTER);
        panel.add(underline, BorderLayout.SOUTH);
        return panel;
    }

    private void addUnderlineToggle(JButton button, JPanel underline, boolean initiallySelected) {
        if (initiallySelected) {
            button.setForeground(Color.WHITE);
            underline.setVisible(true);
        } else {
            underline.setVisible(false);
        }

        button.addMouseListener(new UnderlineToggleMouseAdapter(underline, button));
    }

    private void resetAllUnderlines() {
        popularUnderline.setVisible(false);
        recentUnderline.setVisible(false);
        userUnderline.setVisible(false);
        photosUnderline.setVisible(false);

        popularButton.setForeground(Color.GRAY);
        recentButton.setForeground(Color.GRAY);

    }

    // 내부 클래스들
    private class HoverButtonMouseAdapter extends MouseAdapter {
        private final JButton button;

        public HoverButtonMouseAdapter(JButton button) {
            this.button = button;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!button.getForeground().equals(Color.WHITE)) {
                button.setForeground(Color.LIGHT_GRAY);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!button.getForeground().equals(Color.WHITE)) {
                button.setForeground(Color.GRAY);
            }
        }
    }

    private class UnderlineToggleMouseAdapter extends MouseAdapter {
        private final JPanel underline;
        private final JButton button;

        public UnderlineToggleMouseAdapter(JPanel underline, JButton button) {
            this.underline = underline;
            this.button = button;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            resetAllUnderlines();
            underline.setVisible(true);
            button.setForeground(Color.WHITE);
        }
    }
}