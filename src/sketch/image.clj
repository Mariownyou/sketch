(ns sketch.image
  (:require [clojure.java.io :as io])
  (:require [quil.core :refer :all :include-macros true]))

(def file "pic.jpeg")
(def resized "resized.jpeg")
(def size [500 500])

(defn resize-image
  [file-in file-out width height]
  (let [img     (javax.imageio.ImageIO/read (io/file file-in))
        imgtype (java.awt.image.BufferedImage/TYPE_INT_RGB)
        simg    (java.awt.image.BufferedImage. width height imgtype)
        g       (.createGraphics simg)]
    (.drawImage g img 0 0 width height nil)
    (.dispose g)
    (javax.imageio.ImageIO/write simg "jpeg" (io/file file-out))))

(def image-size (with-open [r (java.io.FileInputStream. file)]
  (let [image (javax.imageio.ImageIO/read r)]
    [(.getWidth image) (.getHeight image)])))

(def scaled-size (map (fn [x] 
  (int (* x 
    (let [x (float (apply min (map / size image-size)))]
      (if (<= x 1) (- x 0.05) 1))))) image-size))

(def offset (map (fn [c i] (int (/ (- c i) 2))) size scaled-size))

(apply resize-image (list* file resized scaled-size))

(defn rgba [color]
  [(red color) (green color) (blue color)])

(defn blue-filter [c]
  (assoc c 2 (+ (nth c 2) 30)))

(defn set-p [im i j]
  (->> (get-pixel im i j)
       rgba
       ; (map (fn [c] (+ c 10)))
       blue-filter
       (apply color)
       (set-pixel (+ i (first offset)) (+ j (last offset)))))

(defn setup []
  (background 255 255 255)
  (let [url resized]
    (set-state! :image (load-image url))))

(defn draw []
  (let [im (state :image)]
    (when (loaded? im) 
      (let [px (pixels im)]
        (doseq [i (range (first scaled-size))
                j (range (last  scaled-size))]
          (set-p im i j))
          (fill 0 0 0)
          (text file 210 490)
          (no-loop)))))

(defsketch my
  :host "host"
  :size size
  :features [:keep-on-top]
  :setup setup
  :draw draw)