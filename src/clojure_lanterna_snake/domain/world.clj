(ns clojure-lanterna-snake.domain.world
  (:require [schema.core :as s]))

(def Position {:x s/Int :y s/Int})

(def World {:width s/Int :height s/Int})
