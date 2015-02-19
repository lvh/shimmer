(ns shimmer.rules
  (:require [clojure.core.logic :as l]))

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
  (not= (l/run 1 [q]
          (conds (map (partial l/featurec req) features)))
        '()))
