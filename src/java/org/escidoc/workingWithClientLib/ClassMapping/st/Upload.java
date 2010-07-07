package org.escidoc.workingWithClientLib.ClassMapping.st;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.StagingHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * Example how to upload a file to the staging service.
 * 
 * @author SWA
 * 
 */
public class Upload {

    /**
     * A temporary file is created with a random content - our payload. This
     * file is uploaded to staging service afterwards.
     * 
     * @param args
     */
    public static void main(String[] args) {

        try {
            // Create temp file with random content
            File temp =
                File.createTempFile("escidoc-binary-content-example", ".tmp");
            temp.deleteOnExit();

            // Write to temp file
            BufferedWriter out = new BufferedWriter(new FileWriter(temp));

            out.write("A random String " + System.nanoTime());
            out.close();

            // use a user accout with write permissions on staging service (this
            // is usually the role 'depositor').
            Authentication auth =
                new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                    Constants.USER_NAME, Constants.USER_PASSWORD);

            StagingHandlerClient sthc = new StagingHandlerClient();
            sthc.setServiceAddress(auth.getServiceAddress());
            sthc.setHandle(auth.getHandle());

            URL url = sthc.upload(temp);

            System.out.println("The content can be downloaded once from " + url
                + ".");
        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
