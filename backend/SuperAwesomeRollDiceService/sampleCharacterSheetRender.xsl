<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

 <xsl:output encoding="ISO-8859-1" />
<xsl:template match="/">

<html>
<head>
<title>SuperAwesomeRollDice</title>
<style type="text/css">
	body 
	{
		font-family:arial,helvetica,sans-serif;
		font-size:12px;
	}
	#header 
	{
		text-align:center;
		height:60px;
		margin-top:3px;
		margin-bottom:0;
	}
	div
	{
		padding:5px;
	}
	#content 
	{
		text-align: center;
		margin: 0 auto;
		width:660px;
		padding-bottom:10px;
	}
	#infoDiv 
	{
		padding:10px;
		border: 1px solid black;
		height:130px;
	}
	#attrDiv 
	{
		margin-top:10px;
		padding:10px;
		border: 1px solid black;
		height:157px;
	}
	#skillDiv 
	{
		margin-top:10px;
		padding:10px;
		border: 1px solid black;
		height:220px;
	}
	#invDiv 
	{
		text-indent:22px;
		margin-top:10px;
		padding:10px;
		border: 1px solid black;
	}
	div.attrBox 
	{		
		margin-left:3%;
		float:left;
		text-align:left;
		border: 0px solid black;
		height:145px;
		width:200px;
	}
	div.attr
	{	
		font-family:"Trebuchet MS", Helvetica, sans-serif;
		font-size:20px;
	}
	div.attrName 
	{	
		float:left;
		text-align:left;
	}
	div.attrNumber 
	{
		text-align:right;
	}
	#leftInfoDiv 
	{
		float:left;
		margin-left:12%;
		display:inline-block;
		text-align:center;
	}
	#rightInfoDiv 
	{
		margin-right:5%;
		float:right;
		display:inline;
		text-align:center;
	}
	#hitpointDiv 
	{
		float:left;
		margin-left:12%;
		display:inline-block;
		text-align:center;
	}
	p
	{
		text-align:left;
		font-size:16px;
	}
	#armorclassDiv 
	{
		margin-right:5%;
		float:right;
		display:inline;
		text-align:center;
	}
	#babDiv 
	{
		float:left;
		margin-left:20%;
		display:inline-block;
		text-align:center;
	}
	p1.hpac
	{		
		font-size:20px;
	}
	text.bab
	{		
		font-weight:bold;
		font-size:20px;
	}
	text.babNum
	{	
		font-size:18px;
	}
	p.header
	{
		text-align:center;
		font-size:16px;
		font-weight:bold;
	}
	p.stats
	{
		text-align:center;
		font-size:12px;
		font-weight:bold;
	}
	span.weapToStatHeader
	{
		margin-right:220px;
	}
	span.infoHeader
	{
		margin-right:40px;
	}
	span.weapToStat
	{
		margin-right:236px;
	}
	span.info
	{
		margin-right:55px;
	}
	span.topDiv
	{
		margin-right:10px;
	}
	table.weapons
	{
		width:100%;
		text-align:left;
	}
	td
	{
		text-align:center;	
		font-size:12px;
		font-weight:bold;
	}
	td.name
	{	
		text-align:left;
	}
	th
	{
		text-align:center;	
	}
	th.name
	{	
		text-align:left;
	}
