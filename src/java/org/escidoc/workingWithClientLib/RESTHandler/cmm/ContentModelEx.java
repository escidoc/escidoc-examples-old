package org.escidoc.workingWithClientLib.RESTHandler.cmm;

import java.io.File;
import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.rest.RestContentModelHandlerClient;

/**
 * Example how to handle a ContentModel by using the XML REST representation and
 * the eSciDoc Java client library.
 * 
 * The eSciDoc Java client library is used for communication with framework.
 * Unused is mapping between Java classes and XML representations to explain the
 * XML data structure.
 * 
 * eSciDoc XML REST representation is used. Please keep that in mind, if you
 * adapt these examples for SOAP.
 * 
 * @author SWA
 * 
 */
public class ContentModelEx {

    /**
     * 
     */
    ContentModelEx() {
    }

    public void create() throws InternalClientException, EscidocException,
        TransportException, IOException {

        // get handler for organizational units
        RestContentModelHandlerClient rcmhc =
            new RestContentModelHandlerClient();

        // authenticate
        rcmhc.login(Constants.DEFAULT_SERVICE_URL, Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);

        // load XML template of organizational unit
        File templOu = new File("./templates/TUE/content-model.xml");
        String ouXml = Util.getXmlFileAsString(templOu);

        // create
        String crtdOuXML = rcmhc.create(ouXml);
        String[] objidLmd = Util.obtainObjidAndLmd(crtdOuXML);

        System.out.println("Content Model with objid='" + objidLmd[0]
            + "' at '" + objidLmd[1] + "' created");
    }
}
