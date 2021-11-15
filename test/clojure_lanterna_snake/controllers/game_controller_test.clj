(ns clojure-lanterna-snake.controllers.game-controller-test
  (:require [clojure.test :refer :all]
            [clojure-lanterna-snake.views.snake :as views.snake]
            [clojure-lanterna-snake.domain.snake :as domain.snake]
            [clojure-lanterna-snake.controllers.game-controller :as game-controller]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as m]))

(def my-world {:width 81 :height 21})
(def my-game (game-controller/create-game my-world :moving/up))

(deftest creating-a-game
  (testing "puts the snake at the middle of the screen"
    (let
     [expected-snake       #:snake{:current-position {:x 40 :y 10}
                                   :moving-direction :moving/up}
      expected-snake-frame {:pixel/position {:x 40 :y 10}
                            :pixel/content  "x"}
      expected-world-frame {:pixel/position {:x 1 :y 1}
                            :pixel/foreground-color :cyan}
      expected-frame       [expected-snake-frame expected-world-frame]]
      (is (match? expected-snake
                  (get-in my-game [:game-context/game :game/snake])))
      (is (match? (m/embeds expected-frame)
                  (get-in my-game [:game-context/view :game-view/frame]))))))
