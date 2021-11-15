(ns clojure-lanterna-snake.views.screen-test
  (:require [clojure.test :refer :all]
            [clojure-lanterna-snake.views.screen :as views.screen]
            [schema-generators.generators :as g]
            [matcher-combinators.test]))

(def some-pixel #:pixel{:position {:x 0 :y 1}
                        :content  "a"
                        :foreground-color nil})

(def another-pixel #:pixel{:position {:x 0 :y 2}
                           :content  "b"
                           :background-color :red})

(def some-pixels [some-pixel another-pixel])

(deftest join-pixels
  (testing "join to vectors of pixels"
    (is (match? some-pixels (views.screen/join-pixels [some-pixel] [another-pixel]))))

  (testing "removes duplicated at the same position"
    (is (match? [some-pixel] (views.screen/join-pixels
                         [(assoc some-pixel :pixel/content "c")]
                         [some-pixel]))))

  (testing "removes duplicated at the same position"
    (is (match? {:pixel/content "a" :pixel/background-color :red}
                (first (views.screen/join-pixels
                        [(assoc some-pixel
                                :pixel/content "c"
                                :pixel/background-color :red)]
                        [some-pixel]))))))
