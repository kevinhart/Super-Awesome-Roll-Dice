/**
 * @author John Deer
 * @contributor Kevin Hart
 */
 
 var userName = "";
 var wsAddress = "http://saskatoon.cs.rit.edu:4241/sards"
 var proxy = "proxy.php";

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
	var url = wsAddress + "?action=saveSheet&username="+user+"&password="+SHA1(password)+"&cName="+cName+"&xml="+escape( xmldoc );
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
	
	var inputArray = document.getElementsByClassName("armourInput");
	var inputVal = 0;
	for(var i = 0;i < 9;i++){
		if(inputArray[i].checked && inputArray[i].value > inputVal){
			inputVal = inputArray[i].value;
		}
	}
	xmlDoc += inputVal + ":";
	/*
	switch(parseInt(inputVal)){
		case 1:
			xmlDoc += "<armour><name>Padded</name><minStr>-</minStr><acBonus>1</acBonus></armour>";
			break;
		case 2:
			xmlDoc += "<armour><name>Leather</name><minStr>6</minStr><acBonus>2</acBonus></armour>";
			break;
		case 3:
			xmlDoc += "<armour><name>Studded Leather</name><minStr>8</minStr><acBonus>3</acBonus></armour>";
			break;
		case 4:
			xmlDoc += "<armour><name>Scale Mail</name><minStr>10</minStr><acBonus>4</acBonus></armour>";
			break;
		case 5:
			xmlDoc += "<armour><name>Splint Mail</name><minStr>12</minStr><acBonus>5</acBonus></armour>";
			break;
		case 6:
			xmlDoc += "<armour><name>Chain Mail</name><minStr>12</minStr><acBonus>5</acBonus></armour>";
			break;
		case 7:
			xmlDoc += "<armour><name>Banded</name><minStr>14</minStr><acBonus>6</acBonus></armour>";
			break;
		case 8:
			xmlDoc += "<armour><name>Half Plate</name><minStr>14</minStr><acBonus>7</acBonus></armour>";
			break;
		case 9:
			xmlDoc += "<armour><name>Full Plate</name><minStr>15</minStr><acBonus>8</acBonus></armour>";
			break;
		default:
			xmlDoc += "<armour><name>Padded</name><minStr>-</minStr><acBonus>1</acBonus></armour>";
			break;
	}
*/
	inputArray = document.getElementsByClassName("shieldInput");
	inputVal = 0;
	for(var i = 0;i < 5;i++){
		if(inputArray[i].checked && inputArray[i].value > inputVal){
			inputVal = inputArray[i].value;
		}
	}
	xmlDoc += inputVal + ":";
	/*
	switch(parseInt(inputVal)){
		case 1:
			xmlDoc += "<shield><name>Small Steel</name><dexPenalty>-1</dexPenalty><acBonus>+1</acBonus></shield>";
			break;
		case 2:
			xmlDoc += "<shield><name>Small Wooden</name><dexPenalty>-2</dexPenalty><acBonus>+1</acBonus></shield>";
			break;
		case 3:
			xmlDoc += "<shield><name>Large</name><dexPenalty>-2</dexPenalty><acBonus>2</acBonus></shield>";
			break;
		case 4:
			xmlDoc += "<shield><name>Tower</name><dexPenalty>-3</dexPenalty><acBonus>+3</acBonus></shield>";
			break;
		default:
			xmlDoc += "<shield><name>No Shield</name><dexPenalty>0</dexPenalty><acBonus>0</acBonus></shield>";
			break;
	}
	*/
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
		}/*
		switch((parseInt(checkboxes[i].value))){
			case 1:				
				xmlDoc += "<weapon twohanded=\"no\"><name>Dagger</name><stat>Str</stat><damage>light</damage><range>-</range></weapon>";
				xmlDoc += "<weapon twohanded=\"no\"><name>Thrown Dagger</name><stat>Dex</stat><damage>light</damage><range>5</range></weapon>";
				break;
			case 2:
				xmlDoc += "<weapon twohanded=\"no\"><name>Club</name><stat>Str</stat><damage>light</damage><range>-</range></weapon>";
				break;
			case 3:
				xmlDoc += "<weapon twohanded=\"no\"><name>Light Hammer</name><stat>Str</stat><damage>light</damage><range>-</range></weapon>";
				xmlDoc += "<weapon twohanded=\"no\"><name>Thrown Light Hammer</name><stat>Dex</stat><damage>light</damage><range>20</range></weapon>";
				break;
			case 4:
				xmlDoc += "<weapon twohanded=\"no\"><name>Short Sword</name><stat>Str</stat><damage>light</damage><range>-</range></weapon>";
				break;
			case 5:
				xmlDoc += "<weapon twohanded=\"yes\"><name>Spear</name><stat>Str</stat><damage>light</damage><range>-</range></weapon>";
				xmlDoc += "<weapon twohanded=\"yes\"><name>Thrown Spear</name><stat>Dex</stat><damage>light</damage><range>20</range></weapon>";
				break;
			case 6:
				xmlDoc += "<weapon twohanded=\"yes\"><name>Light Crossbow</name><stat>Dex</stat><damage>light</damage><range>80</range></weapon>";
				break;
			case 7:
				xmlDoc += "<weapon twohanded=\"yes\"><name>Short Bow</name><stat>Dex</stat><damage>light</damage><range>60</range></weapon>";
				break;
			case 8:
				xmlDoc += "<weapon twohanded=\"no\"><name>Long Sword</name><stat>Str</stat><damage>heavy</damage><range>-</range></weapon>";
				break;
			case 9:
				xmlDoc += "<weapon twohanded=\"no\"><name>Battle Axe</name><stat>Str</stat><damage>heavy</damage><range>-</range></weapon>";
				break;
			case 10:
				xmlDoc += "<weapon twohanded=\"no\"><name>Flail</name><stat>Str</stat><damage>heavy</damage><range>-</range></weapon>";
				break;
			case 11:
				xmlDoc += "<weapon twohanded=\"yes\"><name>Two-handed Sword</name><stat>Str</stat><damage>heavy</damage><range>-</range></weapon>";
				break;
			case 12:
				xmlDoc += "<weapon twohanded=\"yes\"><name>War Axe</name><stat>Str</stat><damage>heavy</damage><range>-</range></weapon>";
				break;
			case 13:
				xmlDoc += "<weapon twohanded=\"yes\"><name>Heavy Crossbow</name><stat>Dex</stat><damage>heavy</damage><range>120</range></weapon>";
				break;
			case 14:
				xmlDoc += "<weapon twohanded=\"yes\"><name>Long Bow</name><stat>Dex</stat><damage>heavy</damage><range>100</range></weapon>";
				break;
			case 15:
				xmlDoc += "<item><name>Torch</name><descrip>Provides light for 2 hours</descrip></item>";
				break;
			case 16:				
				xmlDoc += "<item><name>Waterskin</name><descrip>Holds enough water for 1 day</descrip></item>";
				break;
			case 17:
				xmlDoc += "<item><name>Crowbar</name><descrip>Gives the hero a +2 bonus to disarming traps and prying open doors</descrip></item>";
				break;
			case 18:
				xmlDoc += "<item><name>Holy Symbol</name><descrip>Allows the hero to channel his god's power</descrip></item>";
				break;
			case 19:
				xmlDoc += "<item><name>Rations</name><descrip>Dried meat, fruit and cheese</descrip></item>";
				break;
			case 20:
				xmlDoc += "<item><name>McGuffin</name><descrip>Allows hero to bypass one plot hook</descrip></item>";
				break;
			case 21:
				xmlDoc += "<item><name>Chalk</name><descrip>Stick of chalk for marking</descrip></item>";
				break;
			case 22:
				xmlDoc += "<item><name>Pitons</name><descrip>Useful for climbing or jamming open doors</descrip></item>";
				break;
			case 23:
				xmlDoc += "<item><name>Hammer</name><descrip>Useful for hitting non-enemy objects</descrip></item>";
				break;
			case 24:
				xmlDoc += "<item><name>Disguise Kit</name><descrip>Gives the hero +2 to +10 on subterfuge checks to disguise self</descrip></item>";
			default:
				break;

		}*/
	}
	
	return xmlDoc;
}
