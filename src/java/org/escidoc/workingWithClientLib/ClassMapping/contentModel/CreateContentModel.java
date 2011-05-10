package org.escidoc.workingWithClientLib.ClassMapping.contentModel;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContentModelHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.cmm.ContentModel;
import de.escidoc.core.resources.cmm.ContentModelProperties;
import de.escidoc.core.resources.cmm.MetadataRecordDefinition;
import de.escidoc.core.resources.cmm.MetadataRecordDefinitions;
import de.escidoc.core.resources.cmm.ResourceDefinition;
import de.escidoc.core.resources.cmm.ResourceDefinitions;

/**
 * Example how to create an Content Model by using the eSciDoc Java client library.
 * 
 * @author SWA
 * 
 */
public class CreateContentModel {

    /**
     * 
     * @param args
     *            ignored
     */
    public static void main(String[] args) {


        try {
            // Prepare a value object with new values of Context.
            ContentModel contentModel = prepareContentModel();

            // call create with VO on eSciDoc
            contentModel = createContentModel(contentModel);

            System.out.println("Content Model with objid='" + contentModel.getObjid() + "' at '"
                + contentModel.getLastModificationDate() + "' created.");
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
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * The value object Context is to create and to fill with (at least required) parameter.
     * 
     * @return Context (which is not created within the infrastructure).
     * 
     * @throws URISyntaxException
     */
    private static ContentModel prepareContentModel() throws URISyntaxException {

        ContentModel contentModel = new ContentModel();

        ContentModelProperties properties = new ContentModelProperties();

        // Context requires a name
        properties.setName("Example_Content_Model");

        // description is nice
        properties.setDescription("Example Content Model.");

        contentModel.setProperties(properties);

        // md-record definition
        MetadataRecordDefinition mdRecordDef = new MetadataRecordDefinition();
        mdRecordDef.setName("Name-" + System.nanoTime());
        mdRecordDef.setSchema(new URI(Constants.DEFAULT_SERVICE_URL + "/xsd/rest/content-model/0.1/content-model.xsd"));
        MetadataRecordDefinitions mdrds = new MetadataRecordDefinitions();
        mdrds.add(mdRecordDef);
        contentModel.setMetadataRecordDefinitions(mdrds);

        // resource definitions
        ResourceDefinition rd1 = new ResourceDefinition();
        rd1.setName("transX" + System.nanoTime());
        rd1.setMetadataRecordName("escidoc");
        rd1.setXslt(new URI(Constants.DEFAULT_SERVICE_URL + "/xsl/mapping-unknown2dc-onlyMD.xsl"));
        ResourceDefinitions rds = new ResourceDefinitions();
        rds.add(rd1);
        contentModel.setResourceDefinitions(rds);

        ResourceDefinition rd2 = new ResourceDefinition();
        rd2.setName("transX" + System.nanoTime());
        rd2.setMetadataRecordName("blafasel");
        rd2.setXslt(new URI(Constants.DEFAULT_SERVICE_URL + "/xsl/copy.xsl"));
        contentModel.getResourceDefinitions().add(rd2);

        return contentModel;
    }

    /**
     * Creating the Content Model within the eSciDoc infrastructure. The value object ContentModel is send to the create
     * method of the infrastructure. The infrastructure delivers the created ContentModel as response. The created
     * ContentModel is enriched with values from the infrastructure.
     * 
     * @param contentModel
     *            The value object of a Content Model.
     * @return Value Object of created ContentModel (enriched with values by infrastructure)
     * 
     * @throws EscidocException
     *             Thrown if eSciDoc infrastructure throws an exception. This happens mostly if data structure is
     *             incomplete for the required operation, method is not allowed in object status or permissions are
     *             restricted.
     * @throws InternalClientException
     *             These are thrown if client library internal failure occur.
     * @throws TransportException
     *             Is thrown if transport between client library and framework is malfunctioned.
     * @throws MalformedURLException
     */
    private static ContentModel createContentModel(final ContentModel contentModel) throws EscidocException,
        InternalClientException, TransportException, MalformedURLException {

        // prepare client object
        Authentication auth =
            new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME_SYSADMIN,
                Constants.USER_PASSWORD_SYSADMIN);
        ContentModelHandlerClient cmhc = new ContentModelHandlerClient(auth.getServiceAddress());
        cmhc.setHandle(auth.getHandle());

        ContentModel createdContentModel = cmhc.create(contentModel);

        return createdContentModel;
    }
}
