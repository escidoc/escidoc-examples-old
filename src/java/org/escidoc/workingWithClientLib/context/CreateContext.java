package org.escidoc.workingWithClientLib.context;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestContextHandlerClient;

public class CreateContext {

    public static void main(String[] args) {

        try {

            String xmlFile = "templates/TUE/Context_create.xml";
            if (args.length > 0) {
                xmlFile = args[0];
            }

            String createdResource =
                createContext(Util.getXmlFileAsString(xmlFile));

            String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
            System.out.println("Context with objid='" + objidLmd[0] + "' at '"
                + objidLmd[1] + "' created.");

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
