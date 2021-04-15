(ns stronghand-3e-api.db.sp-users
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "stronghand_3e_api/db/sp/users.sql")