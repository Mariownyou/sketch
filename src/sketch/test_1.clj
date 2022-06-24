(ns test_1.core
  (:require [quil.core :refer :all]
            [quil.middleware :as m]))

(defn draw [state] 
  (no-loop)
  (smooth)
  (background 220 49 66)
  (rect 0 0 10 10))

(defsketch art
  :title "Art"
  :size [500 500]
  :draw draw
  :features [:keep-on-top]
  :middleware [m/fun-mode])
