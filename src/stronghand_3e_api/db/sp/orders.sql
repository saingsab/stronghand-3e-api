-- :name orders :! :n
INSERT INTO ORDERS (
    ID,
    ISSUE_ID,
    OTHERS,
    IMAGES,
    LOCATIONS,
    TOTAL,
    APPOINTMENT_AT,
    CREATED_BY
) VALUES (
    :ID,
    :ISSUE_ID,
    :OTHERS,
    :IMAGES,
    :LOCATIONS,
    :TOTAL,
    :APPOINTMENT_AT,
    :CREATED_BY
);

-- :name get-order-by-id :? :1
SELECT * FROM ORDERS
WHERE ID = :ID;

-- :name get-issue-by-id :? :1
SELECT * FROM ISSUES
WHERE ID = :ID;

-- :name get-order-top :? :*
SELECT  o.ID, 
        i.ISSUES_NAME,
        o.OTHERS, 
        o.IMAGES, 
        o.LOCATIONS, 
        o.TOTAL, 
        os.ORDER_STATUS_DEC, 
        o.APPOINTMENT_AT, 
        o.SOLUTIONS, 
        o.TECHNICIANS, 
        o.CREATED_AT
FROM ORDERS as o
INNER JOIN ISSUES AS i
ON o.ISSUE_ID = i.ID
INNER JOIN ORDER_STATUS AS os
ON o.ORDER_STATUS = os.ID
WHERE o.CREATED_BY = :CREATED_BY
ORDER BY o.CREATED_AT ASC
LIMIT 10;

-- :name get-order-by-id :? :1
SELECT  o.ID, 
        i.ISSUES_NAME, 
        o.OTHERS, 
        o.IMAGES, 
        o.LOCATIONS, 
        o.TOTAL, 
        os.ORDER_STATUS_DEC, 
        o.APPOINTMENT_AT, 
        o.SOLUTIONS, 
        o.TECHNICIANS, 
        o.CREATED_AT
FROM ORDERS as o
INNER JOIN ISSUES AS i
ON o.ISSUE_ID = i.ID
INNER JOIN ORDER_STATUS AS os
ON o.ORDER_STATUS = os.ID
WHERE o.ID = :ID
ORDER BY o.CREATED_AT ASC;

-- :name get-order-by-status :? :1
SELECT  o.ID, 
        i.ISSUES_NAME, 
        o.OTHERS, 
        o.IMAGES, 
        o.LOCATIONS, 
        o.TOTAL, 
        os.ORDER_STATUS_DEC, 
        o.APPOINTMENT_AT, 
        o.SOLUTIONS, 
        o.TECHNICIANS, 
        o.CREATED_AT
FROM ORDERS as o
INNER JOIN ISSUES AS i
ON o.ISSUE_ID = i.ID
INNER JOIN ORDER_STATUS AS os
ON o.ORDER_STATUS = os.ID
WHERE o.ORDER_STATUS = :ORDER_STATUS
ORDER BY o.CREATED_AT ASC;

-- :name get-order-from-date-to-date :? :*
SELECT  o.ID, 
        i.ISSUES_NAME, 
        o.OTHERS, 
        o.IMAGES, 
        o.LOCATIONS, 
        o.TOTAL, 
        os.ORDER_STATUS_DEC, 
        o.APPOINTMENT_AT, 
        o.SOLUTIONS, 
        o.TECHNICIANS, 
        o.CREATED_AT
FROM ORDERS as o
INNER JOIN ISSUES AS i
ON o.ISSUE_ID = i.ID
INNER JOIN ORDER_STATUS AS os
ON o.ORDER_STATUS = os.ID
WHERE o.CREATED_AT  BETWEEN :FROM_DATE AND :TO_DATE;

-- :name update-orders :! :n
-- :doc Update order status
UPDATE ORDERS
SET SOLUTIONS = :SOLUTIONS,
    TOTAL = :TOTAL,
    ORDER_STATUS  = :ORDER_STATUS,
    UPDATED_BY = :UPDATED_BY,
    UPDATED_AT = CURRENT_TIMESTAMP
WHERE ID = :ID;

-- :name assign-technician :! :n
-- :doc Assign Technician
UPDATE ORDERS
SET TECHNICIANS = :TECHNICIANS,
    ORDER_STATUS  = :ORDER_STATUS,
    UPDATED_BY = :UPDATED_BY,
    UPDATED_AT = CURRENT_TIMESTAMP
WHERE ID = :ID;

-- :name get-order-status :? :1
SELECT * FROM ORDER_STATUS
WHERE ORDER_STATUS_DEC = :ORDER_STATUS_DEC;

-- :name get-all-order-status :? :*
SELECT * FROM ORDER_STATUS;

-- :name get-user-level :? :1
SELECT * FROM USER_LEVEL
WHERE LEVEL_DEC = :LEVEL_DEC;