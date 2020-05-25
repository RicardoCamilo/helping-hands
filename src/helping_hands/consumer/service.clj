(ns helping-hands.consumer.service
  (:require [helping-hands.consumer.core :as core]
           [io.pedestal.http :as http]
           [io.pedestal.http.route :as route]
           [io.pedestal.http.body-params :as body-params]
            [helping-hands.service :as generic-interceptors]))

(def common-interceptors [(body-params/body-params) http/html-body])

(def gen-events
  {:name ::events

   :enter
         (fn [context]
           (if (:response context)
             context
             (assoc context :response {:status 200 :body "SUCCESS"})))

   :error
         (fn [context ex-info]
           (assoc context
             :response {:status 500
                        :body (.getMessage ex-info)}))})

;; Tabular routes
(def routes #{["/consumers/:id" :get (conj common-interceptors `generic-interceptors/auth `core/validate-id `core/get-consumer `gen-events) :route-name :consumer-get]
              ["/consumers/:id" :put (conj common-interceptors `generic-interceptors/auth `core/validate-id `core/upsert-consumer `gen-events) :route-name :consumer-put]
              ["/consumers" :post (conj common-interceptors `generic-interceptors/auth `core/validate `core/create-consumer `gen-events) :route-name :consumer-post]
              ["/consumers/:id" :delete (conj common-interceptors `generic-interceptors/auth `core/validate-id `core/delete-consumer `gen-events) :route-name :delete-get]})

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :immutant
              ::http/port 8080})


