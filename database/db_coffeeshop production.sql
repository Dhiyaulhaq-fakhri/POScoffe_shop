-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 18, 2026 at 10:42 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_coffeeshop`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customer_id` int(11) NOT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `nomor_hp` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `join_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `detail_pesanan`
--

CREATE TABLE `detail_pesanan` (
  `id_detail_pesanan` int(11) NOT NULL,
  `id_pesanan` int(11) DEFAULT NULL,
  `id_produk` int(11) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `harga_satuan` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `detail_pesanan`
--

INSERT INTO `detail_pesanan` (`id_detail_pesanan`, `id_pesanan`, `id_produk`, `jumlah`, `harga_satuan`) VALUES
(1, 1, 2, 1, 12000.00),
(2, 1, 3, 1, 18000.00),
(3, 2, 50, 1, 27000.00),
(4, 2, 56, 1, 23000.00),
(5, 3, 4, 3, 25000.00),
(6, 3, 50, 2, 27000.00),
(7, 4, 1, 1, 13000.00),
(8, 4, 46, 3, 18000.00),
(9, 5, 54, 1, 35000.00),
(10, 5, 50, 1, 27000.00),
(11, 5, 51, 15, 23000.00),
(12, 6, 3, 2, 18000.00),
(13, 7, 6, 2, 17000.00),
(14, 8, 4, 2, 25000.00),
(15, 9, 5, 1, 17000.00),
(16, 10, 7, 1, 16000.00),
(17, 11, 8, 2, 17000.00),
(18, 12, 1, 1, 13000.00),
(19, 13, 38, 2, 17000.00),
(20, 13, 1, 1, 13000.00),
(21, 13, 54, 1, 35000.00),
(22, 13, 55, 2, 20000.00),
(23, 14, 35, 4, 16000.00),
(24, 14, 34, 2, 15000.00),
(25, 14, 30, 1, 17000.00),
(26, 14, 46, 2, 18000.00),
(27, 14, 33, 2, 18000.00),
(28, 14, 11, 2, 17000.00),
(29, 15, 24, 1, 18000.00),
(30, 15, 51, 2, 23000.00);

-- --------------------------------------------------------

--
-- Table structure for table `karyawan`
--

CREATE TABLE `karyawan` (
  `id_karyawan` int(11) NOT NULL,
  `username` varchar(14) DEFAULT NULL,
  `password_akun` varchar(14) DEFAULT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `posisi` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `karyawan`
--

INSERT INTO `karyawan` (`id_karyawan`, `username`, `password_akun`, `nama`, `posisi`) VALUES
(1, '1011', '121256', 'veby rokhmatul ambiya', 'owner'),
(2, NULL, NULL, 'sujono wijayanto', 'waiters'),
(3, NULL, NULL, 'alex morgan', 'barista'),
(4, NULL, NULL, 'laila anjani', 'cook'),
(5, 'binary', '6789', 'Dhiyaulhaq fakhri', 'admin'),
(6, '2045', 'pentest', 'hauzan nabil', 'kasir');

-- --------------------------------------------------------

--
-- Table structure for table `pesanan`
--

CREATE TABLE `pesanan` (
  `id_pesanan` int(11) NOT NULL,
  `tanggal` datetime DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `cashier` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pesanan`
--

INSERT INTO `pesanan` (`id_pesanan`, `tanggal`, `total`, `cashier`) VALUES
(1, '2025-11-26 20:29:42', 28500.00, 'hauzan nabil'),
(2, '2025-11-26 20:31:05', 47500.00, 'hauzan nabil'),
(3, '2025-11-26 21:44:26', 122550.00, 'hauzan nabil'),
(4, '2025-11-28 20:00:46', 63650.00, 'hauzan nabil'),
(5, '2025-12-03 12:09:05', 402930.00, 'hauzan nabil'),
(6, '2025-12-03 12:15:13', 34200.00, 'hauzan nabil'),
(7, '2025-12-16 20:14:57', 32300.00, 'hauzan nabil'),
(8, '2026-01-18 12:19:57', 45000.00, 'hauzan nabil'),
(9, '2026-01-18 12:26:23', 16150.00, 'hauzan nabil'),
(10, '2026-01-18 14:19:02', 15520.00, 'hauzan nabil'),
(11, '2026-01-18 14:27:45', 32300.00, 'hauzan nabil'),
(12, '2026-01-18 14:46:02', 12350.00, 'hauzan nabil'),
(13, '2026-01-18 14:50:15', 109800.00, 'hauzan nabil'),
(14, '2026-01-18 14:57:04', 206150.00, 'hauzan nabil'),
(15, '2026-01-18 15:10:48', 60800.00, 'hauzan nabil');

-- --------------------------------------------------------

--
-- Table structure for table `produk`
--

