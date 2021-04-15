-- :name get-status-by-name :? :1
SELECT * FROM STATUS 
WHERE STATUS_NAME = :STATUS_NAME;