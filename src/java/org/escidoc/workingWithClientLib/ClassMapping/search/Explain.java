package org.escidoc.workingWithClientLib.ClassMapping.search;

import gov.loc.www.zing.srw.ExplainRequestType;

import java.net.MalformedURLException;
import java.net.URL;

import de.escidoc.core.client.SearchHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.sb.Record;
import de.escidoc.core.resources.sb.RecordPacking;
import de.escidoc.core.resources.sb.explain.ExplainResponse;

/**
 * Example for Explain.
 * 
 * @author JHE
 * 
 */
public class Explain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
            String query = "escidoc:query";
            
            if (args.length == 1) {
                query = args[0];
            }
            
           // Explain
            URL serviceAddress = null;
            try {
                serviceAddress = new URL("");
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            
            SearchHandlerClient c =
                new SearchHandlerClient(serviceAddress);
            
            ExplainRequestType request = new ExplainRequestType();
            request.setRecordPacking(RecordPacking.XML.toString());
            request.setVersion("1.1");

            ExplainResponse response;
            try {
                response = c.explain(request, null);
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
            
            Record record = response.getRecord();
            Explain data = record.getRecordData();

            System.out.println("\n=========================\n");
            System.out.println("testSRWExplain: ");
            System.out.println("RecordPacking: ");
            System.out.println(RecordPacking.XML.toString());
            System.out.println("\n");
            System.out.println(data.toString());
            System.out.println("\n");
            
    }
    
}
