(ns clojure-lanterna-snake.main
  (:require [lanterna.screen :as s]
            [clojure-lanterna-snake.controllers.game-controller :as game-controller])
  (:gen-class))

(def scr (s/get-screen :text))
(def world {:width 80 :height 20})

(defn key-to-moviment
  [typed]
  (cond
    (= :up    typed) :moving/up
    (= :right typed) :moving/right
    (= :down  typed) :moving/down
    (= :left  typed) :moving/left))

(defn get-moviment-blocking
  []
  (->>
   (s/get-key-blocking scr)
   (key-to-moviment)))

(defn start-game
  []
  (s/put-string scr 9 8 "Press an arrow key to start")
  (s/redraw scr)

  (game-controller/create-game world
                               (get-moviment-blocking)
                               {:game-random-position/x (rand)
                                :game-random-position/y (rand)}))

(defn pixel->put-string
  [pixel]

  (s/put-string scr
                (get-in pixel [:pixel/position :x])
                (get-in pixel [:pixel/position :y])
                (get-in pixel [:pixel/content])
                {:fg (:pixel/foreground-color pixel) :bg (:pixel/background-color pixel)}))

(defn run
  [game]
  (s/clear scr)

  (if (not (get-in game [:game-context/game :game/snake :snake/is-alive?]))
    (do
      (s/put-string scr 9 8 "Game Over!" {:fg :red})
      (s/redraw scr)
      (s/get-key-blocking scr))
    (do
      (doall
       (map pixel->put-string (get-in game [:game-context/view :game-view/frame])))

      (s/redraw scr)

      (let [key (s/get-key-blocking scr {:timeout (get-in game [:game-context/game
                                                                :game/snake
                                                                :snake/velocity])})]
        (if (= key \q)
          (s/stop scr)
          (recur (game-controller/next-frame
                  game
                  {:game-input/direction   (key-to-moviment key)
                   :game-input/random-position {:game-random-position/x (rand)
                                                :game-random-position/y (rand)}})))))))

(defn -main
  [& args]
  (s/start scr)
  (run (start-game)))
