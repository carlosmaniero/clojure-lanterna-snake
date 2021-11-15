(ns clojure-lanterna-snake.domain.world-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test]
            [clojure-lanterna-snake.domain.world :as domain.world]))

(def my-world {:width 3 :height 3})

(deftest colliding
  (testing "collides with the edge when an edge position is given"
    (are [expected position] (= expected (domain.world/collided-with? my-world position))
      true  {:x 0 :y 0}
      true  {:x 1 :y 0}
      true  {:x 2 :y 0}
      true  {:x 0 :y 1}
      false {:x 1 :y 1}
      true  {:x 2 :y 1}
      true  {:x 0 :y 2}
      true  {:x 1 :y 2}
      true  {:x 2 :y 2})))
