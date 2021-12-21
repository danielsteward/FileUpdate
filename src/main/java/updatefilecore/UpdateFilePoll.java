/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updatefilecore;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import updatefileutils.DateTimeUtil;
import updatefileutils.UFUtil;

/**
 *
 * @author danst
 */
public class UpdateFilePoll {
    
    private WatchService watcher;
    private Map<WatchKey,Path> keys;
    private boolean trace = false;
    private Properties props;
    private static Path pathNameIn;
    private static Path pathOfProcessingDir;
    private static Path pathOfArchiveDir;
    private static Path pathAndNameOfUF;
    private UFUtil ufUtil;
    private String updateFileNamePath;
    private LocalDateTime timeOfLastUFMove;
    SortedSet<Path> ufPathSet = new TreeSet<>();
    private UpdateFileProcessor ufProcessor;
    private final Logger logger;
    private FileHandler fh;
    
    
    public UpdateFilePoll(){
        LocalDateTime timeNow = LocalDateTime.now();
        String timeNowString = DateTimeUtil.stripNonDigits(timeNow.toString());
        logger = Logger.getLogger("UFLog");
        logger.addHandler(fh);  
        try {
            fh = new FileHandler(timeNowString + "_puLogFile.log");
        } catch (IOException | SecurityException ex) {
            logger.log(Level.SEVERE, "Exception in constructor of UpdateFilePoll was {0}", ex.getMessage());
        }
        
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }
    
    
    
    
    
/**
 * Intercepts and moves incoming update files to be processed. Creates a List of Paths of the UFs so if there are multiple
 * incoming UFs they can be processed.
 */
    public void moveNewUpdateFiles() {
        for (;;) {
            this.clearUFDirectory(pathNameIn);//clears the incoming directory of any old ufs or others.They didn't want this but I think it is a good cover-all when the program has to be restarted.
            ufPathSet.clear();
            // wait for key to be signalled
            WatchKey key;// key is a token representing the registration of an object with the WatchService.
            try {
                key = watcher.take();// watcher represents the WatchService
            } catch (InterruptedException x) {
                return;
            }
                //pu = new PEDUtilities();
            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }
            for (WatchEvent<?> event: key.pollEvents()){// This sets the polling to start. Nothing will happen until a change in state in the directory e.g. an update file is put in the dir.
                //this.clearUFDirectory(pathOfProcessingDir);//clears the processing directory of any old ufs or others.getDirPathProcessing()
                WatchEvent.Kind kind = event.kind();// TBD - how to handle v.unlikely event of an overflow.
                if (kind == OVERFLOW) {
                    continue;//???????
                }
                // Need to create a Path object from the WatchEvent context which should be a Path.
                // If it is not a Path then we should break the for loop.
                //WatchEvent<?> ev = event;// Context for directory entry event is the file name of entry.
                
                Path name = (Path) event.context();//the context returns the name of a file. Or the Path of that file.
                UpdateFilePathFeed.setUfName(name);//not used at the moment.
                Path child = dir.resolve(name);//gets the name and path of the UF. Child is a Path
                Path ufProcessingPath;//this will be the path of the processing directory and the name of the UF combined.
                updateFileNamePath = pathOfProcessingDir + "\\" + name.toString();//Generates a string path of the UF to be moved.
                ufProcessingPath = FileSystems.getDefault().getPath(updateFileNamePath);//and creates a (static) Path from that String.
                UpdateFilePathFeed.setUfStringPathProcessing(updateFileNamePath);//sets String version of uf path when in processing directory.
                UpdateFilePathFeed.setUfPathProcessing(ufProcessingPath);//sets Path of uf when in processing directory.
                //pu.setUpdateFileDir(pathOfProcessingDir.toString());//getUpdateFileDir() was no longer used. so setUpdateFileDir() has been removed.
                ufPathSet.add(ufProcessingPath);//adds UF Path of the processing directory to collection.
                try {
                    System.out.println("In main UFPoll " + child + " is the name and path of the uf - " + ufProcessingPath + " - is the Path of the UF in the processing directory.");
                    Files.move(child, ufProcessingPath, REPLACE_EXISTING);// moves UF to processing dir.
                    System.out.println("Moving " + child +  "to "+ufProcessingPath);
                } catch (Exception ex) {//should this be IOException???????
                    StackTraceElement[] s = ex.getStackTrace();
                    logger.log(Level.SEVERE, "2nd catch in moveNewUpdateFiles stack trace is  {0}", s);
                }
                //logger.log(Level.INFO, "Moving UF {0} to {1}", new Object[]{child.toString(), ufProcessingPath.toString()});
                File countFiles = new File(pathNameIn.toString());//count number of files in the dir in the event of a submission of multiple files simultainiously.
                //System.out.println("Pathnamein is " +pathNameIn);
                int noOfUFs = countFiles.list().length;//System.out.println("Number of files in incomming foder is "+noOfUFs);
                if (noOfUFs == 0) {//if there are no more files then start the processing otherwise continue with loop.
                    this.ufFeed();//start processing update files.
                }
            }
            logger.info("UF/s submitted. Resetting key in poll.");
            boolean valid = key.reset();// resets key and remove from set if directory no longer accessible.
            if (!valid) {
                keys.remove(key);
                if (keys.isEmpty()) {
                    break;
                }
            }
            ufPathSet.clear(); //System.out.println("Clearing ufPathSet at the end (outside)  of the small for looop in moveNewUpdateFiles");
        }
        
    }

    private void ufFeed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void clearUFDirectory(Path pathNameIn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
