(ns shimmer.core
  (:gen-class)
  (:require [shimmer.rules :refer [match]]
            [taoensso.timbre :refer [info spy]]
            [aleph.http :refer [start-server request]]))

(defn build-handler
  [target-host target-port]
  (let []
    (fn [incoming-request]
      (if (match (spy incoming-request))
        (let [modified-request (-> incoming-request
                                   (dissoc :scheme) ;; hack
                                   (assoc :host target-host
                                          :port target-port
                                          :throw-exceptions false))]
          (spy (request (spy modified-request))))
        {:status 403 ;; Forbidden
         :headers {"content-type" "text/plain"}
         :body "Doesn't match!"}))))

(defn -main
  "Start a shimmer server"
  [& args]
  (let [handler (build-handler "localhost" 8900)]
    (start-server handler {:port 7467})
    (info "Shimmering"))) ;; 7467 == SHMR
