
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Lenovo
 */
public class adminpanel extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(adminpanel.class.getName());

    /**
     * Creates new form adminpanel
     */
    private String username;
    private String nama;
    
    
    public adminpanel() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    public adminpanel(String username, String nama) {
        initComponents();
        setLocationRelativeTo(null);
        
        this.username = username;
        this.nama = nama;
        
        nama_admin_sekarang.setText(nama);
        nama_admin_sekarang.setEditable(false);
        
        tampilkanDataKaryawan();
        loadProfileImage();
        
        txttempat_mencari_data.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txttempat_mencari_dataKeyReleased(evt);
            }
        });

    }
    
    private void loadProfileImage(){
            java.net.URL imgURL = getClass().getResource("/images/Userpfpconvert.jpg");
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            // Resize gambar agar pas dengan tombol
            Image scaled = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            jprofil.setIcon(new ImageIcon(scaled));
        } else {
            System.out.println("❌ Gambar tidak ditemukan di folder /images/");
        }
    }

    private void tampilkanDataKaryawan() {
    try {
        // Koneksi ke database
        java.sql.Connection conn = koneksi.getConnection();

        // Query ambil semua data dari tabel karyawan
        String sql = "SELECT * FROM karyawan";
        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
        java.sql.ResultSet rs = pst.executeQuery();

        // Buat model untuk JTable
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();

        // Tambahkan kolom ke model (sesuai tabel di database)
        model.addColumn("id_karyawan");
        model.addColumn("username");
        model.addColumn("password_akun");
        model.addColumn("nama");
        model.addColumn("posisi");

        // Loop hasil query dan tambahkan ke model
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_karyawan"), 
                rs.getString("username"),
                rs.getString("password_akun"),
                rs.getString("nama"),
                rs.getString("posisi")
            });
        }

        // Tampilkan ke tabel
        jtabel_karyawan.setModel(model);
        
        // pengaturan tampilan tabel
        jtabel_karyawan.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 18));
        jtabel_karyawan.getTableHeader().setFont(new java.awt.Font("SanSerif", java.awt.Font.BOLD, 16));
        jtabel_karyawan.setRowHeight(28);
        // jtabel_karyawan.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
        // Besar kecil lebar tabel
        javax.swing.table.TableColumnModel columnModel = jtabel_karyawan.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(120);   // ID
        columnModel.getColumn(1).setPreferredWidth(100);  // Username
        columnModel.getColumn(2).setPreferredWidth(150);  // Password
        columnModel.getColumn(3).setPreferredWidth(200);  // Nama
        columnModel.getColumn(4).setPreferredWidth(90);   // Posisi


        rs.close();
        pst.close();

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal menampilkan data: " + e.getMessage());
    }
}
 
    private void cariDataKaryawan(String keyword) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("id_karyawan");
        model.addColumn("username");
        model.addColumn("password");
        model.addColumn("nama");
        model.addColumn("posisi");

        try {
            java.sql.Connection conn = koneksi.getConnection();
            String sql = "SELECT * FROM karyawan "
                       + "WHERE id_karyawan LIKE ? OR username LIKE ? OR password_akun LIKE ? OR nama LIKE ? OR posisi LIKE ?";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);

            String cari = "%" + keyword + "%"; // supaya bisa mencari sebagian kata
            for (int i = 1; i <= 5; i++) {
                pst.setString(i, cari);
            }

            java.sql.ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_karyawan"),
                    rs.getString("username"),
                    rs.getString("password_akun"),
                    rs.getString("nama"),
                    rs.getString("posisi")
                });
            }

        jtabel_karyawan.setModel(model);
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
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
        jprofil = new javax.swing.JButton();
        nama_admin_sekarang = new javax.swing.JTextField();
        txttempat_mencari_data = new javax.swing.JTextField();
        pencarian_data = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        tombol_tambahdata = new javax.swing.JButton();
        tombol_editdata = new javax.swing.JButton();
        tombol_hapusdata = new javax.swing.JButton();
        jlamaninventaris = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtabel_karyawan = new javax.swing.JTable();
        jScrollBar1 = new javax.swing.JScrollBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setForeground(new java.awt.Color(102, 102, 102));

        jprofil.setMaximumSize(new java.awt.Dimension(64, 64));
        jprofil.setMinimumSize(new java.awt.Dimension(64, 64));
        jprofil.setPreferredSize(new java.awt.Dimension(64, 64));
        jprofil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jprofilActionPerformed(evt);
            }
        });

        nama_admin_sekarang.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        nama_admin_sekarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nama_admin_sekarangActionPerformed(evt);
            }
        });

        txttempat_mencari_data.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txttempat_mencari_data.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttempat_mencari_dataActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("cari data :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(702, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(nama_admin_sekarang, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jprofil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txttempat_mencari_data, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pencarian_data, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(nama_admin_sekarang, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jprofil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(pencarian_data, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txttempat_mencari_data, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tombol_tambahdata.setText("Tambah");
        tombol_tambahdata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombol_tambahdataActionPerformed(evt);
            }
        });

        tombol_editdata.setText("Edit");
        tombol_editdata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombol_editdataActionPerformed(evt);
            }
        });

        tombol_hapusdata.setText("Hapus");
        tombol_hapusdata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombol_hapusdataActionPerformed(evt);
            }
        });

        jlamaninventaris.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jlamaninventaris.setText("inventaris");
        jlamaninventaris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jlamaninventarisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jlamaninventaris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tombol_tambahdata, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(tombol_editdata, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tombol_hapusdata, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(tombol_tambahdata, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tombol_editdata, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tombol_hapusdata, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlamaninventaris, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jtabel_karyawan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id karyawan", "username", "password", "nama", "posisi"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jtabel_karyawan);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private profiloption profilForm;
    
    private void jprofilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jprofilActionPerformed
        // TODO add your handling code here:
        if (profilForm == null || !profilForm.isDisplayable()) {
            profilForm = new profiloption(username, nama);    
        }
        profilForm.setVisible(true);
    }//GEN-LAST:event_jprofilActionPerformed

    private void nama_admin_sekarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nama_admin_sekarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nama_admin_sekarangActionPerformed

    private void tombol_tambahdataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombol_tambahdataActionPerformed
        // TODO add your handling code here:
        tambah_data form_tambah = new tambah_data();
        form_tambah.addWindowListener(new java.awt.event.WindowAdapter() {
        
            // Tambahkan listener agar tabel otomatis di-refresh saat jendela ditutup
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                tampilkanDataKaryawan(); // refresh tabel
            }
        });
        form_tambah.setVisible(true);
    }//GEN-LAST:event_tombol_tambahdataActionPerformed

    private void tombol_editdataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombol_editdataActionPerformed
        // TODO add your handling code here:                                             
        int row = jtabel_karyawan.getSelectedRow();
        if (row >= 0) {
            String id = jtabel_karyawan.getValueAt(row, 0).toString();
            String username = jtabel_karyawan.getValueAt(row, 1).toString();
            String password = jtabel_karyawan.getValueAt(row, 2).toString();
            String nama = jtabel_karyawan.getValueAt(row, 3).toString();
            String posisi = jtabel_karyawan.getValueAt(row, 4).toString();

            edit_data formEdit = new edit_data(id, username, password, nama, posisi);
            formEdit.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    tampilkanDataKaryawan();
                }
            });
            formEdit.setVisible(true);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Pilih baris terlebih dahulu!");
        }
    }//GEN-LAST:event_tombol_editdataActionPerformed

    private void tombol_hapusdataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombol_hapusdataActionPerformed
        // TODO add your handling code here:                                                 
    int row = jtabel_karyawan.getSelectedRow(); // ambil baris yang dipilih

    if (row >= 0) { 
        // ambil ID dari kolom pertama (kolom id karyawan)
        String id = jtabel_karyawan.getValueAt(row, 0).toString();

        // konfirmasi ke pengguna sebelum hapus
        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Apakah kamu yakin ingin menghapus data dengan ID: " + id + "?",
            "Konfirmasi Hapus",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
            try {
                java.sql.Connection conn = koneksi.getConnection();
                String sql = "DELETE FROM karyawan WHERE id_karyawan = ?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, id);
                pst.executeUpdate();

                javax.swing.JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                tampilkanDataKaryawan(); // refresh tabel
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
            }
        }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Pilih baris terlebih dahulu!");
    }
 

    }//GEN-LAST:event_tombol_hapusdataActionPerformed

    private void txttempat_mencari_dataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttempat_mencari_dataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttempat_mencari_dataActionPerformed

    private void jlamaninventarisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jlamaninventarisActionPerformed
        // TODO add your handling code here:
        forminventaris fi = new forminventaris(username, nama);
        fi.setVisible(true);
    }//GEN-LAST:event_jlamaninventarisActionPerformed

    private void txttempat_mencari_dataKeyReleased(java.awt.event.KeyEvent evt) {                                            
    String keyword = txttempat_mencari_data.getText();
    cariDataKaryawan(keyword);
    }    
    



    
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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new adminpanel().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jlamaninventaris;
    private javax.swing.JButton jprofil;
    private javax.swing.JTable jtabel_karyawan;
    private javax.swing.JTextField nama_admin_sekarang;
    private javax.swing.JButton pencarian_data;
    private javax.swing.JButton tombol_editdata;
    private javax.swing.JButton tombol_hapusdata;
    private javax.swing.JButton tombol_tambahdata;
    private javax.swing.JTextField txttempat_mencari_data;
    // End of variables declaration//GEN-END:variables
}
