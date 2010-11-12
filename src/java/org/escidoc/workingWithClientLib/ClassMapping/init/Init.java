package org.escidoc.workingWithClientLib.ClassMapping.init;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.ContentModelHandlerClient;
import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.ItemHandlerClient;
import de.escidoc.core.client.OrganizationalUnitHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.cmm.ContentModel;
import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.common.reference.ContentModelRef;
import de.escidoc.core.resources.common.reference.ContextRef;
import de.escidoc.core.resources.common.reference.OrganizationalUnitRef;
import de.escidoc.core.resources.om.container.Container;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;
import de.escidoc.core.resources.om.item.Item;
import de.escidoc.core.resources.oum.OrganizationalUnit;

/**
 * 
 * @author mro
 * 
 */
public class Init {

	public static void main(String[] args) throws TransportException,
			EscidocException, InternalClientException,
			ParserConfigurationException {

		// Auth
		Authentication auth = new Authentication(Constants.SERVICE_URL,
				Constants.USER, Constants.PASS);

		// Create Organizational Unit
		System.out.print("Creating Organizational Unit... ");
		String ouObjid = createOrganizationalUnit(auth);
		System.out.println(ouObjid);

		// Open Organizational Unit
		System.out.print("Opening Organizational Unit... ");
		openOrganizationalUnit(ouObjid, auth);
		System.out.println("done");

		// Create Content Model
		System.out.print("Creating Content Model... ");
		String contentModelObjid = createContentModel(auth);
		System.out.println(contentModelObjid);

		// Create Context
		System.out.print("Creating Context... ");
		String contextObjid = createContext(ouObjid, auth);
		System.out.println(contextObjid);

		// Open Context
		System.out.print("Opening Context... ");
		openContext(contextObjid, auth);
		System.out.println("done");

		// Create Container (promo)
		System.out.print("Creating Container (promo)... ");
		String container1Objid = createContainer(
				"Promo",
				"This container contains promotional info on the ColdFusion project.",
				contextObjid, contentModelObjid, auth);
		System.out.println(container1Objid);

		// Create Container (findings)
		System.out.print("Creating Container (findings)... ");
		String container2Objid = createContainer("Findings",
				"This container contains all our research fndings",
				contextObjid, contentModelObjid, auth);
		System.out.println(container2Objid);

		// SubmitRelease Container
		System.out.print("Releasing Containers... ");
		submitReleaseContainer(container1Objid, auth);
		submitReleaseContainer(container2Objid, auth);
		System.out.println("done");

		// Create Item (flyer)
		System.out.print("Creating Item (flyer)... ");
		String item1Objid = createItem("flyer 2010-11",
				"This is our promotional flyer for november.", contextObjid,
				contentModelObjid, auth);
		System.out.println(item1Objid);

		// Create Item (announcement)
		System.out.print("Creating Item (announcement)... ");
		String item2Objid = createItem("Announcement 2011",
				"This is our customer announcement for 2011.", contextObjid,
				contentModelObjid, auth);
		System.out.println(item2Objid);
		
		// Create Item (splittingNeutrons)
		System.out.print("Creating Item (splittingNeutrons)... ");
		String item3Objid = createItem("Splitting Neutrons",
				"This is a detailed explanation of how to split neutrons.", contextObjid,
				contentModelObjid, auth);
		System.out.println(item2Objid);

		// SubmitRelease Items (flyer and splittingNeutrons)
		System.out.print("SubmitReleasing Items (flyer and splittingNeutrons)... ");
		submitReleaseItem(item1Objid, auth);
		submitReleaseItem(item3Objid, auth);
		System.out.println("done");

		// Add Items to Promo-Container
		System.out.print("Adding Items to Promo-Container... ");
		addItemToContainer(item1Objid, container1Objid, auth);
		addItemToContainer(item2Objid, container1Objid, auth);
		System.out.println("done");
		
		// Add Item to Promo-Container
		System.out.print("Adding Item to Findings-Container... ");
		addItemToContainer(item3Objid, container2Objid, auth);
		System.out.println("done");

		// KTHXBYE
		System.out.println("KTHXBYE");
		auth.logout();
	}

