<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

 <xsl:output encoding="ISO-8859-1" />
<xsl:template match="/">
	
		<table class="content">
			<tr>
				<th colspan="2">
					<text class="editHeader">Info</text>
				</th>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Character Name:</text>
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
					<text class="editLabel">Age:</text>
				</td>
				<td>
					<input class="edit" id="inAge">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/age"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Race:</text>
				</td>
				<td>
					<input class="edit" id="inRace">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/race"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Class:</text>
				</td>
				<td>
					<input class="edit" id="inClass">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/class"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Gender:</text>
				</td>
				<td>
					<input class="edit" id="inGender">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/gender"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>

			<tr>
				<td>
					<text class="editLabel">Level:</text>
				</td>
				<td>
					<input class="edit" id="inLevel">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/level"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>

			<tr>
				<td>
					<text class="editLabel">Experience:</text>
				</td>
				<td>
					<input class="edit" id="inExperience">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/experience"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Hitpoints:</text>
				</td>
				<td>
					<input class="edit" id="inHitpoints">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/hitpoints"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Base Attack Bonus:</text>
				</td>
				<td>
					<input class="edit" id="inBAB">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/baseAttackBonus"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Base Damage Bonus:</text>
				</td>
				<td>
					<input class="edit" id="inBDB">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/baseDamageBonus"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Attacks:</text>
				</td>
				<td>
					<input class="edit" id="inAttacks">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/attacks"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			
			<tr>
				<th colspan="2">
					<text class="editHeader">Attributes</text>
				</th>
			</tr>
			
			<tr>
				<td>
					<text class="editLabel">Strength:</text>
				</td>
				<td>
					<input class="edit" id="inStr">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/attributes/strength"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Dexterity:</text>
				</td>
				<td>
					<input class="edit" id="inDex">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/attributes/dexterity"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Mind:</text>
				</td>
				<td>
					<input class="edit" id="inMind">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/attributes/mind"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Charisma:</text>
				</td>
				<td>
					<input class="edit" id="inCha">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/attributes/charisma"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>	
			
			<tr>
				<th colspan="2">
					<text class="editHeader">Skills</text>
				</th>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Physical:</text>
				</td>
				<td>
					<input class="label" id="inPhys">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/skills/physical"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Subterfuge:</text>
				</td>
				<td>
					<input class="label" id="inSub">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/skills/subterfuge"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Knowledge:</text>
				</td>
				<td>
					<input class="label" id="inKnow">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/skills/knowledge"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Communication:</text>
				</td>
				<td>
					<input class="label" id="inComm">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/skills/communication"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>	
			<tr>
				<td>
					<text class="editLabel">Survival:</text>
				</td>
				<td>
					<input class="label" id="inSurv">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/skills/survival"/>
						</xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td>
					<text class="editLabel">Fabrication:</text>
				</td>
				<td>
					<input class="label" id="inFab">
						<xsl:attribute name="value" >
							<xsl:value-of select="character/skills/fabrication"/>
						</xsl:attribute>
					</input>						
				</td>
			</tr>
			<tr>
				<th >
			<br/>
					<text class="editHeader">Armour</text>
				</th>
				<th >
			<br/>
					<text class="editHeader">Shield</text>
				</th>
			</tr>
			
			<tr>
				<td >					
					<div class="edit">
						<form class="list" id="armourForm">
							<input class="armourInput" type="radio" name="armour" value="1" >
								<xsl:if test="/character/inventory/armour/name = 'Padded'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Padded<br />
							<input  class="armourInput" type="radio" name="armour" value="2" >
								<xsl:if test="/character/inventory/armour/name = 'Leather'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Leather <br />
							<input  class="armourInput" type="radio" name="armour" value="3" >
								<xsl:if test="/character/inventory/armour/name = 'Studded Leather'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Studded Leather <br />
							<input class="armourInput"  type="radio" name="armour" value="4" >
								<xsl:if test="/character/inventory/armour/name = 'Scale Mail'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Scale Mail <br />
							<input  class="armourInput" type="radio" name="armour" value="5" >
								<xsl:if test="/character/inventory/armour/name = 'Splint Mail'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Splint Mail <br />
							<input  class="armourInput" type="radio" name="armour" value="6">
								<xsl:if test="/character/inventory/armour/name = 'Chain Mail'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>
							Chain Mail<br />
							<input class="armourInput"  type="radio" name="armour" value="7" >
								<xsl:if test="/character/inventory/armour/name = 'Banded'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Banded <br />
							<input  class="armourInput" type="radio" name="armour" value="8" >
								<xsl:if test="/character/inventory/armour/name = 'Half Plate'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Half Plate <br />
							<input class="armourInput"  type="radio" name="armour" value="9" >
								<xsl:if test="/character/inventory/armour/name = 'Full Plate'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Full Plate <br />
						</form>
					</div>
				</td>
				<td >				
					<div class="edit">
						<form class="list" id="shieldForm">
							<input class="shieldInput" type="radio" name="shield" value="0" >
								<xsl:if test="not(/character/inventory/shield)">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>No Shield<br />
							<input class="shieldInput" type="radio" name="shield" value="1" >
								<xsl:if test="/character/inventory/shield/name = 'Small Steel'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Small Steel<br />
							<input class="shieldInput" type="radio" name="shield" value="2" >
								<xsl:if test="/character/inventory/shield/name = 'Small Wooden'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Small Wooden <br />
							<input class="shieldInput" type="radio" name="shield" value="3" >
								<xsl:if test="/character/inventory/shield/name = 'Large'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Large<br />
							<input class="shieldInput" type="radio" name="shield" value="4" >
								<xsl:if test="/character/inventory/shield/name = 'Tower'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Tower<br />
						</form>						
					</div>
				</td>
			</tr>
			
			<tr>
				<th colspan="2">
			<br/>
					<text class="editHeader">Weapons</text>
				</th>

			</tr>
						
			<tr>
				<td >
					<div class="edit">
						<form class="list" id="weaponForm1">
							<input type="checkbox" name="weapon" value="1" >
								<xsl:if test="/character/inventory/weapon/name = 'Dagger'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Dagger<br />
							<input type="checkbox" name="weapon" value="2" >
								<xsl:if test="/character/inventory/weapon/name = 'Club'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Club <br />
							<input type="checkbox" name="weapon" value="3" >
								<xsl:if test="/character/inventory/weapon/name = 'Light Hammer'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Light Hammer <br />
							<input type="checkbox" name="weapon" value="4" >
								<xsl:if test="/character/inventory/weapon/name = 'Short Sword'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Short Sword<br />
							<input type="checkbox" name="weapon" value="5" >
								<xsl:if test="/character/inventory/weapon/name = 'Spear'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Spear <br />
							<input type="checkbox" name="weapon" value="6" >
								<xsl:if test="/character/inventory/weapon/name = 'Light Crossbow'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Light Crossbow<br />
							<input type="checkbox" name="weapon" value="7" >
								<xsl:if test="/character/inventory/weapon/name = 'Short Bow'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Short Bow <br />
						</form>
					</div>
				</td>
				<td >
					<div class="edit">
						<form class="list" id="weaponForm2">
							<input type="checkbox" name="weapon" value="8" >
								<xsl:if test="/character/inventory/weapon/name = 'Long Sword'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Long Sword <br />
							<input type="checkbox" name="weapon" value="9" >
								<xsl:if test="/character/inventory/weapon/name = 'Battle Axe'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Battle Axe<br />
							<input type="checkbox" name="weapon" value="10" >
								<xsl:if test="/character/inventory/weapon/name = 'Flail'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Flail<br />
							<input type="checkbox" name="weapon" value="11" >
								<xsl:if test="/character/inventory/weapon/name = 'Two-handed Sword'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Two-handed Sword <br />
							<input type="checkbox" name="weapon" value="12" >
								<xsl:if test="/character/inventory/weapon/name = 'War Axe'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>War Axe<br />
							<input type="checkbox" name="weapon" value="13" >
								<xsl:if test="/character/inventory/weapon/name = 'Heavy Crossbow'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Heavy Crossbow<br />
							<input type="checkbox" name="weapon" value="14" >
								<xsl:if test="/character/inventory/weapon/name = 'Long Bow'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Long Bow <br />
						</form>
					</div>
				</td>
			</tr>
			
			<tr>
				<th colspan="2">
			<br/>
					<text class="editHeader">Items</text>
				</th>

			</tr>
			
			<tr>
				<td >
					<div class="edit">
						<form class="list" id="itemForm1">
							<input type="checkbox" name="item" value="15" >
								<xsl:if test="/character/inventory/item/name = 'Torch'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Torch<br />
							<input type="checkbox" name="item" value="16" >
								<xsl:if test="/character/inventory/item/name = 'Waterskin'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Waterskin <br />
							<input type="checkbox" name="item" value="17" >
								<xsl:if test="/character/inventory/item/name = 'Crowbar'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Crowbar<br />
							<input type="checkbox" name="item" value="18" >
								<xsl:if test="/character/inventory/item/name = 'Holy Symbol'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Holy Symbol<br />
							<input type="checkbox" name="item" value="19" >
								<xsl:if test="/character/inventory/item/name = 'Rations'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Rations <br />
						</form>
					</div>
				</td>
				<td >
					<div class="edit">
						<form class="list" id="itemForm2">
							<input type="checkbox" name="item" value="20" >
								<xsl:if test="/character/inventory/item/name = 'McGuffin'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>McGuffin<br />
							<input type="checkbox" name="item" value="21" >
								<xsl:if test="/character/inventory/item/name = 'Chalk'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Chalk<br />
							<input type="checkbox" name="item" value="22" >
								<xsl:if test="/character/inventory/item/name = 'Pitons'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Pitons<br />
							<input type="checkbox" name="item" value="23" >
								<xsl:if test="/character/inventory/item/name = 'Hammer'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Hammer<br />
							<input type="checkbox" name="item" value="24" >
								<xsl:if test="/character/inventory/item/name = 'Disguise Kit'">
									<xsl:attribute name="checked" >checked</xsl:attribute>
								</xsl:if>
							</input>Disguise Kit<br />
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