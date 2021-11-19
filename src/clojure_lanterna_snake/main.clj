(ns clojure-lanterna-snake.main
  (:require [lanterna.screen :as s]
            [schema.core :as schema]
            [clojure-lanterna-snake.controllers.game-controller :as game-controller])
  (:gen-class))

(schema/set-fn-validation! true)

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

(defn score
  [game]

  (dec (count (get-in game [:game-context/game :game/snake :snake/body]))))

(defn game->score->put-string
  [game]

  (s/put-string scr 0 (:height world) (str "Score: " (score game))))

(defn game->put-string
  [game]

  (doall
   (map pixel->put-string (get-in game [:game-context/view :game-view/frame])))

  (game->score->put-string game)

  (s/redraw scr))

(defn next-game-key
  [game]

  (s/get-key-blocking scr {:timeout (get-in game [:game-context/game
                                                  :game/snake
                                                  :snake/velocity])}))

(defn ask-to-quit? [key-pressed] (= key-pressed \q))

(defn next-game-frame!
  [game key]

  (game-controller/next-frame
   game
   {:game-input/direction       (key-to-moviment key)
    :game-input/random-position {:game-random-position/x (rand)
                                 :game-random-position/y (rand)}}))

(defn ask-for-next-game-frame!
  [game]

  (let [key (next-game-key game)]
    (if (ask-to-quit? key)
      nil
      (next-game-frame! game key))))

(defn show-game-over-screen
  [game]
  (s/put-string scr 9 8 "Game Over!" {:fg :red})
  (s/put-string scr 9 9 "Press q to exit")
  (game->score->put-string game)
  (s/redraw scr)
  (if (= \q (s/get-key-blocking scr))
    nil
    (recur game)))

(defn game-over?
  [game]

  (not (get-in game [:game-context/game :game/snake :snake/is-alive?])))

(defn game-loop!
  [game]
  (s/clear scr)
  (if (some? game)
    (if (game-over? game)
      (show-game-over-screen game)
      (do
        (game->put-string game)

        (recur (ask-for-next-game-frame! game))))
    (s/stop scr)))

(defn -main
  [& args]
  (s/start scr)
  (game-loop! (start-game)))
