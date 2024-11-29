package twitter.main;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import twitter.Controller.postController;
import twitter.Controller.userController;
import twitter.ui.Comment.ExpandedCommentUI;
import twitter.ui.module.CustomSearchField;
import twitter.ui.login.Login_Ui;
import twitter.ui.post.ExpandedPostUI;
import twitter.ui.signup.SignUp_Ui;
import twitter.ui.topic.Gemini_panel;
import twitter.ui.mainPage.BookmarkTopPanel;
import twitter.ui.mainPage.Main_Ui;
import twitter.ui.mainPage.SearchTopPanel;
import twitter.ui.addPost.addPostUi;
import twitter.ui.profile.UserProfile;

public class MainFrame extends JFrame {

    private static Connection connection;
    private JPanel currentPanel;
    private userController userController = new userController();
    private postController postController = new postController();
    private Main_Ui mainUi;
    private boolean profileView;

    public MainFrame(Connection connection, userController userController) {
        MainFrame.connection = connection;
        this.userController = userController;
        mainUi = new Main_Ui(this, connection, userController, postController);
        add(mainUi); // MainFrame에 Main_Ui 추가


        setTitle("Twitter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 700);

        showTwitterMainUiPanel();

    }

    public static Connection getConnection() {
        return connection;
    }

    public userController getUserService() {
        return userController;
    }
    public postController getPostService() {
        return postController;
    }

    // MainFrame 클래스에 showBookmarkTopPanel 메서드 추가

    public void showBookmarkTopPanel() {
       if (currentPanel != null) {
           remove(currentPanel);
      }

    currentPanel = new BookmarkTopPanel(this);  // BookmarkTopPanel로 설정
    add(currentPanel);
    revalidate();
    repaint();
}


    public void showSearchTopPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        profileView = false;
        currentPanel = new SearchTopPanel(this, connection, userController);  // CustomSearchField에 파라미터 전달
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showCustomSearchFieldPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }

        currentPanel = new CustomSearchField();  // CustomSearchField에 파라미터 전달
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showTwitterMainUiPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        profileView = false;
        mainUi = new Main_Ui(this, connection, userController, postController);
        currentPanel = new Main_Ui(this, connection, userController, postController);  // TwitterMainUI 클래스로부터 UI 로직 실행
        add(currentPanel);
        revalidate();
        repaint();
    }
    // Main_Ui를 반환하는 Getter 추가
    public Main_Ui getMainUi() {
        return mainUi;
    }

    public void showGeminiPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }

        currentPanel = new Gemini_panel(this, connection, userController); // connection, userService 전달
        add(currentPanel);
        revalidate();
        repaint();
    }
    public void showExpandedPostPanel(int postId) {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        profileView = false;
        currentPanel = new ExpandedPostUI(postId, connection, this, userController, postController);  // ExpandedPostUI 클래스로부터 UI 로직 실행
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showExpandedCommentUI(int postId) {
        System.out.println("showExpandedCommentUI 호출됨: Post ID = " + postId);

        ExpandedCommentUI expandedCommentUI = new ExpandedCommentUI(postId, this, userController, connection);

        if (currentPanel != null) {
            remove(currentPanel); // 기존 패널 제거
        }

        currentPanel = expandedCommentUI; // 새 패널로 교체
        add(currentPanel);
        revalidate(); // 레이아웃 갱신
        repaint(); // 화면 갱신

        System.out.println("ExpandedCommentUI로 전환 완료");
    }


    /*

    public void showFollowerPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }

        currentPanel = new Follower_Ui(this, connection, userService);  // CustomSearchField에 파라미터 전달
        add(currentPanel);
        revalidate();
        repaint();
    }
     */

    /*
    public void showFollowingPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        profileView = false;
        currentPanel = new Following_Ui(this, connection, userService);  // CustomSearchField에 파라미터 전달
        add(currentPanel);
        revalidate();
        repaint();
    }
     */

    public void showAddPostPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }

        currentPanel = new addPostUi(this, connection, userController, postController);  // CustomSearchField에 파라미터 전달
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showLoginPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = new Login_Ui(this, connection, userController);  // Login_Ui 클래스로부터 UI 로직 실행
        add(currentPanel);
        revalidate();
        repaint();
    }


    public void showSignUpPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = new SignUp_Ui(this, connection, userController);  // SignUp_Ui 클래스로부터 UI 로직 실행
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showUserProfilePanel(int userId) {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        profileView = true;
        currentPanel = new UserProfile(this, connection, userController, postController, userId); // UserProfile 클래스를 추가
        add(currentPanel);
        revalidate();
        repaint();
    }
    public boolean isProfileView() {
        return profileView;
    }

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://58.121.110.129:4472/twitter", "root", "ckwnsgk@1");
            userController userController = new userController();

            // 로그인 UI 띄우기
            SwingUtilities.invokeLater(() -> {
                MainFrame frame = new MainFrame(connection, userController);
                frame.setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}