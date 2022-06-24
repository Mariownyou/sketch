(ns sketch.core
  (:require [quil.core :refer :all]
            [quil.middleware :as m]))

(defn get-color [noise-factor]
  (cond
    (> 0.1 noise-factor) [239 200 139]
    (> 0.2 noise-factor) [5   5   23]
    (> 0.3 noise-factor) [244 227 178]
    (> 0.4 noise-factor) [211 213 215]
    (> 0.5 noise-factor) [207 92  54]
    :else                [255 255 255]))

(defn spawn [n]
  (let [x (* (mod n 50) 10)
        y (* (int (/ n 50)) 10)]
    (fill (get-color (noise x y)))
    (no-stroke)
    (rect x y 10 10)))

(defn rects [number-of-rects]
  (map spawn (range 0 number-of-rects)))

(defn draw [state] 
  (no-loop)
  (smooth)
  ; (color-mode :hsb 360 100 100 1.0) ; b/w mode
  (background 220 49 66)
  (dorun (rects 2500))
  )

(defsketch art
  :title "Art"
  :size [500 500]
  :draw draw
  :features [:keep-on-top]
  :middleware [m/fun-mode])
