package org.sitenv.ccdaparsing.model;

import java.util.ArrayList;

public class CCDAProcedure extends CCDAXmlSnippet{
	
	private boolean isSectionNullFlavourWithNI;
	private ArrayList<CCDAII>       		sectionTemplateId;
	private CCDACode                 		sectionCode;
	private ArrayList<CCDAProcActProc>		procActsProcs;
	private ArrayList<String> referenceLinks;
	
	
	
	public CCDAProcedure()
	{
		
	}
	
	public boolean isSectionNullFlavourWithNI() {
		return isSectionNullFlavourWithNI;
	}

	public void setSectionNullFlavourWithNI(boolean isSectionNullFlavourWithNI) {
		this.isSectionNullFlavourWithNI = isSectionNullFlavourWithNI;
	}

	public ArrayList<CCDAII> getSectionTemplateId() {
		return sectionTemplateId;
	}

	public void setSectionTemplateId(ArrayList<CCDAII> sectionTemplateId) {
		this.sectionTemplateId = sectionTemplateId;
	}

	public CCDACode getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(CCDACode sectionCode) {
		this.sectionCode = sectionCode;
	}

	public ArrayList<CCDAProcActProc> getProcActsProcs() {
		return procActsProcs;
	}

	public void setProcActsProcs(ArrayList<CCDAProcActProc> procActsProcs) {
		this.procActsProcs = procActsProcs;
	}
	
	public ArrayList<String> getReferenceLinks() {
		if(referenceLinks == null)
		{
			this.referenceLinks = new ArrayList<String>();
		}
		return referenceLinks;
	}

	public void setReferenceLinks(ArrayList<String> referenceLinks) {
		this.referenceLinks = referenceLinks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((procActsProcs == null) ? 0 : procActsProcs.hashCode());
		result = prime * result
				+ ((sectionCode == null) ? 0 : sectionCode.hashCode());
		result = prime
				* result
				+ ((sectionTemplateId == null) ? 0 : sectionTemplateId
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CCDAProcedure other = (CCDAProcedure) obj;
		if (procActsProcs == null) {
			if (other.procActsProcs != null)
				return false;
		} else if (!procActsProcs.equals(other.procActsProcs))
			return false;
		if (sectionCode == null) {
			if (other.sectionCode != null)
				return false;
		} else if (!sectionCode.equals(other.sectionCode))
			return false;
		if (sectionTemplateId == null) {
			if (other.sectionTemplateId != null)
				return false;
		} else if (!sectionTemplateId.equals(other.sectionTemplateId))
			return false;
		return true;
	}
	
	
	
}