CREATE TABLE `produk` (
  `id_produk` int(11) NOT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `jenis` varchar(20) DEFAULT NULL,
  `harga` decimal(10,2) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produk`
--

INSERT INTO `produk` (`id_produk`, `nama`, `jenis`, `harga`, `stok`) VALUES
(1, 'americano', 'coffee', 13000.00, 197),
(2, 'expresso', 'coffee', 12000.00, 199),
(3, 'butterscot latte', 'coffee', 18000.00, 197),
(4, 'permen kiss', 'permen', 25000.00, 15),
(5, 'caramel latte', 'coffee', 17000.00, 199),
(6, 'coconut latte', 'coffee', 17000.00, 198),
(7, 'expresso machiato', 'coffee', 16000.00, 199),
(8, 'pandan latte', 'coffee', 17000.00, 198),
(9, 'kopi klepon', 'coffee', 23000.00, 200),
(10, 'vanilla latte', 'coffee', 17000.00, 200),
(11, 'halsenut latte', 'coffee', 17000.00, 198),
(12, 'searah moody', 'coffee', 17000.00, 200),
(13, 'pandan aren', 'coffee', 18000.00, 200),
(14, 'v60', 'coffee', 18000.00, 200),
(15, 'japanese', 'coffee', 19000.00, 200),
(16, 'vietnam drip', 'coffee', 16000.00, 200),
(17, 'caffee latte', 'coffee', 16000.00, 200),
(18, 'cappucino', 'coffee', 16000.00, 200),
(19, 'mochacino', 'coffee', 17000.00, 200),
(20, 'surpresso', 'coffee', 15000.00, 200),
(21, 'melon squash', 'non_coffee', 19000.00, 200),
(22, 'lemon squash', 'non_coffee', 19000.00, 200),
(23, 'blue clouds', 'non_coffee', 18000.00, 200),
(24, 'pink berry', 'non_coffee', 18000.00, 199),
(25, 'lemon tea', 'tea', 13000.00, 200),
(26, 'thai tea', 'tea', 15000.00, 200),
(27, 'berry girl', 'non_coffee', 18000.00, 200),
(28, 'blue sky', 'non_coffee', 19000.00, 200),
(29, 'cold man', 'non_coffee', 18000.00, 200),
(30, 'matcha', 'non_coffee', 17000.00, 199),
(31, 'taro', 'non_coffee', 17000.00, 200),
(32, 'mendoan', 'snack', 13000.00, 200),
(33, 'french fries', 'snack', 18000.00, 198),
(34, 'banana crunchy', 'snack', 15000.00, 198),
(35, 'roti bakar', 'snack', 16000.00, 196),
(36, 'chocoberry', 'non_coffee', 17000.00, 200),
(37, 'chocolate', 'non_coffee', 17000.00, 200),
(38, 'red velvet', 'non_coffee', 17000.00, 198),
(39, 'nasi goreng biasa', 'main_course', 17000.00, 200),
(40, 'nasi goreng searah', 'main_course', 22000.00, 200),
(41, 'mie goreng searah', 'main_course', 17000.00, 200),
(42, 'mie rebus searah', 'main_course', 17000.00, 200),
(43, 'rice bowl saus teriaki', 'main_course', 25000.00, 200),
(44, 'rice bowl saus bbq', 'main_course', 25000.00, 200),
(45, 'rice bowl sambal mentai', 'main_course', 22000.00, 200),
(46, 'cireng ayam', 'snack', 18000.00, 195),
(47, 'dimsum original', 'snack', 18000.00, 200),
(48, 'dimsum mentai', 'snack', 20000.00, 200),
(49, 'ayam bakar kremes', 'main_course', 25000.00, 200),
(50, 'dessert brownies', 'main_course', 27000.00, 196),
(51, 'katsu', 'main_course', 23000.00, 183),
(52, 'ayam geprek bakar', 'main_course', 27000.00, 200),
(53, 'spaghetti searah', 'main_course', 25000.00, 200),
(54, 'chicken steak with creamy mushroom sauce', 'main_course', 35000.00, 198),
(55, 'dimsum goreng', 'snack', 20000.00, 198),
(56, 'searah sharing mix platter', 'snack', 23000.00, 199);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `detail_pesanan`
--
ALTER TABLE `detail_pesanan`
  ADD PRIMARY KEY (`id_detail_pesanan`),
  ADD KEY `id_pesanan` (`id_pesanan`),
  ADD KEY `id_produk` (`id_produk`);

--
-- Indexes for table `karyawan`
--
ALTER TABLE `karyawan`
  ADD PRIMARY KEY (`id_karyawan`);

--
-- Indexes for table `pesanan`
--
ALTER TABLE `pesanan`
  ADD PRIMARY KEY (`id_pesanan`);

--
-- Indexes for table `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`id_produk`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `detail_pesanan`
--
ALTER TABLE `detail_pesanan`
  MODIFY `id_detail_pesanan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `pesanan`
--
ALTER TABLE `pesanan`
  MODIFY `id_pesanan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `produk`
--
ALTER TABLE `produk`
  MODIFY `id_produk` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `detail_pesanan`
--
ALTER TABLE `detail_pesanan`
  ADD CONSTRAINT `detail_pesanan_ibfk_1` FOREIGN KEY (`id_pesanan`) REFERENCES `pesanan` (`id_pesanan`),
  ADD CONSTRAINT `detail_pesanan_ibfk_2` FOREIGN KEY (`id_produk`) REFERENCES `produk` (`id_produk`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
