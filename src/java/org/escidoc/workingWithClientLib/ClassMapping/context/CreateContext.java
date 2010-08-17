package org.escidoc.workingWithClientLib.ClassMapping.context;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;
import de.escidoc.core.resources.om.context.Properties;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * Example how to create an Context by using the eSciDoc Java client library.
 * 
 * At least one Organizational Unit (OU) has to exists. The reference of the OU
 * is to define as reference (see prepareContext method).
 * 
 * @author SWA
 * 
 */
public class CreateContext {

    public static void main(String[] args) {

        // Prepare a value object with new values of Context.
        Context context = prepareContext();

        try {
            // call create with VO on eSciDoc
            context = createContext(context);

            System.out.println("Context with objid='" + context.getObjid()
                + "' at '" + context.getLastModificationDate() + "' created.");
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
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * The value object Context is to create and to fill with (at least
     * required) parameter.
     * 
     * @return Context (which is not created within the infrastructure).
     */
    private static Context prepareContext() {

        Context context = new Context();

        Properties properties = new Properties();

        // Context requires a name
        properties.setName("Example_Package_Context");

        // description is nice
        properties.setDescription("Example package Context.");

        // define the type
        properties.setType("ExampleType");

        /*
         * Organizational Unit(s) is/are required
         */
        OrganizationalUnitRefs ous = new OrganizationalUnitRefs();

        // add the Organizational Unit with objid escidoc:ex3 (the ou of the
        // example eSciDoc representation package) to the list of
        // organizational Units
        ous.add(new ResourceRef("escidoc:ex3"));
        properties.setOrganizationalUnitRefs(ous);

        context.setProperties(properties);

        return context;
    }

    /**
     * Creating the Context within the eSciDoc infrastructure. The value object
     * Context is send to the create method of the infrastructure. The
     * infrastructure delivers the the created Context as response. The created
     * Context is enriched with values from the infrastructure.
     * 
     * @param context
     *            The value object of a Context.
     * @return Value Object of created Context (enriched with values by
     *         infrastructure)
     * @throws EscidocClientException
     */
    private static Context createContext(final Context context)
        throws EscidocClientException {

        // authentication (Use a user account with write permission for Context
        // on the selected Context. Usually is this user with administrator
        // role).
        Authentication auth =
            new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        ContextHandlerClient client = new ContextHandlerClient();
        client.setServiceAddress(auth.getServiceAddress());
        client.setHandle(auth.getHandle());

        Context createdContext = client.create(context);

        return createdContext;
    }
}
