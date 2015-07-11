(ns hermes.core
  (:require [cheshire.core :refer [parse-stream]]
            [postal.core :refer [send-message]]
            [clojure.string :refer [split]]))

(def ^{:dynamic true
       :doc "Settings to use for email delivery (basically SMTP config)"}
  *delivery-config*)

(def ^{:dynamic true
       :doc "Sender's email address (generated by SMTP config file)"}
  *sender-address*)

(defn delivery-config!
  [m]
  (alter-var-root (var *delivery-config*) (constantly m)))

(defn sender-address!
  [m]
  (alter-var-root (var *sender-address*) (constantly m)))

(defmacro with-del-config
    [m & body]
      `(binding [*delivery-config* ~m]
              ~@body))

(defmacro with-sender
    [m & body]
      `(binding [*sender-address* ~m]
              ~@body))

(defn parse-del-config
  [json-file-path]
  (assoc (parse-stream (clojure.java.io/reader json-file-path) true)
         :tls :yes))

(defn get-addr-from-config
  [{:keys [host user]}]
  (let [hostname (get (split host #"smtp.") 1)]
    (str user "@" hostname)))

(defn configure-email!
  [config-file-path]
  (do (delivery-config! (parse-del-config config-file-path))
      (sender-address! (get-addr-from-config *delivery-config*))))

(defn build-email
  [from to sub text]
  {:from from :to to :subject sub :body [{:type "text/plain"
                                          :content text}]})
(defn add-attachment
  [email-map att-file-path]
  (update-in email-map [:body] conj {:type :attachment
                                     :content (java.io.File. att-file-path)}))

(defn send-email
  [to subject content & attachments]
  (send-message *delivery-config* (reduce add-attachment
                                          (build-email *sender-address*
                                                       to subject content)
                                          attachments)))