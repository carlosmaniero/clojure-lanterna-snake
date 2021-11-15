(ns clojure-lanterna-snake.domain.snake
  (:require [schema.core :as s]
            [clojure-lanterna-snake.domain.world :as domain.world]))

;; it should be added globally
(s/set-fn-validation! true)

(def MovingDirection (s/enum :moving/up :moving/right :moving/left :moving/down))

(def ^:private opose-direction-map
  {:moving/up     :moving/down
   :moving/down   :moving/up
   :moving/right  :moving/left
   :moving/left   :moving/right})

(def default-velocity 100)
(def slow-velocity    150)

(s/defn ^:private velocity
  [direction :- MovingDirection]

  (cond
    (= :moving/up    direction) slow-velocity
    (= :moving/down  direction) slow-velocity
    (= :moving/left  direction) default-velocity
    (= :moving/right direction) default-velocity))

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
   :snake/velocity         s/Int
   :snake/body             [domain.world/Position]})

(s/defn create-snake :- Snake [initial-position :- domain.world/Position
                               initial-direction :- MovingDirection]
  {:snake/current-position initial-position
   :snake/moving-direction initial-direction
   :snake/is-alive?        true
   :snake/extra-energy     0
   :snake/velocity         (velocity initial-direction)
   :snake/body             [initial-position]})


(defn is-opose-direction
  [direction current-direction]

  (= (direction opose-direction-map) current-direction))


(defn normalize-direction
  [direction current-direction]

  (if (is-opose-direction direction current-direction)
    current-direction
    direction))

(s/defn ^:private normalized-direction-or-snake-moving-direction :- MovingDirection
  [direction snake]
  (if (nil? direction)
    (:snake/moving-direction snake)
    (normalize-direction direction (:snake/moving-direction snake))))

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

  (let [direction (normalized-direction-or-snake-moving-direction changed-direction snake)
        position  (position-after-move (:snake/current-position snake) direction)]
    (-> snake
        (assoc :snake/moving-direction direction
               :snake/current-position position
               :snake/velocity         (velocity direction))
        (strech-head position)
        (loose-energy))))
