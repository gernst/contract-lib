(declare-sort Char 0)

(declare-datatypes
  ((GapBuffer 0))
   (((GapBuffer (GapBuffer.position Int)
                (GapBuffer.content (Seq Char))))))

(declare-contract GapBuffer.init
    ((this (out GapBuffer)))
    ((true
      (and (= (GapBuffer.position this) 0)
           (= (GapBuffer.content  this) seq.empty)))))

(declare-contract GapBuffer.left
    ((this (inout GapBuffer)))
    (((< 0 (GapBuffer.position this))
      (and (= (GapBuffer.position this)
              (- (old (GapBuffer.position this)) 1))
           (= (GapBuffer.content  this)
              (old (GapBuffer.content this)))))))

(declare-contract GapBuffer.right
    ((this (inout GapBuffer)))
    (((< (GapBuffer.position this)
         (seq.len (GapBuffer.content this)))
      (and (= (GapBuffer.position this)
              (+ (old (GapBuffer.position this)) 1))
           (= (GapBuffer.content  this)
              (old (GapBuffer.content this)))))))

(declare-contract GapBuffer.insert
    ((this (inout GapBuffer)) (char (in Char)))
    (((and (<= 0 (GapBuffer.position this))
           (<= (GapBuffer.position this)
              (seq.len (GapBuffer.content this))))
      (and (= (GapBuffer.position this)
              (+ (old (GapBuffer.position this)) 1))
           (= (GapBuffer.content  this)
              (seq.++    (old (seq.extract (GapBuffer.content this) 0 (GapBuffer.position this)))
                 (seq.++ (seq.unit char)
                         (old (seq.extract (GapBuffer.content this) (GapBuffer.position this) (seq.len (GapBuffer.content this)))))))))))

(declare-contract GapBuffer.delete
    ((this (inout GapBuffer)))
    (((< 0 (GapBuffer.position this))
      (and (= (GapBuffer.position this)
              (- (old (GapBuffer.position this)) 1))
           (= (GapBuffer.content  this)
              (seq.++    (old (seq.extract (GapBuffer.content this) 0 (- (GapBuffer.position this) 1)))
                         (old (seq.extract (GapBuffer.content this) (GapBuffer.position this) (seq.len (GapBuffer.content this))))))))))

(declare-contract GapBuffer.view
    ((this (in GapBuffer)) (content (out (Seq Char))))
    ((true
      (= content
         (GapBuffer.content  this)))))
