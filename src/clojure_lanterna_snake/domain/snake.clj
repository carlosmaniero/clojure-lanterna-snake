(ns clojure-lanterna-snake.domain.snake
  (:require [schema.core :as s]
            [clojure-lanterna-snake.domain.world :as domain.world]))

;; it should be added globally
(s/set-fn-validation! true)

(def MovingDirection (s/enum :moving/up :moving/right :moving/left :moving/down))

(s/defn ^:private position-after-move :- domain.world/Position
  [position :- domain.world/Position
   direction :- MovingDirection]
  (cond
    (= :moving/up    direction) (update position :y dec)
    (= :moving/down  direction) (update position :y inc)
    (= :moving/left  direction) (update position :x dec)
    (= :moving/right direction) (update position :x inc)))

(def Snake
  {:snake/current-position domain.world/Position
   :snake/is-alive?        s/Bool
   :snake/moving-direction MovingDirection
   :snake/extra-energy     s/Int
   :snake/body             [domain.world/Position]})

(s/defn create-snake :- Snake [initial-position :- domain.world/Position
                               initial-direction :- MovingDirection]
  {:snake/current-position initial-position
   :snake/moving-direction initial-direction
   :snake/is-alive?        true
   :snake/extra-energy     0
   :snake/body             [initial-position]})

(s/defn ^:private direction-or-snake-moving-direction :- MovingDirection
  [direction snake]
  (if (nil? direction)
    (:snake/moving-direction snake)
    direction))

(defn ^:private strech-head
  [snake position]
  (assoc snake :snake/body (into [position] (:snake/body snake))))

(defn ^:private loose-energy
  [snake]
  (if (> (:snake/extra-energy snake) 0)
    (update snake :snake/extra-energy dec)
    (update snake :snake/body         drop-last)))

(s/defn with-extra-energy :- Snake
  [snake        :- Snake
   extra-energy :- s/Int]

  (update snake :snake/extra-energy #(+ % extra-energy)))

(s/defn move :- Snake
  [snake             :- Snake
   changed-direction :- (s/maybe MovingDirection)]

  (let [direction (direction-or-snake-moving-direction changed-direction snake)
        position  (position-after-move (:snake/current-position snake) direction)]
    (-> snake
        (assoc :snake/moving-direction direction :snake/current-position position)
        (strech-head position)
        (loose-energy))))
