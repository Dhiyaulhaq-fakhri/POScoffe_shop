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
public class forminventaris extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(forminventaris.class.getName());

    /**
     * Creates new form forminventaris
     */
    private String username;
    private String nama;
    
    public forminventaris() {
        initComponents();
        setLocationRelativeTo(null);
    }

    public forminventaris(String username, String nama) {
        initComponents();
        setLocationRelativeTo(null);
        
        txtdisplaynamapemakai2.setText(nama);
        txtdisplaynamapemakai2.setEditable(false);
        
        tampilkandataproduk();
        loadProfileImage();
        
        txttempatcaridata2.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent evt) {
            String keyword = txttempatcaridata2.getText();
            caridataproduk(keyword);
        }
    });
       
}
    
    private void loadProfileImage(){
            java.net.URL imgURL = getClass().getResource("/images/Userpfpconvert.jpg");
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            // Resize gambar agar pas dengan tombol
            Image scaled = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            jtombolprofil2.setIcon(new ImageIcon(scaled));
        } else {
            System.out.println("❌ Gambar tidak ditemukan di folder /images/");
        }
    }
    
    private void tampilkandataproduk() {
    try {
        java.sql.Connection conn = koneksi.getConnection();
        
        String sql = "SELECT * FROM produk";
        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
        java.sql.ResultSet rs = pst.executeQuery();
        
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
        
        model.addColumn("id_produk");
        model.addColumn("nama");
        model.addColumn("jenis");
        model.addColumn("harga");
        model.addColumn("stok");
        
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_produk"),
                rs.getString("nama"),
                rs.getString("jenis"),
                rs.getString("harga"),
                rs.getString("stok")
            });
        }
        
        jtabelproduk2.setModel(model);
        
        // setting tabel
        
        jtabelproduk2.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 18));
        jtabelproduk2.getTableHeader().setFont(new java.awt.Font("SanSerif", java.awt.Font.BOLD, 16));
        jtabelproduk2.setRowHeight(28);
        
        
        javax.swing.table.TableColumnModel columnModel = jtabelproduk2.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(75);   // id_produk
        columnModel.getColumn(1).setPreferredWidth(100);  // nama
        columnModel.getColumn(2).setPreferredWidth(90);  // jenis
        columnModel.getColumn(3).setPreferredWidth(100);  // harga
        columnModel.getColumn(4).setPreferredWidth(90);   // stok
        
    }   catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal menampilkan data: " + e.getMessage());
    }
    
        
}
    
    private void caridataproduk(String keyword) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("id_produk");
        model.addColumn("nama");
        model.addColumn("jenis");
        model.addColumn("harga");
        model.addColumn("stok");
        
        try {
            java.sql.Connection conn = koneksi.getConnection();
            String sql = "SELECT * FROM produk "
                        + "WHERE id_produk LIKE ? "
                        + "OR nama LIKE ? "
                        + "OR jenis LIKE ? "
                        + "OR harga LIKE ? "
                        + "OR stok LIKE ?";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            
            String cari = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++) {
                pst.setString(i, cari);
            }
            java.sql.ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_produk"),
                    rs.getString("nama"),
                    rs.getString("jenis"),
                    rs.getString("harga"),
                    rs.getString("stok")
                });
            }
        jtabelproduk2.setModel(model);
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

        jtombolprofil2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jtomboltambah2 = new javax.swing.JButton();
        jtomboledit2 = new javax.swing.JButton();
        jtombolhapus2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtabelproduk2 = new javax.swing.JTable();
        txttempatcaridata2 = new javax.swing.JTextField();
        jtombolcari2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtdisplaynamapemakai2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));

        jtombolprofil2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtombolprofil2ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setForeground(new java.awt.Color(102, 102, 102));
        jPanel1.setToolTipText("");

        jtomboltambah2.setText("Tambah");
        jtomboltambah2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtomboltambah2ActionPerformed(evt);
            }
        });

        jtomboledit2.setText("Edit");
        jtomboledit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtomboledit2ActionPerformed(evt);
            }
        });

        jtombolhapus2.setText("Hapus");
        jtombolhapus2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtombolhapus2ActionPerformed(evt);
            }
        });

        jtabelproduk2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id_produk", "nama", "jenis", "harga", "stok"
            }
        ));
        jScrollPane1.setViewportView(jtabelproduk2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jtomboledit2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtomboltambah2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtombolhapus2, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jtomboltambah2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtomboledit2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtombolhapus2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txttempatcaridata2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttempatcaridata2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel1.setText("cari data :");

        txtdisplaynamapemakai2.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txttempatcaridata2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jtombolcari2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(txtdisplaynamapemakai2, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jtombolprofil2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jtombolprofil2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(txtdisplaynamapemakai2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtombolcari2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttempatcaridata2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtomboltambah2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtomboltambah2ActionPerformed
        // TODO add your handling code here:
        TambahProduk tp = new TambahProduk();
        tp.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                tampilkandataproduk(); // Refresh tabel produk setelah tambah
            }
        });
        tp.setVisible(true);
    }//GEN-LAST:event_jtomboltambah2ActionPerformed

    private void jtomboledit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtomboledit2ActionPerformed
        // TODO add your handling code here:
        int row = jtabelproduk2.getSelectedRow();
        if (row >= 0) {
            String id = jtabelproduk2.getValueAt(row, 0).toString();
            String nama = jtabelproduk2.getValueAt(row, 1).toString();
            String jenis = jtabelproduk2.getValueAt(row, 2).toString();
            String harga = jtabelproduk2.getValueAt(row, 3).toString();
            String stok = jtabelproduk2.getValueAt(row, 4).toString();
            
            editproduk formeditp = new editproduk(id, nama, jenis, harga, stok);
            formeditp.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    tampilkandataproduk();
                }
            });
            formeditp.setVisible(true);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Pilih baris terlebih dahulu!");
        }
    }//GEN-LAST:event_jtomboledit2ActionPerformed

    private void jtombolhapus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtombolhapus2ActionPerformed
        // TODO add your handling code here:
    int row = jtabelproduk2.getSelectedRow();
            
    if (row >= 0) {
        String id = jtabelproduk2.getValueAt(row, 0).toString();
        
        int konfirmasi = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Apakah kamu yakin ingin menghapus data dengan ID: " + id + "?",
                "Konfirmasi Hapus",
                javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (konfirmasi == javax.swing.JOptionPane.YES_OPTION) {
            try {
                java.sql.Connection conn = koneksi.getConnection();
                String sql = "DELETE FROM produk WHERE id_produk = ?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, id);
                pst.executeUpdate();
                
                javax.swing.JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                tampilkandataproduk();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
            }
            }
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Pilih baris terlebih dahulu!");
    }
                
    }//GEN-LAST:event_jtombolhapus2ActionPerformed

    private void txttempatcaridata2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttempatcaridata2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttempatcaridata2ActionPerformed

    private void jtombolprofil2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtombolprofil2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtombolprofil2ActionPerformed

    private void txttempatcaridata2(java.awt.event.KeyEvent evt) {
    String keyword = txttempatcaridata2.getText();
    caridataproduk(keyword);
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
        java.awt.EventQueue.invokeLater(() -> new forminventaris().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtabelproduk2;
    private javax.swing.JButton jtombolcari2;
    private javax.swing.JButton jtomboledit2;
    private javax.swing.JButton jtombolhapus2;
    private javax.swing.JButton jtombolprofil2;
    private javax.swing.JButton jtomboltambah2;
    private javax.swing.JTextField txtdisplaynamapemakai2;
    private javax.swing.JTextField txttempatcaridata2;
    // End of variables declaration//GEN-END:variables
}
