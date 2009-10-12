package org.escidoc.workingWithClientLib;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestItemHandlerClient;

public class UploadBinContent {

    public static void main(String[] args) {

        try {

            String xmlFile = null;
            if (args.length < 1) {
                xmlFile = "templates/TUE/content/Beispiel schwäbischer Dialektdaten.xml";
            }

            String createdResourceURL =
                uploadContent(Util.getXmlFileAsString(xmlFile));

            System.out.println(createdResourceURL);

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out
                .println("First parameter must be an eSciDoc Item XML File.");
        }
    }

    private static String uploadContent(String xml)
        throws EscidocClientException {

        RestItemHandlerClient client = new RestItemHandlerClient();
        ItemHandlerClient ihc = new ItemHandlerClient();
        client.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);

        return null;
    }

}
