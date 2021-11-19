(ns clojure-lanterna-snake.domain.snake-matchers
  (:require
   [clojure-lanterna-snake.domain.snake :as domain.snake]
   [clojure.test :refer :all]
   [matcher-combinators.test :refer :all])
  )

(defn match-snake-after-moviment
  [snake snake-move-diff]
  (let [snake-after-move    (domain.snake/move snake)]
    (is (match? snake-move-diff
                    snake-after-move))
    snake-after-move))

(defn snake-match-moviment-position
  [snake expected-position]

  (let [snake-move-diff #:snake{:body [expected-position]}]
    (match-snake-after-moviment snake snake-move-diff)))
