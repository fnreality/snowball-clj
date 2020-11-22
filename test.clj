(defmacro fn->
  [& args]
  `(fn [x#] (-> x# ~@args)))

(defmacro when-not->
  [basis pred & args]
  `(when-not (~pred ~basis) (-> ~basis ~@args)))

(defn snowball
  [start]
  (agent (with-meta start {
                            :snowball true
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
          (assoc result (apply func uses))
          (vary-meta update :sent-keys
            #(conj % result))))))))

;;TEST

(def sb (snowball {
                    :a 10
                    :b 42}))

(while
    ((complement :result) @sb)
  (step! sb :sum
    [:a :b] +)
  (step! sb :result
    [:sum] dec))

(println @sb)

(assert (= @sb {
                 :a 10
                 :b 42
                 :sum 52
                 :result 51}))

(shutdown-agents)
