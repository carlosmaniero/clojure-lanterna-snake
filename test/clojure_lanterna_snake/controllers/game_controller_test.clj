(ns clojure-lanterna-snake.controllers.game-controller-test
  (:require [clojure.test :refer :all]
            [clojure-lanterna-snake.views.snake :as views.snake]
            [clojure-lanterna-snake.domain.snake :as domain.snake]
            [clojure-lanterna-snake.controllers.game-controller :as game-controller]
            [matcher-combinators.test]))

(def my-world {:width 81 :height 21})
(def my-game (game-controller/create-game my-world :moving/up))

(deftest creating-a-game
  (testing "puts the snake at the middle of the screen"
    (let
     [expected-snake #:snake{:current-position {:x 40 :y 10}
                             :moving-direction :moving/up}
      expected-frame {:game-view/frame [{:pixel/position {:x 40 :y 10}}]}
      expected-game  {:game-context/game  {:game/snake expected-snake}
                      :game-context/view  expected-frame}]
      (is (match? expected-game my-game)))))
