(defn snowball
  []
  (agent {}))

(defn base!
  [sb base-key base-val]
  (send sb
    #(assoc % base-key base-val)))

(defn step!
  [sb result needed-keys func]
  (when-not (@sb result)
    (let [
          uses (map @sb needed-keys)]
      (when (every? identity uses)
        (send sb
          #(assoc % result
            (apply func uses)))))))

