/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gradingsystem;

import com.mysql.cj.conf.PropertyKey;
import java.awt.Button;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author jawar
 */
import javax.swing.table.DefaultTableModel;
public class DashBoardMenu extends javax.swing.JFrame {
    
    static final String DB_URL = "jdbc:mysql://localhost/sgs_database";
    static final String USER = "root";
    static final String PASS = ""; 

    static com.sun.jdi.connect.spi.Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Creates new form DashBoardMenu
     */
    
    Connection conn = null;
    ResultSet rs = null;
    Statement s = null;
    PreparedStatement ps = null;
    
    
    String _subject = " ";
    String _totalScore =  " ";
    
    public DashBoardMenu(boolean logIn) {
        initComponents();
        this.setEnabled(logIn);
        
        
        
        
        try {
                
                
               
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
                UIManager.put("control", new Color(128, 128, 128));
                UIManager.put("info", new Color(128, 128, 128));
                UIManager.put("nimbusBase", new Color(18, 30, 49));
                UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
                UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
                UIManager.put("nimbusFocus", new Color(115, 164, 209));
                UIManager.put("nimbusGreen", new Color(176, 179, 50));
                UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
                UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
                UIManager.put("nimbusOrange", new Color(191, 98, 4));
                UIManager.put("nimbusRed", new Color(169, 46, 34));
                UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
                UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
                UIManager.put("text", new Color(230, 230, 230));
                SwingUtilities.updateComponentTreeUI(this);
                
                disableUpdateBtn();
            } catch (UnsupportedLookAndFeelException exc) {
                System.err.println("Nimbus: Unsupported Look and feel!");
            }
    }
    
    private void disableUpdateBtn()
    {
        int count= t_listOfStudents.getModel().getRowCount(); 
        System.out.println(count);
        
        if(count==0)
        {
            b_updateStudents.setEnabled(false);
        }
        else{
            b_updateStudents.setEnabled(true);
        }
    }
    private void onClickBtn(JPanel panel)
    {
        panel.setBackground(new Color(51,51,51));
        //btn.setBorderPainted(false);
       
    }
    
    private void onClickBtnLeave(JPanel panel)
    {
        panel.setBackground(new Color(78,80,82));
        
      
    }
    
    private void clearTableListOfStudents()
    {
        DefaultTableModel model = (DefaultTableModel) t_listOfStudents.getModel();
        model.setRowCount(0);
    }
    
    public void updatingJTable()
    {
        clearTableListOfStudents();
        conn = DbConnection.getStdConnection();
         try {
            String query = "SELECT * FROM tb_addingdata";
            
            
            s = conn.createStatement();
            rs = s.executeQuery(query);
            
            while(rs.next())
            {
//                String _id = String.valueOf(rs.getInt("id"));
                String std_id = rs.getString("std_id");
                String std_name = rs.getString("std_name");
                String std_level = rs.getString("std_level");
                String std_curriculum = rs.getString("std_curriculum");
                String std_remarks = rs.getString("std_remarks");
                
                
                String tbData[] ={std_id, std_name, std_level, std_curriculum, std_remarks};
                DefaultTableModel tbModel = (DefaultTableModel) t_listOfStudents.getModel();
                
                tbModel.addRow(tbData);
                
            }
            
            
        } catch (Exception e) {
            
             JOptionPane.showMessageDialog(null, "Error! Please contact developer. This is the error: " + e.getMessage());
        }
    }
    
    
    private void updatingDataToInsertToTable()
    {
         DefaultTableModel tbModel = (DefaultTableModel) t_listOfStudents.getModel();
        int rows = tbModel.getRowCount();
        
        try (Connection conns =DriverManager.getConnection(DB_URL,USER, PASS); Statement st = conns.createStatement();){
            
            for(int r = 0; r<rows;r++)
            {
                
                String std_id = tbModel.getValueAt(r, 0).toString();

                String std_name = tbModel.getValueAt(r, 1).toString();
                String std_level = tbModel.getValueAt(r, 2).toString();
                String std_curriculum = tbModel.getValueAt(r, 3).toString();
                
                System.out.println(std_name);

                String sqlString = "UPDATE tb_addingdata SET std_name ='" + std_name +
                        "', std_level='" + std_level + "', std_curriculum='" +std_curriculum+"' WHERE std_id='" + std_id +"'";
                
                String query = "SELECT std_name, std_level, std_curriculum FROM tb_addingdata";
               
                st.executeUpdate(sqlString);
                rs = st.executeQuery(query);
                
                


//                    ps = conn.prepareStatement(query);
//                    ps.executeUpdate();
                   System.out.println(sqlString);

            }
           
            JOptionPane.showMessageDialog(null,"Updated Successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Unable to update. Here is the error code: " + e.getMessage());
        }
    }
    
    
    private void updateTableForGrade(String _idStd, String _nameStd)
    {
        DefaultTableModel model = (DefaultTableModel) t_listofSubject.getModel();
        model.setRowCount(0);
        
         try {
             Connection conn = DbConnection.selectingName();
             
                        String query = "SELECT * FROM tb_" +_idStd;
                        
                        s = conn.createStatement();
                        rs = s.executeQuery(query);

                        while(rs.next())
                        {
                            String sem = rs.getString("sem");
                            String filipino = rs.getString("filipino");
                            String english = rs.getString("english");
                            String math = rs.getString("math");
                            String science = rs.getString("science");
                            String pe = rs.getString("pe");
                            String programming = rs.getString("programming");
                            
                            
                            String tbData[] ={sem, filipino, english, math, science,pe, programming};
                            DefaultTableModel tbModel = (DefaultTableModel) t_listofSubject.getModel();
                
                            tbModel.addRow(tbData);
                        }
                    } catch (Exception e) {
                    }
            
            
    }
    
    
    
    public void updatingRecords()
    {
                DefaultTableModel model = (DefaultTableModel) t_recordList.getModel();
                model.setRowCount(0);
                conn = DbConnection.getStdConnection();
        
         try {
            String query = "SELECT * FROM tb_addingdata";
            
            s = conn.createStatement();
            rs = s.executeQuery(query);
            
            while(rs.next())
            {
//                String _id = String.valueOf(rs.getInt("id"));
                String std_id = rs.getString("std_id");
                String std_name = rs.getString("std_name");
                String std_remarks = rs.getString("std_remarks");
               
                
                
                String tbData[] ={std_id, std_name, std_remarks};
                DefaultTableModel tbModel = (DefaultTableModel) t_recordList.getModel();
                
                tbModel.addRow(tbData);
                
            }
            
            
        } catch (Exception e) {
            
             JOptionPane.showMessageDialog(null, "Error! Please contact developer. This is the error: " + e.getMessage());
        }
    }
    
