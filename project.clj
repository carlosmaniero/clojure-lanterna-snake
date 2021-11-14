(defproject clojure-lanterna-snake "0.1.0-SNAPSHOT"
  :description "A console based snake game wrote in Clojure"
  :url "https://github.com/carlosmaniero/clojure-lanterna-snake"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [prismatic/schema "1.2.0"]
                 [prismatic/schema-generators "0.1.3"]
                 [clojure-lanterna "0.9.7"]
                 [nubank/matcher-combinators "3.3.1"]]
  :main ^:skip-aot clojure-lanterna-snake.main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
