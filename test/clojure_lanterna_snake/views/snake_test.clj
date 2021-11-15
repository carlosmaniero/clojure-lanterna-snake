(ns clojure-lanterna-snake.views.snake-test
  (:require [clojure.test :refer :all]
            [clojure-lanterna-snake.views.snake :as views.snake]
            [clojure-lanterna-snake.domain.snake :as domain.snake]
            [matcher-combinators.test]))

(def my-snake (domain.snake/create-snake {:x 2 :y 2} :moving/up))
(def my-snake-with-body (assoc my-snake :snake/body [{:x 2 :y 2}
                                                     {:x 2 :y 3}]))

(deftest snake->view
  (testing "maps the head to pixels"
    (let
     [my-view         (views.snake/->view my-snake)
      expected-pixels [#:pixel{:position         {:x 2 :y 2}
                               :foreground-color :green}]]
      (is (match? expected-pixels my-view))))

  (testing "maps the body to pixels"
    (let
     [my-view         (views.snake/->view my-snake-with-body)
      expected-pixels [#:pixel{:position         {:x 2 :y 2}
                               :foreground-color :green}
                       #:pixel{:position         {:x 2 :y 3}
                               :foreground-color :blue}]]
      (is (match? expected-pixels my-view)))))
