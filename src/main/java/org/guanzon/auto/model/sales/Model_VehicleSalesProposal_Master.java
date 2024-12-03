/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.model.sales;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
//import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.iface.GEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class Model_VehicleSalesProposal_Master implements GEntity{
    final String XML = "Model_VehicleSalesProposal_Master.xml";
    private final String psDefaultDate = "1900-01-01";
    private String psBranchCd;
    private String psExclude = "sInquryID»sTranStat»sBuyCltNm»cClientTp»sAddressx»dInqryDte»sInqCltID»sInqCltNm»cInqCltTp»sContctID»sContctNm»sSourceCD»sSourceNo»sPlatform»sAgentIDx»sAgentNmx»sEmployID»sSENamexx"
                             + "»sCoCltNmx»sCSNoxxxx»sPlateNox»sFrameNox»sEngineNo»sKeyNoxxx»sVhclDesc»sVhclFDsc»sColorDsc»sBranchNm»sTPLBrIns»sTPLInsNm»sCOMBrIns»sCOMInsNm»sApplicNo»sBrBankNm»sBankName"
                             + "»sUDRNoxxx»sUDRDatex»sJONoxxxx»sSINoxxxx»sGatePsNo»dBirthDte»sTaxIDNox»cOfficexx»sMobileNo»sEmailAdd»sTPLTrans»sTPLRefrn»sTPLTypex»sCOMTrans»sCOMRefrn»sCOMTypex»sBOTTrans»sBOTRefrn»sBOTTypex"
                             + "»cVhclSize»sUnitType»sBodyType»dApprovex»sApprover";//» 
    GRider poGRider;                //application driver
    CachedRowSet poEntity;          //rowset
    JSONObject poJSON;              //json container
    int pnEditMode;                 //edit mode

    /**
     * Entity constructor
     *
     * @param foValue - GhostRider Application Driver
     */
    public Model_VehicleSalesProposal_Master(GRider foValue) {
        if (foValue == null) {
            System.err.println("Application Driver is not set.");
            System.exit(1);
        }

        poGRider = foValue;

        initialize();
    }
    
    private void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);        
            poEntity.updateObject("dTransact", poGRider.getServerDate()); 
            poEntity.updateObject("dDelvryDt", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));   
            poEntity.updateObject("dDcStatDt", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateObject("dLockedDt", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateObject("dCancelld", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
//            poEntity.updateObject("dApproved", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateObject("dInqryDte", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateObject("dApprovex", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateString("cTranStat", TransactionStatus.STATE_OPEN); //TransactionStatus.STATE_OPEN why is the value of STATE_OPEN is 0 while record status active is 1
            //RecordStatus.ACTIVE
            poEntity.updateString("cIsVhclNw", "0");  
            poEntity.updateString("cIsVIPxxx", "0");  
            poEntity.updateInt("nInsurYrx", 0);   
            
            
            poEntity.updateBigDecimal("nUnitPrce", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nAdvDwPmt", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nOthrChrg", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nLaborAmt", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nAccesAmt", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nInsurAmt", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nTPLAmtxx", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nCompAmtx", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nLTOAmtxx", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nChmoAmtx", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nPromoDsc", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nFleetDsc", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nSPFltDsc", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nBndleDsc", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nAddlDscx", new BigDecimal("0.00"));
//            poEntity.updateBigDecimal("nDealrInc", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nTranTotl", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nResrvFee", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nDownPaym", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nNetTTotl", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nAmtPaidx", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nFrgtChrg", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nDue2Supx", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nDue2Dlrx", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nSPFD2Sup", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nSPFD2Dlr", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nPrmD2Sup", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nPrmD2Dlr", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nDealrRte", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nDealrAmt", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nSlsInRte", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nSlsInAmt", new BigDecimal("0.00"));

            
//            poEntity.updateBigDecimal("nUnitPrce", new BigDecimal("0.00")); 
//            poEntity.updateDouble("nAdvDwPmt", 0.00); 
//            poEntity.updateDouble("nOthrChrg", 0.00); 
//            poEntity.updateDouble("nLaborAmt", 0.00);
//            poEntity.updateDouble("nAccesAmt", 0.00);
//            poEntity.updateDouble("nInsurAmt", 0.00);
//            poEntity.updateDouble("nTPLAmtxx", 0.00);
//            poEntity.updateDouble("nCompAmtx", 0.00);
//            poEntity.updateDouble("nLTOAmtxx", 0.00);
//            poEntity.updateDouble("nChmoAmtx", 0.00);
//            poEntity.updateDouble("nPromoDsc", 0.00);
//            poEntity.updateDouble("nFleetDsc", 0.00);
//            poEntity.updateDouble("nSPFltDsc", 0.00);
//            poEntity.updateDouble("nBndleDsc", 0.00);
//            poEntity.updateDouble("nAddlDscx", 0.00);
//            poEntity.updateDouble("nDealrInc", 0.00);
//            poEntity.updateDouble("nTranTotl", 0.00);
//            poEntity.updateDouble("nResrvFee", 0.00);
//            poEntity.updateDouble("nDownPaym", 0.00);
//            poEntity.updateDouble("nNetTTotl", 0.00);
//            poEntity.updateDouble("nAmtPaidx", 0.00);
//            poEntity.updateDouble("nFrgtChrg", 0.00);
//            poEntity.updateDouble("nDue2Supx", 0.00);
//            poEntity.updateDouble("nDue2Dlrx", 0.00);
//            poEntity.updateDouble("nSPFD2Sup", 0.00);
//            poEntity.updateDouble("nSPFD2Dlr", 0.00);
//            poEntity.updateDouble("nPrmD2Sup", 0.00);
//            poEntity.updateDouble("nPrmD2Dlr", 0.00);
//            poEntity.updateDouble("nDealrRte", 0.00);
//            poEntity.updateDouble("nDealrAmt", 0.00);
//            poEntity.updateDouble("nSlsInRte", 0.00);
//            poEntity.updateDouble("nSlsInAmt", 0.00);
            
            poEntity.insertRow();
            poEntity.moveToCurrentRow();
            poEntity.absolute(1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Gets the column index name.
     *
     * @param fnValue - column index number
     * @return column index name
     */
    @Override
    public String getColumn(int fnValue) {
        try {
            return poEntity.getMetaData().getColumnLabel(fnValue);
        } catch (SQLException e) {
        }
        return "";
    }

    /**
     * Gets the column index number.
     *
     * @param fsValue - column index name
     * @return column index number
     */
    @Override
    public int getColumn(String fsValue) {
        try {
            return MiscUtil.getColumnIndex(poEntity, fsValue);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Gets the total number of column.
     *
     * @return total number of column
     */
    @Override
    public int getColumnCount() {
        try {
            return poEntity.getMetaData().getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int getEditMode() {
        return pnEditMode;
    }
    
    /**
     * Gets the table name.
     *
     * @return table name
     */
    @Override
    public String getTable() {
        return "vsp_master";
    }
    
    /**
     * Gets the value of a column index number.
     *
     * @param fnColumn - column index number
     * @return object value
     */
    @Override
    public Object getValue(int fnColumn) {
        try {
            return poEntity.getObject(fnColumn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the value of a column index name.
     *
     * @param fsColumn - column index name
     * @return object value
     */
    @Override
    public Object getValue(String fsColumn) {
        try {
            return poEntity.getObject(MiscUtil.getColumnIndex(poEntity, fsColumn));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets column value.
     *
     * @param fnColumn - column index number
     * @param foValue - value
     * @return result as success/failed
     */
    @Override
    public JSONObject setValue(int fnColumn, Object foValue) {
        try {
            poJSON = MiscUtil.validateColumnValue(System.getProperty("sys.default.path.metadata") + XML, MiscUtil.getColumnLabel(poEntity, fnColumn), foValue);
            if ("error".equals((String) poJSON.get("result"))) {
                if(foValue != null){
                    System.out.println("ERROR : " + MiscUtil.getColumnLabel(poEntity, fnColumn) + " VALUE : " + foValue + " : " + (String) poJSON.get("message"));
                }
                return poJSON;
            }

            poEntity.updateObject(fnColumn, foValue);
            poEntity.updateRow();

            poJSON = new JSONObject();
            poJSON.put("result", "success");
            poJSON.put("value", getValue(fnColumn));
        } catch (SQLException e) {
            e.printStackTrace();
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }

        return poJSON;
    }
    
//    public JSONObject setValueDate(int fnColumn, Object foValue) {
//        try {
//            poJSON = validateColumnValue(System.getProperty("sys.default.path.metadata") + XML, MiscUtil.getColumnLabel(poEntity, fnColumn), foValue);
//            if ("error".equals((String) poJSON.get("result"))) {
//                return poJSON;
//            }
//
//            poEntity.updateObject(fnColumn, foValue);
//            poEntity.updateRow();
//
//            poJSON = new JSONObject();
//            poJSON.put("result", "success");
//            poJSON.put("value", getValue(fnColumn));
//        } catch (SQLException e) {
//            e.printStackTrace();
//            poJSON.put("result", "error");
//            poJSON.put("message", e.getMessage());
//        }
//
//        return poJSON;
//    }
//    
//    public JSONObject setValueDate(String fsColumn, Object foValue) {
//        poJSON = new JSONObject();
//
//        try {
//            return setValueDate(MiscUtil.getColumnIndex(poEntity, fsColumn), foValue);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            poJSON.put("result", "error");
//            poJSON.put("message", e.getMessage());
//        }
//        return poJSON;
//    }
//    
//     public static JSONObject validateColumnValue(String fsXMLFile, String fsColumnNm, Object foValue) {
//    JSONObject loJSON = new JSONObject();
//    try {
//      File file = new File(fsXMLFile);
//      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//      Document doc = dBuilder.parse(file);
//      doc.getDocumentElement().normalize();
//      NodeList nodeList = doc.getElementsByTagName("column");
//      int lnRow = nodeList.getLength();
//      RowSetMetaData meta = new RowSetMetaDataImpl();
//      meta.setColumnCount(lnRow);
//      for (int i = 0; i < lnRow; i++) {
//        Element element = (Element)nodeList.item(i);
//        String columnName = element.getElementsByTagName("COLUMN_LABEL").item(0).getTextContent();
//        if (columnName.equals(fsColumnNm)) {
//          String dataType = element.getElementsByTagName("DATA_TYPE").item(0).getTextContent();
//          String nullable = element.getElementsByTagName("NULLABLE").item(0).getTextContent();
//          String length = element.getElementsByTagName("LENGTH").item(0).getTextContent();
//          String precision = element.getElementsByTagName("PRECISION").item(0).getTextContent();
//          String scale = element.getElementsByTagName("SCALE").item(0).getTextContent();
//          int columnType = Integer.parseInt(dataType);
//          int columnDisplaySize = Integer.parseInt(length);
//          int columnScale = Integer.parseInt(scale);
//          int columnPrecision = Integer.parseInt(precision);
//          boolean isNullable = nullable.equals("1");
//          return validMetadata(columnType, columnDisplaySize, columnScale, columnPrecision, isNullable, foValue);
//        } 
//      } 
//      loJSON.put("result", "error");
//      loJSON.put("message", "Unable to find column to validate.");
//    } catch (IOException|SQLException|javax.xml.parsers.ParserConfigurationException|org.w3c.dom.DOMException|org.xml.sax.SAXException e) {
//      loJSON.put("result", "error");
//      loJSON.put("message", e.getMessage());
//    } 
//    return loJSON;
//  }
//    
//    private static JSONObject validMetadata(int columnType, int columnDisplaySize, int columnScale, int columnPrecision, boolean isNullable, Object foValue) {
//    long longValue;
//    double doubleValue, decimalValue;
//    String stringValue;
//    JSONObject loJSON = new JSONObject();
//    if (foValue == null && !isNullable) {
//      loJSON.put("result", "error");
//      loJSON.put("message", "Value cannot be null.");
//      return loJSON;
//    } 
//    switch (columnType) {
//      case -6:
//      case -5:
//      case 4:
//      case 5:
//        if (!(foValue instanceof Number)) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value must be a number.");
//          return loJSON;
//        } 
//        longValue = ((Number)foValue).longValue();
//        if (longValue < -Math.pow(10.0D, columnPrecision) || longValue > Math.pow(10.0D, columnPrecision) - 1.0D) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value is out of range.");
//          return loJSON;
//        } 
//        loJSON.put("result", "success");
//        loJSON.put("message", "Value is valid for this field.");
//        return loJSON;
//      case 6:
//      case 7:
//      case 8:
//        if (!(foValue instanceof Number)) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value must be a number.");
//          return loJSON;
//        } 
//        doubleValue = ((Number)foValue).doubleValue();
//        if (doubleValue < -Math.pow(10.0D, (columnPrecision - columnScale)) || doubleValue > Math.pow(10.0D, (columnPrecision - columnScale))) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value is out of range.");
//          return loJSON;
//        } 
//        loJSON.put("result", "success");
//        loJSON.put("message", "Value is valid for this field.");
//        return loJSON;
//      case 3:
//        if (!(foValue instanceof Number)) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value must be a number.");
//          return loJSON;
//        } 
//        decimalValue = ((Number)foValue).doubleValue();
//        if (decimalValue < -Math.pow(10.0D, (columnPrecision - columnScale)) || decimalValue > Math.pow(10.0D, (columnPrecision - columnScale))) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value is out of range.");
//          return loJSON;
//        } 
//        loJSON.put("result", "success");
//        loJSON.put("message", "Value is valid for this field.");
//        return loJSON;
//      case -1:
//      case 1:
//      case 12:
//        if (!(foValue instanceof String)) {
//          loJSON.put("success", "error");
//          loJSON.put("message", "Value must be a string.");
//          return loJSON;
//        } 
//        stringValue = (String)foValue;
//        if (stringValue.length() > columnDisplaySize) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value exceeds maximum length for the field.");
//          return loJSON;
//        } 
//        loJSON.put("result", "success");
//        loJSON.put("message", "Value is valid for this field.");
//        return loJSON;
//      case 91:
//        if (!(foValue instanceof java.sql.Date) && !(foValue instanceof Date)) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value must be a date object.");
//          return loJSON;
//        } 
//        loJSON.put("result", "success");
//        loJSON.put("message", "Value is valid for this field.");
//        return loJSON;
//      case 92:
//        if (!(foValue instanceof java.sql.Time)) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value must be a time object.");
//          return loJSON;
//        } 
//        loJSON.put("result", "success");
//        loJSON.put("message", "Value is valid for this field.");
//        return loJSON;
//      case 93:
//        if (!(foValue instanceof java.sql.Timestamp)) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value must be a java.sql.Timestamp object.");
//          return loJSON;
//        } 
//        loJSON.put("result", "success");
//        loJSON.put("message", "Value is valid for this field.");
//        return loJSON;
//    } 
//    loJSON.put("result", "error");
//    loJSON.put("message", "Unsupported data type for validation.");
//    return loJSON;
//  }

    /**
     * Sets column value.
     *
     * @param fsColumn - column index name
     * @param foValue - value
     * @return result as success/failed
     */
    @Override
    public JSONObject setValue(String fsColumn, Object foValue) {
        poJSON = new JSONObject();

        try {
            return setValue(MiscUtil.getColumnIndex(poEntity, fsColumn), foValue);
        } catch (SQLException e) {
            e.printStackTrace();
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }
        return poJSON;
    }

    /**
     * Set the edit mode of the entity to new.
     *
     * @return result as success/failed
     */
    @Override
    public JSONObject newRecord() {
        pnEditMode = EditMode.ADDNEW;

        //replace with the primary key column info
        setTransNo(MiscUtil.getNextCode(getTable(), "sTransNox", true, poGRider.getConnection(), poGRider.getBranchCode()+"VSP"));
        setVSPNO(MiscUtil.getNextCode(getTable(), "sVSPNOxxx", true, poGRider.getConnection(), poGRider.getBranchCode()));
        setTransactDte(poGRider.getServerDate());
        setPrinted("0");
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }

    /**
     * Opens a record.
     *
     * @param fsValue - filter values
     * @return result as success/failed
     */
    @Override
    public JSONObject openRecord(String fsValue) {
        poJSON = new JSONObject();

        String lsSQL = getSQL();//MiscUtil.makeSelect(this, ""); //exclude the columns called thru left join

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sTransNox = " + SQLUtil.toSQL(fsValue)
                                                + " GROUP BY a.sTransNox "
                                                );
        ResultSet loRS = poGRider.executeQuery(lsSQL);

        try {
            if (loRS.next()) {
                for (int lnCtr = 1; lnCtr <= loRS.getMetaData().getColumnCount(); lnCtr++) {
                    setValue(lnCtr, loRS.getObject(lnCtr));
                }

                pnEditMode = EditMode.UPDATE;

                poJSON.put("result", "success");
                poJSON.put("message", "Record loaded successfully.");
            } else {
                poJSON.put("result", "error");
                poJSON.put("message", "No record to load.");
            }
        } catch (SQLException e) {
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
        }

        return poJSON;
    }
    
    /**
     * Save the entity.
     *
     * @return result as success/failed
     */
    @Override
    public JSONObject saveRecord() {
        poJSON = new JSONObject();

        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            String lsSQL; //nRsvAmtTl
            if (pnEditMode == EditMode.ADDNEW) {
                //replace with the primary key column info
                setTransNo(MiscUtil.getNextCode(getTable(), "sTransNox", true, poGRider.getConnection(), poGRider.getBranchCode()+"VSP"));
                setVSPNO(MiscUtil.getNextCode(getTable(), "sVSPNOxxx", true, poGRider.getConnection(), poGRider.getBranchCode()));
                setEntryBy(poGRider.getUserID());
                setEntryDte(poGRider.getServerDate());
                setModifiedBy(poGRider.getUserID());
                setModifiedDte(poGRider.getServerDate());
                setLockedBy("");
                setLockedDte(SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
                setCancelldDte(SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
//                setApprovedDte(SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
                setDcStatDte(SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE)); //TODO
//                try {
//                    poEntity.updateObject("dCancelld", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
//                    poEntity.updateObject("dApproved", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
//                    poEntity.updateObject("dLockedDt", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_TIMESTAMP));
//                } catch (SQLException ex) {
//                    Logger.getLogger(Model_Inquiry_Master.class.getName()).log(Level.SEVERE, null, ex);
//                }
                
                lsSQL = MiscUtil.makeSQL(this, psExclude);
                
               // lsSQL = "Select * FROM " + getTable() + " a left join (" + makeSQL() + ") b on a.column1 = b.column "
                if (!lsSQL.isEmpty()) {
                    if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), getTargetBranchCd()) > 0) {
                        poJSON.put("result", "success");
                        poJSON.put("message", "Record saved successfully.");
                    } else {
                        poJSON.put("result", "error");
                        poJSON.put("message", poGRider.getErrMsg());
                    }
                } else {
                    poJSON.put("result", "error");
                    poJSON.put("message", "No record to save.");
                }
            } else {
                Model_VehicleSalesProposal_Master loOldEntity = new Model_VehicleSalesProposal_Master(poGRider);
                //replace with the primary key column info
                JSONObject loJSON = loOldEntity.openRecord(this.getTransNo());
                if ("success".equals((String) loJSON.get("result"))) {
                    //set VSP into open when user modify it. 
//                    if(getTranStat().equals(TransactionStatus.STATE_CLOSED)){
//                        if(loOldEntity.getNetTTotl().compareTo(this.getNetTTotl()) < 0){
//                            setTranStat(TransactionStatus.STATE_OPEN); //set back to for approval
//                        }
//                    }
                    
                    setModifiedBy(poGRider.getUserID());
                    setModifiedDte(poGRider.getServerDate());
                    //Clear Locked by/date
                    setLockedBy("");
                    setLockedDte(SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
//                    try {
//                        poEntity.updateObject("dLockedDt", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
//                    } catch (SQLException ex) {
//                        Logger.getLogger(Model_Inquiry_Master.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    
                    System.out.println("getDelvryDt() : " + getDelvryDt());
                    System.out.println("getCancelldDte() : " + getCancelldDte());
                    
                    //replace the condition based on the primary key column of the record
                    lsSQL = MiscUtil.makeSQL(this, loOldEntity, "sTransNox = " + SQLUtil.toSQL(this.getTransNo()), psExclude);

                    if (!lsSQL.isEmpty()) {
                        if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), getTargetBranchCd()) > 0) {
                            poJSON.put("result", "success");
                            poJSON.put("message", "Record saved successfully.");
                        } else {
                            poJSON.put("result", "error");
                            poJSON.put("message", poGRider.getErrMsg());
                        }
                    } else {
                        poJSON.put("result", "success");
                        poJSON.put("message", "No updates has been made.");
                    }
                } else {
                    poJSON.put("result", "error");
                    poJSON.put("message", "Record discrepancy. Unable to save record.");
                }
            }
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "Invalid update mode. Unable to save record.");
            return poJSON;
        }

        return poJSON;
    }
    
    public JSONObject updateTables(){
        JSONObject loJSON = new JSONObject();
        //Update customer_inquiry status to with VSP
        String lsSQL = "";
        if(getUDRNo() == null || getUDRNo().equals("")){
            lsSQL = "UPDATE customer_inquiry SET" 
                    + " cTranStat = '3'" 
                    + ", dLastUpdt = " + SQLUtil.toSQL(poGRider.getServerDate()) 
                    + " WHERE sTransNox = " + SQLUtil.toSQL(getInqTran());
            if (poGRider.executeQuery(lsSQL, "customer_inquiry", poGRider.getBranchCode(), getTargetBranchCd()) <= 0){
                loJSON.put("result", "error");
                loJSON.put("continue", true);
                loJSON.put("message", "UPDATE CUSTOMER INQUIRY: " + poGRider.getErrMsg() + "; " + poGRider.getMessage());
                return loJSON;
            } 
            
            //Update Vehicle Serial
            if(getSerialID() != null){
                if (!getSerialID().trim().isEmpty()){
                    lsSQL = "UPDATE vehicle_serial SET" +
                            " cSoldStat = '2'" +
                            ", sClientID = " + SQLUtil.toSQL(getClientID()) +
                            ", sCoCltIDx = " + SQLUtil.toSQL(getCoCltID()) +
                            " WHERE sSerialID = " + SQLUtil.toSQL(getSerialID());
                    if (poGRider.executeQuery(lsSQL, "vehicle_serial", poGRider.getBranchCode(), getTargetBranchCd()) <= 0){
                        loJSON.put("result", "error");
                        loJSON.put("continue", true);
                        loJSON.put("message", "UPDATE VEHICLE SERIAL: " + poGRider.getErrMsg() + "; " + poGRider.getMessage());
                        return loJSON;
                    } 
                }
            }
        }
        
        return loJSON;
    }
    
    private String getTargetBranchCd(){
        if (!poGRider.getBranchCode().equals(getBranchCD())){
            return getBranchCD();
        } else {
            return "";
        }
    }
    
    /**
     * Prints all the public methods used<br>
     * and prints the column names of this entity.
     */
    @Override
    public void list() {
        Method[] methods = this.getClass().getMethods();

        System.out.println("--------------------------------------------------------------------");
        System.out.println("LIST OF PUBLIC METHODS FOR " + this.getClass().getName() + ":");
        System.out.println("--------------------------------------------------------------------");
        for (Method method : methods) {
            System.out.println(method.getName());
        }

        try {
            int lnRow = poEntity.getMetaData().getColumnCount();

            System.out.println("--------------------------------------------------------------------");
            System.out.println("ENTITY COLUMN INFO");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Total number of columns: " + lnRow);
            System.out.println("--------------------------------------------------------------------");

            for (int lnCtr = 1; lnCtr <= lnRow; lnCtr++) {
                System.out.println("Column index: " + (lnCtr) + " --> Label: " + poEntity.getMetaData().getColumnLabel(lnCtr));
                if (poEntity.getMetaData().getColumnType(lnCtr) == Types.CHAR
                        || poEntity.getMetaData().getColumnType(lnCtr) == Types.VARCHAR) {

                    System.out.println("Column index: " + (lnCtr) + " --> Size: " + poEntity.getMetaData().getColumnDisplaySize(lnCtr));
                }
            }
        } catch (SQLException e) {
        }

    }
    
    /**
     * Gets the SQL statement for this entity.
     * 
     * @return SQL Statement
     */
    public String makeSQL() {
        return MiscUtil.makeSQL(this, psExclude); //exclude columns called thru left join
    }
    
    /**
     * Gets the SQL Select statement for this entity.
     *
     * @return SQL Select Statement
     */
    public String makeSelectSQL() {
        return MiscUtil.makeSelect(this,psExclude);
    }
    
    public String getSQL(){
        return    " SELECT DISTINCT "                                                                      
                + "   a.sTransNox "                                                               
                + " , a.dTransact "                                                               
                + " , a.sVSPNOxxx "                                                               
                + " , a.dDelvryDt "                                                               
                + " , a.sInqryIDx "                                                               
                + " , a.sClientID "                                                               
                + " , a.sCoCltIDx "                                                               
                + " , a.sSerialID "                                                               
                + " , a.nUnitPrce "                                                               
                + " , a.sRemarksx "                                                               
                + " , a.nAdvDwPmt "                                                               
                + " , a.sOthrDesc "                                                               
                + " , a.nOthrChrg "                                                               
                + " , a.nLaborAmt "                                                               
                + " , a.nAccesAmt "                                                               
                + " , a.nInsurAmt "                                                               
                + " , a.nTPLAmtxx "                                                               
                + " , a.nCompAmtx "                                                               
                + " , a.nLTOAmtxx "                                                               
                + " , a.nChmoAmtx "                                                               
                + " , a.sChmoStat "                                                               
                + " , a.sTPLStatx "                                                               
                + " , a.sCompStat "                                                               
                + " , a.sLTOStatx "                                                               
                + " , a.sInsurTyp "                                                               
                + " , a.nInsurYrx "                                                               
                + " , a.sInsTplCd "                                                               
                + " , a.sInsCodex "                                                               
                + " , a.nToLabDsc "                                                              
                + " , a.nToPrtDsc "                                                              
                + " , a.nPromoDsc "                                                               
                + " , a.nFleetDsc "                                                               
                + " , a.nSPFltDsc "                                                               
                + " , a.nBndleDsc "                                                               
                + " , a.nAddlDscx "                                                               
//                + " , a.nDealrInc "                                                               
                + " , a.cPayModex "                                                               
                + " , a.sBnkAppCD "                                                               
                + " , a.nTranTotl "                                                               
                + " , a.nResrvFee "                                                               
                + " , a.nDownPaym "                                                               
                + " , a.nNetTTotl "                                                               
                + " , a.nAmtPaidx "                                                               
                + " , a.nFrgtChrg "                                                               
                + " , a.nDue2Supx "                                                               
                + " , a.nDue2Dlrx "                                                               
                + " , a.nSPFD2Sup "                                                               
                + " , a.nSPFD2Dlr "                                                               
                + " , a.nPrmD2Sup "                                                               
                + " , a.nPrmD2Dlr "                                                               
                + " , a.sEndPlate "                                                               
                + " , a.sBranchCD "                                                               
                + " , a.nDealrRte "                                                               
                + " , a.nDealrAmt "                                                               
                + " , a.nSlsInRte "                                                               
                + " , a.nSlsInAmt "                                                               
                + " , a.cIsVhclNw "                                                               
                + " , a.cIsVIPxxx "                                                               
                + " , a.sDcStatCd "                                                               
                + " , a.dDcStatDt "                                                               
                + " , a.cPrintedx "                                                               
                + " , a.sLockedBy "                                                               
                + " , a.dLockedDt "                                                                
                + " , a.dCancelld "                                                            
                + " , a.sCancelld "                                                                
                + " , a.cTranStat "                                                                
//                + " , a.sApproved "                                                               
//                + " , a.dApproved "                                                               
                + " , a.sEntryByx "                                                               
                + " , a.dEntryDte "                                                               
                + " , a.sModified "                                                               
                + " , a.dModified "                                                        
                + "  , CASE "          
                + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_CLOSED)+" THEN 'APPROVED' "                     
                + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)+" THEN 'CANCELLED' "                  
                + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_OPEN)+" THEN 'FOR APPROVAL' "                    
                + " 	WHEN a.cTranStat = "+SQLUtil.toSQL(TransactionStatus.STATE_POSTED)+" THEN 'POSTED' "                                      
                + " 	ELSE 'VOID'  "                                                          
                + "    END AS sTranStat "   
                  /*BUYING COSTUMER*/                                                             
                + " , b.sCompnyNm AS sBuyCltNm"                                                               
                + " , b.cClientTp "                                                               
                + " , TRIM(IFNULL(CONCAT( IFNULL(CONCAT(d.sHouseNox,' ') , ''),  "                     
                + "   IFNULL(CONCAT(d.sAddressx,' ') , ''),                 "                     
                + "   IFNULL(CONCAT(e.sBrgyName,' '), ''),                  "                     
                + "   IFNULL(CONCAT(f.sTownName, ', '),''),                 "                     
                + "   IFNULL(CONCAT(g.sProvName),'') )	, '')) AS sAddressx  "                     
                  /*INQUIRY*/                                                            
                + " , h.sInqryIDx AS sInquryID "                                                                
                + " , DATE(h.dTransact) AS dInqryDte "                                                  
                + " , h.sClientID AS sInqCltID "                                                  
                + " , i.sCompnyNm AS sInqCltNm "                                                  
                + " , i.cClientTp AS cInqCltTp "                                                  
                + " , h.sContctID              "                                                  
                + " , j.sCompnyNm AS sContctNm "                                                  
                + " , h.sSourceCD              "                                                  
                + " , h.sSourceNo              "                                                  
                + " , k.sPlatform              "                                                  
                + " , h.sAgentIDx              "                                                  
                + " , l.sCompnyNm AS sAgentNmx "                                                  
                + " , h.sEmployID              "                                                  
                + " , m.sCompnyNm AS sSENamexx "                                                  
                //+ " , SUM(n.nAmountxx) AS nRsvAmtTl "                                                  
                  /*CO-CLIENT*/                                                                   
                + " , o.sCompnyNm AS sCoCltNmx "                                                  
                  /*VEHICLE INFORMATION*/                                                         
                + " , p.sCSNoxxxx "                                                               
                + " , q.sPlateNox "                                                               
                + " , p.sFrameNox "                                                               
                + " , p.sEngineNo "                                                               
                + " , p.sKeyNoxxx "                                                               
                + " , r.sDescript AS sVhclFDsc "
                + " , TRIM(CONCAT_WS(' ',ra.sMakeDesc, rb.sModelDsc, rc.sTypeDesc, r.sTransMsn, r.nYearModl )) AS sVhclDesc "
                + " , rd.sColorDsc "
                  /*BRANCH*/                                                                      
                + " , s.sBranchNm "                                                               
                  /*INSURANCE*/                                                                   
                + " , t.sBrInsNme AS sTPLBrIns "                                                  
                + " , u.sInsurNme AS sTPLInsNm "                                                  
                + " , v.sBrInsNme AS sCOMBrIns "                                                  
                + " , w.sInsurNme AS sCOMInsNm "                                                  
                  /*BANK*/                                                                        
                + " , x.sApplicNo "                                                               
                + " , y.sBrBankNm "                                                               
                + " , z.sBankName " 
                 /*VSP LINKED THRU THE FOLLOWING FORMS*/     
                + " , za.sReferNox AS sUDRNoxxx "
                + " , DATE(za.dTransact) AS sUDRDatex "
                + " , GROUP_CONCAT( DISTINCT zb.sDSNoxxxx) AS sJONoxxxx "
                + " , GROUP_CONCAT( DISTINCT zd.sReferNox) AS sSINoxxxx "    
                + " , ze.sTransNox AS sGatePsNo "
                + " , b.dBirthDte " 
                + " , b.sTaxIDNox " 
                + " , c.cOfficexx " 
                + " , ba.sMobileNo " 
                + " , bb.sEmailAdd "       
                /*TPL PROPOSAL*/                  
                + " , zf.sTransNox AS sTPLTrans " 
                + " , zf.sReferNox AS sTPLRefrn " 
//                + " , zf.dTransact AS sTPLDatex " 
                + " , zf.sInsTypID AS sTPLTypex " 
                /*COMPRE PROPOSAL*/               
                + " , zg.sTransNox AS sCOMTrans " 
                + " , zg.sReferNox AS sCOMRefrn " 
//                + " , zg.dTransact AS sCOMDatex " 
                + " , zg.sInsTypID AS sCOMTypex " 
                /*BOTH PROPOSAL*/                 
                + " , zh.sTransNox AS sBOTTrans " 
                + " , zh.sReferNox AS sBOTRefrn " 
//                + " , zh.dTransact AS sBOTDatex " 
                + " , zh.sInsTypID AS sBOTTypex "
                + "  , r.cVhclSize "
                + "  , rb.sUnitType "
                + "  , rb.sBodyType "                                                                                
                + " , DATE(zi.dApproved) AS dApprovex "                                                                           
                + " , zj.sCompnyNm AS sApprover "  
                + " FROM vsp_master a "                                                           
                 /*BUYING CUSTOMER*/                                                              
                + " LEFT JOIN client_master b ON b.sClientID = a.sClientID "                      
                + " LEFT JOIN client_address c ON c.sClientID = a.sClientID AND c.cPrimaryx = 1 " 
                + " LEFT JOIN addresses d ON d.sAddrssID = c.sAddrssID "                          
                + " LEFT JOIN barangay e ON e.sBrgyIDxx = d.sBrgyIDxx  "                          
                + " LEFT JOIN towncity f ON f.sTownIDxx = d.sTownIDxx  "                          
                + " LEFT JOIN province g ON g.sProvIDxx = f.sProvIDxx  "
                + " LEFT JOIN client_mobile ba ON ba.sClientID = b.sClientID AND ba.cPrimaryx = 1 "
                + " LEFT JOIN client_email_address bb ON bb.sClientID = b.sClientID AND bb.cPrimaryx = 1 "                          
                 /*INQUIRY*/                                                                      
                + " LEFT JOIN customer_inquiry h ON h.sTransNox = a.sInqryIDx "                   
                + " LEFT JOIN client_master i ON i.sClientID = h.sClientID    "                   
                + " LEFT JOIN client_master j ON j.sClientID = h.sContctID    "                   
                + " LEFT JOIN online_platforms k ON k.sTransNox = h.sSourceNo "                   
                + " LEFT JOIN client_master l ON l.sClientID = h.sAgentIDx    "                   
                + " LEFT JOIN ggc_isysdbf.client_master m ON m.sClientID = h.sEmployID    "       
                //+ " LEFT JOIN customer_inquiry_reservation n ON n.sSourceNo = a.sInqryIDx "       
                 /*CO CLIENT*/                                                                    
                + " LEFT JOIN client_master o ON o.sClientID = a.sCoCltIDx "                      
                 /*VEHICLE INFORMATION*/                                                          
                + " LEFT JOIN vehicle_serial p ON p.sSerialID = a.sSerialID "                     
                + " LEFT JOIN vehicle_serial_registration q ON q.sSerialID = a.sSerialID "        
                + " LEFT JOIN vehicle_master r ON r.sVhclIDxx = p.sVhclIDxx "   
                + " LEFT JOIN vehicle_make ra ON ra.sMakeIDxx = r.sMakeIDxx  "
                + " LEFT JOIN vehicle_model rb ON rb.sModelIDx = r.sModelIDx "
                + " LEFT JOIN vehicle_type rc ON rc.sTypeIDxx = r.sTypeIDxx  "
                + " LEFT JOIN vehicle_color rd ON rd.sColorIDx = r.sColorIDx "
                 /*BRANCH*/                                                                       
                + " LEFT JOIN branch s ON s.sBranchCd = a.sBranchCD "                             
                 /*TPL INSURANCE*/                                                                
                + " LEFT JOIN insurance_company_branches t ON t.sBrInsIDx = a.sInsTplCd "         
                + " LEFT JOIN insurance_company u ON u.sInsurIDx = t.sInsurIDx "                  
                 /*COMPREHENSIVE INSURANCE*/                                                      
                + " LEFT JOIN insurance_company_branches v ON v.sBrInsIDx = a.sInsCodex "         
                + " LEFT JOIN insurance_company w ON w.sInsurIDx = v.sInsurIDx "                  
                 /*BANK*/                                                                         
                + " LEFT JOIN bank_application x ON x.sTransNox = a.sBnkAppCD "                   
                + " LEFT JOIN banks_branches y ON y.sBrBankID = x.sBrBankID   "                   
                + " LEFT JOIN banks z ON z.sBankIDxx = y.sBankIDxx            "  
                 /*VSP LINKED THRU THE FOLLOWING FORMS*/                                                             
                + " LEFT JOIN udr_master za ON za.sSourceNo = a.sTransNox AND za.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)  
                + " LEFT JOIN diagnostic_master zb ON zb.sSourceNo = a.sTransNox AND zb.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                + " LEFT JOIN si_master_source zc ON zc.sSourceNo = za.sTransNox  "
                + " LEFT JOIN si_master zd ON zd.sTransNox = zc.sReferNox AND zd.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                + " LEFT JOIN vehicle_gatepass ze ON ze.sSourceCD = a.sTransNox AND ze.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)  
                + " LEFT JOIN insurance_policy_proposal zf ON zf.sVSPNoxxx = a.sTransNox AND zf.sInsTypID = 'y' AND zf.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                + " LEFT JOIN insurance_policy_proposal zg ON zg.sVSPNoxxx = a.sTransNox AND zg.sInsTypID = 'c' AND zg.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                + " LEFT JOIN insurance_policy_proposal zh ON zh.sVSPNoxxx = a.sTransNox AND zh.sInsTypID = 'b' AND zh.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                + " LEFT JOIN transaction_status_history zi ON zi.sSourceNo = a.sTransNox AND zi.cRefrStat = "+ SQLUtil.toSQL(TransactionStatus.STATE_CLOSED) + " AND zi.cTranStat <> "+ SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                + " LEFT JOIN ggc_isysdbf.client_master zj ON zj.sClientID = zi.sApproved " ;
    }
    
    private static String xsDateShort(Date fdValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(fdValue);
        return date;
    }

    private static String xsDateShort(String fsValue) throws org.json.simple.parser.ParseException, java.text.ParseException {
        SimpleDateFormat fromUser = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String lsResult = "";
        lsResult = myFormat.format(fromUser.parse(fsValue));
        return lsResult;
    }
    
    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }
    
    /**
     * Description: Sets the ID of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setTransNo(String fsValue) {
        return setValue("sTransNox", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getTransNo() {
        return (String) getValue("sTransNox");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setTransactDte(Date fdValue) {
        return setValue("dTransact", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getTransactDte() {
        Date date = null;
        if(!getValue("dTransact").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dTransact").toString());
        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setVSPNO(String fsValue) {
        return setValue("sVSPNOxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getVSPNO() {
        return (String) getValue("sVSPNOxxx");
    }
    
//    /**
//     * Description: Sets the Value of this record.
//     *
//     * @param fdValue
//     * @return result as success/failed
//     */
//    public JSONObject setDelvryDt(Date fdValue) {
//        Timestamp timestamp = new Timestamp(((Date) fdValue).getTime());
//        return setValue("dDelvryDt", timestamp); //dDelvryDt datatype is time stamp so convert it into timestamp
//    }
//    
//    /**
//     * @return The Value of this record.
//     */
//    public Date getDelvryDt() {
//        Date date = null;
//        if(getValue("dDelvryDt") == null || getValue("dDelvryDt").equals("")){
//            date = SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE);
//        } else {
//            date = SQLUtil.toDate(xsDateShort((Date) getValue("dDelvryDt")), SQLUtil.FORMAT_SHORT_DATE);
//        }
//        return date;
//    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setDelvryDt(Date fdValue) {
        JSONObject loJSON = new JSONObject();
//        if (!(fdValue instanceof java.sql.Date) && !(fdValue instanceof Date)) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value must be a date object.");
//          return loJSON;
//        } 
//        loJSON.put("result", "success");
//        loJSON.put("message", "Value is valid for this field.");
        return setValue("dDelvryDt", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getDelvryDt() {
        Date date = null;
//        if(!getValue("dInqryDte").toString().isEmpty()){
//            date = CommonUtils.toDate(getValue("dInqryDte").toString());
//        }
//        
        if(getValue("dDelvryDt") == null || getValue("dDelvryDt").equals("")){
            date = SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE);
        } else {
            date = SQLUtil.toDate(xsDateShort((Date) getValue("dDelvryDt")), SQLUtil.FORMAT_SHORT_DATE);
        }
            
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setInqryID(String fsValue) {
        return setValue("sInquryID", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getInqryID() {
        return (String) getValue("sInquryID");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setInqTran(String fsValue) {
        return setValue("sInqryIDx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getInqTran() {
        return (String) getValue("sInqryIDx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setClientID(String fsValue) {
        return setValue("sClientID", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getClientID() {
        return (String) getValue("sClientID");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setCoCltID(String fsValue) {
        return setValue("sCoCltIDx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCoCltID() {
        return (String) getValue("sCoCltIDx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setSerialID(String fsValue) {
        return setValue("sSerialID", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getSerialID() {
        return (String) getValue("sSerialID");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setUnitPrce(BigDecimal fdbValue) {
        return setValue("nUnitPrce", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getUnitPrce() {
        if(getValue("nUnitPrce") == null || getValue("nUnitPrce").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nUnitPrce")));
        }
        //return Double.parseDouble(String.valueOf(getValue("nUnitPrce")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setRemarks(String fsValue) {
        return setValue("sRemarksx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getRemarks() {
        return (String) getValue("sRemarksx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setAdvDwPmt(BigDecimal fdbValue) {
        return setValue("nAdvDwPmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getAdvDwPmt() {
        if(getValue("nAdvDwPmt") == null || getValue("nAdvDwPmt").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nAdvDwPmt")));
        }
//        return new BigDecimal(String.valueOf(getValue("nAdvDwPmt")));
        //return Double.parseDouble(String.valueOf(getValue("nAdvDwPmt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setOthrDesc(String fsValue) {
        return setValue("sOthrDesc", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getOthrDesc() {
        return (String) getValue("sOthrDesc");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setOthrChrg(BigDecimal fdbValue) {
        return setValue("nOthrChrg", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getOthrChrg() {
        if(getValue("nOthrChrg") == null || getValue("nOthrChrg").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nOthrChrg")));
        }
//        return new BigDecimal(String.valueOf(getValue("nOthrChrg")));
//        return Double.parseDouble(String.valueOf(getValue("nOthrChrg")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setLaborAmt(BigDecimal fdbValue) {
        return setValue("nLaborAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getLaborAmt() {
        if(getValue("nLaborAmt") == null || getValue("nLaborAmt").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nLaborAmt")));
        }
//        return new BigDecimal(String.valueOf(getValue("nLaborAmt")));
//        return Double.parseDouble(String.valueOf(getValue("nLaborAmt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setAccesAmt(BigDecimal fdbValue) {
        return setValue("nAccesAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getAccesAmt() {
        if(getValue("nAccesAmt") == null || getValue("nAccesAmt").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nAccesAmt")));
        }
//        return new BigDecimal(String.valueOf(getValue("nAccesAmt")));
//        return Double.parseDouble(String.valueOf(getValue("nAccesAmt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setInsurAmt(BigDecimal fdbValue) {
        return setValue("nInsurAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getInsurAmt() {
        if(getValue("nInsurAmt") == null || getValue("nInsurAmt").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nInsurAmt")));
        }
//        return new BigDecimal(String.valueOf(getValue("nInsurAmt")));
//        return Double.parseDouble(String.valueOf(getValue("nInsurAmt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setTPLAmt(BigDecimal fdbValue) {
        return setValue("nTPLAmtxx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getTPLAmt() {
        if(getValue("nTPLAmtxx") == null || getValue("nTPLAmtxx").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nTPLAmtxx")));
        }
//        return new BigDecimal(String.valueOf(getValue("nTPLAmtxx")));
//        return Double.parseDouble(String.valueOf(getValue("nTPLAmtxx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setCompAmt(BigDecimal fdbValue) {
        return setValue("nCompAmtx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getCompAmt() {
        if(getValue("nCompAmtx") == null || getValue("nCompAmtx").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nCompAmtx")));
        }
//        return new BigDecimal(String.valueOf(getValue("nCompAmtx")));
//        return Double.parseDouble(String.valueOf(getValue("nCompAmtx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setLTOAmt(BigDecimal fdbValue) {
        return setValue("nLTOAmtxx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getLTOAmt() {
        if(getValue("nLTOAmtxx") == null || getValue("nLTOAmtxx").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nLTOAmtxx")));
        }
//        return new BigDecimal(String.valueOf(getValue("nLTOAmtxx")));
//        return Double.parseDouble(String.valueOf(getValue("nLTOAmtxx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setChmoAmt(BigDecimal fdbValue) {
        return setValue("nChmoAmtx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getChmoAmt() {
        if(getValue("nChmoAmtx") == null || getValue("nChmoAmtx").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nChmoAmtx")));
        }
//        return new BigDecimal(String.valueOf(getValue("nChmoAmtx")));
//        return Double.parseDouble(String.valueOf(getValue("nChmoAmtx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setChmoStat(String fsValue) {
        return setValue("sChmoStat", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getChmoStat() {
        return (String) getValue("sChmoStat");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setTPLStat(String fsValue) {
        return setValue("sTPLStatx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getTPLStat() {
        return (String) getValue("sTPLStatx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setCompStat(String fsValue) {
        return setValue("sCompStat", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCompStat() {
        return (String) getValue("sCompStat");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setLTOStat(String fsValue) {
        return setValue("sLTOStatx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getLTOStat() {
        return (String) getValue("sLTOStatx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setInsurTyp(String fsValue) {
        return setValue("sInsurTyp", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getInsurTyp() {
        return (String) getValue("sInsurTyp");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fnValue
     * @return result as success/failed
     */
    public JSONObject setInsurYr(Integer fnValue) {
        return setValue("nInsurYrx", fnValue);
    }

    /**
     * @return The Value of this record.
     */
    public Integer getInsurYr() {
        return Integer.parseInt(String.valueOf(getValue("nInsurYrx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setInsTplCd(String fsValue) {
        return setValue("sInsTplCd", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getInsTplCd() {
        return (String) getValue("sInsTplCd");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setInsCode(String fsValue) {
        return setValue("sInsCodex", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getInsCode() {
        return (String) getValue("sInsCodex");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setToLabDsc(BigDecimal fdbValue) {
        return setValue("nToLabDsc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getToLabDsc() {
        if(getValue("nToLabDsc") == null || getValue("nToLabDsc").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nToLabDsc")));
        }
//        return new BigDecimal(String.valueOf(getValue("nToLabDsc")));
//        return Double.parseDouble(String.valueOf(getValue("nToLabDsc")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setToPrtDsc(BigDecimal fdbValue) {
        return setValue("nToPrtDsc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getToPrtDsc() {
        if(getValue("nToPrtDsc") == null || getValue("nToPrtDsc").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nToPrtDsc")));
        }
//        return new BigDecimal(String.valueOf(getValue("nToPrtDsc")));
//        return Double.parseDouble(String.valueOf(getValue("nToPrtDsc")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setPromoDsc(BigDecimal fdbValue) {
        return setValue("nPromoDsc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getPromoDsc() {
        if(getValue("nPromoDsc") == null || getValue("nPromoDsc").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nPromoDsc")));
        }
//        return new BigDecimal(String.valueOf(getValue("nPromoDsc")));
//        return Double.parseDouble(String.valueOf(getValue("nPromoDsc")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setFleetDsc(BigDecimal fdbValue) {
        return setValue("nFleetDsc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getFleetDsc() {
        if(getValue("nFleetDsc") == null || getValue("nFleetDsc").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nFleetDsc")));
        }
//        return new BigDecimal(String.valueOf(getValue("nFleetDsc")));
//        return Double.parseDouble(String.valueOf(getValue("nFleetDsc")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setSPFltDsc(BigDecimal fdbValue) {
        return setValue("nSPFltDsc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getSPFltDsc() {
        if(getValue("nSPFltDsc") == null || getValue("nSPFltDsc").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nSPFltDsc")));
        }
//        return new BigDecimal(String.valueOf(getValue("nSPFltDsc")));
//        return Double.parseDouble(String.valueOf(getValue("nSPFltDsc")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setBndleDsc(BigDecimal fdbValue) {
        return setValue("nBndleDsc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getBndleDsc() {
        if(getValue("nBndleDsc") == null || getValue("nBndleDsc").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nBndleDsc")));
        }
//        return new BigDecimal(String.valueOf(getValue("nBndleDsc")));
//        return Double.parseDouble(String.valueOf(getValue("nBndleDsc")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setAddlDsc(BigDecimal fdbValue) {
        return setValue("nAddlDscx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getAddlDsc() {
        if(getValue("nAddlDscx") == null || getValue("nAddlDscx").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nAddlDscx")));
        }
//        return new BigDecimal(String.valueOf(getValue("nAddlDscx")));
//        return Double.parseDouble(String.valueOf(getValue("nAddlDscx")));
    }
    
//    /**
//     * Description: Sets the Value of this record.
//     *
//     * @param fdbValue
//     * @return result as success/failed
//     */
//    public JSONObject setDealrInc(BigDecimal fdbValue) {
//        return setValue("nDealrInc", fdbValue);
//    }
//
//    /**
//     * @return The Value of this record.
//     */
//    public BigDecimal getDealrInc() {
//        if(getValue("nDealrInc") == null || getValue("nDealrInc").equals("")){
//            return new BigDecimal("0.00");
//        } else {
//            return new BigDecimal(String.valueOf(getValue("nDealrInc")));
//        }
////        return new BigDecimal(String.valueOf(getValue("nDealrInc")));
////        return Double.parseDouble(String.valueOf(getValue("nDealrInc")));
//    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setPayMode(String fsValue) {
        return setValue("cPayModex", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getPayMode() {
        return (String) getValue("cPayModex");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setBnkAppCD(String fsValue) {
        return setValue("sBnkAppCD", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBnkAppCD() {
        return (String) getValue("sBnkAppCD");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setTranTotl(BigDecimal fdbValue) {
        return setValue("nTranTotl", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getTranTotl() {
        if(getValue("nTranTotl") == null || getValue("nTranTotl").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nTranTotl")));
        }
//        return new BigDecimal(String.valueOf(getValue("nTranTotl")));
//        return Double.parseDouble(String.valueOf(getValue("nTranTotl")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setResrvFee(BigDecimal fdbValue) {
        return setValue("nResrvFee", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getResrvFee() {
        if(getValue("nResrvFee") == null || getValue("nResrvFee").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nResrvFee")));
        }
//        return new BigDecimal(String.valueOf(getValue("nResrvFee")));
//        return Double.parseDouble(String.valueOf(getValue("nResrvFee")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setDownPaym(BigDecimal fdbValue) {
        return setValue("nDownPaym", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getDownPaym() {
        if(getValue("nDownPaym") == null || getValue("nDownPaym").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nDownPaym")));
        }
//        return new BigDecimal(String.valueOf(getValue("nDownPaym")));
//        return Double.parseDouble(String.valueOf(getValue("nDownPaym")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setNetTTotl(BigDecimal fdbValue) {
        return setValue("nNetTTotl", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getNetTTotl() {
        if(getValue("nNetTTotl") == null || getValue("nNetTTotl").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nNetTTotl")));
        }
//        return new BigDecimal(String.valueOf(getValue("nNetTTotl")));
//        return Double.parseDouble(String.valueOf(getValue("nNetTTotl")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setAmtPaid(BigDecimal fdbValue) {
        return setValue("nAmtPaidx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getAmtPaid() {
        if(getValue("nAmtPaidx") == null || getValue("nAmtPaidx").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nAmtPaidx")));
        }
//        return new BigDecimal(String.valueOf(getValue("nAmtPaidx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setFrgtChrg(BigDecimal fdbValue) {
        return setValue("nFrgtChrg", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getFrgtChrg() {
        if(getValue("nFrgtChrg") == null || getValue("nFrgtChrg").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nFrgtChrg")));
        }
//        return new BigDecimal(String.valueOf(getValue("nFrgtChrg")));
//        return Double.parseDouble(String.valueOf(getValue("nFrgtChrg")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setDue2Sup(Double fdbValue) {
        return setValue("nDue2Supx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getDue2Sup() {
        return Double.parseDouble(String.valueOf(getValue("nDue2Supx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setDue2Dlr(Double fdbValue) {
        return setValue("nDue2Dlrx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getDue2Dlr() {
        return Double.parseDouble(String.valueOf(getValue("nDue2Dlrx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setSPFD2Sup(Double fdbValue) {
        return setValue("nSPFD2Sup", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getSPFD2Sup() {
        return Double.parseDouble(String.valueOf(getValue("nSPFD2Sup")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setSPFD2Dlr(Double fdbValue) {
        return setValue("nSPFD2Dlr", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getSPFD2Dlr() {
        return Double.parseDouble(String.valueOf(getValue("nSPFD2Dlr")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setPrmD2Sup(Double fdbValue) {
        return setValue("nPrmD2Sup", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getPrmD2Sup() {
        return Double.parseDouble(String.valueOf(getValue("nPrmD2Sup")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setPrmD2Dlr(Double fdbValue) {
        return setValue("nPrmD2Dlr", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getPrmD2Dlr() {
        return Double.parseDouble(String.valueOf(getValue("nPrmD2Dlr")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setEndPlate(String fsValue) {
        return setValue("sEndPlate", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getEndPlate() {
        return (String) getValue("sEndPlate");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setBranchCD(String fsValue) {
        return setValue("sBranchCD", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBranchCD() {
        return (String) getValue("sBranchCD");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setDealrRte(Double fdbValue) {
        return setValue("nDealrRte", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getDealrRte() {
        return Double.parseDouble(String.valueOf(getValue("nDealrRte")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setDealrAmt(BigDecimal fdbValue) {
        return setValue("nDealrAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getDealrAmt() {
        if(getValue("nDealrAmt") == null || getValue("nDealrAmt").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nDealrAmt")));
        }
//        return new BigDecimal(String.valueOf(getValue("nDealrAmt")));
//        return Double.parseDouble(String.valueOf(getValue("nDealrAmt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setSlsInRte(Double fdbValue) {
        return setValue("nSlsInRte", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getSlsInRte() {
        return Double.parseDouble(String.valueOf(getValue("nSlsInRte")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setSlsInAmt(BigDecimal fdbValue) {
        return setValue("nSlsInAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getSlsInAmt() {
        if(getValue("nSlsInAmt") == null || getValue("nSlsInAmt").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nSlsInAmt")));
        }
//        return new BigDecimal(String.valueOf(getValue("nSlsInAmt")));
//        return Double.parseDouble(String.valueOf(getValue("nSlsInAmt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setIsVhclNw(String fsValue) {
        return setValue("cIsVhclNw", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getIsVhclNw() {
        return (String) getValue("cIsVhclNw");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setIsVIP(String fsValue) {
        return setValue("cIsVIPxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getIsVIP() {
        return (String) getValue("cIsVIPxxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setDcStatCd(String fsValue) {
        return setValue("sDcStatCd", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getDcStatCd() {
        return (String) getValue("sDcStatCd");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setDcStatDte(Date fdValue) {
        Timestamp timestamp = new Timestamp(((Date) fdValue).getTime());
        return setValue("dDcStatDt", timestamp); //dDcStatDt datatype is time stamp so convert it into timestamp
//        return setValue("dDcStatDt", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getDcStatDte() {
        Date date = null;
        if(getValue("dDcStatDt") == null || getValue("dDcStatDt").equals("")){
            date = SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE);
        } else {
            date = SQLUtil.toDate(xsDateShort((Date) getValue("dDcStatDt")), SQLUtil.FORMAT_SHORT_DATE);
        }
//        if(!getValue("dTransact").toString().isEmpty()){
//            date = CommonUtils.toDate(getValue("dDcStatDt").toString());
//        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setPrinted(String fsValue) {
        return setValue("cPrintedx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getPrinted() {
        return (String) getValue("cPrintedx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setLockedBy(String fsValue) {
        return setValue("sLockedBy", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getLockedBy() {
        return (String) getValue("sLockedBy");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setLockedDte(Date fdValue) {
        Timestamp timestamp = new Timestamp(((Date) fdValue).getTime());
        return setValue("dLockedDt", timestamp); //dLockedDt datatype is time stamp so convert it into timestamp
//        return setValue("dLockedDt", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getLockedDte() {
        Date date = null;
//        if(!getValue("dLockedDt").toString().isEmpty()){
//            date = CommonUtils.toDate(getValue("dLockedDt").toString());
//        }
        if(getValue("dLockedDt") == null || getValue("dLockedDt").equals("")){
            date = SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE);
        } else {
            date = SQLUtil.toDate(xsDateShort((Date) getValue("dLockedDt")), SQLUtil.FORMAT_SHORT_DATE);
        }
            
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setTranStat(String fsValue) {
        return setValue("cTranStat", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getTranStat() {
        return (String) getValue("cTranStat");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setCancelld(String fsValue) {
        return setValue("sCancelld", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCancelld() {
        return (String) getValue("sCancelld");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setCancelldDte(Date fdValue) {
        return setValue("dCancelld", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getCancelldDte() {
        Date date = null;
        if(!getValue("dCancelld").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dCancelld").toString());
        }
        
        return date;
    }
    
//    /**
//     * Description: Sets the Value of this record.
//     *
//     * @param fsValue
//     * @return True if the record assignment is successful.
//     */
//    public JSONObject setApproved(String fsValue) {
//        return setValue("sApproved", fsValue);
//    }
//
//    /**
//     * @return The Value of this record.
//     */
//    public String getApproved() {
//        return (String) getValue("sApproved");
//    }
//    
//    /**
//     * Description: Sets the Value of this record.
//     *
//     * @param fdValue
//     * @return result as success/failed
//     */
//    public JSONObject setApprovedDte(Date fdValue) {
//        Timestamp timestamp = new Timestamp(((Date) fdValue).getTime());
//        return setValue("dApproved", timestamp); //dApproved datatype is time stamp so convert it into timestamp
////        return setValue("dApproved", fdValue);
//    }
//
//    /**
//     * @return The Value of this record.
//     */
//    public Date getApprovedDte() {
//        Date date = null;
////        if(!getValue("dApproved").toString().isEmpty()){
////            date = CommonUtils.toDate(getValue("dApproved").toString());
////        }
//        if(getValue("dApproved") == null || getValue("dApproved").equals("")){
//            date = SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE);
//        } else {
//            date = SQLUtil.toDate(xsDateShort((Date) getValue("dApproved")), SQLUtil.FORMAT_SHORT_DATE);
//        }
//            
//        return date;
//    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setEntryBy(String fsValue) {
        return setValue("sEntryByx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getEntryBy() {
        return (String) getValue("sEntryByx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setEntryDte(Date fdValue) {
        return setValue("dEntryDte", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getEntryDte() {
        Date date = null;
        if(!getValue("dEntryDte").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dEntryDte").toString());
        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setModifiedBy(String fsValue) {
        return setValue("sModified", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getModifiedBy() {
        return (String) getValue("sModified");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setModifiedDte(Date fdValue) {
        return setValue("dModified", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getModifiedDte() {
        Date date = null;
        if(!getValue("dModified").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dModified").toString());
        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setBuyCltNm(String fsValue) {
        return setValue("sBuyCltNm", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBuyCltNm() {
        return (String) getValue("sBuyCltNm");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setClientTp(String fsValue) {
        return setValue("cClientTp", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getClientTp() {
        return (String) getValue("cClientTp");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setAddress(String fsValue) {
        return setValue("sAddressx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getAddress() {
        return (String) getValue("sAddressx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setInqryDte(Date fdValue) {
        JSONObject loJSON = new JSONObject();
//        if (!(fdValue instanceof java.sql.Date) && !(fdValue instanceof Date)) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value must be a date object.");
//          return loJSON;
//        } 
//        loJSON.put("result", "success");
//        loJSON.put("message", "Value is valid for this field.");
        return setValue("dInqryDte", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getInqryDte() {
        Date date = null;
//        if(!getValue("dInqryDte").toString().isEmpty()){
//            date = CommonUtils.toDate(getValue("dInqryDte").toString());
//        }
//        
        if(getValue("dInqryDte") == null || getValue("dInqryDte").equals("")){
            date = SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE);
        } else {
            date = SQLUtil.toDate(xsDateShort((Date) getValue("dInqryDte")), SQLUtil.FORMAT_SHORT_DATE);
        }
            
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setInqCltID(String fsValue) {
        return setValue("sInqCltID", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getInqCltID() {
        return (String) getValue("sInqCltID");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setInqCltNm(String fsValue) {
        return setValue("sInqCltNm", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getInqCltNm() {
        return (String) getValue("sInqCltNm");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setInqCltTp(String fsValue) {
        return setValue("cInqCltTp", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getInqCltTp() {
        return (String) getValue("cInqCltTp");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setContctID(String fsValue) {
        return setValue("sContctID", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getContctID() {
        return (String) getValue("sContctID");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setContctNm(String fsValue) {
        return setValue("sContctNm", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getContctNm() {
        return (String) getValue("sContctNm");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setSourceCD(String fsValue) {
        return setValue("sSourceCD", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getSourceCD() {
        return (String) getValue("sSourceCD");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setSourceNo(String fsValue) {
        return setValue("sSourceNo", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getSourceNo() {
        return (String) getValue("sSourceNo");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setPlatform(String fsValue) {
        return setValue("sPlatform", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getPlatform() {
        return (String) getValue("sPlatform");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setAgentID(String fsValue) {
        return setValue("sAgentIDx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getAgentID() {
        return (String) getValue("sAgentIDx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setAgentNm(String fsValue) {
        return setValue("sAgentNmx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getAgentNm() {
        return (String) getValue("sAgentNmx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setEmployID(String fsValue) {
        return setValue("sEmployID", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getEmployID() {
        return (String) getValue("sEmployID");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setSEName(String fsValue) {
        return setValue("sSENamexx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getSEName() {
        return (String) getValue("sSENamexx");
    }
    
//    /**
//     * Description: Sets the Value of this record.
//     *
//     * @param fdbValue
//     * @return result as success/failed
//     */
//    public JSONObject setRsvAmtTl(BigDecimal fdbValue) {
//        return setValue("nRsvAmtTl", fdbValue);
//    }
//
//    /**
//     * @return The Value of this record.
//     */
//    public BigDecimal getRsvAmtTl() {
//        return new BigDecimal(String.valueOf(getValue("nRsvAmtTl")));
////        return Double.parseDouble(String.valueOf(getValue("nRsvAmtTl")));
//    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setCoCltNm(String fsValue) {
        return setValue("sCoCltNmx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCoCltNm() {
        return (String) getValue("sCoCltNmx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setCSNo(String fsValue) {
        return setValue("sCSNoxxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCSNo() {
        return (String) getValue("sCSNoxxxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setPlateNo(String fsValue) {
        return setValue("sPlateNox", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getPlateNo() {
        return (String) getValue("sPlateNox");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setFrameNo(String fsValue) {
        return setValue("sFrameNox", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getFrameNo() {
        return (String) getValue("sFrameNox");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setEngineNo(String fsValue) {
        return setValue("sEngineNo", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getEngineNo() {
        return (String) getValue("sEngineNo");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setKeyNo(String fsValue) {
        return setValue("sKeyNoxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getKeyNo() {
        return (String) getValue("sKeyNoxxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setVhclDesc(String fsValue) {
        return setValue("sVhclDesc", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getVhclDesc() {
        return (String) getValue("sVhclDesc");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setVhclFDsc(String fsValue) {
        return setValue("sVhclFDsc", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getVhclFDsc() {
        return (String) getValue("sVhclFDsc");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setColorDsc(String fsValue) {
        return setValue("sColorDsc", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getColorDsc() {
        return (String) getValue("sColorDsc");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setBranchNm(String fsValue) {
        return setValue("sBranchNm", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBranchNm() {
        return (String) getValue("sBranchNm");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setTPLBrIns(String fsValue) {
        return setValue("sTPLBrIns", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getTPLBrIns() {
        return (String) getValue("sTPLBrIns");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setTPLInsNm(String fsValue) {
        return setValue("sTPLInsNm", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getTPLInsNm() {
        return (String) getValue("sTPLInsNm");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setCOMBrIns(String fsValue) {
        return setValue("sCOMBrIns", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCOMBrIns() {
        return (String) getValue("sCOMBrIns");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setCOMInsNm(String fsValue) {
        return setValue("sCOMInsNm", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCOMInsNm() {
        return (String) getValue("sCOMInsNm");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setApplicNo(String fsValue) {
        return setValue("sApplicNo", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getApplicNo() {
        return (String) getValue("sApplicNo");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setBrBankNm(String fsValue) {
        return setValue("sBrBankNm", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBrBankNm() {
        return (String) getValue("sBrBankNm");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setBankName(String fsValue) {
        return setValue("sBankName", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBankName() {
        return (String) getValue("sBankName");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setUDRNo(String fsValue) {
        return setValue("sUDRNoxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getUDRNo() {
        return (String) getValue("sUDRNoxxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setUDRDate(Date fdValue) {
        JSONObject loJSON = new JSONObject();
//        if (!(fdValue instanceof java.sql.Date) && !(fdValue instanceof Date)) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value must be a date object.");
//          return loJSON;
//        } 
//        loJSON.put("result", "success");
//        loJSON.put("message", "Value is valid for this field.");
        return setValue("sUDRDatex", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getUDRDate() {
        Date date = null;
//        if(!getValue("dInqryDte").toString().isEmpty()){
//            date = CommonUtils.toDate(getValue("dInqryDte").toString());
//        }
//        
        if(getValue("sUDRDatex") == null || getValue("sUDRDatex").equals("")){
            date = SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE);
        } else {
            date = SQLUtil.toDate(xsDateShort((Date) getValue("sUDRDatex")), SQLUtil.FORMAT_SHORT_DATE);
        }
            
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setJONo(String fsValue) {
        return setValue("sJONoxxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getJONo() {
        return (String) getValue("sJONoxxxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setSINo(String fsValue) {
        return setValue("sSINoxxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getSINo() {
        return (String) getValue("sSINoxxxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setGatePsNo(String fsValue) {
        return setValue("sGatePsNo", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getGatePsNo() {
        return (String) getValue("sGatePsNo");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setBirthDte(Date fdValue) {
        return setValue("dBirthDte", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getBirthDte() {
        Date date = null;
        if(getValue("dBirthDte") == null || getValue("dBirthDte").equals("")){
            date = SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE);
        } else {
            date = SQLUtil.toDate(xsDateShort((Date) getValue("dBirthDte")), SQLUtil.FORMAT_SHORT_DATE);
        }
            
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setTaxIDNo(String fsValue) {
        return setValue("sTaxIDNox", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getTaxIDNo() {
        return (String) getValue("sTaxIDNox");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setOffice(String fsValue) {
        return setValue("cOfficexx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getOffice() {
        return (String) getValue("cOfficexx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setMobileNo(String fsValue) {
        return setValue("sMobileNo", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getMobileNo() {
        return (String) getValue("sMobileNo");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setEmailAdd(String fsValue) {
        return setValue("sEmailAdd", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getEmailAdd() {
        return (String) getValue("sEmailAdd");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setTPLTrans(String fsValue) {
        return setValue("sTPLTrans", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getTPLTrans() {
        return (String) getValue("sTPLTrans");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setTPLRefrn(String fsValue) {
        return setValue("sTPLRefrn", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getTPLRefrn() {
        return (String) getValue("sTPLRefrn");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setTPLType(String fsValue) {
        return setValue("sTPLTypex", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getTPLType() {
        return (String) getValue("sTPLTypex");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setCOMTrans(String fsValue) {
        return setValue("sCOMTrans", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCOMTrans() {
        return (String) getValue("sCOMTrans");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setCOMRefrn(String fsValue) {
        return setValue("sCOMRefrn", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCOMRefrn() {
        return (String) getValue("sCOMRefrn");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setCOMType(String fsValue) {
        return setValue("sCOMTypex", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCOMType() {
        return (String) getValue("sCOMTypex");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setBOTTrans(String fsValue) {
        return setValue("sBOTTrans", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBOTTrans() {
        return (String) getValue("sBOTTrans");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setBOTRefrn(String fsValue) {
        return setValue("sBOTRefrn", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBOTRefrn() {
        return (String) getValue("sBOTRefrn");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setBOTType(String fsValue) {
        return setValue("sBOTTypex", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBOTType() {
        return (String) getValue("sBOTTypex");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setVhclSize(String fsValue) {
        return setValue("cVhclSize", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getVhclSize() {
        return (String) getValue("cVhclSize");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setUnitType(String fsValue) {
        return setValue("sUnitType", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getUnitType() {
        return (String) getValue("sUnitType");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setBodyType(String fsValue) {
        return setValue("sBodyType", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBodyType() {
        return (String) getValue("sBodyType");
    } 
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setApproveDte(Date fdValue) {
        return setValue("dApprovex", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getApproveDte() {
        Date date = null;
        if(getValue("dApprovex") == null || getValue("dApprovex").equals("")){
            date = SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE);
        } else {
            date = SQLUtil.toDate(xsDateShort((Date) getValue("dApprovex")), SQLUtil.FORMAT_SHORT_DATE);
        }
            
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setApprover(String fsValue) {
        return setValue("sApprover", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getApprover() {
        return (String) getValue("sApprover");
    }
    
}
