package org.escidoc.workingWithClientLib.ClassMapping.crud;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.om.item.Item;

/**
 * Create eSciDoc resources with escidoc-core-client-library.
 * 
 * @author SWA
 * 
 */
public class DemoItem {

    /**
     * Create an Item.
     * 
     * @throws TransportException
     *             Thrown in case of errors on transport level.
     * @throws InternalClientException
     *             Thrown if client library internal errors occur.
     * @throws EscidocException
     *             Thrown if framework throws exception.
     * @throws ParserConfigurationException
     */
    public void createItem(final Authentication auth) throws EscidocException,
        InternalClientException, TransportException,
        ParserConfigurationException {

        ItemHandlerClient ihc = new ItemHandlerClient(auth.getServiceAddress());
        
        ihc.setHandle(auth.getHandle());

        Item item = new Item();

        item.getProperties().setContext(
            new ContextRef(Constants.EXAMPLE_CONTEXT_ID));
        item.getProperties().setContentModel(
            new ContentModelRef(Constants.EXAMPLE_CONTENT_MODEL_ID));

        // Metadata Record(s)
        MetadataRecords mdRecords = new MetadataRecords();
        mdRecords.add(getMdRecord("escidoc"));
        item.setMetadataRecords(mdRecords);

        // create
        Item newItem = ihc.create(item);

        System.out.println(newItem.getObjid());
    }

    /**
     * Item lifecycle.
     * 
     * @throws ParserConfigurationException
     * @throws EscidocClientException
     */
    public void lifecycle(final Authentication auth)
        throws ParserConfigurationException, EscidocClientException {

        ItemHandlerClient ihc = new ItemHandlerClient(auth.getServiceAddress());
        
        ihc.setHandle(auth.getHandle());

        // create
        Item item = ihc.create(prepareItem());

        System.out.println(item.getObjid());

        // submit
        TaskParam tp = new TaskParam();
        tp.setLastModificationDate(item.getLastModificationDate());
        tp.setComment("Submitted to show lifecycle");

        Result result = ihc.submit(item, tp);

        // release
        tp.setLastModificationDate(result.getLastModificationDate());
        tp.setComment("Released to show lifecycle");

        ihc.release(item, tp);
    }

    /**
     * Show how to assign persistent identifier to Item.
     * 
     * @throws ParserConfigurationException
     * @throws EscidocClientException
     * @throws MalformedURLException
     */
    public void assignPids(final Authentication auth)
        throws ParserConfigurationException, EscidocClientException,
        MalformedURLException {

        ItemHandlerClient ihc = new ItemHandlerClient(auth.getServiceAddress());
        
        ihc.setHandle(auth.getHandle());

        // create --------------------------------------------------------------
        Item item = ihc.create(prepareItem());

        System.out.println(item.getObjid());

        // assign object PID ---------------------------------------------------
        TaskParam taskParam = new TaskParam();
        taskParam.setLastModificationDate(item.getLastModificationDate());
        taskParam.setUrl(new URL("http://solution/for/this/resource"));
        taskParam.setComment("Object PID on eSciDoc Days 2009");

        Result result = ihc.assignObjectPid(item, taskParam);

        // assign version PID --------------------------------------------------
        taskParam.setLastModificationDate(result.getLastModificationDate());
        taskParam.setUrl(new URL("http://solution/for/this/resource"));
        taskParam.setComment("Object PID on eSciDoc Days 2009");

        ihc.assignObjectPid(item, taskParam);

    }

    /**
     * Prepare Item for create. taskParam@return Item.
     * 
     * @throws ParserConfigurationException
     */
    private Item prepareItem() throws ParserConfigurationException {
        Item item = new Item();

        item.getProperties().setContext(
            new ContextRef(Constants.EXAMPLE_CONTEXT_ID));
        item.getProperties().setContentModel(
            new ContentModelRef(Constants.EXAMPLE_CONTENT_MODEL_ID));

        // Metadata Record(s)
        MetadataRecords mdRecords = new MetadataRecords();
        mdRecords.add(getMdRecord("escidoc"));
        item.setMetadataRecords(mdRecords);

        return item;
    }

    /**
     * Get MetadataRecord.
     * 
     * @param name
     *            Name of md-record.
     * @return MetadataRecord
     * @throws ParserConfigurationException
     */
    private MetadataRecord getMdRecord(final String name)
        throws ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        MetadataRecord mdRecord = new MetadataRecord();
        mdRecord.setName(name);

        Element element = doc.createElementNS(null, "myMdRecord");
        mdRecord.setContent(element);

        return mdRecord;
    }
}
