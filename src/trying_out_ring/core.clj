(ns trying-out-ring.core
  (:require
    [ring.middleware.reload :refer [wrap-reload]]))

(defn req-info [request]
  (let [_ (println "======REQUEST======")
        _ (doseq [[k v] request]
            (println k v))]))

(def page-hello-world
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World"})

(defn page-greet [request]
  (let [q_str (:query-string request)
        name  (if q_str
                (last
                  (.split (:query-string request) "=")))]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body    (if name
                (str "Greetings " name "!")
                "Greetings stranger!")}))

(def home-page
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Jou"})

(defn handler [request]
  (let [uri (:uri request)
        _   (req-info request)]
    (cond (= uri "/") home-page
          (= uri "/hello-world") page-hello-world
          (= uri "/greet-person") (page-greet request)
          :else {:status  404
                 :headers {"Content-Type" "text/html"}
                 :body    "Whoops, gotta present 404!"})))

(def reloading-handler (wrap-reload #'handler))