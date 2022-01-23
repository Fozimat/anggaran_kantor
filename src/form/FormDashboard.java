/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import konfigurasi.DBKoneksi;

/**
 *
 * @author lenovo
 */
public class FormDashboard extends javax.swing.JFrame {

    /**
     * Creates new form FormDashboard
     */
    private DefaultTableModel TablePengeluaran;
    private String SQL;
    String total;
    public String SQL_hari, SQL_bulan, SQL_tahun, SQL_total;
    DecimalFormat Rp = (DecimalFormat) DecimalFormat.getCurrencyInstance();
    DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
    Statement st_hari, st_bulan, st_tahun, st_total;
    ResultSet rs_hari, rs_bulan, rs_tahun, rs_total;


    public FormDashboard() {
        initComponents();
        formatRupiah();
        getPengeluaran();
        TampilDataPengeluaran();
//        jLabel19.setVisible(false);
//        jTextField_cari.setVisible(false);
    }
    
     private void formatRupiah() {
        
        formatRp.setCurrencySymbol("Rp ");
        formatRp.setGroupingSeparator('.');
        Rp.setMaximumFractionDigits(0);
        Rp.setDecimalFormatSymbols(formatRp);
    }
     
     private void TampilDataPengeluaran() {
        TablePengeluaran = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        TablePengeluaran.addColumn("NO");
        TablePengeluaran.addColumn("ID");
        TablePengeluaran.addColumn("JENIS");
        TablePengeluaran.addColumn("TANGGAL");
        TablePengeluaran.addColumn("KETERANGAN");
        TablePengeluaran.addColumn("JUMLAH");
        jTable_pengeluaran.setModel(TablePengeluaran);
        Connection conn = new DBKoneksi().connect();
        try {
            Statement stmt = conn.createStatement();
            if(jComboBox_waktu.getSelectedItem() == "Hari Ini") {
                SQL = "SELECT pengeluaran.id, jenis_anggaran, tanggal, keterangan, jumlah AS total FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE DATE(tanggal) = CURRENT_DATE()";
                total = "SELECT SUM(jumlah) as jumlah FROM pengeluaran WHERE DATE(tanggal) = CURRENT_DATE()";
            } else if(jComboBox_waktu.getSelectedItem() == "Bulan Ini") {
                SQL = "SELECT pengeluaran.id, jenis_anggaran, tanggal, keterangan, jumlah AS total FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE MONTH(tanggal)= MONTH(CURDATE()) ORDER BY tanggal ASC";
                 total = "SELECT SUM(jumlah) as jumlah FROM pengeluaran WHERE MONTH(tanggal)= MONTH(CURDATE())";
            } else if(jComboBox_waktu.getSelectedItem() == "Tahun Ini") {
                SQL = "SELECT pengeluaran.id, jenis_anggaran, tanggal, keterangan, jumlah AS total FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE YEAR(tanggal) = YEAR(CURRENT_DATE()) ORDER BY tanggal ASC";
                total = "SELECT SUM(jumlah) as jumlah FROM pengeluaran WHERE YEAR(tanggal) = YEAR(CURRENT_DATE())";
            } else if(jComboBox_waktu.getSelectedItem() == "Keseluruhan") {
                SQL = "SELECT pengeluaran.id, jenis_anggaran, tanggal, keterangan, jumlah AS total FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id ORDER BY tanggal ASC";
                total = "SELECT SUM(jumlah) as jumlah FROM pengeluaran";
            } else {
                SQL = "SELECT pengeluaran.id, jenis_anggaran, tanggal, keterangan, jumlah AS total FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE DATE(tanggal) = CURRENT_DATE()";
                total = "SELECT SUM(jumlah) as jumlah FROM pengeluaran WHERE DATE(tanggal) = CURRENT_DATE()";
            }
            ResultSet rs = stmt.executeQuery(SQL);
            int no = 1;
            while(rs.next()) {
                TablePengeluaran.addRow(new Object[] {                 
                    no++,
                    rs.getString("id"),
                    rs.getString("jenis_anggaran"),
                    rs.getString("tanggal"),
                    rs.getString("keterangan"),
                    Rp.format(Double.parseDouble(rs.getString("total"))),
                });
            }
            
            
            ResultSet total_rs = stmt.executeQuery(total);
            if(total_rs.next()) {
                if(total_rs.getString("jumlah") != null) {
                        TablePengeluaran.addRow(new Object[] {
                        "",
                        "", 
                        "",
                        "",
                        "TOTAL PENGELUARAN", 
                        Rp.format(Double.parseDouble(total_rs.getString("jumlah"))),
                    });
                } else {
                    TablePengeluaran.addRow(new Object[] {
                        "",
                        "", 
                        "",
                        "",
                        "Tidak Ada Pengeluaran", 
                        "",
                    });
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        jTable_pengeluaran.getColumnModel().getColumn(0).setPreferredWidth(1);
        jTable_pengeluaran.getColumnModel().getColumn(1).setPreferredWidth(1);
        jTable_pengeluaran.getColumnModel().getColumn(2).setPreferredWidth(250);
        jTable_pengeluaran.getColumnModel().getColumn(3).setPreferredWidth(50);
        jTable_pengeluaran.getColumnModel().getColumn(4).setPreferredWidth(120);
        jTable_pengeluaran.getColumnModel().getColumn(5).setPreferredWidth(100);
        jTable_pengeluaran.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        
        jTable_pengeluaran.getColumnModel().getColumn(1).setMinWidth(0);
        jTable_pengeluaran.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable_pengeluaran.getColumnModel().getColumn(1).setWidth(0);
    }
    
    private void getPengeluaran() {
        Connection conn = new DBKoneksi().connect();
        try{
            SQL_hari = "SELECT sum(jumlah) AS jumlah FROM pengeluaran WHERE DATE(tanggal) = CURRENT_DATE()";
            st_hari = conn.createStatement();
            rs_hari = st_hari.executeQuery(SQL_hari);
            if(rs_hari.next()){
                if(rs_hari.getString("jumlah") != null) {
                    jLabel_hari.setText(Rp.format(Double.parseDouble(rs_hari.getString("jumlah"))));  
                } else {
                 jLabel_hari.setText("Rp 0");  
                }
            } 
            
            SQL_bulan = "SELECT sum(jumlah) AS jumlah FROM pengeluaran WHERE MONTH(tanggal)= MONTH(CURDATE())";
            st_bulan = conn.createStatement();
            rs_bulan = st_bulan.executeQuery(SQL_bulan);
            if(rs_bulan.next()){
                if(rs_bulan.getString("jumlah") != null) {
                jLabel_bulan.setText(Rp.format(Double.parseDouble(rs_bulan.getString("jumlah"))));
                } else {
                jLabel_bulan.setText("Rp 0");  
                }
            } 
            
            SQL_tahun = "SELECT SUM(jumlah) AS jumlah FROM pengeluaran WHERE YEAR(tanggal) = YEAR(CURRENT_DATE())";
            st_tahun = conn.createStatement();
            rs_tahun = st_tahun.executeQuery(SQL_tahun);
            if(rs_tahun.next()){
                if(rs_tahun.getString("jumlah") != null) {
                jLabel_tahun.setText(Rp.format(Double.parseDouble(rs_tahun.getString("jumlah"))));
                } else {
                jLabel_tahun.setText("Rp 0");  
                }
            } 
            
            SQL_total = "SELECT SUM(jumlah) AS jumlah FROM pengeluaran";
            st_total = conn.createStatement();
            rs_total = st_total.executeQuery(SQL_total);
            if(rs_total.next()){
                if(rs_total.getString("jumlah") != null) {
                jLabel_total.setText(Rp.format(Double.parseDouble(rs_total.getString("jumlah"))));  
                } else {
                jLabel_total.setText("Rp 0");  
                }
            } 
            
        } catch(SQLException e) {
             JOptionPane.showMessageDialog(null,e,"Error",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void cariDataPengeluaran() {
        TablePengeluaran = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        TablePengeluaran.addColumn("NO");
        TablePengeluaran.addColumn("ID");
        TablePengeluaran.addColumn("JENIS");
        TablePengeluaran.addColumn("TANGGAL");
        TablePengeluaran.addColumn("KETERANGAN");
        TablePengeluaran.addColumn("JUMLAH");
        jTable_pengeluaran.setModel(TablePengeluaran);
        Connection conn = new DBKoneksi().connect();
        try {
            Statement stmt = conn.createStatement();
            String key = jTextField_cari.getText();
            if(jComboBox_waktu.getSelectedItem() == "Hari Ini") {
                SQL = "SELECT pengeluaran.id, jenis_anggaran, tanggal, keterangan, jumlah AS total FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE DATE(tanggal) = CURRENT_DATE() AND jenis_anggaran LIKE '%"+key+"%'";
                total = "SELECT SUM(jumlah) AS jumlah FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE DATE(tanggal) = CURRENT_DATE() AND jenis_anggaran LIKE '%"+key+"%'";
            } else if(jComboBox_waktu.getSelectedItem() == "Bulan Ini") {
                SQL = "SELECT pengeluaran.id, jenis_anggaran, tanggal, keterangan, jumlah AS total FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE MONTH(tanggal)= MONTH(CURDATE()) AND jenis_anggaran LIKE '%"+key+"%' ORDER BY tanggal ASC";
                total = "SELECT SUM(jumlah) AS jumlah FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE MONTH(tanggal)= MONTH(CURDATE()) AND jenis_anggaran LIKE '%"+key+"%'";
            } else if(jComboBox_waktu.getSelectedItem() == "Tahun Ini") {
                SQL = "SELECT pengeluaran.id, jenis_anggaran, tanggal, keterangan, jumlah AS total FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE YEAR(tanggal) = YEAR(CURRENT_DATE()) AND jenis_anggaran LIKE '%"+key+"%' ORDER BY tanggal ASC";
                total = "SELECT SUM(jumlah) AS jumlah FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE YEAR(tanggal) = YEAR(CURRENT_DATE()) AND jenis_anggaran LIKE '%"+key+"%'";
            } else if(jComboBox_waktu.getSelectedItem() == "Keseluruhan") {
                SQL = "SELECT pengeluaran.id, jenis_anggaran, tanggal, keterangan, jumlah AS total FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE jenis_anggaran LIKE '%"+key+"%' ORDER BY tanggal ASC";
                total = "SELECT SUM(jumlah) AS jumlah FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE jenis_anggaran LIKE '%"+key+"%'";
            } else {
                SQL = "SELECT pengeluaran.id, jenis_anggaran, tanggal, keterangan, jumlah AS total FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE DATE(tanggal) = CURRENT_DATE() AND jenis_anggaran LIKE '%"+key+"%'";
                total = "SELECT SUM(jumlah) AS jumlah FROM pengeluaran INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id WHERE DATE(tanggal) = CURRENT_DATE() AND jenis_anggaran LIKE '%"+key+"%'";
            }
            ResultSet rs = stmt.executeQuery(SQL);
            int no = 1;
            while(rs.next()) {
                TablePengeluaran.addRow(new Object[] {                 
                    no++,
                    rs.getString("id"),
                    rs.getString("jenis_anggaran"),
                    rs.getString("tanggal"),
                    rs.getString("keterangan"),
                    Rp.format(Double.parseDouble(rs.getString("total"))),
                });
            }
            
            
            ResultSet total_rs = stmt.executeQuery(total);
            if(total_rs.next()) {
                if(total_rs.getString("jumlah") != null) {
                        TablePengeluaran.addRow(new Object[] {
                        "",
                        "", 
                        "",
                        "",
                        "TOTAL PENGELUARAN", 
                        Rp.format(Double.parseDouble(total_rs.getString("jumlah"))),
                    });
                } else {
                    TablePengeluaran.addRow(new Object[] {
                        "",
                        "", 
                        "",
                        "",
                        "Tidak ditemukan", 
                        "",
                    });
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        jTable_pengeluaran.getColumnModel().getColumn(0).setPreferredWidth(1);
        jTable_pengeluaran.getColumnModel().getColumn(1).setPreferredWidth(1);
        jTable_pengeluaran.getColumnModel().getColumn(2).setPreferredWidth(250);
        jTable_pengeluaran.getColumnModel().getColumn(3).setPreferredWidth(50);
        jTable_pengeluaran.getColumnModel().getColumn(4).setPreferredWidth(120);
        jTable_pengeluaran.getColumnModel().getColumn(5).setPreferredWidth(100);
        jTable_pengeluaran.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        
        jTable_pengeluaran.getColumnModel().getColumn(1).setMinWidth(0);
        jTable_pengeluaran.getColumnModel().getColumn(1).setMaxWidth(0);
        jTable_pengeluaran.getColumnModel().getColumn(1).setWidth(0);
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
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel_logout = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel_bulan = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel_hari = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel_tahun = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel_total = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel_exit = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_pengeluaran = new javax.swing.JTable();
        jComboBox_waktu = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jTextField_cari = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(34, 45, 65));

        jPanel5.setBackground(new java.awt.Color(34, 45, 65));
        jPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel5MouseExited(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("DATA PENGELUARAN");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_money_25px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(40, 40, 40))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel6)))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(34, 45, 65));
        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel3MouseExited(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DATA PEGAWAI");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_user_25px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(44, 44, 44))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(82, 108, 156));
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel4MouseExited(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("DASHBOARD");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_home_25px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addGap(63, 63, 63))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_cash_in_hand_100px.png"))); // NOI18N

        jPanel6.setBackground(new java.awt.Color(34, 45, 65));
        jPanel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel6MouseExited(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("LAPORAN");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_printer_25px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                .addGap(82, 82, 82))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel8)))
                .addContainerGap())
        );

        jPanel_logout.setBackground(new java.awt.Color(34, 45, 65));
        jPanel_logout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel_logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel_logoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel_logoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel_logoutMouseExited(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("LOGOUT");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_lock_25px.png"))); // NOI18N

        javax.swing.GroupLayout jPanel_logoutLayout = new javax.swing.GroupLayout(jPanel_logout);
        jPanel_logout.setLayout(jPanel_logoutLayout);
        jPanel_logoutLayout.setHorizontalGroup(
            jPanel_logoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_logoutLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_logoutLayout.setVerticalGroup(
            jPanel_logoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_logoutLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_logoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel_logoutLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel10)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel_logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jLabel3))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel3)
                .addGap(10, 10, 10)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanel_logout, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE))
        );

        jLabel15.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(96, 83, 150));
        jLabel15.setText("Dashboard");

        jLabel26.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(96, 83, 150));
        jLabel26.setText("Pengeluaran Bulan ini");

        jPanel14.setBackground(new java.awt.Color(34, 45, 65));

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_today_25px.png"))); // NOI18N

        jLabel_bulan.setFont(new java.awt.Font("Poppins", 0, 20)); // NOI18N
        jLabel_bulan.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_bulan.setText("Rp. 100.000");

        jLabel25.setFont(new java.awt.Font("Poppins Light", 0, 15)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Total");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel_bulan, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel23)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_bulan)
                .addGap(16, 16, 16))
        );

        jPanel16.setBackground(new java.awt.Color(34, 45, 65));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_today_25px.png"))); // NOI18N

        jLabel_hari.setFont(new java.awt.Font("Poppins", 0, 20)); // NOI18N
        jLabel_hari.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_hari.setText("Rp. 100.000");

        jLabel32.setFont(new java.awt.Font("Poppins Light", 0, 15)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Total");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel_hari, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel30)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_hari)
                .addGap(16, 16, 16))
        );

        jLabel33.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(96, 83, 150));
        jLabel33.setText("Pengeluaran Hari ini");

        jLabel27.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(96, 83, 150));
        jLabel27.setText("Pengeluaran Tahun ini");

        jPanel15.setBackground(new java.awt.Color(34, 45, 65));

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_today_25px.png"))); // NOI18N

        jLabel_tahun.setFont(new java.awt.Font("Poppins", 0, 20)); // NOI18N
        jLabel_tahun.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_tahun.setText("Rp. 100.000");

        jLabel34.setFont(new java.awt.Font("Poppins Light", 0, 15)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Total");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel_tahun, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel28)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_tahun)
                .addGap(16, 16, 16))
        );

        jPanel17.setBackground(new java.awt.Color(34, 45, 65));

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_today_25px.png"))); // NOI18N

        jLabel_total.setFont(new java.awt.Font("Poppins", 0, 20)); // NOI18N
        jLabel_total.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_total.setText("Rp. 100.000");

        jLabel37.setFont(new java.awt.Font("Poppins Light", 0, 15)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Total");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel_total, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel35)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_total)
                .addGap(16, 16, 16))
        );

        jLabel38.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(96, 83, 150));
        jLabel38.setText("Pengeluaran Keseluruhan");

        jPanel8.setBackground(new java.awt.Color(102, 15, 244));

        jLabel_exit.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel_exit.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_exit.setText("X");
        jLabel_exit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel_exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_exitMouseClicked(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Poppins", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("APLIKASI PENGELUARAN ANGGARAN KANTOR");

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_app_symbol_25px_1.png"))); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable_pengeluaran.setFont(new java.awt.Font("Poppins Light", 0, 14)); // NOI18N
        jTable_pengeluaran.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable_pengeluaran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_pengeluaranMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_pengeluaran);

        jComboBox_waktu.setFont(new java.awt.Font("Poppins Light", 0, 14)); // NOI18N
        jComboBox_waktu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hari Ini", "Bulan Ini", "Tahun Ini", "Keseluruhan" }));
        jComboBox_waktu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_waktuItemStateChanged(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(96, 83, 150));
        jLabel12.setText("Pilih Waktu Pengeluaran");

        jLabel19.setFont(new java.awt.Font("Poppins Light", 0, 14)); // NOI18N
        jLabel19.setText("Pencarian");

        jTextField_cari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_cariKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel15))
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jComboBox_waktu, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel19)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))))
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel33)
                                .addGap(1, 1, 1)
                                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addGap(1, 1, 1)
                                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addGap(1, 1, 1)
                                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addGap(1, 1, 1)
                                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox_waktu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel19)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel_exitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_exitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel_exitMouseClicked

    private void jPanel_logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel_logoutMouseClicked
        String ObjButton[] = {"YES","NO"};
        int pilihan = JOptionPane.showOptionDialog(null,"Apakah Anda yakin ingin keluar?","Message", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null,ObjButton,ObjButton[1]);
        if(pilihan == 0){
            this.setVisible(false);
            FormLogin login = new FormLogin();
            login.setVisible(true);
        }
    }//GEN-LAST:event_jPanel_logoutMouseClicked

    private void jPanel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseEntered
        jPanel3.setBackground(new Color(82, 108, 156));
    }//GEN-LAST:event_jPanel3MouseEntered

    private void jPanel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseExited
        jPanel3.setBackground(new Color(34,45,65));
    }//GEN-LAST:event_jPanel3MouseExited

    private void jPanel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseEntered
        
    }//GEN-LAST:event_jPanel4MouseEntered

    private void jPanel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseExited
       
    }//GEN-LAST:event_jPanel4MouseExited

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        this.setVisible(false);
        FormPengeluaran pengeluaran = new FormPengeluaran();
        pengeluaran.setVisible(true);
    }//GEN-LAST:event_jPanel5MouseClicked

    private void jPanel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseEntered
        jPanel5.setBackground(new Color(82, 108, 156));
    }//GEN-LAST:event_jPanel5MouseEntered

    private void jPanel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseExited
        jPanel5.setBackground(new Color(34,45,65));
    }//GEN-LAST:event_jPanel5MouseExited

    private void jPanel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseEntered
       jPanel6.setBackground(new Color(82, 108, 156));
    }//GEN-LAST:event_jPanel6MouseEntered

    private void jPanel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseExited
        jPanel6.setBackground(new Color(34,45,65));
    }//GEN-LAST:event_jPanel6MouseExited

    private void jPanel_logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel_logoutMouseEntered
        jPanel_logout.setBackground(new Color(82, 108, 156));
    }//GEN-LAST:event_jPanel_logoutMouseEntered

    private void jPanel_logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel_logoutMouseExited
        jPanel_logout.setBackground(new Color(34,45,65));
    }//GEN-LAST:event_jPanel_logoutMouseExited

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked
        this.setVisible(false);
        FormPegawai pegawai = new FormPegawai();
        pegawai.setVisible(true);
    }//GEN-LAST:event_jPanel3MouseClicked

    private void jPanel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseClicked
        this.setVisible(false);
        FormLaporan laporan = new FormLaporan();
        laporan.setVisible(true);
    }//GEN-LAST:event_jPanel6MouseClicked

    private void jTable_pengeluaranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_pengeluaranMouseClicked
        
    }//GEN-LAST:event_jTable_pengeluaranMouseClicked

    private void jComboBox_waktuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_waktuItemStateChanged
        TampilDataPengeluaran();
    }//GEN-LAST:event_jComboBox_waktuItemStateChanged

    private void jTextField_cariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_cariKeyReleased
       int baris = jTable_pengeluaran.getRowCount();
        if(baris == 0) {
            JOptionPane.showMessageDialog(null, "Data tidak ditemukan", "Pesan", JOptionPane.INFORMATION_MESSAGE);
            jTextField_cari.setText("");
        } else {
             cariDataPengeluaran();
        }
    }//GEN-LAST:event_jTextField_cariKeyReleased

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
            java.util.logging.Logger.getLogger(FormDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox_waktu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
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
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_bulan;
    private javax.swing.JLabel jLabel_exit;
    private javax.swing.JLabel jLabel_hari;
    private javax.swing.JLabel jLabel_tahun;
    private javax.swing.JLabel jLabel_total;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel_logout;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_pengeluaran;
    private javax.swing.JTextField jTextField_cari;
    // End of variables declaration//GEN-END:variables
}
