package sards;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

@WebServiceProvider
@BindingType( value=HTTPBinding.HTTP_BINDING )
public class SARDS implements Provider< Source > {

    @Resource( type=Object.class )
    protected WebServiceContext wsContext;

    private HashMap< String, String > validUsers;
    
    private final String DB_DIR_NAME = "CHARACTERDB";   
    
    public SARDS() {
    	validUsers = new HashMap< String, String >();
    	
    	// ensure character db directory exists
    	File charDir = new File( DB_DIR_NAME );
		if ( !charDir.exists() ) charDir.mkdir();
    	
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
    	
    	indexCharacters(); // too often?
    	
        StringTokenizer st = new StringTokenizer( str, "=&" );
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
        } else if ( action.equals( "createNew" ) ) {
        	body = CreateNew();
        } else if ( action.equals( "saveSheet" ) ) {
        	body = SaveSheet( args );
        } else if ( action.equals( "viewSheets" ) ) {
        	boolean usernameGiven = args.containsKey( "username" );
        	boolean cNameGiven = args.containsKey( "cName" );
        	boolean isForEdit = args.containsKey( "forEdit" ) && args.get( "forEdit" ).equals( "yes" );
        	
        	if ( !usernameGiven ) {
        		body = viewSheets();
        	} else {
        		if ( cNameGiven ) {
        			body = viewSheets( args.get( "username" ), args.get( "cName" ), isForEdit );
        		} else {
        			body = viewSheets( args.get( "username" ) );
        		}
        	}
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
    
    private void indexCharacters() {    	
    	StringBuilder index = new StringBuilder();
		index.append( "<database>" );
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String[] indexableTags = { "strength", "dexterity", "mind", "charisma", "physical", "subterfuge", "knowledge", "communication", "survival", "fabrication" };
		
    	File dir = new File( DB_DIR_NAME );
		String[] userNames = dir.list();		
		for( int i = 0; i < userNames.length; i++ ) {
			index.append( "<user name=\"" + userNames[ i ] + "\">" );
			File dir2 = new File( DB_DIR_NAME + "/" + userNames[ i ] );
			String[] characters = dir2.list();
			for ( int j = 0; j < characters.length; j++ ) {
				
		    	try {
		    		StringBuilder character = new StringBuilder();
		    		character.append( "<character name=\"" + characters[ j ].replace( ".xml", "" ) + "\">" );
		    		DocumentBuilder builder = factory.newDocumentBuilder();
		    		Document doc = builder.parse( DB_DIR_NAME + "/" + userNames[ i ] + "/" + characters[ j ] );
		    		
		    		for ( int n = 0; n < indexableTags.length; n++ ) {
		    			String tName = indexableTags[ n ];
		    			NodeList tags = doc.getElementsByTagName( tName );
		    			if ( tags.getLength() > 0 ) {
		    				character.append( "<" + tName + ">" );
		    				character.append( tags.item( 0 ).getTextContent() );
		    				character.append( "</" + tName + ">" );
		    			}
		    		}
		    		
		    		character.append( "</character>" );
		    		index.append( character.toString() );
		    	} catch ( Exception e ) {
		    		// pass
		    	}
			}
			index.append( "</user>" );
		}
		
		index.append( "</database>" );
		
		try {
			BufferedWriter fout = new BufferedWriter( new FileWriter( "database.xml", false ) );
			fout.write( index.toString() );
			fout.close();
		} catch ( Exception e ) {
			// pass
		}
    }
    
    private String toSafeXml( String rawXml ) {
    	if ( rawXml == null ) return rawXml;
    	return rawXml.replace( "<", "&lt;" )
    	             .replace( ">", "&gt;" )
    	             .replace( "\"", "\\\"" )
    	             .replace( "\'", "\\\'" )
    	             .replace( "\t", "" )
    	             .replace( "\n", "" );
    }
    
    private String Login( HashMap< String, String > args ) {
    	if ( !( args.containsKey( "username" ) && args.containsKey( "password" ) ) ){
    		return "{\"r\":1,\"t\":\"[Login] Username and/or password not specified.\"}";
    	}
    	
    	String user = args.get( "username" );
    	String pass = args.get( "password" );
    	
    	if ( validUsers.containsKey( user ) && validUsers.get( user ).equals( pass ) ) {
    		return "{\"r\":0,\"t\":\"[Login] Successful.\"}";
    	} else {
    		return "{\"r\":2,\"t\":\"[Login] Invalid username or password.\"}";
    	}
    }
    
    private String SaveSheet( HashMap< String, String > args ) {
    	if ( !( args.containsKey( "username" ) &&
    			args.containsKey( "password" ) &&
    			args.containsKey( "xml" ) &&
    			args.containsKey( "cName" ) ) ) {
    		return "{\"r\":3,\"t\":\"[SaveSheet] Insufficient arguments.\"}";
    	}
    	
    	String user = args.get( "username" );
    	String pass = args.get( "password" );
		String xml = args.get( "xml" );
    	String cName = args.get( "cName" );
    	
    	// ensure character db directory exists
    	File charDir = new File( DB_DIR_NAME );
		if ( !charDir.exists() ) charDir.mkdir();
    	
    	if ( validUsers.containsKey( user ) && validUsers.get( user ).equals( pass ) ) {
    		File userDir = new File( DB_DIR_NAME + "/" + user );
    		if ( !userDir.exists() ) userDir.mkdir();
    		String outLoc = DB_DIR_NAME + "/" + user + "/" + cName + ".xml";
    		BufferedWriter fout = null;
    		
    		try {
				fout = new BufferedWriter( new FileWriter( outLoc, false ) );
				fout.write( xml );
				fout.close();
			} catch ( IOException e ) {
				e.printStackTrace();
				return "{\"r\":2,\"t\":\"[SaveSheet] Error while writing to output file.\",\"d\":\"" + e.toString() + "\"}";
			}
			
			return "{\"r\":0,\"t\":\"[SaveSheet] Success.\"}";
    	}
    	
    	return "{\"r\":1,\"t\":\"[SaveSheet] Invalid password for user \\\"" + user + "\\\".\"}";
    }
    
    private String CreateNew() {
    	Scanner fin = null;
    	try {
			 fin = new Scanner( new FileInputStream( "template.xml" ) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "{\"r\":1,\"t\":\"[CreateNew] Template file (template.xml) could not be found.\"}";
		}
		
		StringBuilder fileContents = new StringBuilder();
		try {
			while ( fin.hasNextLine() ) {
				fileContents.append( fin.nextLine() );
			}
		} finally {
			fin.close();
		}
		
		String fileText = fileContents.toString();
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = null;
		
		try {
			trans = tf.newTransformer( new StreamSource( "sampleCharacterSheetEdit.xsl" ) );
			trans.setOutputProperty( OutputKeys.METHOD, "xml" );
			trans.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, "yes" );
			StringWriter writer = new StringWriter();
			StreamResult output = new StreamResult( writer );
			trans.transform( new StreamSource( new ByteArrayInputStream( fileText.getBytes() ) ), output );
			if ( output != null ) {
				return "{\"r\":0,\"t\":\"[CreateNew] Success.\",\"d\":\"" + toSafeXml( writer.toString() ) + "\"}";
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}		
		
		return "{\"r\":4,\"t\":\"[CreateNew] Could not transform with XSLT.\"}";
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
    
    
    
    
    /**
     * viewSheets Methods
     */
 	public String viewSheets(){
		File dir = new File(DB_DIR_NAME);
		String[] userNames = dir.list();
		//for every username
		// get a list of characters 
		StringBuilder sheet = new StringBuilder();
		sheet.append("{");
		for(int i=0; i<userNames.length; i++){
			StringBuilder tmp = new StringBuilder();
			File dir2 = new File( DB_DIR_NAME + "/" + userNames[i]);
			String[] characters = dir2.list();
			tmp.append("[");
			for( int j=0; j<characters.length; j++){
				tmp.append("\"" + characters[j].substring(0, characters[j].length()-4) + "\"");
				if( j != characters.length-1){
				tmp.append(", ");
				}
			}
			tmp.append("]");
			String theSheet = tmp.toString();
			sheet.append("\""+userNames[i]+"\": " + theSheet);
			if(i!=userNames.length-1){
			sheet.append(", ");
			}
		}
		sheet.append("}");
		String result = sheet.toString();
		return "{\"r\":0,\"t\":\"[viewSheets] Success.\",\"d\":" + result + "}";
		
	}
	
	public String viewSheets( String username ){
		StringBuilder sheet = new StringBuilder();
		File dir = new File( DB_DIR_NAME + "/" + username );
		if ( dir.exists() ) {
			String[] characters = dir.list();
			sheet.append("[");
			for( int i=0; i<characters.length; i++){
				sheet.append("\"" + characters[i].substring(0, characters[i].length()-4) + "\"");
				if( i != characters.length-1)
				sheet.append(", ");
			}
			sheet.append("]");
			String theSheet = sheet.toString();
			return "{\"r\":0,\"t\":\"[viewSheets] Success.\",\"d\":" + theSheet + "}";
		} else {
			return "{\"r\":1,\"t\":\"[viewSheets] Username does not exist.\"}";
		}		 
	}
	
		
	public String viewSheets( String username, String characterName, boolean isEdit ){
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	Document doc = null;
	String theResult = null;
    	try {    		
    		DocumentBuilder builder = factory.newDocumentBuilder();
    		doc = builder.parse( DB_DIR_NAME + "/" + username+ "/" +characterName + ".xml");
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			StreamSource xslt = isEdit ? new StreamSource( "sampleCharacterSheetEdit.xsl" ) : new StreamSource( "sampleCharacterSheetRender.xsl" );
			Transformer transformer = tf.newTransformer( xslt );
			transformer.setOutputProperty( OutputKeys.METHOD, "xml" );
			transformer.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, "yes" );
			transformer.transform(domSource, result);
			theResult = writer.toString();
    	} catch ( Exception e ) {
    		// pass
    	}
		return "{\"r\":0,\"t\":\"[viewSheets] Success.\",\"d\":\"" + toSafeXml( theResult ) + "\"}"; 
	}
	
	/**
	 * End viewShets
	 */
 
 
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