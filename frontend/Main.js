/**
 * @author John Deer
 * @contributor Kevin Hart
 */
 
 var userName = "";
 var wsAddress = "http://saskatoon.cs.rit.edu:4241/sards"
 var proxy = "proxy.php";

function resetRightDiv() {
	document.getElementById( "rightSide" ).innerHTML = "<img src=\"Beacon_logo.jpg\" width=\"100%\" />";
}
 
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
		for(var i = 0; i < users[name].length;i++){
			var row = table.insertRow(table.rows.length);
			var cell = row.insertCell(0);
			cell.innerHTML = "<text class='view'>"+users[name][i]+"</text>";
			cell = row.insertCell(1);
			cell.innerHTML = "<text class='view'>"+name+"</text>";
			cell = row.insertCell(2);
			cell.innerHTML = "<button class='view' onclick='viewSheet(\""+users[name][i]+"\", \""+name+"\", 0)'>View</button>";
			cell = row.insertCell(3);
			cell.innerHTML = "<button class='view' onclick='viewSheet(\""+users[name][i]+"\", \""+name+"\", 2)'>Edit</button>";
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
		cell.innerHTML = "<button class='view' onclick='viewSheet(\""+names[i]+"\", \""+user+"\", 0)'>View</button>";
		cell = row.insertCell(3);
		cell.innerHTML = "<button class='view' onclick='viewSheet(\""+names[i]+"\", \""+user+"\", 2)'>Edit</button>";
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
		var url = wsAddress + "?action=viewSheets&username="+user+"&cName="+cName;
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
		var url = wsAddress + "?action=viewSheets&username="+user+"&cName="+cName+"&forEdit=yes";
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
	var xmldoc = xmlify();
	var user = document.getElementById("unInput").value;
	var password = document.getElementById("pwInput").value; 
	var cName = document.getElementById("inName").value;
	var url = wsAddress + "?action=saveSheet&username="+user+"&password="+SHA1(password)+"&cName="+cName+"&xml="+xmldoc;
	$.ajax( {
		async: false,
		data: { "path" : encodeURI( url ) },
		url: proxy,
		success: function( data, textStatus, jqxhr ) { saveSheetSuccess(data) },
		error: function( jqhxr, status, errorThrown ) { serviceCallError() },
		dataType: "json"
	} );
}

function xmlify(){
	var xmlDoc = document.getElementById("inName").value + ":";
	xmlDoc += document.getElementById("unInput").value + ":";	
	xmlDoc += document.getElementById("inAge").value + ":";
	xmlDoc += document.getElementById("inRace").value + ":";
	xmlDoc += document.getElementById("inClass").value + ":";
	xmlDoc += document.getElementById("inGender").value + ":";
	xmlDoc += document.getElementById("inLevel").value + ":";
	xmlDoc += document.getElementById("inExperience").value + ":";
	xmlDoc += document.getElementById("inHitpoints").value + ":";
	xmlDoc += document.getElementById("inBAB").value + ":";
	xmlDoc += document.getElementById("inBDB").value + ":";
	xmlDoc += document.getElementById("inAttacks").value + ":";
	xmlDoc += document.getElementById("inStr").value + ":";
	xmlDoc += document.getElementById("inDex").value + ":";
	xmlDoc += document.getElementById("inMind").value + ":";
	xmlDoc += document.getElementById("inCha").value + ":";
	xmlDoc += document.getElementById("inPhys").value + ":";
	xmlDoc += document.getElementById("inSub").value + ":";
	xmlDoc += document.getElementById("inKnow").value + ":";
	xmlDoc += document.getElementById("inComm").value + ":";
	xmlDoc += document.getElementById("inSurv").value + ":";
	xmlDoc += document.getElementById("inFab").value + ":";
	xmlDoc += document.getElementById("inGold").value + ":";
	xmlDoc += document.getElementById("inSilver").value + ":";
	xmlDoc += document.getElementById("inCopper").value + ":";
	var inputArray = document.getElementsByClassName("armourInput");
	var inputVal = 0;
	for(var i = 0;i < 9;i++){
		if(inputArray[i].checked && inputArray[i].value > inputVal){
			inputVal = inputArray[i].value;
		}
	}
	xmlDoc += inputVal + ":";
	inputArray = document.getElementsByClassName("shieldInput");
	inputVal = 0;
	for(var i = 0;i < 5;i++){
		if(inputArray[i].checked && inputArray[i].value > inputVal){
			inputVal = inputArray[i].value;
		}
	}
	xmlDoc += inputVal + ":";
	
	//stolen from interwebs @http://bytes.com/topic/javascript/answers/712764-how-getelementbytype-check-validation-using-javascript
    var node_list = document.getElementsByTagName('input');
    var checkboxes = [];
 
    for (var i = 0; i < node_list.length; i++) {
        var node = node_list[i];
 
        if (node.getAttribute('type') == 'checkbox') {
            checkboxes.push(node);
        }
	}
	//end stolen code
	
	for(var i = 0;i < 24;i++){	
		if(checkboxes[i].checked){
			xmlDoc += checkboxes[i].value + ":";
		}
	}
	
	return xmlDoc;
}

/*
**
*/
function searchForSheet(){	
	var query = "";
	var input = document.getElementById("StrInput");
	if(input.value != ""){
		query += "(strength, "+parseInt(input.value)+", 2)";
	}
	
	input = document.getElementById("DexInput");
	if(input.value != ""){
		query += "(dexterity, "+parseInt(input.value)+", 2)";
	}
	input = document.getElementById("MindInput");
	if(input.value != ""){
		query += "(mind, "+parseInt(input.value)+", 2)";
	}
	input = document.getElementById("ChaInput");
	if(input.value != ""){
		query += "(charisma, "+parseInt(input.value)+", 2)";
	}
	input = document.getElementById("physInput");
	if(input.value != ""){
		query += "(physical, "+parseInt(input.value)+", 2)";
	}
	input = document.getElementById("subInput");
	if(input.value != ""){
		query += "(search, "+parseInt(input.value)+", 2)";
	}
	input = document.getElementById("knowInput");
	if(input.value != ""){
		query += "(knowledge, "+parseInt(input.value)+", 2)";
	}
	input = document.getElementById("comInput");
	if(input.value != ""){
		query += "(communication, "+parseInt(input.value)+", 2)";
	}
	input = document.getElementById("surInput");
	if(input.value != ""){
		query += "(survival, "+parseInt(input.value)+", 2)";
	}
	input = document.getElementById("fabInput");
	if(input.value != ""){
		query += "(fabrication, "+parseInt(input.value)+", 2)";
	}
	var url = wsAddress + "?action=query&querystring="+query;
		$.ajax( {
			async: false,
			data: { "path" : encodeURI( url ) },
			url: proxy,
			success: function( data, textStatus, jqxhr ) { populateTableMultiUsers( data ); },
			error: function( jqhxr, status, errorThrown ) { serviceCallError() },
			dataType: "json"
		} );
}
