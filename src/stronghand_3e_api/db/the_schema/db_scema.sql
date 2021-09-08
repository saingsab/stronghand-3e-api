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
    USER_LEVEL_ID VARCHAR (36) NOT NULL DEFAULT '9ca90744-65a3-4879-84e9-908cdb3d63e7',
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
    ID	             VARCHAR (36) PRIMARY KEY,
    CATEGORY_NAME	 TEXT,
    CATEGORY_NAME_KH TEXT,
    CREATED_AT	     TIMESTAMP NOT NULL default current_timestamp,
    CREATED_BY	     VARCHAR (36),
    UPDATED_BY	     VARCHAR (36),
    UPDATED_AT	     TIMESTAMP
);

-- :PRE DATA OF CAEGORIES TABLE
INSERT INTO CAEGORIES (ID, CATEGORY_NAME, CATEGORY_NAME_KH)
VALUES  ('504e7e78-4c63-4d00-959b-55509a1a06f8', 'Mechanical', 'មេកានិច'),
        ('e285725c-3958-4700-a751-f5e57e600a16', 'Electrical', 'អគ្គិសនី'),
        ('1a8bdfc5-a11a-42dd-b7e5-7d36cc605be4', 'Plumbing', 'ប្រព័ន្ធទឹក');

-- : 6 SUB_CAEGORIES TALBE
CREATE TABLE IF NOT EXISTS SUB_CAEGORIES(
    ID	                 VARCHAR (36) PRIMARY KEY,
    SUB_CATEGORY_NAME	 TEXT,
    SUB_CATEGORY_NAME_KH TEXT,
    CATEGORY_ID          VARCHAR (36),
    CREATED_AT	         TIMESTAMP NOT NULL default current_timestamp,
    CREATED_BY	         VARCHAR (36),
    UPDATED_BY	         VARCHAR (36),
    UPDATED_AT	         TIMESTAMP
);

-- :PRE DATA OF CAEGORIES TABLE
INSERT INTO SUB_CAEGORIES (ID, SUB_CATEGORY_NAME, SUB_CATEGORY_NAME_KH, CATEGORY_ID)
VALUES  ('504e7e78-4c63-4d00-959b-55509a1a06f9', 'Air Conditioner', 'ម៉ាស៊ីនត្រជាក់', '504e7e78-4c63-4d00-959b-55509a1a06f8'),
        ('e285725c-3958-4700-a751-f5e57e600a17', 'Laundry Machine', 'ម៉ាស៊ីនបោកគក់', '504e7e78-4c63-4d00-959b-55509a1a06f8'),
        ('1a8bdfc5-a11a-42dd-b7e5-7d36cc605be6', 'Refrigerator', 'ទូទឹកកក', '504e7e78-4c63-4d00-959b-55509a1a06f8'),
        ('e285725c-3958-4700-a751-f5e57e600a16', 'Electrical', 'អគ្គិសនី', 'e285725c-3958-4700-a751-f5e57e600a16'),
        ('1a8bdfc5-a11a-42dd-b7e5-7d36cc605be4', 'Plumbing', 'ប្រព័ន្ធទឹក', '1a8bdfc5-a11a-42dd-b7e5-7d36cc605be4');

-- : 7 ISSUES TABLE
CREATE TABLE IF NOT EXISTS ISSUES(
    ID	            VARCHAR (36) PRIMARY KEY,
    ISSUES_NAME     TEXT,
    ISSUES_NAME_KH  TEXT,
    PRICE           NUMERIC,
    SUB_CATEGORY_ID VARCHAR (36),
    CREATED_AT	    TIMESTAMP NOT NULL default current_timestamp,
    CREATED_BY	    VARCHAR (36),
    UPDATED_BY	    VARCHAR (36),
    UPDATED_AT	    TIMESTAMP
);

