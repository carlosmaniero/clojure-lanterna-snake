(ns clojure-lanterna-snake.views.screen
  (:require [schema.core :as s]))

(def PixelColor (s/enum :black
                        :white
                        :red
                        :green
                        :blue
                        :cyan
                        :magenta
                        :yellow
                        :default))

(def PixelStyle (s/enum :bold
                        :reverse
                        :underline
                        :blinking))

(def Pixel {:pixel/position         {:x s/Int :y s/Int}
            :pixel/content          s/Str
            :pixel/foreground-color s/maybe PixelColor
            :pixel/background-color s/maybe PixelColor})


(s/defn join-pixels
  [pixels-a :- [Pixel]
   pixels-b :- [Pixel]]

  (->> (into pixels-a pixels-b)
       (group-by :pixel/position)
       (map (fn [[_ pixels]] (last pixels)))))
