(ns fx-clj.core.convert
  (:require
    [fx-clj.impl.bootstrap]
    [fx-clj.util :as util]
    [clojure.core.async :refer [put!]]
    [camel-snake-kebab.core :as csk])
  (:import (javafx.event EventHandler)
           (clojure.core.async.impl.channels ManyToManyChannel)
           (javafx.util Callback)
           (javafx.collections ObservableList FXCollections)
           (javafx.scene Node)
           (javax.swing JComponent)
           (javafx.embed.swing SwingNode)))

(defmulti convert-arg
  "Converts arguments sent to the pset! function and
  other functions which take similar arguments.

  Implementing methods take three parameters:
  the requested type, the value to be converted, and an optional options map.
  Methods dispatch on a vector of two Java Class instances:
  the requested type to be converted to and the type to convert from."
  (fn [requested-type value opts] [requested-type (type value)]))

(defmethod convert-arg :default [_ v _] v)

(defmethod convert-arg [EventHandler clojure.lang.IFn] [_ f _]
  (util/event-handler* f))

(defmethod convert-arg [EventHandler ManyToManyChannel] [_ ch _]
  (util/event-handler [e] (put! ch e)))

(defmethod convert-arg [Callback clojure.lang.IFn] [_ f _]
  (util/callback* f))

(defmethod convert-arg [Enum clojure.lang.Keyword] [enum kw _]
  (Enum/valueOf enum (csk/->SNAKE_CASE_STRING kw)))

(defmethod convert-arg [ObservableList clojure.lang.Sequential]
           [_ v {:keys [element-type] :or {element-type Object}}]
  (FXCollections/observableList (for [x v] (convert-arg element-type x nil))))

(defmethod convert-arg [Node JComponent] [nt jc _]
  (doto (SwingNode.)
    (.setContent jc)))