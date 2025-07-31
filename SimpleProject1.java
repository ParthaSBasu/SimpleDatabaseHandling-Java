import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

class DatabaseModel{
    public int roll;
    public String name;
    public String address;
    public int age;
    public DatabaseModel(int roll,String nm,String addr,int age){
        this.roll=roll;
        this.name=nm;
        this.address=addr;
        this.age=age;
    }
}

class CreateConnection{
    public Connection con;
        public Connection create(){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url="jdbc:mysql://127.0.0.1:3306/my_schema";
                String username="root";
                String password="root";
                con=DriverManager.getConnection(url, username, password);
            }catch(ClassNotFoundException | SQLException e){
                e.printStackTrace();
            }
            return con;
        }
        public void Close(){
            try{
                con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }    
        }
    }

public class SimpleProject1 {

    private ArrayList<DatabaseModel> studentList=new ArrayList<>();
    private int index=0;
    private CreateConnection dbCon=new CreateConnection();
    private Connection con=dbCon.create();
    private JFrame frame;
    private JTextField[] fields=new JTextField[4];
    private String[] buttons={"Insert","Update","Previous","show","Next","Delete","Clear"};
    private JPanel mainPanel;
    private JPanel fieldPanel;
    private JPanel buttonPanel;
    private JButton tableButton;
    private JLabel topLabel;

