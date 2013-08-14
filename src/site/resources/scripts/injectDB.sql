--type param
insert into TYPE_PARAM (id, NOM, version) values (nextval('typeparam_sequence'),'ACTIVITE',0);
insert into TYPE_PARAM (id, NOM, version) values (nextval('typeparam_sequence'),'ACTION',0);
insert into TYPE_PARAM (id, NOM, version) values (nextval('typeparam_sequence'),'CONTROLEUR',0);
insert into TYPE_PARAM (id, NOM, version) values (nextval('typeparam_sequence'),'DOCUMENT',0);
insert into TYPE_PARAM (id, NOM, version) values (nextval('typeparam_sequence'),'SUITE_DONNEE',0);
--param
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'CANTINE MUNICIPALE',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'CENTRE DE VACANCE ET DE LOISIRS',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'COMMERCE ITINERANT',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'DEBIT DE BOISSONS',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'EPICERIE, SUPERETTE',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'FABRICATION A CARACTERE ARTISANAL DE BOISSONS',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'FABRICATION DE PLATS CUISINES A L''AVANCE',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'FABRICATION DE PLATS CUISINES A L''AVANCE + COMMERCE ITINERANT',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'FABRICATION DE PLATS CUISINES A L''AVANCE + EPICERIE',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'GARDERIE AGREEE',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'GARDERIE SAUVAGE',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'MARCHE MUNICIPAL',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'NAKAMAL',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'PIZZERIA',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'PATISSERIE, BISCUITERIE, BOULANGERIE',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'RESTAURATION COMMERCIALE',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'TORREFACTION ET CONDITIONNEMENT DE CAFE',1,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'CONTRÔLE',2,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'ETAT DES LIEUX',2,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'VISITE CONFORMITE',2,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'PLAINTES/TIAC',2,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'DEBIT BOISSON',2,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'CARTE CIRCU',2,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'ENTRETIEN INFO',2,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'ENTR TEL',2,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'ENTR AVIS/PLAN',2,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'CAFAT',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'CCAS',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'DAE',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'DB',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'DEA',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'DITTT',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'DPASS',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'DPM',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'GM',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'OPJ',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'PAF',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'PS',3,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'COURRIER',4,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'FICHE STATUT',4,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'NOTE PERMIS',4,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'NOTE CIRCU',4,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'NOTE FERMETURE',4,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'NOTE GARDERIE',4,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'ARRÊTE REOUVERTURE',4,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'SANS (aucune sanction)',5,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'CONF (conformité)',5,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'AV (avertissement)',5,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'MD (mise en demeure)',5,'t',0);
insert into param (id, nom, type_param, actif, version) values (nextval('param_sequence'),'FERM (fermeture)',5,'t',0);
--droits
INSERT INTO DROIT (ID,NOM,VERSION) VALUES (nextval('droit_sequence'),'Controleur' ,0);
INSERT INTO DROIT (ID,NOM,VERSION) VALUES (nextval('droit_sequence'),'Administrateur' ,0);
--controleurs siale
INSERT INTO CONTROLEURSIALE (ID,DISPLAYNAME,USERNAME,VERSION,actif) VALUES (nextval('controleursiale_sequence'),'BOURDIL luc','boulu72' ,0 ,'t');
INSERT INTO CONTROLEURSIALE (ID,DISPLAYNAME,USERNAME,VERSION,actif) VALUES (nextval('controleursiale_sequence'),'COTIGNOLA mickael', 'cotmi78' ,0 ,'t');
INSERT INTO CONTROLEURSIALE (ID,DISPLAYNAME,USERNAME,VERSION,actif) VALUES (nextval('controleursiale_sequence'),'MONNIER jennifer','rivje87' ,0 ,'t');
--droits (nom1 :  controleur, nom 2, admin, nom3 admin et controleur
INSERT INTO CONTROLEURSIALE_DROITS (CONTROLEURSIALE,DROITS) VALUES (1 ,1 );
INSERT INTO CONTROLEURSIALE_DROITS (CONTROLEURSIALE,DROITS) VALUES (1 ,2 );
INSERT INTO CONTROLEURSIALE_DROITS (CONTROLEURSIALE,DROITS) VALUES (2 ,2 );
INSERT INTO CONTROLEURSIALE_DROITS (CONTROLEURSIALE,DROITS) VALUES (3 ,1 );
--Les baremes
INSERT INTO bareme (id,date_creation,nom,version) VALUES (nextval('bareme_sequence'),'2013-05-03 16:16:06.110','Nouveau Bareme',0);
INSERT INTO note_groupe (id,nom,ponderation,version,bareme) VALUES (nextval('notegroupe_sequence'),'grp1',0.4,0,1);
INSERT INTO note_groupe (id,nom,ponderation,version,bareme) VALUES (nextval('notegroupe_sequence'),'grp2',0.6,0,1);
INSERT INTO note_critere (id,nom,ponderation,version,note_groupe) VALUES (nextval('notecritere_sequence'),'grp1crt1',0.8,0,1);
INSERT INTO note_critere (id,nom,ponderation,version,note_groupe) VALUES (nextval('notecritere_sequence'),'grp1crt2',0.2,0,1);
INSERT INTO note_critere (id,nom,ponderation,version,note_groupe) VALUES (nextval('notecritere_sequence'),'grp2crt3',0.7,0,2);
INSERT INTO note_critere (id,nom,ponderation,version,note_groupe) VALUES (nextval('notecritere_sequence'),'grp2crt2',0.2,0,2);
INSERT INTO note_critere (id,nom,ponderation,version,note_groupe) VALUES (nextval('notecritere_sequence'),'grp2crt1',0.1,0,2);