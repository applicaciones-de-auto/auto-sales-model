/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.model.sales;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Date Start : 08-01-2024
 */
public class Model_Inquiry_Master implements GEntity {
    
    final String XML = "Model_Inquiry_Master.xml";
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
    public Model_Inquiry_Master(GRider foValue) {
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
            poEntity.updateObject("dTargetDt", poGRider.getServerDate());    
            poEntity.updateObject("dTransact", poGRider.getServerDate());    
            poEntity.updateObject("dLastUpdt", poGRider.getServerDate());  
            poEntity.updateObject("dLockedDt", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateString("cTranStat", "0"); 
            
            poEntity.updateString("cIsVhclNw", "0");  
            poEntity.updateString("cIntrstLv", "a");  
            poEntity.updateString("sSourceCD", "0");   
            
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
        return "customer_inquiry";
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

        //replace with the primary key column info
        setTransactDte(poGRider.getServerDate());
        setTransNo(MiscUtil.getNextCode(getTable(), "sTransNox", true, poGRider.getConnection(), poGRider.getBranchCode()+"IQ"));
        setInqryID(MiscUtil.getNextCode(getTable(), "sInqryIDx", true, poGRider.getConnection(), poGRider.getBranchCode()));
        setBranchCd(poGRider.getBranchCode());
        setBranchNm(poGRider.getBranchName());
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public JSONObject openRecord(String fsValue) {
        pnEditMode = EditMode.UPDATE;
        poJSON = new JSONObject();

        String lsSQL = getSQL();
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sTransNox = " + SQLUtil.toSQL(fsValue));
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

    @Override
    public JSONObject saveRecord() {
        poJSON = new JSONObject();
        
        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE){
            String lsSQL;
            String lsExclude = "sClientNm»cClientTp»sAddressx»sMobileNo»sEmailAdd»sAccountx»sContctNm»sSalesExe»sSalesAgn»sPlatform»sActTitle»sBranchNm»" +
                               "sFrameNox»sEngineNo»sCSNoxxxx»sPlateNox»sDescript";
            
            if (pnEditMode == EditMode.ADDNEW){
                setTransNo(MiscUtil.getNextCode(getTable(), "sTransNox", true, poGRider.getConnection(), poGRider.getBranchCode()+"IQ"));
                setInqryID(MiscUtil.getNextCode(getTable(), "sInqryIDx", true, poGRider.getConnection(), poGRider.getBranchCode()));
                setEntryBy(poGRider.getUserID());
                setEntryDte(poGRider.getServerDate());
                setModifiedBy(poGRider.getUserID());
                setModifiedDate(poGRider.getServerDate());
                setLockedBy(poGRider.getUserID());
                setLockedDt(poGRider.getServerDate());
                setLastUpdt(poGRider.getServerDate());
                
                System.out.println("AGENT ID : " + getAgentID());
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
            } else {
                Model_Inquiry_Master loOldEntity = new Model_Inquiry_Master(poGRider);
                JSONObject loJSON = loOldEntity.openRecord(this.getTransNo());
                System.out.println("AGENT ID : " + getAgentID());
                if ("success".equals((String) loJSON.get("result"))){
                    setModifiedBy(poGRider.getUserID());
                    setModifiedDate(poGRider.getServerDate());
                    setLastUpdt(poGRider.getServerDate());
                    //Clear Locked by/date
                    setLockedBy("");
                    try {
                        poEntity.updateObject("dLockedDt", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
                    } catch (SQLException ex) {
                        Logger.getLogger(Model_Inquiry_Master.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    lsSQL = MiscUtil.makeSQL(this, loOldEntity, " sTransNox = " + SQLUtil.toSQL(this.getTransNo()), lsExclude);
                    
                    if (!lsSQL.isEmpty()) {
                        if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), "") > 0) {
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
    
    public JSONObject lostSale(String fsValue){
        poJSON = new JSONObject();
        
        String lsSQL = " UPDATE "+getTable()+" SET cTranStat = '2' " 
                     + ", sModified = " + SQLUtil.toSQL(poGRider.getUserID()) 
                     + ", dModified = " + SQLUtil.toSQL(poGRider.getServerDate()) 
                     + " WHERE sTransNox = " + SQLUtil.toSQL(fsValue);
        
        if (!lsSQL.isEmpty()) {
            if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), "") > 0) {
                poJSON.put("result", "success");
                poJSON.put("message", "Record saved successfully.");
            } else {
                poJSON.put("result", "error");
                poJSON.put("continue", true);
                poJSON.put("message", poGRider.getErrMsg());
            }
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
                + "   a.sTransNox "                                                                            
                + " , a.sInqryIDx "                                                                       
                + " , a.sBranchCd "                                                                        
                + " , a.dTransact "                                                                        
                + " , a.sEmployID "                                                                        
                + " , a.cIsVhclNw "                                                                        
                + " , a.sVhclIDxx "                                                                        
                + " , a.sClientID "                                                                         
                + " , a.sContctID "                                                                        
                + " , a.sRemarksx "                                                                        
                + " , a.sAgentIDx "                                                                        
                + " , a.dTargetDt "                                                                        
                + " , a.cIntrstLv "                                                                        
                + " , a.sSourceCD "                                                                        
                + " , a.sSourceNo "                                                                        
                + " , a.sTestModl "                                                                        
                + " , a.sActvtyID "                                                                        
                + " , a.dLastUpdt "                                                                           
                + " , a.sLockedBy "                                                                        
                + " , a.dLockedDt "                                                                        
                + " , a.sApproved "                                                                        
                + " , a.sSerialID "                                                                        
                + " , a.sInqryCde "                                                                        
                + " , a.cTranStat "                                                                        
                + " , a.cPayModex "                                                                        
                + " , a.cCustGrpx "                                                                        
                + " , a.sEntryByx "                                                                        
                + " , a.dEntryDte "                                                                        
                + " , a.sModified "                                                                        
                + " , a.dModified "                                                                        
                + " , b.sCompnyNm AS sClientNm"                                                                        
                + " , b.cClientTp "                                                                        
                + " , IFNULL(CONCAT( IFNULL(CONCAT(d.sHouseNox,' ') , ''), "                               
                + "	IFNULL(CONCAT(d.sAddressx,' ') , ''),  "                                               
                + "	IFNULL(CONCAT(e.sBrgyName,' '), ''),   "                                               
                + "	IFNULL(CONCAT(f.sTownName, ', '),''),  "                                               
                + "	IFNULL(CONCAT(g.sProvName),'') )	, '') AS sAddressx  "                                
                + " , h.sMobileNo "                                                                        
                + " , i.sEmailAdd "                                                                        
                + " , j.sAccountx "                                                                        
                + " , k.sCompnyNm AS sContctNm "                                                           
                + " , l.sCompnyNm AS sSalesExe "                                                           
                + " , m.sCompnyNm AS sSalesAgn "                                                           
                + " , n.sPlatform "                                                                        
                + " , o.sActTitle "                                                                        
                + " , p.sBranchNm "                                                                                      
                + " , q.sFrameNox "                                                                                           
                + " , q.sEngineNo "                                                                                                        
                + " , q.sCSNoxxxx "                                                                                   
                + " , r.sPlateNox "                                                                                            
                + " , s.sDescript "                                                                      
                + " FROM customer_inquiry a "                                                              
                + " LEFT JOIN client_master b ON a.sClientID = b.sClientID   "                             
                + " LEFT JOIN client_address c ON c.sClientID = a.sClientID AND c.cPrimaryx = 1 "          
                + " LEFT JOIN addresses d ON d.sAddrssID = c.sAddrssID  "                                  
                + " LEFT JOIN barangay e ON e.sBrgyIDxx = d.sBrgyIDxx   "                                  
                + " LEFT JOIN towncity f ON f.sTownIDxx = d.sTownIDxx   "                                  
                + " LEFT JOIN province g ON g.sProvIDxx = f.sProvIDxx   "                                  
                + " LEFT JOIN client_mobile h ON h.sClientID = a.sClientID AND h.cPrimaryx = 1  "          
                + " LEFT JOIN client_email_address i ON i.sClientID = a.sClientID AND h.cPrimaryx = 1  "   
                + " LEFT JOIN client_social_media j ON j.sClientID = a.sClientID AND j.cRecdStat = 1   "   
                + " LEFT JOIN client_master k ON k.sClientID = a.sContctID   "                             
                + " LEFT JOIN ggc_isysdbf.client_master l ON l.sClientID = a.sEmployID "                   
                + " LEFT JOIN client_master m ON m.sClientID = a.sAgentIDx    "                            
                + " LEFT JOIN online_platforms n ON n.sTransNox = a.sSourceCD "                            
                + " LEFT JOIN activity_master o ON o.sActvtyID = a.sActvtyID  "                            
                + " LEFT JOIN branch p ON p.sBranchCd = a.sBranchCd           "                             
                + " LEFT JOIN vehicle_serial q ON q.sSerialID = a.sSerialID           "    
                + " LEFT JOIN vehicle_serial_registration r ON r.sSerialID = a.sSerialID "              
                + " LEFT JOIN vehicle_master s ON s.sVhclIDxx = q.sVhclIDxx "     ;             
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
    public JSONObject setInqryID(String fsValue) {
        return setValue("sInqryIDx", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getInqryID() {
        return (String) getValue("sInqryIDx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setBranchCd(String fsValue) {
        return setValue("sBranchCd", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBranchCd() {
        return (String) getValue("sBranchCd");
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
     * @return result as success/failed
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
     * @return result as success/failed
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
     * @return result as success/failed
     */
    public JSONObject setVhclID(String fsValue) {
        return setValue("sVhclIDxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getVhclID() {
        return (String) getValue("sVhclIDxx");
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
     * @param fsValue
     * @return result as success/failed
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
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setTargetDt(Date fdValue) {
        return setValue("dTargetDt", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getTargetDt() {
        Date date = null;
        if(!getValue("dTargetDt").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dTargetDt").toString());
        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setIntrstLv(String fsValue) {
        return setValue("cIntrstLv", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getIntrstLv() {
        return (String) getValue("cIntrstLv");
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
    
    /**
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
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setTestModl(String fsValue) {
        return setValue("sTestModl", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getTestModl() {
        return (String) getValue("sTestModl");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setActvtyID(String fsValue) {
        return setValue("sActvtyID", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getActvtyID() {
        return (String) getValue("sActvtyID");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setLastUpdt(Date fdValue) {
        return setValue("dLastUpdt", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getLastUpdt() {
        Date date = null;
        if(!getValue("dLastUpdt").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dLastUpdt").toString());
        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
//    public JSONObject setReserved(String fsValue) {
//        return setValue("sReserved", fsValue);
//    }
//
//    /**
//     * @return The Value of this record.
//     */
//    public String getReserved() {
//        return (String) getValue("sReserved");
//    }
//    
//    /**
//     * Description: Sets the Value of this record.
//     *
//     * @param fdbValue
//     * @return result as success/failed
//     */
//    public JSONObject setRsrvTotl(Double fdbValue) {
//        return setValue("nRsrvTotl", fdbValue);
//    }
//
//    /**
//     * @return The Value of this record.
//     */
//    public Double getRsrvTotl() {
//        return Double.parseDouble(String.valueOf(getValue("nRsrvTotl")));
//    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
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
    public JSONObject setLockedDt(Date fdValue) {
        return setValue("dLockedDt", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getLockedDt() {
        Date date = null;
        if(!getValue("dLockedDt").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dLockedDt").toString());
        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
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
     * @return result as success/failed
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
     * @return result as success/failed
     */
    public JSONObject setInqryCde(String fsValue) {
        return setValue("sInqryCde", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getInqryCde() {
        return (String) getValue("sInqryCde");
    }
    
    /*
        -cTranStat	
        0	For Follow-up
        1	On Process
        2	Lost Sale
        3	VSP
        4	Sold
        5	Cancelled
    */
    
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
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
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
     * @return result as success/failed
     */
    public JSONObject setCustGrp(String fsValue) {
        return setValue("cCustGrpx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCustGrp() {
        return (String) getValue("cCustGrpx");
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
    public JSONObject setClientNm(String fsValue){
        return setValue("sClientNm", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getClientNm(){
        return (String) getValue("sClientNm");
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
    public JSONObject setMobileNo(String fsValue){
        return setValue("sMobileNo", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getMobileNo(){
        return (String) getValue("sMobileNo");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setEmailAdd(String fsValue){
        return setValue("sEmailAdd", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getEmailAdd(){
        return (String) getValue("sEmailAdd");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setAccount(String fsValue){
        return setValue("sAccountx", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getAccount(){
        return (String) getValue("sAccountx");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setContctNm(String fsValue){
        return setValue("sContctNm", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getContctNm(){
        return (String) getValue("sContctNm");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setSalesExe(String fsValue){
        return setValue("sSalesExe", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getSalesExe(){
        return (String) getValue("sSalesExe");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setSalesAgn(String fsValue){
        return setValue("sSalesAgn", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getSalesAgn(){
        return (String) getValue("sSalesAgn");
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
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setActTitle(String fsValue){
        return setValue("sActTitle", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getActTitle(){
        return (String) getValue("sActTitle");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setBranchNm(String fsValue){
        return setValue("sBranchNm", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getBranchNm(){
        return (String) getValue("sBranchNm");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setFrameNo(String fsValue){
        return setValue("sFrameNox", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getFrameNo(){
        return (String) getValue("sFrameNox");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setEngineNo(String fsValue){
        return setValue("sEngineNo", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getEngineNo(){
        return (String) getValue("sEngineNo");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setCSNo(String fsValue){
        return setValue("sCSNoxxxx", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getCSNo(){
        return (String) getValue("sCSNoxxxx");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setPlateNo(String fsValue){
        return setValue("sPlateNox", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getPlateNo(){
        return (String) getValue("sPlateNox");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setDescript(String fsValue){
        return setValue("sDescript", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getDescript(){
        return (String) getValue("sDescript");
    }
    
    
}
