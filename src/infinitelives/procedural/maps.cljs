(ns infinitelives.procedural.maps
  [:require [noise]])

; shortcut - scaled perlin at point
(defn p [[x y] s] (.simplex2 js/noise (/ x s) (/ y s)))

(defn make-rpg-map [width height & [seed]]
  (let [seed (or seed (Math.random))
        x-offset (* seed 435898247)
        y-offset (* seed 382842987)]
    ; we add the offsets as well as the seed because
    ; this perlin algorithm always has zeroes at 0,0
    (.seed js/noise seed)
    (into {} (for [y (range height) x (range width)]
      (let [x-o (+ x x-offset)
            y-o (+ y y-offset)
            xy [x-o y-o]
            ; probabilities
            detail (js/Math.abs (p xy 300.0))
            desert1 (* -1 (p xy 100.0))
            desert2 (p xy 10.0)
            desertp (+ (* desert1 detail) (* desert2 (- 1.0 detail)))
            waterp (p xy 150.0)
            roadsp (js/Math.abs (p xy 50))
            treep1 (+ (* (+ (p xy 30.0) (p xy 300.0)) 0.4) (* (p xy 3.0) 0.2))
            treep2 (+ (* (p xy 300.0) 0.8) (* (p xy 3.0) 0.2))
            ; hot or not
            oasis (> desertp 0.95)
            lake (< desertp -0.6)
            river (and (< desert1 0.6) (< (js/Math.abs waterp) 0.075))
            desert (and (> desertp 0.6) (or (< desertp 1.0) (> desert1 0.8)))
            roads (< (js/Math.abs roadsp) 0.05)
            riversideRoads (and (< desertp 0.6) (< (js/Math.abs waterp) 0.11) (> (js/Math.abs waterp) 0.08))
            trees (or (> treep1 0.4) (> treep2 0.4))
            terrain (cond (or lake river oasis) "water"
                          (or roads riversideRoads) "road"
                          desert "sand"
                          trees "trees"
                          true "grass")]
        [[x y] terrain])))))

