(ns clojure-lanterna-snake.controllers.game-controller
  (:require [schema.core :as s]
            [clojure-lanterna-snake.domain.snake :as domain.snake]
            [clojure-lanterna-snake.domain.world :as domain.world]
            [clojure-lanterna-snake.domain.game  :as domain.game]
            [clojure-lanterna-snake.views.snake  :as views.snake]
            [clojure-lanterna-snake.views.screen :as views.screen]))

(def GameView #:game-view{:frame [views.screen/Pixel]})

(def GameContext
  #:game-context{:game domain.game/Game
                 :view GameView})

(s/defn make-frame :- GameView
  [game :- GameContext]
  #:game-view{:frame (views.snake/->view (get-in game [:game-context/game :game/snake]))})

(s/defn create-game :- GameContext
  [world             :- domain.world/World
   initial-direction :- domain.snake/MovingDirection]

  (let [game #:game-context{:game (domain.game/create-game world initial-direction)
                            :view #:game-view{:frame []}}]
    (assoc game :game-context/view (make-frame game))))

(s/defn next-frame :- GameContext
  [game-context :- GameContext
   input        :- domain.game/GameInput]

  (let [next-game (update game-context :game-context/game #(domain.game/update-game % input))]
    (assoc next-game :game-context/view (make-frame next-game))))
