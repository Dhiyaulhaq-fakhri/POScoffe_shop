/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Lenovo
 */
import javax.swing.ImageIcon;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.HeadlessException;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class halamanutama extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(halamanutama.class.getName());

    /**
     * Creates new form halamanutama
     */
    private String username;
    private String nama;
    private Connection conn;
    DefaultTableModel model;


    public halamanutama() {
        initComponents();
        // Tampilkan form di tengah layar
        setLocationRelativeTo(null);

        try {
            conn = koneksi.getConnection();   // ==== WAJIB ====
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadProfileImage();
        tampilkanNomorOrder();
        setTanggalDenganJam();

        // === Tambahkan gambar profil ke tombol ===
    }

    public halamanutama(String username, String nama) {
        initComponents();
        setLocationRelativeTo(null);
        try {
            conn = koneksi.getConnection();   // ==== WAJIB ====
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tampilkanNomorOrder();
        setTanggalDenganJam();

        model = new DefaultTableModel();
        model.addColumn("Kode");
        model.addColumn("Nama Barang");
        model.addColumn("Jumlah");
        model.addColumn("Harga Satuan");
        model.addColumn("Total");

        // Set model ke JTable kamu
        jTabelpembelian.setModel(model);

        jTabelpembelian.setRowHeight(25);

        // Atur lebar tiap kolom
        TableColumnModel columnModel = jTabelpembelian.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);   // Kode
        columnModel.getColumn(1).setPreferredWidth(200);  // Nama Barang
        columnModel.getColumn(2).setPreferredWidth(70);   // Jumlah
        columnModel.getColumn(3).setPreferredWidth(100);  // Harga Satuan
        columnModel.getColumn(4).setPreferredWidth(120);  // Total

        this.username = username;
        this.nama = nama;

        txtdisplaynama.setText(nama);
        txtdisplaynama.setEditable(false);

        txtkasir.setText(nama);
        txtkasir.setEditable(false);

        // textfield tidak boleh di edit
        txtnoorder.setEditable(false);
        txttanggal.setEditable(false);
        txtsubtotal.setEditable(false);
        txttotalakhir.setEditable(false);
        txtkembalian.setEditable(false);
        txtnamabarang.setEditable(false);
        txtstok.setEditable(false);
        txthargasatuan.setEditable(false);

        txtdiskon.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitungTotalAkhir();
            }
        });

        txtbayar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitungKembalian();
            }
        });

        // Tampilkan juga foto profil agar tidak hilang
        loadProfileImage();
    }

    private void loadProfileImage() {
        java.net.URL imgURL = getClass().getResource("/images/Userpfpconvert.jpg");
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            // Resize gambar agar pas dengan tombol
            Image scaled = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            btnprofil.setIcon(new ImageIcon(scaled));
        } else {
            System.out.println("❌ Gambar tidak ditemukan di folder /images/");
        }
    }

    public String generateNomorNota() {
        String nomorBaru = "";

        String sql = "SELECT id_pesanan FROM pesanan "
                + "WHERE DATE(tanggal) = CURDATE() "
                + "ORDER BY id_pesanan DESC LIMIT 1";

        try (Connection conn = koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            // Format tanggal: YYYYMMDD
            String today = java.time.LocalDate.now().toString().replace("-", "");

            int urutan = 1;

            if (rs.next()) {
                int idTerakhir = rs.getInt("id_pesanan");
                urutan = idTerakhir + 1;
            }

            nomorBaru = today + "-" + String.format("%03d", urutan);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nomorBaru;
    }

    public void tampilkanNomorOrder() {
        String nomor = generateNomorNota();
        txtnoorder.setText(nomor);
    }

    private void cariBarang() {
        String kode = txtkodebarang.getText().trim();
        if (kode.isEmpty()) {
            return; // kalau kosong, tidak usah query
        }

        try {
            String sql = "SELECT nama, jenis, harga, stok FROM produk WHERE id_produk = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtnamabarang.setText(rs.getString("nama"));
                txtstok.setText(rs.getString("stok"));
                txthargasatuan.setText(rs.getString("harga"));
            } else {
                // kalau kode tidak ditemukan
                txtnamabarang.setText("");
                txtstok.setText("");
                txthargasatuan.setText("");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error : " + e.getMessage());
        }
    }

    private void setTanggalDenganJam() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        txttanggal.setText(now.format(format));
    }

    private void hitungPratinjauTotal() {
        try {
            String hargaText = txthargasatuan.getText().trim();
            String jumlahText = txtjumlahjual.getText().trim();

            if (hargaText.isEmpty() || jumlahText.isEmpty()) {
                txthargaakhir.setText("");
                return;
            }

            double harga = Double.parseDouble(hargaText);
            int jumlah = Integer.parseInt(jumlahText);

            double total = harga * jumlah;

            txthargaakhir.setText(String.valueOf(total));

        } catch (NumberFormatException e) {
            txthargaakhir.setText("0");
        }
    }

    private void hitungSubtotal() {
        int rowCount = jTabelpembelian.getRowCount();
        double subtotal = 0;

        for (int i = 0; i < rowCount; i++) {
            Object nilai = jTabelpembelian.getValueAt(i, 4); // kolom total harga
            if (nilai != null) {
                subtotal += Double.parseDouble(nilai.toString());
            }
        }

        txtsubtotal.setText(String.valueOf((int) subtotal));
    }

    private void hitungTotalAkhir() {
        double subtotal = 0;
        double diskonPersen = 0;

        // Ambil subtotal
        if (!txtsubtotal.getText().isEmpty()) {
            subtotal = Double.parseDouble(txtsubtotal.getText());
        }

        // Ambil diskon (jika kosong, tetap 0)
        if (!txtdiskon.getText().isEmpty()) {
            diskonPersen = Double.parseDouble(txtdiskon.getText());
        }

        // Hitung potongan
        double potongan = subtotal * (diskonPersen / 100);

        // Hitung total akhir
        double totalAkhir = subtotal - potongan;

        // Tampilkan
        txttotalakhir.setText(String.valueOf((int) totalAkhir));
    }

    private void hitungKembalian() {
        try {
            double totalAkhir = Double.parseDouble(txttotalakhir.getText());
            double uangPembeli = Double.parseDouble(txtbayar.getText());

            double kembalian = uangPembeli - totalAkhir;

            // kalau negatif jadikan 0 saja biar rapi
            if (kembalian < 0) {
                txtkembalian.setText("0");
            } else {
                txtkembalian.setText(String.valueOf(kembalian));
            }

        } catch (Exception e) {
            // kalau textfield kosong atau bukan angka
            txtkembalian.setText("0");
        }
    }

    private void checkout() {
        Connection conn = null;
        PreparedStatement pstPesanan = null;
        PreparedStatement pstDetail = null;

        try {
            conn = koneksi.getConnection();
            conn.setAutoCommit(false);

            // Ambil data penting
            double totalAkhir = Double.parseDouble(txttotalakhir.getText());
            String kasir = txtkasir.getText();
            String nomorNota = txtnoorder.getText(); // nomor nota buatanmu (tanggal + jam)

            // ============================
            // 1. INSERT PESANAN
            // ============================
            String sqlPesanan = "INSERT INTO pesanan (tanggal, total, cashier) VALUES (NOW(), ?, ?)";
            pstPesanan = conn.prepareStatement(sqlPesanan, Statement.RETURN_GENERATED_KEYS);

            pstPesanan.setDouble(1, totalAkhir);
            pstPesanan.setString(2, kasir);
            pstPesanan.executeUpdate();

            // Ambil id_pesanan (PRIMARY KEY)
            ResultSet rs = pstPesanan.getGeneratedKeys();
            int idPesanan = 0;
            if (rs.next()) {
                idPesanan = rs.getInt(1);
            }

            // ============================
            // 2. INSERT DETAIL PESANAN
            // ============================
            String sqlDetail = "INSERT INTO detail_pesanan (id_pesanan, id_produk, jumlah, harga_satuan) VALUES (?, ?, ?, ?)";
            pstDetail = conn.prepareStatement(sqlDetail);

            DefaultTableModel model = (DefaultTableModel) jTabelpembelian.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {
                pstDetail.setInt(1, idPesanan);
                pstDetail.setInt(2, Integer.parseInt(model.getValueAt(i, 0).toString()));
                pstDetail.setInt(3, Integer.parseInt(model.getValueAt(i, 2).toString()));
                pstDetail.setDouble(4, Double.parseDouble(model.getValueAt(i, 3).toString()));
                pstDetail.addBatch();
            }

            pstDetail.executeBatch();

            // ============================
            // 3. UPDATE STOK PRODUK
            // ============================
            for (int i = 0; i < model.getRowCount(); i++) {

                int idProduk = Integer.parseInt(model.getValueAt(i, 0).toString());
                int jumlah = Integer.parseInt(model.getValueAt(i, 2).toString());

                String sqlStok = "UPDATE produk SET stok = stok - ? WHERE id_produk = ?";
                PreparedStatement pstStok = conn.prepareStatement(sqlStok);
                pstStok.setInt(1, jumlah);
                pstStok.setInt(2, idProduk);
                pstStok.executeUpdate();
            }

            // ============================
            // 4. COMMIT
            // ============================
            conn.commit();

            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");

            // ============================
            // 5. BUKA HALAMAN NOTA
            // ============================
            halamannota nota = new halamannota(
                    idPesanan, // penting untuk load detail nota
                    nomorNota, // nomor nota buatanmu
                    kasir, // nama kasir
                    totalAkhir // total pembayaran
            );

            nota.setVisible(true);

            // ============================
            // 6. BERSIHKAN FORM
            // ============================
            model.setRowCount(0);
            txttotalakhir.setText("");
            txtnoorder.setText("");  // boleh clear setelah dipakai

        } catch (Exception e) {

            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
            }

            JOptionPane.showMessageDialog(this, "Checkout gagal: " + e.getMessage());
        }
    }

    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) jTabelpembelian.getModel();
        model.setRowCount(0);
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
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtnoorder = new javax.swing.JTextField();
        txtpelanggan = new javax.swing.JTextField();
        txttanggal = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txtdisplaynama = new javax.swing.JTextField();
        btnprofil = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtsubtotal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtdiskon = new javax.swing.JTextField();
        txttotalakhir = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtbayar = new javax.swing.JTextField();
        txtkembalian = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButtonselesai = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtkodebarang = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtnamabarang = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtstok = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txthargasatuan = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtjumlahjual = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txthargaakhir = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtkasir = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jButtonsimpan = new javax.swing.JButton();
        jButtonbatal = new javax.swing.JButton();
        jButtonreset = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabelpembelian = new javax.swing.JTable();
        jButtonhapusitem = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 153, 0));
        setPreferredSize(new java.awt.Dimension(1000, 720));

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 51, 0)));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel7.setText("No.Order");

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel8.setText("Pelanggan");

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel9.setText("Tanggal");

        txtnoorder.setBackground(new java.awt.Color(153, 153, 153));
        txtnoorder.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtnoorder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnoorderActionPerformed(evt);
            }
        });

        txtpelanggan.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        txttanggal.setBackground(new java.awt.Color(153, 153, 153));
        txttanggal.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txttanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttanggalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtnoorder)
                    .addComponent(txtpelanggan)
                    .addComponent(txttanggal, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
                .addContainerGap(127, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtnoorder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtpelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txttanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58))
        );

        jPanel4.setBackground(new java.awt.Color(102, 102, 255));

        txtdisplaynama.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txtdisplaynama.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtdisplaynama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdisplaynamaActionPerformed(evt);
            }
        });

        btnprofil.setBorderPainted(false);
        btnprofil.setContentAreaFilled(false);
        btnprofil.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnprofil.setFocusPainted(false);
        btnprofil.setPreferredSize(new java.awt.Dimension(64, 64));
        btnprofil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprofilActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtdisplaynama, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnprofil, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtdisplaynama, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnprofil, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Total : ");

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Sub Total :");

        txtsubtotal.setBackground(new java.awt.Color(153, 153, 153));
        txtsubtotal.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtsubtotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsubtotalActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Diskon :");

        txtdiskon.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        txtdiskon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdiskonActionPerformed(evt);
            }
        });

        txttotalakhir.setBackground(new java.awt.Color(153, 153, 153));
        txttotalakhir.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        txttotalakhir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalakhirActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(51, 0, 0));

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Bayar :");

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Kembalian :");

        txtbayar.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N

        txtkembalian.setBackground(new java.awt.Color(153, 153, 153));
        txtkembalian.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
        jLabel6.setText("%");

        jButtonselesai.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButtonselesai.setText("Selesai");
        jButtonselesai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonselesaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtsubtotal)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txttotalakhir, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                                .addGap(2, 2, 2))
                            .addComponent(txtdiskon))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtbayar, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtkembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jButtonselesai)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtsubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtdiskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addGap(8, 8, 8)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txttotalakhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtbayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtkembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonselesai))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel10.setText("Kode Barang");

        txtkodebarang.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txtkodebarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtkodebarangKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel11.setText("Nama Barang");

        txtnamabarang.setBackground(new java.awt.Color(153, 153, 153));
        txtnamabarang.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel12.setText("Stok");

        txtstok.setBackground(new java.awt.Color(153, 153, 153));
        txtstok.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel13.setText("Harga Satuan");

        txthargasatuan.setBackground(new java.awt.Color(153, 153, 153));
        txthargasatuan.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel14.setText("Jumlah Jual");

        txtjumlahjual.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txtjumlahjual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtjumlahjualActionPerformed(evt);
            }
        });
        txtjumlahjual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtjumlahjualKeyReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel15.setText("Harga Akhir");

        txthargaakhir.setBackground(new java.awt.Color(153, 153, 153));
        txthargaakhir.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        jLabel16.setFont(new java.awt.Font("SansSerif", 2, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel16.setText("Kasir :");

        txtkasir.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel17.setFont(new java.awt.Font("SansSerif", 2, 18)); // NOI18N
        jLabel17.setText("Rp");

        jLabel18.setFont(new java.awt.Font("SansSerif", 2, 18)); // NOI18N
        jLabel18.setText("Rp");

        jButtonsimpan.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButtonsimpan.setText("Simpan");
        jButtonsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonsimpanActionPerformed(evt);
            }
        });

        jButtonbatal.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButtonbatal.setText("Batal");

        jButtonreset.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButtonreset.setText("Reset");
        jButtonreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonresetActionPerformed(evt);
            }
        });

        jTabelpembelian.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTabelpembelian);

        jButtonhapusitem.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButtonhapusitem.setText("Hapus Item");
        jButtonhapusitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonhapusitemActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jButton1.setText("Pendapatan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(txtkodebarang, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtnamabarang, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(100, 100, 100)
                                        .addComponent(jLabel11)
                                        .addGap(41, 41, 41)))
                                .addGap(55, 55, 55)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtstok, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txthargasatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(575, 575, 575)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtkasir, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtjumlahjual)
                    .addComponent(jLabel14)
                    .addComponent(jButtonsimpan, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txthargaakhir, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButtonbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonreset, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 21, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1056, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonhapusitem, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtkodebarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnamabarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtstok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txthargasatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtjumlahjual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txthargaakhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtkasir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonsimpan)
                    .addComponent(jButtonbatal)
                    .addComponent(jButtonreset))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(jButtonhapusitem)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(314, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(260, 260, 260))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 815, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(259, 259, 259))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private profiloption profilForm;

    private void btnprofilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprofilActionPerformed
        // TODO add your handling code here:
        if (profilForm == null || !profilForm.isDisplayable()) {
            profilForm = new profiloption(username, nama);
        }
        profilForm.setVisible(true);
    }//GEN-LAST:event_btnprofilActionPerformed

    private void txtdisplaynamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdisplaynamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdisplaynamaActionPerformed

    private void txtnoorderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnoorderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnoorderActionPerformed

    private void txttanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttanggalActionPerformed

    private void txtsubtotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsubtotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtsubtotalActionPerformed

    private void txttotalakhirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalakhirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalakhirActionPerformed

    private void jButtonselesaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonselesaiActionPerformed
        // TODO add your handling code here:
        checkout();
    }//GEN-LAST:event_jButtonselesaiActionPerformed

    private void txtkodebarangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtkodebarangKeyReleased
        // TODO add your handling code here:
        cariBarang();
    }//GEN-LAST:event_txtkodebarangKeyReleased

    private void jButtonsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonsimpanActionPerformed
        // TODO add your handling code here:                                         
        String kode = txtkodebarang.getText();
        String nama = txtnamabarang.getText();
        if (txtjumlahjual.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Quantity belum diisi!");
            return; // stop supaya tidak error
        }
        int jumlah = Integer.parseInt(txtjumlahjual.getText());
        double harga = Double.parseDouble(txthargasatuan.getText());
        double total = jumlah * harga;

        // Tambah baris baru
        model.addRow(new Object[]{
            kode,
            nama,
            jumlah,
            harga,
            total
        });

        hitungSubtotal();

    }//GEN-LAST:event_jButtonsimpanActionPerformed

    private void jButtonhapusitemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonhapusitemActionPerformed
        // TODO add your handling code here:
        int selectedrow = jTabelpembelian.getSelectedRow();

        if (selectedrow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih item yang mau dihapus!");
            return;
        }

        // Konfirmasi
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Hapus item yang dipilih?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(selectedrow);
            hitungSubtotal();

        }
    }//GEN-LAST:event_jButtonhapusitemActionPerformed

    private void jButtonresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonresetActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Hapus semua item dari tabel?",
                "konfirmasi reset",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            model = new DefaultTableModel();
            model.addColumn("Kode");
            model.addColumn("Nama Barang");
            model.addColumn("Jumlah");
            model.addColumn("Harga Satuan");
            model.addColumn("Total");

            jTabelpembelian.setModel(model);

            hitungSubtotal();

        }
    }//GEN-LAST:event_jButtonresetActionPerformed

    private void txtjumlahjualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtjumlahjualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtjumlahjualActionPerformed

    private void txtjumlahjualKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtjumlahjualKeyReleased
        // TODO add your handling code here:
        hitungPratinjauTotal();
    }//GEN-LAST:event_txtjumlahjualKeyReleased

    private void txtdiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdiskonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdiskonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        halamanpendapatanperhari pendaphar = new halamanpendapatanperhari();
        pendaphar.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new halamanutama().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnprofil;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonbatal;
    private javax.swing.JButton jButtonhapusitem;
    private javax.swing.JButton jButtonreset;
    private javax.swing.JButton jButtonselesai;
    private javax.swing.JButton jButtonsimpan;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTabelpembelian;
    private javax.swing.JTextField txtbayar;
    private javax.swing.JTextField txtdiskon;
    private javax.swing.JTextField txtdisplaynama;
    private javax.swing.JTextField txthargaakhir;
    private javax.swing.JTextField txthargasatuan;
    private javax.swing.JTextField txtjumlahjual;
    private javax.swing.JTextField txtkasir;
    private javax.swing.JTextField txtkembalian;
    private javax.swing.JTextField txtkodebarang;
    private javax.swing.JTextField txtnamabarang;
    private javax.swing.JTextField txtnoorder;
    private javax.swing.JTextField txtpelanggan;
    private javax.swing.JTextField txtstok;
    private javax.swing.JTextField txtsubtotal;
    private javax.swing.JTextField txttanggal;
    private javax.swing.JTextField txttotalakhir;
    // End of variables declaration//GEN-END:variables
}
