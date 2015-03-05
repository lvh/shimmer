(ns shimmer.rules
  (:require [clojure.core.logic :as l]))

(defn match
  "Checks if the request is allowed."
  [req]
  (seq (l/run 1 [q]
          (l/conde
           [(l/featurec req {:request-method :get})]
           [(l/featurec req {:request-method :post
                             :headers {"x-some-header" "the right header value"}})]
           [(l/featurec req {:request-method :post})
            (l/featurec req {:headers {"x-some-header" "another right header value"}})]))))

(defn ^:private conds
  "Like conde, but a function."
  [goals]
  (if (empty? goals)
    l/fail
    (l/conde [(first goals)]
             [(conds (rest goals))])))

(defn match*
  "Checks if a request matches a set of features."
  [req features]
  (seq (l/run 1 [q]
         (conds (map (partial l/featurec req) features)))))
