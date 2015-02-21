(ns shimmer.monkey
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer :all]))

;; A monkey has to get on a box to get a banana.
;; States look like: (MonkeyPosX, MonkeyPosY, BoxPos, MonkeyHasBanana)
;; The box is by the window. The monkey is by the door.
(def initial-state
  [:at-door :on-floor :at-window :has-not])

;; The monkey is going to have to push the box, then get on the box,
;; then grab the banana.

(defne moveo
  [before action after]
  ;; If the box is in the middle, with the monkey on top of the box,
  ;; it can grab the banana:
  ([[:middle :on-box :middle :has-not]
    :grab
    [:middle :on-box :middle :has]])
  ;; If the monkey is on the floor, it can climb onto the box:
  ([[pos :on-floor pos has]
    :climb
    [pos :on-box pos has]])
  ;; If the monkey is on the floor, it can push the box around:
  ([[pos1 :on-floor pos1 has]
    :push
    [pos2 :on-floor pos2 has]])
  ;; If the monkey is on the floor, it can walk around:
  ([[pos1 :on-floor box has]
    :walk
    [pos2 :on-floor box has]]))

(defne cangeto
  [state out]
  ([[_ _ _ :has] true])
  ([_ _] (fresh [action next-state]
           (moveo state action next-state)
           (cangeto next-state out))))

;; Can the monkey get the banana?
(def can-the-monkey-get-the-banana
  (run 1 [q]
    (cangeto initial-state q)))

;; What could the monkey do in three steps?
(def what-can-the-monkey-do-in-3-steps
  (run 10 [q]
    (fresh [a b c ;; steps
            i j k] ;; states
      (moveo initial-state a i)
      (moveo i b j)
      (moveo j c k)
      (== q [initial-state a i b j c k]))))
