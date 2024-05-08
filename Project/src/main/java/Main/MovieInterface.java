package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import DataBase.*;
import Helpers.*;

public class MovieInterface extends JFrame {
    private static final long serialVersionUID = 1L;
    private DBConnect connect = new DBConnect();
    private Film film;
    private User user;
    private StarRating starRating;
    private float average;
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private StarRating2 starRatingComponent;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Film film = new Film();
                    User user = new User();
                    MovieInterface frame = new MovieInterface(film, user);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MovieInterface(Film film, User user) throws SQLException {
        this.film = film;
        this.user = user;

        setTitle("Removapp");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1156, 689);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setBackground(Color.BLACK);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(null);

        scrollPane = new JScrollPane(panel);
        scrollPane.setViewportView(panel);
        scrollPane.setBounds(10, 11, 1124, 628);
        contentPane.add(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        Dimension panelSize = panel.getPreferredSize();
        scrollPane.setPreferredSize(panelSize);
        scrollPane.setPreferredSize(new Dimension(1142, 2000));

        panel.setPreferredSize(new Dimension(1124, 728));
        String imageUrl = film.getFilm_gorselleri();
        try {
            URL url = new URL(imageUrl);
            Image image = ImageIO.read(url);
            Image scaledImage = image.getScaledInstance(200, 300, Image.SCALE_AREA_AVERAGING);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel label = new JLabel(scaledIcon);
            label.setBounds(10, 11, 200, 300);
            panel.add(label);

            JTextArea descriptionTextArea = new JTextArea(film.getAciklama());
            descriptionTextArea.setFont(new Font("Yu Gothic UI", Font.PLAIN, 15));
            descriptionTextArea.setBackground(Color.BLACK);
            descriptionTextArea.setForeground(Color.WHITE);
            descriptionTextArea.setEditable(false);
            descriptionTextArea.setFocusable(false);
            descriptionTextArea.setBounds(220, 179, 880, 120);
            descriptionTextArea.setLineWrap(true);
            descriptionTextArea.setWrapStyleWord(true);
            panel.add(descriptionTextArea);

            String cast = film.getOyuncular().substring(0, film.getOyuncular().length() - 2) + ".";
            JTextArea castTextArea = new JTextArea(cast);
            castTextArea.setFont(new Font("Yu Gothic UI", Font.PLAIN, 14));
            castTextArea.setBackground(Color.BLACK);
            castTextArea.setForeground(Color.WHITE);
            castTextArea.setEditable(false);
            castTextArea.setFocusable(false);
            castTextArea.setBounds(220, 343, 880, 325);
            castTextArea.setLineWrap(true);
            castTextArea.setWrapStyleWord(true);
            panel.add(castTextArea);

            JLabel actorsLabel = new JLabel("Cast:");
            actorsLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 23));
            actorsLabel.setForeground(new Color(224, 188, 25));
            actorsLabel.setBounds(220, 306, 190, 37);
            panel.add(actorsLabel);

            JLabel directorLabel = new JLabel("Director:");
            directorLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
            directorLabel.setForeground(new Color(224, 188, 25));
            directorLabel.setBounds(10, 399, 48, 31);
            panel.add(directorLabel);

            JLabel directorNameLabel = new JLabel(film.getYonetmen());
            directorNameLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
            directorNameLabel.setForeground(Color.WHITE);
            directorNameLabel.setBounds(63, 399, 147, 31);
            panel.add(directorNameLabel);

            JLabel durationLabel = new JLabel("Duration:");
            durationLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
            durationLabel.setForeground(new Color(224, 188, 25));
            durationLabel.setBounds(10, 429, 58, 31);
            panel.add(durationLabel);
            String duration = (film.getFilm_suresi() / 60) + "h" + (film.getFilm_suresi() % 60) + "m";
            JLabel durationTimeLabel = new JLabel(duration);
            durationTimeLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
            durationTimeLabel.setForeground(Color.WHITE);
            durationTimeLabel.setBounds(68, 429, 142, 31);
            panel.add(durationTimeLabel);

            JLabel originLabel = new JLabel("Origin:");
            originLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
            originLabel.setForeground(new Color(224, 188, 25));
            originLabel.setBounds(10, 371, 48, 31);
            panel.add(originLabel);

            JLabel countryLabel = new JLabel(film.getUlke());
            countryLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
            countryLabel.setForeground(Color.WHITE);
            countryLabel.setBounds(63, 371, 147, 31);
            panel.add(countryLabel);

