(defmacro fn->
  [& args]
  `(fn [x#] (-> x# ~@args)))

(defn snowball
  []
  (agent (with-meta {} {
                        :snowball true
                        :sent-keys #{}})))

(defn base!
  [sb base-key base-val]
  (when-not ((comp base-key :sent-keys)
      (meta @sb))
    (send sb (fn->
      (assoc base-key base-val)
      (vary-meta update :sent-keys
        #(conj % base-key))))))

(defn step!
  [sb result needed-keys func]
  (when-not ((comp result :sent-keys)
      (meta @sb))
    (let [
          uses (map @sb needed-keys)]
      (when (every? identity uses)
        (send sb (fn->
          (assoc result (apply func uses))
          (vary-meta update :sent-keys
            #(conj % result))))))))

