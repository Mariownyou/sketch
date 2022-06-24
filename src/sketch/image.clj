(ns sketch.image
  (:require [clojure.java.io :as io])
  (:require [quil.core :refer :all :include-macros true]))

(def file "red.png")
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

(defn rgb [color] 
  (let [colors (rest (clojure.string/split color #""))
        red (take 2 colors)
        green (take 2 (drop 2 colors))
        blue (take 2 (drop 4 colors))]
    (map #(-> (conj % "0x") (clojure.string/join) (read-string)) [red green blue])))

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
          ; (set-pixel (+ i (first offset)) (+ j (last offset))
          ;   (apply color (rgb (str \# (hex (get-pixel im i j)))))))
          (set-pixel i j
            (apply color (rgb (str \# (hex (get-pixel im i j)))))))
          (fill 0 0 0)
          (text file 210 490)
          (let [p (get-pixel im 0 0)]
            (println p (hex p) (rgb (str \# (hex p)))))
          (no-loop)))))

(defsketch my
  :host "host"
  :size size
  :features [:keep-on-top]
  :setup setup
  :draw draw)