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
public class Model_Activity_Master implements GEntity {

    final String XML = "Model_Activity_Master.xml";
    private final String psDefaultDate = "1900-01-01";

    GRider poGRider;                //application driver
    CachedRowSet poEntity;          //rowset
    JSONObject poJSON;              //json container
    int pnEditMode;                 //edit mode

    /**
     * Entity constructor
     *
     * @param foValue - GhostRider Application Driver
     */
    public Model_Activity_Master(GRider foValue) {
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
            poEntity.updateString("cTranStat", RecordStatus.ACTIVE);
            poEntity.updateObject("dDateFrom", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateObject("dDateThru", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateObject("dApproved", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateDouble("nPropBdgt", 0.00);
            poEntity.updateDouble("nRcvdBdgt", 0.00);
            poEntity.updateInt("nTrgtClnt", 0);
            
            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            pnEditMode = EditMode.UNKNOWN;
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
        return "activity_master";
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
        setActvtyID(MiscUtil.getNextCode(getTable(), "sActvtyID", true, poGRider.getConnection(), poGRider.getBranchCode()+"AC"));
        setActNo(MiscUtil.getNextCode(getTable(), "sActNoxxx", true, poGRider.getConnection(), poGRider.getBranchCode()+"ACT"));

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

        String lsSQL = getSQL(); //MiscUtil.makeSelect(this);

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, " sActvtyID = " + SQLUtil.toSQL(fsValue));

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
        String lsExclude = "sDeptName»sCompnyNm»sBranchNm»sEventTyp»sActTypDs»dApprovex»sApprover";
        poJSON = new JSONObject();

        if (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE) {
            String lsSQL;
            if (pnEditMode == EditMode.ADDNEW) {
                //replace with the primary key column info
                setActvtyID(MiscUtil.getNextCode(getTable(), "sActvtyID", true, poGRider.getConnection(), poGRider.getBranchCode()+"AC"));
                setActNo(MiscUtil.getNextCode(getTable(), "sActNoxxx", true, poGRider.getConnection(), poGRider.getBranchCode()+"ACT"));
                setEntryBy(poGRider.getUserID());
                setEntryDte(poGRider.getServerDate());
                setModified(poGRider.getUserID());
                setModifiedDte(poGRider.getServerDate());
                
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
                Model_Activity_Master loOldEntity = new Model_Activity_Master(poGRider);

                JSONObject loJSON = loOldEntity.openRecord(this.getActvtyID());

                if ("success".equals((String) loJSON.get("result"))) {
                    setModified(poGRider.getUserID());
                    setModifiedDte(poGRider.getServerDate());
                    //replace the condition based on the primary key column of the record
                    lsSQL = MiscUtil.makeSQL(this, loOldEntity, " sActvtyID = " + SQLUtil.toSQL(this.getActvtyID()),lsExclude);

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
        return MiscUtil.makeSQL(this, "");
    }
    
    /**
     * Gets the SQL Select statement for this entity.
     *
     * @return SQL Select Statement
     */
    public String makeSelectSQL() {
        return MiscUtil.makeSelect(this);
    }
    
    public String getSQL() {
        return    " SELECT "                                                                   
                + "   a.sActvtyID "                                                            
                + " , a.sActNoxxx "                                                            
                + " , a.sActTitle "                                                            
                + " , a.sActDescx "                                                            
                + " , a.sActTypID "                                                            
                + " , a.sActSrcex "                                                            
                + " , a.dDateFrom "                                                            
                + " , a.dDateThru "                                                             
                + " , a.sLocation "                                                              
                + " , a.nPropBdgt "                                                            
                + " , a.nRcvdBdgt "                                                            
                + " , a.nTrgtClnt "                                                            
                + " , a.sEmployID "                                                            
                + " , a.sDeptIDxx "                                                            
                + " , a.sLogRemrk "                                                            
                + " , a.sRemarksx "                                                            
                + " , a.cTranStat "                                                            
                + " , a.sEntryByx "                                                            
                + " , a.dEntryDte "                                                            
                + " , a.sApproved "                                                            
                + " , a.dApproved "                                                            
                + " , a.sModified "                                                            
                + " , a.dModified "                                                            
                + " , b.sDeptName "                                                            
                + " , d.sCompnyNm "                                                            
                + " , e.sBranchNm "                                                              
                + " , f.sEventTyp "                                                              
                + " , f.sActTypDs "                                                                                     
                + " , DATE(g.dApproved) AS dApprovex "                                                                           
                + " , h.sCompnyNm AS sApprover "                                                       
                + " FROM activity_master a "                                                   
                + " LEFT JOIN GGC_ISysDBF.Department b ON b.sDeptIDxx = a.sDeptIDxx "          
                + " LEFT JOIN GGC_ISysDBF.Employee_Master001 c ON c.sEmployID = a.sEmployID "  
                + " LEFT JOIN GGC_ISysDBF.Client_Master d ON d.sClientID = a.sEmployID "       
                + " LEFT JOIN branch e ON e.sBranchCd = a.sLocation "                           
                + " LEFT JOIN event_type f ON f.sActTypID = a.sActTypID "
                + " LEFT JOIN transaction_status_history g ON g.sSourceNo = a.sActvtyID AND g.cTranStat <> "+ SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED)
                + " LEFT JOIN ggc_isysdbf.client_master h ON h.sClientID = g.sApproved " ;                     
    }
    
    /**
     * Description: Sets the ID of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setActvtyID(String fsValue) {
        return setValue("sActvtyID", fsValue);
    }

    /**
     * @return The ID of this record.
     */
    public String getActvtyID() {
        return (String) getValue("sActvtyID");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setActNo(String fsValue) {
        return setValue("sActNoxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getActNo() {
        return (String) getValue("sActNoxxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setActTitle(String fsValue) {
        return setValue("sActTitle", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getActTitle() {
        return (String) getValue("sActTitle");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setActDesc(String fsValue) {
        return setValue("sActDescx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getActDesc() {
        return (String) getValue("sActDescx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setActTypID(String fsValue) {
        return setValue("sActTypID", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getActTypID() {
        return (String) getValue("sActTypID");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setActTypDs(String fsValue) {
        return setValue("sActTypDs", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getActTypDs() {
        return (String) getValue("sActTypDs");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setActSrce(String fsValue) {
        return setValue("sActSrcex", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getActSrce() {
        return (String) getValue("sActSrcex");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setDateFrom(Date fdValue) {
        return setValue("dDateFrom", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getDateFrom() {
        Date date = null;
        if(!getValue("dDateFrom").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dDateFrom").toString());
        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setDateThru(Date fdValue) {
        return setValue("dDateThru", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getDateThru() {
        Date date = null;
        if(!getValue("dDateThru").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dDateThru").toString());
        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setLocation(String fsValue) {
        return setValue("sLocation", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getLocation() {
        return (String) getValue("sLocation");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setPropBdgt(Double fdbValue) {
        return setValue("nPropBdgt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getPropBdgt() {
        return Double.parseDouble(String.valueOf(getValue("nPropBdgt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setRcvdBdgt(Double fdbValue) {
        return setValue("nRcvdBdgt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getRcvdBdgt() {
        return Double.parseDouble(String.valueOf(getValue("nRcvdBdgt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fnValue
     * @return result as success/failed
     */
    public JSONObject setTrgtClnt(Integer fnValue) {
        return setValue("nTrgtClnt", fnValue);
    }

    /**
     * @return The Value of this record.
     */
    public Integer getTrgtClnt() {
        return Integer.parseInt(String.valueOf(getValue("nTrgtClnt")));
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
    public JSONObject setDeptID(String fsValue) {
        return setValue("sDeptIDxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getDeptID() {
        return (String) getValue("sDeptIDxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setLogRemrk(String fsValue) {
        return setValue("sLogRemrk", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getLogRemrk() {
        return (String) getValue("sLogRemrk");
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
     * Sets record as active.
     *
     * @param fbValue
     * @return result as success/failed
     */
    public JSONObject setActive(boolean fbValue) {
        return setValue("cTranStat", fbValue ? "1" : "2");
    }

    /**
     * @return If record is active.
     */
    public boolean isActive() {
        return ((String) getValue("cTranStat")).equals("1");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
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
     * Sets the date and time the record was modified.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setEntryDte(Date fdValue) {
        return setValue("dEntryDte", fdValue);
    }

    /**
     * @return The date and time the record was modified.
     */
    public Date getEntryDte() {
        return (Date) getValue("dEntryDte");
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
     * Sets the date and time the record was modified.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setApprovedDte(Date fdValue) {
        return setValue("dApproved", fdValue);
    }

    /**
     * @return The date and time the record was modified.
     */
    public Date getApprovedDte() {
        Date date = null;
        if(!getValue("dApproved").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dApproved").toString());
        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setModified(String fsValue) {
        return setValue("sModified", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getModified() {
        return (String) getValue("sModified");
    }
    
    /**
     * Sets the date and time the record was modified.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setModifiedDte(Date fdValue) {
        return setValue("dModified", fdValue);
    }

    /**
     * @return The date and time the record was modified.
     */
    public Date getModifiedDte() {
        return (Date) getValue("dModified");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setDeptName(String fsValue) {
        return setValue("sDeptName", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getDeptName() {
        return (String) getValue("sDeptName");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setEmpInCharge(String fsValue) {
        return setValue("sCompnyNm", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getEmpInCharge() {
        return (String) getValue("sCompnyNm");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
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
     * @return result as success/failed
     */
    public JSONObject setProvName(String fsValue) {
        return setValue("sProvName", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getProvName() {
        return (String) getValue("sProvName");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return result as success/failed
     */
    public JSONObject setEventTyp(String fsValue) {
        return setValue("sEventTyp", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getEventTyp() {
        return (String) getValue("sEventTyp");
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
        if(!getValue("dApprovex").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dApprovex").toString());
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
