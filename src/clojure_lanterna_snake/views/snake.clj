(ns clojure-lanterna-snake.views.snake
  (:require [schema.core :as s]
            [clojure-lanterna-snake.views.screen :as views.screen]
            [clojure-lanterna-snake.domain.snake :as domain.snake]
            [clojure-lanterna-snake.domain.world :as domain.world]))

(s/set-fn-validation! true)

(def head-content "x")
(def body-content "o")

(s/defn ^:private body->pixel :- views.screen/Pixel
  [position :- domain.world/Position]
  #:pixel{:position         position
          :content          body-content
          :foreground-color :blue})

(s/defn ^:private head->pixel :- views.screen/Pixel
  [position :- domain.world/Position]

  #:pixel{:position         position
          :content          head-content
          :foreground-color :red})

(s/defn ^:private snake->head->pixel :- views.screen/Pixel
  [snake :- domain.snake/Snake]

  (head->pixel (first (:snake/body snake))))

(s/defn ^:private snake->body->pixels :- [views.screen/Pixel]
  [snake :- domain.snake/Snake]

  (->> snake
       (:snake/body)
       (rest)
       (map body->pixel)))

(s/defn ->view :- [views.screen/Pixel]
  [snake :- domain.snake/Snake]
  (views.screen/join-pixels
   [(snake->head->pixel snake)]
   (snake->body->pixels snake)))
