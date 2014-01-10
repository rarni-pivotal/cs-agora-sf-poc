import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

class CSDataInserter implements Runnable{
	
	int start=0;
	int numRecords = 100000;
	String type = null;
	int runCount = 1;
	
	Random rand ;
	java.util.Date date;			
	java.sql.Timestamp myDate;
	
	int multiplier = 1;	
	Connection conn;
	String insertSQLFA, insertSQLFD, insertSQLO; 
	PreparedStatement psFA, psFD, psO;
	String prefix; 
	
	public CSDataInserter(int init, int nRecords, int myMultiplier, String myPrefix, String connURL) throws SQLException {
		runCount = init;
		date = new java.util.Date();			
		myDate = new java.sql.Timestamp(date.getTime());
		prefix = myPrefix;
		
		conn = DriverManager.getConnection(connURL, null);
		start = ((Integer)init).intValue();
		numRecords = ((Integer)nRecords).intValue();
		multiplier = myMultiplier;
		
		
		insertSQLFA = "insert into FillAllocations (ACTReportInfo1,AllocQuantity,AutoBustAllocation,BackCancelCorrect,BackOfficeFillID,BookingTag,BrokerFee,ChangedTimestamp,CumulativeQuantity,CustomerAccount,DestinationRegionCode,DestinationServiceName,DestinationServiceSeqNum,DestinationUserCC,ExecutionID,ExecutionUser,FOBOFillTradeID,FXForwardPoints,FillBookingState,FillID,FillState,GiveUp,InstanceID,OrderID,ParentFillID,ParentFillOrderID,Reason,ReasonCode,SalesCreditAccount,SourceRegionCode,SourceServiceName,SourceUserCC,TransactionName,TransactionNumber,TransactionTimeStamp,UnAllocQuantity,UniqueClientFillID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		psFA = conn.prepareStatement(insertSQLFA);	
		
		insertSQLO = "insert into Orders (ACTREPORTINFO2, AESINSTANCEID, AESMVDSEQUENCESTATUS, AESOMINSTANCEID, AESOMORDERID, AESOMPARENTORDERID, AESOMROUTINGDEST, AESORDERGENERATORID, AESORDERINTERNALSTATE, AESPAUSECODE, AESROUTINGRESTRICTIONS, ACCOUNT, ACCOUNTTYPE, ACCRUEDINTEREST, ADDITIONALFEE, ADJUSTTKTPRICE, ALLORNONE, ALLOCQUANTITY, ANONYMOUS, ARRIVALPRICE, ASOFTIMESTAMP, AUTOALLOCATE, AUTOBOOKEXTERNALACCOUNT, AUTOBOOKINTERNALACCOUNT, AUTOCONFIRMATION, AUTOTRADE, AVERAGEFXRATE, AVERAGEPRICE, AVGFXFORWARDPOINTS, BACKCANCELCORRECT, BACKOFFICEORDERID, BACKOFFICESYSTEM, BACKOFFICETACTIC, BARGAINTERMCODE, BASECURRENCY, BASKETLEGID, BASKETTYPE, BLOCKTRADEBROKERKEY, BLOCKTRADEREPORTCODE, BOOKINGENTITY, BOOKINGSECURITYID, BOOKINGSECURITYIDSOURCE, BRANCHSEQNUMBER, BULLETINBOARDSIZE, CANCELSTATE, CHANGEDTIMESTAMP, CHECKEDCASHVALUE, CHECKEDCLIENTACCOUNT, CHECKEDGROUPID, CHECKEDNETSELL, CLEARINGINSTRUCTIONS, CLIENTID, CLIENTLOCATION, CLIENTORDERID, CLIENTPORTFOLIOID, CLIENTSECURITYID, CLIENTSECURITYIDSOURCE, COMMISSION, COMMISSIONTYPE, COMPRESSPARAMETER, COMPRESSTIMEOUT, CONNECTIONID, CONTROLLINGUSER, COUPON, CROSSINSTRUCTIONS, CROSSINTERNALPRICE, CUMULATIVEQUANTITY, CURRENTMARKER, CUSTOMERACCOUNT, DAYCUMULATIVEQUANTITY, DEFAULTDESTINATION, DELIVERYTYPE, DESTINATIONACCOUNTTYPE, DESTINATIONDESK, DESTINATIONREGIONCODE, DESTINATIONSERVICEMESSAGE, DESTINATIONSERVICENAME, DESTINATIONSERVICEORDERID, DESTINATIONSERVICESEQNUM, DESTINATIONUSER, DESTINATIONUSERCC, DISCRETION, DIVIDENDADJUSTMENT, EXTRIGGERACTIVATIONSTATUS, EXCHANGE, EXCHANGERATE, EXCHANGETAG, EXCHANGETRADEDATE, EXECUTIONALGO, EXECUTIONCAPACITY, EXECUTIONMODE, EXECUTIONPRICE, EXECUTIONTRIGGER, EXTERNALCLIENTORDERID, FIXSESSIONID, FOBOORDERTRADEID, FXAVERAGEPRICEALLIN, FXCCY2, FXCUMULATIVEQUANTITYBASECCY, FXCUMULATIVEQUANTITYLOCALCCY, FXFWSWFARLEGSETTLEMENT, FXFWSWNEARLEGSETTLEMENT, FXFWSWQUOTEID, FXFWDBOOKINGSTATUS, FXFWDINSTRUCTIONS, FXLOCALCCYRATE, FXQUANTITYLOCALCCY, FXRATE, GIVEUP, IOI, INSTANCEID, INTERMARKETSWEEP, INTERNALCROSSELIGIBLE, INTERNALIOI, INTERNALIOIPRICE, INTERNALIOIQUANTITY, INVESTORID, JITNEYBROKERCODE, LASTUSER, LATESTFXFORWARDPOINTS, LEAVESQUANTITY, LOCALTAXES, LOCATEBROKERID, LOCATEPROVIDED, LOCATEDSHARES, MLID, MLLEGNUMBER, MLPARENTID, MLPARENTLEGNUMBER, MLPARENTTOTALLEGS, MLTOTALLEGS, MARKETHELD, MARKUPPRICE, MATURITYDATE, MAXSHOW, MEMO, MIFIDROUTING, MIFIDSHOWLMT, MIFIDTKTQUANTITY, MINACCEPTABLEQUANTITY, MINCOMMISSION, NYSERULE92TERMS, NOTHINGDONEFLAG, OPENCLOSEINDICATOR, ORDERBOOKINGSTATE, ORDERENTRYTYPE, ORDERID, ORDERORIGINATORREGIONCODE, ORDERRECEIPTTIMESTAMP, ORDERTIMESTAMP, PADJUSTTKTPRICE, PCLIENTORDERID, PEXECUTIONALGO, PEXECUTIONMODE, PEXECUTIONTRIGGER, PEXTERNALCLIENTORDERID, PFXQUANTITYLOCALCCY, PMARKETHELD, PMAXSHOW, PMEMO, PPRICEINSTRUCTION, PQUANTITY, PQUOTESIZE, PSSEXEMPTCODE, PSIDE, PTMLEVY, PTIMEINFORCE, PAIRSMAXLEGQUANTITY, BID, ASK, PAIRSSTOPLOSSAMOUNT, PARENTORDERID, PRECISION, PRICEINSTRUCTION, PRIMARYEXCHANGE, PUTORCALL, QUANTITY, QUERYID, QUOTESIZE, RECONCILED, RECYCLEDATE, REFERENCEGROUPID, REFERENCEORDERID, REGNMSEXEMPTCODE, REGNMSOPTOUTISO, REJECTEDSHARES, REPNUMBER, ROLLUP, ROOTORDERID, ROUTEFLAG, ROUTETEXT, ROUTETYPE, RULETYPE, SSEXEMPTCODE, SALESCREDIT, SALESCREDITACCOUNT, SCHEDULECODE, SECURITYID, SECURITYIDASENTERED, SECURITYIDSOURCE, SECURITYROOTID, SECURITYROOTIDSOURCE, SECURITYSUBTYPE, SECURITYTYPE, SERVERID, SETTLEMENTCURRENCY, SETTLEMENTCURRENCYPRICE, SETTLEMENTINSTRUCTION, SIDE, SOFTCOMMISSION, SOLICITATION, SOURCEDESK, SOURCEREGIONCODE, SOURCESERVICENAME, SOURCESERVICEORDERID, SOURCESERVICEUSER, SOURCEUSER, SOURCEUSERCC, STAMPTAX, STATE, STLCURAVERAGEPRICE, STOPPRICE, STOPPEDTIME, STRATEGYACCOUNT, STRATEGYCROSSACCOUNT, STRIKEPRICE, SWITCHACCOUNT, TSXBYPASS, TSXREGID, TACTIC, TAXTYPE, TIMEINFORCE, TOPTRADE, TRANCHE, TRANSACTIONNAME, TRANSACTIONNUMBER, TRANSACTIONTIMESTAMP, UNDERLYINGSECURITYID, UNIQUECLIENTORDERID, WSSAREACODE, WAVEID, WAVEMARK, WGHTEDSUMAVERAGEFXRATE, WGHTEDSUMAVERAGEPRICE, WGHTEDSUMSTLCURAVERAGEPRICE, RISK, RANK, ADVISORYBROKER, GLOBALREFERENCEGROUPID, ORDERTYPETAG, EXTERNALCLIENTBOOKINGTAG, ORIGINALLCDTAG, UNSOLICITEDOUT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		psO = conn.prepareStatement(insertSQLO);
		
		insertSQLFD = "insert into FillDetails (ACTREPORT, ASOFTIMESTAMP, BOOKSTREETSIDE, BOOKINGENTITY, BOOKINGORIGINALFILLID, CLEARINGBROKER, COMPRESSED, COMPRESSEDFILLID, CONTRABROKER, COUNTERPARTYID, CROSSTRADINGACCOUNT, EXCHANGE, EXCHANGETAG, EXCHANGETRADEDATE, EXECUTINGBROKER, EXECUTIONCAPACITY, EXECUTIONDESK, EXECUTIONEXCHANGEID, EXECUTIONEXCHANGETIMESTAMP, EXECUTIONID, EXECUTIONORIGINUSER, EXECUTIONPRICE, EXECQUANTITY, FXRATE, FILLTIMESTAMP, INSTANCEID, LATEFILL, LATEFLAG, LIQUIDITYINDICATOR, MARKUPPRICE, NBBOASK, NBBOBID, OATSREPORT, ORDERORIGINATORREGIONCODE, ORIGINALEXECUTIONID, PTMLEVY, RECYCLEDATE, REGNMSEXEMPTCODE, ROOTFILLID, SSEXEMPTCODE, SECURITYID, SERVERID, SETTLEMENTCURRENCYPRICE, SIDE, STAMPTAX, TAGTYPE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		psFD = conn.prepareStatement(insertSQLFD);
	   }
	

