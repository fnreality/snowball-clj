(defn staircase
  []
  (agent {}))

(defn base!
  [sc base-key base-val]
  (send sc
    #(assoc % base-key basw-val)))

(defn step!
  [sc result needed-keys func]
  (let [
        uses (map @sc needed-keys)]
    (when (every? identity uses)
      (send sc
        #(assoc % result
          (apply func uses))))))

