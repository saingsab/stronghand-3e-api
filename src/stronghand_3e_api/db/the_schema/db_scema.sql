-- :doc Create at 14APR2021 by Saing
-- :doc Modified at 14APR2021 by Saing
-- : All the schema table here

---: 1 User Information
CREATE TABLE IF NOT EXISTS USERS (
    ID VARCHAR (36) PRIMARY KEY,
    FIRST_NAME VARCHAR (50),
    MID_NAME VARCHAR (50),
    LAST_NAME VARCHAR (50),
    DESCRIPTION text,
    EMAIL   VARCHAR (50) UNIQUE,
    GENDER VARCHAR (1),
    PROFILE_IMG VARCHAR,
    WALLET VARCHAR (56) UNIQUE,
    SEED VARCHAR (256),
    PASSWORD VARCHAR (256) NOT NULL,
    TEMP_TOKEN TEXT,
    PIN VARCHAR (256),
    USER_STATUS VARCHAR (36),
    USER_LEVEL_ID VARCHAR (36) NOT NULL,
    NATIONALITY VARCHAR (50),
    OCCUPATION VARCHAR (50),
    PHONENUMBER VARCHAR (13) UNIQUE,
    DOCUMENTS_ID VARCHAR (36),
    STATUS_ID VARCHAR (36),
    ADDRESS VARCHAR,
    CREATED_AT TIMESTAMP NOT NULL default current_timestamp,
    UPDATED_AT TIMESTAMP,
    CREATED_BY VARCHAR (36),
    UPDATED_BY VARCHAR (36)
);

-- :2 User status
CREATE TABLE IF NOT EXISTS STATUS (
    ID VARCHAR (36) PRIMARY KEY,
    STATUS_NAME VARCHAR (10),
    CREATED_AT timestamp NOT NULL default current_timestamp,
    UPDATED_AT timestamp
);
-- : PRE DATA
-- :name insert-status-data :! :n 
INSERT INTO STATUS (ID, STATUS_NAME)
VALUES  ('cb6fcc67-0c03-405c-9426-ce1685208f68', 'inactive'),
        ('a1ca5b7b-d15c-44a3-9c06-c32793a16b89', 'active'),
        ('7acb0fc7-c873-4b1a-83ad-3942e57bb151', 'verifying'),
        ('17d67b96-3f29-4320-a945-b1fd86ff5486', 'verified'),
        ('aeb6c1a1-fd82-4a30-8520-29d3d9435375', 'disabled');

-- :3 DOCUMENTS table
CREATE TABLE IF NOT EXISTS DOCUMENTS (
    ID VARCHAR (36) PRIMARY KEY,
    DOCUMENTS_NO VARCHAR (50),
    DOCUMENTTYPE_ID VARCHAR (50),
    DOCUMENT_URI TEXT NOT NULL,
    FACE_URI TEXT NOT NULL,
    ISSUE_DATE TEXT NOT NULL,
    EXPIRE_DATE TEXT NOT NULL,
    CREATED_AT TIMESTAMP NOT NULL default current_timestamp,
    UPDATED_AT TIMESTAMP,
    CREATED_BY VARCHAR (36) UNIQUE,
    UPDATED_BY VARCHAR (36)
);

-- : 4 DOCUMENTTYPE table
CREATE TABLE IF NOT EXISTS DOCUMENTTYPE (
    ID VARCHAR (36) PRIMARY KEY,
    DOCUMENT_NAME VARCHAR (15),
    CREATED_AT timestamp NOT NULL default current_timestamp,
    UPDATED_AT timestamp
);

-- : PRE DATA
INSERT INTO DOCUMENTTYPE (ID, DOCUMENT_NAME)
VALUES  ('819fb558-1d8e-4254-ab0a-a65cc509b8ca', 'National ID'),
        ('c9b1552c-6424-4ce6-87d4-f45c8a3edc7a', 'Passport'),
        ('92f63b80-5a3c-486d-b41f-bc6983649b93', 'Driver License');
    
-- : 5 CAEGORIES TALBE
CREATE TABLE IF NOT EXISTS CAEGORIES(
    ID	            VARCHAR (36) PRIMARY KEY,
    CATEGORY_NAME	TEXT,
    CREATED_AT	    TIMESTAMP NOT NULL default current_timestamp,
    CREATED_BY	    VARCHAR (36),
    UPDATED_BY	    VARCHAR (36),
    UPDATED_AT	    TIMESTAMP
);

-- :PRE DATA OF CAEGORIES TABLE
INSERT INTO CAEGORIES (ID, CATEGORY_NAME)
VALUES  ('504e7e78-4c63-4d00-959b-55509a1a06f8', 'Mechanical'),
        ('e285725c-3958-4700-a751-f5e57e600a16', 'Electrical'),
        ('1a8bdfc5-a11a-42dd-b7e5-7d36cc605be4', 'Plumbing');

-- : 6 SUB_CAEGORIES TALBE
CREATE TABLE IF NOT EXISTS SUB_CAEGORIES(
    ID	            VARCHAR (36) PRIMARY KEY,
    SUB_CATEGORY_NAME	TEXT,
    CATEGORY_ID     VARCHAR (36),
    CREATED_AT	    TIMESTAMP NOT NULL default current_timestamp,
    CREATED_BY	    VARCHAR (36),
    UPDATED_BY	    VARCHAR (36),
    UPDATED_AT	    TIMESTAMP
);

