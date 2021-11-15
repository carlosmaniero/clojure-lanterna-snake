(ns clojure-lanterna-snake.views.food
  (:require [schema.core :as s]
            [clojure-lanterna-snake.views.screen :as views.screen]
            [clojure-lanterna-snake.domain.food :as domain.food]))

(s/defn ->view :- [views.screen/Pixel]
  [food :- domain.food/Food]

  [#:pixel{:position         (:food/position food)
          :content          "█"
          :foreground-color :red}])
