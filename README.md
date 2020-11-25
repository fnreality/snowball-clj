# snowball-clj
A simple concurrency model embracing immutability, for Clojure. 

(Make a snowball)
Create a snowball with some starting data:

```clojure
(snowball {
            :a 10
            :b 42})
```

(roll the snowball in the snow)
Try to accumulate more data, using the data it has:

```clojure
(try! sb [
           :sum <- + <- [:a :b]
           :result <- dec <- [:sum]])
```

(try to do something with the snowball)
Try to output some data that the snowball has:

```clojure
(try! sb [
           :done? <- (returning true println) <- [:result]])
```

NOTE: this last one requires `returning`:

```clojure
(defn returning
  [x proc]
  #(do (apply proc %&) x))
```
