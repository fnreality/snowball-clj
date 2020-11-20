(defn staircase
  []
  (agent {}))

(defn base!
  [sc base-key base-val]
  (send sc
    #(assoc % base-key base-val)))

(defn step!
  [sc result needed-keys func]
  (let [
        uses (map @sc needed-keys)]
    (when (every? identity uses)
      (send sc
        #(assoc % result
          (apply func uses))))))

;;TEST

(def sc (staircase))

(while
    ((complement :result) @sc)
  (base! sc :a 10)
  (base! sc :b 42)
  (step! sc :sum
    [:a :b] +)
  (step! sc :result
    [:sum] dec))

(println @sc)

(assert (= @sc {
                :a 10 :b 42
                :sum 52
                :result 51
                }))

(shutdown-agents)
