-- :name set-rates :! :n
INSERT INTO RATES(
    ID,
    RATE_STAR,  
    RATE_DEC,
    RATE_TO,
    CREATED_BY
)
VALUES (
    :ID,
    :RATE_STAR,  
    :RATE_DEC,
    :RATE_TO,
    :CREATED_BY
 );

--  :name get-rate-by-owner :? :1
SELECT  r.ID, 
        r.RATE_STAR, 
        r.RATE_DEC, 
        (u.FIRST_NAME, u.LAST_NAME) AS TECHNICIANS, 
        u.EMAIL, 
        u.PHONENUMBER
FROM RATES AS r
INNER JOIN USERS AS u
ON r.rate_to = u.ID
WHERE r.CREATED_BY = :CREATED_BY;

--  :name get-rate-from-date-to-date :? :1
SELECT  r.ID, 
        r.RATE_STAR, 
        r.RATE_DEC, 
        (u.FIRST_NAME, u.LAST_NAME) AS TECHNICIANS, 
        u.EMAIL, 
        u.PHONENUMBER,
        r.CREATED_AT
FROM RATES AS r
INNER JOIN USERS AS u
ON r.rate_to = u.ID
WHERE o.CREATED_AT  BETWEEN :FROM_DATE AND :TO_DATE;