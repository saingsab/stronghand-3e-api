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

-- :name get-price :