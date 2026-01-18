
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.plot.PlotOrientation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.HeadlessException;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Lenovo
 */
public class lamanpemilik extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(lamanpemilik.class.getName());

    private JFreeChart chart;
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private String username;
    private String nama;
    private Connection conn;

    /**
     * Creates new form lamanpemilik
     */
    public lamanpemilik() {
        initComponents();
        setLocationRelativeTo(null);

        //    Test
        dataset.addValue(800, "Transaksi", "Januari");
        dataset.addValue(900, "Transaksi", "Februari");
        dataset.addValue(700, "Transaksi", "Maret");
        dataset.addValue(650, "Transaksi", "April");
        dataset.addValue(750, "Transaksi", "Mei");
        dataset.addValue(780, "Transaksi", "Juni");
        dataset.addValue(690, "Transaksi", "Juli");
        dataset.addValue(880, "Transaksi", "Agustus");
        dataset.addValue(890, "Transaksi", "September");
        dataset.addValue(820, "Transaksi", "Oktober");
        dataset.addValue(830, "Transaksi", "November");
        dataset.addValue(960, "Transaksi", "Desember");

        chart = ChartFactory.createBarChart(
                "Transaksi Bulanan",
                "Bulan",
                "Jumlah",
                dataset
        );

        // Ambil plot
        CategoryPlot plot = chart.getCategoryPlot();

        // Ambil renderer
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Set warna bar (biru)
        renderer.setSeriesPaint(0, new Color(52, 152, 219)); // biru soft
        // renderer.setSeriesPaint(0, Color.BLUE); // biru standar

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(648, 154));
        chartPanel.setMouseWheelEnabled(false);
        chartPanel.setPopupMenu(null);

        panelChart.removeAll();
        panelChart.setLayout(
                new BorderLayout());
        panelChart.add(chartPanel, BorderLayout.CENTER);
        panelChart.revalidate();
        panelChart.repaint();
        //        END TEST
    }

    public lamanpemilik(String username, String nama) {
        initComponents();
        setLocationRelativeTo(null);
        try {
            conn = koneksi.getConnection();   // ==== WAJIB ====
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtnamapemilik.setText(nama);

        loadProfileImage();
    }

    private void loadProfileImage() {
        java.net.URL imgURL = getClass().getResource("/images/Userpfpconvert.jpg");
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            // Resize gambar agar pas dengan tombol
            Image scaled = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            btnpfp3.setIcon(new ImageIcon(scaled));
        } else {
            System.out.println("❌ Gambar tidak ditemukan di folder /images/");
        }
    }

    private String potongNama(String nama) {
        return nama.length() > 18 ? nama.substring(0, 18) + "..." : nama;
    }

    private void loadProdukTerlaris(int bulan, int tahun) {

        String sql = """
        SELECT pr.nama AS nama_produk, SUM(dp.jumlah) AS total_terjual
        FROM detail_pesanan dp
        JOIN pesanan p ON dp.id_pesanan = p.id_pesanan
        JOIN produk pr ON dp.id_produk = pr.id_produk
        WHERE MONTH(p.tanggal) = ?
          AND YEAR(p.tanggal) = ?
        GROUP BY dp.id_produk
        ORDER BY total_terjual DESC
        LIMIT 1
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bulan);
            ps.setInt(2, tahun);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtprodukterlaris.setText(
                        potongNama(rs.getString("nama_produk"))
                );
                txtprodukterlaristtltrnsksi.setText(
                        String.valueOf(rs.getInt("total_terjual"))
                );
            } else {
                txtprodukterlaris.setText("-");
                txtprodukterlaristtltrnsksi.setText("0 item");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error produk terlaris:\n" + e.getMessage()
            );
        }

    }

    private void loadTotalProdukTerjual(int bulan, int tahun) {

        String sql = """
        SELECT IFNULL(SUM(dp.jumlah), 0) AS total_terjual
        FROM detail_pesanan dp
        JOIN pesanan p ON dp.id_pesanan = p.id_pesanan
        WHERE MONTH(p.tanggal) = ?
        AND YEAR(p.tanggal) = ?;
    """;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bulan);
            ps.setInt(2, tahun);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txttotalprodukterjual.setText(
                        String.valueOf(rs.getInt("total_terjual"))
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat total produk terjual\n" + e.getMessage());
        }
    }

    private void loadPendapatan(int bulan, int tahun) {

        String sql = """
        SELECT IFNULL(SUM(total), 0) AS pendapatan
        FROM pesanan
        WHERE MONTH(tanggal) = ?
        AND YEAR(tanggal) = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bulan);
            ps.setInt(2, tahun);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtpendapatankotor.setText(
                        String.valueOf(rs.getDouble("pendapatan"))
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat pendapatan\n" + e.getMessage());
        }
    }

    private String formatAngka(double angka) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(angka);
    }

    private void loadPerformaKasir(int bulan, int tahun) {

        String sql = """
        SELECT 
            p.cashier,
            COUNT(p.id_pesanan) AS total_transaksi,
            SUM(p.total) AS pendapatan
        FROM pesanan p
        WHERE MONTH(p.tanggal) = ?
          AND YEAR(p.tanggal) = ?
        GROUP BY p.cashier
        ORDER BY total_transaksi DESC
        LIMIT 1
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bulan);
            ps.setInt(2, tahun);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtkasirterbaik.setText(rs.getString("cashier"));
                txtmelakukantransaksi.setText(
                        String.valueOf(rs.getInt("total_transaksi"))
                );

//            // Pendapatan (pakai formatter)
//            txtpendapatankotor.setText(
//                formatAngka(rs.getDouble("pendapatan"))
//            );
            } else {
                txtkasirterbaik.setText("-");
                txtmelakukantransaksi.setText("0");
                txtpendapatankotor.setText("0");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error performa kasir:\n" + e.getMessage()
            );
        }
    }

    private void loadChartPenjualanTahunan(int tahun) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String[] bulan = {
            "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
            "Jul", "Agu", "Sep", "Okt", "Nov", "Des"
        };

        for (String b : bulan) {
            dataset.addValue(0, "Transaksi", b);
        }

        String sql = """
        SELECT 
            MONTH(tanggal) AS bulan,
            COUNT(id_pesanan) AS total
        FROM pesanan
        WHERE YEAR(tanggal) = ?
        GROUP BY MONTH(tanggal)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tahun);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int bln = rs.getInt("bulan");
                int total = rs.getInt("total");

                dataset.setValue(
                        total,
                        "Transaksi",
                        bulan[bln - 1]
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error load chart:\n" + e.getMessage()
            );
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Penjualan Tahun " + tahun,
                "Bulan",
                "Jumlah Transaksi",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        // STYLE CHART
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        renderer.setSeriesPaint(0, new Color(52, 152, 219));
        renderer.setShadowVisible(false);

        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setOutlineVisible(false);

        // TAMPILKAN
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(panelChart.getSize());
        chartPanel.setMouseWheelEnabled(false);
        chartPanel.setPopupMenu(null);

        panelChart.removeAll();
        panelChart.setLayout(new BorderLayout());
        panelChart.add(chartPanel, BorderLayout.CENTER);
        panelChart.revalidate();
        panelChart.repaint();

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
        btnpfp3 = new javax.swing.JButton();
        txtnamapemilik = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtbulanpdpt = new javax.swing.JTextField();
        txttahunpdpt = new javax.swing.JTextField();
        btncaripendapatan = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtprodukterlaris = new javax.swing.JTextField();
        txtprodukterlaristtltrnsksi = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txttotalprodukterjual = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtpendapatankotor = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtkasirterbaik = new javax.swing.JTextField();
        txtmelakukantransaksi = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        panelChart = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 204, 255));

        btnpfp3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpfp3ActionPerformed(evt);
            }
        });

        txtnamapemilik.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtnamapemilik, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnpfp3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnpfp3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(txtnamapemilik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel1.setText("Masukan periode :");

        txtbulanpdpt.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txtbulanpdpt.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txttahunpdpt.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        txttahunpdpt.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btncaripendapatan.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        btncaripendapatan.setText("Cari");
        btncaripendapatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncaripendapatanActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(102, 204, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Produk terlaris");

        txtprodukterlaris.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtprodukterlaris.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtprodukterlaristtltrnsksi.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtprodukterlaristtltrnsksi.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Transaksi");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtprodukterlaris, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtprodukterlaristtltrnsksi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtprodukterlaris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtprodukterlaristtltrnsksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(75, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(102, 204, 255));

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel4.setText("Produk terjual");

        txttotalprodukterjual.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Item terjual");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel4))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(txttotalprodukterjual, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel5)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txttotalprodukterjual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(102, 204, 255));

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Pendapatan");

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel7.setText("Rp.");

        txtpendapatankotor.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 32, Short.MAX_VALUE))
                    .addComponent(txtpendapatankotor))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtpendapatankotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(102, 204, 255));

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel8.setText("Performa kasir");

        txtkasirterbaik.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtkasirterbaik.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtmelakukantransaksi.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        txtmelakukantransaksi.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel9.setText("Transaksi");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel8))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtkasirterbaik, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(txtmelakukantransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtkasirterbaik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtmelakukantransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(0, 204, 255));

        panelChart.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelChartLayout = new javax.swing.GroupLayout(panelChart);
        panelChart.setLayout(panelChartLayout);
        panelChartLayout.setHorizontalGroup(
            panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelChartLayout.setVerticalGroup(
            panelChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 154, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(12, 12, 12)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtbulanpdpt)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txttahunpdpt)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btncaripendapatan)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(67, 67, 67))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtbulanpdpt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txttahunpdpt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncaripendapatan))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btncaripendapatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncaripendapatanActionPerformed
        // TODO add your handling code here:
        int bulan = Integer.parseInt(txtbulanpdpt.getText()); // contoh: 8
        int tahun = Integer.parseInt(txttahunpdpt.getText()); // contoh: 2025

        loadProdukTerlaris(bulan, tahun);
        loadTotalProdukTerjual(bulan, tahun);
        loadPendapatan(bulan, tahun);
        loadPerformaKasir(bulan, tahun);
        loadChartPenjualanTahunan(tahun);
    }//GEN-LAST:event_btncaripendapatanActionPerformed

    private profiloption profilForm;

    private void btnpfp3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpfp3ActionPerformed
        // TODO add your handling code here:
        if (profilForm == null || !profilForm.isDisplayable()) {
            profilForm = new profiloption(username, nama);
        }
        profilForm.setVisible(true);
    }//GEN-LAST:event_btnpfp3ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new lamanpemilik().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncaripendapatan;
    private javax.swing.JButton btnpfp3;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel panelChart;
    private javax.swing.JTextField txtbulanpdpt;
    private javax.swing.JTextField txtkasirterbaik;
    private javax.swing.JTextField txtmelakukantransaksi;
    private javax.swing.JTextField txtnamapemilik;
    private javax.swing.JTextField txtpendapatankotor;
    private javax.swing.JTextField txtprodukterlaris;
    private javax.swing.JTextField txtprodukterlaristtltrnsksi;
    private javax.swing.JTextField txttahunpdpt;
    private javax.swing.JTextField txttotalprodukterjual;
    // End of variables declaration//GEN-END:variables
}
