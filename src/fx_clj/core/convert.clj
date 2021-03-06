(ns ^:no-doc fx-clj.core.convert
  (:require
    [fx-clj.core.extensibility :refer [convert-arg]]
    [fx-clj.util :as util]
    [clojure.core.async :refer [put!]]
    ;;[camel-snake-kebab.core :as csk]
    [org.tobereplaced.lettercase :as lettercase])
  (:import (javafx.event EventHandler)
           (clojure.core.async.impl.channels ManyToManyChannel)
           (javafx.util Callback)
           (javafx.collections ObservableList FXCollections)
           (javafx.scene Node)
           (javax.swing JComponent)
           (javafx.embed.swing SwingNode)
           (javafx.beans.property Property)
           (fx_clj.binding ReactiveAtomObservable)
           [freactive IReactiveAtom]))

(defmethod convert-arg :default [_ v _] v)

(defmethod convert-arg [EventHandler clojure.lang.IFn] [_ f _]
  (util/event-handler* f))

(defmethod convert-arg [EventHandler ManyToManyChannel] [_ ch _]
  (util/event-handler [e] (put! ch e)))

(defmethod convert-arg [Callback clojure.lang.IFn] [_ f _]
  (util/callback* f))

(defmethod convert-arg [Enum clojure.lang.Keyword] [enum kw _]
  (Enum/valueOf enum (lettercase/upper-underscore-name kw)))

(defmethod convert-arg [ObservableList clojure.lang.Sequential]
           [_ v {:keys [element-type] :or {element-type Object}}]
  (FXCollections/observableList (for [x v] (convert-arg element-type x nil))))

(prefer-method convert-arg
               [ObservableList clojure.lang.Sequential]
               [Object clojure.lang.PersistentVector])

(defmethod convert-arg [Node JComponent] [nt jc _]
  (doto (SwingNode.)
    (.setContent jc)))

(defmethod convert-arg [Property IReactiveAtom] [_ a _]
  (ReactiveAtomObservable. a))
