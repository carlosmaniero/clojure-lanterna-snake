(ns clojure-lanterna-snake.domain.snake-test
  (:require [clojure.test :refer :all]
            [matcher-combinators.test]
            [clojure-lanterna-snake.domain.snake :as domain.snake]
            [clojure-lanterna-snake.domain.aux-matchers :as aux-matchers]))

(def initial-position {:x 1 :y 2})
(def initial-direction :moving/up)
(def my-snake (domain.snake/create-snake initial-position initial-direction))

(deftest snake-creation-tests
  (testing "creates a snake with the initial position"
    (is (match? #:snake{:is-alive?        true
                        :current-position initial-position
                        :moving-direction initial-direction
                        :body             [initial-position]}
                my-snake))))

(deftest moving-a-snake
  #_(testing "follows the current moviment given no changed-direction"
      (aux-matchers/snake-match-moviment-position my-snake nil {:x 1 :y 1}))

  (testing "goes up when direction changes to up and does not allows moving down"
    (let [snake-moving-down (assoc my-snake :snake/moving-direction :moving/left)]
      (-> snake-moving-down
          (aux-matchers/snake-match-moviment-position :moving/up   {:x 1 :y 1})
          (aux-matchers/snake-match-moviment-position nil          {:x 1 :y 0})
          (aux-matchers/snake-match-moviment-position nil          {:x 1 :y -1})
          (aux-matchers/snake-match-moviment-position :moving/down {:x 1 :y -2}))))

  (testing "goes down when direction changes to down and does not allows moving up"
    (-> (assoc my-snake :snake/moving-direction :moving/left)
        (aux-matchers/snake-match-moviment-position :moving/down {:x 1 :y 3})
        (aux-matchers/snake-match-moviment-position nil          {:x 1 :y 4})
        (aux-matchers/snake-match-moviment-position nil          {:x 1 :y 5})
        (aux-matchers/snake-match-moviment-position :moving/up   {:x 1 :y 6})))

  (testing "goes left when direction changes to left and does not allows moving right"
    (-> my-snake
        (aux-matchers/snake-match-moviment-position :moving/left  {:x 0 :y 2})
        (aux-matchers/snake-match-moviment-position nil           {:x -1 :y 2})
        (aux-matchers/snake-match-moviment-position nil           {:x -2 :y 2})
        (aux-matchers/snake-match-moviment-position :moving/right {:x -3 :y 2})))

  (testing "goes right when direction changes to right and does not allows moving left"
    (-> my-snake
        (aux-matchers/snake-match-moviment-position :moving/right {:x 2 :y 2})
        (aux-matchers/snake-match-moviment-position nil           {:x 3 :y 2})
        (aux-matchers/snake-match-moviment-position nil           {:x 4 :y 2})
        (aux-matchers/snake-match-moviment-position :moving/left  {:x 5 :y 2}))))

(deftest with-extra-energy
  (testing "does not removes tails when moving given an extra energy"
    (-> my-snake
        (domain.snake/with-extra-energy 1)
        (aux-matchers/snake-match-moviment :moving/up {:snake/body [{:x 1 :y 1} {:x 1 :y 2}]})
        (aux-matchers/snake-match-moviment :moving/up {:snake/body [{:x 1 :y 0} {:x 1 :y 1}]})))

  (testing "extra energy is culmulative"
    (is (match? {:snake/extra-energy 2}
                (-> my-snake
                    (domain.snake/with-extra-energy 1)
                    (domain.snake/with-extra-energy 1))))))

(deftest controlling-velocity
  (testing "snake controlls velocy"
    (are
     [expected direction]
     (= expected (:snake/velocity (domain.snake/move my-snake direction)))
      100      :moving/left
      100      :moving/right
      150      :moving/up
      150      :moving/down)))


(deftest snake-eating-itself
  (testing "snake dies when it eat itself"
    (let [snake-with-extra-energy (domain.snake/with-extra-energy my-snake 6)
          dead-snake              (-> snake-with-extra-energy
                                      (domain.snake/move :moving/right)
                                      (domain.snake/move :moving/down)
                                      (domain.snake/move :moving/left)
                                      (domain.snake/move :moving/up))]
      (is (false? (:snake/is-alive? dead-snake))))))