            JLabel releaseDateLabel = new JLabel("Release Date:");
            releaseDateLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
            releaseDateLabel.setForeground(new Color(224, 188, 25));
            releaseDateLabel.setBounds(10, 458, 75, 31);
            panel.add(releaseDateLabel);

            JLabel releaseDate = new JLabel(film.getVizyon_tarihi());
            releaseDate.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
            releaseDate.setForeground(Color.WHITE);
            releaseDate.setBounds(89, 458, 121, 31);
            panel.add(releaseDate);

            JLabel tmdbVoteLabel = new JLabel("TMDB Rating");
            tmdbVoteLabel.setHorizontalAlignment(SwingConstants.CENTER);
            tmdbVoteLabel.setFont(new Font("Yu Gothic UI", Font.BOLD | Font.ITALIC, 20));
            tmdbVoteLabel.setForeground(new Color(224, 188, 25));
            tmdbVoteLabel.setBounds(10, 499, 190, 41);
            panel.add(tmdbVoteLabel);

            JLabel tmdbVoteValueLabel = new JLabel(Double.toString(film.getFilm_puani()));
            tmdbVoteValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            tmdbVoteValueLabel.setFont(new Font("Yu Gothic UI", Font.BOLD | Font.ITALIC, 20));
            tmdbVoteValueLabel.setForeground(Color.WHITE);
            tmdbVoteValueLabel.setBounds(37, 540, 136, 38);
            panel.add(tmdbVoteValueLabel);

