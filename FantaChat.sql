USE [master]
GO
/****** Object:  Database [FantaChat]    Script Date: 07/28/2020 10:25:34 AM ******/
CREATE DATABASE [FantaChat]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'FantaChat', FILENAME = N'D:\SLQ2019\MSSQL15.MSSQLSERVER\MSSQL\DATA\FantaChat.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'FantaChat_log', FILENAME = N'D:\SLQ2019\MSSQL15.MSSQLSERVER\MSSQL\DATA\FantaChat_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [FantaChat] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [FantaChat].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [FantaChat] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [FantaChat] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [FantaChat] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [FantaChat] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [FantaChat] SET ARITHABORT OFF 
GO
ALTER DATABASE [FantaChat] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [FantaChat] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [FantaChat] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [FantaChat] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [FantaChat] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [FantaChat] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [FantaChat] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [FantaChat] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [FantaChat] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [FantaChat] SET  DISABLE_BROKER 
GO
ALTER DATABASE [FantaChat] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [FantaChat] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [FantaChat] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [FantaChat] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [FantaChat] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [FantaChat] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [FantaChat] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [FantaChat] SET RECOVERY FULL 
GO
ALTER DATABASE [FantaChat] SET  MULTI_USER 
GO
ALTER DATABASE [FantaChat] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [FantaChat] SET DB_CHAINING OFF 
GO
ALTER DATABASE [FantaChat] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [FantaChat] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [FantaChat] SET DELAYED_DURABILITY = DISABLED 
GO
EXEC sys.sp_db_vardecimal_storage_format N'FantaChat', N'ON'
GO
ALTER DATABASE [FantaChat] SET QUERY_STORE = OFF
GO
USE [FantaChat]
GO
/****** Object:  Table [dbo].[Accounts]    Script Date: 07/28/2020 10:25:34 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Accounts](
	[UserName] [nvarchar](50) NOT NULL,
	[Password] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_Accounts] PRIMARY KEY CLUSTERED 
(
	[UserName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MessageHistory]    Script Date: 07/28/2020 10:25:34 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MessageHistory](
	[UserName] [nvarchar](50) NOT NULL,
	[Message] [nvarchar](max) NOT NULL,
	[Time] [nvarchar](max) NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PrivateMessageHistory]    Script Date: 07/28/2020 10:25:34 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PrivateMessageHistory](
	[NameSend] [nvarchar](50) NOT NULL,
	[Message] [nvarchar](max) NOT NULL,
	[NameReceive] [nvarchar](50) NOT NULL,
	[Time] [nvarchar](max) NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
INSERT [dbo].[Accounts] ([UserName], [Password]) VALUES (N'Fanta', N'12345678')
INSERT [dbo].[Accounts] ([UserName], [Password]) VALUES (N'Luffy', N'12345678')
INSERT [dbo].[Accounts] ([UserName], [Password]) VALUES (N'Zoro', N'12345678')
GO
INSERT [dbo].[MessageHistory] ([UserName], [Message], [Time]) VALUES (N'Fanta', N' Hello', N'28-07-2020 10:02:17')
INSERT [dbo].[MessageHistory] ([UserName], [Message], [Time]) VALUES (N'Zoro', N' Hiii', N'28-07-2020 10:02:20')
INSERT [dbo].[MessageHistory] ([UserName], [Message], [Time]) VALUES (N'Luffy', N' Hey bro', N'28-07-2020 10:02:24')
GO
INSERT [dbo].[PrivateMessageHistory] ([NameSend], [Message], [NameReceive], [Time]) VALUES (N'Fanta', N' Hi', N'Luffy', N'28-07-2020 10:06:38')
GO
USE [master]
GO
ALTER DATABASE [FantaChat] SET  READ_WRITE 
GO
