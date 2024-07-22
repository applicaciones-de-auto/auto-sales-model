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
import org.guanzon.appdriver.iface.GEntity;
import org.json.simple.JSONObject;

/**
 *
 * @author MIS-PC
 */
public class Model_Inquiry_VehiclePriority implements GEntity{
    final String XML = "Model_Inquiry_VehiclePriority.xml";
    Connection poConn;          //connection
    CachedRowSet poEntity;      //rowset
    String psMessage;           //warning, success or error message
    GRider poGRider;
    private String psBranchCd;
    int pnEditMode;
    public JSONObject poJSON;
    public Model_Inquiry_VehiclePriority(GRider poValue){
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
        return "Customer_Inquiry_Vehicle_Priority";
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
    
    /**
     * Sets the Transaction Code of this record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setTransNox(String fsValue){
        return setValue("sTransNox", fsValue);
    }
    
    /**
     * @return The Transaction Code of this record. 
     */
    public String getTransNox(){
        return (String) getValue("sTransNox");
    }
    
    /**
     * Sets the Vehicle id.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setVehicleID(String fsValue){
        return setValue("sVhclIDxx", fsValue);
    }
    
    /**
     * @return The Vehicle id.
     */
    public String getVehicleID(){
        return (String) getValue("sVhclIDxx");
    }
    
    @Override
    public JSONObject newRecord() {
        pnEditMode = EditMode.ADDNEW;
        if (psBranchCd.isEmpty()) psBranchCd = poGRider.getBranchCode();
        
        //replace with the primary key column info
        //setClientID(MiscUtil.getNextCode(getTable(), "sClientID", true, poConn, poGRider.getBranchCode()));
        
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
            String lsExcluded = "sDescript";
            
            if (pnEditMode == EditMode.ADDNEW){
                setValue("dEntryDte",poGRider.getServerDate());
                lsSQL = MiscUtil.makeSQL(this, lsExcluded);
                
                if (!lsSQL.isEmpty()){
                    if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), "") > 0){
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
                Model_Inquiry_VehiclePriority loOldEntity = new Model_Inquiry_VehiclePriority(poGRider);
                JSONObject loJSON = loOldEntity.openRecord(this.getTransNox());
                
                if ("success".equals((String) loJSON.get("result"))){
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

    @Override
    public void list() {
        Method[] methods = this.getClass().getMethods();
        
        System.out.println("List of public methods for class " + this.getClass().getName() + ":");
        for (Method method : methods) {
            System.out.println(method.getName());
        }
    }
    
    public String getMessage(){
        return psMessage;
    }
    
    private String getSQL(){
        return  "SELECT " + 
                    " a.sTransNox " + //1
                    ", a.nPriority" + //2
                    ", a.sVhclIDxx" + //3
                    ", a.sEntryByx" + //4
                    ", a.dEntryDte" + //5
                    ", IFNULL(b.sDescript, '') sDescript" + //6
                " FROM Customer_Inquiry_Vehicle_Priority a" +
                    " LEFT JOIN vehicle_master b ON a.sVhclIDxx = b.sVhclIDxx";                          
    }
}
