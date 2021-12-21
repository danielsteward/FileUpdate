/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updatefileutils;
//import corepedinventory.DBConnection;
//import corepedinventory.PED;
//import updatefileprocesses.UpdateFileObject;
 import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import updatefilecore.PED;
import updatefilecore.UpdateFileObject;
import updatefilecore.UpdateFilePoll;
/**
 *
 * @author danst
 */
public class UFUtil {
    private DBConnection myDBConn;
    private ResultSet rs;
    private PED ped;
    private Connection conn;
    private Statement sment;
    private BufferedWriter outputStream;
    private Files file;
    private PrintWriter pw;
    //private User user;
    //private ReportsCSV r;
    private UpdateFilePoll updateFileChecker;
    private static String updateFileNameAndPath;//name and path when in processing folder.
    private static String updateFileDir;
    private boolean logok;
    private static Path ufPathProcessingDir;
    private String ufStringPathProcessingDir;//NOT USED
    private static boolean ufOrGui;// depends on whetehr the update is made via an update file or via the GUI. false is uf and true if GUI.
    private static UpdateFileObject ufo;
    private int noOfEntriesInUF;
    private static String dateTimePEDFromUF;
    private Logger logger;
    private FileHandler fh;
    private boolean ufok;//true indicates the uf integrity is ok.
    private boolean PEDFound;//indicates whether or not a PED is found when searched for on the edit ped status tab.
    private String trlSp;
    
}
