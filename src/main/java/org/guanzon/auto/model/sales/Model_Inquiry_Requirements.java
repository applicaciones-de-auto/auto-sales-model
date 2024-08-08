/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.guanzon.auto.model.sales;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
 * @author Arsiela
 */
public class Model_Inquiry_Requirements implements GEntity{
    final String XML = "Model_Inquiry_Requirements.xml";
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
    public Model_Inquiry_Requirements(GRider foValue){
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
        return "customer_inquiry_requirements";
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

        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }

    @Override
    public JSONObject openRecord(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Opens a record.
     * @param fsValue - filter values
     * @param fsValue2 - filter values
     * @return result as success/failed
     */
    public JSONObject openRecord(String fsValue, String fsValue2) {
        poJSON = new JSONObject();

        String lsSQL = getSQL(); //MiscUtil.makeSelect(this);

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sTransNox = " + SQLUtil.toSQL(fsValue) + " AND a.sVhclIDxx = " + SQLUtil.toSQL(fsValue2));

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
            String lsExclude = "dReceived»sDescript»cPayModex»cCustGrpx»sCompnyNm";
            
            if (pnEditMode == EditMode.ADDNEW){
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
                Model_Inquiry_Requirements loOldEntity = new Model_Inquiry_Requirements(poGRider);
                JSONObject loJSON = loOldEntity.openRecord(this.getTransNo(), this.getRqrmtCde());
                
                if ("success".equals((String) loJSON.get("result"))){
                    lsSQL = MiscUtil.makeSQL(this, loOldEntity, " sTransNox = " + SQLUtil.toSQL(this.getTransNo()) + " AND sRqrmtCde = " + SQLUtil.toSQL(this.getRqrmtCde()), lsExclude);
                    
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
    
    public JSONObject deleteRecord(){
        poJSON = new JSONObject();
        
        String lsSQL = "DELETE FROM "+getTable()+" WHERE "
                + " sTransNox = " + SQLUtil.toSQL(this.getTransNo())
                + " AND nEntryNox = " + SQLUtil.toSQL(this.getEntryNo())
                + " AND sRqrmtCde = " + SQLUtil.toSQL(this.getRqrmtCde());
        if (!lsSQL.isEmpty()) {
            if (poGRider.executeQuery(lsSQL, getTable(), poGRider.getBranchCode(), "") > 0) {
                poJSON.put("result", "success");
                poJSON.put("message", "Record deleted successfully.");
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
                + "    a.sTransNox "                                                    
                + "  , a.nEntryNox "                                                    
                + "  , a.sRqrmtCde "                                                    
                + "  , a.cRequired "                                                    
                + "  , a.cSubmittd "                                                    
                + "  , a.sReceived "                                                    
                + "  , a.dReceived "                                                    
                + "  , b.sDescript "                                                    
                + "  , d.cPayModex "                                                    
                + "  , d.cCustGrpx "                                                    
                + "  , e.sCompnyNm "                                                    
                + " FROM customer_inquiry_requirements a "                              
                + " LEFT JOIN requirement_source b ON a.sRqrmtCde = b.sRqrmtCde "       
                + " LEFT JOIN customer_inquiry d ON d.sTransNox = a.sTransNox   "       
                + " LEFT JOIN GGC_ISysDBF.Client_Master e ON e.sClientID = a.sReceived " ;                          
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
     * @param fnValue
     * @return result as success/failed
     */
    public JSONObject setEntryNo(Integer fnValue) {
        return setValue("nEntryNox", fnValue);
    }

    /**
     * @return The Value of this record.
     */
    public Integer getEntryNo() {
        return Integer.parseInt(String.valueOf(getValue("nEntryNox")));
    }
    
    /**
     * Description: Sets the ID of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setRqrmtCde(String fsValue) {
        return setValue("sRqrmtCde", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getRqrmtCde() {
        return (String) getValue("sRqrmtCde");
    }
    
    /**
     * Description: Sets the ID of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setRequired(String fsValue) {
        return setValue("cRequired", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getRequired() {
        return (String) getValue("cRequired");
    }
    
    /**
     * Description: Sets the ID of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setSubmittd(String fsValue) {
        return setValue("cSubmittd", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getSubmittd() {
        return (String) getValue("cSubmittd");
    }
    
    /**
     * Description: Sets the ID of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setReceived(String fsValue) {
        return setValue("sReceived", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getReceived() {
        return (String) getValue("sReceived");
    }
    
    /**
     * Sets the date and time the record was modified.
     * 
     * @param fdValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setReceivedDte(Date fdValue){
        return setValue("dReceived", fdValue);
    }
    
    /**
     * @return The date and time the record was modified.
     */
    public Date getReceivedDte(){
        return (Date) getValue("dReceived");
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
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setPayMode(String fsValue){
        return setValue("cPayModex", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getPayMode(){
        return (String) getValue("cPayModex");
    }
    
    /**
     * Sets the user encoded/updated the record.
     * 
     * @param fsValue 
     * @return  True if the record assignment is successful.
     */
    public JSONObject setCustGrp(String fsValue){
        return setValue("cCustGrpx", fsValue);
    }
    
    /**
     * @return The user encoded/updated the record 
     */
    public String getCustGrp(){
        return (String) getValue("cCustGrpx");
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
}