	@Override
	public void run() {
		
		
			
		insertOrders();
		
		for(int i = 0; i<multiplier;i++) {
			
				insertFA();
				insertFD();
			
		}
		
			
		
	}
	
	
	 private void insertOrders() {
		int i = 0;
		 try {
		 //ACTREPORTINFO2 VARCHAR
		 psO.setString(1, "");
		 //AESINSTANCEID INTEGER
		 psO.setInt(2, 1);
		 //AESMVDSEQUENCESTATUS INTEGER
		 psO.setInt(3, 1);
		 //AESOMINSTANCEID INTEGER
		 psO.setInt(4, 1);
		 //AESOMORDERID INTEGER
		 psO.setInt(5, 1);
		 //AESOMPARENTORDERID VARCHAR
		 psO.setString(6, "");
		 //AESOMROUTINGDEST VARCHAR
		 psO.setString(7, "");
		 //AESORDERGENERATORID VARCHAR
		 psO.setString(8, "");
		 //AESORDERINTERNALSTATE VARCHAR
		 psO.setString(9, "");
		 //AESPAUSECODE INTEGER
		 psO.setInt(10, 1);
		 //AESROUTINGRESTRICTIONS INTEGER
		 psO.setInt(11, 1);
		 //ACCOUNT VARCHAR
		 psO.setString(12, "");
		 //ACCOUNTTYPE VARCHAR
		 psO.setString(13, "");
		 //ACCRUEDINTEREST NUMERIC
		 psO.setBigDecimal(14, new BigDecimal(10.1));
		 //ADDITIONALFEE NUMERIC
		 psO.setBigDecimal(15, new BigDecimal(10.1));
		 //ADJUSTTKTPRICE INTEGER
		 psO.setInt(16, 1);
		 //ALLORNONE INTEGER
		 psO.setInt(17, 1);
		 //ALLOCQUANTITY NUMERIC
		 psO.setBigDecimal(18, new BigDecimal(10.1));
		 //ANONYMOUS INTEGER
		 psO.setInt(19, 1);
		 //ARRIVALPRICE NUMERIC
		 psO.setBigDecimal(20, new BigDecimal(10.1));
		 //ASOFTIMESTAMP DATE/TIMESTAMP
		 psO.setTimestamp(21, myDate);
		 //AUTOALLOCATE INTEGER
		 psO.setInt(22, 1);
		 //AUTOBOOKEXTERNALACCOUNT VARCHAR
		 psO.setString(23, "");
		 //AUTOBOOKINTERNALACCOUNT VARCHAR
		 psO.setString(24, "");
		 //AUTOCONFIRMATION INTEGER
		 psO.setInt(25, 1);
		 //AUTOTRADE VARCHAR
		 psO.setString(26, "");
		 //AVERAGEFXRATE NUMERIC
		 psO.setBigDecimal(27, new BigDecimal(10.1));
		 //AVERAGEPRICE NUMERIC
		 psO.setBigDecimal(28, new BigDecimal(10.1));
		 //AVGFXFORWARDPOINTS NUMERIC
		 psO.setBigDecimal(29, new BigDecimal(10.1));
		 //BACKCANCELCORRECT VARCHAR
		 psO.setString(30, "");
		 //BACKOFFICEORDERID INTEGER
		 psO.setInt(31, 1);
		 //BACKOFFICESYSTEM VARCHAR
		 psO.setString(32, "");
		 //BACKOFFICETACTIC VARCHAR
		 psO.setString(33, "");
		 //BARGAINTERMCODE VARCHAR
		 psO.setString(34, "");
		 //BASECURRENCY VARCHAR
		 psO.setString(35, "");
		 //BASKETLEGID VARCHAR
		 psO.setString(36, "");
		 //BASKETTYPE VARCHAR
		 psO.setString(37, "");
		 //BLOCKTRADEBROKERKEY VARCHAR
		 psO.setString(38, "");
		 //BLOCKTRADEREPORTCODE INTEGER
		 psO.setInt(39, 1);
		 //BOOKINGENTITY VARCHAR
		 psO.setString(40, "");
		 //BOOKINGSECURITYID VARCHAR
		 psO.setString(41, "");
		 //BOOKINGSECURITYIDSOURCE VARCHAR
		 psO.setString(42, "");
		 //BRANCHSEQNUMBER VARCHAR
		 psO.setString(43, "");
		 //BULLETINBOARDSIZE INTEGER
		 psO.setInt(44, 1);
		 //CANCELSTATE VARCHAR
		 psO.setString(45, "");
		 //CHANGEDTIMESTAMP DATE/TIMESTAMP
		 psO.setTimestamp(46, myDate);
		 //CHECKEDCASHVALUE NUMERIC
		 psO.setBigDecimal(47, new BigDecimal(10.1));
		 //CHECKEDCLIENTACCOUNT VARCHAR
		 psO.setString(48, "");
		 //CHECKEDGROUPID VARCHAR
		 psO.setString(49, "");
		 //CHECKEDNETSELL NUMERIC
		 psO.setBigDecimal(50, new BigDecimal(10.1));
		 //CLEARINGINSTRUCTIONS VARCHAR
		 psO.setString(51, "");
		 //CLIENTID VARCHAR
		 psO.setString(52, "");
		 //CLIENTLOCATION VARCHAR
		 psO.setString(53, "");
		 //CLIENTORDERID VARCHAR
		 psO.setString(54, "");
		 //CLIENTPORTFOLIOID VARCHAR
		 psO.setString(55, "");
		 //CLIENTSECURITYID VARCHAR
		 psO.setString(56, "");
		 //CLIENTSECURITYIDSOURCE VARCHAR
		 psO.setString(57, "");
		 //COMMISSION VARCHAR
		 psO.setString(58, "");
		 //COMMISSIONTYPE VARCHAR
		 psO.setString(59, "");
		 //COMPRESSPARAMETER INTEGER
		 psO.setInt(60, 1);
		 //COMPRESSTIMEOUT INTEGER
		 psO.setInt(61, 1);
		 //CONNECTIONID VARCHAR
		 psO.setString(62, "");
		 //CONTROLLINGUSER VARCHAR
		 psO.setString(63, "");
		 //COUPON NUMERIC
		 psO.setBigDecimal(64, new BigDecimal(10.1));
		 //CROSSINSTRUCTIONS INTEGER
		 psO.setInt(65, 1);
		 //CROSSINTERNALPRICE NUMERIC
		 psO.setBigDecimal(66, new BigDecimal(10.1));
		 //CUMULATIVEQUANTITY NUMERIC
		 psO.setBigDecimal(67, new BigDecimal(10.1));
		 //CURRENTMARKER INTEGER
		 psO.setInt(68, 1);
		 //CUSTOMERACCOUNT VARCHAR
		 psO.setString(69, "");
		 //DAYCUMULATIVEQUANTITY NUMERIC
		 psO.setBigDecimal(70, new BigDecimal(10.1));
		 //DEFAULTDESTINATION VARCHAR
		 psO.setString(71, "");
		 //DELIVERYTYPE VARCHAR
		 psO.setString(72, "");
		 //DESTINATIONACCOUNTTYPE VARCHAR
		 psO.setString(73, "");
		 //DESTINATIONDESK VARCHAR
		 psO.setString(74, "");
		 //DESTINATIONREGIONCODE VARCHAR
		 psO.setString(75, "");
		 //DESTINATIONSERVICEMESSAGE VARCHAR
		 psO.setString(76, "");
		 //DESTINATIONSERVICENAME VARCHAR
		 psO.setString(77, "");
		 //DESTINATIONSERVICEORDERID VARCHAR
		 psO.setString(78, "");
		 //DESTINATIONSERVICESEQNUM INTEGER
		 psO.setInt(79, 1);
		 //DESTINATIONUSER VARCHAR
		 psO.setString(80, "");
		 //DESTINATIONUSERCC VARCHAR
		 psO.setString(81, "");
		 //DISCRETION INTEGER
		 psO.setInt(82, 1);
		 //DIVIDENDADJUSTMENT VARCHAR
		 psO.setString(83, "");
		 //EXTRIGGERACTIVATIONSTATUS INTEGER
		 psO.setInt(84, 1);
		 //EXCHANGE VARCHAR
		 psO.setString(85, "");
		 //EXCHANGERATE NUMERIC
		 psO.setBigDecimal(86, new BigDecimal(10.1));
		 //EXCHANGETAG VARCHAR
		 psO.setString(87, "");
		 //EXCHANGETRADEDATE DATE/TIMESTAMP
		 psO.setTimestamp(88, myDate);
		 //EXECUTIONALGO VARCHAR
		 psO.setString(89, "");
		 //EXECUTIONCAPACITY VARCHAR
		 psO.setString(90, "");
		 //EXECUTIONMODE VARCHAR
		 psO.setString(91, "");
		 //EXECUTIONPRICE NUMERIC
		 psO.setBigDecimal(92, new BigDecimal(10.1));
		 //EXECUTIONTRIGGER VARCHAR
		 psO.setString(93, "");
		 //EXTERNALCLIENTORDERID VARCHAR
		 psO.setString(94, "");
		 //FIXSESSIONID VARCHAR
		 psO.setString(95, "");
		 //FOBOORDERTRADEID VARCHAR
		 psO.setString(96, "");
		 //FXAVERAGEPRICEALLIN NUMERIC
		 psO.setBigDecimal(97, new BigDecimal(10.1));
		 //FXCCY2 INTEGER
		 psO.setInt(98, 1);
		 //FXCUMULATIVEQUANTITYBASECCY NUMERIC
		 psO.setBigDecimal(99, new BigDecimal(10.1));
		 //FXCUMULATIVEQUANTITYLOCALCCY NUMERIC
		 psO.setBigDecimal(100, new BigDecimal(10.1));
		 //FXFWSWFARLEGSETTLEMENT VARCHAR
		 psO.setString(101, "");
		 //FXFWSWNEARLEGSETTLEMENT VARCHAR
		 psO.setString(102, "");
		 //FXFWSWQUOTEID VARCHAR
		 psO.setString(103, "");
		 //FXFWDBOOKINGSTATUS INTEGER
		 psO.setInt(104, 1);
		 //FXFWDINSTRUCTIONS INTEGER
		 psO.setInt(105, 1);
		 //FXLOCALCCYRATE NUMERIC
		 psO.setBigDecimal(106, new BigDecimal(10.1));
		 //FXQUANTITYLOCALCCY NUMERIC
		 psO.setBigDecimal(107, new BigDecimal(10.1));
		 //FXRATE NUMERIC
		 psO.setBigDecimal(108, new BigDecimal(10.1));
		 //GIVEUP VARCHAR
		 psO.setString(109, "");
		 //IOI INTEGER
		 psO.setInt(110, 1);
		 //INSTANCEID VARCHAR
		 psO.setString(111, "");
		 //INTERMARKETSWEEP VARCHAR
		 psO.setString(112, "");
		 //INTERNALCROSSELIGIBLE VARCHAR
		 psO.setString(113, "");
		 //INTERNALIOI INTEGER
		 psO.setInt(114, 1);
		 //INTERNALIOIPRICE NUMERIC
		 psO.setBigDecimal(115, new BigDecimal(10.1));
		 //INTERNALIOIQUANTITY INTEGER
		 psO.setInt(116, 1);
		 //INVESTORID VARCHAR
		 psO.setString(117, "");
		 //JITNEYBROKERCODE VARCHAR
		 psO.setString(118, "");
		 //LASTUSER VARCHAR
		 psO.setString(119, "");
		 //LATESTFXFORWARDPOINTS NUMERIC
		 psO.setBigDecimal(120, new BigDecimal(10.1));
		 //LEAVESQUANTITY NUMERIC
		 psO.setBigDecimal(121, new BigDecimal(10.1));
		 //LOCALTAXES NUMERIC
		 psO.setBigDecimal(122, new BigDecimal(10.1));
		 //LOCATEBROKERID VARCHAR
		 psO.setString(123, "");
		 //LOCATEPROVIDED VARCHAR
		 psO.setString(124, "");
		 //LOCATEDSHARES NUMERIC
		 psO.setBigDecimal(125, new BigDecimal(10.1));
		 //MLID VARCHAR
		 psO.setString(126, "");
		 //MLLEGNUMBER INTEGER
		 psO.setInt(127, 1);
		 //MLPARENTID VARCHAR
		 psO.setString(128, "");
		 //MLPARENTLEGNUMBER INTEGER
		 psO.setInt(129, 1);
		 //MLPARENTTOTALLEGS INTEGER
		 psO.setInt(130, 1);
		 //MLTOTALLEGS INTEGER
		 psO.setInt(131, 1);
		 //MARKETHELD INTEGER
		 psO.setInt(132, 1);
		 //MARKUPPRICE NUMERIC
		 psO.setBigDecimal(133, new BigDecimal(10.1));
		 //MATURITYDATE VARCHAR
		 psO.setString(134, "");
		 //MAXSHOW NUMERIC
		 psO.setBigDecimal(135, new BigDecimal(10.1));
		 //MEMO VARCHAR
		 psO.setString(136, "");
		 //MIFIDROUTING VARCHAR
		 psO.setString(137, "");
		 //MIFIDSHOWLMT INTEGER
		 psO.setInt(138, 1);
		 //MIFIDTKTQUANTITY INTEGER
		 psO.setInt(139, 1);
		 //MINACCEPTABLEQUANTITY NUMERIC
		 psO.setBigDecimal(140, new BigDecimal(10.1));
		 //MINCOMMISSION NUMERIC
		 psO.setBigDecimal(141, new BigDecimal(10.1));
		 //NYSERULE92TERMS VARCHAR
		 psO.setString(142, "");
		 //NOTHINGDONEFLAG INTEGER
		 psO.setInt(143, 1);
		 //OPENCLOSEINDICATOR VARCHAR
		 psO.setString(144, "");
		 //ORDERBOOKINGSTATE VARCHAR
		 psO.setString(145, "");
		 //ORDERENTRYTYPE VARCHAR
		 psO.setString(146, "");
		 //ORDERID VARCHAR
		 //psO.setString(147, "");
		 //ORDERORIGINATORREGIONCODE VARCHAR
		 psO.setString(148, "");
		 //ORDERRECEIPTTIMESTAMP DATE/TIMESTAMP
		 psO.setTimestamp(149, myDate);
		 //ORDERTIMESTAMP DATE/TIMESTAMP
		 psO.setTimestamp(150, myDate);
		 //PADJUSTTKTPRICE INTEGER
		 psO.setInt(151, 1);
		 //PCLIENTORDERID VARCHAR
		 psO.setString(152, "");
		 //PEXECUTIONALGO VARCHAR
		 psO.setString(153, "");
		 //PEXECUTIONMODE VARCHAR
		 psO.setString(154, "");
		 //PEXECUTIONTRIGGER VARCHAR
		 psO.setString(155, "");
		 //PEXTERNALCLIENTORDERID VARCHAR
		 psO.setString(156, "");
		 //PFXQUANTITYLOCALCCY NUMERIC
		 psO.setBigDecimal(157, new BigDecimal(10.1));
		 //PMARKETHELD INTEGER
		 psO.setInt(158, 1);
		 //PMAXSHOW NUMERIC
		 psO.setBigDecimal(159, new BigDecimal(10.1));
		 //PMEMO VARCHAR
		 psO.setString(160, "");
		 //PPRICEINSTRUCTION VARCHAR
		 psO.setString(161, "");
		 //PQUANTITY NUMERIC
		 psO.setBigDecimal(162, new BigDecimal(10.1));
		 //PQUOTESIZE NUMERIC
		 psO.setBigDecimal(163, new BigDecimal(10.1));
		 //PSSEXEMPTCODE INTEGER
		 psO.setInt(164, 1);
		 //PSIDE VARCHAR
		 psO.setString(165, "");
		 //PTMLEVY NUMERIC
		 psO.setBigDecimal(166, new BigDecimal(10.1));
		 //PTIMEINFORCE VARCHAR
		 psO.setString(167, "");
		 //PAIRSMAXLEGQUANTITY NUMERIC
		 psO.setBigDecimal(168, new BigDecimal(10.1));
		 //BID NUMERIC
		 psO.setBigDecimal(169, new BigDecimal(10.1));
		 //ASK NUMERIC
		 psO.setBigDecimal(170, new BigDecimal(10.1));
		 //PAIRSSTOPLOSSAMOUNT NUMERIC
		 psO.setBigDecimal(171, new BigDecimal(10.1));
		 //PARENTORDERID VARCHAR
		 psO.setString(172, "");
		 //PRECISION INTEGER
		 psO.setInt(173, 1);
		 //PRICEINSTRUCTION VARCHAR
		 psO.setString(174, "");
		 //PRIMARYEXCHANGE VARCHAR
		 psO.setString(175, "");
		 //PUTORCALL VARCHAR
		 psO.setString(176, "");
		 //QUANTITY NUMERIC
		 psO.setBigDecimal(177, new BigDecimal(10.1));
		 //QUERYID INTEGER
		 psO.setInt(178, 1);
		 //QUOTESIZE NUMERIC
		 psO.setBigDecimal(179, new BigDecimal(10.1));
		 //RECONCILED INTEGER
		 psO.setInt(180, 1);
		 //RECYCLEDATE DATE/TIMESTAMP
		 psO.setTimestamp(181, myDate);
		 //REFERENCEGROUPID INTEGER
		 psO.setInt(182, 1);
		 //REFERENCEORDERID INTEGER
		 psO.setInt(183, 1);
		 //REGNMSEXEMPTCODE VARCHAR
		 psO.setString(184, "");
		 //REGNMSOPTOUTISO VARCHAR
		 psO.setString(185, "");
		 //REJECTEDSHARES NUMERIC
		 psO.setBigDecimal(186, new BigDecimal(10.1));
		 //REPNUMBER VARCHAR
		 psO.setString(187, "");
		 //ROLLUP INTEGER
		 psO.setInt(188, 1);
		 //ROOTORDERID VARCHAR
		 psO.setString(189, "");
		 //ROUTEFLAG INTEGER
		 psO.setInt(190, 1);
		 //ROUTETEXT VARCHAR
		 psO.setString(191, "");
		 //ROUTETYPE INTEGER
		 psO.setInt(192, 1);
		 //RULETYPE VARCHAR
		 psO.setString(193, "");
		 //SSEXEMPTCODE INTEGER
		 psO.setInt(194, 1);
		 //SALESCREDIT VARCHAR
		 psO.setString(195, "");
		 //SALESCREDITACCOUNT VARCHAR
		 psO.setString(196, "");
		 //SCHEDULECODE VARCHAR
		 psO.setString(197, "");
		 //SECURITYID VARCHAR
		 psO.setString(198, "");
		 //SECURITYIDASENTERED VARCHAR
		 psO.setString(199, "");
		 //SECURITYIDSOURCE VARCHAR
		 psO.setString(200, "");
		 //SECURITYROOTID VARCHAR
		 psO.setString(201, "");
		 //SECURITYROOTIDSOURCE VARCHAR
		 psO.setString(202, "");
		 //SECURITYSUBTYPE VARCHAR
		 psO.setString(203, "");
		 //SECURITYTYPE VARCHAR
		 psO.setString(204, "");
		 //SERVERID VARCHAR
		 psO.setString(205, "");
		 //SETTLEMENTCURRENCY VARCHAR
		 psO.setString(206, "");
		 //SETTLEMENTCURRENCYPRICE NUMERIC
		 psO.setBigDecimal(207, new BigDecimal(10.1));
		 //SETTLEMENTINSTRUCTION VARCHAR
		 psO.setString(208, "");
		 //SIDE VARCHAR
		 psO.setString(209, "");
		 //SOFTCOMMISSION VARCHAR
		 psO.setString(210, "");
		 //SOLICITATION INTEGER
		 psO.setInt(211, 1);
		 //SOURCEDESK VARCHAR
		 psO.setString(212, "");
		 //SOURCEREGIONCODE VARCHAR
		 psO.setString(213, "");
		 //SOURCESERVICENAME VARCHAR
		 psO.setString(214, "");
		 //SOURCESERVICEORDERID VARCHAR
		 psO.setString(215, "");
		 //SOURCESERVICEUSER VARCHAR
		 psO.setString(216, "");
		 //SOURCEUSER VARCHAR
		 psO.setString(217, "");
		 //SOURCEUSERCC VARCHAR
		 psO.setString(218, "");
		 //STAMPTAX NUMERIC
		 psO.setBigDecimal(219, new BigDecimal(10.1));
		 //STATE VARCHAR
		 psO.setString(220, "");
		 //STLCURAVERAGEPRICE NUMERIC
		 psO.setBigDecimal(221, new BigDecimal(10.1));
		 //STOPPRICE NUMERIC
		 psO.setBigDecimal(222, new BigDecimal(10.1));
		 //STOPPEDTIME DATE/TIMESTAMP
		 psO.setTimestamp(223, myDate);
		 //STRATEGYACCOUNT VARCHAR
		 psO.setString(224, "");
		 //STRATEGYCROSSACCOUNT VARCHAR
		 psO.setString(225, "");
		 //STRIKEPRICE NUMERIC
		 psO.setBigDecimal(226, new BigDecimal(10.1));
		 //SWITCHACCOUNT VARCHAR
		 psO.setString(227, "");
		 //TSXBYPASS VARCHAR
		 psO.setString(228, "");
		 //TSXREGID VARCHAR
		 psO.setString(229, "");
		 //TACTIC VARCHAR
		 psO.setString(230, "");
		 //TAXTYPE VARCHAR
		 psO.setString(231, "");
		 //TIMEINFORCE VARCHAR
		 psO.setString(232, "");
		 //TOPTRADE INTEGER
		 psO.setInt(233, 1);
		 //TRANCHE VARCHAR
		 psO.setString(234, "");
		 //TRANSACTIONNAME VARCHAR
		 psO.setString(235, "");
		 //TRANSACTIONNUMBER INTEGER
		 psO.setInt(236, 1);
		 //TRANSACTIONTIMESTAMP DATE/TIMESTAMP
		 psO.setTimestamp(237, myDate);
		 //UNDERLYINGSECURITYID VARCHAR
		 psO.setString(238, "");
		 //UNIQUECLIENTORDERID VARCHAR
		 psO.setString(239, "");
		 //WSSAREACODE VARCHAR
		 psO.setString(240, "");
		 //WAVEID VARCHAR
		 psO.setString(241, "");
		 //WAVEMARK VARCHAR
		 psO.setString(242, "");
		 //WGHTEDSUMAVERAGEFXRATE NUMERIC
		 psO.setBigDecimal(243, new BigDecimal(10.1));
		 //WGHTEDSUMAVERAGEPRICE NUMERIC
		 psO.setBigDecimal(244, new BigDecimal(10.1));
		 //WGHTEDSUMSTLCURAVERAGEPRICE NUMERIC
		 psO.setBigDecimal(245, new BigDecimal(10.1));
		 //RISK VARCHAR
		 psO.setString(246, "");
		 //RANK INTEGER
		 psO.setInt(247, 1);
		 //ADVISORYBROKER VARCHAR
		 psO.setString(248, "");
		 //GLOBALREFERENCEGROUPID VARCHAR
		 psO.setString(249, "");
		 //ORDERTYPETAG VARCHAR
		 psO.setString(250, "");
		 //EXTERNALCLIENTBOOKINGTAG VARCHAR
		 psO.setString(251, "");
		 //ORIGINALLCDTAG INTEGER
		 psO.setInt(252, 1);
		 //UNSOLICITEDOUT VARCHAR
		 psO.setString(253, "");
		 
		 for (i=0;i<numRecords;i++){
				
				psO.setString(147, prefix+runCount+i);
				psO.executeUpdate();
			}
			
		 } catch (SQLException se) {
			 System.out.println("Orders: Error inserting "+prefix+runCount+i);
			 se.printStackTrace();
		 }
		
	}


