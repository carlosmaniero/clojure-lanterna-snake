(ns clojure-lanterna-snake.domain.game-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test :refer :all]
            [clojure-lanterna-snake.domain.game :as domain.game]))

(def my-world {:width 5 :height 5})

(def random-position {:game-random-position/x 0
                      :game-random-position/y 0})

(def random-max-position {:game-random-position/x 1
                          :game-random-position/y 1})

(deftest spawning-food
  (testing "does not spawns food at the borders"
    (let [game-min-position (domain.game/create-game my-world :moving/up random-position)
          game-max-position (domain.game/create-game my-world :moving/up random-max-position)]
      (is (= {:x 1 :y 1} (get-in game-min-position [:game/food :food/position])))
      (is (= {:x 4 :y 4} (get-in game-max-position [:game/food :food/position]))))))

(deftest eating-food
  (testing "adds extra energy and spawn another food"
    (let [game                    (domain.game/create-game my-world :moving/up random-position)
          game-snake-about-to-eat (assoc-in game [:game/snake :snake/current-position] {:x 1 :y 2})
          game-snake-ate          (domain.game/update-game game-snake-about-to-eat
                                                           {:game-input/direction nil
                                                            :game-input/random-position random-max-position})]

      (is (match? {:snake/extra-energy 1} (:game/snake game-snake-ate)))
      (is (= {:x 4 :y 4} (get-in game-snake-ate [:game/food :food/position]))))))
