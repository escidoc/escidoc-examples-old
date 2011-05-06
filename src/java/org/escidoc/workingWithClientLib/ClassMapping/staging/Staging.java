package org.escidoc.workingWithClientLib.ClassMapping.staging;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.StagingHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.notfound.FileNotFoundException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.interfaces.StagingHandlerClientInterface;

/**
 * Example for file uploading via staging area.
 * 
 * @author JHE
 * 
 */
public class Staging {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        // file for upload
        String file = "templates/generic/img/escidoc-logo.jpg";
        
        if (args.length == 1) {
            file = args[0];
        }
        
        Authentication auth = null;
        
        try {
            auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_SYSADMIN,
                Constants.USER_PASSWORD_SYSADMIN);
        }
        catch (AuthenticationException e) {
            e.printStackTrace();
        }
        catch (TransportException e) {
            e.printStackTrace();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
     // get staging handler client
        StagingHandlerClientInterface sthc = new StagingHandlerClient(auth.getServiceAddress());
        try {
            sthc.setHandle(auth.getHandle());
        }
        catch (InternalClientException e) {
            e.printStackTrace();
        }
        
      // read data from filesystem
        InputStream inputStream = null;
        try {
            inputStream = load(file);
            int i = 0;
            try {
                while (inputStream.read() > -1) {
                    i++;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            
            inputStream = load(file);
            
            System.out.println( i + " bytes read from file: " + file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        URL url = null;
        
        try {
            // upload file and get retrieval url for the staging area
            url = sthc.upload(inputStream);
            System.out.println("file uploaded,\nurl for staging retrieval is:\n" + url.toString());
            
        }
        catch (EscidocException e) {
            e.printStackTrace();
        }
        catch (InternalClientException e) {
            e.printStackTrace();
        }
        catch (TransportException e) {
            e.printStackTrace();
        }
        
        
      // fetch data form staging area
        InputStream ins = null;
        try {
            ins = url.openStream();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            int i = 0;
            while (ins.read() > -1) {
                i++;
            }
            
           System.out.println( i + " bytes fetched from staging area");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Get InputStream from template.
     * 
     * @param path
     *            Path to template file
     * @return FileInputStream
     * 
     * @throws FileNotFoundException
     *             If file could not be found
     */
    public static InputStream load(final String path)
        throws FileNotFoundException {
        
        try {
            return new FileInputStream(path);
        }
        catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
