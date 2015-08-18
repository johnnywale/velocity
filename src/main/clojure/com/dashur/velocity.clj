(ns com.dashur.velocity
  (:gen-class :methods)
  (:import [org.apache.velocity.app VelocityEngine]
           [org.apache.velocity.runtime RuntimeConstants]
           [org.apache.velocity.runtime.resource.loader ClasspathResourceLoader]
           [org.apache.velocity VelocityContext Template]
           [java.io StringWriter FileWriter]
           [clojure.lang Keyword]))

(defn global-singleton [f]
  (let [instance (atom nil)
        make-instance (fn [_] (f))]
    (fn [] (or @instance (swap! instance make-instance)))))
(defrecord  CodeEnveriment [template-name file-name])
(defn- ^{:tag VelocityEngine}  new-velocity-engine []
  (let [^VelocityEngine v (VelocityEngine.)]
    (.setProperty v org.apache.velocity.runtime.RuntimeConstants/RESOURCE_LOADER "classpath") ;
    (.setProperty v "classpath.resource.loader.class" "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
    (.init v)
    v)
  )


(def ^{:dynamic true :private true :constant true} single-engine  (new-velocity-engine))

(defn ^{:tag VelocityContext}  context [kvs] ; private VelocityContent
  "Convert a vector of key/value pairs to a velocity context instance"
  (let [^VelocityContext ctx (VelocityContext.)]
    (loop [[k v & r] kvs]
      (.put ctx (name k) v)
      (when r
        (recur r)
        )
      )
    (.put ctx "key" Keyword)
    ctx
    )
  )



(defn write-with-template[  ^CodeEnveriment env,   kvs   ]
  (let  [^Template template (.getTemplate   single-engine    (:template-name env))]
    (let [^VelocityContext ctx ( context kvs) ^StringWriter sw (StringWriter.)]
      (.merge template ctx sw)
      (println (str sw))
      (let [file2Write (clojure.java.io/writer (:file-name env))]
        (.merge template ctx file2Write)
        (.flush file2Write)
        )
      )
    )
  )