(ns clojure-rumi
  (:import (com.trolltech.qt.gui QApplication QGraphicsView QGraphicsScene)
           (com.trolltech.qt.core QCoreApplication)))

(defn init []
  (QApplication/initialize (make-array String 0)))
(defn exec []
  (QApplication/exec))

(defmacro qt4 [& rest]
  `(do
     (try (init) (catch RuntimeException e# (println e#)))
     ~@rest
     (exec)))


(defn start []
  (qt4
   (let [app (QCoreApplication/instance)
         rumi-canvas (new QGraphicsView (new QGraphicsScene))]
     (.. rumi-canvas scene (addText "Hello world!"))
     (.show rumi-canvas))))

