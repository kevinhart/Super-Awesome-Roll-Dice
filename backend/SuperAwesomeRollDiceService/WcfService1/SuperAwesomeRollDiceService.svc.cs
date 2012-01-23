using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using System.Xml.Linq;
using System.Xml;
using System.IO;

namespace SuperAwesomeRollDiceService {
	public class LoginCredential {
		public string username { get; set; }
		public string hashedPass { get; set; }
	}

	public class SuperAwesomeRollDiceService : ISuperAwesomeRollDiceService {
		private static List< LoginCredential > cachedCredentials;

		public SuperAwesomeRollDiceService() {
			CacheLoginCredentials();
		}

		private void CacheLoginCredentials() {
			// hard-code "loginCredentials.xml" for now
			cachedCredentials = new List< LoginCredential >();
			XDocument credentialsDoc = XDocument.Load( XmlReader.Create( @"C:\Users\Kevin\Desktop\loginCredentials.xml" ) );
			foreach ( XElement validUser in credentialsDoc.Descendants( "validUser" ) ) {
				string username = validUser.Element( "username" ).Value;
				string password = validUser.Element( "password" ).Value;
				if ( username != null && password != null ) cachedCredentials.Add( new LoginCredential { username = username, hashedPass = password } );
			}
		}

		public int Login( string username, string hashedPass ) {
			if ( username == null || hashedPass == null ) return -1;

			foreach ( LoginCredential login in cachedCredentials ) {
				if ( username.Equals( login.username ) && hashedPass.Equals( login.hashedPass ) ) return 1;
			}

			return 0;
		}
	}
}
