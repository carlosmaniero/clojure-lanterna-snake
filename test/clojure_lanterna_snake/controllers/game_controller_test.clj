(ns clojure-lanterna-snake.controllers.game-controller-test
  (:require [clojure.test :refer :all]
            [clojure-lanterna-snake.views.snake :as views.snake]
            [clojure-lanterna-snake.domain.snake :as domain.snake]
            [clojure-lanterna-snake.controllers.game-controller :as game-controller]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as m]))

(def my-world {:width 21 :height 11})

(def random-position {:game-random-position/x 0.25
                      :game-random-position/y 0.75})

(def my-game (game-controller/create-game my-world :moving/up random-position))

(deftest creating-a-game
  (testing "creates the view of the current state"
    (let
     [expected-snake       #:snake{:current-position {:x 10 :y 5}
                                   :moving-direction :moving/up}
      expected-food-frame  {:pixel/content          "█"
                            :pixel/foreground-color :red}
      expected-snake-frame {:pixel/position         {:x 10 :y 5}
                            :pixel/foreground-color :green}
      expected-world-frame {:pixel/position         {:x 0 :y 0}
                            :pixel/foreground-color :cyan}
      expected-frame       [expected-snake-frame
                            expected-world-frame
                            expected-food-frame]]
      (is (match? expected-snake
                  (get-in my-game [:game-context/game :game/snake])))
      (is (match? (m/embeds expected-frame)
                  (get-in my-game [:game-context/view :game-view/frame])))))

  (testing "creates the view for the next-frame"
    (let
     [expected-snake-frame {:pixel/position         {:x 10 :y 4}
                            :pixel/foreground-color :green}
      expected-frame       [expected-snake-frame]
      next-frame-game      (game-controller/next-frame my-game
                                                       {:game-input/direction nil
                                                        :game-input/random-position random-position})]
      (is (match? (m/embeds expected-frame)
                  (get-in next-frame-game [:game-context/view :game-view/frame]))))))
