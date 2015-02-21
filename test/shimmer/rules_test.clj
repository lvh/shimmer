(ns shimmer.rules-test
  (:require [shimmer.rules :as r]
            [clojure.test :refer :all]
            [clojure.core.logic :as l]
            [ring.mock.request :as m]))

(deftest match-tests
  (testing "GET anywhere matches"
    (are [req] (r/match req)
         (m/request :get "/")
         (-> (m/request :get "/whatever/xyz")
             (m/header "X-Some-Header" "some header value")
             (m/header "X-Some-Other-Header" "some other header value"))))
  (testing "POST / match, but only if they have the right headers"
    (are [req] (r/match req)
         (-> (m/request :post "/")
             (m/header "X-Some-Header" "the right header value"))
         (-> (m/request :post "/")
             (m/header "X-Some-Header" "the right header value")
             (m/header "Yet-Another-Header" "doesn't affect the match")))
    (are [req] (not (r/match req))
         (m/request :post "/")
         (-> (m/request :post "/")
             (m/header "X-Some-Header" "not the right header value")))))
