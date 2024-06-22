INSERT INTO USERS (USERNAME, PASSWORD, ENABLED) VALUES('apurva','test',true);
INSERT INTO USERS (USERNAME, PASSWORD, ENABLED) VALUES('vishakha','test',true);

INSERT INTO authorities(username, authority) VALUES('apurva','ROLE_USER');
INSERT INTO authorities(username, authority) VALUES('vishakha','ROLE_ADMIN');