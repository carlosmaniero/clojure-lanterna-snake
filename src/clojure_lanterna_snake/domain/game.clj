(ns clojure-lanterna-snake.domain.game
  (:require [schema.core :as s]
            [clojure-lanterna-snake.domain.food  :as domain.food]
            [clojure-lanterna-snake.domain.snake :as domain.snake]
            [clojure-lanterna-snake.domain.world :as domain.world]))

(def GameFloatRandomPosition
  #:game-random-position{:x s/Num
                         :y s/Num})

(def Game
  #:game{:snake      domain.snake/Snake
         :world      domain.world/World
         :food       domain.food/Food})

(def GameInput
  #:game-input{:direction       (s/maybe domain.snake/MovingDirection)
               :random-position GameFloatRandomPosition})

(s/defn ^:private create-game-with-snake-and-food
  [snake :- domain.snake/Snake
   food  :- domain.food/Food
   world :- domain.world/World]

  #:game{:snake snake :world world :food food})

(defn ^:private normalize-random-position
  [random-position max-value]

  (+ 2 (int (* random-position (- max-value 3)))))

(s/defn random-position->-world-position
  [random-position world]

  {:x (normalize-random-position
       (:game-random-position/x random-position)
       (:width world))
   :y (normalize-random-position
       (:game-random-position/y random-position)
       (:height world))})

(s/defn create-food
  [random-position world]

  (domain.food/regular-food
   (random-position->-world-position random-position world)))

(s/defn create-game :- Game
  [world             :- domain.world/World
   initial-direction :- domain.snake/MovingDirection
   random-position   :- GameFloatRandomPosition]

  (-> world
      (domain.world/center-position)
      (domain.snake/create-snake initial-direction)
      (create-game-with-snake-and-food (create-food random-position world) world)))

(s/defn update-game :- Game
  [game  :- Game
   input :- GameInput]

  (update game :game/snake #(domain.snake/move % (:game-input/direction input))))

