(ns clojure-lanterna-snake.domain.food
  (:require [schema.core :as s]
            [clojure-lanterna-snake.domain.world :as domain.world]))

(def Food
  #:food{:energy   s/Int
         :position domain.world/Position
         :eaten?   s/Bool})

(s/defn regular-food
  [position :- domain.world/Position]

  #:food{:energy   1
         :position position
         :eaten?   false})

(s/defn ^:private can-eat? :- s/Bool
  [food :- Food
   eater-position :- domain.world/Position]

  (-> (:food/position food)
      (= eater-position)))

(s/defn try-to-eat :- Food
  [food :- Food
   eater-position :- domain.world/Position]

  (assoc food :food/eaten? (can-eat? food eater-position)))