    private void updatingPassingRecord(String _id)
    {
        DefaultTableModel model = (DefaultTableModel) t_listofSubject.getModel();
        model.setRowCount(0);
         List<Integer> listF = new ArrayList<>();
         List<Integer> listEng = new ArrayList<>();
         List<Integer> listMath = new ArrayList<>();

         List<Integer> listScie = new ArrayList<>();
         List<Integer> listPe= new ArrayList<>();
         List<Integer> listProg = new ArrayList<>();
    
        
        
        System.out.println(_id);
        
         try {
             Connection conn = DbConnection.selectingName();
             
                        String query = "SELECT * FROM tb_" +_id;
                        
                        s = conn.createStatement();
                        rs = s.executeQuery(query);
                        
                        
                        int a = rs.getRow();
                         System.out.println("HSAHFAHSF");
                        System.out.println(a);
                        
                        try {
                            int sumF=0, sumEng=0, sumM=0, sumSci =0, sumP=0, sumProg=0;
                            
                            int f=0, eng=0, m=0, sci=0, p=0, prog=0; 
                            String w = "PASS";
                                String b = "NOT PASS";

                        while(rs.next())
                        {
                            String filipino = rs.getString("filipino");
                            String english = rs.getString("english");
                            String math = rs.getString("math");
                            String science = rs.getString("science");
                            String pe = rs.getString("pe");
                            String programming = rs.getString("programming");
                            
                            
                            
                            
                            
                            
                            
                            
                                f = Integer.parseInt(filipino);
                                eng = Integer.parseInt(english);
                                m = Integer.parseInt(math);
                                sci = Integer.parseInt(science);
                                p = Integer.parseInt(pe);
                                prog = Integer.parseInt(programming);
                                System.out.println(f);
                                
                                
                                listF.add(f);
                                listEng.add(eng);
                                listMath.add(m);
                                
                                listScie.add(sci);
                                listPe.add(p);
                                listProg.add(prog);
                                
                                sumF = sumF+f;
                                sumEng = sumEng +eng;
                                
                                
                                sumM = sumM+m;
                                sumSci = sumSci +sci;
                                
                                sumP = sumP+ p;
                                sumProg = sumProg +prog;
                                
                                
                                
                               
                            
                            
                            
                            
//                            String tbData[] ={filipino, english, math, science,pe, programming};
//                            DefaultTableModel tbModel = (DefaultTableModel) t_listofSubject.getModel();
//                
//                            tbModel.addRow(tbData);
                        }
                        sumF = sumF/listF.size();
                        sumEng = sumEng/listEng.size();
                        sumM = sumM/listMath.size();
                        
                        sumSci = sumSci/listScie.size();
                        sumP = sumP/listPe.size();
                        sumProg = sumProg/listProg.size();
                        
                         if(sumF >=75 && sumF<=100)
                                {
                                    l_pFil.setText(w);
                                }
                                else{
                                    l_pFil.setText(b);
                                }
                                
                                if(sumEng >=75 && sumEng<=100)
                                {
                                    l_pEng.setText(w);
                                }
                                else{
                                    l_pEng.setText(b);
                                }
                                
                                
                                if(sumM >=75 && sumM<=100)
                                {
                                    l_pMath.setText(w);
                                }
                                else{
                                    l_pMath.setText(b);
                                }
                                
                                if(sumSci >=75 && sumSci<=100)
                                {
                                    l_pSci.setText(w);
                                }
                                else{
                                    l_pSci.setText(b);
                                }
                                
                                
                                if(sumP >=75 && sumP<=100)
                                {
                                    l_pPe.setText(w);
                                }
                                else{
                                    l_pPe.setText(b);
                                }
                                
                                if(sumProg >=75 && sumProg<=100)
                                {
                                    l_pProg.setText(w);
                                }
                                else{
                                    l_pProg.setText(b);
                                }
                                
                                
                                
                                l_filG.setText(String.valueOf(sumF));
                                l_engG.setText(String.valueOf(sumEng));
                                l_mathG.setText(String.valueOf(sumM));
                                l_sciG.setText(String.valueOf(sumSci));
                                l_peG.setText(String.valueOf(sumP));
                                l_progG.setText(String.valueOf(sumProg));
                                
                        
                        
                        } catch (Exception e) {
                                
                                System.out.print(e.getMessage());
                            }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
    }
    
    private void updatingRemarks(String std_remarks, String std_id)
    {
        DefaultTableModel tbModel = (DefaultTableModel) t_recordList.getModel();
        int rows = tbModel.getRowCount();
        
        try (Connection conns =DriverManager.getConnection(DB_URL,USER, PASS); Statement st = conns.createStatement();){
            
           

                String sqlString = "UPDATE tb_addingdata SET std_remarks ='" + std_remarks +"' WHERE std_id='" + std_id +"'";
                
                String query = "SELECT std_remarks FROM tb_addingdata";
               
                st.executeUpdate(sqlString);
                rs = st.executeQuery(query);
                
                


//                    ps = conn.prepareStatement(query);
//                    ps.executeUpdate();
                   System.out.println(sqlString);

            
           
            JOptionPane.showMessageDialog(null,"Updated Successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Unable to update. Here is the error code: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        p_dashboard = new javax.swing.JPanel();
        l_dashboard = new javax.swing.JLabel();
        p_masterlist = new javax.swing.JPanel();
        l_masterlist = new javax.swing.JLabel();
        p_records = new javax.swing.JPanel();
        l_records = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        b_exit = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        CenterDesktop = new javax.swing.JPanel();
        DashBoard = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        l_addStudents = new javax.swing.JLabel();
        i_addStudents = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        i_addStudents1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        MasterList = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        p_students = new javax.swing.JPanel();
        l_studentMenu = new javax.swing.JLabel();
        p_subject = new javax.swing.JPanel();
        l_subjectMenu = new javax.swing.JLabel();
        MasterDesktop = new javax.swing.JPanel();
        StudentsPanel = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_listOfStudents = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        b_deleteStudents = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        b_addStudents = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        b_updateStudents = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        SubjectsPanel = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        t_listofSubject = new javax.swing.JTable();
        l_idStd = new javax.swing.JLabel();
        l_updateGrade = new javax.swing.JButton();
        tf_search = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        p_addingPanelHide = new javax.swing.JPanel();
        tf_filipino = new javax.swing.JTextField();
        tf_english = new javax.swing.JTextField();
        tf_programming = new javax.swing.JTextField();
        tf_math = new javax.swing.JTextField();
        tf_science = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        c_sem = new javax.swing.JComboBox<>();
        b_addSubject1 = new javax.swing.JButton();
        tf_pe = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        p_addGrade = new javax.swing.JPanel();
        l_addingGrade = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        l_nameStd = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        AddGradesPanel = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        label_subj = new javax.swing.JLabel();
        label_id = new javax.swing.JLabel();
        label_name = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        tf_homework = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        tf_test = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        l_btnCompute = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        tf_total = new javax.swing.JTextField();
        Records = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        l_nameStdRecords = new javax.swing.JLabel();
        l_idStdRecord = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        t_recordList = new javax.swing.JTable();
        jPanel23 = new javax.swing.JPanel();
        l_progG = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        l_peG = new javax.swing.JLabel();
        l_sciG = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        l_mathG = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        l_engG = new javax.swing.JLabel();
        l_filG = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        l_pEng = new javax.swing.JLabel();
        l_pFil = new javax.swing.JLabel();
        l_pSci = new javax.swing.JLabel();
        l_pMath = new javax.swing.JLabel();
        l_pPe = new javax.swing.JLabel();
        l_pProg = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        l_updateRemarks = new javax.swing.JLabel();
        Reports = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(78, 80, 82));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/pngwing.com.png"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 247, 175));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("ADMINISTRATOR");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 250, -1));

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 250, 10));

        p_dashboard.setBackground(new java.awt.Color(78, 80, 82));
        p_dashboard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        l_dashboard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/dashboard.png"))); // NOI18N
        l_dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_dashboardMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout p_dashboardLayout = new javax.swing.GroupLayout(p_dashboard);
        p_dashboard.setLayout(p_dashboardLayout);
        p_dashboardLayout.setHorizontalGroup(
            p_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_dashboardLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(l_dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        p_dashboardLayout.setVerticalGroup(
            p_dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_dashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_dashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(p_dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 250, 120));

        p_masterlist.setBackground(new java.awt.Color(78, 80, 82));
        p_masterlist.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        p_masterlist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p_masterlistMouseClicked(evt);
            }
        });

