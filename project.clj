(defproject hermes "0.0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cheshire "5.5.0"]
                 [stencil "0.4.0"]
                 [com.draines/postal "1.11.3"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
