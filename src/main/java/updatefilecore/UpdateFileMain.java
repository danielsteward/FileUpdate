/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updatefilecore;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author danst
 */
public class UpdateFileMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UpdateFilePathFeed f = new UpdateFilePathFeed();//inits this class as it has xml properties files to read.
        } catch (IOException ex) {
            Logger.getLogger(UpdateFileMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        UpdateFilePoll polly = new UpdateFilePoll();
        polly.moveNewUpdateFiles(); 
    }
    
}
