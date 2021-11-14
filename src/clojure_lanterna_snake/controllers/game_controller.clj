(ns clojure-lanterna-snake.controllers.game-controller
  (:require [schema.core :as s]
            [clojure-lanterna-snake.domain.snake :as domain.snake]
            [clojure-lanterna-snake.domain.world :as domain.world]
            [clojure-lanterna-snake.views.snake  :as views.snake]
            [clojure-lanterna-snake.views.screen :as views.screen]))

(def GameView #:game-view{:frame [views.screen/Pixel]})

(def GameContext
  #:game-context{:snake      domain.snake/Snake
                 :world      domain.world/World
                 :view       GameView})

(s/defn make-frame :- GameView
  [game :- GameContext]
  #:game-view{:frame (views.snake/->view (:game-context/snake game))})

(s/defn create-game :- GameContext
  [world             :- domain.world/World
   initial-direction :- domain.snake/MovingDirection]

  (let [game #:game-context{:snake      (domain.snake/create-snake (domain.world/center-position world) initial-direction)
                            :world      world
                            :view       #:game-view{:frame []}}]
    (assoc game :game-context/view (make-frame game))))

(s/defn next-frame :- GameContext
  [game :- GameContext
   direction :- (s/maybe domain.snake/MovingDirection)]

  (let [next-game (update game :game-context/snake #(domain.snake/move % direction))]
    (assoc next-game :game-context/view (make-frame next-game))))
