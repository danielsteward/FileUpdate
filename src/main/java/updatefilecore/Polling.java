/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updatefilecore;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
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
    private LocalDateTime timeOfLastUFMove;//??????????????????
    SortedSet<Path> ufPathSet = new TreeSet<>();//will contain a single update file Path or multiple.
    private UpdateFileProcessor ufProcessor;//can be a Singleton
    private Logger logger;
    private FileHandler fh;
    boolean trace = false;
// to dos - class description, logging.....
    Polling(){
        dirIn = PathSupplier.PATHNAMESINSTANCE.getDirIn();
        dirProcess = PathSupplier.PATHNAMESINSTANCE.getDirProcess();
        dirArchive = PathSupplier.PATHNAMESINSTANCE.getDirArchive();
        dirBin = PathSupplier.PATHNAMESINSTANCE.getDirBin();
        keys = new HashMap<WatchKey, Path>();// the Collection for extraction of the events, Paths etc when they happen.
        try {
            watcher = FileSystems.getDefault().newWatchService();
            registerDir(dirIn);// Registers the directory where the update file will be placed.
        } catch (IOException ex) {
            Logger.getLogger(Polling.class.getName()).log(Level.SEVERE, null, ex);// to do - improve logging.
        }
        
    }


    public void moveNewUpdateFiles() {
        for (;;) {
            boolean processorNeeded = false;// TO DO. Positive if an update file[s] need processing.
            //ufPathSet.clear();//IS THIS NEEDED HERE?????? The folder could be cleared elsewhere. Definitely at the end of the loop.
            WatchKey key;//new WatchKey as reset at end of loop.
            try {// Now watching for an Event e.g. an update file is put in the dir.
                key = watcher.take();//Retrieves the WatchKey that was generated in the registration.
            } catch (InterruptedException ex) {
                return;//TO DO. Need a logger.
            }
            for (WatchEvent<?> event : key.pollEvents()) {// Loops through the events. The key can hold multiple events.
                WatchEvent.Kind eventKind = event.kind();// Get the kind of the WatchEvent
                System.out.println("eventKind is " + eventKind.toString());
                Path fileName = (Path) event.context();// Get the name of the file.
                Path fileNamePath = dirIn.resolve(fileName);
                if(eventKind == ENTRY_CREATE){// If it is ENTRY_CREATE then create the Path to dirProcess[ing] & move the file there.
                    Path targetDir = dirProcess.resolve(fileName);
                    try {
                        Files.move(fileNamePath, targetDir, REPLACE_EXISTING);
                    } catch (IOException ex) {
                        Logger.getLogger(Polling.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{// TO SORT. Otherwise get Path to bin and move file[s] to dirBin.???????????????????
                    try {
                    Files.move(fileNamePath, dirBin, REPLACE_EXISTING);
                    } catch (IOException ex) {
                        Logger.getLogger(Polling.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            // TO DO. Then call the processor if files have been moved to the processing file - dirProcess.
            key.reset();// resets key.
        }

    }


    private void registerDir(Path dir){
        WatchKey key2 = null;// TO SORT. For possible collection of keys and dirs.
        try {
            key2 = dir.register(watcher, ENTRY_CREATE); //watcher is a WatchService. register() is a method of Path. register returns a WatchKey.
        } catch (IOException ex) {
            Logger.getLogger(Polling.class.getName()).log(Level.SEVERE, null, ex);
        }
        //keys.put(key2, dir);//if needed later......
        
    }

}
    

