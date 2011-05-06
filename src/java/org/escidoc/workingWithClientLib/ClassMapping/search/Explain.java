package org.escidoc.workingWithClientLib.ClassMapping.search;

import gov.loc.www.zing.srw.ExplainRequestType;

import java.util.List;

import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.interfaces.ItemHandlerClientInterface;
import de.escidoc.core.resources.sb.Record;
import de.escidoc.core.resources.sb.RecordPacking;
import de.escidoc.core.resources.sb.explain.ExplainResponse;
import de.escidoc.core.resources.sb.explain.Index;

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
        
        ItemHandlerClientInterface c = new ItemHandlerClient();

        ExplainRequestType request = new ExplainRequestType();
        
        ExplainResponse response =  null;
        
        try {
            response = c.retrieveItems(request);
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
        
        Record<de.escidoc.core.resources.sb.explain.Explain> record = response.getRecord();
        
        de.escidoc.core.resources.sb.explain.Explain data = record.getRecordData();

        List<Index> indexes = data.getIndexInfo().getIndexes();
        
            System.out.println("\n=========================\n");
            System.out.println("Test Explain: ");
            System.out.println("RecordPacking: " + RecordPacking.XML.toString());
            System.out.println("\n\n");
            System.out.println("available search fields for Items:\n");
            for (Index index : indexes){
                System.out.println(index.getTitle());
            }
            
            System.out.println("\n");
            
    }
    
}
