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
public class Model_Inquiry_Reservation  implements GEntity{
    final String XML = "Model_Inquiry_Reservation.xml";
    private final String psDefaultDate = "1900-01-01";
    private String psTargetBranchCd;

    GRider poGRider;                //application driver
    CachedRowSet poEntity;          //rowset
    JSONObject poJSON;              //json container
    int pnEditMode;                 //edit mode
    
     /**
     * Entity constructor
     *
     * @param foValue - GhostRider Application Driver
     */
    public Model_Inquiry_Reservation(GRider foValue){
        if (foValue == null) {
            System.err.println("Application Driver is not set.");
            System.exit(1);
        }

        poGRider = foValue;

        initialize();
    }
    
    private void initialize(){
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);   
            poEntity.updateObject("dTransact", poGRider.getServerDate());
//            poEntity.updateObject("dApproved", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateObject("dApprovex", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateString("cTranStat", TransactionStatus.STATE_OPEN); 
            poEntity.updateString("cResrvTyp","0");   
            poEntity.updateDouble("nAmountxx", 0.00);  
            poEntity.updateInt("nPrintedx",0);    
            
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
        return "customer_inquiry_reservation";
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

    @Override
    public JSONObject newRecord() {
        pnEditMode = EditMode.ADDNEW;
        
        setTransactDte(poGRider.getServerDate());
        setTransNo(MiscUtil.getNextCode(getTable(), "sTransNox", true, poGRider.getConnection(), poGRider.getBranchCode()+"R"));
        //setReferNo(MiscUtil.getNextCode(getTable(), "sReferNox", true, poGRider.getConnection(), poGRider.getBranchCode()));
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public JSONObject openRecord(String fsValue) {
        poJSON = new JSONObject();

        String lsSQL = getSQL(); //MiscUtil.makeSelect(this);

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sTransNox = " + SQLUtil.toSQL(fsValue))
                                                + " GROUP BY a.sTransNox ";
        System.out.println(lsSQL);
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
    
    public JSONObject openRecord(String fsValue, String fsSITranNo) {
        poJSON = new JSONObject();

        String lsSQL = getSQL(); //MiscUtil.makeSelect(this);

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sTransNox = " + SQLUtil.toSQL(fsValue))
                                                + " AND i.sTransNox = " + SQLUtil.toSQL(fsSITranNo);
        System.out.println(lsSQL);
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

//    /**
//     * Opens a record.
//     * @param fsValue - filter values
//     * @param fsValue2 - filter values
//     * @return result as success/failed
//     */
//    public JSONObject openRecord(String fsValue, String fsValue2) {
//        poJSON = new JSONObject();
//
//        String lsSQL = getSQL(); //MiscUtil.makeSelect(this);
//
//        //replace the condition based on the primary key column of the record
//        lsSQL = MiscUtil.addCondition(lsSQL, " a.sTransNox = " + SQLUtil.toSQL(fsValue) + " AND a.sReferNox = " + SQLUtil.toSQL(fsValue2));
//
//        ResultSet loRS = poGRider.executeQuery(lsSQL);
//
//        try {
//            if (loRS.next()) {
//                for (int lnCtr = 1; lnCtr <= loRS.getMetaData().getColumnCount(); lnCtr++) {
//                    setValue(lnCtr, loRS.getObject(lnCtr));
//                }
//
//                pnEditMode = EditMode.UPDATE;
//
//                poJSON.put("result", "success");
//                poJSON.put("message", "Record loaded successfully.");
//            } else {
//                poJSON.put("result", "error");
//                poJSON.put("message", "No record to load.");
//            }
//        } catch (SQLException e) {
//            poJSON.put("result", "error");
//            poJSON.put("message", e.getMessage());
//        }
//
//        return poJSON;
//    }
    
    @Override
    public JSONObject saveRecord() {
        poJSON = new JSONObject();
        
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE){
            String lsSQL;
            String lsExclude = "sCompnyNm»sAddressx»cClientTp»sSITranNo»sSINoxxxx»dSIDatexx»nTranAmtx»dApprovex»sApprover";
            
            if (pnEditMode == EditMode.ADDNEW){
                setTransNo(MiscUtil.getNextCode(getTable(), "sTransNox", true, poGRider.getConnection(), poGRider.getBranchCode()+"R"));
                setReferNo(MiscUtil.getNextCode(getTable(), "sReferNox", true, poGRider.getConnection(), poGRider.getBranchCode()));
                setEntryBy(poGRider.getUserID());
                setEntryDte(poGRider.getServerDate());
                setModifiedBy(poGRider.getUserID());
                setModifiedDate(poGRider.getServerDate());
                
                lsSQL = MiscUtil.makeSQL(this, lsExclude);

                if (!lsSQL.isEmpty()) {
                    if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), psTargetBranchCd) > 0) {
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
                Model_Inquiry_Reservation loOldEntity = new Model_Inquiry_Reservation(poGRider);
                JSONObject loJSON = loOldEntity.openRecord(this.getTransNo());
                
                if ("success".equals((String) loJSON.get("result"))){
                    setModifiedBy(poGRider.getUserID());
                    setModifiedDate(poGRider.getServerDate());
                    
                    lsSQL = MiscUtil.makeSQL(this, loOldEntity, " sTransNox = " + SQLUtil.toSQL(this.getTransNo()), lsExclude);
                    
                    if (!lsSQL.isEmpty()) {
                        if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), psTargetBranchCd) > 0) {
                            poJSON.put("result", "success");
                            poJSON.put("message", "Record saved successfully.");
                        } else {
                            poJSON.put("result", "error");
                            poJSON.put("message", poGRider.getErrMsg());
                        }
                    } else {
                        poJSON.put("result", "success");
                        poJSON.put("continue", true);
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
    
    public void setTargetBranchCd(String fsBranchCd){
        if (!poGRider.getBranchCode().equals(fsBranchCd)){
            psTargetBranchCd = fsBranchCd;
        } else {
            psTargetBranchCd = "";
        }
    }
    
//    public JSONObject cancelRecord(String fsTransNo){
//        poJSON = new JSONObject();
//        
//        String lsSQL = "UPDATE "+getTable()+" SET "
//                + "  cTranStat = '0'"
//                + " ,sModified = " + SQLUtil.toSQL(poGRider.getUserID())
//                + " ,dModified = " + SQLUtil.toSQL(poGRider.getServerDate())
//                + " WHERE sTransNox = " + SQLUtil.toSQL(fsTransNo);
//        if (!lsSQL.isEmpty()) {
//            if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), "") > 0) {
//                poJSON.put("result", "success");
//                poJSON.put("message", "Record cancelled successfully.");
//            } else {
//                poJSON.put("result", "error");
//                poJSON.put("message", poGRider.getErrMsg());
//            }
//        }
//        return poJSON;
//    }
    
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
    
    public String getSQL(){
        return    " SELECT "                                                                      
                + "    a.sTransNox "                                                              
                + "  , a.dTransact "                                                              
                + "  , a.sReferNox "                                                              
                + "  , a.sClientID "                                                              
                + "  , a.nAmountxx "                                                              
                + "  , a.sRemarksx "                                                              
                + "  , a.sSourceCD "                                                              
                + "  , a.sSourceNo "                                                              
                + "  , a.nPrintedx "                                                              
                + "  , a.sResrvCde "                                                              
                + "  , a.cResrvTyp "                                                             
                + "  , a.sTransIDx "    //where the reservation has been linked                                                           
                + "  , a.cTranStat "                                                              
//                + "  , a.sApproved "                                                              
//                + "  , a.dApproved "                                                              
                + "  , a.sEntryByx "                                                              
                + "  , a.dEntryDte "                                                              
                + "  , a.sModified "                                                              
                + "  , a.dModified "                                                              
                + "  , b.sCompnyNm "                                                              
                + "  , b.cClientTp "                                                              
                + "  , IFNULL(CONCAT( IFNULL(CONCAT(d.sHouseNox,' ') , ''), "                     
                + "  IFNULL(CONCAT(d.sAddressx,' ') , ''), "                                      
                + "  IFNULL(CONCAT(e.sBrgyName,' '), ''),  "                                      
                + "  IFNULL(CONCAT(f.sTownName, ', '),''), "                                      
                + "  IFNULL(CONCAT(g.sProvName),'') )	, '') AS sAddressx  "
//                + "  , GROUP_CONCAT(DISTINCT i.sReferNox)  AS sSINoxxxx " 
//                + "  , GROUP_CONCAT(DISTINCT DATE(i.dTransact)) AS dSIDatexx "      
//                + "  , SUM(h.nTranAmtx) "    
                + "  , i.sTransNox  AS sSITranNo " 
                + "  , i.sReferNox  AS sSINoxxxx " 
                + "  , DATE(i.dTransact) AS dSIDatexx "     
                + "  , h.nTranAmtx "                                                                                  
                + " , DATE(j.dApproved) AS dApprovex "                                                                           
                + " , k.sCompnyNm AS sApprover "                  
                + " FROM customer_inquiry_reservation a    "                                      
                + " LEFT JOIN client_master b ON b.sClientID = a.sClientID "                      
                + " LEFT JOIN client_address c ON c.sClientID = a.sClientID AND c.cPrimaryx = 1 " 
                + " LEFT JOIN addresses d ON d.sAddrssID = c.sAddrssID "                          
                + " LEFT JOIN barangay e ON e.sBrgyIDxx = d.sBrgyIDxx  "                          
                + " LEFT JOIN towncity f ON f.sTownIDxx = d.sTownIDxx  "                          
                + " LEFT JOIN province g ON g.sProvIDxx = f.sProvIDxx  "
                + " LEFT JOIN cashier_receivables hh ON hh.sReferNox = a.sTransNox " 
                + " LEFT JOIN si_master_source h ON h.sSourceNo = hh.sTransNox " 
                + " LEFT JOIN si_master i ON i.sTransNox = h.sReferNox AND i.cTranStat <> " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)  
                + " LEFT JOIN transaction_status_history j ON j.sSourceNo = a.sTransNox AND j.cRefrStat = "+ SQLUtil.toSQL(TransactionStatus.STATE_CLOSED) + " AND j.cTranStat <> "+ SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                + " LEFT JOIN ggc_isysdbf.client_master k ON k.sClientID = j.sApproved "    ;                          
    }
    
    /**
     * Description: Sets the ID of this record.
     *
     * @param fsValue
     * @return result as success/failed
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
     * Description: Sets the ID of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setReferNo(String fsValue) {
        return setValue("sReferNox", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getReferNo() {
        return (String) getValue("sReferNox");
    }  
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
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
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setAmount(Double fdbValue) {
        return setValue("nAmountxx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getAmount() {
        return Double.parseDouble(String.valueOf(getValue("nAmountxx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
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
     * @param fsValue
     * @return result as success/failed
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
    
    /** INQUIRY TRANSACTION NUMBER
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
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
     * @param fnValue
     * @return result as success/failed
     */
    public JSONObject setPrinted(Integer fnValue) {
        return setValue("nPrintedx", fnValue);
    }

    /**
     * @return The Value of this record.
     */
    public Integer getPrinted() {
        return Integer.parseInt(String.valueOf(getValue("nPrintedx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setResrvCde(String fsValue) {
        return setValue("sResrvCde", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getResrvCde() {
        return (String) getValue("sResrvCde");
    }   
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setResrvTyp(String fsValue) {
        return setValue("cResrvTyp", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getResrvTyp() {
        return (String) getValue("cResrvTyp");
    }
    
    /**
     * Description: Sets the ID of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setTransID(String fsValue) {
        return setValue("sTransIDx", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getTransID() {
        return (String) getValue("sTransIDx");
    }   
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
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
    
//    /**
//     * Description: Sets the Value of this record.
//     *
//     * @param fsValue
//     * @return result as success/failed
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
//        return setValue("dApproved", fdValue);
//    }
//
//    /**
//     * @return The Value of this record.
//     */
//    public Date getApprovedDte() {
//        Date date = null;
//        if(!getValue("dApproved").toString().isEmpty()){
//            date = CommonUtils.toDate(getValue("dApproved").toString());
//        }
//        
//        return date;
//    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setEntryBy(String fsValue){
        return setValue("sEntryByx", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getEntryBy(){
        return (String) getValue("sEntryByx");
    }
    
    /**
     * Sets the date and time the record was modified.
     * 
     * @param fdValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setEntryDte(Date fdValue){
        return setValue("dEntryDte", fdValue);
    }
    
    /**
     * @return The date and time the record was modified.
     */
    public Date getEntryDte(){
        return (Date) getValue("dEntryDte");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setModifiedBy(String fsValue){
        return setValue("sModified", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getModifiedBy(){
        return (String) getValue("sModified");
    }
    
    /**
     * Sets the date and time the record was modified.
     * 
     * @param fdValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setModifiedDate(Date fdValue){
        return setValue("dModified", fdValue);
    }
    
    /**
     * @return The date and time the record was modified.
     */
    public Date getModifiedDate(){
        return (Date) getValue("dModified");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setCompnyNm(String fsValue){
        return setValue("sCompnyNm", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getCompnyNm(){
        return (String) getValue("sCompnyNm");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setClientTp(String fsValue){
        return setValue("cClientTp", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getClientTp(){
        return (String) getValue("cClientTp");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setAddress(String fsValue){
        return setValue("sAddressx", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getAddress(){
        return (String) getValue("sAddressx");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setSITranNo(String fsValue){
        return setValue("sSITranNo", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getSITranNo(){
        return (String) getValue("sSITranNo");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setSINo(String fsValue){
        return setValue("sSINoxxxx", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getSINo(){
        return (String) getValue("sSINoxxxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setSIDate(Date fdValue) {
        JSONObject loJSON = new JSONObject();
//        if (!(fdValue instanceof java.sql.Date) && !(fdValue instanceof Date)) {
//          loJSON.put("result", "error");
//          loJSON.put("message", "Value must be a date object.");
//          return loJSON;
//        } 
//        loJSON.put("result", "success");
//        loJSON.put("message", "Value is valid for this field.");
        return setValue("dSIDatexx", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getSIDate() {
        Date date = null;
        if(getValue("dSIDatexx") == null || getValue("dSIDatexx").equals("")){
            date = SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE);
        } else {
            date = SQLUtil.toDate(xsDateShort((Date) getValue("dSIDatexx")), SQLUtil.FORMAT_SHORT_DATE);
        }
            
        return date;
    }
    
//    /**
//     * Description: Sets the Value of this record.
//     *
//     * @param fsValue
//     * @return result as success/failed
//     */
//    public JSONObject setSIDate(String fsValue) {
//        return setValue("dSIDatexx", fsValue);
//    }
//
//    /**
//     * @return The Value of this record.
//     */
//    public String getSIDate() {
//        return (String) getValue("dSIDatexx");
//    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setTranAmt(BigDecimal fdbValue) {
        return setValue("nTranAmtx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getTranAmt() {
        if(getValue("nTranAmtx") == null || getValue("nTranAmtx").equals("")){
            return new BigDecimal("0.00");
        } else {
            return new BigDecimal(String.valueOf(getValue("nTranAmtx")));
        }
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
    
}
