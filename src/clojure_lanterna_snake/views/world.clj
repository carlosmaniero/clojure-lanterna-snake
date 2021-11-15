(ns clojure-lanterna-snake.views.world
  (:require [schema.core :as s]
            [clojure-lanterna-snake.domain.world :as domain.world]
            [clojure-lanterna-snake.views.screen :as views.screen]))

(defn create-edge-pixel
  [position]

  {:pixel/content "▓"
   :pixel/position position
   :pixel/foreground-color :cyan
   :pixel/background-color :red})

(defn ^:private create-zebra-pixel
  [position]

  {:pixel/content (if (odd? (+ (:x position) (:y position))) "░" " ")
   :pixel/position position})

(defn ^:private ->edge
  [axis start size fixed-axis fixed-axis-value]

  (->> (range (dec start) size)
       (map inc)
       (map #(create-edge-pixel {axis % fixed-axis fixed-axis-value}))))

(defn ^:private ->top-edge    [world] (->edge :x 1 (:width world) :y 1))
(defn ^:private ->bottom-edge [world] (->edge :x 1 (:width world) :y (:height world)))
(defn ^:private ->left-edge   [world] (->edge :y 2 (dec (:height world)) :x 1))
(defn ^:private ->right-edge  [world] (->edge :y 2 (dec (:height world)) :x (:width world)))

(defn tap
  [any]

  (do
    (println any)
    any))

(defn ->zebra-line
  [y world]

  (->> (:width world)
       (dec)
       (dec)
       (range)
       (map inc)
       (map inc)
       (map #(create-zebra-pixel {:x % :y y}))))

(defn ->zebra
  [world]

  (->> (:height world)
       (dec)
       (dec)
       (range)
       (map inc)
       (map inc)
       (map #(->zebra-line % world))
       (flatten)))

(s/defn ->view :- [views.screen/Pixel]
  [world :- domain.world/World]

  (-> []
      (into (->top-edge    world))
      (into (->bottom-edge world))
      (into (->right-edge  world))
      (into (->left-edge   world))
      (into (->zebra       world))))

