(defproject fx-clj "0.2.0-alpha1"
  :description "A Clojure library for JavaFX"
  :url "https://github.com/aaronc/fx-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.374"]
                 [garden "1.3.1"]
                 [potemkin "0.4.3"]
                 [org.tobereplaced/lettercase "1.0.0"]
                 [freactive.core "0.2.0-alpha1"]]
  :java-source-paths ["src"]
  :javac-options ["-Xlint:unchecked"]
  :jvm-opts ["-Dclojure.compiler.direct-linking=true"]
  :profiles
  {:dev
    {:plugins
      [[codox "0.8.10"]]}
   :uberjar {:aot :all}})