        l_masterlist.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_masterlist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/students.png"))); // NOI18N
        l_masterlist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_masterlistMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout p_masterlistLayout = new javax.swing.GroupLayout(p_masterlist);
        p_masterlist.setLayout(p_masterlistLayout);
        p_masterlistLayout.setHorizontalGroup(
            p_masterlistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_masterlistLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(l_masterlist, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        p_masterlistLayout.setVerticalGroup(
            p_masterlistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_masterlistLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_masterlist, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(p_masterlist, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 250, 120));

        p_records.setBackground(new java.awt.Color(78, 80, 82));
        p_records.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        l_records.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_records.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/data.png"))); // NOI18N
        l_records.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_recordsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout p_recordsLayout = new javax.swing.GroupLayout(p_records);
        p_records.setLayout(p_recordsLayout);
        p_recordsLayout.setHorizontalGroup(
            p_recordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_recordsLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(l_records, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        p_recordsLayout.setVerticalGroup(
            p_recordsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_recordsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_records, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(p_records, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, 250, 120));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 250, 0));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 1090));

        jPanel3.setBackground(new java.awt.Color(78, 80, 82));

        b_exit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b_exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/x_.png"))); // NOI18N
        b_exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                b_exitMouseClicked(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/-.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("STUDENT GRADING SYSTEM");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(271, 271, 271)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 708, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(28, 28, 28)
                .addComponent(b_exit)
                .addGap(23, 23, 23))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(b_exit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1520, 60));

        CenterDesktop.setBackground(new java.awt.Color(51, 51, 51));
        CenterDesktop.setLayout(new java.awt.CardLayout());

        DashBoard.setBackground(new java.awt.Color(51, 51, 51));
        DashBoard.setPreferredSize(new java.awt.Dimension(1250, 1340));
        DashBoard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("WELCOME YOU YOUR DASHBOARD");
        DashBoard.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 711, 108));

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 920, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        DashBoard.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 920, 10));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("STUDENTS");
        DashBoard.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 340, 30));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        DashBoard.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 350, 10));

        l_addStudents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_addStudents.setText("ADD STUDENTS");

        i_addStudents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        i_addStudents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/person.png"))); // NOI18N
        i_addStudents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                i_addStudentsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(i_addStudents, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 9, Short.MAX_VALUE)
                        .addComponent(l_addStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(i_addStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_addStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        DashBoard.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 150, 160));

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("EDIT STUDENT RECORD");

        i_addStudents1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        i_addStudents1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/person.png"))); // NOI18N
        i_addStudents1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                i_addStudents1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(i_addStudents1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(i_addStudents1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        DashBoard.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 250, 150, 160));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Administrator");
        DashBoard.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, 340, 30));

        jPanel11.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        DashBoard.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 350, 10));

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/person.png"))); // NOI18N
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("ADD USER");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(50, 50, 50))
        );

        DashBoard.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 560, 150, 160));

        CenterDesktop.add(DashBoard, "card2");

        MasterList.setBackground(new java.awt.Color(51, 51, 51));
        MasterList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setText("Master List");
        MasterList.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 487, 55));

        jPanel12.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1270, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        MasterList.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1270, 5));

        p_students.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(78, 80, 82)));

        l_studentMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_studentMenu.setText("STUDENTS");
        l_studentMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_studentMenuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout p_studentsLayout = new javax.swing.GroupLayout(p_students);
        p_students.setLayout(p_studentsLayout);
        p_studentsLayout.setHorizontalGroup(
            p_studentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_studentsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_studentMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addContainerGap())
        );
        p_studentsLayout.setVerticalGroup(
            p_studentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_studentsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_studentMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MasterList.add(p_students, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 75, 120, 30));

        p_subject.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(78, 80, 82)));

        l_subjectMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_subjectMenu.setText("SUBJECTS");
        l_subjectMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_subjectMenuMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout p_subjectLayout = new javax.swing.GroupLayout(p_subject);
        p_subject.setLayout(p_subjectLayout);
        p_subjectLayout.setHorizontalGroup(
            p_subjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_subjectLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_subjectMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addContainerGap())
        );
        p_subjectLayout.setVerticalGroup(
            p_subjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_subjectLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_subjectMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        MasterList.add(p_subject, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 75, 120, 30));

        MasterDesktop.setLayout(new java.awt.CardLayout());

        StudentsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setText("LIST OF STUDENTS");
        StudentsPanel.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 341, 30));

        t_listOfStudents.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        t_listOfStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STUDENT ID", "NAME OF STUDENT", "LEVEL", "CURRICULUM", "REMARKS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_listOfStudents.setSelectionBackground(new java.awt.Color(153, 153, 153));
        t_listOfStudents.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        t_listOfStudents.setShowGrid(false);
        t_listOfStudents.getTableHeader().setReorderingAllowed(false);
        t_listOfStudents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_listOfStudentsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_listOfStudents);
        if (t_listOfStudents.getColumnModel().getColumnCount() > 0) {
            t_listOfStudents.getColumnModel().getColumn(0).setResizable(false);
            t_listOfStudents.getColumnModel().getColumn(1).setResizable(false);
            t_listOfStudents.getColumnModel().getColumn(2).setResizable(false);
            t_listOfStudents.getColumnModel().getColumn(3).setResizable(false);
            t_listOfStudents.getColumnModel().getColumn(4).setResizable(false);
        }

        StudentsPanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 1218, 300));

        jPanel17.setBackground(new java.awt.Color(179, 0, 12));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        b_deleteStudents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b_deleteStudents.setText("DELETE");
        b_deleteStudents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                b_deleteStudentsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(b_deleteStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(b_deleteStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        StudentsPanel.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 380, 160, 50));

        jPanel18.setBackground(new java.awt.Color(51, 51, 51));
        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        b_addStudents.setBackground(new java.awt.Color(51, 51, 51));
        b_addStudents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b_addStudents.setText("ADD");
        b_addStudents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                b_addStudentsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(b_addStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(b_addStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        StudentsPanel.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 380, 160, 50));

        jPanel19.setBackground(new java.awt.Color(102, 204, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        b_updateStudents.setBackground(new java.awt.Color(102, 204, 255));
        b_updateStudents.setForeground(new java.awt.Color(51, 51, 51));
        b_updateStudents.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        b_updateStudents.setText("UPDATE");
        b_updateStudents.setEnabled(false);
        b_updateStudents.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                b_updateStudentsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(b_updateStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(b_updateStudents, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        StudentsPanel.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 380, -1, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 153, 153));
        jLabel13.setText("If you want to update the data. Click the desired data and type your new data, PRESS ENTER and click UPDATE button.");
        StudentsPanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 750, 30));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 153, 153));
        jLabel17.setText("Please take note that we can't update STUDENT ID AND REMARKS. We can add grades by double clicking STUDENT ID.");
        StudentsPanel.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 620, 780, -1));

        MasterDesktop.add(StudentsPanel, "card2");

        SubjectsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("GRADES OF STUDENTS IN EVERY SUBJECT");
        SubjectsPanel.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 300, -1));

        jPanel21.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1270, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        SubjectsPanel.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1270, 5));

        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        t_listofSubject.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SEM", "FILIPINO", "ENGLISH", "MATH", "SCIENCE", "PE", "PROGRAMMING"
            }
        ));
        jScrollPane4.setViewportView(t_listofSubject);

        jPanel22.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 75, 1220, 240));

        l_idStd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        l_idStd.setText("Student's ID");
        jPanel22.add(l_idStd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 26, 310, 30));

        l_updateGrade.setText("UPDATE");
        l_updateGrade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_updateGradeMouseClicked(evt);
            }
        });
        jPanel22.add(l_updateGrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 330, 140, 40));

        tf_search.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tf_search.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jPanel22.add(tf_search, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 30, 230, 30));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("CHANGE STUDENT");
        jPanel22.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 30, 170, -1));

        p_addingPanelHide.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tf_filipino.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf_filipino.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tf_filipinoMouseClicked(evt);
            }
        });
        p_addingPanelHide.add(tf_filipino, new org.netbeans.lib.awtextra.AbsoluteConstraints(396, 13, 101, 32));

        tf_english.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf_english.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tf_englishMouseClicked(evt);
            }
        });
        p_addingPanelHide.add(tf_english, new org.netbeans.lib.awtextra.AbsoluteConstraints(396, 58, 101, 32));

        tf_programming.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf_programming.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tf_programmingMouseClicked(evt);
            }
        });
        p_addingPanelHide.add(tf_programming, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 13, 101, 32));

        tf_math.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf_math.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tf_mathMouseClicked(evt);
            }
        });
        p_addingPanelHide.add(tf_math, new org.netbeans.lib.awtextra.AbsoluteConstraints(638, 13, 101, 32));

        tf_science.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf_science.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tf_scienceMouseClicked(evt);
            }
        });
        p_addingPanelHide.add(tf_science, new org.netbeans.lib.awtextra.AbsoluteConstraints(638, 58, 101, 32));

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("ENGLISH");
        p_addingPanelHide.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(279, 58, 105, 32));

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("FILIPINO");
        p_addingPanelHide.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(279, 13, 105, 32));

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("PROGRAMMING");
        p_addingPanelHide.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 13, 105, 32));

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("SCIENCE");
        p_addingPanelHide.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(515, 57, 105, 32));

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("MATH");
        p_addingPanelHide.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(515, 13, 105, 32));

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("SEMISTER");
        p_addingPanelHide.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, 105, 32));

        c_sem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4" }));
        p_addingPanelHide.add(c_sem, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 18, 139, -1));

        b_addSubject1.setText("ADD");
        b_addSubject1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                b_addSubject1MouseClicked(evt);
            }
        });
        p_addingPanelHide.add(b_addSubject1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 100, 218, 32));

        tf_pe.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tf_pe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tf_peMouseClicked(evt);
            }
        });
        p_addingPanelHide.add(tf_pe, new org.netbeans.lib.awtextra.AbsoluteConstraints(897, 58, 101, 32));

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("PE");
        p_addingPanelHide.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 58, 105, 32));

        jPanel22.add(p_addingPanelHide, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 1220, 200));

        p_addGrade.setBackground(new java.awt.Color(255, 102, 102));
        p_addGrade.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        p_addGrade.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        l_addingGrade.setForeground(new java.awt.Color(51, 51, 51));
        l_addingGrade.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_addingGrade.setText("ADD");
        l_addingGrade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_addingGradeMouseClicked(evt);
            }
        });
        p_addGrade.add(l_addingGrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 40));

        jPanel22.add(p_addGrade, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 330, 130, 40));

        jButton2.setText("SEARCH");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jPanel22.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 30, -1, 30));

        l_nameStd.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        l_nameStd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        l_nameStd.setText("Name");
        jPanel22.add(l_nameStd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, -10, 220, 30));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 153, 153));
        jLabel19.setText("If you want to update the data. Click the desired data and type your new data, PRESS ENTER and click UPDATE button.");
        jPanel22.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 320, 790, -1));

        SubjectsPanel.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1250, 610));

        MasterDesktop.add(SubjectsPanel, "card3");

        AddGradesPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_subj.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        label_subj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_subj.setText("SUBJECT");
        jPanel25.add(label_subj, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 40, 498, 46));

        label_id.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_id.setText("ID");
        jPanel25.add(label_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 490, 30));

        label_name.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        label_name.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label_name.setText("STUDENT NAME");
        jPanel25.add(label_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 116, 500, 30));

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Home Work score not exceeding to 30%");
        jPanel25.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 230, 40));
        jPanel25.add(tf_homework, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 240, 170, 40));

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Test Score not exceeding 70%");
        jPanel25.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, 170, 40));
        jPanel25.add(tf_test, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 300, 170, 40));

        jPanel26.setBackground(new java.awt.Color(255, 102, 102));

        l_btnCompute.setForeground(new java.awt.Color(51, 51, 51));
        l_btnCompute.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_btnCompute.setText("COMPUTE");
        l_btnCompute.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_btnComputeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_btnCompute, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_btnCompute, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel25.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 430, 180, 50));

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/x_.png"))); // NOI18N
        jLabel35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel35MouseClicked(evt);
            }
        });
        jPanel25.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(447, 6, 50, 50));

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Total grade");
        jPanel25.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 360, 170, 40));

        tf_total.setEditable(false);
        jPanel25.add(tf_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 360, 170, 40));

        AddGradesPanel.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 510, 500));

        MasterDesktop.add(AddGradesPanel, "card5");

        MasterList.add(MasterDesktop, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 140, 1270, 720));

        CenterDesktop.add(MasterList, "card3");

        Records.setBackground(new java.awt.Color(51, 51, 51));
        Records.setPreferredSize(new java.awt.Dimension(1272, 1340));
        Records.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setText(" Records and Grades");
        Records.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 692, 55));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        Records.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 360, 10));

        l_nameStdRecords.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        l_nameStdRecords.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        l_nameStdRecords.setText("NAME OF THE STUDENT");
        Records.add(l_nameStdRecords, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 250, -1));

        l_idStdRecord.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        l_idStdRecord.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        l_idStdRecord.setText("STUDENT''S ID");
        Records.add(l_idStdRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 290, -1));

        t_recordList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STUDENT ID", "NAME", "REMARKS"
            }
        ));
        t_recordList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_recordListMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(t_recordList);

        Records.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 230, 830, 380));

        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        l_progG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        l_progG.setText("0");
        jPanel23.add(l_progG, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 330, 40, 40));

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel44.setText("PROGRAMMING");
        jPanel23.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 140, 40));

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel42.setText("PE");
        jPanel23.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 140, 40));

        l_peG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        l_peG.setText("0");
        jPanel23.add(l_peG, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 40, 40));

        l_sciG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        l_sciG.setText("0");
        jPanel23.add(l_sciG, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, 40, 40));

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel39.setText("SCIENCE");
        jPanel23.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 140, 40));

        l_mathG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        l_mathG.setText("0");
        jPanel23.add(l_mathG, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, 40, 40));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel38.setText("MATH");
        jPanel23.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 140, 40));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel32.setText("ENGLISH");
        jPanel23.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 140, 40));

        l_engG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        l_engG.setText("0");
        jPanel23.add(l_engG, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 40, 40));

        l_filG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        l_filG.setText("0");
        jPanel23.add(l_filG, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 40, 40));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setText("FILIPINO");
        jPanel23.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 140, 40));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setText("GRADES COMPUTATIONS");
        jPanel23.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 310, 50));

        l_pEng.setText("PASSED");
        jPanel23.add(l_pEng, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 60, 30));

        l_pFil.setText("PASSED");
        jPanel23.add(l_pFil, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, 60, 30));

        l_pSci.setText("PASSED");
        jPanel23.add(l_pSci, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 240, 60, 30));

        l_pMath.setText("PASSED");
        jPanel23.add(l_pMath, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, 60, 30));

        l_pPe.setText("PASSED");
        jPanel23.add(l_pPe, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 290, 60, 30));

        l_pProg.setText("PASSED");
        jPanel23.add(l_pProg, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 60, 30));

        Records.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 370, 380));

        jPanel24.setBackground(new java.awt.Color(255, 102, 102));

        l_updateRemarks.setBackground(new java.awt.Color(255, 102, 102));
        l_updateRemarks.setForeground(new java.awt.Color(51, 51, 51));
        l_updateRemarks.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_updateRemarks.setText("UPDATE REMARKS");
        l_updateRemarks.setToolTipText("");
        l_updateRemarks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                l_updateRemarksMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_updateRemarks, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_updateRemarks, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        Records.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 650, 180, 50));

        CenterDesktop.add(Records, "card4");

        Reports.setBackground(new java.awt.Color(51, 51, 51));
        Reports.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setText("Reports");
        Reports.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, 556, 65));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        Reports.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 77, 360, 10));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        Reports.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 1180, -1));

        CenterDesktop.add(Reports, "card5");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1272, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 790, Short.MAX_VALUE)
        );

        CenterDesktop.add(jPanel9, "card6");

        jPanel1.add(CenterDesktop, new org.netbeans.lib.awtextra.AbsoluteConstraints(251, 60, 1270, 790));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 0, 1520, 1400));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void b_exitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_exitMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_b_exitMouseClicked

    private void l_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_dashboardMouseClicked
        // TODO add your handling code here:
        
        onClickBtn(p_dashboard);
        onClickBtnLeave(p_masterlist);
        onClickBtnLeave(p_records);

        
        DashBoard.setVisible(true);
        MasterList.setVisible(false);
        Records.setVisible(false);
        Reports.setVisible(false);
     
        
        
        
    }//GEN-LAST:event_l_dashboardMouseClicked

    private void l_masterlistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_masterlistMouseClicked
        // TODO add your handling code here:
        
        
        onClickBtnLeave(p_dashboard);
        onClickBtn(p_masterlist);
        onClickBtnLeave(p_records);
     
        
        DashBoard.setVisible(false);
        MasterList.setVisible(true);
        Records.setVisible(false);
        Reports.setVisible(false);
        
        p_addGrade.setVisible(true);
        l_addingGrade.setText("ADD");
        
        DbConnection dc = new DbConnection();
        dc.creatingDb();
        dc.creatingTableForAdding();
        
        updatingJTable();
        
        disableUpdateBtn();
        
       
       
        
        
        
        
        
        
    }//GEN-LAST:event_l_masterlistMouseClicked

    private void l_studentMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_studentMenuMouseClicked
        // TODO add your handling code here:
        onClickBtn(p_students);
        onClickBtnLeave(p_subject);
       
        
        
        StudentsPanel.setVisible(true);
        SubjectsPanel.setVisible(false);
       
         AddGradesPanel.setVisible(false);
        
    }//GEN-LAST:event_l_studentMenuMouseClicked

    private void l_subjectMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_subjectMenuMouseClicked
        // TODO add your handling code here:
        onClickBtnLeave(p_students);
        onClickBtn(p_subject);
       
        
        
        StudentsPanel.setVisible(false);
        SubjectsPanel.setVisible(true);
       
        
        
        StudentsPanel.setVisible(false);
        SubjectsPanel.setVisible(true);
         AddGradesPanel.setVisible(false);
         
         p_addGrade.setVisible(true);
         p_addingPanelHide.setVisible(false);
         l_addingGrade.setText("ADD");
    }//GEN-LAST:event_l_subjectMenuMouseClicked

    private void i_addStudentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_i_addStudentsMouseClicked
        // TODO add your handling code here:
        AddingStudents as = new AddingStudents();
        as.setVisible(true);
        
        
       
    }//GEN-LAST:event_i_addStudentsMouseClicked

    private void b_deleteStudentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_deleteStudentsMouseClicked
        // TODO add your handling code here:
         int idnum = t_listOfStudents.getSelectedRow();
        String _id = t_listOfStudents.getValueAt(idnum, 0).toString();
        
        try {
           
            
            
            
            String query = "DELETE FROM tb_addingdata WHERE std_id ='"+_id+ "'";
            System.out.println(query);
            
            ps = conn.prepareStatement(query);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deleted");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        
         try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement stmt = conn.createStatement();
      ) {		      
         String sql = "DROP TABLE tb_" + _id;
         stmt.executeUpdate(sql);
         System.out.println("Table deleted in given database...");   	  
      } catch (SQLException e) {
         e.printStackTrace();
      } 
        
        
        
        
        updatingJTable();
       // updatingDataToInsertToTable();
        
       
    }//GEN-LAST:event_b_deleteStudentsMouseClicked

    private void i_addStudents1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_i_addStudents1MouseClicked
        // TODO add your handling code here:
        onClickBtnLeave(p_dashboard);
        onClickBtnLeave(p_masterlist);
        onClickBtn(p_records);
     

        DashBoard.setVisible(false);
        MasterList.setVisible(false);
        Records.setVisible(true);
        Reports.setVisible(false);
        
        DbConnection dc = new DbConnection();
        dc.creatingDb();
        dc.creatingTableForAdding();
        
        
        
        
        
         updatingRecords();
    }//GEN-LAST:event_i_addStudents1MouseClicked

    private void p_masterlistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_masterlistMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_p_masterlistMouseClicked

    private void b_addStudentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_addStudentsMouseClicked
        // TODO add your handling code here:
        
        
        
        if(b_addStudents.getText().equals("ADD"))
        {
             AddingStudents as = new AddingStudents();
             as.setVisible(true);
             b_addStudents.setText("REFRESH");
        }
        
        else{
             updatingJTable();
             b_addStudents.setText("ADD");
        }
        
         
         
         
         
         
       
    }//GEN-LAST:event_b_addStudentsMouseClicked

    private void b_updateStudentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_updateStudentsMouseClicked
        // TODO add your handling code here:
        
        if(!b_updateStudents.isEnabled())
        {
            JOptionPane.showMessageDialog(null, "Please addd student first!");
        }
        else{
            updatingDataToInsertToTable();
        }
        
        
       
    }//GEN-LAST:event_b_updateStudentsMouseClicked

    private void t_listOfStudentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_listOfStudentsMouseClicked
        // TODO add your handling code here:
        
        int a = t_listOfStudents.getSelectedRow();
        
        
       t_listOfStudents.addMouseListener(new MouseAdapter(){
        public void mousePressed(MouseEvent mouseEvent)
        {
            JTable jTable = (JTable) mouseEvent.getSource();
            Point point = mouseEvent.getPoint();
            int row = jTable.rowAtPoint(point);
            
            int numOfClicked = mouseEvent.getClickCount();
            
            if(numOfClicked ==2 && jTable.getSelectedRow()!= -1)
            {
                
                
                 String _idStd = t_listOfStudents.getValueAt(row, 0).toString();
                    String _nameStd = t_listOfStudents.getValueAt(row, 1).toString();
                    
                   
                    
                    
                    
                    l_idStd.setText(_idStd);
                    l_nameStd.setText(_nameStd);
                    p_addingPanelHide.setVisible(false);
                    StudentsPanel.setVisible(false);
                    SubjectsPanel.setVisible(true);
                    p_addGrade.setVisible(true);
                    l_addingGrade.setText("ADD");
                    
                    
                    System.out.println(numOfClicked);
                    
                     updateTableForGrade(_idStd, _nameStd);
                     
                     
//                Object[] options = { "YES", "N0" };
//                Object selectedValue = JOptionPane.showOptionDialog(null, "Are you sure you want to edit grade of " + t_listOfStudents.getValueAt( row, 1) + "?", "Warning",
//                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
//                null, options, options[0]); 
//
//                System.out.println(selectedValue);
//
//                if(selectedValue.equals(0))
//                {
//                   
//                    
//                    
//                   
//                    
//                }
                 numOfClicked=0;
            }else{
                numOfClicked=0;
            }
        }
    });
       
        
    }//GEN-LAST:event_t_listOfStudentsMouseClicked

    private void b_addSubject1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_addSubject1MouseClicked
        // TODO add your handling code here:\
        
                DefaultTableModel tmodel = (DefaultTableModel) t_listofSubject.getModel();
                String sem = (String) c_sem.getSelectedItem();
                String filipino = tf_filipino.getText();
                String english = tf_english.getText();
                String math = tf_math.getText();
                String science = tf_science.getText();
                String programming = tf_programming.getText();
                String pe = tf_pe.getText();
                
                
                int rowCount = tmodel.getRowCount();
                if(rowCount ==0)
                {
                    if(sem.equals("") || filipino.equals("") || english.equals("") ||
                        math.equals("") || science.equals("") || pe.equals("") || programming.equals(""))
                             
                            {
                                
                                JOptionPane.showMessageDialog(null, "Please fill up grades.");
                            }
                        else
                        {
                            String tbData[] ={sem, filipino, english, math, science,pe, programming};
                            DefaultTableModel tbModel = (DefaultTableModel) t_listofSubject.getModel();
                            tbModel.addRow(tbData);
                            l_addingGrade.setText("SAVE");
                            p_addingPanelHide.setVisible(false);
                            p_addGrade.setVisible(true);
                        }
                }
                
                
                else{
                    String sems="";
                    boolean isExist = true;
                        for(int numRow = 0; numRow < rowCount; numRow++)
                        {
                            sems = tmodel.getValueAt(numRow, 0).toString();

                            if(sems.equals(sem))
                            {
                                if(sem.equals("4"))
                                {
                                    JOptionPane.showMessageDialog(null, "Sem " +sem+ " exist. You can only edit your grade in this sem");
                                }
                                else{
                                    JOptionPane.showMessageDialog(null, "Sem " +sem+ " exist. You can only edit your grade in this sem or add another sem");
                                }
                                
                                break;
                            }
                            
                            else{
                                isExist = false;
                            }
                            
                            
                        
                        }
                        
                        if(isExist == false)
                        {
                             if(!sems.equals(sem)){

                                if(sem.equals("") || filipino.equals("") || english.equals("") ||
                                math.equals("") || science.equals("") || pe.equals("") || programming.equals(""))

                                    {

                                        JOptionPane.showMessageDialog(null, "Please fill up grades.");
                                    }
                                else
                                {
                                    String tbData[] ={sem, filipino, english, math, science,pe, programming};
                                    DefaultTableModel tbModel = (DefaultTableModel) t_listofSubject.getModel();
                                    tbModel.addRow(tbData);
                                    l_addingGrade.setText("SAVE");
                                    p_addingPanelHide.setVisible(false);
                                    p_addGrade.setVisible(true);
                                }
                                
                            }
                        }
                
                }
        
    }//GEN-LAST:event_b_addSubject1MouseClicked

    private void l_addingGradeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_addingGradeMouseClicked
        // TODO add your handling code here:
        
        String initText = l_addingGrade.getText();
        
        if(initText.equals("ADD"))
        {
            p_addingPanelHide.setVisible(true);
            p_addGrade.setVisible(false);
            
        }
        
        if(initText.equals("SAVE"))
        {
            //SAVING TO DATABASE
            
            
            
            if(l_idStd.equals("Student's ID"))
            {
                JOptionPane.showConfirmDialog(null, "Please select your student first");
            }
            else{
                //saving to db
                DefaultTableModel tbModel = (DefaultTableModel) t_listofSubject.getModel();
                int rows = tbModel.getRowCount();
                rows = rows-1;
                
                
                String _id = l_idStd.getText().toString().toLowerCase();
        
    
            

                        String _sem = tbModel.getValueAt(rows,0).toString();
                        String _filipio = tbModel.getValueAt(rows, 1).toString();

                        String _english = tbModel.getValueAt(rows, 2).toString();
                        String _math = tbModel.getValueAt(rows, 3).toString();
                        String _science = tbModel.getValueAt(rows, 4).toString();
                        String _pe = tbModel.getValueAt(rows, 5).toString();
                        String _programming = tbModel.getValueAt(rows, 6).toString();
                        
                        DbConnection dc = new DbConnection();
                        dc.insertingGradesOfStudents(_id,_sem, _filipio, _english, _math, _science,_pe, _programming);

                        
                    
                    JOptionPane.showMessageDialog(null,"Grades are inserted and updated to this id: " + _id);
                    
                    l_addingGrade.setText("ADD");


                }
           
            
            
        }
    }//GEN-LAST:event_l_addingGradeMouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
        
        String nameSearch =tf_search.getText();
        
        
        Connection conn = DbConnection.selectingName();
        
        try {
            String query = "SELECT std_id, std_name FROM tb_addingdata";
            
            
            s = conn.createStatement();
            rs = s.executeQuery(query);
            
            String std_name = "";
            String std_id = "";
            
            boolean isExist = true;
            
            
            while(rs.next())
            {
//                String _id = String.valueOf(rs.getInt("id"));
                 std_id = rs.getString("std_id");
                 std_name = rs.getString("std_name");
                 
                if(std_name.equals(nameSearch))
                {
                    l_idStd.setText(std_id);
                    l_nameStd.setText(std_name);
                    
                    updateTableForGrade(std_id, std_name);
                    isExist = true;
                    break;
                    
                }
                else{
                     isExist = false;
                }
            }
            
            System.out.println(isExist);
            if(isExist == false)
            {
                
                if(!std_name.equals(nameSearch))
                {
                    JOptionPane.showMessageDialog(null, "There is no " + nameSearch + " in the database");
                }
            }
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
    }//GEN-LAST:event_jButton2MouseClicked

    private void tf_filipinoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tf_filipinoMouseClicked
        // TODO add your handling code here:
        String _id = l_idStd.getText();
        String _name = l_nameStd.getText();

        label_subj.setText("Filipino");
        label_id.setText("Student ID: " + _id);
        label_name.setText("Name: " +_name);
        
        AddGradesPanel.setVisible(true);
        StudentsPanel.setVisible(false);
        SubjectsPanel.setVisible(false);
         
        tf_homework.setText("");
        tf_test.setText("");
        l_btnCompute.setText("COMPUTE");
       
       
       
      
        
        
    }//GEN-LAST:event_tf_filipinoMouseClicked

    private void l_btnComputeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_btnComputeMouseClicked
        // TODO add your handling code here:

        String btnText = l_btnCompute.getText();
        
