package org.escidoc.workingWithClientLib.RESTHandler.context;

import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestContainerHandlerClient;
import de.escidoc.core.client.rest.RestContextHandlerClient;

/**
 * Example how to set status of a Context to 'closed' by using the XML REST
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
public class CloseContext {

    /**
     * Set status of Organizational Unit to closed.
     * 
     * @param args
     *            If args[0] is given, it is used as objid for Organizational
     *            Unit. Otherwise is default value used.
     */
    public static void main(String[] args) {

        String id = "escidoc:context";
        if (args.length == 1) {
            id = args[0];
        }

        try {
            String responseXml = closeContext(id);

            // write out objid and last modification date
            String[] objidLmd = Util.obtainObjidAndLmd(responseXml);
            System.out.println("Context with objid='" + id + "' at '"
                + objidLmd[1] + "' set to closed");

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
			e.printStackTrace();
		}

    }

    /**
     * Close Context.
     * 
     * <ul>
     *   <li>get REST handler for object</li>
     *   <li>login</li>
     *   <li>call close method at handler</li>
     * </ul>
     * @param id
     *            objid of to close Context
     * @return XML response of close method from framework
     * @throws EscidocClientException
     * @throws MalformedURLException 
     */
    public static String closeContext(final String id) throws EscidocClientException, MalformedURLException {

        // prepare client handler
        Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME, Constants.USER_PASSWORD);
        RestContextHandlerClient rchc = new RestContextHandlerClient(auth.getServiceAddress());
        rchc.setHandle(auth.getHandle());

        // retrieving the context
        String contextXml = rchc.retrieve(id);

        // we need last-modification-date
        String[] objidLmd = Util.obtainObjidAndLmd(contextXml);

        String taskParam =
            "<param last-modification-date=\"" + objidLmd[1] + "\">\n"
                + "</param>";
        
     // task oriented method close()
        String closeResult = rchc.close(objidLmd[0], taskParam);

        return closeResult;
    }
}
