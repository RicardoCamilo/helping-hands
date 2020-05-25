(ns helping-hands.provider.service
  (:require [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http :as http]
            [helping-hands.service :as generic-interceptors]
            [helping-hands.provider.core :as core]))

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

(def routes #{["/providers/:id"
               :get (conj common-interceptors `generic-interceptors/auth `core/validate-id `core/upsert-provider `gen-events)
               :route-name :provider-get]
              ["/providers/:id"
               :put (conj common-interceptors `generic-interceptors/auth `core/validate-id `core/upsert-provider `gen-events)
               :route-name :provider-put]
              ["/providers/:id/rate"
               :put (conj common-interceptors `generic-interceptors/auth `core/validate-id `core/upsert-provider `gen-events)
               :route-name :provider-rate]
              ["/providers"
               :post (conj common-interceptors `generic-interceptors/auth `core/validate `core/create-provider `gen-events)
               :route-name :provider-post]
              ["/providers/:id"
               :delete (conj common-interceptors `generic-interceptors/auth `core/validate-id `core/delete-provider `gen-events)
               :route-name :provider-delete]})

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :immutant
              ::http/port 8080})
