(ns clojure-lanterna-snake.domain.food-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test]
            [clojure-lanterna-snake.domain.food :as domain.food]))

(def my-food (domain.food/regular-food {:x 1 :y 1}))

(deftest eating-food
  (testing "food is eaten when the eater position matches the food position"
    (-> my-food
        (domain.food/try-to-eat {:x 1 :y 1})
        (:food/eaten?)
        (is)))

  (testing "food is not eaten when the eater position doffers from food position"
    (-> my-food
        (domain.food/try-to-eat {:x 1 :y 2})
        (:food/eaten?)
        (false?)
        (is))))
