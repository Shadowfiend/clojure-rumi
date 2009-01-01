(ns rumi-logic)
; Rumikube contains four colors, denoted herein by the symbols :blue, :red,
; :orange, and :black. In addition, it contains thirteen numbers, numbered from
; one to thirteen

(def colors '(:blue :red :orange :black))
(def numbers (range 1 14))

;------------------------- Tile Generation Functions -------------------------;
(defn generate-tiles-with-color
  ([color number-list] (generate-tiles-with-color color number-list '()))
  ([color number-list tile-list]
      (if (= 0 (count number-list))
        (tile-list)
        (recur color 
               (rest number-list) 
               (cons {:color color :number (first number-list) :wildcard false}
                     tile-list)))))


(defn generate-tiles
  ([color-list number-list] (generate-tiles color-list number-list '()))
  ([color-list number-list tile-list]
      (if (= 0 (count color-list))
        (concat '({:wildcard true} {:wildcard true}) tile-list)
        (recur (rest color-list) 
               number-list
               (concat tile-list
                       (generate-tiles-with-color
                         (first color-list) number-list))))

(defn have-same-attribute?
  "Returns true if all the tiles contained in the list have the same passed attribute, and false otherwise."
  [tile-list attribute]
  (if (= 1 (count tile-list))
    true
    (if (= (get (first tile-list) attribute)
           (get (first (rest tile-list)) attribute))
      (recur (rest tile-list) attribute)
      false)))

(defn are-of-same-color?
  "Returns true if all the tiles contained in the list have the same color, and false otherwise."
  [tile-list]
  (have-same-attribute? tile-list :color))

(defn have-same-number?
  "Returns true if all the tiles contained in the list have the same number,and false otherwise."
  [tile-list]
  (have-same-attribute? tile-list :number))

(defn tile-number-for
  "Returns the number associated with the tile, or nil if none."
  [tile]
  (get tile :number))

(defn are-sorted-tiles-sequential? "Checks whether or not the tiles are sequential, given that they are already sorted."
  [tile-list]
  (if (= 1 (count tile-list))
    true
    (if (not (= 1 
                (- (get (second tile-list) :number) 
                   (get (first tile-list) :number))))
      false
      (recur (rest tile-list)))))

(defn are-tiles-sequential?
  "Returns whether the numbered tiles in the list are sequential. Does not care whether or not they are of the same color."
  [tile-list]
  (are-sorted-tiles-sequential? (sort-by tile-number-for tile-list)))

(defn colors-are-unique?
  "Returns true if each of the tiles in the list has a unique color, false otherwise. If a list of colors is passed, this method returns true if each of the tiles in the list has a unique color not contained in the passed list, and false otherwise."
  ([tile-list]
              (colors-are-unique? tile-list '()))
  ([tile-list color-list]
              (let [num-tiles (count tile-list)
                    current-color (get (first tile-list) :color)]
                (if (= 0 num-tiles)
                  true
                  (if (< (count colors) num-tiles)
                    false
                    (if (and
                          (not (= 0 (count color-list)))
                          (< (- 1) (. (vec color-list)
                                  (indexOf current-color))))
                      false
                      (recur (rest tile-list)
                             (cons current-color color-list))))))))


(defn is-valid-combination? 
  "Returns true if the tiles contained in the list represent a valid combination of tiles, and false otherwise. Note that the tiles are assumed to be valid individual tiles." 
  [tile-list]
  (if (> 3 (count tile-list))
    false
    (if (are-of-same-color? tile-list)
      (are-tiles-sequential? tile-list)
      (and (have-same-number? tile-list) (colors-are-unique? tile-list)))))


(defn add-tile-to-list
  "Adds the given tile to the list, if possible, and returns the new list, or nil if no modifications were made. If no list is provided, a new list is created with the given tile as the sole element. This is always guaranteed to return a list."
  ([tile]
         (list tile))
  ([tile tile-list]
         (let [new-list (cons tile tile-list)]
           (if (is-valid-combination? new-list)
             new-list
             nil))))
