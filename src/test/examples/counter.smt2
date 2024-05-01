(declare-proc Counter.init
    ((counter (out Int)))
    ((true
      (= counter 0))))

(declare-proc Counter.value
    ((counter (in Int))
     (result (out Int)))
    ((true
      (= result counter))))

(declare-proc Counter.increment
    ((counter (out Int)))
    ((true
      (= counter (+ 1 (old counter))))))
