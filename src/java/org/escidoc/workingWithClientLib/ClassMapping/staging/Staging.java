package org.escidoc.workingWithClientLib.ClassMapping.staging;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.StagingHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.notfound.FileNotFoundException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.interfaces.StagingHandlerClientInterface;

/**
 * Example for Staging.
 * 
 * @author JHE
 * 
 */
public class Staging {

    /**
     * @param args
     */
    public static void main(String[] args) {

        Authentication auth = null;;
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
        
        StagingHandlerClientInterface sthc = new StagingHandlerClient(auth.getServiceAddress());
        try {
            sthc.setHandle(auth.getHandle());
        }
        catch (InternalClientException e) {
            e.printStackTrace();
        }

        InputStream inputStream = null;
        try {
            inputStream = load("/soap/om/item/0.6/item.xml");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        URL url = null;
        try {
            url = sthc.upload(inputStream);
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

        String stagingFile = "";
        
        String s;
        InputStream ins = null;
        try {
            ins = url.openStream();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        DataInputStream dis = new DataInputStream(new BufferedInputStream(ins));
        try {
            while ((s = dis.readLine()) != null) {
                stagingFile += s;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            inputStream = load("/soap/om/item/0.6/item.xml");
        }
        catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        String localFile = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line;
            try {
                while ((line = in.readLine()) != null) {
                    localFile += line;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        finally {
            try {
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Get FileInputStream from template.
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

        if (path == null)
            return null;

        return Template.class.getResourceAsStream(path);
    }

}
