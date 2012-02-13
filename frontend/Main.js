/**
 * @author John Deer
 * @contributor Kevin Hart is gay
 */
 
 var userName = "";
 var wsAddress = "http://saskatoon.cs.rit.edu:4241/sards"
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
		
		userName = login;
		
	}
}

function loadSheets( user ){
	document.getElementById("rightSide").innerHTML = "<table id='sheetTable' class='sheetTable' ><tr><td><text class='label'>Character Name</text></td><td /><td /></tr></table>";

	populateTable(user);
}

function populateTable(user){
	var arrayOfSheetNames = getSheetNames(user);
	
	var table = document.getElementById("sheetTable");
	
	for(name in arrayOfSheetNames){
		var row = table.insertRow(table.rows.length);
		var cell = row.insertCell(0);
		cell.innerHTML = "<text class='view'>"+arrayOfSheetNames[name].slice(0, arrayOfSheetNames[name].indexOf("."))+"</text>";
		cell = row.insertCell(1);
		cell.innerHTML = "<button class='view' onclick='viewSheet(/*characterName, userName, 0*/)'>View</button>";
		cell = row.insertCell(2);
		cell.innerHTML = "<button class='view' onclick='viewSheet(/*characterName, userName, 1*/)'>Edit</button>";
	}	
}

function getSheetNames(userName){
	var names = "Torik.xml"//call the database
	/*  Call ViewSheets
		$.ajax( {
		async: false,
		data: { "path" : encodeURI( url ) },
		url: proxy,
		success: function( data, textStatus, jqxhr ) { loginResult( data ); },
		error: function( jqhxr, status, errorThrown ) { alert( "An error occurred connecting to the login server." ); },
		dataType: "json"
	} );*/
	var nameArray = names.split(",");
	return nameArray;
}

function viewSheetCallback( data ){
	if(data['r'] == 0){
		document.getElementById("rightSide").innerHTML = data['d'];
	}else{
		alert(data['t']);
	}
}

function viewSheetError( ){
	alert("Couldn't connect to server! Oh Noes!");
}

function viewSheet(cName, user, mode){
	if(mode == 0){
		var url = wsAddress + "?action=viewSheet";
		$.ajax( {
			async: false,
			data: { "path" : encodeURI( url ) },
			url: proxy,
			success: function( data, textStatus, jqxhr ) { viewSheetCallback( data ); },
			error: function( jqhxr, status, errorThrown ) { viewSheetError() },
			dataType: "json"
		} );
	}else if(mode == 1){//create new
		var url = wsAddress + "?action=createNew";
		$.ajax( {
			async: false,
			data: { "path" : encodeURI( url ) },
			url: proxy,
			success: function( data, textStatus, jqxhr ) { viewSheetCallback( data ); },
			error: function( jqhxr, status, errorThrown ) { viewSheetError() },
			dataType: "json"
		} );
	}else if(mode == 2){//edit sheet
	
	}
}

function saveSheetSuccess( data ){
	if(data['r'] == 0){
		alert("Your character has been saved successfully!");
	}else{
		alert(data['t']);
	}
}

function saveSheet(user, password, xmldoc, cName){
	var url = wsAddress + "?action=saveSheet&username="+BLAH+"&password="+BLAH+"&cName="+BLAH+"&xml="+escape( xmlString );
	$.ajax( {
		async: false,
		data: { "path" : encodeURI( url ) },
		url: proxy,
		success: function( data, textStatus, jqxhr ) { saveSheetSuccess(data) },
		error: function( jqhxr, status, errorThrown ) { /*Update user with failure*/ },
		dataType: "json"
	} );
}

