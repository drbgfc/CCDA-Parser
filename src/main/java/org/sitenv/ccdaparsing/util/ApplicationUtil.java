package org.sitenv.ccdaparsing.util;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.sitenv.ccdaparsing.model.CCDAAddress;
import org.sitenv.ccdaparsing.model.CCDACode;
import org.sitenv.ccdaparsing.model.CCDADataElement;
import org.sitenv.ccdaparsing.model.CCDAEffTime;
import org.sitenv.ccdaparsing.model.CCDAFrequency;
import org.sitenv.ccdaparsing.model.CCDAID;
import org.sitenv.ccdaparsing.model.CCDAII;
import org.sitenv.ccdaparsing.model.CCDAPQ;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ApplicationUtil {
	
	public static CCDACode readCode(Element codeElement)throws TransformerException
	{
		CCDACode code = null;
		if(codeElement != null)
		{
			code = new CCDACode();
			if(!isEmpty(codeElement.getAttribute("code")))
			{
				code.setCode(codeElement.getAttribute("code"));
			}
			if(!isEmpty(codeElement.getAttribute("codeSystem")))
			{
				code.setCodeSystem(codeElement.getAttribute("codeSystem"));
			}
			if(!isEmpty(codeElement.getAttribute("codeSystemName")))
			{
				code.setCodeSystemName(codeElement.getAttribute("codeSystemName"));
			}
			if(!isEmpty(codeElement.getAttribute("displayName")))
			{
				code.setDisplayName(codeElement.getAttribute("displayName"));
			}
			if(!isEmpty(codeElement.getAttribute("xsi:type")))
			{
				code.setXpath(codeElement.getAttribute("xsi:type"));
			}
			
			codeElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			code.setXmlString(nodeToString((Node)codeElement));
			code.setLineNumber(codeElement.getUserData("lineNumber").toString());
		}
		return code;
	}

	public static CCDAII readTemplateID(Element templateElement) throws TransformerException {
		return readTemplateID(templateElement, true);
	}	
	public static CCDAII readTemplateID(Element templateElement, boolean setXmlAndLinNumber) throws TransformerException
	{
		CCDAII templateID = null;
		
		if(templateElement != null)
		{
			templateID = new CCDAII();
			if(!isEmpty(templateElement.getAttribute("root")))
			{
				templateID.setRootValue(templateElement.getAttribute("root"));
			}
			if(!isEmpty(templateElement.getAttribute("extension")))
			{
				templateID.setExtValue(templateElement.getAttribute("extension"));
			}
			
			if(setXmlAndLinNumber) {
				templateID.setXmlString(nodeToString((Node)templateElement));
				templateID.setLineNumber(templateElement.getUserData("lineNumber").toString());
			}
			
		}
		return templateID;
	}
	
	public static CCDAID readID(Element idElement, String parentElement)throws TransformerException
	{
		CCDAID id = null;
		
		if(idElement != null)
		{
			id = new CCDAID();
			if(!isEmpty(idElement.getAttribute("root")))
			{
				id.setRoot(idElement.getAttribute("root"));
			}
			
			if(!isEmpty(idElement.getAttribute("extension")))
			{
				id.setExtension(idElement.getAttribute("extension"));
			}
			
			id.setParentElementName(parentElement);
			
			id.setXmlString(nodeToString((Node)idElement));
			id.setLineNumber(idElement.getUserData("lineNumber").toString());
		}
		return id;
	}
		
	public static ArrayList<CCDAII> readTemplateIdList(NodeList templateIDNodeList) throws TransformerException {
		return readTemplateIdList(templateIDNodeList, true);
	}	
	public static ArrayList<CCDAII> readTemplateIdList(NodeList templateIDNodeList, boolean setXmlAndLinNumber) throws TransformerException
	{
		ArrayList<CCDAII> templateList = null;
		if( ! isNodeListEmpty(templateIDNodeList))
		{
			templateList = new ArrayList<>();
		}
		Element templateElement;
		for (int i = 0; i < templateIDNodeList.getLength(); i++) {
			templateElement = (Element) templateIDNodeList.item(i);
			if(setXmlAndLinNumber) {
				templateList.add(readTemplateID(templateElement));
			} else {
				templateList.add(readTemplateID(templateElement, false));
			}
		}
		return templateList;
	} 
	
	public static CCDAEffTime readEffectivetime(Element effectiveTimeElement,XPath xPath) throws XPathExpressionException,TransformerException
	{
		CCDAEffTime effectiveTime = null;
		
		if(effectiveTimeElement != null)
		{
			effectiveTime = new CCDAEffTime();
			
			if(!isEmpty(effectiveTimeElement.getAttribute("value")))
			{
				effectiveTime.setValue(effectiveTimeElement.getAttribute("value"));
				effectiveTime.setValuePresent(true);
			}else
			{
				effectiveTime.setValuePresent(false);
			}
			
			effectiveTime.setLow(readDataElement((Element) xPath.compile("./low[not(@nullFlavor)]").
	    				evaluate(effectiveTimeElement, XPathConstants.NODE)));
			effectiveTime.setHigh(readDataElement((Element) xPath.compile("./high[not(@nullFlavor)]").
	    				evaluate(effectiveTimeElement, XPathConstants.NODE)));
			if(effectiveTime.getLow() != null)
			{
				effectiveTime.setLowPresent(true);
			}else
				effectiveTime.setLowPresent(false);
			
			if(effectiveTime.getHigh() != null)
			{
				effectiveTime.setHighPresent(true);
			}else
				effectiveTime.setHighPresent(false);
			
			effectiveTimeElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			effectiveTime.setXmlString(nodeToString((Node)effectiveTimeElement));
			effectiveTime.setLineNumber(effectiveTimeElement.getUserData("lineNumber").toString() + " - " + effectiveTimeElement.getUserData("endLineNumber"));
				
		}
		return effectiveTime;
	}
	
	/*public static ArrayList<String> readSectionTextReferences(NodeList rowList,XPath xPath) throws XPathExpressionException,TransformerException
	{
		ArrayList<String> textReferences=new ArrayList<String>();
		NodeList tdColumnList = null;
		Element tdColumnElement = null;
		Element contentElement = null;
		
		if( ! isNodeListEmpty(rowList))
		{
			for (int i = 0; i < rowList.getLength(); i++) 
			{
				tdColumnList = (NodeList)xPath.compile("./td[not(@nullFlavor)]").evaluate((Element)rowList.item(i), XPathConstants.NODESET);
				
				if( ! isNodeListEmpty(tdColumnList))
				{
					for (int j = 0; j < tdColumnList.getLength(); j++)
					{
						tdColumnElement = (Element) tdColumnList.item(j);
						if(tdColumnElement!=null)
						{
							if(!isEmpty(tdColumnElement.getAttribute("ID")))
							{
								textReferences.add("#"+tdColumnElement.getAttribute("ID"));
							}
							
							contentElement = (Element)xPath.compile("./content[not(@nullFlavor)]").evaluate(tdColumnElement, XPathConstants.NODE);
							if(contentElement!=null)
							{
								if(!isEmpty(contentElement.getAttribute("ID")))
								{
									textReferences.add("#"+contentElement.getAttribute("ID"));
								}
							}
						}
					}
				}
			}
		}
		return textReferences;
	}*/
	
	public static ArrayList<String> readSectionTextReferences(NodeList referenceList) throws XPathExpressionException,TransformerException
	{
		ArrayList<String> textReferences=new ArrayList<String>();
		Element referenceElement = null;
		
		if( ! isNodeListEmpty(referenceList))
		{
			for (int i = 0; i < referenceList.getLength(); i++) 
			{
				referenceElement = (Element) referenceList.item(i);
				if(referenceElement!=null)
				{
					if(!isEmpty(referenceElement.getAttribute("ID")))
					{
					   textReferences.add("#"+referenceElement.getAttribute("ID"));
					}
				}
			}
		}
		return textReferences;
	}
	
	
	public static ArrayList<CCDADataElement> readTextReferences(NodeList referenceList) throws XPathExpressionException,TransformerException
	{
		ArrayList<CCDADataElement> textReferences=new ArrayList<CCDADataElement>();
		Element referenceElement = null;
		CCDADataElement dataElement= null;
		
		if( ! isNodeListEmpty(referenceList))
		{
			for (int i = 0; i < referenceList.getLength(); i++) 
			{
				referenceElement = (Element) referenceList.item(i);
				if(referenceElement!=null)
				{
					if(!isEmpty(referenceElement.getAttribute("value")))
					{
					   dataElement = new CCDADataElement();
					   dataElement.setValue(referenceElement.getAttribute("value"));
					   dataElement.setXmlString(nodeToString((Node)referenceElement));
					   dataElement.setLineNumber(referenceElement.getUserData("lineNumber").toString());
					   textReferences.add(dataElement);
					}
				}
			}
		}
		return textReferences;
	}
	
	public static CCDADataElement readDataElement(Element nodeElement)throws TransformerException
	{
		CCDADataElement dataElement = null;
		if(nodeElement != null)
		{
			dataElement = new CCDADataElement();
			if(!isEmpty(nodeElement.getAttribute("value")))
			{
				dataElement.setValue(nodeElement.getAttribute("value"));
			}
			/*if(!isEmpty(nodeElement.getAttribute("lineNumber")))
			{
				dataElement.setLineNumber(Integer.parseInt(nodeElement.getAttribute("lineNumber")));
			}*/
			if(!isEmpty(nodeElement.getAttribute("xpath")))
			{
				dataElement.setXpath(nodeElement.getAttribute("xpath"));
			}
			if(!isEmpty(nodeElement.getAttribute("use")))
			{
				dataElement.setUse(nodeElement.getAttribute("use"));
			}
			
			nodeElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			dataElement.setXmlString(nodeToString((Node)nodeElement));
			dataElement.setLineNumber(nodeElement.getUserData("lineNumber").toString());
		}
		
		
		return dataElement;
	}
	
	public static ArrayList<CCDADataElement> readDataElementList(NodeList dataElementNodeList)throws TransformerException
	{
		ArrayList<CCDADataElement> dataElementList = null;
		if( ! isNodeListEmpty(dataElementNodeList))
		{
			dataElementList = new ArrayList<>();
		}
		Element dataElement;
		for (int i = 0; i < dataElementNodeList.getLength(); i++) {
			dataElement = (Element) dataElementNodeList.item(i);
			dataElementList.add(readDataElement(dataElement));
		}
		return dataElementList;
	}
	
	public static CCDAPQ readQuantity(Element quantityElement)throws TransformerException
	{
		CCDAPQ quantity = null;
		if(quantityElement != null)
		{
			quantity = new CCDAPQ();
			if(!isEmpty(quantityElement.getAttribute("unit")))
			{
				quantity.setUnits(quantityElement.getAttribute("unit"));
			}
			if(!isEmpty(quantityElement.getAttribute("value")))
			{
				quantity.setValue(quantityElement.getAttribute("value"));
			}
			quantityElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			quantity.setXmlString(nodeToString((Node)quantityElement));
			quantity.setLineNumber(quantityElement.getUserData("lineNumber").toString());
		}
		return quantity;
	}
	
	public static ArrayList<CCDACode> readCodeList(NodeList codeNodeList)throws TransformerException
	{
		ArrayList<CCDACode> codeList = null;
		if( ! isNodeListEmpty(codeNodeList))
		{
			codeList = new ArrayList<>();
		}
		Element codeElement;
		for (int i = 0; i < codeNodeList.getLength(); i++) {
			codeElement = (Element) codeNodeList.item(i);
			codeList.add(readCode(codeElement));
		}
		return codeList;
	}
	
	public static CCDAFrequency readFrequency(Element frequencyElement)throws TransformerException
	{
		CCDAFrequency frequency = null;
		if(frequencyElement != null)
		{
			frequency =new CCDAFrequency();
			if(!isEmpty(frequencyElement.getAttribute("institutionSpecified")))
			{
				frequency.setInstitutionSpecified(Boolean.parseBoolean(frequencyElement.getAttribute("institutionSpecified")));
			}
			if(!isEmpty(frequencyElement.getAttribute("operator")))
			{
				frequency.setOperator(frequencyElement.getAttribute("operator"));
			}
			Element periodElement  = (Element) frequencyElement.getElementsByTagName("period").item(0);
			if(periodElement != null)
			{
				if(!isEmpty(periodElement.getAttribute("value")))
				{
					frequency.setValue(periodElement.getAttribute("value"));
				}
				if(!isEmpty(periodElement.getAttribute("unit")))
				{
					frequency.setUnit(periodElement.getAttribute("unit"));
				}
			}
			frequencyElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			frequency.setXmlString(nodeToString((Node)frequencyElement));
			frequency.setLineNumber(frequencyElement.getUserData("lineNumber").toString());
		}
		
		return frequency;
	}
	
	public static boolean isEmpty(final String str)
	{
		return str == null || str.trim().length() == 0;
	}
	
	public static boolean isNodeListEmpty(final NodeList nodeList)
	{
		return nodeList == null || nodeList.getLength() == 0;
	}
	
	public static ArrayList<CCDAAddress> readAddressList(NodeList addressNodeList, XPath xpath)throws XPathExpressionException
	{
		ArrayList<CCDAAddress> addressList = null;
		if( ! isNodeListEmpty(addressNodeList))
		{
			addressList = new ArrayList<CCDAAddress>(); 
		}
		Element addrElement;
		for (int i = 0; i < addressNodeList.getLength(); i++) {
			addrElement = (Element) addressNodeList.item(i);
			addressList.add(readAddress(addrElement, xpath));
		}
		return addressList;
	}
	
	public static ArrayList<CCDADataElement> readTextContentList(NodeList inputNodeList)
	{
		ArrayList<CCDADataElement> dataList = null;
		if( ! isNodeListEmpty(inputNodeList))
		{
			dataList = new ArrayList<CCDADataElement>();
		}
		for (int i = 0; i < inputNodeList.getLength(); i++) {
			Element value = (Element) inputNodeList.item(i);
			dataList.add(readTextContext(value));
		}
		return dataList;
	}
	
	public static CCDADataElement readTextContext(Element element)
	{
		return element == null ? null : new CCDADataElement(element.getTextContent()) ;
	}
	
	public static CCDAAddress readAddress(Element addrElement , XPath xPath)throws XPathExpressionException
	{
		CCDAAddress address = null;
		if(addrElement != null)
		{
			address = new CCDAAddress();
			
			if(!isEmpty(addrElement.getAttribute("use")))
			{
				address.setPostalAddressUse(addrElement.getAttribute("use"));
			}
			
			address.setAddressLine1(readTextContext((Element) xPath.compile("./streetAddressLine[not(@nullFlavor)]").
	    				evaluate(addrElement, XPathConstants.NODE)));
			
			address.setAddressLine2(readTextContext((Element) xPath.compile("./streetAddressLine2[not(@nullFlavor)]").
	    				evaluate(addrElement, XPathConstants.NODE)));
			
			address.setCity(readTextContext((Element) xPath.compile("./city[not(@nullFlavor)]").
	    				evaluate(addrElement, XPathConstants.NODE)));
			
			address.setState(readTextContext((Element) xPath.compile("./state[not(@nullFlavor)]").
	    				evaluate(addrElement, XPathConstants.NODE)));
			
			address.setPostalCode(readTextContext((Element) xPath.compile("./postalCode[not(@nullFlavor)]").
	    				evaluate(addrElement, XPathConstants.NODE)));
			
			address.setCountry(readTextContext((Element) xPath.compile("./country[not(@nullFlavor)]").
	    				evaluate(addrElement, XPathConstants.NODE)));
		}
		return address;
	}
	
	public static String nodeToString(Node node)
			throws TransformerException
	{
		StringWriter buf = new StringWriter();
		Transformer xform = TransformerFactory.newInstance().newTransformer();
	    xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	    xform.transform(new DOMSource(node), new StreamResult(buf));
	    return(buf.toString());
	}
	
	public static String readLineNuberString(Node node)
			throws TransformerException
	{
		StringWriter buf = new StringWriter();
		Transformer xform = TransformerFactory.newInstance().newTransformer();
	    xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	    xform.transform(new DOMSource(node), new StreamResult(buf));
	    return(buf.toString());
	}
	
	public static boolean checkForNullFlavourNI(Element element)
	{
		return element!=null && element.getAttribute("nullFlavor").equalsIgnoreCase("NI");
	}
}
