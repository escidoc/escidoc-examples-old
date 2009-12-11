package org.escidoc.workingWithClientLib.RESTHandler.context;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestContextHandlerClient;

/**
 * Example how to set status of a Context to 'open' by using the XML REST
 * representation and the eSciDoc Java client library.
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
public class OpenContext {

    /**
     * Set status of Organizational Unit to open
     * 
     * @param args
     *            If args[0] is given, it is used as objid for Organizational
     *            Unit. Otherwise is escidoc:ex1 used.
     */
    public static void main(String[] args) {

        String id = "escidoc:1";
        if (args.length > 0) {
            id = args[0];
        }

        try {
            openContext(id);
        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }

    }

    public static String openContext(String id) throws EscidocClientException {

        // prepare client object
        RestContextHandlerClient rchc = new RestContextHandlerClient();
        rchc.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
            Constants.SYSTEM_ADMIN_PASSWORD);

        // retrieving the context
        String contextXml = rchc.retrieve(id);

        // we need last-modification-date
        String[] objidLmd = Util.obtainObjidAndLmd(contextXml);

        String taskParam =
            "<param last-modification-date=\"" + objidLmd[1] + "\">\n"
                + "</param>";

        // task oriented method open()
        String openResult = rchc.open(objidLmd[0], taskParam);

        return openResult;
    }
}
