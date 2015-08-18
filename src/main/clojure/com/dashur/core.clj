(ns com.dashur.core
  (:gen-class)
  (:use com.dashur.velocity))

(defprotocol TestMethod (getTest [this]))
(defrecord Person [fname lname address] TestMethod (getTest [this] "Implementation"))
(defrecord Address [street city state zip])
(defrecord dto-template [^Class dtoClazz ^Class updateModel])
(defn dto-name [^dto-template dto] (.getSimpleName (:dtoClazz dto)))
(defn contrcoller-name [^dto-template dto] (str (dto-name dto) "Controller"))
(defn service-name [^dto-template dto] (str (dto-name dto) "Service"))
(defn update-model [^dto-template dto] (if (nil? (:updateModel dto)) (dto-name dto) (.getSimpleName (:updateModel dto))))
(defn model-url [^dto-template dto] (.to com.google.common.base.CaseFormat/LOWER_CAMEL com.google.common.base.CaseFormat/LOWER_HYPHEN (dto-name dto)))
(defn dao-name [^dto-template dto] (str (dto-name dto) "Dao"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def stu (Person. "StuLLL" "Halloway"
             (Address. "200 N Mangum"
               "Durham"
               "NC"
               27701)))
  (def kvs [:name "name" :person stu])
  (def env (com.dashur.velocity.CodeEnveriment. "hello.vm" "generated/Hello.txt")) ;
  (write-with-template env kvs)
  (def mo (dto-template. com.dashur.code_template.User nil))
  (println (:dtoClazz mo))
  (println (nil? (:updateModel mo)))
  (println (dto-name mo))
  (println (update-model mo))
  (println (update-model (dto-template. com.dashur.code_template.User com.dashur.code_template.UserUpdateModel)))
  )
