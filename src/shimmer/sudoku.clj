(ns shimmer.sudoku
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer :all]
            [clojure.core.logic.fd :as fd]))

(defn get-square [rows x y]
  (for [x (range x (+ x 3))
        y (range y (+ y 3))]
    (get-in rows [x y])))

(defn apply-hints
  [vars hints]
  (if (seq vars)
    (let [hint (first hints)]
      (all
       (if-not (zero? hint)
         (== (first vars) hint)
         succeed)
       (apply-hints (next vars) (next hints))))
    succeed))

(defn sudokufd
  [n hints]
  (let [vars (repeatedly 81 lvar)
        rows (->> vars
                  (partition 9)
                  (map vec)
                  (into []))
        cols (apply map vector rows)
        sqs  (for [x (range 0 9 3)
                   y (range 0 9 3)]
               (get-square rows x y))]
    (run n [q]
      (== q vars)
      (everyg #(fd/in % (fd/domain 1 2 3 4 5 6 7 8 9)) vars)
      (apply-hints vars hints)
      (everyg fd/distinct rows)
      (everyg fd/distinct cols)
      (everyg fd/distinct sqs))))

(def hard-sudoku
  [8 0 0  0 0 0  0 0 0
   0 0 3  6 0 0  0 0 0
   0 7 0  0 9 0  2 0 0

   0 5 0  0 0 7  0 0 0
   0 0 0  0 4 5  7 0 0
   0 0 0  1 0 0  0 3 0

   0 0 1  0 0 0  0 6 8
   0 0 8  5 0 0  0 1 0
   0 9 0  0 0 0  4 0 0])

(defn solve-example
  [n hints]
  (let [solns (sudokufd n hints)]
    [(count solns)
     (map #(partition 9 %) solns)]))

(def first-soln
  (solve-example 1 hard-sudoku))

(def second-soln
  (solve-example 2 hard-sudoku))

(def third-soln
  (solve-example 3 (repeat 81 0)))
