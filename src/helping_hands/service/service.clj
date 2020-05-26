(ns helping-hands.service.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]))

;; body-params converte de acordo com o content-type
(def common-interceptors [(body-params/body-params) http/html-body])

(def routes #{["/services/:id"
               :get (conj common-interceptors `generic-interceptors/auth `core/validate-id `core/get-service `gen-events)
               :route-name :service-get]
              ["/services/:id"
               :put (conj common-interceptors `generic-interceptors/auth `core/validate-id `core/upsert-service `gen-events)
               :route-name :service-put]
              ["/services/:id/rate"
               :put (conj common-interceptors `generic-interceptors/auth `core/validate-id `core/upsert-service `gen-events)
               :route-name :service-put]
              ["/services"
               :post (conj common-interceptors `generic-interceptors/auth `core/validate `core/upsert-service `gen-events)
               :route-name :service-post]
              ["/services/:id"
               :delete (conj common-interceptors `generic-interceptors/auth `core/validate-id `core/upsert-service `gen-events)
               :route-name :service-delete]})

(def service {})


