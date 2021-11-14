(ns clojure-lanterna-snake.domain.world
  (:require [schema.core :as s]))

(def Position {:x s/Int :y s/Int})

(def World {:width s/Int :height s/Int})

(s/defn center-position :- Position
  [world :- World]

  {:x (int (/ (:width world) 2))
   :y (int (/ (:height world) 2))})
