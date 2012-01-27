package sards;

import javax.xml.ws.Endpoint;

public class SARDSPublisher {
	public static void main( String[] args ) {
		if ( args.length < 1 ) {
			System.err.println( "usage: SARDSPublisher ENDPOINT_ADDRESS" );
		} else {
			Endpoint.publish( args[ 0 ], new SARDS() );
		}
	}
}
