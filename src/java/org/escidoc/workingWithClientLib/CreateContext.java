package org.escidoc.workingWithClientLib;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestContextHandlerClient;

public class CreateContext {

    public static void main(String[] args) {

        try {

            String xmlFile = null;
            if (args.length < 1) {
                xmlFile = "templates/TUE/Context_create.xml";
            }

            String createdResource =
                createContext(Util.getXmlFileAsString(xmlFile));

            System.out.println(createdResource);

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out
                .println("First parameter must be an eSciDoc Item XML File.");
        }
    }

    private static String createContext(String contextXml)
        throws EscidocClientException {

        RestContextHandlerClient client = new RestContextHandlerClient();
        client.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);
        String createdContext = client.create(contextXml);

        return createdContext;
    }

}
