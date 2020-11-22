(defmacro fn->
  [& args]
  `(fn [through-fn->]
    (-> through-fn-> ~@args)))

(defn snowball
  []
  (agent (with-meta {} {
                        :snowball true
                        :sent-keys #{}})))

(defn base!
  [sb base-key base-val]
  (send sb (fn->
    (assoc base-key base-val)
    (vary-meta update-in :sent-keys
      #(conj % base-key)))))

(defn step!
  [sb result needed-keys func]
  (let [
        uses (map @sb needed-keys)]
    (when (and
        (every? identity ((juxt :snowball
            (comp not result :sent-keys)
            (constantly uses))
          (meta @sb))))
      (send sb (fn->
        (assoc result (apply func uses))
        (vary-meta update-in :sent-keys
          #(conj % result)))))))

;;TEST

(def sb (snowball))

(while
    ((complement :result) @sb)
  (base! sb :a 10)
  (base! sb :b 42)
  (step! sb :sum
    [:a :b] +)
  (step! sb :result
    [:sum] dec))

(println @sb)

(assert (= @sb {
                :a 10 :b 42
                :sum 52
                :result 51}))

(shutdown-agents)
