(ns quil-sketches.gen-art.10-custom-rand
  (:require [quil.core :refer :all]
            [quil.helpers.drawing :refer [line-join-points]]
            [quil.helpers.seqs :refer [range-incl]]
            [quil.helpers.calc :refer [mul-add]]))

(defn custom-rand
  []
  (- 1 (pow (random 1) 5)))

(defn setup []
  (background 255)
  (stroke-weight 5)
  (smooth)
  (stroke 0 5)
  (line 20 50 480 50)
  (stroke 20 50 70)

  (let [xs        (range-incl 20 480 5)
        ys        (repeatedly custom-rand)
        scaled-ys (mul-add ys 60 20)
        line-args (line-join-points xs scaled-ys)]

    (dorun (map #(apply line %) line-args))))

(defsketch gen-art-10
  :title "Custom Random Function"
  :setup setup
  :size [500 100])

(defn -main [& args])