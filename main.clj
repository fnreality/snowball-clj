(defmacro fn->
  [& args]
  `(fn [x#] (-> x# ~@args)))

(defmacro when-not->
  [basis pred & args]
  `(when-not (~pred ~basis) (-> ~basis ~@args)))

(defn snowball
  [start]
  (agent (with-meta start {
                            :sent-keys #{}})))

(defn key-sent?
  [target-key]
  (comp target-key :sent-keys meta deref))

(defn step!
  [sb result needed-keys func]
  (let [
        uses (map @sb needed-keys)]
    (when (every? identity uses)
      (when-not-> sb (key-sent? result)
        (send (fn->
          (vary-meta update :sent-keys
            #(conj % result))
          (assoc result (apply func uses))))))))

(defmacro try!
  [sb paths*]
  `(do ~@(map (fn [[x _ f _ needed]] `(step! ~sb ~x ~needed ~f))
    (partition 5 paths*))))

(defn returning
  [x proc]
  #(do (apply proc %&) x))

