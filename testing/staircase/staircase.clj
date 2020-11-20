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

