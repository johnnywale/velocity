(ns com.dashur.velocity
  (:gen-class)
  (:import [org.apache.velocity.app VelocityEngine]
           [org.apache.velocity.runtime RuntimeConstants]
           [org.apache.velocity.runtime.resource.loader ClasspathResourceLoader]
           [org.apache.velocity VelocityContext Template]
           [java.io StringWriter]))


(defn prepare-context []
  (let [ velocity (VelocityEngine.)]
  (.setProperty velocity org.apache.velocity.runtime.RuntimeConstants/RESOURCE_LOADER "classpath") ;
  (.setProperty velocity "classpath.resource.loader.class" "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
  (.init velocity)
       velocity))

