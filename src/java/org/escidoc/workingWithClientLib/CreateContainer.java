package org.escidoc.workingWithClientLib;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestContainerHandlerClient;

public class CreateContainer {

    public static void main(String[] args) {

        try {

            String xmlFile = null;
            if (args.length < 1) {
                xmlFile = "templates/TUE/Container_create_minimal.xml";
            }

            String createdResource =
                createContainer(Util.getXmlFileAsString(xmlFile));
            
            String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
            System.out.println("Container with objid='" + objidLmd[0]
                + "' at '" + objidLmd[1] + "' created");

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out
                .println("First parameter must be an eSciDoc Item XML File.");
        }
    }

    private static String createContainer(String containerXml)
        throws EscidocClientException {

        RestContainerHandlerClient client = new RestContainerHandlerClient();
        client.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);
        String createdContainer = client.create(containerXml);

        return createdContainer;
    }

}
