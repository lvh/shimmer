(ns shimmer.rules-test
  (:require [shimmer.rules :as r]
            [clojure.test :refer :all]
            [clojure.core.logic :as l]))

(deftest match*-tests
  (testing "exact match"
    (is (r/match* {:request-method :post
                   :headers {"host" "localhost"}}
                  [{:request-method :post
                    :headers {"host" "localhost"}}])))
  (testing "one matching clause is enough"
    (is (r/match* {:request-method :post
                   :headers {"host" "localhost"}}
                  [{:request-method :post
                    :headers {"host" "localhost"}}
                   {:sure :whatever :some :stuff}])))
  (testing "no match"
    (is (not (r/match* {:request-method :post
                        :headers {"host" "localhost"}}
                       [])))))