	private void insertFD() {
		int i =0;
		try {
		//ACTREPORT VARCHAR
		psFD.setString(1, "");
		//ASOFTIMESTAMP DATE/TIMESTAMP
		psFD.setTimestamp(2, myDate);
		//BOOKSTREETSIDE VARCHAR
		psFD.setString(3, "");
		//BOOKINGENTITY VARCHAR
		psFD.setString(4, "");
		//BOOKINGORIGINALFILLID VARCHAR
		psFD.setString(5, "");
		//CLEARINGBROKER VARCHAR
		psFD.setString(6, "");
		//COMPRESSED INTEGER
		psFD.setInt(7, 1);
		//COMPRESSEDFILLID INTEGER
		psFD.setInt(8, 1);
		//CONTRABROKER VARCHAR
		psFD.setString(9, "");
		//COUNTERPARTYID VARCHAR
		psFD.setString(10, "");
		//CROSSTRADINGACCOUNT VARCHAR
		psFD.setString(11, "");
		//EXCHANGE VARCHAR
		psFD.setString(12, "");
		//EXCHANGETAG VARCHAR
		psFD.setString(13, "");
		//EXCHANGETRADEDATE DATE/TIMESTAMP
		psFD.setTimestamp(14, myDate);
		//EXECUTINGBROKER VARCHAR
		psFD.setString(15, "");
		//EXECUTIONCAPACITY VARCHAR
		psFD.setString(16, "");
		//EXECUTIONDESK VARCHAR
		psFD.setString(17, "");
		//EXECUTIONEXCHANGEID VARCHAR
		psFD.setString(18, "");
		//EXECUTIONEXCHANGETIMESTAMP DATE/TIMESTAMP
		psFD.setTimestamp(19, myDate);
		//EXECUTIONID VARCHAR
		psFD.setString(20, "");
		//EXECUTIONORIGINUSER VARCHAR
		psFD.setString(21, "");
		//EXECUTIONPRICE NUMERIC
		psFD.setBigDecimal(22, new BigDecimal(10.1));
		//EXECQUANTITY NUMERIC
		psFD.setBigDecimal(23, new BigDecimal(10.1));
		//FXRATE NUMERIC
		psFD.setBigDecimal(24, new BigDecimal(10.1));
		//FILLTIMESTAMP DATE/TIMESTAMP
		psFD.setTimestamp(25, myDate);
		//INSTANCEID VARCHAR
		psFD.setString(26, "");
		//LATEFILL VARCHAR
		psFD.setString(27, "");
		//LATEFLAG VARCHAR
		psFD.setString(28, "");
		//LIQUIDITYINDICATOR VARCHAR
		psFD.setString(29, "");
		//MARKUPPRICE NUMERIC
		psFD.setBigDecimal(30, new BigDecimal(10.1));
		//NBBOASK NUMERIC
		psFD.setBigDecimal(31, new BigDecimal(10.1));
		//NBBOBID NUMERIC
		psFD.setBigDecimal(32, new BigDecimal(10.1));
		//OATSREPORT VARCHAR
		psFD.setString(33, "");
		//ORDERORIGINATORREGIONCODE VARCHAR
		psFD.setString(34, "");
		//ORIGINALEXECUTIONID VARCHAR
		psFD.setString(35, "");
		//PTMLEVY NUMERIC
		psFD.setBigDecimal(36, new BigDecimal(10.1));
		//RECYCLEDATE DATE/TIMESTAMP
		psFD.setTimestamp(37, myDate);
		//REGNMSEXEMPTCODE VARCHAR
		psFD.setString(38, "");
		//ROOTFILLID VARCHAR
		psFD.setString(39, "");
		//SSEXEMPTCODE INTEGER
		psFD.setInt(40, 1);
		//SECURITYID VARCHAR
		psFD.setString(41, "");
		//SERVERID VARCHAR
		psFD.setString(42, "");
		//SETTLEMENTCURRENCYPRICE NUMERIC
		psFD.setBigDecimal(43, new BigDecimal(10.1));
		//SIDE VARCHAR
		psFD.setString(44, "");
		//STAMPTAX NUMERIC
		psFD.setBigDecimal(45, new BigDecimal(10.1));
		//TAGTYPE INTEGER
		psFD.setInt(46, 1);
		
		for (i=0;i<numRecords;i++){
			
			psFD.setString(20, prefix+runCount+i);
			psFD.executeUpdate();
		}
		
		} catch (SQLException se) {
			 System.out.println("FillDetails: Error inserting "+prefix+runCount+i); 
			se.printStackTrace();
		}
		
		
	}


