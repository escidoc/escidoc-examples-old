package org.escidoc.workingWithClientLib.ClassMapping.search;

import java.net.MalformedURLException;
import java.net.URL;

import de.escidoc.core.client.SearchHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.Resource;
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
public class SRWSearch {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String query = "escidoc:query";

        if (args.length == 1) {
            query = args[0];
        }

       // SRW Search
        URL serviceAddress = null;
        try {
            serviceAddress = new URL("");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        SearchHandlerClient c = new SearchHandlerClient(serviceAddress);

        SearchRetrieveResponse response = null;
        try {
            response = c.search(query, null);
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

        System.out.println("\n=========================\n");
        System.out.println("SRWSearch: query=");
        System.out.println(query);
        System.out.println(" [RecordPacking: ");
        System.out.println(RecordPacking.XML.toString());
        System.out.println("]\n");
        System.out.println("Results: ");
        System.out.println(response.getNumberOfResultingRecords());
        System.out.println("\n");

        for (SearchResultRecord record : response.getRecords()) {

            SearchResult data = record.getRecordData();

            if (data.getContent() instanceof Resource) {

                Resource res = (Resource) data.getContent();

                System.out.println(res.getResourceType().name() + ": ID[" + res.getObjid() + "], Href["
                    + res.getXLinkHref() + "], Score[" + data.getScore() + "]\n");
            }
        }
    }
}
