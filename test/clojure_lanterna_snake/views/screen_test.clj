(ns clojure-lanterna-snake.views.screen-test
  (:require [clojure.test :refer :all]
            [clojure-lanterna-snake.views.screen :as views.screen]
            [schema-generators.generators :as g]
            [matcher-combinators.test]))


(def some-pixel #:pixel{:position {:x 0 :y 1}
                        :content  "a"})

(def another-pixel #:pixel{:position {:x 0 :y 2}
                           :content  "b"})

(def some-pixels [some-pixel another-pixel])


(deftest join-pixels
  (testing "join to vectors of pixels"
    (is (= some-pixels (views.screen/join-pixels [some-pixel] [another-pixel]))))

  (testing "removes duplicated at the same position"
    (is (= [some-pixel] (views.screen/join-pixels
                         [(assoc some-pixel :pixel/content "c")]
                         [some-pixel]))))
  )
