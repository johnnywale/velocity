(ns com.dashur.core
  (:gen-class)
  (:use com.dashur.velocity))

(defrecord DtoTemplate [^Class dtoClazz ^Class updateModel])
(defn dto-name [^DtoTemplate dto] (.getSimpleName (:dtoClazz dto)))
(defn contrcoller-name [^DtoTemplate dto] (str (dto-name dto) "Controller"))
(defn service-name [^DtoTemplate dto] (str (dto-name dto) "Service"))
(defn update-model [^DtoTemplate dto] (if (nil? (:updateModel dto)) (dto-name dto) (.getSimpleName (:updateModel dto))))
(defn model-url [^DtoTemplate dto] (.to com.google.common.base.CaseFormat/LOWER_CAMEL com.google.common.base.CaseFormat/LOWER_HYPHEN (dto-name dto)))
(defn dao-name [^DtoTemplate dto] (str (dto-name dto) "Dao"))
(defn entity-class [^DtoTemplate dto] (str (.getCanonicalName (:dtoClazz dto))))
(defn update-model-class [^DtoTemplate dto] (if (nil? (:updateModel dto)) (.getCanonicalName (:dtoClazz dto)) (.getCanonicalName (:updateModel dto))))
(defn sequence-type [^DtoTemplate dto]   (clojure.string/upper-case (dto-name dto)))
(defn name-package  [^DtoTemplate dto]   (let [n  (dto-name dto)] (str  (.toLowerCase  (subs n 0 1)) (subs n 1))))
(defrecord Generate [ name url contrcollerName updateModel  entityClass updateModelClass sequenceType packageName  serviceName] )
(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  (def mo  (DtoTemplate. com.dashur.data.dto.User com.dashur.service.user.model.UserUpdateModel))

  (def gen (Generate. (dto-name mo)  (model-url mo) (contrcoller-name mo) (update-model mo)  (entity-class mo) (update-model-class mo) (sequence-type mo) (name-package mo)  (service-name mo  )   ))

  (def kvs [:name "name" :entity (com.dashur.clojure.Converter/convertRecord gen )])

  (write-with-template (com.dashur.velocity.CodeEnveriment. "controller.vm" "generated/Controller.java") kvs)
  (write-with-template (com.dashur.velocity.CodeEnveriment. "service.vm" "generated/Service.java") kvs)


  )
