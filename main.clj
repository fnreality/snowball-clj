defmacro when-not->
  [basis pred & args]
  `(when-not (~pred ~basis) (-> ~basis ~@args)))

(defn snowball
  [start]
  (agent start :meta {
                       :snowball true
                       :sent-keys #{}}))

(defn key-sent?
  [target-key]
  (comp target-key :sent-keys meta))

(defn step!
  [sb result needed-keys func]
  (let [
        uses (map @sb needed-keys)]
    (when (every? identity uses)
      (when-not-> sb (key-sent? result)
        (send assoc result (apply func uses))
        (alter-meta! update :sent-keys
          #(conj % result))))))

(defmacro try!
  [sb paths*]
  `(do ~@(map (fn [[x _ f _ needed]]
    `(step! ~sb ~x ~needed ~f))
    (partition 5 paths*))))

(defn returning
  [x proc]
  #(do (apply proc %&) x))

