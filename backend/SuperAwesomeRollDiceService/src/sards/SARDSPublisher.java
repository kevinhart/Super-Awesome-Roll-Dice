package sards;

import javax.xml.ws.Endpoint;

public class SARDSPublisher {
	public static void main( String[] args ) {
		Endpoint.publish( "http://kubrick.student.rit.edu:8080/SARDS", new SARDS() );
	}
}
