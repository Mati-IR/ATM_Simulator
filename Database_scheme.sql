-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Cze 01, 2023 at 06:35 PM
-- Wersja serwera: 10.4.28-MariaDB
-- Wersja PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `atm`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `account_info`
--

CREATE TABLE `account_info` (
  `Account_Id` int(11) NOT NULL,
  `Balance` int(11) NOT NULL,
  `Pin` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `account_info`
--

INSERT INTO `account_info` (`Account_Id`, `Balance`, `Pin`) VALUES
(1, 1000, 2137);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `operation_info`
--

CREATE TABLE `operation_info` (
  `ID` int(11) NOT NULL,
  `Date` datetime NOT NULL,
  `Operation_Type` int(11) NOT NULL,
  `Amount` int(11) DEFAULT NULL,
  `Account_Id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `operation_info`
--

INSERT INTO `operation_info` (`ID`, `Date`, `Operation_Type`, `Amount`, `Account_Id`) VALUES
(2, '2023-06-01 18:26:45', 4, NULL, 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `operation_type`
--

CREATE TABLE `operation_type` (
  `ID` int(11) NOT NULL,
  `Operation_Name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `operation_type`
--

INSERT INTO `operation_type` (`ID`, `Operation_Name`) VALUES
(1, 'Cash_Out'),
(2, 'Cash_In'),
(3, 'Cash_Out_Euro'),
(4, 'Account_Check'),
(5, 'Pin_Change'),
(6, 'Phone_add');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `account_info`
--
ALTER TABLE `account_info`
  ADD PRIMARY KEY (`Account_Id`);

--
-- Indeksy dla tabeli `operation_info`
--
ALTER TABLE `operation_info`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Account_Operation` (`Account_Id`),
  ADD KEY `Info_Type` (`Operation_Type`);

--
-- Indeksy dla tabeli `operation_type`
--
ALTER TABLE `operation_type`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `operation_info`
--
ALTER TABLE `operation_info`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `operation_info`
--
ALTER TABLE `operation_info`
  ADD CONSTRAINT `Account_Operation` FOREIGN KEY (`Account_Id`) REFERENCES `account_info` (`Account_Id`),
  ADD CONSTRAINT `Info_Type` FOREIGN KEY (`Operation_Type`) REFERENCES `operation_type` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
