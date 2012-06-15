package org.knoesis.sparql.api;

public class Triple {

	private String subject;
	private String predicate;
	private String object;
	
	public Triple(String subject,String predicate,String object){
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		
		//System.out.println(subject+"--"+predicate+"--"+object);
		
	}

	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getObject() {
		return object;
	}
	
	public String toString(){
		return subject + " " + predicate + " " + object + " .";
	}
	
}
