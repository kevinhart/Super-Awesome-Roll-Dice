/**
 * @author John Deer
 * @contributor Kevin Hart is gay
 */
 
 var userName = "";
 var wsAddress = "http://saskatoon.cs.rit.edu:4241/sards"
 var proxy = "proxy.php";
 /*
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
*/
/*called by View Sheets ... buttons.  If called with the user input filled in, it will get the users
**only for that user
*/
function loadSheets( user ){
	
	if(user == ""){
		getSheetNames();	
	}else{
		getSheetNamesUser(user);
	}
}

/*
**Callback for eventual service call for View Sheets.. buttons
**This callback is used for calls that return associative arrays
*/
function populateTableMultiUsers( data ){
	var users;
	if(data['r'] == 0){
		users = data['d'];
	}else{
		alert(data['t']);
		return;
	}
	document.getElementById("rightSide").innerHTML = "<table id='sheetTable' class='sheetTable' ><tr><td><text class='label'>Character Name</text></td><td><text class='label'>User Name</text></td><td /><td /></tr></table>";
	var table = document.getElementById("sheetTable");
	
	for(name in users){
		for(var i = 0; i < users[user].length;i++){
			var row = table.insertRow(table.rows.length);
			var cell = row.insertCell(0);
			cell.innerHTML = "<text class='view'>"+users[user][i]+"</text>";
			cell = row.insertCell(1);
			cell.innerHTML = "<text class='view'>"+name+"</text>";
			cell = row.insertCell(2);
			cell.innerHTML = "<button class='view' onclick='viewSheet(\'"+users[user][i]+"\', \'"+name+"\', 0)'>View</button>";
			cell = row.insertCell(3);
			cell.innerHTML = "<button class='view' onclick='viewSheet(\'"+users[user][i]+"\', \'"+name+"\', 2)'>Edit</button>";
		}
	}	

}

/*
**Callback for eventual service call for View Sheets.. buttons
**This callback is used for calls that a list of only character names.
*/
function populateTableOneUser( data, user ){
	var names;
	if(data['r'] == 0){
		names = data['d'];
	}else{
		alert(data['t']);
		return;
	}
	document.getElementById("rightSide").innerHTML = "<table id='sheetTable' class='sheetTable' ><tr><td><text class='label'>Character Name</text></td><td><text class='label'>User Name</text></td><td /><td /></tr></table>";
	var table = document.getElementById("sheetTable");
	
	for(var i = 0; i < names.length; i++){
		var row = table.insertRow(table.rows.length);
		var cell = row.insertCell(0);
		cell.innerHTML = "<text class='view'>"+names[i]+"</text>";
		cell = row.insertCell(1);
		cell.innerHTML = "<text class='view'>"+user+"</text>";
		cell = row.insertCell(2);
		cell.innerHTML = "<button class='view' onclick='viewSheet(\'"+names[i]+"\', \'"+user+"\', 0)'>View</button>";
		cell = row.insertCell(3);
		cell.innerHTML = "<button class='view' onclick='viewSheet(\'"+names[i]+"\', \'"+user+"\', 2)'>Edit</button>";
	}
		

}

/*
**Call to service from View Sheets for All Users button
*/
function getSheetNames(){
	var url = wsAddress + "?action=viewSheets";
	$.ajax( {
		async: false,
		data: { "path" : encodeURI( url ) },
		url: proxy,
		success: function( data, textStatus, jqxhr ) { populateTableMultiUsers( data ); },
		error: function( jqhxr, status, errorThrown ) { serviceCallError() },
		dataType: "json"
	} );
}

/*
**Call to service from View Sheets for All User button
*/
function getSheetNamesUser(user){
	var url = wsAddress + "?action=viewSheets&username="+user;
	$.ajax( {
		async: false,
		data: { "path" : encodeURI( url ) },
		url: proxy,
		success: function( data, textStatus, jqxhr ) { populateTableOneUser( data, user ); },
		error: function( jqhxr, status, errorThrown ) { serviceCallError() },
		dataType: "json"
	} );
}

/*
**Callback for View button associated with a character sheet or the Create New button
*/
function viewSheetCallback( data ){
	if(data['r'] == 0){
		document.getElementById("rightSide").innerHTML = data['d'];
	}else{
		alert(data['t']);
	}
}

/*
**Callback for all errors on service calls
*/
function serviceCallError( ){
	alert("Couldn't connect to server! Oh Noes!");
}

/*
**Fucntions for all calls to service that return character sheets
** View buttons(mode 0), Edit buttons(mode 2) and Create New button(mode 1)
*/
function viewSheet(cName, user, mode){
	if(mode == 0){//view
		var url = wsAddress + "?action=viewSheet&username="+user+"&characterName="+cName;
		$.ajax( {
			async: false,
			data: { "path" : encodeURI( url ) },
			url: proxy,
			success: function( data, textStatus, jqxhr ) { viewSheetCallback( data ); },
			error: function( jqhxr, status, errorThrown ) { serviceCallError() },
			dataType: "json"
		} );
	}else if(mode == 1){//create new
		var url = wsAddress + "?action=createNew";
		$.ajax( {
			async: false,
			data: { "path" : encodeURI( url ) },
			url: proxy,
			success: function( data, textStatus, jqxhr ) { viewSheetCallback( data ); },
			error: function( jqhxr, status, errorThrown ) { serviceCallError() },
			dataType: "json"
		} );
	}else if(mode == 2){//edit sheet
		var url = wsAddress + "?action=viewSheet&username="+user+"&characterName="+cName+"&forEdit=yes";
		$.ajax( {
			async: false,
			data: { "path" : encodeURI( url ) },
			url: proxy,
			success: function( data, textStatus, jqxhr ) { viewSheetCallback( data ); },
			error: function( jqhxr, status, errorThrown ) { serviceCallError() },
			dataType: "json"
		} );
	}
}

/*
**Callback to alert the user of a successful save
*/
function saveSheetSuccess( data ){
	if(data['r'] == 0){
		alert("Your character has been saved successfully!");
	}else{
		alert(data['t']);
	}
}

/*
**Function to handle entire save process.
**Will first convert the edit page to xml
**Then will call service to save
*/
function saveSheet(){
	var xmldoc = ""
	//go over each element in right side
		//add relevant xml to string
	var user = Document.getElementById("unInput").value;
	var password = Document.getElementById("pwInput").value;
	var url = wsAddress + "?action=saveSheet&username="+user+"&password="+password+"&cName="+cName+"&xml="+escape( xmldoc );
	$.ajax( {
		async: false,
		data: { "path" : encodeURI( url ) },
		url: proxy,
		success: function( data, textStatus, jqxhr ) { saveSheetSuccess(data) },
		error: function( jqhxr, status, errorThrown ) { serviceCallError() },
		dataType: "json"
	} );
}


