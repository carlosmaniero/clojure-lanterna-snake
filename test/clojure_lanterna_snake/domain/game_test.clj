(ns clojure-lanterna-snake.domain.game-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test :refer :all]
            [clojure-lanterna-snake.domain.game :as domain.game]))

(def my-world {:width 5 :height 5})

(deftest spawning-food
  (testing "does not spawns food at the borders"
    (let [game-min-position (domain.game/create-game my-world :moving/up
                                                     {:game-random-position/x 0
                                                      :game-random-position/y 0})
          game-max-position (domain.game/create-game my-world :moving/up
                                                     {:game-random-position/x 1
                                                      :game-random-position/y 1})]
      (is (= {:x 2 :y 2} (get-in game-min-position [:game/food :food/position])))
      (is (= {:x 4 :y 4} (get-in game-max-position [:game/food :food/position]))))))
