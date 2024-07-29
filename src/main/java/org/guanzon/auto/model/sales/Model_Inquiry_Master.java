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
import java.util.Date;
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.iface.GEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author Arsiela
 */
public class Model_Inquiry_Master implements GEntity {
    final String XML = "Model_Inquiry_Master.xml";
    Connection poConn;          //connection
    CachedRowSet poEntity;      //rowset
    String psMessage;           //warning, success or error message
    GRider poGRider;
    int pnEditMode;
    private String psBranchCd;
    public JSONObject poJSON;
    
    public Model_Inquiry_Master(GRider poValue){
        if (poValue.getConnection() == null){
            System.err.println("Database connection is not set.");
            System.exit(1);
        }
        pnEditMode = EditMode.UNKNOWN;
        poGRider = poValue;
        poConn = poGRider.getConnection();
        
        initialize();
    }
    
    private void initialize(){
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());
            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);    
            poEntity.updateString("cIsVhclNw", "0");  
            poEntity.updateString("cIntrstLv", "a");  
            poEntity.updateString("cTranStat", "0"); 
            poEntity.updateString("sSourceCD", "0");
            poEntity.updateObject("dTargetDt", poGRider.getServerDate());    
            poEntity.updateObject("dTransact", poGRider.getServerDate());      
            
            poEntity.insertRow();
            poEntity.moveToCurrentRow();
            poEntity.absolute(1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }    
    
    @Override
    public String getColumn(int fnCol) {
        try {
            return poEntity.getMetaData().getColumnLabel(fnCol); 
        } catch (SQLException e) {
        }
        return "";
    }

    @Override
    public int getColumn(String fsCol) {
        try {
            return MiscUtil.getColumnIndex(poEntity, fsCol);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

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

    @Override
    public JSONObject setValue(int fnColumn, Object foValue) {
        poJSON = new JSONObject();
        try {
            poEntity.updateObject(fnColumn, foValue);
            poEntity.updateRow();
            poJSON.put("result", "success");
            poJSON.put("value", getValue(fnColumn));
            return poJSON;
        } catch (SQLException e) {
            e.printStackTrace();
            psMessage = e.getMessage();
            poJSON.put("result", "error");
            poJSON.put("message", e.getMessage());
            return poJSON;
        }
    }

    @Override
    public JSONObject setValue(String fsValue, Object foValue) {
        try {
            return setValue(MiscUtil.getColumnIndex(poEntity, fsValue), foValue);
        } catch (SQLException ex) {
            
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", ex.getMessage());
            return poJSON;
            
        }
    }

    @Override
    public JSONObject newRecord() {
        pnEditMode = EditMode.ADDNEW;
        if (psBranchCd.isEmpty()) psBranchCd = poGRider.getBranchCode();
        
        //replace with the primary key column info
//        setClientID(MiscUtil.getNextCode(getTable(), "sClientID", true, poConn, poGRider.getBranchCode()));
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public JSONObject openRecord(String fsValue) {
        pnEditMode = EditMode.UPDATE;
        poJSON = new JSONObject();

        String lsSQL = getSQL();
        lsSQL = MiscUtil.addCondition(getSQL(), "a.sTransNox = " + SQLUtil.toSQL(fsValue));
        System.out.println(lsSQL);
        ResultSet loRS = poGRider.executeQuery(lsSQL);

        try {
            if (loRS.next()){
                for (int lnCtr = 1; lnCtr <= loRS.getMetaData().getColumnCount(); lnCtr++){
                    setValue(lnCtr, loRS.getObject(lnCtr));
                    System.out.println(loRS.getMetaData().getColumnLabel(lnCtr) + " = " + loRS.getString(lnCtr));
                }

                pnEditMode = EditMode.UPDATE;

                poJSON.put("result", "success");
                poJSON.put("message", "Record loaded successfully.");
            } else {
                poJSON.put("result", "error");
                poJSON.put("message", "No record to load.");
            }
            MiscUtil.close(loRS);
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
            String lsExcluded = "sCompnyNm»sMobileNo»sAccountx»sEmailAdd»sAddressx»sSalesExe»sSalesAgn»sPlatform»sActTitle»sBranchNm»cClientTp»sContctNm";
            
            if (pnEditMode == EditMode.ADDNEW){
                setValue("sTransNox", MiscUtil.getNextCode(getTable(), "sTransNox", true, poGRider.getConnection(), psBranchCd));
                setValue("dModified",poGRider.getServerDate());
                lsSQL = MiscUtil.makeSQL(this, lsExcluded);
                
                if (!lsSQL.isEmpty()){
                    if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), "") > 0){
                        poJSON.put("result", "success");
                        poJSON.put("sTransNox", getTransNox());
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
                JSONObject loJSON = loOldEntity.openRecord(this.getTransNox());
                
                if ("success".equals((String) loJSON.get("result"))){
                    setModifiedDate(poGRider.getServerDate());
                    
                    lsSQL = MiscUtil.makeSQL(this, loOldEntity, "sTransNox = " + SQLUtil.toSQL(this.getTransNox()), lsExcluded);
                    
                    if (!lsSQL.isEmpty()){
                        if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), "") > 0){
                            poJSON.put("result", "success");
                            poJSON.put("sTransNox", getTransNox());
                            poJSON.put("message", "Record saved successfully.");
                        } else {
                            poJSON.put("result", "error");
                            poJSON.put("message", poGRider.getErrMsg());
                        }
                    } else {
                        poJSON.put("result", "error");
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
    
    /** 
     * @return The ID of this record. 
     */
    public String getTransNox(){
        return (String) getValue("sTransNox");
    }

    @Override
    public void list() {
        Method[] methods = this.getClass().getMethods();
        
        System.out.println("List of public methods for class " + this.getClass().getName() + ":");
        for (Method method : methods) {
            System.out.println(method.getName());
        }
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
    
    public String getMessage(){
        return psMessage;
    }
    
    private String getSQL(){
        return "SELECT " +
                    " a.sTransNox"  + //1
                    ", a.sBranchCD" + //2
                    ", a.dTransact" + //3
                    ", a.sEmployID" + //4
                    ", a.cIsVhclNw" + //5
                    ", a.sVhclIDxx" + //6
                    ", a.sClientID" + //7
                    ", a.sRemarksx" + //8
                    ", a.sAgentIDx" + //9
                    ", a.dTargetDt" + //10
                    ", a.cIntrstLv" + //11
                    ", a.sSourceCD" + //12
                    ", a.sSourceNo" + //13
                    ", a.sTestModl" + //14
                    ", a.sActvtyID" + //15
                    ", a.dLastUpdt" + //16
                    ", a.nReserved" + //17
                    ", a.nRsrvTotl" + //18
                    ", a.sLockedBy" + //19
                    ", a.sLockedDt" + //20
                    ", a.sApproved" + //21
                    ", a.sSerialID" + //22
                    ", a.sInqryCde" + //23
                    ", a.cTranStat" + //24
                    ", a.sEntryByx" + //25
                    ", a.dEntryDte" + //26
                    ", a.sModified" + //27
                    ", a.dModified" + //28
                    ",IFNULL(b.sCompnyNm,'') as sCompnyNm " +//29
                    ",IFNULL(c.sMobileNo,'') as sMobileNo " +//30
                    ",IFNULL(h.sAccountx,'') as sAccountx " +//31
                    ",IFNULL(i.sEmailAdd,'') as sEmailAdd " +//32
                    ", IFNULL(CONCAT( IFNULL(CONCAT(dd.sHouseNox,' ') , ''), IFNULL(CONCAT(dd.sAddressx,' ') , ''), " +
                    " 	IFNULL(CONCAT(f.sBrgyName,' '), ''), " +
                    " 	IFNULL(CONCAT(e.sTownName, ', '),''), " +
                    " 	IFNULL(CONCAT(g.sProvName),'') )	, '') AS sAddressx " + //33
                    " ,IFNULL(j.sCompnyNm, '') AS sSalesExe   " + //34
                    " ,IFNULL(l.sCompnyNm, '') AS sSalesAgn   " + //35
                    " ,IFNULL(m.sPlatform, '') AS sPlatform   " + //36
                    " ,IFNULL(n.sActTitle, '') AS sActTitle   " + //37
                    " ,IFNULL(k.sBranchNm, '') AS sBranchNm   " + //38
                    " ,IFNULL(b.cClientTp,'') AS cClientTp" + //39
                    " ,IFNULL(a.sContctID,'') AS sContctID" + //40
                    " ,IFNULL(o.sCompnyNm,'') AS sContctNm" + //41
                " FROM  " + getTable() + " a " +
                " LEFT JOIN client_master b ON b.sClientID = a.sClientID" +
                " LEFT JOIN client_mobile c ON c.sClientID = b.sClientID AND c.cPrimaryx = '1' " +
                " LEFT JOIN client_address d ON d.sClientID = b.sClientID AND d.cPrimaryx = '1' " + 
                " LEFT JOIN addresses dd ON dd.sAddrssID = d.sAddrssID" + 
                " LEFT JOIN TownCity e ON e.sTownIDxx = dd.sTownIDxx" +
                " LEFT JOIN Barangay f ON f.sBrgyIDxx = dd.sBrgyIDxx AND f.sTownIDxx = dd.sTownIDxx" + 
                " LEFT JOIN Province g on g.sProvIDxx = e.sProvIDxx" +
                " LEFT JOIN client_social_media h ON h.sClientID = b.sClientID" +
                " LEFT JOIN client_email_address i ON i.sClientID = b.sClientID AND i.cPrimaryx = '1' " +
                " LEFT JOIN ggc_isysdbf.client_master j ON j.sClientID = a.sEmployID  " +
                " LEFT JOIN branch k on k.sBranchCd = a.sBranchCd " +
                " LEFT JOIN client_master l ON l.sClientID = a.sAgentIDx" + 
                " LEFT JOIN online_platforms m ON m.sTransNox = a.sSourceNo" + 
                " LEFT JOIN activity_master n ON n.sActvtyID = a.sActvtyID"  +
                " LEFT JOIN client_master o ON o.sClientID = a.sContctID" ;                                 
    }
    
}
