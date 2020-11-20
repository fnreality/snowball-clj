(defn staircase
  []
  (agent {}))

(defn base!
  [sc b_key b_val]
  (send sc
    #(assoc % b_key b_val)))

(defn step!
  [sc result needed-keys func]
  (let [
        uses (map @sc needed-keys)]
    (when (every? identity uses)
      (send sc
        #(assoc % result
          (apply func uses))))))

;;TEST
(def c (staircase))

(while
    ((complement :result) @c)
  (base! c :a 10)
  (base! c :b 42)
  (step! c :sum
    [:a :b] +)
  (step! c :result
    [:sum] dec))

(println @c)
(assert (= @c {
                :a 10 :b 42
                :sum 52
                :result 51
                }))
