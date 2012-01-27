/**
 * @author John Deer
 * @contributor Kevin Hart
 */
 
 var userName = "";
 var wsAddress = "http://kubrick.student.rit.edu:8080/SARDS"
 var proxy = "proxy.php";
 
 function login()
{
	var login = document.getElementById("login").value;
	var password = document.getElementById("password").value;
	handleLogin(login, SHA1(password));
}

function handleLogin( user, pass ) {
	var url = wsAddress + "?action=login&username=" + user + "&password=" + pass;
	$.ajax( {
		async: false,
		data: { "path" : encodeURI( url ) },
		url: proxy,
		success: function( data, textStatus, jqxhr ) { loginResult( data ); },
		error: function( jqhxr, status, errorThrown ) { alert( "An error occurred connecting to the login server." ); },
		dataType: "json"
	} );
}

function loginResult( result ) {
	if ( result[ "r" ] != 0 ) {
		alert( result[ "t" ] );
	} else {
		alert( result[ "t" ] );
		document.getElementById("login").style.visibility = 'hidden';
		document.getElementById("password").style.visibility = 'hidden';
		document.getElementById("loginText").style.visibility = 'hidden';
		document.getElementById("passwordText").style.visibility = 'hidden';
		document.getElementById("loginButton").style.visibility = 'hidden';
		
		userName = login;
		
		document.getElementById("viewButton").style.visibility = 'visible';
		document.getElementById("createButton").style.visibility = 'visible';
	}
}

function loadSheets(){
	document.all.innerPage.src="viewSheets.html"; 
}

function populateTable(){
	var arrayOfSheetNames = getSheetNames(userName);
	
	var table = document.getElementById("sheetTable");
	
	for(name in arrayOfSheetNames){
		var row = table.insertRow(table.rows.length);
		var cell = row.insertCell(0);
		//cell.innerHTML = "<a href='http://129.21.141.16/"+arrayOfSheetNames[name]+"' >"+arrayOfSheetNames[name].slice(0, arrayOfSheetNames[name].indexOf("."))+"</a>";
		cell.innerHTML = arrayOfSheetNames[name].slice(0, arrayOfSheetNames[name].indexOf("."));
		cell = row.insertCell(1);
		cell.innerHTML = "<a href='http://129.21.141.16/"+arrayOfSheetNames[name]+"' >View</a>";
		cell = row.insertCell(2);
		cell.innerHTML = "<button onclick='editSheet'>Edit</button>";
	}	
}

function getSheetNames(userName){
	var names = "xml.xml, xml1.xml, xml2.xml"//call the database
	var nameArray = names.split(",");
	return nameArray;
}

function updateButtons(){
	if( document.getElementById("innerPage").src.slice(document.getElementById("innerPage").src.lastIndexOf("."), document.getElementById("innerPage").src.length - 1) == "xml"){
		document.getElementById("createButton").style.visibility = 'hidden';		
		document.getElementById("editButton").style.visibility = 'visible';
	}else{		
		document.getElementById("editButton").style.visibility = 'hidden';
	}
	
}