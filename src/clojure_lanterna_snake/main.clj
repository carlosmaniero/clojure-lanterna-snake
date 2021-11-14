(ns clojure-lanterna-snake.main
  (:require [lanterna.screen :as s])
  (:gen-class))

(def scr (s/get-screen :text))

(defn run
  [] (do
       (s/start scr)

       (s/put-string scr 10 10 "Hello, world!")
       (s/put-string scr 10 11 "Press any key to exit!")

       (s/redraw scr)
       (s/get-key-blocking scr)

       (s/stop scr)))

(defn -main
  [& args]
  (run))
