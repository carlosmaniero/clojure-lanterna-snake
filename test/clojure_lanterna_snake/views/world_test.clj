(ns clojure-lanterna-snake.views.world-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test :refer :all]
            [matcher-combinators.matchers :as m]
            [clojure-lanterna-snake.views.world :as views.world]))

(def my-world {:width 3 :height 3})

(def expected-view [(views.world/create-edge-pixel {:x 1 :y 1})
                    (views.world/create-edge-pixel {:x 2 :y 1})
                    (views.world/create-edge-pixel {:x 3 :y 1})
                    (views.world/create-edge-pixel {:x 1 :y 2})
                    (views.world/create-edge-pixel {:x 3 :y 2})
                    (views.world/create-edge-pixel {:x 1 :y 3})
                    (views.world/create-edge-pixel {:x 2 :y 3})
                    (views.world/create-edge-pixel {:x 3 :y 3})])

(defn is-my-world-center-pixel
  [pixel]

  (= {:x 2 :y 2} (:pixel/position pixel)))

(deftest world->view
  (testing "maps world to view"
    (let [my-world-pixels (views.world/->view my-world)
          center-pixel    (some is-my-world-center-pixel my-world-pixels)]
      (is (match? (m/embeds expected-view) my-world-pixels))
      (is (not (= :red (:pixel/background-color center-pixel)))))))
