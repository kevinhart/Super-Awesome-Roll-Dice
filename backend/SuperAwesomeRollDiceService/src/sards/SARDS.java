package sards;

import javax.annotation.Resource;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
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
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringBufferInputStream;
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
    
    private static final String DB_DIR_NAME = "CHARACTERDB";   
    private static final String XSD_FILE_NAME = "character.xsd";
    
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
    	             //.replace( "\'", "\\\'" )
    	             .replace( "\t", "" )
    	             .replace( "\n", "" );
    }
    
    private String toCharacterSheetXml( String raw ) {
    	try {
	    	StringTokenizer st = new StringTokenizer( raw, ":" );
	    	StringBuilder xmlDoc = new StringBuilder();
	    	xmlDoc.append( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><character>" );
	    	xmlDoc.append( "<name>" + st.nextToken() + "</name>" );
	    	xmlDoc.append( "<player>" + st.nextToken() + "</player>" );	
	    	xmlDoc.append( "<age>" + st.nextToken() + "</age>" );
	    	xmlDoc.append( "<race>" + st.nextToken() + "</race>" );
	    	xmlDoc.append( "<class>" + st.nextToken() + "</class>" );
	    	xmlDoc.append( "<gender>" + st.nextToken() + "</gender>" );
	    	xmlDoc.append( "<level>" + st.nextToken() + "</level>" );
	    	xmlDoc.append( "<experience>" + st.nextToken() + "</experience>" );
	    	xmlDoc.append( "<hitpoints>" + st.nextToken() + "</hitpoints>" );
	    	xmlDoc.append( "<baseAttackBonus>" + st.nextToken() + "</baseAttackBonus>" );
	    	xmlDoc.append( "<baseDamageBonus>" + st.nextToken() + "</baseDamageBonus>" );
	    	xmlDoc.append( "<attacks>" + st.nextToken() + "</attacks>" );
	    	xmlDoc.append( "<attributes><strength>" + st.nextToken() + "</strength>" );
	    	xmlDoc.append( "<dexterity>" + st.nextToken() + "</dexterity>" );
	    	xmlDoc.append( "<mind>" + st.nextToken() + "</mind>" );
	    	xmlDoc.append( "<charisma>" + st.nextToken() + "</charisma></attributes>" );
	    	xmlDoc.append( "<skills><physical>" + st.nextToken() + "</physical>" );
	    	xmlDoc.append( "<subterfuge>" + st.nextToken() + "</subterfuge>" );
	    	xmlDoc.append( "<knowledge>" + st.nextToken() + "</knowledge>" );
	    	xmlDoc.append( "<communication>" + st.nextToken() + "</communication>" );
	    	xmlDoc.append( "<survival>" + st.nextToken() + "</survival>" );
	    	xmlDoc.append( "<fabrication>" + st.nextToken() + "</fabrication></skills>" );
	    	xmlDoc.append( "<inventory>" );
	    	
	    	int inputVal = Integer.parseInt( st.nextToken() );
	    	switch ( inputVal ) {
				case 1:
					xmlDoc.append( "<armour><name>Padded</name><minStr>0</minStr><acBonus>1</acBonus></armour>" );
					break;
				case 2:
					xmlDoc.append( "<armour><name>Leather</name><minStr>6</minStr><acBonus>2</acBonus></armour>" );
					break;
				case 3:
					xmlDoc.append( "<armour><name>Studded Leather</name><minStr>8</minStr><acBonus>3</acBonus></armour>" );
					break;
				case 4:
					xmlDoc.append( "<armour><name>Scale Mail</name><minStr>10</minStr><acBonus>4</acBonus></armour>" );
					break;
				case 5:
					xmlDoc.append( "<armour><name>Splint Mail</name><minStr>12</minStr><acBonus>5</acBonus></armour>" );
					break;
				case 6:
					xmlDoc.append( "<armour><name>Chain Mail</name><minStr>12</minStr><acBonus>5</acBonus></armour>" );
					break;
				case 7:
					xmlDoc.append( "<armour><name>Banded</name><minStr>14</minStr><acBonus>6</acBonus></armour>" );
					break;
				case 8:
					xmlDoc.append( "<armour><name>Half Plate</name><minStr>14</minStr><acBonus>7</acBonus></armour>" );
					break;
				case 9:
					xmlDoc.append( "<armour><name>Full Plate</name><minStr>15</minStr><acBonus>8</acBonus></armour>" );
					break;
				default:
					xmlDoc.append( "<armour><name>Padded</name><minStr>0</minStr><acBonus>1</acBonus></armour>" );
					break;
	    	}
	    
	    	inputVal = Integer.parseInt( st.nextToken() );
	    	switch ( inputVal ) {
				case 1:
					xmlDoc.append( "<shield><name>Small Steel</name><dexPenalty>-1</dexPenalty><acBonus>+1</acBonus></shield>" );
					break;
				case 2:
					xmlDoc.append( "<shield><name>Small Wooden</name><dexPenalty>-2</dexPenalty><acBonus>+1</acBonus></shield>" );
					break;
				case 3:
					xmlDoc.append( "<shield><name>Large</name><dexPenalty>-2</dexPenalty><acBonus>2</acBonus></shield>" );
					break;
				case 4:
					xmlDoc.append( "<shield><name>Tower</name><dexPenalty>-3</dexPenalty><acBonus>+3</acBonus></shield>" );
					break;
				default:
					xmlDoc.append( "<shield><name>No Shield</name><dexPenalty>0</dexPenalty><acBonus>0</acBonus></shield>" );
					break;
	    	}
	    	
	    	while ( st.hasMoreTokens() ) {
	    		inputVal = Integer.parseInt( st.nextToken() );
	    		switch ( inputVal ) {
					case 1:				
						xmlDoc.append( "<weapon twohanded=\"no\"><name>Dagger</name><stat>Str</stat><damage>light</damage><range>-</range></weapon>" );
						xmlDoc.append( "<weapon twohanded=\"no\"><name>Thrown Dagger</name><stat>Dex</stat><damage>light</damage><range>5</range></weapon>" );
						break;
					case 2:
						xmlDoc.append( "<weapon twohanded=\"no\"><name>Club</name><stat>Str</stat><damage>light</damage><range>-</range></weapon>" );
						break;
					case 3:
						xmlDoc.append( "<weapon twohanded=\"no\"><name>Light Hammer</name><stat>Str</stat><damage>light</damage><range>-</range></weapon>" );
						xmlDoc.append( "<weapon twohanded=\"no\"><name>Thrown Light Hammer</name><stat>Dex</stat><damage>light</damage><range>20</range></weapon>" );
						break;
					case 4:
						xmlDoc.append( "<weapon twohanded=\"no\"><name>Short Sword</name><stat>Str</stat><damage>light</damage><range>-</range></weapon>" );
						break;
					case 5:
						xmlDoc.append( "<weapon twohanded=\"yes\"><name>Spear</name><stat>Str</stat><damage>light</damage><range>-</range></weapon>" );
						xmlDoc.append( "<weapon twohanded=\"yes\"><name>Thrown Spear</name><stat>Dex</stat><damage>light</damage><range>20</range></weapon>" );
						break;
					case 6:
						xmlDoc.append( "<weapon twohanded=\"yes\"><name>Light Crossbow</name><stat>Dex</stat><damage>light</damage><range>80</range></weapon>" );
						break;
					case 7:
						xmlDoc.append( "<weapon twohanded=\"yes\"><name>Short Bow</name><stat>Dex</stat><damage>light</damage><range>60</range></weapon>" );
						break;
					case 8:
						xmlDoc.append( "<weapon twohanded=\"no\"><name>Long Sword</name><stat>Str</stat><damage>heavy</damage><range>-</range></weapon>" );
						break;
					case 9:
						xmlDoc.append( "<weapon twohanded=\"no\"><name>Battle Axe</name><stat>Str</stat><damage>heavy</damage><range>-</range></weapon>" );
						break;
					case 10:
						xmlDoc.append( "<weapon twohanded=\"no\"><name>Flail</name><stat>Str</stat><damage>heavy</damage><range>-</range></weapon>" );
						break;
					case 11:
						xmlDoc.append( "<weapon twohanded=\"yes\"><name>Two-handed Sword</name><stat>Str</stat><damage>heavy</damage><range>-</range></weapon>" );
						break;
					case 12:
						xmlDoc.append( "<weapon twohanded=\"yes\"><name>War Axe</name><stat>Str</stat><damage>heavy</damage><range>-</range></weapon>" );
						break;
					case 13:
						xmlDoc.append( "<weapon twohanded=\"yes\"><name>Heavy Crossbow</name><stat>Dex</stat><damage>heavy</damage><range>120</range></weapon>" );
						break;
					case 14:
						xmlDoc.append( "<weapon twohanded=\"yes\"><name>Long Bow</name><stat>Dex</stat><damage>heavy</damage><range>100</range></weapon>" );
						break;
					case 15:
						xmlDoc.append( "<item><name>Torch</name><descrip>Provides light for 2 hours</descrip></item>" );
						break;
					case 16:				
						xmlDoc.append( "<item><name>Waterskin</name><descrip>Holds enough water for 1 day</descrip></item>" );
						break;
					case 17:
						xmlDoc.append( "<item><name>Crowbar</name><descrip>Gives the hero a +2 bonus to disarming traps and prying open doors</descrip></item>" );
						break;
					case 18:
						xmlDoc.append( "<item><name>Holy Symbol</name><descrip>Allows the hero to channel his god's power</descrip></item>" );
						break;
					case 19:
						xmlDoc.append( "<item><name>Rations</name><descrip>Dried meat, fruit and cheese</descrip></item>" );
						break;
					case 20:
						xmlDoc.append( "<item><name>McGuffin</name><descrip>Allows hero to bypass one plot hook</descrip></item>" );
						break;
					case 21:
						xmlDoc.append( "<item><name>Chalk</name><descrip>Stick of chalk for marking</descrip></item>" );
						break;
					case 22:
						xmlDoc.append( "<item><name>Pitons</name><descrip>Useful for climbing or jamming open doors</descrip></item>" );
						break;
					case 23:
						xmlDoc.append( "<item><name>Hammer</name><descrip>Useful for hitting non-enemy objects</descrip></item>" );
						break;
					case 24:
						xmlDoc.append( "<item><name>Disguise Kit</name><descrip>Gives the hero +2 to +10 on subterfuge checks to disguise self</descrip></item>" );
					default:
						break;
	    		}
	    	}
	    	
	    	xmlDoc.append( "</inventory></character>" );
	    	return xmlDoc.toString();
    	} catch ( Exception e ) { return null; }
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
		xml = toCharacterSheetXml( xml );
		if ( xml == null ) {
			return "{\"r\":5,\"t\":\"[SaveSheet] Could not parse character sheet data.\"}";
		}
		
		// validate xml
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			@SuppressWarnings( "deprecation" )
			Document document = parser.parse( new StringBufferInputStream( xml ) );
			// create a SchemaFactory capable of understanding WXS schemas
			SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );

			// load a WXS schema, represented by a Schema instance
			Source schemaFile = new StreamSource( new File( XSD_FILE_NAME ) );
			Schema schema = factory.newSchema( schemaFile );			
			Validator validator = schema.newValidator();

			// validate the DOM tree				
			validator.validate(new DOMSource(document));
		} catch ( SAXException e ) {
			try {
				FileWriter f = new FileWriter( "CHARACTERTEST.xml" );
				f.write( xml );
				f.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return "{\"r\":6,\"t\":\"[SaveSheet] Character sheet invalid against schema.\"}";
		} catch ( Exception e ) {
			e.printStackTrace();
			return "{\"r\":7,\"t\":\"[SaveSheet] Error occurred when validating character sheet.\",\"d\":\"" + e.toString() + "\"}";
		}
		
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
		// make sure that character exists for the user
		File file = new File( DB_DIR_NAME + "/" + username+ "/" +characterName + ".xml" );
		if ( !file.exists() ) {
			return "{\"r\":1,\"t\":\"[viewSheets] Character does not exist for given user.\"}";
		}
			
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		String theResult = null;
    	try {    		
    		DocumentBuilder builder = factory.newDocumentBuilder();
    		doc = builder.parse( DB_DIR_NAME + "/" + username+ "/" +characterName + ".xml" );
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
    		e.printStackTrace();
    		return "{\"r\":1,\"t\":\"[viewSheets] Failure.\",\"d\":\"" + toSafeXml( e.toString() ) + "\"}"; 
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