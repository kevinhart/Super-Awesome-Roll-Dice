<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

 <xsl:output encoding="ISO-8859-1" />
<xsl:template match="/">
	
		<table class="content">
			<tr>
				<th colspan="2">
					<text class="header">Info</text>
				</th>
			</tr>
			<tr>
				<td>
					<text class="label">Character Name:</text>
				</td>
				<td>
					<input class="edit" id="inName">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/name"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="label">Race:</text>
				</td>
				<td>
					<input class="edit" id="inRace"/>
				</td>
			</tr>
			<tr>
				<td>
					<text class="label">Class:</text>
				</td>
				<td>
					<input class="edit" id="inClass"/>
				</td>
			</tr>
			<tr>
				<td>
					<text class="label">Gender:</text>
				</td>
				<td>
					<input class="edit" id="inGender"/>
				</td>
			</tr>

			<tr>
				<td>
					<text class="label">Level:</text>
				</td>
				<td>
					<input class="edit" id="inLevel"/>
				</td>
			</tr>

			<tr>
				<td>
					<text class="label">Experience:</text>
				</td>
				<td>
					<input class="edit" id="inExperience"/>
				</td>
			</tr>
			
			<tr>
				<th colspan="2">
					<text class="header">Attributes</text>
				</th>
			</tr>
			
			<tr>
				<td>
					<text class="label">Strength:</text>
				</td>
				<td>
					<input class="edit" id="inStr"></input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="label">Dexterity:</text>
				</td>
				<td>
					<input class="edit" id="inDex"></input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="label">Mind:</text>
				</td>
				<td>
					<input class="edit" id="inMind"></input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="label">Charisma:</text>
				</td>
				<td>
					<input class="edit" id="inCha"></input>
				</td>
			</tr>	
			
			<tr>
				<th colspan="2">
					<text class="header">Skills</text>
				</th>
			</tr>
			<tr>
				<td>
					<text>Physical:</text>
				</td>
				<td>
					<input class="label" id="inPhys"></input>
				</td>
			</tr>
			<tr>
				<td>
					<text>Subterfuge:</text>
				</td>
				<td>
					<input class="label" id="inSub"></input>
				</td>
			</tr>
			<tr>
				<td>
					<text>Knowledge:</text>
				</td>
				<td>
					<input class="label" id="inKnow"></input>
				</td>
			</tr>
			<tr>
				<td>
					<text>Communication:</text>
				</td>
				<td>
					<input class="label" id="inComm"></input>
				</td>
			</tr>	
			<tr>
				<td>
					<text>Survival:</text>
				</td>
				<td>
					<input class="label" id="inSurv"></input>
				</td>
			</tr>
			<tr>
				<td>
					<text>Fabrication:</text>
				</td>
				<td>
					<input class="label" id="inFab"></input>
				</td>
			</tr>
			<tr>
				<th >
			<br/>
					<text class="header">Armour</text>
				</th>
				<th >
			<br/>
					<text class="header">Shield</text>
				</th>
			</tr>
			
			<tr>
				<td >					
					<div class="edit">
						<form class="list" id="armourForm">
							<input type="radio" name="armour" value="Padded" />Padded<br />
							<input type="radio" name="armour" value="Leather" />Leather <br />
							<input type="radio" name="armour" value="Studded Leather" />Studded Leather <br />
							<input type="radio" name="armour" value="Scale Mail" />Scale Mail <br />
							<input type="radio" name="armour" value="Splint Mail" />Splint Mail <br />
							<input type="radio" name="armour" value="Chain Mail" />Chain Mail<br />
							<input type="radio" name="armour" value="Banded" />Banded <br />
							<input type="radio" name="armour" value="Half Plate" />Half Plate <br />
							<input type="radio" name="armour" value="Full Plate" />Full Plate <br />
						</form>
					</div>
				</td>
				<td >				
					<div class="edit">
						<form class="list" id="shieldForm">
							<input type="radio" name="shield" value="Small Steel" />Small Steel<br />
							<input type="radio" name="shield" value="Small Wooden" />Small Wooden <br />
							<input type="radio" name="shield" value="Large" />Large<br />
							<input type="radio" name="shield" value="Tower" />Tower<br />
						</form>						
					</div>
				</td>
			</tr>
			
			<tr>
				<th colspan="2">
			<br/>
					<text class="header">Weapons</text>
				</th>

			</tr>
			
			<tr>
				<td >
					<div class="edit">
						<form class="list" id="weaponForm1">
							<input type="checkbox" name="weapon" value="Dagger" />Dagger<br />
							<input type="checkbox" name="weapon" value="Club" />Club <br />
							<input type="checkbox" name="weapon" value="Light Hammer" />Light Hammer <br />
							<input type="checkbox" name="weapon" value="Short Sword" />Short Sword<br />
							<input type="checkbox" name="weapon" value="Spear" />Spear <br />
							<input type="checkbox" name="weapon" value="Light Crossbow" />Light Crossbow<br />
							<input type="checkbox" name="weapon" value="Short Bow" />Short Bow <br />
						</form>
					</div>
				</td>
				<td >
					<div class="edit">
						<form class="list" id="weaponForm2">
							<input type="checkbox" name="weapon" value="Long Sword" />Long Sword <br />
							<input type="checkbox" name="weapon" value="Battle Axe" />Battle Axe<br />
							<input type="checkbox" name="weapon" value="Flail" />Flail<br />
							<input type="checkbox" name="weapon" value="Two-handed Sword" />Two-handed Sword <br />
							<input type="checkbox" name="weapon" value="War Axe" />War Axe<br />
							<input type="checkbox" name="weapon" value="Heavy Crossbow" />Heavy Crossbow<br />
							<input type="checkbox" name="weapon" value="Long Bow" />Long Bow <br />
						</form>
					</div>
				</td>
			</tr>
			
			<tr>
				<th colspan="2">
			<br/>
					<text class="header">Items</text>
				</th>

			</tr>
			
			<tr>
				<td >
					<div class="edit">
						<form class="list" id="itemForm1">
							<input type="checkbox" name="item" value="Torch" />Torch<br />
							<input type="checkbox" name="item" value="Waterskin" />Waterskin <br />
							<input type="checkbox" name="item" value="Crowbar" />Crowbar<br />
							<input type="checkbox" name="item" value="Holy Symbol" />Holy Symbol<br />
							<input type="checkbox" name="item" value="Rations" />Rations <br />
						</form>
					</div>
				</td>
				<td >
					<div class="edit">
						<form class="list" id="itemForm2">
							<input type="checkbox" name="item" value="McGuffin" />McGuffin<br />
							<input type="checkbox" name="item" value="Chalk" />Chalk<br />
							<input type="checkbox" name="item" value="Pitons" />Pitons<br />
							<input type="checkbox" name="item" value="Hammer" />Hammer<br />
							<input type="checkbox" name="item" value="Disguise Kit" />Disguise Kit<br />
						</form>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<br />
				</td>
			</tr>
			<tr>
				<td>
					<text>Username:</text>
					<input id="unInput"></input>
				</td>
				<td>
					<text>Password:</text>				
					<input id="pwInput"></input>
				</td>
			</tr>
			<tr>
				<td colspan="2">					
					<button id="saveSheetButton" onclick="saveSheet()">Save</button>
				</td>
			</tr>
		</table>
</xsl:template>

</xsl:stylesheet>