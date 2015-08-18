(ns  com.dashur.core
  (:gen-class)
  (:use com.dashur.velocity)
   (:import [org.apache.velocity.app VelocityEngine]
           [org.apache.velocity.runtime RuntimeConstants]
           [org.apache.velocity.runtime.resource.loader ClasspathResourceLoader]
           [org.apache.velocity VelocityContext Template]
           [java.io StringWriter]))


(defprotocol TestMethod (getTest [this] ))
(defrecord Person  [fname lname address]  TestMethod  (  getTest [this] "Implementation"   ) )
(defrecord Address [street city state zip])
(defrecord dto-template [  ^Class dtoClazz ^Class updateModel  ] )


(defn dto-name [^dto-template  dto] (.getSimpleName (:dtoClazz dto)))
(defn contrcoller-name [ ^dto-template dto ](str (dto-name dto) "Controller"))
(defn service-name [ ^dto-template dto ](str  (dto-name dto) "Service"))
(defn update-model [ ^dto-template dto ](if (nil?(:updateModel dto)) (dto-name dto ) (.getSimpleName(:updateModel dto))))
(def model-url [^dto-template dto]   .to com.google.common.base.CaseFormat.LOWER_CAMEL    )

(defn- ^{:tag VelocityContext} ->context [kvs] ; private VelocityContent
  "Convert a vector of key/value pairs to a velocity context instance"
  (let [^VelocityContext ctx (VelocityContext.)]
    (loop [[k v & r] kvs]
      (.put ctx (name k) v)
      (when r
        (recur r)))
    ctx))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def v (prepare-context))
  (def ^Template template (.getTemplate v "hello.vm"))
  (def kvs [:name "name" :key "abcdef"  ])
  (let [^VelocityContext ctx (->context kvs) ^StringWriter sw (StringWriter.)]
  (.merge template ctx sw)
    (println (.toString sw))
  )
  (def stu (Person. "Stu" "Halloway"
             (Address. "200 N Mangum"
               "Durham"
               "NC"
               27701)))
;  (println  contrcollerName          )
;  (println  (:lname stu))
;  (println  (getTest stu))
  (def mo (dto-template.   com.dashur.code_template.User nil ))
  (println(:dtoClazz  mo))
  (println (nil?(:updateModel mo)))
  (println (dto-name mo ) )
  (println   (update-model  mo))
  (println  (update-model  (dto-template.   com.dashur.code_template.User com.dashur.code_template.UserUpdateModel )))










;;  (println  (update-model  (dto-template.   com.dashur.code_template.User nil )))
  )
