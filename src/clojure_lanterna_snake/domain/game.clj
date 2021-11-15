(ns clojure-lanterna-snake.domain.game
  (:require [schema.core :as s]
            [clojure-lanterna-snake.domain.snake :as domain.snake]
            [clojure-lanterna-snake.domain.world :as domain.world]))

(def Game
  #:game{:snake      domain.snake/Snake
         :world      domain.world/World})

(def GameInput
  #:game-input{:direction (s/maybe domain.snake/MovingDirection)})

(s/defn create-game-with-snake
  [snake :- domain.snake/Snake
   world :- domain.world/World]

  #:game{:snake snake :world world})

(s/defn create-game :- Game
  [world             :- domain.world/World
   initial-direction :- domain.snake/MovingDirection]

  (-> world
      (domain.world/center-position)
      (domain.snake/create-snake initial-direction)
      (create-game-with-snake world)))

(s/defn update-game :- Game
  [game  :- Game
   input :- GameInput]

  (update game :game/snake #(domain.snake/move % (:game-input/direction input))))

