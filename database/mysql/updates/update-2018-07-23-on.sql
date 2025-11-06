
DELETE FROM LookupListItem where value='CNO' AND lookupListId = (select id from LookupList where name = 'practitionerNoType');
INSERT INTO `LookupListItem` VALUES (\N,(select id from LookupList where name = 'practitionerNoType'),'OCP','Ontario College of Pharmacists (OCP)',3,1,'oscar',now());
INSERT INTO `LookupListItem` VALUES (\N,(select id from LookupList where name = 'practitionerNoType'),'CNORNP','RNP - College of Nurses of Ontario (CNO)',3,1,'oscar',now());
INSERT INTO `LookupListItem` VALUES (\N,(select id from LookupList where name = 'practitionerNoType'),'CNORN','RN - College of Nurses of Ontario  (CNO)',3,1,'oscar',now());
INSERT INTO `LookupListItem` VALUES (\N,(select id from LookupList where name = 'practitionerNoType'),'CNORPN','RPN - College of Nurses of Ontario  (CNO)',3,1,'oscar',now());
INSERT INTO `LookupListItem` VALUES (\N,(select id from LookupList where name = 'practitionerNoType'),'CMO','College of Midwives of Ontario',3,1,'oscar',now());


