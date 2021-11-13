(ns clojure-lanterna-snake.domain.aux-matchers
  (:require [clojure.test :refer :all]
            [matcher-combinators.test]
            [clojure-lanterna-snake.domain.snake :as domain.snake])
  )

(defn snake-match-moviment
  [snake changed-direction snake-move-diff]
  (let [snake-after-move    (domain.snake/move snake changed-direction)]
    (do (is (match? snake-move-diff
                    snake-after-move)))
    snake-after-move))

(defn snake-match-moviment-position
  [snake changed-direction expected-position]

  (let [snake-move-diff #:snake{:body             [expected-position]
                                :current-position expected-position}]
    (snake-match-moviment snake changed-direction snake-move-diff)))
