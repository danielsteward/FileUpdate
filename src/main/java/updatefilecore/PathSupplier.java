
package updatefilecore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *
 * @author danst Gets directory paths from xml properties doc and supplies them
 * as requested. Singleton
 */
public enum PathSupplier {

    //TO DO need Paths for the reports.
    //
    PATHNAMESINSTANCE;

    
    private Path dirIn;
    private Path dirProcess;
    private Path dirArchive;
    private Path dirBin;
    private Properties props;

    PathSupplier(){
        // sets all the Path names for the update file.
        try{
        this.setProperties("update_filepaths.xml");
        }
        catch(IOException x){//TO DO
            
        }
        
    }

    private void setProperties(String fileName) throws FileNotFoundException, IOException {
        this.props = new Properties();
        FileInputStream fis = new FileInputStream(fileName);
        props.loadFromXML(fis);
        dirIn = Paths.get(props.getProperty("pathname_in"));
        dirProcess = Paths.get(props.getProperty("pathname_processing"));
        dirArchive = Paths.get(props.getProperty("pathname_archive"));
        dirBin = Paths.get(props.getProperty("pathname_bin"));
    }
    
    public Properties getProps() {
        return props;
    }

    public Path getDirIn() {
        return dirIn;
    }

    public Path getDirProcess() {
        return dirProcess;
    }

    public Path getDirArchive() {
        return dirArchive;
    }

    public Path getDirBin() {
        return dirBin;
    }

}