-- :PRE DATA OF ISSUES TABLE
INSERT INTO ISSUES (ID, ISSUES_NAME, ISSUES_NAME_KH, PRICE, SUB_CATEGORY_ID)
VALUES  ('504e7e78-4c63-4d00-959b-55509a1a06zx', 'Remote Control does not work', 'តេឡេមិនដំណើរការ', 15.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('504e7e78-4c63-4d00-959b-55509a1a06zo', 'AC Cleaning', 'សម្អាត និងថែទាំម៉ាស៊ីនត្រជាក់', 15.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('a3e000bc-d7bd-4e86-86cc-552810463f87', 'Drainage Leakage', 'មា៉សុីនត្រជាក់ស្រក់ទឹក', 15.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('9f47ade2-ec71-4d95-b0b4-3d8cebef8eda', 'Air Flow but not cool', 'កង្ហាមា៉សុីនដំេណើរការ  ែតមិន្រតជាក់', 15.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('7635f72f-9d19-4642-b9ed-288a0d979c1f', 'Frozen on coil', 'កកទុយោ ឬកកកញ្ជ្រែងមា៉សុីន្រតជាក់', 15.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('877e6aea-d472-4950-b0b1-f86a653fcba7', 'Timer alarm check', 'លោតភ្លើងសញ្ញាលើមុខម៉ាស៊ីន', 15.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('9ad3098a-7d83-47a0-a426-42e03ae6571e', 'Auto cut-off AC', 'ម៉ាស៊ីនដើរឈប់ៗដោយស្វ័យប្រវត្តិ', 15.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('64724ccd-2f82-4d45-a21a-5675aed61071', 'Toilet exhaust fan not working', 'កង្ហារបឺតខ្យល់បន្ទប់ទឹកមិនដំណើរការ', 15.00, '504e7e78-4c63-4d00-959b-55509a1a06f9'),
        ('504e7e78-4c63-4d00-959b-55509a1a0xyz', 'Electricel Distribution Maintennace', 'តំហែទាំទូភ្លើង', 25.00, 'e285725c-3958-4700-a751-f5e57e600a16'),
        ('93071641-0d4f-4a52-ab0e-dae551a22b5d', 'Circuit breaker overload, trips', 'លោតឌីសង់ទ័រ', 10.00, 'e285725c-3958-4700-a751-f5e57e600a16'),
        ('f87a55c2-08d7-486a-83e8-127409f381d8', 'Wiring connection loss', 'ខ្សែមិនមានចរន្ត', 10.00, 'e285725c-3958-4700-a751-f5e57e600a16'),
        ('44db0169-cb67-4e2a-b33d-67f369116723', 'Lightning protection check', 'ត្រួតពិនិត្យប្រព័ន្នការពាររន្ទះ', 35.00, 'e285725c-3958-4700-a751-f5e57e600a16'),
        ('6b44d34a-31b9-4253-86ac-8bfb3ad1b3d1', 'Grounding check', 'ការត្រួតពិនិត្យប្រព័ន្នខ្សែក្រោមដី', 35.00, 'e285725c-3958-4700-a751-f5e57e600a16'),
        ('71f39151-e794-4ec3-b86d-a9089dc1abdd', 'Light switches not working', 'កុងតាក់បិតបើកអំពូលមិនដំណើរការ', 3.00, 'e285725c-3958-4700-a751-f5e57e600a16'),
        ('dabc4fac-f13b-4f90-81ed-b506a68b5adb', 'Light bulbs burning out too often', 'អំពូលឧស្សាខូចជាញឹកញាប់', 5.00, 'e285725c-3958-4700-a751-f5e57e600a16'),
        ('b9d18c44-f651-4937-87c5-6425fe848838', 'Power socket not working', 'ព្រីភ្លើងមិនមានភ្លើង', 3.00, 'e285725c-3958-4700-a751-f5e57e600a16'),
        ('504e7e78-4c63-4d00-959b-55509a1a0syz', 'Water leaks from pipes or clogs', 'លិច ឫជ្រាបទឹកចេញពីទុយោ', 0.00, '1a8bdfc5-a11a-42dd-b7e5-7d36cc605be4'),
        ('62745294-d6ea-47d0-88a8-b1e8bcf11c6c', 'Clogged toilet', 'បង្គន់ស្ទះ', 0.00, '1a8bdfc5-a11a-42dd-b7e5-7d36cc605be4'),
        ('5daa7210-85d2-4cf1-b0df-86683665a459', 'Broken faucet', 'ខូចរ៉ូប៊ីណេ', 0.00, '1a8bdfc5-a11a-42dd-b7e5-7d36cc605be4'),
        ('b1ffa66e-bcc6-4881-9339-90aacbf06015', 'Install motor water pressure', 'តម្លើងម៉ូទ័ររុញសំពាធទឹក', 0.00, '1a8bdfc5-a11a-42dd-b7e5-7d36cc605be4'),
        ('2f9d2b20-fda7-420d-8a14-b81fccb9083c', 'Install and repair Hot water system', 'តម្លើង និងជួសជុលប្រព័ន្ធទឹកក្តៅ', 0.00, '1a8bdfc5-a11a-42dd-b7e5-7d36cc605be4'),
        ('57277f26-bf79-4168-9d41-5d09f30b8f02', 'Machine does not work', 'ម៉ាស៊ីនមិនដំណើរការ', 0.00, 'e285725c-3958-4700-a751-f5e57e600a17'),
        ('b3016fdb-05ed-4e4b-b90a-e66fa9e72911', 'Machine does not rotate', 'ម៉ាស៊ីនមិនវិល ឫក្រឡុក់', 0.00, 'e285725c-3958-4700-a751-f5e57e600a17'),
        ('c5516309-9051-4ef1-92c7-770890e999f4', 'Door machine not open', 'ទ្វារមិនបើក', 0.00, 'e285725c-3958-4700-a751-f5e57e600a17'),
        ('48e99d30-4ea3-4ee2-bec9-9498f655c87b', 'Non-drying machine does not emit much
heat', 'ម៉ាស៊ីនមិនសម្ងួតមិនសូវចេញកម្តៅ', 0.00, 'e285725c-3958-4700-a751-f5e57e600a17'),
        ('5a4003aa-bdd2-4767-a3ff-358c7faeefca', 'The Drain of machine does not work', 'ម៉ាស៊ីនបោកខោអាវមិនបង្ហូរទឹក', 0.00, 'e285725c-3958-4700-a751-f5e57e600a17'),
        ('42a355f8-0f72-4f5e-8a93-5ae33c5dc8a3', 'Machine leak Water', 'ម៉ស៊ីនបោកខោអាវជ្រាបទឹក', 0.00, 'e285725c-3958-4700-a751-f5e57e600a17'),
        ('73e1acf9-b419-43fe-845a-c6c45da26bbd', 'Machine Excessive vibration', 'រំញ័រហួសក្រមិត', 0.00, 'e285725c-3958-4700-a751-f5e57e600a17');

-- : 8 ORDERS TALBE
CREATE TABLE IF NOT EXISTS ORDERS(
    ID	             VARCHAR (36) PRIMARY KEY,
    ISSUE_ID	     VARCHAR (36),
    OTHERS           TEXT,
    IMAGES           TEXT[],
    LOCATIONS        TEXT,
    TOTAL	         NUMERIC,
    ORDER_STATUS     VARCHAR (36) NOT NULL default '504e7e78-4c63-4d00-959b-55509a1a06f8',
    APPOINTMENT_AT	 BIGINT,
    SOLUTIONS        TEXT,
    TECHNICIANS      TEXT[],
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
    ORDER_STATUS_DEC	TEXT,
    ORDER_STATUS_DEC_KH TEXT,
    IDEX                NUMERIC
);

-- : PRE DATA ORDER STATUS
INSERT INTO ORDER_STATUS (ID, ORDER_STATUS_DEC, ORDER_STATUS_DEC_KH, IDEX)
VALUES  ('504e7e78-4c63-4d00-959b-55509a1a06f8', 'Place Order', 'បញ្ជា កម្មង', 0),
        ('e285725c-3958-4700-a751-f5e57e600a16', 'Technician Accepted', 'អ្នកបច្ចេកទេសទទួលយក', 1),
        ('1a8bdfc5-a11a-42dd-b7e5-7d36cc605be4', 'On Our Way', 'កំពុងនៅតាមផ្លូវ', 2),
        ('1a8bdfc5-a11a-42dd-b7e5-7d36cc605bee', 'Arrived and Fixing', 'បានមកដល់ហើយកំពុងជួសជុល', 3),
        ('06eb36d5-8a81-477a-b4c1-2e72566559cc', 'Successfully Completed', 'បានបញ្ចប់ដោយជោគជ័យ', 4),
        ('06eb36d5-8a81-477a-b4c1-2e72566559dd', 'Cancelled', 'បានលុបចោល', 5);

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

-- : 12 RATES Table
CREATE TABLE IF NOT EXISTS RATES(
    ID	        VARCHAR (36) PRIMARY KEY,
    RATE_STAR	NUMERIC,
    RATE_DEC	VARCHAR,
    RATE_TO     VARCHAR (36) NOT NULL,
    CREATED_AT  TIMESTAMP NOT NULL default current_timestamp,
    CREATED_BY  VARCHAR (36)
);