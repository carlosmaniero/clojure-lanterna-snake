(ns clojure-lanterna-snake.controllers.game-controller
  (:require [schema.core :as s]
            [clojure-lanterna-snake.domain.snake :as domain.snake]
            [clojure-lanterna-snake.domain.world :as domain.world]))


(def GameComponents #:game-components{})

(def GameView #:game-view{:frame s/Str})

(def GameContext
  #:game-context{:snake      domain.snake/Snake
                 :world      domain.world/World
                 :view       GameView
                 :components GameComponents})

(s/defn create-game :- GameContext
  [world             :- domain.world/World
   initial-direction :- domain.snake/MovingDirection
   components        :- GameComponents]

  #:game-context{:snake      (domain.snake/create-snake {:x 0 :y 0} initial-direction)
                 :world      world
                 :view       #:game-view{:frame ""}
                 :components components})
