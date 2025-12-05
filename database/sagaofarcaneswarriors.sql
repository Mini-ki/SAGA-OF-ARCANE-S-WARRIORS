-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 05, 2025 at 02:03 PM
-- Server version: 10.4.22-MariaDB
-- PHP Version: 7.3.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sagaofarcaneswarriors`
--

-- --------------------------------------------------------

--
-- Table structure for table `hero`
--

CREATE TABLE `hero` (
  `id_hero` varchar(5) NOT NULL,
  `name_hero` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `monster`
--

CREATE TABLE `monster` (
  `id_monster` varchar(5) NOT NULL,
  `name_monster` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `storymode`
--

CREATE TABLE `storymode` (
  `id_user` varchar(5) NOT NULL,
  `id_hero` varchar(5) NOT NULL,
  `current_hp` int(5) NOT NULL,
  `current_mp` int(5) NOT NULL,
  `duration` time NOT NULL,
  `id_monster` varchar(5) DEFAULT NULL,
  `boss_level` int(1) DEFAULT NULL,
  `item` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `storymode`
--

INSERT INTO `storymode` (`id_user`, `id_hero`, `current_hp`, `current_mp`, `duration`, `id_monster`, `boss_level`, `item`) VALUES
('1', '001', 1, 1, '00:00:00', NULL, 2, NULL),
('1', '001', 1, 1, '00:00:00', NULL, 2, NULL),
('1', '001', 1, 1, '00:00:00', NULL, 2, NULL),
('7186', 'H01', 1991, 700, '00:00:00', 'M01', 2, NULL),
('7186', 'H02', 1200, 10000, '00:00:00', 'M01', 2, NULL),
('1207', 'H01', 2000, 1000, '00:00:00', 'M01', 1, 1),
('1207', 'H02', 1200, 10000, '00:00:00', 'M01', 1, 1),
('1207', 'H01', 1375, 1000, '00:00:50', 'M01', 2, 0),
('1207', 'H02', 1200, 10000, '00:00:50', 'M01', 2, 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id_users` varchar(5) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id_users`, `username`, `password`) VALUES
('1207', 'Jyee', 'Jyee123'),
('7186', '2', '2'),
('8874', '1', '1');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_users`),
  ADD UNIQUE KEY `username` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
