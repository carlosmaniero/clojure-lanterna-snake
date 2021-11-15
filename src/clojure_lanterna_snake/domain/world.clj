(ns clojure-lanterna-snake.domain.world
  (:require [schema.core :as s]))

(def Position {:x s/Int :y s/Int})

(def World {:width s/Int :height s/Int})

(s/defn center-position :- Position
  [world :- World]

  {:x (int (/ (:width world) 2))
   :y (int (/ (:height world) 2))})

(s/defn collided-with? :- s/Bool
  [world           :- World
   object-position :- Position]

  (some? (some true? [(= 0                     (:x object-position))
                      (= 0                     (:y object-position))
                      (= (dec (:width  world)) (:x object-position))
                      (= (dec (:height world)) (:y object-position))])))