-- :PRE DATA OF CAEGORIES TABLE
INSERT INTO SUB_CAEGORIES (ID, SUB_CATEGORY_NAME,CATEGORY_ID)
VALUES  ('504e7e78-4c63-4d00-959b-55509a1a06f9', 'Air Conditioner','504e7e78-4c63-4d00-959b-55509a1a06f8'),
        ('e285725c-3958-4700-a751-f5e57e600a17', 'Laundry Machine', '504e7e78-4c63-4d00-959b-55509a1a06f8'),
        ('1a8bdfc5-a11a-42dd-b7e5-7d36cc605be6', 'Refrigerator', '504e7e78-4c63-4d00-959b-55509a1a06f8');

-- : 7 ISSUES TABLE
CREATE TABLE IF NOT EXISTS ISSUES(
    ID	            VARCHAR (36) PRIMARY KEY,
    ISSUES_NAME	TEXT,
    PRICE NUMERIC,
    SUB_CATEGORY_ID     VARCHAR (36),
    CREATED_AT	    TIMESTAMP NOT NULL default current_timestamp,
    CREATED_BY	    VARCHAR (36),
    UPDATED_BY	    VARCHAR (36),
    UPDATED_AT	    TIMESTAMP
);

-- :PRE DATA OF ISSUES TABLE
INSERT INTO ISSUES (ID, ISSUES_NAME, PRICE, SUB_CATEGORY_ID)
VALUES  ('504e7e78-4c63-4d00-959b-55509a1a06zx', 'Sensor Problems', 5.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('504e7e78-4c63-4d00-959b-55509a1a0xyz', 'Electric Control Failure', 15.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('504e7e78-4c63-4d00-959b-55509a1a0syz', 'Refrigerant Leaks', 15.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('504e7e78-4c63-4d00-959b-55509a1a0tyz', 'Drainage Problems', 15.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('504e7e78-4c63-4d00-959b-55509a1a0zzz', 'Washer Not Turn On', 15.00, 'e285725c-3958-4700-a751-f5e57e600a17'),
        ('504e7e78-4c63-4d00-959b-55509a1a0yyy', 'Not Draining Properly', 15.00, 'e285725c-3958-4700-a751-f5e57e600a17'),
        ('504e7e78-4c63-4d00-959b-55509a1a0xxx', 'Washer Leaking Water', 15.00, 'e285725c-3958-4700-a751-f5e57e600a17'),
        ('504e7e78-4c63-4d00-959b-55509a1a0aaa', 'Water Leakage', 15.00, '1a8bdfc5-a11a-42dd-b7e5-7d36cc605be6');

-- : 8 ORDERS TALBE
CREATE TABLE IF NOT EXISTS ORDERS(
    ID	             VARCHAR (36) PRIMARY KEY,
    ISSUE_ID	     VARCHAR (36),
    SHIPPING_ADDRESS TEXT,
    USERS_ID	     VARCHAR (36),
    OTHERS           TEXT,
    TOTAL	         NUMERIC,
    ORDERS_ID	     VARCHAR (36),
    CREATED_AT	     TIMESTAMP NOT NULL default current_timestamp,
    CREATED_BY	     VARCHAR (36),
    UPDATED_BY	     VARCHAR (36),
    UPDATED_AT	     TIMESTAMP
);

-- : 9 IMAGES STOCK
CREATE TABLE IF NOT EXISTS IMAGE_STOCKS(
    ID	             VARCHAR (36) PRIMARY KEY,
    URL              VARCHAR,
    CREATED_AT	     TIMESTAMP NOT NULL default current_timestamp,
    CREATED_BY	     VARCHAR (36),
    UPDATED_BY	     VARCHAR (36),
    UPDATED_AT	     TIMESTAMP
);

-- : 10 ORDER STATUS
CREATE TABLE IF NOT EXISTS ORDER_STATUS(
    ID	                VARCHAR (36) PRIMARY KEY,
    ORDER_STATUS_DEC	TEXT
);

-- : PRE DATA ORDER STATUS
INSERT INTO ORDER_STATUS (ID, ORDER_STATUS_DEC)
VALUES  ('504e7e78-4c63-4d00-959b-55509a1a06f8', 'Place Order'),
        ('e285725c-3958-4700-a751-f5e57e600a16', 'Technician Accepted'),
        ('1a8bdfc5-a11a-42dd-b7e5-7d36cc605be4', 'On Our Way'),
        ('1a8bdfc5-a11a-42dd-b7e5-7d36cc605bee', 'Arrived and Fixing'),
        ('06eb36d5-8a81-477a-b4c1-2e72566559cc', 'Successfully Completed');

-- : 11 STAFF LEVEL
CREATE TABLE IF NOT EXISTS USER_LEVEL(
    ID	        VARCHAR (36) PRIMARY KEY,
    LEVEL_DEC	VARCHAR,
    CREATED_AT  TIMESTAMP NOT NULL default current_timestamp,
    CREATED_BY  VARCHAR (36),
    UPDATED_BY  VARCHAR (36),
    UPDATED_AT  TIMESTAMP
);

-- : PRE DATA SATFF LEVEL
INSERT INTO USER_LEVEL(ID, LEVEL_DEC)
VALUES  ('8fd6d854-4046-4242-ac01-9b820c2b513d', 'Technician'),
        ('01a8bd76-a0c0-41ff-aece-0614e5349ddd', 'Operation'),
        ('9ca90744-65a3-4879-84e9-908cdb3d63e7', 'User');