	private static String createOrganizationalUnit(Authentication auth)
			throws ParserConfigurationException, EscidocException,
			InternalClientException, TransportException {

		OrganizationalUnitHandlerClient handler = new OrganizationalUnitHandlerClient(
				auth.getServiceAddress());
		handler.setHandle(auth.getHandle());

		OrganizationalUnit ou = new OrganizationalUnit();
		de.escidoc.core.resources.oum.Properties properties = new de.escidoc.core.resources.oum.Properties();
		ou.setProperties(properties);

		MetadataRecord mdRecord = getMdRecord("escidoc", "shishoMeta",
				"shisho Labs",
				"shisho Labs is a well-known research facility located in San Seriffe.");
		MetadataRecords mdRecords = new MetadataRecords();
		mdRecords.add(mdRecord);
		ou.setMetadataRecords(mdRecords);

		ou = handler.create(ou);
		return ou.getObjid();
	}

	private static void openOrganizationalUnit(String ouObjid,
			Authentication auth) throws EscidocException,
			InternalClientException, TransportException {

		OrganizationalUnitHandlerClient handler = new OrganizationalUnitHandlerClient(
				auth.getServiceAddress());
		handler.setHandle(auth.getHandle());

		OrganizationalUnit ou = handler.retrieve(ouObjid);

		TaskParam taskParam = new TaskParam();
		taskParam.setComment("Organizational Unit opened");
		taskParam.setLastModificationDate(ou.getLastModificationDate());

		handler.open(ou.getObjid(), taskParam);
	}

	private static String createContentModel(Authentication auth)
			throws EscidocException, InternalClientException,
			TransportException {

		ContentModelHandlerClient handler = new ContentModelHandlerClient(
				auth.getServiceAddress());
		handler.setHandle(auth.getHandle());

		ContentModel contentModel = new ContentModel();
		contentModel.getProperties().setName("shisho Lab's ContentModel");
		contentModel.getProperties()
				.setDescription("This is our ContentModel.");

		contentModel = handler.create(contentModel);
		return contentModel.getObjid();
	}

	private static String createContext(String ouObjid, Authentication auth)
			throws EscidocException, InternalClientException,
			TransportException {

		ContextHandlerClient handler = new ContextHandlerClient(
				auth.getServiceAddress());
		handler.setHandle(auth.getHandle());

		Context context = new Context();
		de.escidoc.core.resources.om.context.Properties properties = new de.escidoc.core.resources.om.context.Properties();

		properties.setName("ColdFusion Context" + System.nanoTime());
		properties
				.setDescription("This is the Context of our ColdFusion Project.");
		properties.setType("Type A-Positive");
		OrganizationalUnitRefs ous = new OrganizationalUnitRefs();
		ous.add(new OrganizationalUnitRef(ouObjid));
		properties.setOrganizationalUnitRefs(ous);
		context.setProperties(properties);

		context = handler.create(context);
		return context.getObjid();
	}

	private static void openContext(String contextObjid, Authentication auth)
			throws EscidocException, InternalClientException,
			TransportException {

		ContextHandlerClient handler = new ContextHandlerClient(
				auth.getServiceAddress());
		handler.setHandle(auth.getHandle());

		Context context = handler.retrieve(contextObjid);

		TaskParam taskParam = new TaskParam();
		taskParam.setComment("Context opened");
		taskParam.setLastModificationDate(context.getLastModificationDate());

		handler.open(context.getObjid(), taskParam);
	}

	private static String createContainer(String name, String description,
			String contextObjid, String contentModelObjid, Authentication auth)
			throws ParserConfigurationException, EscidocException,
			InternalClientException, TransportException {

		ContainerHandlerClient handler = new ContainerHandlerClient(
				auth.getServiceAddress());
		handler.setHandle(auth.getHandle());

		Container container = new Container();

		container.getProperties().setContext(new ContextRef(contextObjid));
		container.getProperties().setContentModel(
				new ContentModelRef(contentModelObjid));

		MetadataRecords mdRecords = new MetadataRecords();
		MetadataRecord mdrecord = getMdRecord("escidoc", "shishoMeta", name,
				description);
		mdRecords.add(mdrecord);
		container.setMetadataRecords(mdRecords);

		container = handler.create(container);
		return container.getObjid();
	}

