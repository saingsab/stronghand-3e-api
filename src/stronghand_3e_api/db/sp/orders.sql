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

-- :name update-orders-status :! :n
-- :doc Update order status
UPDATE ORDERS
SET ORDER_STATUS  = :STATUS_ID
WHERE ID = :ID

-- :name get-user-level :? :1
SELECT * FROM USER_LEVEL
WHERE LEVEL_DEC = :LEVEL_DEC;