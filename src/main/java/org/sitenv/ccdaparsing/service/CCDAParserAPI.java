package org.sitenv.ccdaparsing.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sitenv.ccdaparsing.model.CCDAID;
import org.sitenv.ccdaparsing.model.CCDARefModel;
import org.sitenv.ccdaparsing.processing.CareTeamMemberProcessor;
import org.sitenv.ccdaparsing.processing.EncounterDiagnosesProcessor;
import org.sitenv.ccdaparsing.processing.GoalsProcessor;
import org.sitenv.ccdaparsing.processing.HealthConcernsProcessor;
import org.sitenv.ccdaparsing.processing.ImmunizationProcessor;
import org.sitenv.ccdaparsing.processing.LaboratoryResultsProcessor;
import org.sitenv.ccdaparsing.processing.LaboratoryTestProcessor;
import org.sitenv.ccdaparsing.processing.MediactionAllergiesProcessor;
import org.sitenv.ccdaparsing.processing.MedicationProcessor;
import org.sitenv.ccdaparsing.processing.POTProcessor;
import org.sitenv.ccdaparsing.processing.PatientProcessor;
import org.sitenv.ccdaparsing.processing.ProblemProcessor;
import org.sitenv.ccdaparsing.processing.ProcedureProcessor;
import org.sitenv.ccdaparsing.processing.SmokingStatusProcessor;
import org.sitenv.ccdaparsing.processing.UDIProcessor;
import org.sitenv.ccdaparsing.processing.UsrhSubTypeProcessor;
import org.sitenv.ccdaparsing.processing.VitalSignProcessor;
import org.sitenv.ccdaparsing.util.PositionalXMLReader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class CCDAParserAPI {

	private static final Logger logger = Logger.getLogger(CCDAParserAPI.class);
	
	private static XPath xPath = XPathFactory.newInstance().newXPath();
	
	//private static String filePath = "C:/Projects/Dragon/CCDAParser/170.315_b1_toc_amb_ccd_r21_sample1_v1.xml";
	 
	public static CCDARefModel parseCCDA2_1(InputStream inputStream) {
		
		CCDARefModel refModel = new CCDARefModel();
		ArrayList<CCDAID> idList = new ArrayList<CCDAID>();
	    try {
			    
	    	logger.info("Parsing CCDA document");
			Document doc = PositionalXMLReader.readXML(inputStream);
			if(doc.getDocumentElement()!= null && doc.getDocumentElement().getChildNodes().getLength()>1)
			{
				refModel.setPatient(PatientProcessor.retrievePatientDetails(xPath, doc));
				refModel.setEncounter(EncounterDiagnosesProcessor.retrieveEncounterDetails(xPath, doc,idList));
				refModel.setProblem(ProblemProcessor.retrieveProblemDetails(xPath, doc,idList));
				refModel.setMedication(MedicationProcessor.retrieveMedicationDetails(xPath, doc,idList));
				refModel.setAllergy(MediactionAllergiesProcessor.retrieveAllergiesDetails(xPath, doc,idList));
				refModel.setSmokingStatus(SmokingStatusProcessor.retrieveSmokingStatusDetails(xPath, doc,idList));
				refModel.setLabTests(LaboratoryTestProcessor.retrieveLabTests(xPath, doc,idList));
				refModel.setLabResults(LaboratoryResultsProcessor.retrieveLabResults(xPath, doc,idList));
				refModel.setVitalSigns(VitalSignProcessor.retrieveVitalSigns(xPath, doc,idList));
				refModel.setProcedure(ProcedureProcessor.retrievePrcedureDetails(xPath, doc,idList));
				refModel.setMembers(CareTeamMemberProcessor.retrieveCTMDetails(xPath, doc));
				refModel.setImmunization(ImmunizationProcessor.retrieveImmunizationDetails(xPath, doc,idList));
				refModel.setUdi(UDIProcessor.retrieveUDIDetails(refModel.getProcedure()));
				refModel.setPlanOfTreatment(POTProcessor.retrievePOTDetails(xPath, doc,idList));
				refModel.setGoals(GoalsProcessor.retrieveGoalsDetails(xPath, doc));
				refModel.setHcs(HealthConcernsProcessor.retrieveHealthConcernDetails(xPath, doc));
				refModel.setUsrhSubType(UsrhSubTypeProcessor.retrieveUsrhSubTypeDetails(xPath, doc));
				refModel.setIdList(idList);
			}
			else
			{
				refModel.setEmpty(true);
			}
			
			logger.info("Parsing CCDA document completed");
		    }
	    	catch (FileNotFoundException fnfException) 
	    	{
	    		logger.error(fnfException);
			}
	    	catch(XPathExpressionException xpeException)
			{
	    		logger.error(xpeException);
			}
	    	catch (IOException ioException) 
	    	{
	    		logger.error(ioException);
			}
	    	catch (SAXException saxException) 
	    	{
	    		logger.info("Parsing CCDA document failed");
	    		logger.error(saxException);
	    		refModel.setEmpty(true);
			}
	    	catch (NullPointerException npException) 
	    	{
	    		logger.error(npException);
			}
	        catch(TransformerException te)
	    	{
	            logger.error(te);
	    	}
	    
	    return refModel;
	
	}
}
