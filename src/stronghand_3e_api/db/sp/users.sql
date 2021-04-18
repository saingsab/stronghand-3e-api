-- :name register-users-by-phone :! :n 
INSERT INTO USERS (ID, PHONENUMBER, PASSWORD, TEMP_TOKEN, STATUS_ID)
VALUES (:ID, :PHONENUMBER, :PASSWORD, :TEMP_TOKEN, :STATUS_ID);

-- :name register-users-by-mail :! :n 
INSERT INTO USERS (ID, EMAIL, PASSWORD, TEMP_TOKEN, STATUS_ID)
VALUES (:ID, :EMAIL, :PASSWORD, :TEMP_TOKEN, :STATUS_ID);

-- :name get-users-by-phone :? :1
SELECT * FROM USERS 
WHERE PHONENUMBER = :PHONENUMBER;

-- :name get-users-by-mail :? :1
SELECT * FROM USERS 
WHERE EMAIL = :EMAIL;

-- :name get-all-users-by-id :? :1
SELECT * FROM USERS 
WHERE ID = :ID;

-- :name update-temp :! :n
UPDATE USERS
SET TEMP_TOKEN = :TEMP_TOKEN 
WHERE PHONENUMBER   = :PHONENUMBER;

-- :name update-temp-mail :! :n
UPDATE USERS
SET TEMP_TOKEN = :TEMP_TOKEN,
    STATUS_ID  = :STATUS_ID
WHERE EMAIL   = :EMAIL;