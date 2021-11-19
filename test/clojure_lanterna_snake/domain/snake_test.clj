(ns clojure-lanterna-snake.domain.snake-test
  (:require
   [clojure-lanterna-snake.domain.snake :as domain.snake]
   [clojure-lanterna-snake.domain.snake-matchers :as aux-matchers]
   [clojure.test :refer :all]
   [matcher-combinators.test :refer [match?]]))

(def initial-position {:x 1 :y 2})
(def initial-direction :moving/up)
(def my-snake (domain.snake/create-snake initial-position initial-direction))

(deftest snake-creation-tests
  (testing "creates a snake with the initial position"
    (is (match? #:snake{:is-alive?        true
                        :moving-direction initial-direction
                        :body             [initial-position]}
                my-snake))))

(deftest moving-a-snake
  (testing "follows the current moviment given no changed-direction"
    (aux-matchers/snake-match-moviment-position my-snake {:x 1 :y 1}))

  (testing "goes up when direction changes to up and does not allows moving down"
    (-> my-snake
          (aux-matchers/snake-match-moviment-position {:x 1 :y 1})
          (aux-matchers/snake-match-moviment-position {:x 1 :y 0})
          (aux-matchers/snake-match-moviment-position {:x 1 :y -1})))

  (testing "goes down when direction changes to down and does not allows moving up"
    (-> (domain.snake/create-snake initial-position :moving/down)
        (aux-matchers/snake-match-moviment-position {:x 1 :y 3})
        (aux-matchers/snake-match-moviment-position {:x 1 :y 4})
        (aux-matchers/snake-match-moviment-position {:x 1 :y 5})))

  (testing "goes left when direction changes to left and does not allows moving right"
    (-> (domain.snake/create-snake initial-position :moving/left)
        (aux-matchers/snake-match-moviment-position {:x 0 :y 2})
        (aux-matchers/snake-match-moviment-position {:x -1 :y 2})
        (aux-matchers/snake-match-moviment-position {:x -2 :y 2})))

  (testing "goes right when direction changes to right and does not allows moving left"
    (-> (domain.snake/create-snake initial-position :moving/right)
        (aux-matchers/snake-match-moviment-position {:x 2 :y 2})
        (aux-matchers/snake-match-moviment-position {:x 3 :y 2})
        (aux-matchers/snake-match-moviment-position {:x 4 :y 2}))))

(deftest with-extra-energy
  (testing "does not removes tails when moving given an extra energy"
    (-> my-snake
        (domain.snake/adds-extra-energy 1)
        (aux-matchers/match-snake-after-moviment {:snake/body [{:x 1 :y 1} {:x 1 :y 2}]})
        (aux-matchers/match-snake-after-moviment {:snake/body [{:x 1 :y 0} {:x 1 :y 1}]})))

  (testing "extra energy is culmulative"
    (is (match? {:snake/extra-energy 2}
                (-> my-snake
                    (domain.snake/adds-extra-energy 1)
                    (domain.snake/adds-extra-energy 1))))))

(deftest controlling-velocity
  (testing "snake controlls velocy when creating a snake"
    (are
     [expected direction]
     (= expected (-> (domain.snake/create-snake initial-position direction)
                     (:snake/velocity)))
      100      :moving/left
      100      :moving/right
      150      :moving/up
      150      :moving/down))

  (testing "snake controlls velocy when changing directions"
    (are
        [expected current-direction next-direction]
        (= expected (-> (domain.snake/create-snake initial-position current-direction)
                        (domain.snake/change-direction next-direction)
                        (:snake/velocity)))
      100   :moving/down    :moving/left
      100   :moving/down    :moving/right
      150   :moving/left    :moving/up
      150   :moving/left    :moving/down)))

(deftest changing-direction
  (testing "snake cannot reverse the current direction"
    (are
     [expected current-direction next-direction]
     (= expected (-> (domain.snake/create-snake initial-position current-direction)
                     (domain.snake/change-direction next-direction)
                     (:snake/moving-direction)))
      ;; allowed
      :moving/right :moving/up    :moving/right
      :moving/right :moving/down  :moving/right
      :moving/left  :moving/up    :moving/left
      :moving/left  :moving/down  :moving/left
      :moving/down  :moving/left  :moving/down
      :moving/down  :moving/right :moving/down
      :moving/up    :moving/left  :moving/up
      :moving/up    :moving/right :moving/up

      ;; not-allowed
      :moving/left  :moving/left  :moving/right
      :moving/right :moving/right :moving/left
      :moving/down  :moving/down  :moving/up
      :moving/up    :moving/up    :moving/down)))

(deftest snake-eating-itself
  (testing "snake dies when it eat itself"
    (let [snake-with-extra-energy (domain.snake/adds-extra-energy my-snake 6)
          dead-snake              (-> snake-with-extra-energy
                                      (domain.snake/change-direction :moving/right)
                                      (domain.snake/move)
                                      (domain.snake/change-direction :moving/down)
                                      (domain.snake/move)
                                      (domain.snake/change-direction :moving/left)
                                      (domain.snake/move)
                                      (domain.snake/change-direction :moving/up)
                                      (domain.snake/move))]
      (is (false? (:snake/is-alive? dead-snake))))))