	private static void submitReleaseContainer(String containerObjid,
			Authentication auth) throws EscidocException,
			InternalClientException, TransportException {

		ContainerHandlerClient handler = new ContainerHandlerClient(
				auth.getServiceAddress());
		handler.setHandle(auth.getHandle());

		Container container = handler.retrieve(containerObjid);

		TaskParam taskParam = new TaskParam();
		taskParam.setLastModificationDate(container.getLastModificationDate());
		taskParam.setComment("Container submitted");
		Result submitResult = handler.submit(container, taskParam);

		taskParam = new TaskParam();
		taskParam.setLastModificationDate(submitResult
				.getLastModificationDate());
		taskParam.setComment("Container released");
		handler.release(container, taskParam);
	}

	private static String createItem(String name, String description,
			String contextObjid, String contentModelObjid, Authentication auth)
			throws ParserConfigurationException, EscidocException,
			InternalClientException, TransportException {

		ItemHandlerClient ihc = new ItemHandlerClient(auth.getServiceAddress());
		ihc.setHandle(auth.getHandle());

		Item item = new Item();

		item.getProperties().setContext(new ContextRef(contextObjid));
		item.getProperties().setContentModel(
				new ContentModelRef(contentModelObjid));

		MetadataRecords mdRecords = new MetadataRecords();
		MetadataRecord mdrecord = getMdRecord("escidoc", "shishoMeta", name,
				description);
		mdRecords.add(mdrecord);
		item.setMetadataRecords(mdRecords);

		item = ihc.create(item);
		return item.getObjid();
	}

	private static void submitReleaseItem(String itemObjid, Authentication auth)
			throws EscidocException, InternalClientException,
			TransportException {

		ItemHandlerClient handler = new ItemHandlerClient(
				auth.getServiceAddress());
		handler.setHandle(auth.getHandle());

		Item item = handler.retrieve(itemObjid);

		TaskParam taskParam = new TaskParam();
		taskParam.setLastModificationDate(item.getLastModificationDate());
		taskParam.setComment("Item submitted");
		Result submitResult = handler.submit(item, taskParam);

		taskParam = new TaskParam();
		taskParam.setLastModificationDate(submitResult
				.getLastModificationDate());
		taskParam.setComment("Item released");
		handler.release(item, taskParam);
	}

	private static void addItemToContainer(String itemObjid,
			String containerObjid, Authentication auth)
			throws EscidocException, InternalClientException,
			TransportException {

		ContainerHandlerClient handler = new ContainerHandlerClient(
				auth.getServiceAddress());
		handler.setHandle(auth.getHandle());

		Container container = handler.retrieve(containerObjid);

		TaskParam taskParam = new TaskParam();
		taskParam.setLastModificationDate(container.getLastModificationDate());
		taskParam.setComment("Members added");
		taskParam.addResourceRef(itemObjid);

		handler.addMembers(container.getObjid(), taskParam);
	}

	private static MetadataRecord getMdRecord(final String mdRecordName,
			final String rootElementName, final String title,
			final String description) throws ParserConfigurationException {

		// md-record
		MetadataRecord mdRecord = new MetadataRecord();
		mdRecord.setName(mdRecordName);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setCoalescing(true);
		factory.setValidating(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document doc = builder.newDocument();
		Element mdRecordContent = doc.createElementNS(null, rootElementName);
		mdRecord.setContent(mdRecordContent);

		// title
		Element titleElmt = doc.createElementNS(
				"http://purl.org/dc/elements/1.1/", "title");
		titleElmt.setPrefix("dc");
		titleElmt.setTextContent(title);
		mdRecordContent.appendChild(titleElmt);

		// dc:description
		Element descriptionElmt = doc.createElementNS(
				"http://purl.org/dc/elements/1.1/", "description");
		descriptionElmt.setPrefix("dc");
		descriptionElmt.setTextContent(description);
		mdRecordContent.appendChild(descriptionElmt);
		mdRecord.setContent(mdRecordContent);

		return mdRecord;
	}
}
