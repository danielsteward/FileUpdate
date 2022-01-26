/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updatefilecore;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author danst
 */
public enum Polling {
    
    POLLINGOB;

    private WatchService watcher;
    private Map<WatchKey, Path> keys;
    private Path dirIn;
    private Path dirProcess;
    private Path dirArchive;
    private Path dirBin;
    private static Path pathAndNameOfUF;//???????????????????
    private LocalDateTime timeOfLastUFMove;//??????????????????
    SortedSet<Path> ufPathSet = new TreeSet<>();//will contain a single update file Path or multiple if multiple UFs are submitted simultainiously.
    private UpdateFileProcessor ufProcessor;//can be a Singleton
    private Logger logger;
    private FileHandler fh;
    private WatchKey key;//This was in the polling method but not sure why it should not be declared here.
    boolean trace = false;
    //private PEDUtilities pu;
    //private String updateFileNamePath;

    Polling() {
        this.keys = new HashMap<WatchKey, Path>();
        try {
            //set Paths from PathSupplier.
            watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException ex) {
            Logger.getLogger(Polling.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void moveNewUpdateFiles() {
        for (;;) {

            //ufPathSet.clear();//IS THIS NEEDED HERE?????? The folder could be cleared elsewhere. In constructor?
            // wait for key to be signalled.
            try {// Now watching for an Event e.g. an update file is put in the dir.
                key = watcher.take();// watcher represents the WatchService. Originally registered in constructor.
                //Retrieves and removes next watch key, waiting if none are yet present.
            } catch (InterruptedException x) {
                return;//need a logger.
            }

            Path dir = keys.get(key); //keys is a Map of WatchKeys and Path_s. This is reset at the end of the method. 
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");//need something better here.
                continue;
            }
            for (WatchEvent<?> event : key.pollEvents()) {// starts the processing.
                //this.clearUFDirectory(pathOfProcessingDir);//clears the processing directory of any old ufs or others.
                WatchEvent.Kind kind = event.kind();// TBD - how to handle v.unlikely event of an overflow. Or check for other events?
                if (kind == OVERFLOW) {//This event can be used by the consumer as a trigger to re-examine the state of the object.
                    //Events may be rejected if reported faster than can be processed.
                    continue;//???????
                }
                // Need to create a Path object from the WatchEvent context which should be a Path.
                // If it is not a Path then we should break the for loop.
                WatchEvent<?> ev = event;// Context for directory entry event is the file name of entry.
                //NEED A HELPER METHOD HERE AS METHOD TOO LONG.
                Path name = (Path) event.context();//the context returns the name of a file. Or the Path of that file. SHOULD NOT NEED TO CAST IF CHECKED THE EVENT IS AN UPDATE FILE AND NOT A DIFFERENT EVENT.
                Path child = dir.resolve(name);//gets the name and path of the UF. Child is a Path. MUST BE A METHOD TO GET NAME OF FILE.
                //Path ufProcessingPath;//this will be the path of the processing directory and the name of the UF combined.
                //updateFileNamePath = pathOfProcessingDir + "\\" + name.toString();//Generates a string path of the UF to be moved.
                //ufProcessingPath = FileSystems.getDefault().getPath(updateFileNamePath);//and creates a (static) Path from that String.
                //UpdateFilePathFeed.setUfStringPathProcessing(updateFileNamePath);//sets String version of uf path when in processing directory.
                //UpdateFilePathFeed.setUfPathProcessing(ufProcessingPath);//sets Path of uf when in processing directory.
                //pu.setUpdateFileDir(pathOfProcessingDir.toString());//getUpdateFileDir() was no longer used. so setUpdateFileDir() has been removed.
                //ufPathSet.add(ufProcessingPath);//adds UF Path of the processing directory to collection.
                try {
                    //System.out.println("In main UFPoll " + child + " is the name and path of the uf - " + ufProcessingPath + " - is the Path of the UF in the processing directory.");
                    //Files.move(child, ufProcessingPath, REPLACE_EXISTING);// moves UF to processing dir.

                } catch (Exception ex) {//should this be IOException???????
                    StackTraceElement[] s = ex.getStackTrace();
                    //logger.log(Level.SEVERE, "2nd catch in moveNewUpdateFiles stack trace is  {0}", s);

                }
                //logger.log(Level.INFO, "Moving UF {0} to {1}", new Object[]{child.toString(), ufProcessingPath.toString()});

                //File countFiles = new File(pathNameIn.toString());//count number of files in the dir in the event of a submission of multiple files simultainiously.
                //int noOfUFs = countFiles.list().length;//System.out.println("Number of files in incomming foder is "+noOfUFs);
                //if (noOfUFs == 0) {//if there are no more files then start the processing otherwise continue with loop. Might not need this check as key.poll() would be empty of events.
                //this.ufFeed();//start processing update files.
            }
        }

        //logger.info("UF/s submitted. Resetting key in poll.");
        /*            boolean valid = key.reset();// resets key and remove from set if directory no longer accessible.
            if (!valid) {
                keys.remove(key);
                if (keys.isEmpty()) {
                    break;
                }
            }
            ufPathSet.clear(); //System.out.println("Clearing ufPathSet at the end (outside)  of the small for looop in moveNewUpdateFiles");

         */
    }

    public void pollTest() throws IOException, InterruptedException {
        dirIn = PathSupplier.PATHNAMESINSTANCE.getDirIn();
        System.out.println(dirIn);
        key = this.registerDir(dirIn);
        
        key = watcher.take();
        
        for(WatchEvent<?> event : key.pollEvents()){
            System.out.println(event.context().toString());
        }

    }

    private WatchKey registerDir(Path dir) throws IOException {
        WatchKey key2 = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);//watcher is a WatchService. register() is a method of Path.
                                                                                        //register returns a WatchKey.
//        if (!trace) {
//            Path prev = keys.get(key);
//            if (prev == null) {
//                System.out.format("register: %s\n", dir);
//            } else {
//                if (!dir.equals(prev)) {
//                    System.out.format("update: %s -> %s\n", prev, dir);
//                }
//            }
//        }
        keys.put(key2, dir);
        return key2;
        
    }

}
    

