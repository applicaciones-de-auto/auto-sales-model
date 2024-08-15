/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.model.sales;

import static java.lang.String.format;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class Model_Inquiry_FollowUp implements GEntity{
    final String XML = "Model_Inquiry_FollowUp.xml";
    private final String psDefaultDate = "1900-01-01";
    private String psBranchCd;

    GRider poGRider;                //application driver
    CachedRowSet poEntity;          //rowset
    JSONObject poJSON;              //json container
    int pnEditMode;                 //edit mode
    
    /**
     * Entity constructor
     *
     * @param foValue - GhostRider Application Driver
     */
    public Model_Inquiry_FollowUp(GRider foValue){
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
            poEntity.updateObject("dTransact", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateObject("dFollowUp", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateObject("tFollowUp", Time.valueOf("00:00:00"));
            poEntity.updateString("sMethodCd", "0");   
            
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
        return "customer_inquiry_followup";
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
        setReferNo(MiscUtil.getNextCode(getTable(), "sReferNox", true, poGRider.getConnection(), poGRider.getBranchCode()));
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public JSONObject openRecord(String fsValue) {
        poJSON = new JSONObject();

        String lsSQL = getSQL(); //MiscUtil.makeSelect(this);

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sReferNox = " + SQLUtil.toSQL(fsValue));

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
    
    @Override
    public JSONObject saveRecord() {
        poJSON = new JSONObject();
        
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE){
            String lsSQL;
            String lsExclude = "sPlatform»sCompnyNm"; //»
            
            if (pnEditMode == EditMode.ADDNEW){
                setReferNo(MiscUtil.getNextCode(getTable(), "sReferNox", true, poGRider.getConnection(), poGRider.getBranchCode()));
                setEntryBy(poGRider.getUserID());
                setEntryDte(poGRider.getServerDate());
                setEmployID(poGRider.getUserID());
                
                lsSQL = MiscUtil.makeSQL(this, lsExclude);

                if (!lsSQL.isEmpty()) {
                    if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), "") > 0) {
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
            }
//            else {
//                Model_Inquiry_FollowUp loOldEntity = new Model_Inquiry_FollowUp(poGRider);
//                JSONObject loJSON = loOldEntity.openRecord(this.getTransNo());
//                
//                if ("success".equals((String) loJSON.get("result"))){
//                    setModifiedBy(poGRider.getUserID());
//                    setModifiedDate(poGRider.getServerDate());
//                    
//                    lsSQL = MiscUtil.makeSQL(this, loOldEntity, " sTransNox = " + SQLUtil.toSQL(this.getTransNo()), lsExclude);
//                    
//                    if (!lsSQL.isEmpty()) {
//                        if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), "") > 0) {
//                            poJSON.put("result", "success");
//                            poJSON.put("message", "Record saved successfully.");
//                        } else {
//                            poJSON.put("result", "error");
//                            poJSON.put("message", poGRider.getErrMsg());
//                        }
//                    } else {
//                        poJSON.put("result", "success");
//                        poJSON.put("continue", true);
//                        poJSON.put("message", "No updates has been made.");
//                    }
//                } else {
//                    poJSON.put("result", "error");
//                    poJSON.put("message", "Record discrepancy. Unable to save record.");
//                }
//            }
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "Invalid update mode. Unable to save record.");
            return poJSON;
        }
        
        return poJSON;
    }
    
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
    
    private String getSQL(){
        return    " SELECT "                                                    
                + "    a.sTransNox "                                            
                + "  , a.sReferNox "                                            
                + "  , a.dTransact "                                            
                + "  , a.sRemarksx "                                            
                + "  , a.sMessagex "                                            
                + "  , a.sMethodCd "                                            
                + "  , a.sSclMedia "                                            
                + "  , a.dFollowUp "                                            
                + "  , a.tFollowUp "                                            
                + "  , a.sGdsCmptr "                                            
                + "  , a.sMkeCmptr "                                            
                + "  , a.sDlrCmptr "                                            
                + "  , a.sRspnseCd "                                            
                + "  , a.sEmployID "                                            
                + "  , a.sEntryByx "                                            
                + "  , a.dEntryDte "                                            
                + "  , b.sPlatform "
                + "  , c.sCompnyNm "   
                //+ "  , d.sDisValue "                                            
                + " FROM customer_inquiry_followup  a "                         
                + " LEFT JOIN online_platforms b ON b.sTransNox = a.sSclMedia "
                + " LEFT JOIN GGC_ISysDBF.Client_Master c ON c.sClientID = a.sEmployID ";
                //+ " LEFT JOIN xxxform_typelist d ON d.sDataValx = a.sRspnseCd " ;                          
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
    public JSONObject setRemarks(String fsValue) {
        return setValue("sRemarksx", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getRemarks() {
        return (String) getValue("sRemarksx");
    }
    
    /**
     * Description: Sets the ID of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setMessage(String fsValue) {
        return setValue("sMessagex", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getMessage() {
        return (String) getValue("sMessagex");
    }   
    
    /**
     * Description: Sets the ID of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setMethodCd(String fsValue) {
        return setValue("sMethodCd", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getMethodCd() {
        return (String) getValue("sMethodCd");
    }   
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setSclMedia(String fsValue) {
        return setValue("sSclMedia", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getSclMedia() {
        return (String) getValue("sSclMedia");
    }   
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setFollowUpDte(Date fdValue) {
        return setValue("dFollowUp", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getFollowUpDte() {
        Date date = null;
        if(!getValue("dFollowUp").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dFollowUp").toString());
        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param ftValue
     * @return result as success/failed
     */
    public JSONObject setFollowUpTme(Time ftValue) {
        return setValue("tFollowUp", ftValue);
    }

    /**
     * @return The Value of this record.
     */
    public Time getFollowUpTme() {
        Time lTime = Time.valueOf("00:00:00");
        if(getValue("tFollowUp") != null){
            lTime = (Time) getValue("tFollowUp");
        } 
        return lTime;
    }  
    
//    
//    /**
//     * Description: Sets the Value of this record.
//     *
//     * @param ftValue
//     * @return result as success/failed
//     */
//    public JSONObject setFollowUpTme(LocalTime ftValue) {
//        return setValue("tFollowUp", ftValue);
//    }
//
//    /**
//     * @return The Value of this record.
//     */
//    public LocalTime getFollowUpTme() {
//        LocalTime ltTime = LocalTime.MIDNIGHT;
//        if(getValue("tFollowUp") != null){
//            try {
//                // Assuming the value is a String representing time in "HH:mm:ss" format
//                String timeString = getValue("tFollowUp").toString();
//                
//                if (!timeString.isEmpty()) {
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//                    ltTime = LocalTime.parse(timeString, formatter);
//                }
//            } catch (DateTimeParseException e) {
//                System.err.println("Failed to parse the time string: " + e.getMessage());
//            }
//        }
//        
//        return ltTime;
//    }  
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setGdsCmptr(String fsValue) {
        return setValue("sGdsCmptr", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getGdsCmptr() {
        return (String) getValue("sGdsCmptr");
    }   
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setMkeCmptr(String fsValue){
        return setValue("sMkeCmptr", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getMkeCmptr(){
        return (String) getValue("sMkeCmptr");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setDlrCmptr(String fsValue){
        return setValue("sDlrCmptr", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getDlrCmptr(){
        return (String) getValue("sDlrCmptr");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setRspnseCd(String fsValue){
        return setValue("sRspnseCd", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getRspnseCd(){
        return (String) getValue("sRspnseCd");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setEmployID(String fsValue){
        return setValue("sEmployID", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getEmployID(){
        return (String) getValue("sEmployID");
    }
    
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
    public JSONObject setPlatform(String fsValue){
        return setValue("sPlatform", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getPlatform(){
        return (String) getValue("sPlatform");
    }
    
}
