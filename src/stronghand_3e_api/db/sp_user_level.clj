(ns stronghand-3e-api.db.sp-user-level
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "stronghand_3e_api/db/sp/user_level.sql")
