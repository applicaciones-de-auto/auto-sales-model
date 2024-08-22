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
            poEntity.updateObject("dDelvryDt", poGRider.getServerDate());   
            poEntity.updateObject("dLockedDt", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateObject("dCancelld", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateObject("dApproved", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
            poEntity.updateString("cTranStat", TransactionStatus.STATE_OPEN); 
            
            poEntity.updateString("cIsVhclNw", "0");  
            poEntity.updateString("cIsVIPxxx", "0");  
            poEntity.updateInt("nInsurYrx", 0);   
            
            poEntity.updateDouble("nUnitPrce", 0.00); 
            poEntity.updateDouble("nAdvDwPmt", 0.00); 
            poEntity.updateDouble("nOthrChrg", 0.00); 
            poEntity.updateDouble("nLaborAmt", 0.00);
            poEntity.updateDouble("nAccesAmt", 0.00);
            poEntity.updateDouble("nInsurAmt", 0.00);
            poEntity.updateDouble("nTPLAmtxx", 0.00);
            poEntity.updateDouble("nCompAmtx", 0.00);
            poEntity.updateDouble("nLTOAmtxx", 0.00);
            poEntity.updateDouble("nChmoAmtx", 0.00);
            poEntity.updateDouble("nPromoDsc", 0.00);
            poEntity.updateDouble("nFleetDsc", 0.00);
            poEntity.updateDouble("nSPFltDsc", 0.00);
            poEntity.updateDouble("nBndleDsc", 0.00);
            poEntity.updateDouble("nAddlDscx", 0.00);
            poEntity.updateDouble("nDealrInc", 0.00);
            poEntity.updateDouble("nTranTotl", 0.00);
            poEntity.updateDouble("nResrvFee", 0.00);
            poEntity.updateDouble("nDownPaym", 0.00);
            poEntity.updateDouble("nNetTTotl", 0.00);
            poEntity.updateDouble("nAmtPaidx", 0.00);
            poEntity.updateDouble("nFrgtChrg", 0.00);
            poEntity.updateDouble("nDue2Supx", 0.00);
            poEntity.updateDouble("nDue2Dlrx", 0.00);
            poEntity.updateDouble("nSPFD2Sup", 0.00);
            poEntity.updateDouble("nSPFD2Dlr", 0.00);
            poEntity.updateDouble("nPrmD2Sup", 0.00);
            poEntity.updateDouble("nPrmD2Dlr", 0.00);
            poEntity.updateDouble("nDealrRte", 0.00);
            poEntity.updateDouble("nDealrAmt", 0.00);
            poEntity.updateDouble("nSlsInRte", 0.00);
            poEntity.updateDouble("nSlsInAmt", 0.00);
            
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
        setTransNo(MiscUtil.getNextCode(getTable(), "sTransNox", true, poGRider.getConnection(), poGRider.getBranchCode()+"VSP"));
        setVSPNO(MiscUtil.getNextCode(getTable(), "sVSPNOxxx", true, poGRider.getConnection(), poGRider.getBranchCode()));
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

        String lsSQL = getSQL();//MiscUtil.makeSelect(this, ""); //exclude the columns called thru left join

        //replace the condition based on the primary key column of the record
        lsSQL = MiscUtil.addCondition(lsSQL, " a.sTransNox = " + fsValue);

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
            String lsSQL;
            String lsExclude = "sTranStat»sBuyCltNm»cClientTp»sAddressx»dInqryDte»sInqCltID»sInqCltNm»cInqCltTp»sContctID»sContctNm»sSourceCD»sSourceNo»sPlatform»sAgentIDx»sAgentNmx»sEmployID»sSENamexx"
                             + "»nRsvAmtTl»sCoCltNmx»sCSNoxxxx»sPlateNox»sFrameNox»sEngineNo»sKeyNoxxx»sVhclDesc»sBranchNm»sTPLBrIns»sTPLInsNm»sCOMBrIns»sCOMInsNm»sApplicNo»sBrBankNm»sBankName";//»
            if (pnEditMode == EditMode.ADDNEW) {
                //replace with the primary key column info
                setTransNo(MiscUtil.getNextCode(getTable(), "sTransNox", true, poGRider.getConnection(), poGRider.getBranchCode()+"VSP"));
                setVSPNO(MiscUtil.getNextCode(getTable(), "sVSPNOxxx", true, poGRider.getConnection(), poGRider.getBranchCode()));
                setEntryBy(poGRider.getUserID());
                setEntryDte(poGRider.getServerDate());
                setModifiedBy(poGRider.getUserID());
                setModifiedDte(poGRider.getServerDate());
                setLockedBy("");
                try {
                    poEntity.updateObject("dCancelld", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
                    poEntity.updateObject("dApproved", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
                    poEntity.updateObject("dLockedDt", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
                } catch (SQLException ex) {
                    Logger.getLogger(Model_Inquiry_Master.class.getName()).log(Level.SEVERE, null, ex);
                }
                
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
                Model_VehicleSalesProposal_Master loOldEntity = new Model_VehicleSalesProposal_Master(poGRider);
                
                //replace with the primary key column info
                JSONObject loJSON = loOldEntity.openRecord(this.getTransNo());

                if ("success".equals((String) loJSON.get("result"))) {
                    setModifiedBy(poGRider.getUserID());
                    setModifiedDte(poGRider.getServerDate());
                    //Clear Locked by/date
                    setLockedBy("");
                    try {
                        poEntity.updateObject("dLockedDt", SQLUtil.toDate(psDefaultDate, SQLUtil.FORMAT_SHORT_DATE));
                    } catch (SQLException ex) {
                        Logger.getLogger(Model_Inquiry_Master.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    //replace the condition based on the primary key column of the record
                    lsSQL = MiscUtil.makeSQL(this, loOldEntity, "sTransNox = " + SQLUtil.toSQL(this.getTransNo()), lsExclude);

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
        return MiscUtil.makeSQL(this, ""); //exclude columns called thru left join
    }
    
    public String getSQL(){
        return    " SELECT "                                                                      
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
                + " , a.nPromoDsc "                                                               
                + " , a.nFleetDsc "                                                               
                + " , a.nSPFltDsc "                                                               
                + " , a.nBndleDsc "                                                               
                + " , a.nAddlDscx "                                                               
                + " , a.nDealrInc "                                                               
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
                + " , a.cTranStat "                                                               
                + " , a.sCancelld "                                                               
                + " , a.dCancelld "                                                               
                + " , a.sApproved "                                                               
                + " , a.dApproved "                                                               
                + " , a.sEntryByx "                                                               
                + " , a.dEntryDte "                                                               
                + " , a.sModified "                                                               
                + " , a.dModified "                                                        
                + "  , CASE "                           
                + " 	WHEN a.cTranStat = '1' THEN 'ACTIVE' "                                         
                + " 	ELSE 'CANCELLED'  "                                                          
                + "    END AS sTranStat "   
                  /*BUYING COSTUMER*/                                                             
                + " , b.sCompnyNm AS sBuyCltNm"                                                               
                + " , b.cClientTp "                                                               
                + " , IFNULL(CONCAT( IFNULL(CONCAT(d.sHouseNox,' ') , ''),  "                     
                + "   IFNULL(CONCAT(d.sAddressx,' ') , ''),                 "                     
                + "   IFNULL(CONCAT(e.sBrgyName,' '), ''),                  "                     
                + "   IFNULL(CONCAT(f.sTownName, ', '),''),                 "                     
                + "   IFNULL(CONCAT(g.sProvName),'') )	, '') AS sAddressx  "                     
                  /*INQUIRY*/                                                                     
                + " , h.dTransact AS dInqryDte "                                                  
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
                + " , SUM(n.nAmountxx) AS nRsvAmtTl "                                                  
                  /*CO-CLIENT*/                                                                   
                + " , o.sCompnyNm AS sCoCltNmx "                                                  
                  /*VEHICLE INFORMATION*/                                                         
                + " , p.sCSNoxxxx "                                                               
                + " , q.sPlateNox "                                                               
                + " , p.sFrameNox "                                                               
                + " , p.sEngineNo "                                                               
                + " , p.sKeyNoxxx "                                                               
                + " , r.sDescript AS sVhclDesc "                                                  
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
                + " FROM vsp_master a "                                                           
                 /*BUYING CUSTOMER*/                                                              
                + " LEFT JOIN client_master b ON b.sClientID = a.sClientID "                      
                + " LEFT JOIN client_address c ON c.sClientID = a.sClientID AND c.cPrimaryx = 1 " 
                + " LEFT JOIN addresses d ON d.sAddrssID = c.sAddrssID "                          
                + " LEFT JOIN barangay e ON e.sBrgyIDxx = d.sBrgyIDxx  "                          
                + " LEFT JOIN towncity f ON f.sTownIDxx = d.sTownIDxx  "                          
                + " LEFT JOIN province g ON g.sProvIDxx = f.sProvIDxx  "                          
                 /*INQUIRY*/                                                                      
                + " LEFT JOIN customer_inquiry h ON h.sTransNox = a.sInqryIDx "                   
                + " LEFT JOIN client_master i ON i.sClientID = h.sClientID    "                   
                + " LEFT JOIN client_master j ON j.sClientID = h.sContctID    "                   
                + " LEFT JOIN online_platforms k ON k.sTransNox = h.sSourceNo "                   
                + " LEFT JOIN client_master l ON l.sClientID = h.sAgentIDx    "                   
                + " LEFT JOIN ggc_isysdbf.client_master m ON m.sClientID = h.sEmployID    "       
                + " LEFT JOIN customer_inquiry_reservation n ON n.sSourceNo = a.sInqryIDx "       
                 /*CO CLIENT*/                                                                    
                + " LEFT JOIN client_master o ON o.sClientID = a.sCoCltIDx "                      
                 /*VEHICLE INFORMATION*/                                                          
                + " LEFT JOIN vehicle_serial p ON p.sSerialID = a.sSerialID "                     
                + " LEFT JOIN vehicle_serial_registration q ON q.sSerialID = a.sSerialID "        
                + " LEFT JOIN vehicle_master r ON r.sVhclIDxx = p.sVhclIDxx "                     
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
                + " LEFT JOIN banks z ON z.sBankIDxx = y.sBankIDxx            " ;
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
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setDelvryDt(Date fdValue) {
        return setValue("dDelvryDt", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getDelvryDt() {
        Date date = null;
        if(!getValue("dDelvryDt").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dDelvryDt").toString());
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
        return setValue("sInqryIDx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getInqryID() {
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
    public JSONObject setUnitPrce(Double fdbValue) {
        return setValue("nUnitPrce", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getUnitPrce() {
        return Double.parseDouble(String.valueOf(getValue("nUnitPrce")));
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
    public JSONObject setAdvDwPmt(Double fdbValue) {
        return setValue("nAdvDwPmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getAdvDwPmt() {
        return Double.parseDouble(String.valueOf(getValue("nAdvDwPmt")));
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
    public JSONObject setOthrChrg(Double fdbValue) {
        return setValue("nOthrChrg", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getOthrChrg() {
        return Double.parseDouble(String.valueOf(getValue("nOthrChrg")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setLaborAmt(Double fdbValue) {
        return setValue("nLaborAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getLaborAmt() {
        return Double.parseDouble(String.valueOf(getValue("nLaborAmt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setAccesAmt(Double fdbValue) {
        return setValue("nAccesAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getAccesAmt() {
        return Double.parseDouble(String.valueOf(getValue("nAccesAmt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setInsurAmt(Double fdbValue) {
        return setValue("nInsurAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getInsurAmt() {
        return Double.parseDouble(String.valueOf(getValue("nInsurAmt")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setTPLAmt(Double fdbValue) {
        return setValue("nTPLAmtxx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getTPLAmt() {
        return Double.parseDouble(String.valueOf(getValue("nTPLAmtxx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setCompAmt(Double fdbValue) {
        return setValue("nCompAmtx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getCompAmt() {
        return Double.parseDouble(String.valueOf(getValue("nCompAmtx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setLTOAmt(Double fdbValue) {
        return setValue("nLTOAmtxx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getLTOAmt() {
        return Double.parseDouble(String.valueOf(getValue("nLTOAmtxx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setChmoAmt(Double fdbValue) {
        return setValue("nChmoAmtx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getChmoAmt() {
        return Double.parseDouble(String.valueOf(getValue("nChmoAmtx")));
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
    public JSONObject setPromoDsc(Double fdbValue) {
        return setValue("nPromoDsc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getPromoDsc() {
        return Double.parseDouble(String.valueOf(getValue("nPromoDsc")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setFleetDsc(Double fdbValue) {
        return setValue("nFleetDsc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getFleetDsc() {
        return Double.parseDouble(String.valueOf(getValue("nFleetDsc")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setSPFltDsc(Double fdbValue) {
        return setValue("nSPFltDsc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getSPFltDsc() {
        return Double.parseDouble(String.valueOf(getValue("nSPFltDsc")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setBndleDsc(Double fdbValue) {
        return setValue("nBndleDsc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getBndleDsc() {
        return Double.parseDouble(String.valueOf(getValue("nBndleDsc")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setAddlDsc(Double fdbValue) {
        return setValue("nAddlDscx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getAddlDsc() {
        return Double.parseDouble(String.valueOf(getValue("nAddlDscx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setDealrInc(Double fdbValue) {
        return setValue("nDealrInc", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getDealrInc() {
        return Double.parseDouble(String.valueOf(getValue("nDealrInc")));
    }
    
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
    public JSONObject setTranTotl(Double fdbValue) {
        return setValue("nTranTotl", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getTranTotl() {
        return Double.parseDouble(String.valueOf(getValue("nTranTotl")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setResrvFee(Double fdbValue) {
        return setValue("nResrvFee", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getResrvFee() {
        return Double.parseDouble(String.valueOf(getValue("nResrvFee")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setDownPaym(Double fdbValue) {
        return setValue("nDownPaym", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getDownPaym() {
        return Double.parseDouble(String.valueOf(getValue("nDownPaym")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setNetTTotl(Double fdbValue) {
        return setValue("nNetTTotl", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getNetTTotl() {
        return Double.parseDouble(String.valueOf(getValue("nNetTTotl")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setAmtPaid(Double fdbValue) {
        return setValue("nAmtPaidx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getAmtPaid() {
        return Double.parseDouble(String.valueOf(getValue("nAmtPaidx")));
    }
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setFrgtChrg(Double fdbValue) {
        return setValue("nFrgtChrg", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getFrgtChrg() {
        return Double.parseDouble(String.valueOf(getValue("nFrgtChrg")));
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
    public JSONObject setDue2Dlrx(Double fdbValue) {
        return setValue("nDue2Dlrx", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getDue2Dlrx() {
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
    public JSONObject setDealrAmt(Double fdbValue) {
        return setValue("nDealrAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getDealrAmt() {
        return Double.parseDouble(String.valueOf(getValue("nDealrAmt")));
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
    public JSONObject setSlsInAmt(Double fdbValue) {
        return setValue("nSlsInAmt", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getSlsInAmt() {
        return Double.parseDouble(String.valueOf(getValue("nSlsInAmt")));
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
    public JSONObject setIsVIPxxx(String fsValue) {
        return setValue("cIsVIPxxx", fsValue);
    }

    /**
     * @return The Value of this record.
     */
    public String getIsVIPxxx() {
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
    public JSONObject setDcStatDt(Date fdValue) {
        return setValue("dDcStatDt", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getDcStatDt() {
        Date date = null;
        if(!getValue("dTransact").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dDcStatDt").toString());
        }
        
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
        return setValue("dLockedDt", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getLockedDte() {
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
     * @param fdValue
     * @return result as success/failed
     */
    public JSONObject setApprovedDte(Date fdValue) {
        return setValue("dApproved", fdValue);
    }

    /**
     * @return The Value of this record.
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
        return setValue("dInqryDte", fdValue);
    }

    /**
     * @return The Value of this record.
     */
    public Date getInqryDte() {
        Date date = null;
        if(!getValue("dInqryDte").toString().isEmpty()){
            date = CommonUtils.toDate(getValue("dInqryDte").toString());
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
    
    /**
     * Description: Sets the Value of this record.
     *
     * @param fdbValue
     * @return result as success/failed
     */
    public JSONObject setRsvAmtTl(Double fdbValue) {
        return setValue("nRsvAmtTl", fdbValue);
    }

    /**
     * @return The Value of this record.
     */
    public Double getRsvAmtTl() {
        return Double.parseDouble(String.valueOf(getValue("nRsvAmtTl")));
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
    
}
