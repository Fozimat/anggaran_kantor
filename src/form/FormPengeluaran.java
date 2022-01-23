/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import konfigurasi.DBKoneksi;

/**
 *
 * @author lenovo
 */
public class FormPengeluaran extends javax.swing.JFrame {

    /**
     * Creates new form FormPengeluaran
     */
    
    private DefaultTableModel TablePengeluaran;
    private String SQL;
    DecimalFormat Rp = (DecimalFormat) DecimalFormat.getCurrencyInstance();
    
    public FormPengeluaran() {
        initComponents();
        anggaranFromComboBox();
        hideId();
        hideIdAnggaran();
        formatRupiah();
    }
    
    private void hideId() {
        jLabel_id.setVisible(false);
    }
    
    private void hideIdAnggaran() {
        jLabel_id_anggaran.setVisible(false);
    }
    
     private void formatRupiah() {
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        
        formatRp.setCurrencySymbol("Rp ");
        formatRp.setGroupingSeparator('.');
        Rp.setMaximumFractionDigits(0);
        Rp.setDecimalFormatSymbols(formatRp);
    }
    
    public void TampilDataPengeluaran() {
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
            SQL = "SELECT pengeluaran.id, tanggal, jenis_anggaran, keterangan,  total FROM\n" +
                    "(\n" +
                    "	SELECT pengeluaran.id, tanggal, COALESCE(jenis_anggaran, 'SUB TOTAL') as jenis_anggaran, keterangan, SUM(jumlah) AS total\n" +
                    "    FROM pengeluaran \n" +
                    "    INNER JOIN anggaran ON pengeluaran.id_anggaran = anggaran.id\n" +
                    "    GROUP BY tanggal, pengeluaran.id\n" +
                    "    ORDER BY pengeluaran.id ASC, tanggal ASC\n" +
                    ") as pengeluaran \n" +
                    "UNION ALL\n" +
                    "SELECT null, tanggal, null, 'SUB TOTAL', SUM(jumlah) AS total\n" +
                    "FROM pengeluaran GROUP BY tanggal  \n" +
                    "ORDER BY tanggal, jenis_anggaran DESC";
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
            
            String total = "SELECT SUM(jumlah) as jumlah FROM pengeluaran";
            ResultSet total_rs = stmt.executeQuery(total);
            if(total_rs.next()) {
                TablePengeluaran.addRow(new Object[] {
                    "",
                    "", 
                    "",
                    "",
                    "TOTAL PENGELUARAN", 
                    Rp.format(Double.parseDouble(total_rs.getString("jumlah"))),
                });
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
    
    public void FilterAngka(KeyEvent b) {
        if (Character.isAlphabetic(b.getKeyChar())) {
            b.consume();
            JOptionPane.showMessageDialog(null, "Masukan Hanya Angka", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
    
      public final void anggaranFromComboBox() {
        Connection conn = new DBKoneksi().connect();
        try {
            SQL = "SELECT * FROM anggaran";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while(rs.next()) {
                jComboBox_anggaran.addItem(rs.getString("jenis_anggaran"));
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void resetForm() {
        jComboBox_anggaran.setSelectedItem("--- PILIH ---");
        jTextField_jumlah.setText("");
        jTextArea_keterangan.setText("");
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
        jPanel8 = new javax.swing.JPanel();
        jLabel_exit = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_pengeluaran = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField_jumlah = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_keterangan = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jLabel_simpan = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel_ubah = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel_hapus = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel_reset = new javax.swing.JLabel();
        jLabel_id = new javax.swing.JLabel();
        jDateChooser_tanggal = new com.toedter.calendar.JDateChooser();
        jComboBox_anggaran = new javax.swing.JComboBox();
        jLabel_id_anggaran = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(34, 45, 65));

        jPanel5.setBackground(new java.awt.Color(82, 108, 156));
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

        jPanel4.setBackground(new java.awt.Color(34, 45, 65));
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel15.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(96, 83, 150));
        jLabel15.setText("Data Pengeluaran");

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

        jLabel12.setFont(new java.awt.Font("Poppins Light", 0, 14)); // NOI18N
        jLabel12.setText("Jenis Anggaran");

        jLabel16.setFont(new java.awt.Font("Poppins Light", 0, 14)); // NOI18N
        jLabel16.setText("Tanggal");

        jLabel17.setFont(new java.awt.Font("Poppins Light", 0, 14)); // NOI18N
        jLabel17.setText("Jumlah");

        jLabel18.setFont(new java.awt.Font("Poppins Light", 0, 14)); // NOI18N
        jLabel18.setText("Keterangan");

        jTextField_jumlah.setFont(new java.awt.Font("Poppins Light", 0, 14)); // NOI18N
        jTextField_jumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_jumlahKeyTyped(evt);
            }
        });

        jTextArea_keterangan.setColumns(20);
        jTextArea_keterangan.setFont(new java.awt.Font("Poppins Light", 0, 14)); // NOI18N
        jTextArea_keterangan.setRows(5);
        jScrollPane2.setViewportView(jTextArea_keterangan);

        jPanel7.setBackground(new java.awt.Color(69, 90, 128));

        jLabel_simpan.setFont(new java.awt.Font("Poppins Light", 0, 12)); // NOI18N
        jLabel_simpan.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_simpan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_save_20px.png"))); // NOI18N
        jLabel_simpan.setText("Simpan");
        jLabel_simpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel_simpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_simpanMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_simpan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_simpan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(69, 90, 128));
        jPanel9.setPreferredSize(new java.awt.Dimension(84, 33));

        jLabel_ubah.setFont(new java.awt.Font("Poppins Light", 0, 12)); // NOI18N
        jLabel_ubah.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_ubah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_ubah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_pencil_20px_1.png"))); // NOI18N
        jLabel_ubah.setText("Ubah");
        jLabel_ubah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel_ubah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_ubahMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_ubah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_ubah, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        jPanel10.setBackground(new java.awt.Color(69, 90, 128));

        jLabel_hapus.setFont(new java.awt.Font("Poppins Light", 0, 12)); // NOI18N
        jLabel_hapus.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_hapus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_hapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_trash_20px.png"))); // NOI18N
        jLabel_hapus.setText("Hapus");
        jLabel_hapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel_hapus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_hapusMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_hapus, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_hapus, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        jPanel11.setBackground(new java.awt.Color(69, 90, 128));
        jPanel11.setPreferredSize(new java.awt.Dimension(84, 33));

        jLabel_reset.setFont(new java.awt.Font("Poppins Light", 0, 12)); // NOI18N
        jLabel_reset.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_reset.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_reset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/icons8_reset_20px.png"))); // NOI18N
        jLabel_reset.setText("Reset");
        jLabel_reset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel_reset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_resetMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_reset, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_reset, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );

        jComboBox_anggaran.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--- PILIH ---" }));
        jComboBox_anggaran.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_anggaranItemStateChanged(evt);
            }
        });

        jLabel_id_anggaran.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel12)
                                .addComponent(jLabel16)
                                .addComponent(jLabel17)
                                .addComponent(jLabel18))
                            .addGap(30, 30, 30)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField_jumlah)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jDateChooser_tanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                                .addComponent(jComboBox_anggaran, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(60, 60, 60)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(46, 46, 46)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(65, 65, 65)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel_id_anggaran)
                                        .addComponent(jLabel_id))))
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 805, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 23, Short.MAX_VALUE))
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel_id)
                                .addGap(30, 30, 30))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)
                                    .addComponent(jComboBox_anggaran, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jDateChooser_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel17)
                                    .addComponent(jTextField_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel_id_anggaran))
                        .addGap(20, 20, 20))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseClicked

    private void jPanel5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseEntered
        
    }//GEN-LAST:event_jPanel5MouseEntered

    private void jPanel5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseExited
        
    }//GEN-LAST:event_jPanel5MouseExited

    private void jPanel3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseEntered
        jPanel3.setBackground(new Color(82, 108, 156));
    }//GEN-LAST:event_jPanel3MouseEntered

    private void jPanel3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseExited
        jPanel3.setBackground(new Color(34,45,65));
    }//GEN-LAST:event_jPanel3MouseExited

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        this.setVisible(false);
        FormDashboard dashboard = new FormDashboard();
        dashboard.setVisible(true);
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jPanel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseEntered
        jPanel4.setBackground(new Color(82, 108, 156));
    }//GEN-LAST:event_jPanel4MouseEntered

    private void jPanel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseExited
        jPanel4.setBackground(new Color(34,45,65));
    }//GEN-LAST:event_jPanel4MouseExited

    private void jPanel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseEntered
        jPanel6.setBackground(new Color(82, 108, 156));
    }//GEN-LAST:event_jPanel6MouseEntered

    private void jPanel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseExited
        jPanel6.setBackground(new Color(34,45,65));
    }//GEN-LAST:event_jPanel6MouseExited

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

    private void jPanel_logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel_logoutMouseEntered
        jPanel_logout.setBackground(new Color(82, 108, 156));
    }//GEN-LAST:event_jPanel_logoutMouseEntered

    private void jPanel_logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel_logoutMouseExited
        jPanel_logout.setBackground(new Color(34,45,65));
    }//GEN-LAST:event_jPanel_logoutMouseExited

    private void jLabel_exitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_exitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel_exitMouseClicked

    private void jTable_pengeluaranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_pengeluaranMouseClicked
        int baris = jTable_pengeluaran.getSelectedRow();
        jLabel_id.setText(TablePengeluaran.getValueAt(baris, 1).toString());
        jComboBox_anggaran.setSelectedItem(TablePengeluaran.getValueAt(baris, 2).toString());
               
        try{
            SQL = "SELECT jumlah FROM pengeluaran WHERE id = '"+jLabel_id.getText()+"'";
            Connection conn = new DBKoneksi().connect();
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(SQL);
            if(res.next()){
                jTextField_jumlah.setText(res.getString("jumlah"));
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null,ex,"Error",JOptionPane.INFORMATION_MESSAGE);
        }
        
        jTextArea_keterangan.setText(TablePengeluaran.getValueAt(baris, 4).toString());
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Date dateValue = null;
        try {
            dateValue = date.parse((String) TablePengeluaran.getValueAt(baris, 3));
            jDateChooser_tanggal.setDate(dateValue);
        } catch (ParseException ex) {
            Logger.getLogger(FormPengeluaran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTable_pengeluaranMouseClicked

    private void jTextField_jumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_jumlahKeyTyped
        FilterAngka(evt);
    }//GEN-LAST:event_jTextField_jumlahKeyTyped

    private void jLabel_simpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_simpanMouseClicked
        if(jTextField_jumlah.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Jumlah tidak boleh kosong");
            jTextField_jumlah.requestFocus();
        } else if(jDateChooser_tanggal.getDate() == null){
            JOptionPane.showMessageDialog(null, "Tanggal tidak boleh kosong");
        } 
        else if(jTextArea_keterangan.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Keterangan tidak boleh kosong");
            jTextArea_keterangan.requestFocus();
        }
        else {
            Connection conn = new DBKoneksi().connect();
            try {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO pengeluaran (id_anggaran, tanggal, jumlah, keterangan) VALUES (?,?,?,?)");
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
                String tanggal = String.valueOf(fm.format(jDateChooser_tanggal.getDate()));
                try {
                    stmt.setString(1, jLabel_id_anggaran.getText());
                    stmt.setString(2, tanggal);
                    stmt.setString(3, jTextField_jumlah.getText());
                    stmt.setString(4, jTextArea_keterangan.getText());
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data berhasil disimpan", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    resetForm();
                    TampilDataPengeluaran();
                } catch(SQLException e) {
                   JOptionPane.showMessageDialog(null, "Data Gagal disimpan", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_jLabel_simpanMouseClicked

    private void jLabel_ubahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_ubahMouseClicked
        if(jTextField_jumlah.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Jumlah tidak boleh kosong");
            jTextField_jumlah.requestFocus();
        } else if(jDateChooser_tanggal.getDate() == null){
            JOptionPane.showMessageDialog(null, "Tanggal tidak boleh kosong");
        } 
        else if(jTextArea_keterangan.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Keterangan tidak boleh kosong");
            jTextArea_keterangan.requestFocus();
        }
        else {
            Connection conn = new DBKoneksi().connect();
            try {
                PreparedStatement stmt = conn.prepareStatement("UPDATE pengeluaran SET id_anggaran = ? , tanggal = ? , jumlah = ?, keterangan = ? WHERE id = ?");
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
                String tanggal = String.valueOf(fm.format(jDateChooser_tanggal.getDate()));
                try {
                    stmt.setString(1, jLabel_id_anggaran.getText());
                    stmt.setString(2, tanggal);
                    stmt.setString(3, jTextField_jumlah.getText());
                    stmt.setString(4, jTextArea_keterangan.getText());
                    stmt.setString(5, jLabel_id.getText());
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data berhasil diubah", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                    resetForm();
                    TampilDataPengeluaran();
                } catch(SQLException e) {
                   JOptionPane.showMessageDialog(null, "Data Gagal diubah", "Pesan", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_jLabel_ubahMouseClicked

    private void jLabel_hapusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_hapusMouseClicked
        Connection conn = new DBKoneksi().connect();
        int OK = JOptionPane.showConfirmDialog(null, "Apakah anda yakin?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(OK == 0) {
            try {
                SQL = "DELETE FROM pengeluaran WHERE id='"+jLabel_id.getText()+"'";
                java.sql.PreparedStatement stmt = conn.prepareStatement(SQL);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
                TampilDataPengeluaran();
                resetForm();
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(null, "Data gagal dihapus");
            }
        }
    }//GEN-LAST:event_jLabel_hapusMouseClicked

    private void jLabel_resetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_resetMouseClicked
        resetForm();
    }//GEN-LAST:event_jLabel_resetMouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        TampilDataPengeluaran();
    }//GEN-LAST:event_formWindowActivated

    private void jComboBox_anggaranItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_anggaranItemStateChanged
        Connection conn = new DBKoneksi().connect();
        try {
            SQL = "SELECT id FROM anggaran WHERE jenis_anggaran='"+jComboBox_anggaran.getSelectedItem()+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while(rs.next()) {
                Object[] ob = new Object[2];
                ob[0] = rs.getString(1);
                jLabel_id_anggaran.setText((String) ob[0]);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_jComboBox_anggaranItemStateChanged

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
            java.util.logging.Logger.getLogger(FormPengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPengeluaran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormPengeluaran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox_anggaran;
    private com.toedter.calendar.JDateChooser jDateChooser_tanggal;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_exit;
    private javax.swing.JLabel jLabel_hapus;
    private javax.swing.JLabel jLabel_id;
    private javax.swing.JLabel jLabel_id_anggaran;
    private javax.swing.JLabel jLabel_reset;
    private javax.swing.JLabel jLabel_simpan;
    private javax.swing.JLabel jLabel_ubah;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel_logout;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable_pengeluaran;
    private javax.swing.JTextArea jTextArea_keterangan;
    private javax.swing.JTextField jTextField_jumlah;
    // End of variables declaration//GEN-END:variables
}
