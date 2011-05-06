package org.escidoc.workingWithClientLib.ClassMapping.search;

import gov.loc.www.zing.srw.SearchRetrieveRequestType;

import java.util.Collection;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.resources.sb.RecordPacking;
import de.escidoc.core.resources.sb.search.SearchResult;
import de.escidoc.core.resources.sb.search.SearchResultRecord;
import de.escidoc.core.resources.sb.search.SearchRetrieveResponse;

/**
 * Example for SRW Search.
 * 
 * @author JHE
 * 
 */
public class FilterSearch {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String query = "PID=e*";

        if (args.length == 1) {
            query = args[0];
        }

        ItemHandlerClientInterface c = new ItemHandlerClient();

        SearchRetrieveRequestType request = new SearchRetrieveRequestType();
        request.setQuery(query);
//        request.setMaximumRecords(maximumRecords)
        request.setRecordPacking(RecordPacking.XML.toString());

        SearchRetrieveResponse response = null;
        try {
            response = c.retrieveItems(request);
        }
        catch (EscidocException e1) {
            e1.printStackTrace();
        }
        catch (InternalClientException e1) {
            e1.printStackTrace();
        }
        catch (TransportException e1) {
            e1.printStackTrace();
        }

        System.out.println("\n=========================\n");
        System.out.println("testFilterSearch [1]: query=");
        System.out.println(query);
        System.out.println(" [RecordPacking: ");
        System.out.println(RecordPacking.XML.toString());
        System.out.println("]\n");
        System.out.println("Results: ");
        System.out.println(response.getNumberOfResultingRecords());
        System.out.println("\n");

        for (SearchResultRecord record : response.getRecords()) {
            SearchResult result = record.getRecordData();

            if (result.getContent() instanceof Item) {
                Item item = (Item) result.getContent();
                System.out.println("Item: ID[" + item.getObjid() + "], Href[" + item.getXLinkHref() + "]\n");
            }
        }

//        Collection<Item> items = null;
//        try {
//            items = c.retrieveItemsAsList(request);
//        }
//        catch (EscidocException e) {
//            e.printStackTrace();
//        }
//        catch (InternalClientException e) {
//            e.printStackTrace();
//        }
//        catch (TransportException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("\ntestFilterSearch [2]:\n");
//        System.out.println("Results: ");
//        System.out.println(items.size());
//        System.out.println("\n");
//
//        for (Item item : items) {
//            System.out.println("Item: ID[");
//            System.out.println(item.getObjid());
//            System.out.println("], Href[");
//            System.out.println(item.getXLinkHref());
//            System.out.println("]\n");
//        }
    }
}