//        label_subj.setText(_subj);
//        label_name.setText(_name);
//        label_id.setText(_id);
//        

        if(btnText.equals("COMPUTE"))
        {
            try {
                int _homework = Integer.parseInt(tf_homework.getText());
                int _test = Integer.parseInt(tf_test.getText());

                int totalScore = _homework+_test;
                String s_totalScore = String.valueOf(totalScore);

                tf_total.setText(s_totalScore);

                l_btnCompute.setText("SAVE");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            l_btnCompute.setText("COMPUTE");
            System.out.println(label_subj.getText());
            if(label_subj.getText().equals("Filipino"))
            {
                
                
            String _totalScore = tf_total.getText();
            String _subject = label_subj.getText();

            tf_filipino.setText(_totalScore);
            AddGradesPanel.setVisible(false);
            SubjectsPanel.setVisible(true);
            }
            
            if(label_subj.getText().equals("English"))
            {
                
            String _totalScore = tf_total.getText();
            String _subject = label_subj.getText();

            tf_english.setText(_totalScore);
            AddGradesPanel.setVisible(false);
            SubjectsPanel.setVisible(true);
            }
            
            if(label_subj.getText().equals("Math"))
            {
                
            String _totalScore = tf_total.getText();
            String _subject = label_subj.getText();

            tf_math.setText(_totalScore);
            AddGradesPanel.setVisible(false);
            SubjectsPanel.setVisible(true);
            }
            if(label_subj.getText().equals("Science"))
            {
                
            String _totalScore = tf_total.getText();
            String _subject = label_subj.getText();

            tf_science.setText(_totalScore);
            AddGradesPanel.setVisible(false);
            SubjectsPanel.setVisible(true);
            }
            if(label_subj.getText().equals("PE"))
            {
                
            String _totalScore = tf_total.getText();
            String _subject = label_subj.getText();

            tf_pe.setText(_totalScore);
            AddGradesPanel.setVisible(false);
            SubjectsPanel.setVisible(true);
            }
            if(label_subj.getText().equals("Programming"))
            {
                
            String _totalScore = tf_total.getText();
            String _subject = label_subj.getText();

            tf_programming.setText(_totalScore);
            AddGradesPanel.setVisible(false);
            SubjectsPanel.setVisible(true);
            }

        }

    }//GEN-LAST:event_l_btnComputeMouseClicked

    private void tf_englishMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tf_englishMouseClicked
        // TODO add your handling code here:
        String _id = l_idStd.getText();
        String _name = l_nameStd.getText();

        label_subj.setText("English");
        label_id.setText("Student ID: " + _id);
        label_name.setText("Name: " +_name);
        
        AddGradesPanel.setVisible(true);
        StudentsPanel.setVisible(false);
        SubjectsPanel.setVisible(false);
         
        tf_homework.setText("");
        tf_test.setText("");
        l_btnCompute.setText("COMPUTE");
       
    }//GEN-LAST:event_tf_englishMouseClicked

    private void tf_mathMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tf_mathMouseClicked
        // TODO add your handling code here:
        String _id = l_idStd.getText();
        String _name = l_nameStd.getText();

        label_subj.setText("Math");
        label_id.setText("Student ID: " + _id);
        label_name.setText("Name: " +_name);
        
        AddGradesPanel.setVisible(true);
        StudentsPanel.setVisible(false);
        SubjectsPanel.setVisible(false);
         
        tf_homework.setText("");
        tf_test.setText("");
        l_btnCompute.setText("COMPUTE");
       
    }//GEN-LAST:event_tf_mathMouseClicked

    private void tf_scienceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tf_scienceMouseClicked
        // TODO add your handling code here:
        String _id = l_idStd.getText();
        String _name = l_nameStd.getText();

        label_subj.setText("Science");
        label_id.setText("Student ID: " + _id);
        label_name.setText("Name: " +_name);
        
        AddGradesPanel.setVisible(true);
        StudentsPanel.setVisible(false);
        SubjectsPanel.setVisible(false);
         
        tf_homework.setText("");
        tf_test.setText("");
        l_btnCompute.setText("COMPUTE");
       
    }//GEN-LAST:event_tf_scienceMouseClicked

    private void tf_programmingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tf_programmingMouseClicked
        // TODO add your handling code here:
        String _id = l_idStd.getText();
        String _name = l_nameStd.getText();

        label_subj.setText("Programming");
        label_id.setText("Student ID: " + _id);
        label_name.setText("Name: " +_name);
        
        AddGradesPanel.setVisible(true);
        StudentsPanel.setVisible(false);
        SubjectsPanel.setVisible(false);
         
        tf_homework.setText("");
        tf_test.setText("");
        l_btnCompute.setText("COMPUTE");
      
    }//GEN-LAST:event_tf_programmingMouseClicked

    private void tf_peMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tf_peMouseClicked
        // TODO add your handling code here:
        String _id = l_idStd.getText();
        String _name = l_nameStd.getText();

        label_subj.setText("PE");
        label_id.setText("Student ID: " + _id);
        label_name.setText("Name: " +_name);
        
        AddGradesPanel.setVisible(true);
        StudentsPanel.setVisible(false);
        SubjectsPanel.setVisible(false);
         
        tf_homework.setText("");
        tf_test.setText("");
        l_btnCompute.setText("COMPUTE");
       
    }//GEN-LAST:event_tf_peMouseClicked

    private void l_recordsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_recordsMouseClicked
        // TODO add your handling code here:
        onClickBtnLeave(p_dashboard);
        onClickBtnLeave(p_masterlist);
        onClickBtn(p_records);
   

        DashBoard.setVisible(false);
        MasterList.setVisible(false);
        Records.setVisible(true);
        Reports.setVisible(false);
        
        DbConnection dc = new DbConnection();
        dc.creatingDb();
        dc.creatingTableForAdding();
        
        
        
        
        
         updatingRecords();
        
        

    }//GEN-LAST:event_l_recordsMouseClicked

    private void l_updateGradeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_updateGradeMouseClicked
        // TODO add your handling code here:
        
        
         DefaultTableModel tbModel = (DefaultTableModel) t_listofSubject.getModel();
         int rows = tbModel.getRowCount();
         
         if(rows <=0)
         {
             JOptionPane.showMessageDialog(null, "Please select the name of student first");
         }
         else{
             int numRox = t_listofSubject.getSelectedRow();
            System.out.println(rows);
            System.out.println(numRox);

            String _tbName = l_idStd.getText();

            String _id = String.valueOf(tbModel.getValueAt(numRox, 0).toString());

                   String _filipino = String.valueOf(tbModel.getValueAt(numRox, 1).toString());
                   String _english = String.valueOf(tbModel.getValueAt(numRox, 2).toString());
                   String _math = String.valueOf(tbModel.getValueAt(numRox, 3).toString());
                   String _science = String.valueOf(tbModel.getValueAt(numRox, 4).toString());
                   String _pe = String.valueOf(tbModel.getValueAt(numRox, 5).toString());
                   String _programming = String.valueOf(tbModel.getValueAt(numRox, 6).toString());


                    String sqlString = "UPDATE tb_"+ _tbName+ " SET filipino ='" + _filipino +
                           "', english='" + _english + "', math='" +_math+"', science ='" + _science +
                           "', pe='" + _pe+ "', programming='" +_programming+"' WHERE id='" + _id +"'";

                     System.out.println(sqlString);

           try (Connection conns =DriverManager.getConnection(DB_URL,USER, PASS); Statement st = conns.createStatement();)
           {
                   String query = "SELECT filipino, english, math, science, pe, programming FROM tb_" + _tbName;

                   st.executeUpdate(sqlString);
                   rs = st.executeQuery(query);     
                   JOptionPane.showMessageDialog(null,"Updated Successfully");
           } catch (Exception e) 
           {
               JOptionPane.showMessageDialog(null,"Unable to update.");
               System.out.println(e.getMessage());
           }
         }
    }//GEN-LAST:event_l_updateGradeMouseClicked

    private void jLabel35MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel35MouseClicked
        // TODO add your handling code here:
        
        tf_homework.setText("");
        tf_test.setText("");
        l_btnCompute.setText("COMPUTE");
    }//GEN-LAST:event_jLabel35MouseClicked

    private void t_recordListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_recordListMouseClicked
        // TODO add your handling code here:
        int a = t_recordList.getSelectedRow();
        
       
       t_recordList.addMouseListener(new MouseAdapter(){
        public void mousePressed(MouseEvent mouseEvent)
        {
            JTable jTable = (JTable) mouseEvent.getSource();
            Point point = mouseEvent.getPoint();
            int row = jTable.rowAtPoint(point);
            
            int numOfClicked = 0;
            numOfClicked = mouseEvent.getClickCount();
            numOfClicked += numOfClicked;
            
            if(numOfClicked ==2 && jTable.getSelectedRow()!= -1)
            {
                Object[] options = { "YES", "N0" };
                Object selectedValue = JOptionPane.showOptionDialog(null, "You are going to analyze grades of " + t_recordList.getValueAt( row, 1) + "?", "Warning",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]); 

                System.out.println(selectedValue);

                if(selectedValue.equals(0)){
                    String _id = t_recordList.getValueAt(row, 0).toString();
                    String _name = t_recordList.getValueAt(row, 1).toString();
                    
                    
                    l_nameStdRecords.setText(_name);
                    l_idStdRecord.setText(_id);
                    
                    System.out.println(_id + String.valueOf(numOfClicked));
                    updatingPassingRecord(_id);
                    numOfClicked=0;
                }
                else
                {
                     numOfClicked=0;
                }
                 numOfClicked=0;
            }
//            
        }
    });
        
        
    }//GEN-LAST:event_t_recordListMouseClicked

    private void l_updateRemarksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_l_updateRemarksMouseClicked
        // TODO add your handling code here:
        String p = "PASS";
        
      
                if(l_idStdRecord.getText().equals("STUDENT''S ID"))
                {
                    JOptionPane.showMessageDialog(null, "Please select student first before proceeding");
                }
                else{
                        if(l_pFil.getText().equals(p) && l_pEng.getText().equals(p) && l_pMath.getText().equals(p) &&
                            l_pSci.getText().equals(p) && l_pPe.getText().equals(p) && l_pProg.getText().equals(p))
                    {
                        updatingRemarks(p, l_idStdRecord.getText().toString());
                    }
                else{
                        updatingRemarks("NOT PASS", l_idStdRecord.getText().toString());

                }
                }
                    updatingRecords();
                    
       
        
        
        
        
        
    }//GEN-LAST:event_l_updateRemarksMouseClicked

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        // TODO add your handling code here:
        UserAdminEditor ua = new UserAdminEditor();
        ua.setVisible(true);
    }//GEN-LAST:event_jLabel18MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DashBoardMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashBoardMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashBoardMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashBoardMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashBoardMenu(true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AddGradesPanel;
    private javax.swing.JPanel CenterDesktop;
    private javax.swing.JPanel DashBoard;
    private javax.swing.JPanel MasterDesktop;
    private javax.swing.JPanel MasterList;
    private javax.swing.JPanel Records;
    private javax.swing.JPanel Reports;
    private javax.swing.JPanel StudentsPanel;
    private javax.swing.JPanel SubjectsPanel;
    private javax.swing.JLabel b_addStudents;
    private javax.swing.JButton b_addSubject1;
    private javax.swing.JLabel b_deleteStudents;
    private javax.swing.JLabel b_exit;
    private javax.swing.JLabel b_updateStudents;
    private javax.swing.JComboBox<String> c_sem;
    private javax.swing.JLabel i_addStudents;
    private javax.swing.JLabel i_addStudents1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel l_addStudents;
    private javax.swing.JLabel l_addingGrade;
    private javax.swing.JLabel l_btnCompute;
    private javax.swing.JLabel l_dashboard;
    private javax.swing.JLabel l_engG;
    private javax.swing.JLabel l_filG;
    private javax.swing.JLabel l_idStd;
    private javax.swing.JLabel l_idStdRecord;
    private javax.swing.JLabel l_masterlist;
    private javax.swing.JLabel l_mathG;
    private javax.swing.JLabel l_nameStd;
    private javax.swing.JLabel l_nameStdRecords;
    private javax.swing.JLabel l_pEng;
    private javax.swing.JLabel l_pFil;
    private javax.swing.JLabel l_pMath;
    private javax.swing.JLabel l_pPe;
    private javax.swing.JLabel l_pProg;
    private javax.swing.JLabel l_pSci;
    private javax.swing.JLabel l_peG;
    private javax.swing.JLabel l_progG;
    private javax.swing.JLabel l_records;
    private javax.swing.JLabel l_sciG;
    private javax.swing.JLabel l_studentMenu;
    private javax.swing.JLabel l_subjectMenu;
    private javax.swing.JButton l_updateGrade;
    private javax.swing.JLabel l_updateRemarks;
    private javax.swing.JLabel label_id;
    private javax.swing.JLabel label_name;
    private javax.swing.JLabel label_subj;
    private javax.swing.JPanel p_addGrade;
    private javax.swing.JPanel p_addingPanelHide;
    private javax.swing.JPanel p_dashboard;
    private javax.swing.JPanel p_masterlist;
    private javax.swing.JPanel p_records;
    private javax.swing.JPanel p_students;
    private javax.swing.JPanel p_subject;
    private javax.swing.JTable t_listOfStudents;
    private javax.swing.JTable t_listofSubject;
    private javax.swing.JTable t_recordList;
    private javax.swing.JTextField tf_english;
    private javax.swing.JTextField tf_filipino;
    private javax.swing.JTextField tf_homework;
    private javax.swing.JTextField tf_math;
    private javax.swing.JTextField tf_pe;
    private javax.swing.JTextField tf_programming;
    private javax.swing.JTextField tf_science;
    private javax.swing.JTextField tf_search;
    private javax.swing.JTextField tf_test;
    private javax.swing.JTextField tf_total;
    // End of variables declaration//GEN-END:variables
}
