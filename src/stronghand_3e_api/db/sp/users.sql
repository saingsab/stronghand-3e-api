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

-- :name get-users-by-id :? :1
SELECT * FROM USERS
WHERE ID = :ID;

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

-- :name user-activation :! :n
UPDATE USERS
SET TEMP_TOKEN = :TEMP_TOKEN,
    STATUS_ID = :STATUS_ID
WHERE ID = :ID;

-- :name user-activation-by-phone :! :n
UPDATE USERS
SET TEMP_TOKEN = :TEMP_TOKEN,
    STATUS_ID = :STATUS_ID
WHERE PHONENUMBER = :PHONENUMBER;

-- :name get-users-token :? :1
SELECT ID, TEMP_TOKEN
FROM USERS 
WHERE ID = :ID;

-- :name get-users-token-phone :? :1
SELECT PHONENUMBER, TEMP_TOKEN
FROM USERS 
WHERE PHONENUMBER = :PHONENUMBER;

-- :name user-activation :! :n
UPDATE USERS
SET TEMP_TOKEN = :TEMP_TOKEN,
    STATUS_ID = :STATUS_ID
WHERE ID = :ID;

-- :name user-activation-by-phone :! :n
UPDATE USERS
SET TEMP_TOKEN = :TEMP_TOKEN,
    STATUS_ID = :STATUS_ID
WHERE PHONENUMBER = :PHONENUMBER;

-- :name update-status :! :n
UPDATE USERS
SET NATIONALITY = :NATIONALITY,
    OCCUPATION  = :OCCUPATION,
    ADDRESS = :ADDRESS,
    STATUS_ID = :STATUS_ID
where ID = :ID;

-- :name setup-user-profile :! :n
-- :doc Update status after setup profile, last modified:20201207SAING
UPDATE USERS
SET FIRST_NAME  = :FIRST_NAME,
    MID_NAME    = :MID_NAME,
    LAST_NAME   = :LAST_NAME,
    GENDER      = :GENDER,
    PROFILE_IMG = :PROFILE_IMG,
    ADDRESS     = :ADDRESS,
    STATUS_ID   = :STATUS_ID
WHERE ID = :ID;

-- :name reset-password :! :n
UPDATE USERS
SET PASSWORD = :PASSWORD, 
    TEMP_TOKEN = 0
WHERE PHONENUMBER   = :PHONENUMBER;

-- :name reset-password-by-mail :! :n
UPDATE USERS
SET PASSWORD = :PASSWORD, 
    TEMP_TOKEN = 0
WHERE EMAIL   = :EMAIL;

-- :name change-password :! :n
UPDATE USERS
SET PASSWORD = :PASSWORD
WHERE ID   = :ID;

-- :name update-temp :! :n
UPDATE USERS
SET TEMP_TOKEN = :TEMP_TOKEN 
WHERE PHONENUMBER   = :PHONENUMBER;

-- :name update-temp-mail :! :n
UPDATE USERS
SET TEMP_TOKEN = :TEMP_TOKEN,
    STATUS_ID  = :STATUS_ID
WHERE EMAIL   = :EMAIL;

-- :name update-phone-by-id :! :n
UPDATE USERS
SET PHONENUMBER = :PHONENUMBER 
WHERE ID   = :ID;

-- :name set-phonenumber-by-id :! :n
UPDATE USERS
SET PHONENUMBER = :PHONENUMBER,
    TEMP_TOKEN  = :TEMP_TOKEN
WHERE ID   = :ID;

-- :name find-user-by-name :? :*
-- :doc need to filter only tech
SELECT ID, FIRST_NAME, MID_NAME, LAST_NAME, DESCRIPTION, EMAIL, GENDER, PROFILE_IMG, PHONENUMBER FROM USERS
WHERE FIRST_NAME LIKE :FIRST_NAME; 

-- :name find-user-by-email :? :1
-- :doc need to filter only tech
SELECT ID, FIRST_NAME, MID_NAME, LAST_NAME, DESCRIPTION, EMAIL, GENDER, PROFILE_IMG, PHONENUMBER FROM USERS
WHERE EMAIL = :EMAIL; 

-- :name find-user-by-phone :? :1
-- :doc need to filter only tech
SELECT ID, FIRST_NAME, MID_NAME, LAST_NAME, DESCRIPTION, EMAIL, GENDER, PROFILE_IMG, PHONENUMBER FROM USERS
WHERE PHONENUMBER = :PHONENUMBER; 
