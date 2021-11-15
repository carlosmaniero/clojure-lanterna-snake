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

  (inc (int (* random-position (- max-value 2)))))

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

(s/defn snake-try-to-eat :- Game
  [game :- Game
   input :- GameInput]

  (let [snake           (:game/snake game)
        snake-position  (:snake/current-position snake)
        food            (:game/food  game)
        eaten-food      (domain.food/try-to-eat food snake-position)
        random-position (:game-input/random-position input)
        world           (:game/world game)]

    (if (:food/eaten? eaten-food)
      (assoc game :game/snake (domain.snake/with-extra-energy snake (:food/energy eaten-food))
             :game/food  (create-food random-position world))
      game)))

(s/defn snake-dead-if-collides
  [game :- Game]

  (let [snake          (:game/snake game)
        snake-position (:snake/current-position snake)
        world          (:game/world game)
        colided        (domain.world/collided-with? world snake-position)]

    (if colided
      (update game :game/snake domain.snake/kill-snake)
      game)))

(s/defn update-game :- Game
  [game  :- Game
   input :- GameInput]

  (-> (update game :game/snake #(domain.snake/move % (:game-input/direction input)))
      (snake-try-to-eat input)
      (snake-dead-if-collides)))

