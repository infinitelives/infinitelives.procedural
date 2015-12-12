(ns infinitelives.procedural.testpage
  (:require [infinitelives.procedural.maps :as maps]
            [goog.dom :as dom]))

(enable-console-print!)

(defonce app (.getElementById js/window.document "app"))

(def w 200)
(def h 200)

(defn make-table-cells [m]
  (doall (for [y (range h)]
    (str "<tr>" (apply str (doall (for [x (range w)] (str "<td class='" (get m [x y]) "'></td>")))) "</tr>"))))

(let [rpg-map (maps/make-rpg-map w h)]
  (set! (.-innerHTML app) (str "<table>" (apply str (make-table-cells rpg-map)) "</table>")))

(defn on-js-reload []
  ; ...
)
