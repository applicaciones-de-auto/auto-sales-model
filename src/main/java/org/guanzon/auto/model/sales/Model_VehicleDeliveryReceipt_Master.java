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
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.constant.TransactionStatus;
import org.guanzon.appdriver.iface.GEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class Model_VehicleDeliveryReceipt_Master implements GEntity {
    final String XML = "Model_VehicleDeliveryReceipt_Master.xml";
    private final String psDefaultDate = "1900-01-01";
    private String psBranchCd;
    private String psExclude = "sTranStat»sBuyCltNm»cClientTp»sAddressx»sVSPTrans»dVSPDatex»sVSPNOxxx»cIsVhclNw»dDelvryDt»sInqryIDx»sCoCltNmx»"
                                + "sCSNoxxxx»sPlateNox»sFrameNox»sEngineNo»sKeyNoxxx»sVhclDesc»sBranchNm»sBranchCD";//» 
    
    GRider poGRider;                //application driver
    CachedRowSet poEntity;          //rowset
    JSONObject poJSON;              //json container
    int pnEditMode;                 //edit mode

    /**
     * Entity constructor
     *
     * @param foValue - GhostRider Application Driver
     */
    public Model_VehicleDeliveryReceipt_Master(GRider foValue) {
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
            poEntity.updateObject("dVSPDatex", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));  
            poEntity.updateObject("dDelvryDt", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));  
            poEntity.updateString("cTranStat", TransactionStatus.STATE_OPEN); //TransactionStatus.STATE_OPEN why is the value of STATE_OPEN is 0 while record status active is 1
            //RecordStatus.ACTIVE
            poEntity.updateString("cCustType", "0");  
            poEntity.updateString("cIsVhclNw", "0");  
            
            poEntity.updateBigDecimal("nGrossAmt", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nDiscount", new BigDecimal("0.00"));
            poEntity.updateBigDecimal("nTranTotl", new BigDecimal("0.00"));

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
    
    @Override
    public String getTable() {
        return "udr_master";
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
        setTransNo(MiscUtil.getNextCode(getTable(), "sTransNox", true, poGRider.getConnection(), poGRider.getBranchCode()+"VDR"));
        setReferNo(MiscUtil.getNextCode(getTable(), "sReferNox", true, poGRider.getConnection(), poGRider.getBranchCode()));
        setTransactDte(poGRider.getServerDate());
        
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

        String lsSQL = getSQL(); //makeSelectSQL(); // MiscUtil.makeSelect(this, psExclude) getSQL();//exclude the columns called thru left join

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sTransNox = " + SQLUtil.toSQL(fsValue)
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
                setTransNo(MiscUtil.getNextCode(getTable(), "sTransNox", true, poGRider.getConnection(), poGRider.getBranchCode()+"VDR"));
                setReferNo(MiscUtil.getNextCode(getTable(), "sReferNox", true, poGRider.getConnection(), poGRider.getBranchCode()));
                setEntryBy(poGRider.getUserID());
                setEntryDte(poGRider.getServerDate());
                setModifiedBy(poGRider.getUserID());
                setModifiedDte(poGRider.getServerDate());
                
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
                Model_VehicleDeliveryReceipt_Master loOldEntity = new Model_VehicleDeliveryReceipt_Master(poGRider);
                //replace with the primary key column info
                JSONObject loJSON = loOldEntity.openRecord(this.getTransNo());
                if ("success".equals((String) loJSON.get("result"))) {
                    setModifiedBy(poGRider.getUserID());
                    setModifiedDte(poGRider.getServerDate());
                    
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
    
//    public JSONObject updateTables(){
//        JSONObject loJSON = new JSONObject();
//        //Update customer_inquiry status to with VSP
//        String lsSQL = "";
//        String lsInqStat = "";
//        String lsVhclStat = "";
//        
//        if(getTranStat().equals(TransactionStatus.STATE_CANCELLED)){
//            lsInqStat = "3"; //Set Inquiry Status to WITH VSP
//            if(getCustType().equals("0")){ //CUSTOMER
//                lsVhclStat = "2"; //Set Vehicle Status to VSP
//            } else { //SUPPLIER
//                lsVhclStat = "1"; //Set Vehicle Status to AVAILABLE  
//            }
//        } else {
//            lsInqStat = "4"; //Set Inquiry Status to 
//            lsVhclStat = "3"; //Set Vehicle Status to SOLD
//        }
//        
//        if(getInqTran() != null){
//            if(!getInqTran().trim().isEmpty()){
//                lsSQL = "UPDATE customer_inquiry SET" 
//                        + " cTranStat = "+ SQLUtil.toSQL(lsInqStat) 
//                        + " dLastUpdt = " + SQLUtil.toSQL(poGRider.getServerDate()) 
//                        + " WHERE sTransNox = " + SQLUtil.toSQL(getInqTran());
//                if (poGRider.executeQuery(lsSQL, "customer_inquiry", poGRider.getBranchCode(), getTargetBranchCd()) <= 0){
//                    loJSON.put("result", "error");
//                    loJSON.put("continue", true);
//                    loJSON.put("message", "UPDATE CUSTOMER INQUIRY: " + poGRider.getErrMsg() + "; " + poGRider.getMessage());
//                    return loJSON;
//                } 
//            }
//        }
//
//        //Update Vehicle Serial
//        if(getSerialID() != null){
//            if (!getSerialID().trim().isEmpty()){
//                lsSQL = "UPDATE vehicle_serial SET " 
//                        + " cSoldStat = " + SQLUtil.toSQL(lsVhclStat) 
//                        + " WHERE sSerialID = " + SQLUtil.toSQL(getSerialID());
//                if (poGRider.executeQuery(lsSQL, "vehicle_serial", poGRider.getBranchCode(), getTargetBranchCd()) <= 0){
//                    loJSON.put("result", "error");
//                    loJSON.put("continue", true);
//                    loJSON.put("message", "UPDATE VEHICLE SERIAL: " + poGRider.getErrMsg() + "; " + poGRider.getMessage());
//                    return loJSON;
//                } 
//            }
//        }
//        
//        
//        return loJSON;
//    }
    
    private String getTargetBranchCd(){
        if(getBranchCD() != null) {
            if (!poGRider.getBranchCode().equals(getBranchCD())){
                return getBranchCD();
            } else {
                return "";
            }
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
        return MiscUtil.makeSelect(this, psExclude);
    }
    
    public String getSQL(){
        return    " SELECT "                                                                                       
                + "    a.sTransNox "                                                                              
                + "  , a.dTransact "                                                                              
                + "  , a.cCustType "                                                                              
                + "  , a.sClientID "                                                                              
                + "  , a.sSerialID "                                                                              
                + "  , a.sReferNox "                                                                              
                + "  , a.sRemarksx "                                                                              
                + "  , a.nGrossAmt "                                                                              
                + "  , a.nDiscount "                                                                              
                + "  , a.nTranTotl "                                                                              
                + "  , a.sPONoxxxx "                                                                              
                + "  , a.sSourceCd "                                                                              
                + "  , a.sSourceNo "                                                                              
                + "  , a.cPrintedx "                                                                              
                + "  , a.sPrepared "                                                                              
                + "  , a.sApproved "                                                                              
                + "  , a.cCallStat "                                                                              
                + "  , a.cTranStat "                                                                              
                + "  , a.sEntryByx "                                                                              
                + "  , a.dEntryDte "                                                                              
                + "  , a.sModified "                                                                              
                + "  , a.dModified "                                                       
                + "  , CASE "          
                + " 	WHEN a.cTranStat = "+ SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)+" THEN 'CANCELLED' "     
                + " 	WHEN a.cTranStat = "+ SQLUtil.toSQL(TransactionStatus.STATE_CLOSED)+" THEN 'CLOSED' "        
                + " 	WHEN a.cTranStat = "+ SQLUtil.toSQL(TransactionStatus.STATE_OPEN)+" THEN 'ACTIVE' "          
                + " 	WHEN a.cTranStat = "+ SQLUtil.toSQL(TransactionStatus.STATE_POSTED)+" THEN 'POSTED' "                             
                + " 	ELSE 'ACTIVE'  "                                                          
                + "    END AS sTranStat "                                                                                
                /*BUYING COSTUMER*/                                                                               
                + "  , b.sCompnyNm AS sBuyCltNm "                                                                 
                + "  , b.cClientTp "                                                                              
                + "  , TRIM(IFNULL(CONCAT( IFNULL(CONCAT(d.sHouseNox,' ') , ''), "                                
                + "     IFNULL(CONCAT(d.sAddressx,' ') , ''),                    "                                
                + "     IFNULL(CONCAT(e.sBrgyName,' '), ''),                     "                                
                + "     IFNULL(CONCAT(f.sTownName, ', '),''),                    "                                
                + "     IFNULL(CONCAT(g.sProvName),'') )	, '')) AS sAddressx    "                                
                /*VSP*/                                                                                           
                + "  , h.sTransNox AS sVSPTrans"                                                                              
                + "  , DATE(h.dTransact) AS dVSPDatex "                                                                              
                + "  , h.sVSPNOxxx "                                                                             
                + "  , h.cIsVhclNw "                                                                             
                + "  , DATE(h.dDelvryDt) AS dDelvryDt "                                                                              
                + "  , h.sInqryIDx "                                                                               
                + "  , h.sBranchCD "                                                                              
                /*CO-CLIENT*/                                                                                     
                + "  , i.sCompnyNm AS sCoCltNmx "                                                                 
                /*VEHICLE INFORMATION*/                                                                           
                + "  , j.sCSNoxxxx "                                                                              
                + "  , k.sPlateNox "                                                                              
                + "  , j.sFrameNox "                                                                              
                + "  , j.sEngineNo "                                                                              
                + "  , j.sKeyNoxxx "                                                                              
                + "  , l.sDescript AS sVhclDesc "                                                                 
                /*BRANCH*/                                                                                        
                + "  , m.sBranchNm     "                                                                          
                + "  FROM udr_master a "                                                                          
                 /*BUYING CUSTOMER*/                                                                              
                + "  LEFT JOIN client_master b ON b.sClientID = a.sClientID "                                     
                + "  LEFT JOIN client_address c ON c.sClientID = a.sClientID AND c.cPrimaryx = 1 "                
                + "  LEFT JOIN addresses d ON d.sAddrssID = c.sAddrssID "                                         
                + "  LEFT JOIN barangay e ON e.sBrgyIDxx = d.sBrgyIDxx  "                                         
                + "  LEFT JOIN towncity f ON f.sTownIDxx = d.sTownIDxx  "                                         
                + "  LEFT JOIN province g ON g.sProvIDxx = f.sProvIDxx  "                                         
                + "  LEFT JOIN client_mobile ba ON ba.sClientID = b.sClientID AND ba.cPrimaryx = 1 "              
                + "  LEFT JOIN client_email_address bb ON bb.sClientID = b.sClientID AND bb.cPrimaryx = 1 "       
                /*VSP*/                                                                                           
                + "  LEFT JOIN vsp_master h ON h.sTransNox = a.sSourceNo "                                        
                /*CO CLIENT*/                                                                                     
                + "  LEFT JOIN client_master i ON i.sClientID = h.sCoCltIDx "                                     
                /*VEHICLE INFORMATION*/                                                                           
                + "  LEFT JOIN vehicle_serial j ON j.sSerialID = a.sSerialID "                                    
                + "  LEFT JOIN vehicle_serial_registration k ON k.sSerialID = a.sSerialID "                       
                + "  LEFT JOIN vehicle_master l ON l.sVhclIDxx = j.sVhclIDxx "                                    
                /*BRANCH*/                                                                                        
                + "  LEFT JOIN branch m ON m.sBranchCd = h.sBranchCD "   ;
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
    public JSONObject setCustType(String fsValue) {
        return setValue("cCustType", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCustType() {
        return (String) getValue("cCustType");
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
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setReferNo(String fsValue) {
        return setValue("sReferNox", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getReferNo() {
        return (String) getValue("sReferNox");
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
    public JSONObject setGrossAmt(BigDecimal fdbValue) {
        return setValue("nGrossAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getGrossAmt() {
        if(getValue("nGrossAmt") == null || getValue("nGrossAmt").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nGrossAmt")));
        }
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setDiscount(BigDecimal fdbValue) {
        return setValue("nDiscount", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getDiscount() {
        if(getValue("nDiscount") == null || getValue("nDiscount").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nDiscount")));
        }
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
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setPONo(String fsValue) {
        return setValue("sPONoxxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getPONo() {
        return (String) getValue("sPONoxxxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setSourceCd(String fsValue) {
        return setValue("sSourceCd", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getSourceCd() {
        return (String) getValue("sSourceCd");
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
    public JSONObject setPrepared(String fsValue) {
        return setValue("sPrepared", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getPrepared() {
        return (String) getValue("sPrepared");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setApproved(String fsValue) {
        return setValue("sApproved", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getApproved() {
        return (String) getValue("sApproved");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setCallStat(String fsValue) {
        return setValue("cCallStat", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCallStat() {
        return (String) getValue("cCallStat");
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
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setVSPTrans(String fsValue) {
        return setValue("sVSPTrans", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getVSPTrans() {
        return (String) getValue("sVSPTrans");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setVSPDate(Date fdValue) {
        return setValue("dVSPDatex", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getVSPDate() {
        Date date = null;
        if(getValue("dVSPDatex") == null || getValue("dVSPDatex").equals("")){
            date = SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE);
        } else {
            date = SQLUtil.toDate(xsDateShort((Date) getValue("dVSPDatex")), SQLUtil.FORMAT_SHORT_DATE);
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
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setDelvryDte(Date fdValue) {
        return setValue("dDelvryDt", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getDelvryDte() {
        Date date = null;
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
    public JSONObject setBranchCD(String fsValue) {
        return setValue("sBranchCD", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBranchCD() {
        String lsValue = "";
        if(getValue("sBranchCD") != null ){
            lsValue = (String) getValue("sBranchCD");
        }
        return lsValue;
    }
    
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
    public JSONObject setBranchNm(String fsValue) {
        return setValue("sBranchNm", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBranchNm() {
        return (String) getValue("sBranchNm");
    }
    
}