    class WelcomeWindow{
        public void start(){
            JWindow window=new JWindow();
            JLabel label=new JLabel();

            String text="<html><body><div style='text-align:center;'><h1>Simple Database Handling using JDBC</h1>"+
                    "<p><h1>Application Developed by Partha Sarathi Basu</h1></p></body></html>";
            label.setText(text);
            label.setFont(new Font("Times New Roman",Font.BOLD,24));
            label.setForeground(Color.BLUE);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setHorizontalAlignment(SwingConstants.CENTER);

            window.setSize(new Dimension(600,400));
            window.setLocationRelativeTo(null);
            window.getContentPane().add(label,BorderLayout.CENTER);
            window.setVisible(true);

            new javax.swing.SwingWorker<Void,Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    Thread.sleep(3000);
                    return null;
                }
                protected void done(){
                    window.setVisible(false);
                    window.dispose();
                    go();
                }
            }.execute();
        }
    }
    class PopulateList{
        public void populate(){
            try{
                Statement stm=con.createStatement();
                String query="select * from students";
                ResultSet rs=stm.executeQuery(query);
                while(rs.next()){
                    int roll=rs.getInt(1);
                    String name=rs.getString(2);
                    String addr=rs.getString(3);
                    int age=rs.getInt(4);
                    studentList.add(new DatabaseModel(roll, name, addr, age));    
                }
                stm.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    public void populateArrayList(){
        studentList.clear();
        new PopulateList().populate();
    }

    public void go(){

        frame=new JFrame();
        frame.setTitle("Simple Database Handling Demonstration using JDBC");
        frame.setSize(new Dimension(800,600));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event){
                int result=JOptionPane.showConfirmDialog(frame,"Do you really want to close ?","Closing Window",JOptionPane.OK_CANCEL_OPTION);
                if(result==JOptionPane.OK_OPTION){
                    closingConnection();
                    frame.dispose();
                } 
            }
        });
        
        mainPanel=new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        fieldPanel=new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel,BoxLayout.Y_AXIS));
        fieldPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder
            (Color.GREEN, 3),BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        fieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        for(int i=0;i<fields.length;i++){
            fields[i]=new JTextField();
            fields[i].setPreferredSize(new Dimension(500,40));
            fields[i].setMaximumSize(new Dimension(500,40));
            fields[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            fields[i].setAlignmentY(Component.CENTER_ALIGNMENT);
            fields[i].setHorizontalAlignment(SwingConstants.CENTER);
            fields[i].setFont(new Font("Times New Roman",Font.PLAIN,18));
            fields[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
            fields[i].setForeground(Color.WHITE);
            fields[i].setBackground(Color.DARK_GRAY);
            fields[i].setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            fields[i].setEditable(true);
            fields[i].setCursor(new Cursor(Cursor.TEXT_CURSOR));
            fields[i].setCaretColor(Color.WHITE);
            fieldPanel.add(Box.createRigidArea(new Dimension(600,10)));
            fieldPanel.add(fields[i]);
        }
        
        buttonPanel=new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        for(String btn:buttons){
            JButton button=new JButton(btn);
            button.setFont(new Font("Ariel",Font.BOLD,15));
            button.setPreferredSize(new Dimension(80,50));
            button.setForeground(Color.BLUE);
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            button.addActionListener(new ButtonActionHandler());
            buttonPanel.add(button);
        }

        tableButton=new JButton("Show Table");
        tableButton.setPreferredSize(new Dimension(120,50));
        tableButton.setFont(new Font("Times New Roman",Font.BOLD,20));
        tableButton.addActionListener(new TableGridGenerator());

        mainPanel.add(fieldPanel);
        mainPanel.add(buttonPanel);

        topLabel=new JLabel();
        String text="<html><div style='text-align:center;'><h2>Ballygunge Govt. High School</h2><h3>Class (IX): Student Details</h3></div></html>";
        topLabel.setText(text);
        topLabel.setFont(new Font("Times New Roman",Font.BOLD,20));
        topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topLabel.setForeground(Color.BLUE);

        frame.getContentPane().add(topLabel,BorderLayout.NORTH);
        frame.getContentPane().add(mainPanel,BorderLayout.CENTER);
        frame.getContentPane().add(tableButton,BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void closingConnection(){
        dbCon.Close();
    }

    public void showInitial(){
        populateArrayList();
        if(studentList.isEmpty()){
            JOptionPane.showMessageDialog(frame,"Table is empty !","Information Window",JOptionPane.INFORMATION_MESSAGE);
            for(JTextField field: fields){
                field.setText("No data found");
            }
        }
        else{
            DatabaseModel std=studentList.get(index);
            fields[0].setText(String.valueOf(std.roll));
            fields[1].setText(std.name);
            fields[2].setText(std.address);
            fields[3].setText(String.valueOf(std.age));
        }      
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new SimpleProject1().new WelcomeWindow().start());
    }

    class TableGridGenerator implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<ArrayList<String>> grid=new ArrayList<>();
            if(studentList.size() > 0){
                for(int i=0;i<studentList.size();i++){
                    ArrayList<String> row=new ArrayList<>();
                    DatabaseModel dm=studentList.get(i);
                    row.add(String.valueOf(dm.roll));
                    row.add(dm.name);
                    row.add(dm.address);
                    row.add(String.valueOf(dm.age));
                    grid.add(row);
                }
            }
            else if(grid.isEmpty()){
                JOptionPane.showMessageDialog(frame, "Table is empty. No data found!", "Information Window", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String[][] rowData=new String[grid.size()][grid.get(0).size()];
            String[] columnData=new String[4];
            columnData[0]="Roll Number";
            columnData[1]="Name";
            columnData[2]="Address";
            columnData[3]="Age";
            for(int row=0;row<grid.size();row++){
                for(int col=0;col<grid.get(row).size();col++){
                    rowData[row][col]=grid.get(row).get(col);
                }
            }

            JWindow tableWindow=new JWindow();
            tableWindow.setSize(new Dimension(700,700));
            tableWindow.setLocationRelativeTo(null);

            DefaultTableModel tableModel=new DefaultTableModel(rowData,columnData);
            JTable table=new JTable(tableModel);

            DefaultTableCellRenderer cellRenderer=new DefaultTableCellRenderer();
            cellRenderer.setHorizontalAlignment(JLabel.CENTER);
            for(int i=0;i<table.getColumnCount();i++){
                table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            }

            table.setFont(new Font("Times New Roman",Font.PLAIN,18));
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setAlignmentX(Component.CENTER_ALIGNMENT);
            table.setAlignmentY(Component.CENTER_ALIGNMENT);
            table.setSize(700,500);
            table.getTableHeader().setFont(new Font("Times New Roman",Font.BOLD,24));
            table.setRowHeight(40);
            table.setRowMargin(5);
            table.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE,5),BorderFactory.createEmptyBorder(20, 20,20,20)));

            JScrollPane scrollPane=new JScrollPane(table);

            tableWindow.getContentPane().add(scrollPane,BorderLayout.CENTER);
            tableWindow.setVisible(true);

            new javax.swing.SwingWorker<Void,Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                   Thread.sleep(5000);
                   return null;
                }

                protected void done(){
                    tableWindow.setVisible(false);
                    tableWindow.dispose();
                }    
            }.execute();
        }    
    }

    class ButtonActionHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            String input=event.getActionCommand();
            if(input.equals("Insert")){
                String query="insert into students(roll,name,address,age) values(?,?,?,?)";
                try {
                    PreparedStatement pstmt=con.prepareStatement(query);
                    pstmt.setInt(1,Integer.parseInt(fields[0].getText()));
                    pstmt.setString(2, fields[1].getText());
                    pstmt.setString(3, fields[2].getText());
                    pstmt.setInt(4,Integer.parseInt(fields[3].getText()));
                    int ack=pstmt.executeUpdate();
                    if(ack==1){
                        JOptionPane.showMessageDialog(frame,"Data inserted successfully","Information Window",JOptionPane.INFORMATION_MESSAGE);
                        fields[0].setText("");
                        fields[1].setText("");
                        fields[2].setText("");
                        fields[3].setText("");
                    }
                    else JOptionPane.showMessageDialog(frame,"Process failed!","Information Window)",JOptionPane.ERROR_MESSAGE);
                    pstmt.close();
                    populateArrayList();
                    showInitial();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(input.equals("Clear")){
                fields[0].setText("");
                fields[1].setText("");
                fields[2].setText("");
                fields[3].setText("");
            }
            if(input.equals("show")){
                showInitial();

            }
            if(input.equals("Update")){
                String query="update students set name=?,address=?,age=? where roll=? ";
                try{
                    PreparedStatement pstmt=con.prepareStatement(query);
                    pstmt.setString(1,fields[1].getText());
                    pstmt.setString(2,fields[2].getText());
                    pstmt.setInt(3,Integer.parseInt(fields[3].getText()));
                    pstmt.setInt(4,Integer.parseInt(fields[0].getText()));
                    int ack=pstmt.executeUpdate();
                    if(ack==1){
                        JOptionPane.showMessageDialog(frame,"Data updated successfully","Information Window",JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(frame,"Data Failed to get Updated","Information",JOptionPane.ERROR_MESSAGE);
                    }
                    pstmt.close();
                    populateArrayList();
                    showInitial();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(input.equals("Next")){
                if(studentList.isEmpty()){
                    return;
                }
                if(index == studentList.size()-1){
                    JOptionPane.showMessageDialog(frame,"You have reached the last record!");
                    showInitial();
                    return;
                }
                if(index < studentList.size()-1) index++;
                DatabaseModel dm=studentList.get(index);
                fields[0].setText(String.valueOf(dm.roll));
                fields[1].setText(dm.name);
                fields[2].setText(dm.address);
                fields[3].setText(String.valueOf(dm.age));
            }
            if(input.equals("Previous")){
                if(studentList.isEmpty()){
                    return;
                }
                if(index == 0){
                    JOptionPane.showMessageDialog(frame,"It's the first record.");
                    return;
                }
                if(index > 0) index--;
                DatabaseModel dm=studentList.get(index);
                fields[0].setText(String.valueOf(dm.roll));
                fields[1].setText(dm.name);
                fields[2].setText(dm.address);
                fields[3].setText(String.valueOf(dm.age));
            }
            if(input.equals("Delete")){
                String query="delete from students where roll=?";
                try{
                    PreparedStatement pstmt=con.prepareStatement(query);
                    pstmt.setInt(1,Integer.parseInt(fields[0].getText()));
                    int ack=pstmt.executeUpdate();
                    if(ack==1){
                        JOptionPane.showMessageDialog(frame,"Record deleted successfully","Information Window",JOptionPane.INFORMATION_MESSAGE);
                        fields[0].setText("");
                        fields[1].setText("");
                        fields[2].setText("");
                        fields[3].setText("");
                    }
                    else{
                        JOptionPane.showMessageDialog(frame,"Deletion failed","Information",JOptionPane.ERROR_MESSAGE);
                    }
                    pstmt.close();
                    populateArrayList();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
