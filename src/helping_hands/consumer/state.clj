(ns helping-hands.consumer.state
  "Initializes State for Consumer Service"
  (:require [mount.core :as mount]
            [helping-hands.consumer.persistence :as p]))

(mount/defstate consumerdb
          :start (p/create-consumer-database)
          :stop (.stop consumerdb))
