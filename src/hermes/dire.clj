(ns hermes.dire
  (:require [dire.core :refer [with-handler!]]
            [hermes.core :refer [send-email send-template]]
            [clojure.string :refer [join]]))

(def ^{:dynamic true}
  *template-name* "dire")

(def ^{:dynamic true}
  *receiver*)

(defn template-name!
    [m]
    (alter-var-root (var *template-name*) (constantly m)))

(defn receiver!
    [m]
    (alter-var-root (var *receiver*) (constantly m)))

(defmacro with-template-name
      [m & body]
        `(binding [*template-name* ~m] ~@body))

(defmacro with-receiver
      [m & body]
        `(binding [*sender-address* ~m] ~@body))

(defmacro hermes-error-handler
  [function exception handle]
 `(with-handler! ~function ~exception
    (fn [~'e & ~'args]
      (do
        (send-email *receiver*
                    (str ~function " passed " ~'args " raised " ~exception)
                    (join "\n" (map str (.getStackTrace ~'e))))
        ~handle))))