	public void insertFA() {//thread starts from here
		 
			int i =0;
			try {
				
				//conn.setAutoCommit(true);
					
				//ACTReportInfo1 varchar(50)
				psFA.setString(1, "");
				// AllocQuantity NUMERIC(19,2),
				psFA.setBigDecimal(2, new BigDecimal(10.1));
				//AutoBustAllocation int
				psFA.setInt(3, 5);
				//BackCancelCorrect varchar(1),
				psFA.setString(4, "");
				 //BackOfficeFillID int,
				psFA.setInt(5, 5);
				   //BookingTag int,
				psFA.setInt(6, 5);
				   //BrokerFee NUMERIC(19,8),
				psFA.setBigDecimal(7, new BigDecimal(10.1));
				   //ChangedTimestamp datetime,
				psFA.setTimestamp(8, myDate);
				   //CumulativeQuantity NUMERIC(19,2),
				psFA.setBigDecimal(9, new BigDecimal(10.1));
				   //CustomerAccount varchar(14),
				psFA.setString(10, "");
				   //DestinationRegionCode varchar(5),
				psFA.setString(11, "");
				   //DestinationServiceName varchar(15),
				psFA.setString(12, "");
				   //DestinationServiceSeqNum int,
				psFA.setInt(13, 5);
				   //DestinationUserCC varchar(14),
				psFA.setString(14, "");
				   //ExecutionID varchar(16),
				psFA.setString(15, "");
				   //ExecutionUser varchar(14),
				psFA.setString(16, "");
				   //FOBOFillTradeID varchar(25),
				psFA.setString(17, "");
				   //FXForwardPoints NUMERIC(19,8),
				psFA.setBigDecimal(18, new BigDecimal(10.1));
				   //FillBookingState varchar(7),
				psFA.setString(19, "");
				   //FillID varchar(16) NOT NULL,
				psFA.setString(20, "");
				   //FillState v20char(6),
				psFA.setString(21, "");
				   //GiveUp varchar(9),
				psFA.setString(22, "");
				   //InstanceID varchar(1),
				psFA.setString(23, "");
				   //OrderID varchar(16),
				psFA.setString(24, "");
				   //ParentFillID varchar(16),
				psFA.setString(25, "");
				   //ParentFillOrderID varchar(16),
				psFA.setString(26, "");
				   //Reason int,
				psFA.setInt(27, 5);
				   //ReasonCode varchar(4),
				psFA.setString(28, "");
				   //SalesCreditAccount varchar(14),
				psFA.setString(29, "");
				   //SourceRegionCode varchar(5),
				psFA.setString(30, "");
				   //SourceServiceName varchar(14),
				psFA.setString(31, "");
				   //SourceUserCC varchar(14),
				psFA.setString(32, "");
				   //TransactionName varchar(4),
				psFA.setString(33, "");
				   //TransactionNumber int,
				psFA.setInt(34, 5);
				   //TransactionTimeStamp datetime,
				psFA.setTimestamp(35, myDate);
				   //UnAllocQuantity NUMERIC(19,2),
				psFA.setBigDecimal(36, new BigDecimal(10.1));
				   //UniqueClientFillID varchar(30),
				psFA.setString(37, "");
					
				for (i=0;i<numRecords;i++){
					
					psFA.setString(20, prefix+runCount+i);
					psFA.executeUpdate();
				}
			
				} catch (SQLException se) {
				
					 System.out.println("FillAllocations: Error inserting "+prefix+runCount+i); 
					se.printStackTrace();
				}
			
			
			
	 }




	
	
	}
