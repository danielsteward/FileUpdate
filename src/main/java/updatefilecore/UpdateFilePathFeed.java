/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updatefilecore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import updatefileutils.DateTimeUtil;

/**
 *
 * @author danst
 */
public class UpdateFilePathFeed {
    // 
    
    private static Path dirPathIncoming;//Path for the directory into which the update files are put. Set in constructor.
    private static Path dirPathProcessing;//Path to which UFs are moved to be processed. Set in constructor.
    private static Path dirPathArchive;//Archive directory. Set in constructor.
    private static Path ufName;//get from UpdateFilePoll. Or this is already done in Utilities. Copy from there? NOT USED AT THE MOMENT.
    private static Path ufPathIncoming;//
    private static Path ufPathProcessing;//constructed in UpdateFilePoll.moveNewUpdateFiles()
    private static Path ufPathArchiveFull;//the Path including the name of the file. Created in UpdateFilePoll.

    static void setUfStringPathProcessing(String updateFileNamePath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static void setUfPathProcessing(Path ufProcessingPath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    private LocalDateTime timeOfLastUFMove;
    private static String ufStringPathProcessing;//used for the file reading in UpdateFileProcessor
    private Properties props;
    private Logger logger;
    private FileHandler fh;
    
    public UpdateFilePathFeed() throws IOException {//does all this need initialising? If it is used immediately - yes.
        this.getProperties("update_filepaths.xml");
        dirPathIncoming = FileSystems.getDefault().getPath(props.getProperty("pathname_in"));
        dirPathProcessing = FileSystems.getDefault().getPath(props.getProperty("pathname_processing"));
        //dirPathArchive = FileSystems.getDefault().getPath(props.getProperty("pathname_archive"));
        dirPathArchive = Paths.get(props.getProperty("pathname_archive"));
        timeOfLastUFMove = LocalDateTime.now();//??????????
        String timeNow = DateTimeUtil.stripNonDigits(timeOfLastUFMove.toString());
        logger = Logger.getLogger("UFLog");//??? does the logger need to be initiated here??????
        fh = new FileHandler(timeNow + "_ufLogFile.log");
        logger.addHandler(fh);
    }

    private void getProperties(String fileName) {
        
        this.props = new Properties();
        try{
        FileInputStream fis = new FileInputStream(fileName);
        props.loadFromXML(fis);
        }
        catch(FileNotFoundException fEx){
            StackTraceElement[] s = fEx.getStackTrace();
            logger.log(Level.SEVERE, "catch in UpdatFilePoll constructor. stack trace is  {0}", s);
        }  
        catch(IOException ioEx){
            StackTraceElement[] s = ioEx.getStackTrace();
            logger.log(Level.SEVERE, "catch in UpdatFilePoll constructor. stack trace is  {0}", s);
        }
      
    }
    
    static void setUfName(Path name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
