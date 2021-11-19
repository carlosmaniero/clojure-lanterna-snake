(ns clojure-lanterna-snake.domain.snake
  (:require
   [clojure-lanterna-snake.domain.world :as domain.world]
   [schema.core :as s]))

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
  {:snake/is-alive?        s/Bool
   :snake/moving-direction MovingDirection
   :snake/extra-energy     s/Int
   :snake/velocity         s/Int
   :snake/body             [domain.world/Position]})

(s/defn create-snake :- Snake
  [initial-position :- domain.world/Position
   initial-direction :- MovingDirection]

  {:snake/moving-direction initial-direction
   :snake/is-alive?        true
   :snake/extra-energy     0
   :snake/velocity         (velocity initial-direction)
   :snake/body             [initial-position]})

(s/defn head-position :- domain.world/Position
  [snake :- Snake]

  (-> snake :snake/body first))

(defn is-opose-direction [direction current-direction]
  (= (direction opose-direction-map) current-direction))

(defn normalize-direction
  [direction current-direction]

  (if (is-opose-direction direction current-direction)
    current-direction
    direction))

(s/defn change-direction :- Snake
  [snake :- Snake
   direction :- MovingDirection]

  (-> snake
      (update :snake/moving-direction #(normalize-direction direction %))
      (assoc :snake/velocity (velocity direction))))

(defn ^:private strech-head
  [snake position]
  (assoc snake :snake/body (into [position] (:snake/body snake))))

(defn ^:private loose-energy
  [snake]
  (if (> (:snake/extra-energy snake) 0)
    (update snake :snake/extra-energy dec)
    (update snake :snake/body         drop-last)))

(s/defn adds-extra-energy :- Snake
  [snake        :- Snake
   extra-energy :- s/Int]

  (update snake :snake/extra-energy #(+ % extra-energy)))

(s/defn ^:private ate-itself? :- s/Bool
  [snake :- Snake]

  (let [body                      (:snake/body snake)
        current-position          (head-position snake)
        total-at-current-position (count (filter #(= current-position %) body))]
    (> total-at-current-position 1)))

(s/defn kill-snake :- Snake
  [snake :- Snake]
  (assoc snake :snake/is-alive? false))

(s/defn ^:private mark-as-dead-if-ate-itself
  [snake :- Snake]

  (if (ate-itself? snake)
    (kill-snake snake)
    snake))

(s/defn move :- Snake
  [snake :- Snake]

  (let [direction (:snake/moving-direction snake)
        position  (position-after-move (head-position snake) direction)]
    (-> snake
        (strech-head position)
        (loose-energy)
        (mark-as-dead-if-ate-itself))))
