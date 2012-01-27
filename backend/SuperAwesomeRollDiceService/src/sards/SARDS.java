package sards;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.StringTokenizer;

@WebServiceProvider
@BindingType( value=HTTPBinding.HTTP_BINDING )
public class SARDS implements Provider< Source > {

    @Resource( type=Object.class )
    protected WebServiceContext wsContext;

    private HashMap< String, String > validUsers;
    
    public SARDS() {
    	validUsers = new HashMap< String, String >();
    	parseLoginCredentials();
    }
    
    public Source invoke( Source source ) {
        try {
            MessageContext mc = wsContext.getMessageContext();
            String query = ( String )mc.get( MessageContext.QUERY_STRING );
            String path = ( String )mc.get( MessageContext.PATH_INFO );
            System.out.println( "Query String = " + query );
            System.out.println( "PathInfo = " + path );
            if ( query != null && query.contains( "action=" ) ) {
                return createSource( query );
            } else {
                throw new HTTPException( 404 );
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new HTTPException( 500 );
        }
    }
    
    private Source createSource( String str ) {
        StringTokenizer st = new StringTokenizer( str, "=&/" );
        st.nextToken();
        String action = st.nextToken();
        HashMap< String, String > args = new HashMap< String, String >();
        while ( st.hasMoreTokens() ) {
        	String k = st.nextToken();
        	if ( !st.hasMoreTokens() ) break;
        	args.put( k, st.nextToken() );
        }
        
        String body = "";
        if ( action.equals( "login" ) ) {
        	body = Login( args );
        } else {        
        	body = null;
        }
        
        if ( body == null ) {
        	body = "<result />";
        } else {
        	body = "<result>" + body + "</result>";
        }
        
        Source source = new StreamSource( new ByteArrayInputStream( body.getBytes() ) );
        return source;
    }
    
    private String Login( HashMap< String, String > args ) {
    	if ( !( args.containsKey( "username" ) && args.containsKey( "password" ) ) ){
    		return "{\"r\":-1,\"t\":\"[Login] Username and/or password not specified.\"}";
    	}
    	
    	String user = args.get( "username" );
    	String pass = args.get( "password" );
    	
    	if ( validUsers.containsKey( user ) && validUsers.get( user ).equals( pass ) ) {
    		return "{\"r\":0,\"t\":\"[Login] Successful.\"}";
    	} else {
    		return "{\"r\":1,\"t\":\"[Login] Invalid username or password.\"}";
    	}
    }
    
    private void parseLoginCredentials() {
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	try {
    		DocumentBuilder builder = factory.newDocumentBuilder();
    		Document doc = builder.parse( "users.xml" );
    		NodeList userNodes = doc.getDocumentElement().getElementsByTagName( "validUser" );
    		if ( userNodes != null && userNodes.getLength() > 0 ) {
    			for ( int i = 0; i < userNodes.getLength(); i++ ) {
    				Element user = ( Element ) userNodes.item( i );
    				String username = user.getElementsByTagName( "username" ).item( 0 ).getTextContent();
    				String password = user.getElementsByTagName( "password" ).item( 0 ).getTextContent();
    				validUsers.put( username, password );
    			}
    		}
    	} catch ( Exception e ) {
    		// pass
    	}
    }
    
    public static String SHA1( String msg ) {
    	MessageDigest md = null;
    	
		try {
			md = MessageDigest.getInstance( "SHA-1" );
		} catch ( NoSuchAlgorithmException e ) {
			e.printStackTrace();
		}
		
		byte[] bytes = null;
		try {
			msg.getBytes( "UTF-8" );
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
		
    	return new String( md.digest( bytes ) );
    }
    
    public static String byteArrayToHexString( byte[] b ) throws Exception {
    	String result = "";
    	for ( int i=0; i < b.length; i++ ) {
    		result += Integer.toString( ( b[ i ] & 0xff ) + 0x100, 16 ).substring( 1 );
    	}
    	return result;
    }
}