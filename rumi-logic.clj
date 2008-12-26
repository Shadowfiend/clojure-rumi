; Rumikube contains four colors, denoted herein by the symbols :blue, :red, :orange, and :black. In addition, it contains twelve numbers, numbered from one to twelve.

(def colors '(:blue :red :orange :black))
(def numbers (range 0 14))


; The methods to generate the tiles:
;
(defn generate-tiles-with-color [color number-list trace-list]
  (if (= 0 (count number-list))
    (cons {:Wildcard :true} (cons {:Wildcard :true} trace-list))
    (recur color 
           (rest number-list) 
           (cons {:Color color :Number (first number-list) :Wildcard :false}
                 trace-list))))


(defn generate-tiles [color-list number-list trace-list]
  (if (= 0 (count color-list))
    trace-list
    (recur (rest color-list) 
           number-list 
           (generate-tiles-with-color (first color-list) 
                                      number-list trace-list))))

(defn #^{:doc "are-of-same-color? [tile-list]
             Returns true if all tiles contained in the list have the same :Color attribute."}
  are-of-same-color [tile-list]
  (if (= 1 (count tile-list))
    true
    (if (= (get (first tile-list) :Color)
           (get (first (rest tile-list)) :Color))
      (recur (rest tile-list))
      false)))

(defn #^{:doc "have-same-attribute? [tile-list attribute]
              Returns true if all the tiles contained in the list have the same passed attribute, and false otherwise."}
  have-same-attribute? [attribute tile-list]
  (if (= 1 (count tile-list))
    true
    (if (= (get (first tile-list) attribute)
           (get (first (rest tile-list)) attribute))
      (recur attribute (rest tile-list))
      false)))

(defn #^{:doc "tile-number-for [tile]
              Returns the number associated with the tile, or nil if none."}
  tile-number-for [tile]
  (get tile :Number))

(defn #^{:doc "are-sorted-tiles-sequential [tile-list]
              Checks whether or not the tiles are sequential, given that they are already sorted."}
  are-sorted-tiles-sequential [tile-list]
  (if (not (= 1 
              (- (get (first tile-list) :Number) 
                 (get (first (rest (tile-list))) :Number))))
    false
    (recur (rest (tile-list)))))

(defn #^{:doc "are-sequential? [tile-list]
              Returns whether the numbered tiles in the list are sequential. Does not care whether or not they are of the same color."}
  are-tiles-sequential? [tile-list]
  (are-sorted-tiles-sequential (sort-by tile-number-for tile-list)))


(defn #^{:doc "is-valid-combination? [tile-list]
          Returns true if the tiles contained in the list represent a valid combination of tiles, and false otherwise."} 
        is-valid-combination? [tile-list]
        (if (have-same-attribute? :Color tile-list)
          (are-tiles-sequential? tile-list)
          (have-same-attribute? :Number tile-list)))
