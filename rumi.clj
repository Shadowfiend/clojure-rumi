(ns clojure-rumi
  (:import (com.trolltech.qt.gui QApplication QGraphicsView QGraphicsScene
                                 QGraphicsItem QBrush QPen QFont QFont$Weight
                                 QColor QTextOption)
           (com.trolltech.qt.core QCoreApplication QRectF Qt Qt$AlignmentFlag
                                  Qt$Alignment)))

(defn init []
  (QApplication/initialize (make-array String 0)))
(defn exec []
  (QApplication/exec))

(defmacro qt4 [& rest]
  `(do
     (try (init) (catch RuntimeException e# (println e#)))
     ~@rest
     (exec)))

(defn tile [tile-data]
  (proxy [QGraphicsItem] []
    (paint [painter option widget]
           (let [font (new QFont)]
             (.setWeight font (.. QFont$Weight Bold value))
             (.setFont painter font))
           (.setPen painter (new QPen (new QColor (tile-data :color))))

           (.drawRoundedRect painter 0 0 40 40 5 5)

           (let [align (new Qt$Alignment 
                            (.. Qt$AlignmentFlag AlignCenter value))
                 option (new QTextOption align)]
             (.drawText painter
                        (.boundingRect this)
                        (.toString (tile-data :number))
                        option)))
    (boundingRect [] (new QRectF 0 0 40 40))))

(defn start []
  (qt4
   (let [app (QCoreApplication/instance)
         rumi-canvas (new QGraphicsView (new QGraphicsScene))
         rumi-tile (tile { :number 5 :color "blue" })]

     (.. rumi-canvas scene (addItem rumi-tile))
     (.show rumi-canvas))))

