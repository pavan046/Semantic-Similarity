-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 20, 2012 at 04:26 PM
-- Server version: 5.5.25
-- PHP Version: 5.4.4

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `tagsim`
--

-- --------------------------------------------------------

--
-- Table structure for table `entity_tag`
--

CREATE TABLE `entity_tag` (
  `hashtag` varchar(150) NOT NULL,
  `tweetId` varchar(400) NOT NULL,
  `entity` varchar(400) NOT NULL,
  `datetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hashtag_analytics`
--

CREATE TABLE `hashtag_analytics` (
  `frequency` double NOT NULL,
  `specificity` double NOT NULL,
  `consistency` double NOT NULL,
  `number_users_distinct` int(11) NOT NULL,
  `number_tweets` int(11) NOT NULL,
  `number_retweets` int(11) NOT NULL,
  `hashtag` varchar(150) NOT NULL,
  `topic_cosine_similarity` double NOT NULL,
  `topic_subsumtion_similarity` double NOT NULL,
  `eventID` varchar(100) NOT NULL,
  `time_last_analyzed` datetime NOT NULL,
  PRIMARY KEY (`hashtag`,`eventID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Analysis of the max last 1500 tweets through twitter seach';

-- --------------------------------------------------------

--
-- Table structure for table `topic_wikipedia_knowledge`
--

CREATE TABLE `topic_wikipedia_knowledge` (
  `eventID` varchar(40) NOT NULL,
  `wiki_article` varchar(100) NOT NULL,
  `link_jackard_similarity` double NOT NULL,
  `jackard_similarity` double NOT NULL,
  `date_generated` datetime NOT NULL,
  PRIMARY KEY (`eventID`,`wiki_article`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tweetId_hashtag`
--

CREATE TABLE `tweetId_hashtag` (
  `tweetid` varchar(40) NOT NULL,
  `hashtag` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `twitterdata`
--

CREATE TABLE `twitterdata` (
  `twitter_ID` varchar(20) NOT NULL,
  `tweet` text NOT NULL,
  `eventID` varchar(20) NOT NULL,
  `published_date` datetime NOT NULL,
  `twitter_author` varchar(100) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  PRIMARY KEY (`twitter_ID`),
  KEY `event_ID` (`eventID`),
  KEY `latitude` (`latitude`),
  KEY `published_date` (`published_date`),
  KEY `twitter_author` (`twitter_author`),
  KEY `longitude` (`longitude`),
  KEY `event_ID2` (`published_date`,`eventID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `twitterdata_tag_analytics`
--

CREATE TABLE `twitterdata_tag_analytics` (
  `twitter_ID` varchar(20) NOT NULL,
  `tweet` text NOT NULL,
  `eventID` varchar(20) NOT NULL,
  `published_date` datetime NOT NULL,
  `twitter_author` varchar(100) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `hashtag` varchar(150) NOT NULL,
  PRIMARY KEY (`twitter_ID`),
  KEY `event_ID` (`eventID`),
  KEY `latitude` (`latitude`),
  KEY `published_date` (`published_date`),
  KEY `twitter_author` (`twitter_author`),
  KEY `longitude` (`longitude`),
  KEY `event_ID2` (`published_date`,`eventID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
