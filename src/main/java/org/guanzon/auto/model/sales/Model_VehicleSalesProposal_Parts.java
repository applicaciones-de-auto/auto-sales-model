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
import java.util.Date;
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
 * @author MIS-PC
 */
public class Model_VehicleSalesProposal_Parts implements GEntity{
    final String XML = "Model_VehicleSalesProposal_Parts.xml";
    private final String psDefaultDate = "1900-01-01";
    private String psTargetBranchCd = "";
    private String psOrigPartDesc = "";

    GRider poGRider;                //application driver
    CachedRowSet poEntity;          //rowset
    JSONObject poJSON;              //json container
    int pnEditMode;                 //edit mode

    /**
     * Entity constructor
     *
     * @param foValue - GhostRider Application Driver
     */
    public Model_VehicleSalesProposal_Parts(GRider foValue) {
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
            poEntity.updateObject("dAddDatex", poGRider.getServerDate());
            poEntity.updateBigDecimal("nUnitPrce", new BigDecimal("0.00"));  
            poEntity.updateBigDecimal("nSelPrice", new BigDecimal("0.00")); 
            poEntity.updateBigDecimal("nNtPrtAmt", new BigDecimal("0.00")); 
            poEntity.updateBigDecimal("nPartsDsc", new BigDecimal("0.00")); 
            poEntity.updateInt("nQuantity", 0); 
            poEntity.updateInt("nReleased", 0); 

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

    /**
     * Gets the table name.
     *
     * @return table name
     */
    @Override
    public String getTable() {
        return "vsp_parts";
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
     *
     * @param fsValue - filter values
     * @param fsValue2 - filter values
     * @return result as success/failed
     */
    public JSONObject openRecord(String fsValue, String fsValue2) {
        poJSON = new JSONObject();

        String lsSQL = getSQL();//MiscUtil.makeSelect(this, ""); //exclude the columns called thru left join

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sTransNox = " + SQLUtil.toSQL(fsValue) + " AND (a.sStockIDx = " + SQLUtil.toSQL(fsValue2) + " OR REPLACE(a.sDescript,' ', '') = " + SQLUtil.toSQL(fsValue2.replace(" ", ""))+ " )" );

        ResultSet loRS = poGRider.executeQuery(lsSQL);

        try {
            if (loRS.next()) {
                for (int lnCtr = 1; lnCtr <= loRS.getMetaData().getColumnCount(); lnCtr++) {
                    setValue(lnCtr, loRS.getObject(lnCtr));
                }
                
                //replace with the primary key column info
                psOrigPartDesc = this.getStockID();
                if(psOrigPartDesc.isEmpty()){
                    psOrigPartDesc = this.getDescript();
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
            String lsSQL;
            String lsExclude = "sDSNoxxxx»dTransact»sCompnyNm»sBarCodex»sPartDesc";
            if (pnEditMode == EditMode.ADDNEW) {
                
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
                Model_VehicleSalesProposal_Parts loOldEntity = new Model_VehicleSalesProposal_Parts(poGRider);

                JSONObject loJSON = loOldEntity.openRecord(this.getTransNo(),psOrigPartDesc);

                if ("success".equals((String) loJSON.get("result"))) {
                    //replace the condition based on the primary key column of the record
                    lsSQL = MiscUtil.makeSQL(this, loOldEntity, "sTransNox = " + SQLUtil.toSQL(this.getTransNo()) + " AND (sStockIDx = " + SQLUtil.toSQL(psOrigPartDesc) + " OR REPLACE(sDescript,' ', '') = " + SQLUtil.toSQL(psOrigPartDesc.replace(" ", ""))+ " )", lsExclude); //+ " )" (sStockIDx = " + SQLUtil.toSQL(this.getStockID()) + " OR 

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
    
    public JSONObject deleteRecord(){
        poJSON = new JSONObject();
        
        String lsSQL = " DELETE FROM "+getTable()+" WHERE "
                    + " sTransNox = " + SQLUtil.toSQL(this.getTransNo())
                    + " AND (sStockIDx = " + SQLUtil.toSQL(this.getStockID())
                    + " OR REPLACE(sDescript,' ', '') = " + SQLUtil.toSQL(this.getDescript().replace(" ", "")) + ") ";
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
        return MiscUtil.makeSQL(this, ""); //exclude columns called thru left join
    }
    
    public String getSQL(){
        return    " SELECT "                                                                                                         
                + "   a.sTransNox "                                                                                                  
                + " , a.nEntryNox "                                                                                                  
                + " , a.sStockIDx "                                                                                                  
                + " , a.nUnitPrce "                                                                                                  
                + " , a.nSelPrice "                                                                                                  
                + " , a.nQuantity "                                                                                                  
                + " , a.nReleased "                                                                                                  
                + " , a.sChrgeTyp "                                                                                                  
                + " , a.sDescript "                                                                                                   
                + " , a.nPartsDsc "                                                                                                    
                + " , a.nNtPrtAmt "                                                                                               
                + " , a.sPartStat "                                                                                                  
                + " , a.dAddDatex "                                                                                                  
                + " , a.sAddByxxx "                                                                                                   
                + " , b.sBarCodex "                                                                                                   
                + " , b.sDescript AS sPartDesc "                                                                                               
                + " , d.sDSNoxxxx "                                                                                                  
                + " , d.dTransact "                                                                                                  
                + " , e.sCompnyNm "                                                                                                   
                + " FROM vsp_parts a "                                                                                               
                + " LEFT JOIN inventory b ON b.sStockIDx = a.sStockIDx "                                                             
                + " LEFT JOIN diagnostic_parts c ON c.sStockIDx = a.sStockIDx "                                                      
                + " LEFT JOIN diagnostic_master d ON d.sTransNox = c.sTransNox AND d.sSourceCD = a.sTransNox AND d.cTranStat = '1' " 
                + " LEFT JOIN GGC_ISysDBF.client_master e ON e.sClientID = a.sAddByxxx "  ;
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
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setStockID(String fsValue) {
        return setValue("sStockIDx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getStockID() {
        return (String) getValue("sStockIDx");
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
        return new BigDecimal(String.valueOf(getValue("nUnitPrce")));
//        return Double.parseDouble(String.valueOf(getValue("nUnitPrce")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setSelPrice(BigDecimal fdbValue) {
        return setValue("nSelPrice", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getSelPrice() {
        return new BigDecimal(String.valueOf(getValue("nSelPrice")));
//        return Double.parseDouble(String.valueOf(getValue("nSelPrice")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fnValue
     * @return result as success/failed
     */
    public JSONObject setQuantity(Integer fnValue) {
        return setValue("nQuantity", fnValue);
    }

    /**
     * @return The Value of this record.
     */
    public Integer getQuantity() {
        return Integer.parseInt(String.valueOf(getValue("nQuantity")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fnValue
     * @return result as success/failed
     */
    public JSONObject setReleased(Integer fnValue) {
        return setValue("nReleased", fnValue);
    }

    /**
     * @return The Value of this record.
     */
    public Integer getReleased() {
        return Integer.parseInt(String.valueOf(getValue("nReleased")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setChrgeTyp(String fsValue) {
        return setValue("sChrgeTyp", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getChrgeTyp() {
        return (String) getValue("sChrgeTyp");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setDescript(String fsValue) {
        return setValue("sDescript", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getDescript() {
        return (String) getValue("sDescript");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setPartsDscount(BigDecimal fdbValue) {
        return setValue("nPartsDsc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getPartsDscount() {
        return new BigDecimal(String.valueOf(getValue("nPartsDsc")));
//        return Double.parseDouble(String.valueOf(getValue("nPartsDsc")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setNtPrtAmt(BigDecimal fdbValue) {
        return setValue("nNtPrtAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public BigDecimal getNtPrtAmt() {
        return new BigDecimal(String.valueOf(getValue("nNtPrtAmt")));
//        return Double.parseDouble(String.valueOf(getValue("nNtPrtAmt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setPartStat(String fsValue) {
        return setValue("sPartStat", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getPartStat() {
        return (String) getValue("sPartStat");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setAddtl(String fsValue) {
        return setValue("cAddtlxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getAddtl() {
        return (String) getValue("cAddtlxxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setAddDate(Date fdValue) {
        return setValue("dAddDatex", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getAddDate() {
        Date date = null;
        if(!getValue("dAddDatex").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dAddDatex").toString());
        }
        
        return date;
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setAddBy(String fsValue) {
        return setValue("sAddByxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getAddBy() {
        return (String) getValue("sAddByxxx");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setDSNo(String fsValue) {
        return setValue("sDSNoxxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getDSNo() {
        return (String) getValue("sDSNoxxxx");
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
    public JSONObject setCompnyNm(String fsValue) {
        return setValue("sCompnyNm", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getCompnyNm() {
        return (String) getValue("sCompnyNm");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setBarCode(String fsValue) {
        return setValue("sBarCodex", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getBarCode() {
        return (String) getValue("sBarCodex");
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fsValue
     * @return True if the record assignment is successful.
     */
    public JSONObject setPartDesc(String fsValue) {
        return setValue("sPartDesc", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getPartDesc() {
        return (String) getValue("sPartDesc");
    }
    
}