</style>
</head>
<body>
<div id="header">
 <h1><xsl:value-of select="character/name"/></h1></div>
	<div id="content">
		<div id="infoDiv">
			<div id="leftInfoDiv">
				<p class="info"><b>Class:</b><span class="topDiv"></span><xsl:value-of select="character/class"/></p>
				<p class="info"><b>Race:</b><span class="topDiv"></span><xsl:value-of select="character/race"/></p>
				<p class="info"><b>Gender:</b><span class="topDiv"></span><xsl:value-of select="character/gender"/></p>
			</div>
			<div id="rightInfoDiv">
				<p class="info"><b>Age</b>:<span class="topDiv"></span><xsl:value-of select="character/age"/></p>
				<p class="info"><b>Level:</b><span class="topDiv"></span><xsl:value-of select="character/level"/></p>
				<p class="info"><b>Experience:</b><span class="topDiv"></span><xsl:value-of select="character/experience"/></p>
			</div>
		</div>
		<div id="attrDiv">
			<div class="attrBox">
				<div class="attr attrName">
					<text class="attr-text" >Strength:</text>
				</div>
				<div class="attr attrNumber">
					<text class="attr-num"><xsl:value-of select="character/attributes/strength"/>(<xsl:value-of select="floor((number(//character/attributes/strength) - 10) div 2)"/>)</text>
				</div>
				<div class="attr attrName">
					<text class="attr-text" >Dexterity:</text>
				</div>
				<div class="attr attrNumber">
					<text class="attr-num"><xsl:value-of select="number(character/attributes/dexterity) + number(character/inventory/shield/dexPenalty)"/>(<xsl:value-of select="floor((number(//character/attributes/dexterity)  + number(character/inventory/shield/dexPenalty)- 10) div 2)"/>)</text>
				</div>
				<div class="attr attrName">
					<text class="attr-text" >Mind:</text>
				</div>
				<div class="attr attrNumber">
					<text class="attr-num"><xsl:value-of select="character/attributes/mind"/>(<xsl:value-of select="floor((number(//character/attributes/mind) - 10) div 2)"/>)</text>
				</div>
				<div class="attr attrName">
					<text class="attr-text" >Charisma:</text>
				</div>
				<div class="attr attrNumber">
					<text class="attr-num"><xsl:value-of select="character/attributes/charisma"/>(<xsl:value-of select="floor((number(//character/attributes/charisma) - 10) div 2)"/>)</text>
				</div>
			</div>
			<div id="armorclassDiv">
				<h1>Armor Class</h1><br/>
				<p1 class="hpac" ><xsl:value-of select="10 + number(//armour/acBonus) + number(//shield/acBonus) + floor((number(//character/attributes/dexterity)  + number(character/inventory/shield/dexPenalty)- 10) div 2)"/></p1>
			</div>
			<div id="hitpointDiv">
				<h1>Hit Points</h1><br/>
				<p1 class="hpac" ><xsl:value-of select="character/hitpoints"/></p1>
			</div>
		</div>
		<div id="skillDiv">
			<div class="attrBox">
				<div class="attr attrName">
					<text class="attr-text" >Physical:</text>
				</div>
				<div class="attr attrNumber">
					<text class="attr-num"><xsl:value-of select="character/skills/physical"/></text>
				</div>
				<div class="attr attrName">
					<text class="attr-text" >Subterfuge:</text>
				</div>
				<div class="attr attrNumber">
					<text class="attr-num"><xsl:value-of select="character/skills/subterfuge"/></text>
				</div>
				<div class="attr attrName">
					<text class="attr-text" >Knoledge:</text>
				</div>
				<div class="attr attrNumber">
					<text class="attr-num"><xsl:value-of select="character/skills/knowledge"/></text>
				</div>
				<div class="attr attrName">
					<text class="attr-text" >Communication:</text>
				</div>
				<div class="attr attrNumber">
					<text class="attr-num"><xsl:value-of select="character/skills/communication"/></text>
				</div>
				<div class="attr attrName">
					<text class="attr-text" >Survival:</text>
				</div>
				<div class="attr attrNumber">
					<text class="attr-num"><xsl:value-of select="character/skills/survival"/></text>
				</div>
				<div class="attr attrName">
					<text class="attr-text" >Fabrication:</text>
				</div>
				<div class="attr attrNumber">
					<text class="attr-num"><xsl:value-of select="character/skills/fabrication"/></text>
				</div>
			</div>
			<div id="babDiv">
				<text class="bab">Base Attack Bonus</text><br/><br/>
				<text class="babNum"><xsl:value-of select="character/baseAttackBonus"/></text><br/><br/>
				<text class="bab">Base Damage Bonus</text><br/><br/>
				<text class="babNum"><xsl:value-of select="character/baseDamageBonus"/></text><br/><br/>
				<text class="bab"># of Attacks</text><br/><br/>
				<text class="babNum"><xsl:value-of select="character/attacks"/></text><br/>
			</div>
		</div>
		<div id="invDiv">
			<table class="weapons">
				<colgroup class="weapon" span="1"></colgroup>
				<colgroup class="info" span="4"></colgroup>
				<th class="name">
				Weapon
				</th>
				<th>
				Stat
				</th>
				<th>
				To Hit
				</th>
				<th>
				Damage
				</th>
				<th>
				Range
				</th>
				<xsl:for-each select="character/inventory/weapon">
					<tr>
						<td class="name">
						<xsl:value-of select="name"/>
						</td>
						<td>
						<xsl:value-of select="stat"/>
						</td>
						<td>
						<xsl:if test="stat='Str'">
							<xsl:value-of select="floor((number(//character/attributes/strength) - 10) div 2) + (number(//character/baseAttackBonus))"/>
						</xsl:if>
						<xsl:if test="stat='Dex'">
							<xsl:value-of select="floor((number(//character/attributes/dexterity) + number(//character/inventory/shield/dexPenalty) - 10) div 2) + (number(//character/baseAttackBonus))"/>
						</xsl:if>
						</td>
						<td>
							<xsl:if test="damage='heavy'">
								<xsl:if test="@twohanded='yes'">
									<xsl:if test="range='-'">										
										1d8	+ <xsl:value-of select="floor((number(//character/attributes/strength) - 10) div 2) + 1 + (number(//character/baseDamageBonus))"/>	
									</xsl:if>
									<xsl:if test="range!='-'">
										1d8	+ <xsl:value-of select="floor((number(//character/attributes/dexterity) + number(//character/inventory/shield/dexPenalty) - 10) div 2) + (number(//character/baseDamageBonus))"/>								
									</xsl:if>
								</xsl:if>
								<xsl:if test="@twohanded!='yes'">
									<xsl:if test="range='-'">										
										1d8	+ <xsl:value-of select="floor((number(//character/attributes/strength) - 10) div 4) + (number(//character/baseDamageBonus))"/>	
									</xsl:if>
									<xsl:if test="range!='-'">
										1d8	+ <xsl:value-of select="floor((number(//character/attributes/dexterity) + number(//character/inventory/shield/dexPenalty) - 10) div 2) + (number(//character/baseDamageBonus))"/>								
									</xsl:if>							
								</xsl:if>
							</xsl:if>
							<xsl:if test="damage='light'">
								<xsl:if test="@twohanded='yes'">
									<xsl:if test="range='-'">										
										1d6	+ <xsl:value-of select="floor((number(//character/attributes/strength) - 10) div 2) + 1 + (number(//character/baseDamageBonus))"/>	
									</xsl:if>
									<xsl:if test="range!='-'">
										1d6	+ <xsl:value-of select="floor((number(//character/attributes/dexterity) + number(//character/inventory/shield/dexPenalty) - 10) div 2) + (number(//character/baseDamageBonus))"/>								
									</xsl:if>
								</xsl:if>
								<xsl:if test="@twohanded!='yes'">
									<xsl:if test="range='-'">										
										1d6	+ <xsl:value-of select="floor((number(//character/attributes/strength) - 10) div 4) + (number(//character/baseDamageBonus))"/>	
									</xsl:if>
									<xsl:if test="range!='-'">
										1d6	+ <xsl:value-of select="floor((number(//character/attributes/dexterity) + number(//character/inventory/shield/dexPenalty) - 10) div 2) + (number(//character/baseDamageBonus))"/>								
									</xsl:if>							
								</xsl:if>
							</xsl:if>
						</td>
						<td>
							<xsl:value-of select="range"/>
						</td>
					</tr>
				</xsl:for-each>
			</table>
			<table class="weapons">
				<th class="name">Armor</th>
				<th>Dex Penalty</th>
				<th>Min Str</th>
				<th>AC Bonus</th>
				<xsl:for-each select="character/inventory/armour">
					<tr>
						<td class="name"><xsl:value-of select="character/inventory/armour/name"/></td>
						<td>0</td>
						<td><xsl:value-of select="character/inventory/armour/minStr"/></td>
						<td><xsl:value-of select="character/inventory/armour/acBonus"/></td>
					</tr>
				</xsl:for-each>
				<xsl:for-each select="character/inventory/shield">
					<tr>
						<td class="name"><xsl:value-of select="character/inventory/shield/name"/></td>
						<td><xsl:value-of select="character/inventory/shield/dexPenalty"/></td>
						<td>0</td>
						<td><xsl:value-of select="character/inventory/shield/acBonus"/></td>
					</tr>
				</xsl:for-each>
			</table>
			<table class="weapons">
				<th class="name">Items</th>
				<th>Descriptions</th>
			<xsl:for-each select="character/inventory/item">
				<tr>
					<td class="name">
						<xsl:value-of select="name"/>
					</td>
					<td>
						<xsl:value-of select="descrip"/>				
					</td>
				</tr>
			</xsl:for-each>	
			</table>
			<p class="header">
				Money
			</p>
			<p class="stats">
				Gold: <xsl:value-of select="character/inventory/money/gold"/>
				<span class="info"></span>
				Silver: <xsl:value-of select="character/inventory/money/silver"/>
				<span class="info"></span>
				Copper: <xsl:value-of select="character/inventory/money/copper"/>
			</p>
		</div>
	</div>
</body>
</html>
</xsl:template>

</xsl:stylesheet>

<!--
Add an if tree for attacks, bab and bdb

-->