            JLabel overviewLabel = new JLabel("Overview:");
            overviewLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 23));
            overviewLabel.setForeground(new Color(224, 188, 25));
            overviewLabel.setBounds(220, 130, 111, 37);
            panel.add(overviewLabel);

            JLabel filmNameLabel = new JLabel(film.getFilm_adi());
            filmNameLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 27));
            filmNameLabel.setForeground(new Color(224, 188, 25));
            filmNameLabel.setBounds(220, 10, 746, 41);
            panel.add(filmNameLabel);

            JLabel genreLabel = new JLabel(film.getTur());
            genreLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 20));
            genreLabel.setForeground(new Color(192, 192, 192));
            genreLabel.setBounds(220, 61, 880, 41);
            panel.add(genreLabel);

            JTextArea commentTextArea = new JTextArea("Write your comment here...(MAX 513 CHARACTERS)");
            commentTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
            commentTextArea.setLineWrap(true);
            commentTextArea.setWrapStyleWord(true);
            commentTextArea.setForeground(Color.GRAY);
            commentTextArea.setBackground(Color.WHITE);
            commentTextArea.setBounds(10, 678, 900, 135);

            commentTextArea.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    if (commentTextArea.getText().equals("Write your comment here...(MAX 513 CHARACTERS)")) {
                        commentTextArea.setText("");
                        commentTextArea.setForeground(Color.BLACK);
                    }
                }

                public void focusLost(FocusEvent e) {
                    if (commentTextArea.getText().isEmpty()) {
                        commentTextArea.setText("Write your comment here...(MAX 513 CHARACTERS)");
                        commentTextArea.setForeground(Color.GRAY);
                    }
                }
            });

            panel.add(commentTextArea);

            JButton submitReviewButton = new JButton("Submit Review");
            submitReviewButton.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
            submitReviewButton.setBounds(920, 783, 180, 30);
            submitReviewButton.setBackground(Color.BLUE);
            submitReviewButton.setForeground(new Color(192, 192, 192));
            panel.add(submitReviewButton);

            starRating = new StarRating();
            starRating.setBounds(920, 700, 178, 25);
            panel.add(starRating, BorderLayout.SOUTH);
            starRating.setBackground(Color.BLACK);

           
            JLabel removappVoteLabel = new JLabel("Removapp Rating");
            removappVoteLabel.setHorizontalAlignment(SwingConstants.CENTER);
            removappVoteLabel.setFont(new Font("Yu Gothic UI", Font.BOLD | Font.ITALIC, 20));
            removappVoteLabel.setForeground(new Color(224, 188, 25));
            removappVoteLabel.setBounds(10, 584, 190, 41);
            panel.add(removappVoteLabel);

            submitReviewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int total = 0;
                    String comment;
                    if (commentTextArea.getText().equals("Write your comment here...(MAX 513 CHARACTERS)"))
                        comment = "";
                    else
                        comment = commentTextArea.getText();
                    int starRatingValue = starRating.getFilledStars();

                    try (Connection con = connect.connectdb();
                         Statement st = con.createStatement()) {
                        ResultSet rs = null;
                        rs = st.executeQuery("SELECT SUM(yildiz) AS total  FROM yorumlar  WHERE username = '" + user.getUsername() + "'  AND film_adi = '" + film.getFilm_adi() + "'");
                        if (rs.next())
                            total = rs.getInt("total");
                        if (total == 0) {
                            int result = 0;
                            if (starRatingValue != 0) {
                                result = st.executeUpdate("INSERT INTO yorumlar (username, yildiz, yorum, film_adi) "
                                        + "VALUES ('" + user.getUsername() + "', '" + starRatingValue + "', '" + comment
                                        + "', '" + film.getFilm_adi() + "')");
                            } else
                                JOptionPane.showMessageDialog(MovieInterface.this, "You did not star rating. Please rate.",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            if (result > 0) {
                                JOptionPane.showMessageDialog(MovieInterface.this, "Comment sent successfully.", "Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                                connect.disconnect();
                                new MovieInterface(film, user).setVisible(true);
                                (MovieInterface.this).dispose();
                            } else {
                                JOptionPane.showMessageDialog(MovieInterface.this,
                                        "Failed to send comment. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(MovieInterface.this, "You can make only one review.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        rs.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            int labelHeight = 92;
            int verticalGap = 10;
            int previousHeight = 813;
            int totalStars = 0;
            int totalRecords = 0;
            Connection con = connect.connectdb();
            Statement st = null;
            ResultSet rs = null;

            try {
  
                st = con.createStatement();

                rs = st.executeQuery(
                        "SELECT SUM(yildiz) AS totalStars FROM yorumlar WHERE film_adi = '" + film.getFilm_adi() + "'");
                if (rs.next()) {
                    totalStars = rs.getInt("totalStars");
                }
                rs = st.executeQuery(
                        "SELECT COUNT(*) AS totalRecords FROM yorumlar WHERE film_adi = '" + film.getFilm_adi() + "'");
                if (rs.next())
                    totalRecords = rs.getInt("totalRecords");
                if (totalRecords > 0) {
                    average = (float) totalStars / totalRecords;
                    String averageString = String.format("%.3f", average).replace(',', '.');
                    JLabel averageLabel = new JLabel(averageString);
                    averageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    averageLabel.setFont(new Font("Yu Gothic UI", Font.BOLD | Font.ITALIC, 20));
                    averageLabel.setForeground(Color.WHITE);
                    averageLabel.setBounds(37, 625, 136, 38);
                    panel.add(averageLabel);
                    previousHeight = 885;
                    JLabel usernameLabel = new JLabel("Username");
                    usernameLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 23));
                    usernameLabel.setForeground(new Color(204, 169, 12));
                    usernameLabel.setBounds(10, 808, 178, 37);
                    panel.add(usernameLabel);

                    JLabel voteLabel = new JLabel("Rating");
                    voteLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 23));
                    voteLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    voteLabel.setForeground(new Color(204, 169, 12));
                    voteLabel.setBounds(223, 808, 178, 37);
                    panel.add(voteLabel);

                    JLabel commentLabel = new JLabel("Comment");
                    commentLabel.setFont(new Font("Yu Gothic UI", Font.BOLD, 23));
                    commentLabel.setForeground(new Color(204, 169, 12));
                    commentLabel.setBounds(435, 808, 178, 37);
                    panel.add(commentLabel);
                    
                    rs = st.executeQuery("SELECT * FROM yorumlar WHERE film_adi = '" + film.getFilm_adi() + 
                    		"' ORDER BY CASE WHEN yorum = '' THEN 1 ELSE 0 END, yildiz DESC, username ASC;");
                    int i = 0;
                    while (rs.next()) {
                    	
                        String commentText = rs.getString("yorum");
                        String username = rs.getString("username");
                        int starRating = rs.getInt("yildiz");
                        JTextArea usernameTextArea = new JTextArea(username);
                        usernameTextArea.setFont(new Font("Yu Gothic UI", Font.BOLD, 20));
                        usernameTextArea.setBackground(Color.BLACK);
                        usernameTextArea.setForeground(new Color(192, 192, 192));
                        usernameTextArea.setEditable(false);
                        usernameTextArea.setFocusable(false);
                        usernameTextArea.setBounds(10, previousHeight + (i * (labelHeight + verticalGap)), 178, labelHeight);
                        usernameTextArea.setLineWrap(true);
                        usernameTextArea.setWrapStyleWord(true);
                        panel.add(usernameTextArea);

                        starRatingComponent = new StarRating2();
                        starRatingComponent.setfilledStars(starRating);
                        starRatingComponent.setBounds(223, previousHeight + (i * (labelHeight + verticalGap)), 178, 25);
                        starRatingComponent.setBackground(Color.BLACK);
                        panel.add(starRatingComponent);

                        if (user.getUsername().equals(username)) {
                            JTextArea commentTextArea2 = new JTextArea(commentText);
                            commentTextArea2.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
                            commentTextArea2.setBackground(Color.BLACK);
                            commentTextArea2.setForeground(Color.WHITE);
                            commentTextArea2.setEditable(false);
                            commentTextArea2.setFocusable(false);
                            commentTextArea2.setBounds(435, previousHeight + (i * (labelHeight + verticalGap)), 577, labelHeight+3);
                            commentTextArea2.setLineWrap(true);
                            commentTextArea2.setWrapStyleWord(true);
                            panel.add(commentTextArea2);

                            JButton deleteButton = new JButton("Delete");
                            deleteButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    String sql = "DELETE FROM yorumlar WHERE username = ? AND film_adi = ?";
                                    try (PreparedStatement statement = con.prepareStatement(sql)) {
                                        statement.setString(1, user.getUsername());
                                        statement.setString(2, film.getFilm_adi());
                                        int rowsAffected = statement.executeUpdate();
                                        if (rowsAffected > 0) {
                                            JOptionPane.showMessageDialog(MovieInterface.this, "Review deleted successfully.", "Success",
                                                    JOptionPane.INFORMATION_MESSAGE);
                                            connect.disconnect();
                                            new MovieInterface(film, user).setVisible(true);
                                            dispose();
                                        } else {
                                            JOptionPane.showMessageDialog(MovieInterface.this,
                                                    "Failed to delete comment. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                            deleteButton.setBounds(1042, previousHeight + 40 + (i * (labelHeight + verticalGap)), 70, 25);
                            deleteButton.setFont(new Font("Yu Gothic UI", Font.BOLD, 11));
                            deleteButton.setBackground(Color.RED);
                            panel.add(deleteButton);
                        } else {
                            JTextArea commentTextArea2 = new JTextArea(commentText);
                            commentTextArea2.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
                            commentTextArea2.setBackground(Color.BLACK);
                            commentTextArea2.setForeground(Color.WHITE);
                            commentTextArea2.setEditable(false);
                            commentTextArea2.setFocusable(false);
                            commentTextArea2.setBounds(435, previousHeight + (i * (labelHeight + verticalGap)), 657, labelHeight);
                            commentTextArea2.setLineWrap(true);
                            commentTextArea2.setWrapStyleWord(true);
                            panel.add(commentTextArea2);
                           
                        }
                        i++;
                    }
                }             
                int contentHeight = (previousHeight + totalRecords * (labelHeight + verticalGap));
                panel.setPreferredSize(new Dimension(1142, contentHeight));
                
                JButton btnNewButton = new JButton("GO TRAILER");
                btnNewButton.setForeground(new Color(0, 0, 0));
                btnNewButton.setBackground(Color.YELLOW);
                btnNewButton.setFont(new Font("Yu Gothic UI", Font.BOLD, 29));
                btnNewButton.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		Connection con = connect.connectdb();
                		String videoAdresi;
                        String query = "SELECT fragmanlar FROM filmler WHERE film_adi = ?";
                        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                            preparedStatement.setString(1, film.getFilm_adi());
                            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                                if (resultSet.next()) {
                                    videoAdresi = resultSet.getString("fragmanlar");
                                	 if (!videoAdresi.isEmpty()) {
                                    URI uri = new URI(videoAdresi);
                                    Desktop.getDesktop().browse(uri);
                                	 }
                                	 else 
                                     	JOptionPane.showMessageDialog(MovieInterface.this, "There is no trailer video.",
                                                 "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (IOException e1) {
								e1.printStackTrace();
							} catch (URISyntaxException e1) {
								e1.printStackTrace();
							}
                            connect.disconnect();
                        } catch (SQLException e1) {
							e1.printStackTrace();
						}
                	}
                });
                btnNewButton.setBounds(10, 321, 200, 41);
                panel.add(btnNewButton);
                scrollPane.setPreferredSize(new Dimension(1142, contentHeight));
            } catch (Exception ex) {
                ex.printStackTrace();
            } 
            

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        JButton backButton = new JButton("Back Page");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	connect.disconnect();
                ListInterface userInt = new ListInterface(user);
                userInt.setVisible(true);
                dispose();
            }
        });
        backButton.setBounds(999, 11, 101, 31);
        backButton.setBackground(Color.RED);
        backButton.setFont(new Font("Yu Gothic UI", Font.BOLD, 14));
        panel.add(backButton);
        
        setVisible(true);
    }
}