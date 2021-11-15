(ns clojure-lanterna-snake.views.world-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test :refer :all]
            [matcher-combinators.matchers :as m]
            [clojure-lanterna-snake.views.world :as views.world]))

(def my-world {:width 3 :height 3})

(def expected-view [{:pixel/position {:x 1 :y 1}}
                    {:pixel/position {:x 2 :y 1}}
                    {:pixel/position {:x 3 :y 1}}
                    {:pixel/position {:x 1 :y 2}}
                    {:pixel/position {:x 3 :y 2}}
                    {:pixel/position {:x 1 :y 3}}
                    {:pixel/position {:x 2 :y 3}}
                    {:pixel/position {:x 3 :y 3}}])

(deftest world->view
  (testing "maps world to view"
    (let [my-world-pixels (views.world/->view my-world)]
      (is (match? (m/in-any-order expected-view) my-world-pixels))
      (is (= 8 (count my-world-pixels))))))
