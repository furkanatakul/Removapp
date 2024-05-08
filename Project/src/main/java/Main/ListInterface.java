package Main;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

import DataBase.*;
import Helpers.*;

public class ListInterface extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DBConnect connect = new DBConnect();
    private JTable table;
    private User u;
    private JLabel lblNewLabel_1;
    private JButton btnNewButton_1;
    private JTextField searchField;
    private JLabel lblNewLabel_2;
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    User u = new User();
                    ListInterface frame = new ListInterface(u);                   
                    frame.setVisible(true);
                    frame.setBackground(Color.BLACK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ListInterface(User u) {  	
        this.u = u;
        setResizable(false);
        setTitle("Removapp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1156, 689);
        contentPane = new JPanel();
        contentPane.setBackground(Color.BLACK);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        setContentPane(contentPane);
        
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Welcome, " + u.getName_surname());
        lblNewLabel.setFont(new Font("Yu Gothic UI", Font.BOLD | Font.ITALIC, 27));
        lblNewLabel.setForeground(new Color(224, 188, 25));
        lblNewLabel.setBounds(10, 10, 438, 48);
        contentPane.add(lblNewLabel);
        
        searchField = new JTextField();
        searchField.setFont(new Font("Yu Gothic UI", Font.PLAIN, 18));
        searchField.setBounds(685, 17, 241, 43);
        contentPane.add(searchField);
        searchField.setColumns(10);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 124, 1122, 518);
        scrollPane.setBackground(Color.BLACK);
        contentPane.add(scrollPane);
        


        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : columnIndex == 3 ? JButton.class : Object.class;
            }
        };
        model.addColumn("Movie Image");
        model.addColumn("Movie Name");
        model.addColumn("Movie Genre");
        model.addColumn("Select");
        try {
            Connection con = connect.connectdb();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT film_adi, film_gorselleri, tur FROM filmler ORDER BY film_adi ASC");
            
            Color baseColor = new Color(179, 148, 9);
            Color gradientColor1 = new Color(204, 169, 12);
            Color gradientColor2 = baseColor.brighter();
            
            while (rs.next()) {
                String filmName = rs.getString("film_adi");
                String imageUrl = rs.getString("film_gorselleri");
                String filmType = rs.getString("tur");
                ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
                imageIcon.setImage(imageIcon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH));
                
                JButton goButton = new JButton("Go to Movie Details");
                goButton.setFont(new Font("Yu Gothic UI", Font.BOLD, 25));
                goButton.setForeground(Color.BLACK);
                goButton.setBorderPainted(true);
                goButton.setFocusPainted(false);
                goButton.setContentAreaFilled(false);
                goButton.setOpaque(false);
                goButton.setPreferredSize(new Dimension(200, 50));
                
                goButton = new JButton("Go to Movie Details") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        GradientPaint gradient = new GradientPaint(0, 0, gradientColor1, 0, getHeight(), gradientColor2);
                        g2d.setPaint(gradient);
                        g2d.fillRect(0, 0, getWidth(), getHeight());
                        g2d.dispose();
                        super.paintComponent(g);
                    }
                };
                
                goButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int row = table.convertRowIndexToModel(table.getSelectedRow());
                        model.setValueAt(filmName, row, 1);
                        if (row != -1) {
                            try {
                                Connection con = connect.connectdb();
                                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM filmler WHERE film_adi = ?");
                                preparedStatement.setString(1, (String) table.getModel().getValueAt(row, 1));
                                ResultSet result = preparedStatement.executeQuery();
                                
                                if (result.next()) {
                                    Film f = new Film();
                                    f.setId(result.getInt("id"));
                                    f.setFilm_adi(result.getString("film_adi"));
                                    f.setAciklama(result.getString("aciklama"));
                                    f.setFilm_puani(result.getDouble("film_puani"));
                                    f.setFilm_suresi(result.getInt("film_suresi"));
                                    f.setFilm_gorselleri(result.getString("film_gorselleri"));
                                    f.setOyuncular(result.getString("oyuncular"));
                                    f.setTur(result.getString("tur"));
                                    f.setUlke(result.getString("ulke"));
                                    f.setVizyon_tarihi(result.getString("vizyon_tarihi"));
                                    f.setYonetmen(result.getString("yonetmen"));
                                    connect.disconnect();
                                    MovieInterface obj = new MovieInterface(f,u);
                                    obj.setVisible(true);
                                    dispose();
                                } else {
                                    JOptionPane.showMessageDialog(ListInterface.this, "Movie not found!", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                                result.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(ListInterface.this, "Error accessing database!", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(ListInterface.this, "Please select a movie!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                goButton.setBackground(new Color(0, 0, 0, 0));
                goButton.setOpaque(true);
                
                Object[] row = {imageIcon, "<html>" + filmName + "</html>" , "<html>" + filmType + "</html>", goButton};
                model.addRow(row);
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException | MalformedURLException e) {
            e.printStackTrace();
        }



        table = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? ImageIcon.class : column == 3 ? JButton.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column != 3)
                    return false;
                return true;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (column != 3) { 
                    component.setBackground(Color.BLACK);
                    component.setForeground(Color.WHITE);
                }
                return component;
            }
        };

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(new Color(204, 169, 12));
        table.setFont(new Font("Yu Gothic UI", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Yu Gothic UI", Font.BOLD, 18));

        table.setDefaultRenderer(JButton.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                return (Component) value;
            }
        });

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            if (column.getCellRenderer() instanceof DefaultTableCellRenderer) {
                DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) column.getCellRenderer();
                renderer.setHorizontalAlignment(JLabel.CENTER);
            }
        }

        table.setRowHeight(200);
        scrollPane.setViewportView(table);
        
        lblNewLabel_1 = new JLabel("The movie list is below. Please select the movie you want to see detailed information about or review.");
        lblNewLabel_1.setFont(new Font("Yu Gothic UI", Font.PLAIN, 22));
        lblNewLabel_1.setForeground(new Color(192, 192, 192));
        lblNewLabel_1.setBounds(10, 68, 1114, 48);
        contentPane.add(lblNewLabel_1);

        btnNewButton_1 = new JButton("LogOut");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connect.disconnect();
                LoginInterface obj = new LoginInterface();
                obj.setVisible(true);
                dispose();
            }
        });
        btnNewButton_1.setFont(new Font("Yu Gothic UI", Font.BOLD, 17));
        btnNewButton_1.setBounds(1024, 10, 100, 28);
        btnNewButton_1.setBackground(Color.RED);
        btnNewButton_1.setForeground(Color.BLACK);
        contentPane.add(btnNewButton_1);

        
        lblNewLabel_2 = new JLabel("Search: ");
        lblNewLabel_2.setFont(new Font("Yu Gothic UI", Font.BOLD, 23));
        lblNewLabel_2.setForeground(new Color(192, 192, 192));
        lblNewLabel_2.setBounds(601, 15, 84, 43);
        contentPane.add(lblNewLabel_2);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = table.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / table.getRowHeight();
                if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                    Object value = table.getValueAt(row, column);
                    if (value instanceof JButton) {
                        ((JButton) value).doClick();
                    }
                }
            }
        });
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(searchField.getText());
            }
        });
        
    }

    private void search(String text) {
        if (text.trim().length() == 0) {
            
            table.setRowSorter(null);
        } else {
            
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
            table.setRowSorter(sorter);
            
            RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + text, 1); 
            sorter.setRowFilter(rowFilter);
        }
    }
}