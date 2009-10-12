package org.escidoc.workingWithClientLib;

import java.io.BufferedReader;
import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.rest.RestItemHandlerClient;

public class CreateItem {

    public static void main(String[] args) {

        try {
            RestItemHandlerClient client = new RestItemHandlerClient();
            client.login(Constants.DEFAULT_SERVICE_URL,
                Constants.SYSTEM_ADMIN_USER, Constants.SYSTEM_ADMIN_PASSWORD);
            client.create(Util.getXmlFileAsString(args[0]));
        }
        catch (InternalClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (EscidocException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (TransportException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out
                .println("First parameter must be an eSciDoc Item XML File.");
        }

    }

}
